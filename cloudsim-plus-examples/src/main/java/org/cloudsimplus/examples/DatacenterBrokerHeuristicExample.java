package org.cloudsimplus.examples;

import org.cloudbus.cloudsim.util.CloudletsTableBuilderHelper;
import org.cloudbus.cloudsim.util.TextTableBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.schedulers.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.CloudletSimple;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterSimple;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.DatacenterCharacteristicsSimple;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.HostSimple;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.resources.FileStorage;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.schedulers.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.VmSimple;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerHeuristic;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.distributions.UniformDistr;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.resources.Bandwidth;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.Ram;
import org.cloudsimplus.heuristics.CloudletToVmMappingHeuristic;
import org.cloudsimplus.heuristics.CloudletToVmMappingSimulatedAnnealing;
import org.cloudsimplus.heuristics.CloudletToVmMappingSolution;
import org.cloudsimplus.heuristics.HeuristicSolution;

/**
 * <p>An example that uses a
 * <a href="http://en.wikipedia.org/wiki/Simulated_annealing">Simulated Annealing</a>
 * heuristic to find a suboptimal mapping between Cloudlets and Vm's submitted to a
 * DatacenterBroker. The number of {@link Pe}s of Vm's and Cloudlets are defined
 * randomly.
 *
 * The {@link DatacenterBrokerHeuristic} is used
 * with the {@link CloudletToVmMappingSimulatedAnnealing} class
 * in order to find an acceptable solution with a high
 * {@link HeuristicSolution#getFitness() fitness value}.</p>
 *
 * <p>Different {@link CloudletToVmMappingHeuristic} implementations can be used
 * with the {@link DatacenterBrokerHeuristic} class.</p>
 *
 * @author Manoel Campos da Silva Filho
 */
public class DatacenterBrokerHeuristicExample {
    /**
     * Virtual Machine Monitor name.
     */
    private static final String VMM = "Xen";

    private List<Cloudlet> cloudletList;
    private List<Vm> vmList;
    private CloudletToVmMappingSimulatedAnnealing heuristic;

    /**
     * Number of cloudlets created so far.
     */
    private int numberOfCreatedCloudlets = 0;
    /**
     * Number of VMs created so far.
     */
    private int numberOfCreatedVms = 0;
    /**
     * Number of hosts created so far.
     */
    private int numberOfCreatedHosts = 0;

    private static final int HOSTS_TO_CREATE = 100;
    private static final int VMS_TO_CREATE = 50;
    private static final int CLOUDLETS_TO_CREATE = 100;

    /**
     * Starts the simulation.
     * @param args
     */
    public static void main(String[] args) {
        new DatacenterBrokerHeuristicExample();
    }

    /**
     * Default constructor where the simulation is built.
     */
    public DatacenterBrokerHeuristicExample() {
        Log.printFormattedLine("Starting %s ...", getClass().getSimpleName());
        try {
            this.vmList = new ArrayList<>();
            this.cloudletList = new ArrayList<>();
            int numberOfCloudUsers = 1;
            boolean traceEvents = false;

            CloudSim.init(numberOfCloudUsers, Calendar.getInstance(), traceEvents);

            Datacenter datacenter0 = createDatacenter("Datacenter0");

            heuristic =
                    new CloudletToVmMappingSimulatedAnnealing(1, new UniformDistr(0, 1));
            heuristic.setColdTemperature(0.0001);
            heuristic.setCoolingRate(0.003);
            heuristic.setNumberOfNeighborhoodSearchesByIteration(50);

            DatacenterBrokerHeuristic broker0 = new DatacenterBrokerHeuristic("Broker0");
            broker0.setHeuristic(heuristic);

            vmList = new ArrayList<>(VMS_TO_CREATE);
            int pesNumber;
            for(int i = 0; i < VMS_TO_CREATE; i++){
                pesNumber = getRandomNumberOfPes(4);
                vmList.add(createVm(broker0, pesNumber));
            }
            broker0.submitVmList(vmList);

            for(int i = 0; i < CLOUDLETS_TO_CREATE; i++){
                pesNumber = getRandomNumberOfPes(4);
                cloudletList.add(createCloudlet(broker0, pesNumber));
            }
            broker0.submitCloudletList(cloudletList);

            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            List<Cloudlet> finishedCloudlets = broker0.getCloudletsFinishedList();
            CloudletsTableBuilderHelper.print(new TextTableBuilder(), finishedCloudlets);

            double roudRobinMappingCost = computeRoudRobinMappingCost();
            printSolution(
                    "Heuristic solution for mapping cloudlets to Vm's         ",
                    heuristic.getBestSolutionSoFar(), false);
            System.out.printf(
                "The heuristic solution cost represents %.2f%% of the round robin mapping cost used by the DatacenterBrokerSimple\n",
                heuristic.getBestSolutionSoFar().getCost()*100.0/roudRobinMappingCost);
	        System.out.printf("The solution finding spend %.2f seconds to finish\n", broker0.getHeuristic().getSolveTime());

            Log.printFormattedLine("\n%s finished!", getClass().getSimpleName());
        } catch (Exception e) {
            Log.printFormattedLine("Unexpected errors happened: %s", e.getMessage());
        }
    }

    /**
     * Randomly gets a number of PEs (CPU cores).
     *
     * @param maxPesNumber the maximum value to get a random number of PEs
     * @return the randomly generated PEs number
     */
    private int getRandomNumberOfPes(int maxPesNumber) {
        return heuristic.getRandomValue(maxPesNumber)+1;
    }

    private DatacenterSimple createDatacenter(String name) {
        List<Host> hostList = new ArrayList<>();
        for(int i = 0; i < HOSTS_TO_CREATE; i++) {
            hostList.add(createHost());
        }

        //Defines the characteristics of the data center
        String arch = "x86"; // system architecture of datacenter hosts
        String os = "Linux"; // operating system of datacenter hosts
        double time_zone = 10.0; // time zone where the datacenter is located
        double cost = 3.0; // the cost of using processing in this datacenter
        double costPerMem = 0.05; // the cost of using memory in this datacenter
        double costPerStorage = 0.001; // the cost of using storage in this datacenter
        double costPerBw = 0.0; // the cost of using bw in this datacenter
        LinkedList<FileStorage> storageList = new LinkedList<>(); // we are not adding SAN devices

        DatacenterCharacteristics characteristics = new DatacenterCharacteristicsSimple(
                arch, os, VMM, hostList, time_zone, cost, costPerMem,
                costPerStorage, costPerBw);

        return new DatacenterSimple(name, characteristics,
                new VmAllocationPolicySimple(hostList), storageList, 0);
    }

    private Host createHost() {
        int  mips = 1000; // capacity of each CPU core (in Million Instructions per Second)
        int  ram = 2048; // host memory (MB)
        long storage = 1000000; // host storage
        long bw = 10000;

        List<Pe> cpuCoresList = new ArrayList<>();
        /*Creates the Host's CPU cores and defines the provisioner
        used to allocate each core for requesting VMs.*/
        for(int i = 0; i < 8; i++)
            cpuCoresList.add(new PeSimple(i, new PeProvisionerSimple(mips)));

        return new HostSimple(numberOfCreatedHosts++,
                new ResourceProvisionerSimple<>(new Ram(ram)),
                new ResourceProvisionerSimple<>(new Bandwidth(bw)),
                storage, cpuCoresList,
                new VmSchedulerTimeShared(cpuCoresList));
    }

    private Vm createVm(DatacenterBroker broker, int pesNumber) {
        double mips = 1000;
        long   storage = 10000; // vm image size (MB)
        int    ram = 512; // vm memory (MB)
        long   bw = 1000; // vm bandwidth

        return new VmSimple(numberOfCreatedVms++,
                broker.getId(), mips, pesNumber, ram, bw, storage,
                VMM, new CloudletSchedulerTimeShared());
    }

    private Cloudlet createCloudlet(DatacenterBroker broker, int numberOfPes) {
        long length = 400000; //in Million Structions (MI)
        long fileSize = 300; //Size (in bytes) before execution
        long outputSize = 300; //Size (in bytes) after execution

        //Defines how CPU, RAM and Bandwidth resources are used
        //Sets the same utilization model for all these resources.
        UtilizationModel utilization = new UtilizationModelFull();

        Cloudlet cloudlet
                = new CloudletSimple(
                        numberOfCreatedCloudlets++, length, numberOfPes,
                        fileSize, outputSize,
                        utilization, utilization, utilization);
        cloudlet.setUserId(broker.getId());

        return cloudlet;
    }

    private double computeRoudRobinMappingCost() {
        CloudletToVmMappingSolution roudRobinSolution =
                new CloudletToVmMappingSolution(heuristic);
        int i = 0;
        for (Cloudlet c : cloudletList) {
            //cyclically selects a Vm (as in a circular queue)
            roudRobinSolution.bindCloudletToVm(c, vmList.get(i));
            i = (i+1) % vmList.size();
        }
        printSolution(
            "Round robin solution used by DatacenterBrokerSimple class",
            roudRobinSolution, false);
        return roudRobinSolution.getCost();
    }

    private static void printSolution(String title,
            CloudletToVmMappingSolution solution,
            boolean showIndividualCloudletFitness) {
        System.out.printf("%s (cost %.2f fitness %.6f)\n",
                title, solution.getCost(), solution.getFitness());
        if(!showIndividualCloudletFitness)
            return;

        for(Map.Entry<Cloudlet, Vm> e: solution.getResult().entrySet()){
            System.out.printf(
                "Cloudlet %3d (%d PEs, %6d MI) mapped to Vm %3d (%d PEs, %6.0f MIPS) with fitness %.2f\n",
                e.getKey().getId(),
                e.getKey().getNumberOfPes(), e.getKey().getCloudletLength(),
                e.getValue().getId(),
                e.getValue().getNumberOfPes(), e.getValue().getMips(),
                solution.getCostOfCloudletToVm(e.getKey(), e.getValue()));
        }
        System.out.println();
    }

}
