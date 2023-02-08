var tables = {};
var intervalId;

$(document).ready(function() {

    $("#accordion").accordion({
        heightStyle: "content",
        active: false,
        collapsible: true
    });

    $('#resetCacheLocal').click(function(e) {
        var token = $("meta[name='_csrf']").attr("content");
        var url = $('#resetCacheLocal').attr('data-url');
        $.ajax({
            url : url,
            headers : {
                'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
            },
            success : function(data) {
            },
            error: hac.global.err
        });
    });

    $('#resetCacheCluster').click(function(e) {
        var token = $("meta[name='_csrf']").attr("content");
        var url = $('#resetCacheCluster').attr('data-url');
        $.ajax({
            url : url,
            headers : {
                'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
            },
            success : function(data) {
            },
            error: hac.global.err
        });
    });

    $( ".buttonClass_fetch" ).each(function(index) {
        $(this).on("click", function(){
            var data_url_attr = $(this).attr('data-cache');
            fetchCacheContent(data_url_attr);
        });
    });

    $( ".buttonClass_clear" ).each(function(index) {
        $(this).on("click", function(){
            var token = $("meta[name='_csrf']").attr("content");
            var data_url_attr = $(this).attr('data-cache');
            var dataUrl = 'clearCacheLocally?cache=' + data_url_attr;
            $.ajax({
                url: dataUrl,
                headers: {
                    'Accept': 'application/json',
                    'X-CSRF-TOKEN': token
                },
                success: function (data) {
                    tables[data_url_attr].fnClearTable();
                },
            });
        });
    });

    $( ".buttonClass_clear_cluster" ).each(function(index) {
        $(this).on("click", function(){
            var token = $("meta[name='_csrf']").attr("content");
            var data_url_attr = $(this).attr('data-cache');
            var dataUrl = 'clearCacheInCluster?cache=' + data_url_attr;
            $.ajax({
                url: dataUrl,
                headers: {
                    'Accept': 'application/json',
                    'X-CSRF-TOKEN': token
                },
                success: function (data) {
                    tables[data_url_attr].fnClearTable();
                },
            });
        });
    });

    $( ".refresh_cache_content" ).each(function(index) {
        $(this).on("change", function(){
            var token = $("meta[name='_csrf']").attr("content");
            var data_url_attr = $(this).attr('data-cache');
            if ($(this).is(":checked") == true) {
                var intervalId = setInterval(() => fetchCacheContent(data_url_attr), 2000);
                $(this).attr('intervalId', intervalId);
            }
            else {
                var intervalIdRemove = $(this).attr('intervalId');
                clearInterval(intervalIdRemove);
            }
        });
    });

    update(true);
    intervalId = setInterval(() => update(false), 2000);

});

function fetchCacheContent(cache_identifier) {
    var token = $("meta[name='_csrf']").attr("content");
    var searchTermExp = "[aria-controls='" +cache_identifier +"_types']";
    var searchTerm = $(searchTermExp);
    var dataUrl = 'entries?cache=' + cache_identifier+'&search='+searchTerm[1].value;
    $.ajax({
        url: dataUrl,
        headers: {
            'Accept': 'application/json',
            'X-CSRF-TOKEN': token
        },
        success: function (data) {
            updateEntries(cache_identifier, data.entries);
        },
    });
}

function updateEntries(cacheIdentifier, entries) {
    tables[cacheIdentifier].fnClearTable();
    entries.forEach(entry => {
        tables[cacheIdentifier].fnAddData([entry.key, entry.value]);
    })
}

function update(init) {
    if (document.hidden) {
        return;
    }
    var token = $("meta[name='_csrf']").attr("content");
    var url = $('#accordion').attr('data-updateDataUrl');
    $.ajax({
        url: url,
        type: 'GET',
        headers: {
            'Accept': 'application/json',
            'X-CSRF-TOKEN': token
        },
        success: function (data) {

            if (init) {
                initTables(data);
            }

            updateRegions(data);

        },
        error: hac.global.err
    });
}

function initTables(regions) {
    for (var pos in regions) {
        if (regions.hasOwnProperty(pos)) {
            var regionName = regions[pos].name;
            var cacheManager = regions[pos].cacheManagerName;
            tables[regionName+'-'+cacheManager] = $(`#${regionName}-${cacheManager}_types`).dataTable({});
        }
    }
}

function updateRegions(regions)
{
    var pattern = /^([a-zA-Z0-9]+) \((.+)\)$/;
    for (var pos in regions)
    {
        if (regions.hasOwnProperty(pos)) {
            updateRegion(regions[pos], pattern);
        }
    }
}

function updateRegion(region, pattern) {
    const name = $("<div/>").text(region.name+'-'+region.cacheManagerName).html();

    $(`#${name}_maxEntries`).text(region.statistics.maxSize);
    $(`#${name}_maxReachedSize`).text(region.statistics.currentSize);
    $(`#${name}_factor`).text(region.statistics.hitFactor);

    try {
        $(`#${name}_Hits`).text(region.statistics.hits);
        $(`#${name}_Misses`).text(region.statistics.misses);
        $(`#${name}_Invalidations`).text(region.statistics.invalidations);
        $(`#${name}_Evictions`).text(region.statistics.evictions);
        $(`#${name}_InstanceCount`).text(region.statistics.instanceCount);
    }
    catch (err) {
        //
    }
}
