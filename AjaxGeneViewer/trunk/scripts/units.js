Timeline.GeneViewerUnit = new Object();

Timeline.GeneViewerUnit.BASES = 0;

Timeline.GeneViewerUnit.getParser = function(format) {
    return Timeline.GeneViewerUnit.parseFromObject;
};

Timeline.GeneViewerUnit.createLabeller = function(locale, timeZone) {
    return new Timeline.GeneViewerLabeller(locale);
};

Timeline.GeneViewerUnit.makeDefaultValue = function () {
    return 0;
};

Timeline.GeneViewerUnit.cloneValue = function (v) {
    return v;
};

Timeline.GeneViewerUnit.parseFromObject = function(o) {
    if (o == null) {
        return null;
    } else if (typeof o == "number") {
        return o;
    } else {
        try {
            return parseInt(o);
        } catch (e) {
            return null;
        }
    }
};

Timeline.GeneViewerUnit.toNumber = function(v) {
    return v
};

Timeline.GeneViewerUnit.fromNumber = function(n) {
    return n;
};

Timeline.GeneViewerUnit.compare = function(v1, v2) {
    return v1 - v2;
};

Timeline.GeneViewerUnit.earlier = function(v1, v2) {
    return Timeline.GeneViewerUnit.compare(v1, v2) < 0 ? v1 : v2;
};

Timeline.GeneViewerUnit.later = function(v1, v2) {
    return Timeline.GeneViewerUnit.compare(v1, v2) > 0 ? v1 : v2;
};

Timeline.GeneViewerUnit.change = function(v, n) {
    return v + n;
};