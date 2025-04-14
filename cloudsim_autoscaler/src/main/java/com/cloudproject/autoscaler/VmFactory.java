package com.cloudproject.autoscaler;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;

public class VmFactory {
    private static int vmIdCounter = 0;

    public static List<Vm> createInitialVms() {
        List<Vm> vms = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            vms.add(createVm());
        }
        return vms;
    }

    public static Vm createVm() {
        Vm vm = new VmSimple(2000, 4); // 2000 MIPS, 4 cores
        vm.setRam(12288)               // 12 GB RAM
          .setBw(25000)                // 25 Gbps
          .setSize(200000);            // 200 GB disk
        vm.setCloudletScheduler(new CloudletSchedulerSpaceShared()); // ✅ change
        vm.setId(vmIdCounter++);
        return vm;
    }
}
