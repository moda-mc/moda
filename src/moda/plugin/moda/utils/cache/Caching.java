package moda.plugin.moda.utils.cache;

public class Caching {
	
	private static Cache localCache;
	private static Cache sharedCache;
	
	private Caching() {}
	
	public static Cache getLocalCache() {
		return localCache;
	}
	
	public static Cache getSharedCache() {
		if (sharedCache == null) {
			return localCache;
		} else {
			return sharedCache;
		}
	}
	
	public static void init() {
		// TODO redis cache
		localCache = new HashMapCache();
	}

}
