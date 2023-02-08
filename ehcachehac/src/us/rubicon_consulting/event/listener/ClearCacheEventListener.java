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
package us.rubicon_consulting.event.listener;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import us.rubicon_consulting.event.ClearCacheEvent;
import us.rubicon_consulting.service.ClearCacheService;

import javax.annotation.Resource;
import java.util.List;

public class ClearCacheEventListener<T extends ClearCacheEvent> extends AbstractSiteEventListener<T> {

    private static final Logger LOG = Logger.getLogger(ClearCacheEventListener.class);

    @Resource
    private ClearCacheService clearCacheService;

    protected void onSiteEvent(final T event) {
        List<Cache> cachesToClear = getClearCacheService().getAllMatchingCaches(event.getCacheManager(), event.getCache());
        if (CollectionUtils.isNotEmpty(cachesToClear)) {
            for (Cache cacheToClear: cachesToClear) {
                cacheToClear.removeAll();
                LOG.debug(String.format("Following Cache been cleared. CacheManager: %s   Cache: %s", cacheToClear.getCacheManager().getName(), cacheToClear.getName()));
            }
        }
    }

    protected boolean shouldHandleEvent(final T event) {
        return (event instanceof ClearCacheEvent) && !StringUtils.isEmpty(event.getCacheManager()) && !StringUtils.isEmpty(event.getCache());
    }

    public ClearCacheService getClearCacheService() {
        return clearCacheService;
    }

    public void setClearCacheService(ClearCacheService clearCacheService) {
        this.clearCacheService = clearCacheService;
    }
}
