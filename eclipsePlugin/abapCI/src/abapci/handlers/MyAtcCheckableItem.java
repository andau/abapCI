package abapci.handlers;

import java.net.URI;

import com.sap.adt.atc.IAtcCheckableItem;

public class MyAtcCheckableItem implements IAtcCheckableItem {

	private final URI uri;
	private final String name;
	private final String type;

	public MyAtcCheckableItem(URI uri, String name, String type) {
		this.uri = uri;
		this.name = name;
		this.type = type;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getAdapter(Class adapter) {
		return null;
	}

	public URI getUri() {
		return uri;
	}

	public String getName() {
		return name;
	}

	public int hashCode() {
		int result = 1;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (uri != null ? uri.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyAtcCheckableItem other = (MyAtcCheckableItem) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	public String getType() {
		return type;
	}
}