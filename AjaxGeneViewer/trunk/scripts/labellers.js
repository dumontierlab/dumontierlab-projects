Timeline.GeneViewerLabeller = function(locale) {
    this._locale = locale;
    this._aaa = "aaa";
};

Timeline.GeneViewerLabeller.labels = [];

Timeline.GeneViewerLabeller.prototype.labelInterval = function(val, intervalUnit) {
    var n = Timeline.GeneViewerUnit.toNumber(val);
    
    var unitLabel = "";
    var labels = Timeline.GeneViewerLabeller.labels[this._locale];
    
    switch (intervalUnit) {
    	case Timeline.GeneViewerUnit.BASES:     unitLabel = labels.bases;      
    	     break;
    }
    
    if(val < 1000){
    	return {text: n+unitLabel, emphasized: false};
	}
	if(val < 1000000){
		var kilos = n / 1000;
		return {text: kilos.toFixed(2)+labels.kilo+unitLabel, emphasized: false};
	}
	if(val < 1000000000){
		var megas = n / 1000000;
		return {text: megas.toFixed(2)+labels.mega+unitLabel, emphasized: false};
	}
};

Timeline.GeneViewerLabeller.prototype.labelPrecise = function(val) {
    return Timeline.GeneViewerUnit.toNumber(val);
};

	