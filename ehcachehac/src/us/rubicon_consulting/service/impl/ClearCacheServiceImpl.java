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
package us.rubicon_consulting.service.impl;

import de.hybris.platform.servicelayer.event.EventService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import us.rubicon_consulting.constants.EhcachehacConstants;
import us.rubicon_consulting.event.ClearCacheEvent;
import us.rubicon_consulting.service.ClearCacheService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class ClearCacheServiceImpl implements ClearCacheService {

    @Resource
    private EventService eventService;

    @Override
    public Boolean clearCacheLocally(String cacheManager, String cacheName) {
        Boolean result = Boolean.FALSE;
        List<Cache> cachesToClear = getAllMatchingCaches(cacheManager, cacheName);
        if (CollectionUtils.isNotEmpty(cachesToClear)) {
            for (Cache cache : cachesToClear) {
                cache.removeAll();
                result = Boolean.TRUE;
            }
        }
        return result;
    }

    @Override
    public Boolean clearCacheCluster(String cacheManager, String cacheName) {
        ClearCacheEvent event = new ClearCacheEvent(cacheManager, cacheName);
        getEventService().publishEvent(event);
        return Boolean.TRUE;
    }

    @Override
    public Boolean clearAllCachesLocally() {
        Boolean result = Boolean.FALSE;
        List<Cache> cachesToClear = getAllMatchingCaches(EhcachehacConstants.ALL_CACHE_WILDCARD, EhcachehacConstants.ALL_CACHE_WILDCARD);
        if (CollectionUtils.isNotEmpty(cachesToClear)) {
            for (Cache cache : cachesToClear) {
                cache.removeAll();
                result = Boolean.TRUE;
            }
        }
        return result;
    }

    @Override
    public Boolean clearAllCachesCluster() {
        ClearCacheEvent event = new ClearCacheEvent(EhcachehacConstants.ALL_CACHE_WILDCARD, EhcachehacConstants.ALL_CACHE_WILDCARD);
        getEventService().publishEvent(event);
        return Boolean.TRUE;
    }

    public List<Cache> getAllMatchingCaches(String cacheManager, String cacheName) {
        List<Cache> cacheList = new ArrayList<>();

        if(StringUtils.isNotEmpty(cacheManager) && StringUtils.isNotEmpty(cacheName)) {
            for (CacheManager manager : CacheManager.ALL_CACHE_MANAGERS) {
                if (StringUtils.equalsIgnoreCase(cacheManager, manager.getName()) || StringUtils.equals(EhcachehacConstants.ALL_CACHE_WILDCARD, cacheManager)) {
                    for (String cacheEntryName : manager.getCacheNames()) {
                        if (StringUtils.equalsIgnoreCase(cacheEntryName, cacheName) || StringUtils.equals(EhcachehacConstants.ALL_CACHE_WILDCARD, cacheName)) {
                            cacheList.add(manager.getCache(cacheEntryName));
                        }
                    }
                }
            }
        }

        return cacheList;
    }

    public EventService getEventService() {
        return eventService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }
}
