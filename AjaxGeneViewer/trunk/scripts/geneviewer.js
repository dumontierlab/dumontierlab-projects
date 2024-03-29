/*==================================================
 *  GeneViewer
 *==================================================
 */

Timeline.GeneViewer = new Object();

Timeline.GeneViewer.create = function(elmt, bandInfos) {
    return Timeline.create(elmt, bandInfos, Timeline.HORIZONTAL, Timeline.GeneViewerUnit);
};


Timeline.GeneViewer.createBandInfo = function(params) {
    var theme = ("theme" in params) ? params.theme : Timeline.getDefaultTheme();
    
    var eventSource = ("eventSource" in params) ? params.eventSource : null;
    
    var ether = new Timeline.LinearEther({ 
        centersOn:          ("date" in params) ? params.date : Timeline.GeneViewerUnit.makeDefaultValue(),
        interval:           1,
        pixelsPerInterval:  params.intervalPixels
    });
    
    var etherPainter = new Timeline.GeneViewerEtherPainter({
        intervalUnit:       params.intervalUnit, 
        multiple:           ("multiple" in params) ? params.multiple : 1,
        align:              params.align,
        theme:              theme 
    });
    
    var eventPainterParams = {
        theme:      theme
    };
    if ("trackHeight" in params) {
        eventPainterParams.trackHeight = params.trackHeight;
    }
    if ("trackGap" in params) {
        eventPainterParams.trackGap = params.trackGap;
    }
    var eventPainter = ("overview" in params && params.overview) ?
        new Timeline.OverviewEventPainter(eventPainterParams) :
        new Timeline.DetailedEventPainter(eventPainterParams);
    
    return {   
        width:          params.width,
        eventSource:    eventSource,
        timeZone:       0,
        ether:          ether,
        etherPainter:   etherPainter,
        eventPainter:   eventPainter
    };
};