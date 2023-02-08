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
package de.hybris.platform.hac.controller;

import de.hybris.platform.ehcachehac.data.CacheContent;
import de.hybris.platform.ehcachehac.data.CacheData;
import de.hybris.platform.ehcachehac.data.CacheManagerData;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import us.rubicon_consulting.facade.CacheDataFacade;

import javax.annotation.Resource;


/**
 *
 */
@Controller
@RequestMapping("/ehcachehac/**")
public class EhcachehacController
{
	@Resource
	private CacheDataFacade cacheDataFacade;

	@RequestMapping(value = "/allManager", method = RequestMethod.GET)
	@ResponseBody
	public List<CacheManagerData> getCacheManagers()
	{
		return cacheDataFacade.getListOfCacheManagerData();
	}

	@RequestMapping(value = "/cacheData", method = RequestMethod.GET)
	@ResponseBody
	public List<CacheData> getAllCaches() {
		return cacheDataFacade.getAllcacheData();
	}

	@RequestMapping(value = "/cache", method = RequestMethod.GET)
	public String getCacheData(final Model model)
	{
		model.addAttribute("caches", getAllCaches());
		return "cache";
	}

	@RequestMapping(value = "/clearcache", method = RequestMethod.GET)
	public String getClearCache(final Model model)
	{
		return "extension";
	}

	@RequestMapping(value = "/cache/entries", method = RequestMethod.GET)
	@ResponseBody
	public CacheContent getEntries(@RequestParam("cache") String cache, @RequestParam("search") String searchTerm)
	{
		String[] cacheData = StringUtils.split(cache, "-");
		return cacheDataFacade.getEntries(cacheData[1], cacheData[0], searchTerm);
	}

	@RequestMapping(value = "/cache/clearCacheLocally", method = RequestMethod.GET)
	@ResponseBody
	public Boolean clearCacheLocally(@RequestParam("cache") String cache)
	{
		String[] cacheData = StringUtils.split(cache, "-");
		return cacheDataFacade.clearCacheLocally(cacheData[1], cacheData[0]);
	}

	@RequestMapping(value = "/cache/clearAllCacheLocally", method = RequestMethod.GET)
	@ResponseBody
	public Boolean clearAllCacheLocally()
	{
		return cacheDataFacade.clearAllCachesLocally();
	}

	@RequestMapping(value = "/cache/clearCacheInCluster", method = RequestMethod.GET)
	@ResponseBody
	public Boolean clearCacheCluster(@RequestParam("cache") String cache)
	{
		String[] cacheData = StringUtils.split(cache, "-");
		return cacheDataFacade.clearCacheCluster(cacheData[1], cacheData[0]);
	}

	@RequestMapping(value = "/cache/clearAllCacheInCluster", method = RequestMethod.GET)
	@ResponseBody
	public Boolean clearAllCacheCluster()
	{
		return cacheDataFacade.clearAllCachesCluster();
	}
}
