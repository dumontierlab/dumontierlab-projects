/*==================================================
 *  GeneViewer Ether Painter
 *==================================================
 */
Timeline.GeneViewerEtherPainter = function(params, band, timeline) {
    this._params = params;
    this._intervalUnit = params.intervalUnit;
    this._multiple = ("multiple" in params) ? params.multiple : 1;
    this._theme = params.theme;
};

Timeline.GeneViewerEtherPainter.prototype.initialize = function(band, timeline) {
    this._band = band;
    this._timeline = timeline;
    
    this._backgroundLayer = band.createLayerDiv(0);
    this._backgroundLayer.setAttribute("name", "ether-background"); // for debugging
    this._backgroundLayer.className = 'timeline-ether-bg';

    //this._backgroundLayer.style.background = this._theme.ether.backgroundColors[band.getIndex()];
    
    this._markerLayer = null;
    this._lineLayer = null;
    
    var align = ("align" in this._params && typeof this._params.align == "string") ? this._params.align : 
        this._theme.ether.interval.marker[timeline.isHorizontal() ? "hAlign" : "vAlign"];
    var showLine = ("showLine" in this._params) ? this._params.showLine : 
        this._theme.ether.interval.line.show;
        
    this._intervalMarkerLayout = new Timeline.GeneViewerEtherMarkerLayout(
        this._timeline, this._band, this._theme, align, showLine);
        
    this._highlight = new Timeline.EtherHighlight(
        this._timeline, this._band, this._theme, this._backgroundLayer);
};

Timeline.GeneViewerEtherPainter.prototype.setHighlight = function(startDate, endDate) {
    this._highlight.position(startDate, endDate);
}

Timeline.GeneViewerEtherPainter.prototype.paint = function() {
    if (this._markerLayer) {
        this._band.removeLayerDiv(this._markerLayer);
    }
    this._markerLayer = this._band.createLayerDiv(100);
    this._markerLayer.setAttribute("name", "ether-markers"); // for debugging
    this._markerLayer.style.display = "none";
    
    if (this._lineLayer) {
        this._band.removeLayerDiv(this._lineLayer);
    }
    this._lineLayer = this._band.createLayerDiv(1);
    this._lineLayer.setAttribute("name", "ether-lines"); // for debugging
    this._lineLayer.style.display = "none";
    
    var minDate = Math.max(0, Math.ceil(Timeline.GeneViewerUnit.toNumber(this._band.getMinDate())));
    var maxDate = Math.floor(Timeline.GeneViewerUnit.toNumber(this._band.getMaxDate()));
    
    var hasMore = function() {
        return minDate < maxDate;
    };
    var multiple = this._multiple;
    
    var increment = function() {
        minDate += multiple;
    };
    
    var labeller = this._band.getLabeller();
    while (true) {
        this._intervalMarkerLayout.createIntervalMarker(
            Timeline.GeneViewerUnit.fromNumber(minDate), 
            labeller, 
            this._intervalUnit, 
            this._markerLayer, 
            this._lineLayer
        );
        if (hasMore()) {
            increment();
        } else {
            break;
        }
    }
    this._markerLayer.style.display = "block";
    this._lineLayer.style.display = "block";
};

Timeline.GeneViewerEtherPainter.prototype.softPaint = function() {
};

/*==================================================
 *  GeneViewer Ether Marker Layout
 *==================================================
 */
Timeline.GeneViewerEtherMarkerLayout = function(timeline, band, theme, align, showLine){
	var horizontal = timeline.isHorizontal();
    if (horizontal) {
        if (align == "Top") {
            this.positionDiv = function(div, offset) {
                div.style.left = offset + "px";
                div.style.top = "0px";
            };
        } else {
            this.positionDiv = function(div, offset) {
                div.style.left = offset + "px";
                div.style.bottom = "0px";
            };
        }
    } else {
        if (align == "Left") {
            this.positionDiv = function(div, offset) {
                div.style.top = offset + "px";
                div.style.left = "0px";
            };
        } else {
            this.positionDiv = function(div, offset) {
                div.style.top = offset + "px";
                div.style.right = "0px";
            };
        }
    }
    
    var markerTheme = theme.ether.interval.marker;
    var lineTheme = theme.ether.interval.line;
    
    var stylePrefix = (horizontal ? "h" : "v") + align;
    var labelStyler = markerTheme[stylePrefix + "Styler"];
    var emphasizedLabelStyler = markerTheme[stylePrefix + "EmphasizedStyler"];
    
    this.createIntervalMarker = function(date, labeller, unit, markerDiv, lineDiv) {
        var offset = Math.round(band.dateToPixelOffset(date));

        if (showLine) {
            var divLine = timeline.getDocument().createElement("div");
            divLine.className = "timeline-ether-lines";
            
            if (lineTheme.opacity < 100) {
                SimileAjax.Graphics.setOpacity(divLine, lineTheme.opacity);
            }
            
            if (horizontal) {
                //divLine.className += " timeline-ether-lines-vertical";
                divLine.style.left = offset + "px";
            } else {
                //divLine.className += " timeline-ether-lines-horizontal";
            	divLine.style.top = offset + "px";
            }
            lineDiv.appendChild(divLine);

        }
        
        var label = labeller.labelInterval(date, unit);
        
        var div = timeline.getDocument().createElement("div");
        div.innerHTML = label.text;
        div.className = 'timeline-date-label'
        if(label.emphasized) div.className += ' timeline-date-label-em'

        
        this.positionDiv(div, offset);
        markerDiv.appendChild(div);
        
        return div;
    };
};

