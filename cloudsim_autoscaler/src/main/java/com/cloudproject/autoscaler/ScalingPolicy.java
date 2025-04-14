package com.cloudproject.autoscaler;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.vms.Vm;

public class ScalingPolicy {
    private static final int CHECK_INTERVAL = 5;
    private static final int MAX_VMS = 50;
    private static final int MAX_CLOUDLETS = 500;
    private static int cloudletCounter = 10;

    public static void monitorAndScale(CloudSim simulation, DatacenterBrokerSimple broker, Datacenter datacenter) {
        simulation.addOnClockTickListener(eventInfo -> {
            double clock = eventInfo.getTime();

            if ((int) clock % CHECK_INTERVAL == 0) {
                List<Cloudlet> waiting = broker.getCloudletWaitingList();
                List<Vm> vms = broker.getVmCreatedList();
                int currentVms = vms.size();

                // --- Auto-Scaling Logic ---
                if (waiting.size() > 1 && currentVms < MAX_VMS) {
                    Vm newVm = VmFactory.createVm();
                    broker.submitVmList(List.of(newVm));
                    System.out.printf("[Time %.0f] Auto-scaled: New VM %d created (waiting cloudlets: %d).\n",
                            clock, newVm.getId(), waiting.size());
                    vms = broker.getVmCreatedList(); // refresh list after new VM
                }

                // --- Debug: Print VM free RAM ---
                for (Vm vm : vms) {
                    System.out.printf("[Time %.0f] VM %d → Running cloudlets: %d | Available RAM: %.2f MB\n",
                            clock,
                            vm.getId(),
                            vm.getCloudletScheduler().getCloudletExecList().size(),
                            (double) vm.getRam().getAvailableResource());

                }

                // --- Controlled Workload Injection ---
                if (cloudletCounter < MAX_CLOUDLETS && !vms.isEmpty() && waiting.size() < 15) {
                    List<Cloudlet> extraLoad = new ArrayList<>();

                    for (int i = 0; i < 2; i++) {
                        if (cloudletCounter >= MAX_CLOUDLETS)
                            break;

                        Cloudlet cl = CloudletFactory.createCloudlet(cloudletCounter++);
                        Vm targetVm = vms.get(i % vms.size()); // round-robin distribution
                        broker.bindCloudletToVm(cl, targetVm);
                        extraLoad.add(cl);
                    }

                    broker.submitCloudletList(extraLoad);
                    System.out.printf("[Time %.0f] Injected %d cloudlets (total: %d).\n",
                            clock, extraLoad.size(), cloudletCounter);
                }
            }
        });
    }
}
