/*
 * Copyright (c) 2023 Rubicon Consulting LLS. All rights reserved.
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions requires the registration of the usage under
 * 	  https://www.rubicon-consulting.us/register-an-sap-commerce-cloud-extension-usage/
 *  * Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided
 *    with the distribution.
 *  * Neither the name of Google Inc. nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */
package us.rubicon_consulting.facade.impl;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.ehcachehac.data.CacheContent;
import de.hybris.platform.ehcachehac.data.CacheData;
import de.hybris.platform.ehcachehac.data.CacheEntry;
import de.hybris.platform.ehcachehac.data.CacheManagerData;
import de.hybris.platform.servicelayer.event.EventService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.apache.commons.lang.StringUtils;
import us.rubicon_consulting.facade.CacheDataFacade;
import us.rubicon_consulting.service.ClearCacheService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class CacheDataFacadeImpl implements CacheDataFacade {

    @Resource
    private Populator<Cache, CacheData> cacheDataPopulator;

    @Resource
    private ClearCacheService clearCacheService;

    @Resource
    private EventService eventService;

    @Override
    public List<CacheManagerData> getListOfCacheManagerData() {
        List<CacheManagerData> cacheMangerList = new ArrayList<>();
        for (CacheManager manager : CacheManager.ALL_CACHE_MANAGERS) {
            CacheManagerData cacheManagerData = new CacheManagerData();
            cacheManagerData.setName(manager.getName());
            cacheManagerData.setCaches(new ArrayList<>());
            cacheMangerList.add(cacheManagerData);
            for (String name : manager.getCacheNames()) {
                CacheData cacheData = new CacheData();
                cacheDataPopulator.populate(manager.getCache(name), cacheData);
                cacheManagerData.getCaches().add(cacheData);
                manager.getCache(name).getCacheConfiguration().setStatistics(true);
            }
        }
        System.out.println(cacheMangerList);
        return cacheMangerList;
    }

    @Override
    public List<CacheData> getAllcacheData() {
        List<CacheData> cacheList = new ArrayList<>();
        for (CacheManager manager : CacheManager.ALL_CACHE_MANAGERS) {
            for (String name : manager.getCacheNames()) {
                if (!name.endsWith("Region")) {
                    CacheData cacheData = new CacheData();
                    cacheDataPopulator.populate(manager.getCache(name), cacheData);
                    cacheList.add(cacheData);
                }
            }
        }
        return cacheList;
    }

    @Override
    public CacheContent getEntries(String cacheManager, String cacheName, String searchTerm) {
        CacheContent content = new CacheContent();
        content.setEntries(new ArrayList<>());

        if(StringUtils.isNotEmpty(cacheManager) && StringUtils.isNotEmpty(cacheName)) {
            for (CacheManager manager : CacheManager.ALL_CACHE_MANAGERS) {
                if (StringUtils.equalsIgnoreCase(cacheManager, manager.getName()) && manager.getCache(cacheName) != null) {
                    Cache cache = manager.getCache(cacheName);
                    for (Object key : cache.getKeys()) {
                        String valueString = "";
                        Object cachedValue = cache.get(key);
                        if (cachedValue != null) {
                            valueString = cachedValue.toString();
                        }
                        CacheEntry entry = new CacheEntry();
                        entry.setKey(key.toString());
                        if(StringUtils.contains(entry.getKey(), searchTerm)) {
                            entry.setValue(valueString);
                            content.getEntries().add(entry);
                        }
                    }
                }
            }
        }

        return content;
    }

    @Override
    public Boolean clearCacheLocally(String cacheManager, String cacheName) {
        return getClearCacheService().clearCacheLocally(cacheManager, cacheName);
    }

    @Override
    public Boolean clearCacheCluster(String cacheManager, String cacheName) {
        return getClearCacheService().clearCacheCluster(cacheManager, cacheName);
    }

    @Override
    public Boolean clearAllCachesLocally() {
        return getClearCacheService().clearAllCachesLocally();
    }

    @Override
    public Boolean clearAllCachesCluster() {
        return getClearCacheService().clearAllCachesCluster();
    }

    public Populator<Cache, CacheData> getCacheDataPopulator() {
        return cacheDataPopulator;
    }

    public void setCacheDataPopulator(Populator<Cache, CacheData> cacheDataPopulator) {
        this.cacheDataPopulator = cacheDataPopulator;
    }

    public ClearCacheService getClearCacheService() {
        return clearCacheService;
    }

    public void setClearCacheService(ClearCacheService clearCacheService) {
        this.clearCacheService = clearCacheService;
    }

    public EventService getEventService() {
        return eventService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }
}
