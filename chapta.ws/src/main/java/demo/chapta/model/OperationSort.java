package demo.chapta.model;

import java.util.Comparator;

public class OperationSort implements Comparator<Operation>{

	@Override
	public int compare(Operation o1, Operation o2) {
		
		return o1.getStartTime().compareTo(o2.getStartTime());
	}

}
