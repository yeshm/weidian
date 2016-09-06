<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Location Search</title>
</head>
<body>

<input type="button" onclick="return setLocationSearch();" value="setLocationSearch">
<br/>
<input type="button" onclick="return getLocationSearch();" value="getLocationSearch">

<script type="text/javascript">
var setLocationSearch = function(){
    var s = location.search;
    location.search = s+"&abcd"
};

var getLocationSearch = function(){
    var s = location.search;
    alert(s);
};
</script>

</body>

</html>