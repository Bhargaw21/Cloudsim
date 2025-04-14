package com.cloudproject.autoscaler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.vms.Vm;

public class AutoScalerSimulation {

    public static void main(String[] args) {
        CloudSim simulation = new CloudSim();

        Datacenter datacenter = DatacenterFactory.createDatacenter(simulation);
        DatacenterBrokerSimple broker = new DatacenterBrokerSimple(simulation);

        List<Vm> vmList = VmFactory.createInitialVms();
        broker.submitVmList(vmList);

        List<Cloudlet> cloudlets = CloudletFactory.createInitialCloudlets();
        broker.submitCloudletList(cloudlets);

        // Start autoscaling policy
        ScalingPolicy.monitorAndScale(simulation, broker, datacenter);

        System.out.println("Starting simulation...");
        simulation.start();
        System.out.println("Simulation finished.");

        // After simulation
        List<Cloudlet> finished = broker.getCloudletFinishedList();

        // Print Summary
        printSummary(finished);

        // Save to CSV
        saveResultsToCSV(finished, "simulation_results.csv");
    }

    private static void printSummary(List<Cloudlet> cloudlets) {
        System.out.println("\n========= Cloudlet Execution Summary =========");
        System.out.printf("%-10s%-10s%-10s%-15s%-15s%-15s\n", 
                          "ID", "STATUS", "VM ID", "Start Time", "Finish Time", "Execution Time");

        for (Cloudlet c : cloudlets) {
            System.out.printf("%-10d%-10s%-10d%-15.2f%-15.2f%-15.2f\n",
                    c.getId(),
                    c.getStatus(),
                    c.getVm().getId(),
                    c.getExecStartTime(),
                    c.getFinishTime(),
                    c.getActualCpuTime());
        }
    }

    private static void saveResultsToCSV(List<Cloudlet> cloudlets, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.append("Cloudlet ID,VM ID,Start Time,Finish Time,Execution Time,Status\n");
            for (Cloudlet c : cloudlets) {
                writer.append(String.format("%d,%d,%.2f,%.2f,%.2f,%s\n",
                        c.getId(),
                        c.getVm().getId(),
                        c.getExecStartTime(),
                        c.getFinishTime(),
                        c.getActualCpuTime(),
                        c.getStatus()));
            }
            System.out.println("\n📁 Results exported to: " + filename);
        } catch (IOException e) {
            System.err.println("⚠️ Error writing CSV file: " + e.getMessage());
        }
    }
}
