package zhishusz.housepresell.util;
import java.net.URL;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import zhishusz.housepresell.database.po.state.S_PoName;
 
/**
 * Created by xxx on 2017/7/29.
 */
public class EhCacheUtil {
    //ehcache.xml 保存在src/main/resources/
    private static final String path = "/ehcache.xml";
 
    private URL url;
 
    private CacheManager manager;
 
    private static EhCacheUtil ehCache;
 
    private EhCacheUtil(String path) {
        url = getClass().getResource(path);
        manager = CacheManager.create(url);
    }
 
    public static EhCacheUtil getInstance() {
        if (ehCache == null) {
            ehCache = new EhCacheUtil(path);
        }
        return ehCache;
    }
 
    public void put(String cacheName, String key, Object value) {
        Cache cache = manager.getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
    }
 
    public Object get(String cacheName, String key) {
        Cache cache = manager.getCache(cacheName);
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue();
    }
 
    public Cache get(String cacheName) {
        return manager.getCache(cacheName);
    }
 
    public void remove(String cacheName) {
    	Integer index = cacheName.indexOf("_$$");
    	if(index != -1)
        {
    		cacheName = cacheName.substring(0,index);
        }
        Cache cache = manager.getCache(cacheName);
        
        if(cache != null)
        {
        	cache.removeAll();
        }
    }
    
    public void removeAll() {
    	String[] cacheNameArr = S_PoName.PoNameArr;
    	for(int i=0;i<cacheNameArr.length;i++)
    	{
    		Cache cache = manager.getCache(cacheNameArr[i]);
    		if(cache != null)
            {
            	cache.removeAll();
            }
    	}
    }
}