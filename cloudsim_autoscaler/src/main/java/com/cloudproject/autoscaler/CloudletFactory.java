package com.cloudproject.autoscaler;



import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;

public class CloudletFactory {
    public static List<Cloudlet> createInitialCloudlets() {
        List<Cloudlet> cloudlets = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            cloudlets.add(createCloudlet(i));
        }
        return cloudlets;
    }

    public static Cloudlet createCloudlet(int id) {
        Cloudlet cloudlet = new CloudletSimple(10000, 2);
        cloudlet.setFileSize(300).setOutputSize(300);
        cloudlet.setUtilizationModel(new UtilizationModelFull());
        cloudlet.setId(id);
        return cloudlet;
    }
}

