# IoTSimSDN

Self-contained simulation tool that enables the modeling and simulating of big data management systems (e.g., YARN), big
data programming models (e.g., MapReduce), and SDN-enabled networks within cloud computing
environments.

## Internet 

### JSON input

"vmType":
Small_VM=1;

    size = 10000; // image size (MB)
    ram = 512; // vm memory (MB)
    mips = 250;
    bw = 1000;
    pesNumber = 1; // number of cpus
    vmm = "Xen"; // VMM name
Medium_VM=2;

    size = 20000; // image size (MB)
    ram = 1024; // vm memory (MB)
    mips = 500;
    bw = 1000;
    pesNumber = 2; // number of cpus
    vmm = "Xen"; // VMM name
Amazon_i2_xlarge=3;

    size = 800000; // image size (MB)- 800 GB (SSD)
    ram = 32750; // vm memory (MB)-- 15.25 GiB (Gibibyte)
    mips = 1250; // bogomips 4 (core) * 1250 = 5000    
    bw = 800; // *.xlarge = 700-900 MBit/s (Mbps) (Moderate Network Performance) 
    pesNumber = 4; // number of cpus
    vmm = "Xen"; // VMM name
Default
			
    size = 40000; // image size (MB)
    ram = 2048; // vm memory (MB)
    mips = 1000;
    bw = 8000; // Mbps --> 1 GB
    pesNumber = 4; // number of cpus
    vmm = "Xen"; // VMM name
		

### JSON preprocessing

The name field of the switches of type gateways is renamed in runtime. The name is changed to <datacenter>_gateway, where <datacenter> is the datacenter this gateway (switch) belongs to. 

This is done to avoid node names duplication at the wanController level.