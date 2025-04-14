package com.cloudproject.autoscaler;



import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;

public class DatacenterFactory {
    public static Datacenter createDatacenter(CloudSim simulation) {
        List<Host> hostList = new ArrayList<>();
    
        for (int i = 0; i < 5; i++) { // create 3 high-capacity hosts
            List<Pe> peList = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                peList.add(new PeSimple(2000)); // 8 cores per host
            }
    
            Host host = new HostSimple(
                    65536,    // 64 GB RAM
                    100000,   // 100 Gbps bandwidth
                    1000000,  // 1 TB storage
                    peList
            );
            host.setVmScheduler(new VmSchedulerTimeShared());
            hostList.add(host);
        }
    
        return new DatacenterSimple(simulation, hostList);
    }
    
}
