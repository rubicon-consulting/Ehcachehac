<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>EhCache</title>

    <link rel="stylesheet" href="<c:url value="/static/css/table.css"/>" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="<c:url value="/static/css/cacheStat.css"/>" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="<c:url value="/static/css/custom-theme/jquery-ui.css"/>" type="text/css" media="screen, projection"/>

    <script type="text/javascript" src="<c:url value="/static/js/jquery.dataTables.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/static/js/cache.js"/>"></script>

</head>
<body>
<div class="prepend-top span-17 colborder" id="content">
    <button id="toggleSidebarButton">&gt;</button>
    <div class="marginLeft marginBottom">
        <h2>Ehcache Monitoring</h2>
        <hr/>
        <button id="resetCacheLocal" data-url="<c:url value="/ehcachehac/cache/clearAllCacheLocally"/>">Clear all caches locally</button>
        <button id="resetCacheCluster" data-url="<c:url value="/ehcachehac/cache/clearAllCacheInCluster"/>">Clear all caches in the cluster</button>
        <br/>
        <div id="accordion" data-updateDataUrl="cacheData">

            <c:forEach var="cache" items="${caches}">
                <h3 id="h-${cache.name}" style="padding-top:5px;padding-bottom:5px;"><b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp ${cache.name} - ${cache.cacheManagerName}</b></h3>
                <div id="div-${cache.name}">
                    <div style="margin-left:-15px">
                        <table>
                            <tr>
                                <td>
                                    <dl class="marginTop">
                                        <dt>Max Size</dt>
                                        <dd id="${cache.name}-${cache.cacheManagerName}_maxEntries"></dd>
                                        <dt>Max Reached Size</dt>
                                        <dd id="${cache.name}-${cache.cacheManagerName}_maxReachedSize"></dd>
                                        <dt>Hit/Miss Ratio</dt>
                                        <dd><span id="${cache.name}-${cache.cacheManagerName}_factor"></span>%</dd>
                                    </dl>
                                </td>
                                <td>
                                    <table>
                                        <thead>
                                        <tr>
                                            <th>Hits</th>
                                            <th>Misses</th>
                                            <th>Invalidations</th>
                                            <th>Evictions</th>
                                            <th>InstanceCount</th>
                                        </tr>
                                        </thead>
                                        <tr>
                                            <td id="${cache.name}-${cache.cacheManagerName}_Hits"></td>
                                            <td id="${cache.name}-${cache.cacheManagerName}_Misses"></td>
                                            <td id="${cache.name}-${cache.cacheManagerName}_Invalidations"></td>
                                            <td id="${cache.name}-${cache.cacheManagerName}_Evictions"></td>
                                            <td id="${cache.name}-${cache.cacheManagerName}_InstanceCount"></td>
                                        </tr>
                                        <tbody>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                        </table>
                        <table>
                            <tr>
                                <td style="width:90%">
                                    <table id="${cache.name}-${cache.cacheManagerName}_types" >
                                        <thead>
                                        <tr>
                                            <th>Keys</th>
                                            <th>Value</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        </tbody>
                                    </table>
                                </td>
                                <td style="width:10%;vertical-align:top">
                                    <button class="buttonClass_fetch" data-cache="${cache.name}-${cache.cacheManagerName}">Fetch CacheEntries</button>
                                    <br />
                                    <input type="checkbox" id="${cache.name}-${cache.cacheManagerName}_refresh" data-cache="${cache.name}-${cache.cacheManagerName}" class="refresh_cache_content" name="refresh_cache_content" value="refresh">
                                    <label for="refresh_cache_content">auto-refresh CacheContent</label><br>
                                    <button class="buttonClass_clear" data-cache="${cache.name}-${cache.cacheManagerName}">Clear Cache</button>
                                    <button class="buttonClass_clear_cluster" data-cache="${cache.name}-${cache.cacheManagerName}">Clear Cache on all nodes</button>
                                    <br />
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
</body>
</html>

