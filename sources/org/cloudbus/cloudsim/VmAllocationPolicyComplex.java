package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VmAllocationPolicyComplex extends VmAllocationPolicy {
	
	
	/** The vm table. */
	private Map<String, Host> vmTable;
	
	
	/** The free pes. */
	private List<Integer> freePes;
	
	/** The used pes. */
	private Map<String, Integer> usedPes;
	

	public VmAllocationPolicyComplex(List<? extends Host> list) {
		super(list);
		
		setFreePes(new ArrayList<Integer>());
		for (Host host : getHostList()) {
			getFreePes().add(host.getNumberOfPes());

		}

		setVmTable(new HashMap<String, Host>());
		setUsedPes(new HashMap<String, Integer>());
		
	}

	private void setUsedPes(Map<String, Integer> usedPes) {
		// TODO Auto-generated method stub
		
		this.usedPes = usedPes;
		
	}

	private void setVmTable(Map<String, Host> vmTable) {
		// TODO Auto-generated method stub
		
		this.vmTable = vmTable;
		
		
	}

	private List<Integer> getFreePes() {
		// TODO Auto-generated method stub
		return freePes;
	}

	private void setFreePes(List<Integer> freePes) {
		// TODO Auto-generated method stub
		
		this.freePes = freePes;
		
		
	}

	@Override
	public boolean allocateHostForVm(Vm vm) {
		// TODO Auto-generated method stub
		
		
		int requiredPes = vm.getNumberOfPes();
		boolean result = false;
		int tries = 0;
		
		
		
		if (!getVmTable().containsKey(vm.getUid()))
		{
			                               
			
			
			Host host = getHostList().get(vm.getProposedHostId());
			
			result = host.vmCreate(vm);
			
			if (result) { // if vm were succesfully created in the host
				getVmTable().put(vm.getUid(), host);
				getUsedPes().put(vm.getUid(), requiredPes);
				getFreePes().set(0, getFreePes().get(0) - requiredPes);
				result = true;
				
			}
			
			else
			{
				Host host1 = getHostList().get(1);
				
				result = host1.vmCreate(vm);
				
				if (result) { // if vm were succesfully created in the host
					getVmTable().put(vm.getUid(), host1);
					getUsedPes().put(vm.getUid(), requiredPes);
					getFreePes().set(0, getFreePes().get(0) - requiredPes);
					result = true;
					
				}
				
				
				
			}
			
			
			
			
			
			
		}
		
		
		
		
		return result;
	}

	private Map<String, Integer> getUsedPes() {
		// TODO Auto-generated method stub
		return usedPes;
	}

	private Map<String, Host> getVmTable() {
		// TODO Auto-generated method stub
		return vmTable;
	}

	@Override
	public boolean allocateHostForVm(Vm vm, Host host) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Map<String, Object>> optimizeAllocation(
			List<? extends Vm> vmList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deallocateHostForVm(Vm vm) {
		// TODO Auto-generated method stub

	}

	@Override
	public Host getHost(Vm vm) {
		// TODO Auto-generated method stub
		return getVmTable().get(vm.getUid());
	}

	@Override
	public Host getHost(int vmId, int userId) {
		// TODO Auto-generated method stub
		return getVmTable().get(Vm.getUid(userId, vmId));
	}

}
