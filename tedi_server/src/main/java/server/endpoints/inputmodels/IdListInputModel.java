package server.endpoints.inputmodels;

import java.util.ArrayList;
import java.util.List;

public class IdListInputModel {
	
	private List<IdInputModel> ids = new ArrayList<>();
	
	public IdListInputModel() {}

	public List<Long> getIds() {
		List<Long> idsToGet = new ArrayList<>();
		for (IdInputModel e : ids) {
			idsToGet.add(e.getId());
		}
		return idsToGet;
	}

	public void setIds(List<IdInputModel> emails) {
		this.ids = emails;
	}

}
