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
package us.rubicon_consulting.populator.impl;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.ehcachehac.data.CacheData;
import de.hybris.platform.ehcachehac.data.CacheStatisticsData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import net.sf.ehcache.Cache;

public class CacheDataPopulator implements Populator<Cache, CacheData> {
    @Override
    public void populate(Cache cache, CacheData cacheData) throws ConversionException {

        cacheData.setName(cache.getName());
        cacheData.setCacheManagerName(cache.getCacheManager().getName());

        CacheStatisticsData cacheStatisticsData = new CacheStatisticsData();
        cacheStatisticsData.setHits(cache.getStatistics().cacheHitCount());
        cacheStatisticsData.setEvictions(cache.getStatistics().cacheEvictedCount());
        cacheStatisticsData.setInvalidations(cache.getStatistics().cacheExpiredCount());
        cacheStatisticsData.setMisses(cache.getStatistics().cacheMissCount());
        int hitFactor = 0;
        if (cacheStatisticsData.getHits() > 0 ) {
            hitFactor = Math.toIntExact((cacheStatisticsData.getHits() * 100) / (cacheStatisticsData.getHits()+ cacheStatisticsData.getMisses()));
        }
        cacheStatisticsData.setHitFactor(hitFactor);
        cacheStatisticsData.setMaxSize(cache.getCacheConfiguration().getMaxEntriesLocalHeap());
        cacheStatisticsData.setCurrentSize(cache.getStatistics().getSize());
        cacheStatisticsData.setInstanceCount(cache.getStatistics().getLocalHeapSize());
        cacheData.setStatistics(cacheStatisticsData);
    }
}
