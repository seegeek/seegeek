
(function (b, a) {
	b.version = "0.9.6";
	b.protocol = 1;
	b.transports = [];
	b.j = [];
	b.sockets = {};
	b.connect = function (c, d) {
		var f = b.util.parseUri(c), g, h;
		a && a.location && (f.protocol = f.protocol || a.location.protocol.slice(0, -1), f.host = f.host || (a.document ? a.document.domain : a.location.hostname), f.port = f.port || a.location.port);
		g = b.util.uniqueUri(f);
		var j = {host:f.host, secure:"https" == f.protocol, port:f.port || ("https" == f.protocol ? 443 : 80), query:f.query || ""};
		b.util.merge(j, d);
		if (j["force new connection"] || !b.sockets[g]) {
			h = new b.Socket(j);
		}
		!j["force new connection"] && h && (b.sockets[g] = h);
		h = h || b.sockets[g];
		return h.of(1 < f.path.length ? f.path : "");
	};
})("object" === typeof module ? module.exports : this.io = {}, this);
(function (b, a) {
	var c = b.util = {}, d = /^(?:(?![^:@]+:[^:@\/]*@)([^:\/?#.]+):)?(?:\/\/)?((?:(([^:@]*)(?::([^:@]*))?)?@)?([^:\/?#]*)(?::(\d*))?)(((\/(?:[^?#](?![^?#\/]*\.[^?#\/.]+(?:[?#]|$)))*\/?)?([^?#\/]*))(?:\?([^#]*))?(?:#(.*))?)/, f = "source,protocol,authority,userInfo,user,password,host,port,relative,path,directory,file,query,anchor".split(",");
	c.parseUri = function (a) {
		for (var a = d.exec(a || ""), b = {}, e = 14; e--; ) {
			b[f[e]] = a[e] || "";
		}
		return b;
	};
	c.uniqueUri = function (b) {
		var c = b.protocol, e = b.host, b = b.port;
		"document" in a ? (e = e || document.domain, b = b || ("https" == c && "https:" !== document.location.protocol ? 443 : document.location.port)) : (e = e || "localhost", !b && "https" == c && (b = 443));
		return (c || "http") + "://" + e + ":" + (b || 80);
	};
	c.query = function (a, b) {
		var e = c.chunkQuery(a || ""), i = [];
		c.merge(e, c.chunkQuery(b || ""));
		for (var d in e) {
			e.hasOwnProperty(d) && i.push(d + "=" + e[d]);
		}
		return i.length ? "?" + i.join("&") : "";
	};
	c.chunkQuery = function (a) {
		for (var b = {}, a = a.split("&"), e = 0, i = a.length, c; e < i; ++e) {
			c = a[e].split("="), c[0] && (b[c[0]] = c[1]);
		}
		return b;
	};
	var g = !1;
	c.load = function (b) {
		if ("document" in a && "complete" === document.readyState || g) {
			return b();
		}
		c.on(a, "load", b, !1);
	};
	c.on = function (a, b, e, i) {
		a.attachEvent ? a.attachEvent("on" + b, e) : a.addEventListener && a.addEventListener(b, e, i);
	};
	c.request = function (a) {
		if (a && "undefined" != typeof XDomainRequest) {
			return new XDomainRequest;
		}
		if ("undefined" != typeof XMLHttpRequest && (!a || c.ua.hasCORS)) {
			return new XMLHttpRequest;
		}
		if (!a) {
			try {
				return new (window[["Active"].concat("Object").join("X")])("Microsoft.XMLHTTP");
			}
			catch (b) {
			}
		}
		return null;
	};
	"undefined" != typeof window && c.load(function () {
		g = !0;
	});
	c.defer = function (a) {
		if (!c.ua.webkit || "undefined" != typeof importScripts) {
			return a();
		}
		c.load(function () {
			setTimeout(a, 100);
		});
	};
	c.merge = function (a, b, e, i) {
		var i = i || [], e = "undefined" == typeof e ? 2 : e, d;
		for (d in b) {
			b.hasOwnProperty(d) && 0 > c.indexOf(i, d) && ("object" !== typeof a[d] || !e ? (a[d] = b[d], i.push(b[d])) : c.merge(a[d], b[d], e - 1, i));
		}
		return a;
	};
	c.mixin = function (a, b) {
		c.merge(a.prototype, b.prototype);
	};
	c.inherit = function (a, b) {
		function e() {
		}
		e.prototype = b.prototype;
		a.prototype = new e;
	};
	c.isArray = Array.isArray || function (a) {
		return "[object Array]" === Object.prototype.toString.call(a);
	};
	c.intersect = function (a, b) {
		for (var e = [], i = a.length > b.length ? a : b, d = a.length > b.length ? b : a, f = 0, g = d.length; f < g; f++) {
			~c.indexOf(i, d[f]) && e.push(d[f]);
		}
		return e;
	};
	c.indexOf = function (a, b, e) {
		for (var i = a.length, e = 0 > e ? 0 > e + i ? 0 : e + i : e || 0; e < i && a[e] !== b; e++) {
		}
		return i <= e ? -1 : e;
	};
	c.toArray = function (a) {
		for (var b = [], e = 0, i = a.length; e < i; e++) {
			b.push(a[e]);
		}
		return b;
	};
	c.ua = {};
	c.ua.hasCORS = "undefined" != typeof XMLHttpRequest && function () {
		try {
			var a = new XMLHttpRequest;
		}
		catch (b) {
			return !1;
		}
		return void 0 != a.withCredentials;
	}();
	c.ua.webkit = "undefined" != typeof navigator && /webkit/i.test(navigator.userAgent);
})("undefined" != typeof io ? io : module.exports, this);
(function (b, a) {
	function c() {
	}
	b.EventEmitter = c;
	c.prototype.on = function (b, c) {
		this.$events || (this.$events = {});
		this.$events[b] ? a.util.isArray(this.$events[b]) ? this.$events[b].push(c) : this.$events[b] = [this.$events[b], c] : this.$events[b] = c;
		return this;
	};
	c.prototype.addListener = c.prototype.on;
	c.prototype.once = function (a, b) {
		function c() {
			h.removeListener(a, c);
			b.apply(this, arguments);
		}
		var h = this;
		c.listener = b;
		this.on(a, c);
		return this;
	};
	c.prototype.removeListener = function (b, c) {
		if (this.$events && this.$events[b]) {
			var g = this.$events[b];
			if (a.util.isArray(g)) {
				for (var h = -1, j = 0, e = g.length; j < e; j++) {
					if (g[j] === c || g[j].listener && g[j].listener === c) {
						h = j;
						break;
					}
				}
				if (0 > h) {
					return this;
				}
				g.splice(h, 1);
				g.length || delete this.$events[b];
			} else {
				(g === c || g.listener && g.listener === c) && delete this.$events[b];
			}
		}
		return this;
	};
	c.prototype.removeAllListeners = function (a) {
		this.$events && this.$events[a] && (this.$events[a] = null);
		return this;
	};
	c.prototype.listeners = function (b) {
		this.$events || (this.$events = {});
		this.$events[b] || (this.$events[b] = []);
		a.util.isArray(this.$events[b]) || (this.$events[b] = [this.$events[b]]);
		return this.$events[b];
	};
	c.prototype.emit = function (b) {
		if (!this.$events) {
			return !1;
		}
		var c = this.$events[b];
		if (!c) {
			return !1;
		}
		var g = Array.prototype.slice.call(arguments, 1);
		if ("function" == typeof c) {
			c.apply(this, g);
		} else {
			if (a.util.isArray(c)) {
				for (var c = c.slice(), h = 0, j = c.length; h < j; h++) {
					c[h].apply(this, g);
				}
			} else {
				return !1;
			}
		}
		return !0;
	};
})("undefined" != typeof io ? io : module.exports, "undefined" != typeof io ? io : module.parent.exports);
(function (b, a) {
	function c(a) {
		return 10 > a ? "0" + a : a;
	}
	function d(a) {
		j.lastIndex = 0;
		return j.test(a) ? "\"" + a.replace(j, function (a) {
			var e = k[a];
			return "string" === typeof e ? e : "\\u" + ("0000" + a.charCodeAt(0).toString(16)).slice(-4);
		}) + "\"" : "\"" + a + "\"";
	}
	function f(a, b) {
		var k, g, h, j, o = e, r, q = b[a];
		q instanceof Date && (q = isFinite(a.valueOf()) ? a.getUTCFullYear() + "-" + c(a.getUTCMonth() + 1) + "-" + c(a.getUTCDate()) + "T" + c(a.getUTCHours()) + ":" + c(a.getUTCMinutes()) + ":" + c(a.getUTCSeconds()) + "Z" : null);
		"function" === typeof m && (q = m.call(b, a, q));
		switch (typeof q) {
		  case "string":
			return d(q);
		  case "number":
			return isFinite(q) ? "" + q : "null";
		  case "boolean":
		  case "null":
			return "" + q;
		  case "object":
			if (!q) {
				return "null";
			}
			e += i;
			r = [];
			if ("[object Array]" === Object.prototype.toString.apply(q)) {
				j = q.length;
				for (k = 0; k < j; k += 1) {
					r[k] = f(k, q) || "null";
				}
				h = 0 === r.length ? "[]" : e ? "[\n" + e + r.join(",\n" + e) + "\n" + o + "]" : "[" + r.join(",") + "]";
				e = o;
				return h;
			}
			if (m && "object" === typeof m) {
				j = m.length;
				for (k = 0; k < j; k += 1) {
					"string" === typeof m[k] && (g = m[k], (h = f(g, q)) && r.push(d(g) + (e ? ": " : ":") + h));
				}
			} else {
				for (g in q) {
					Object.prototype.hasOwnProperty.call(q, g) && (h = f(g, q)) && r.push(d(g) + (e ? ": " : ":") + h);
				}
			}
			h = 0 === r.length ? "{}" : e ? "{\n" + e + r.join(",\n" + e) + "\n" + o + "}" : "{" + r.join(",") + "}";
			e = o;
			return h;
		}
	}
	if (a && a.parse) {
		return b.JSON = {parse:a.parse, stringify:a.stringify};
	}
	var g = b.JSON = {}, h = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g, j = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g, e, i, k = {"\b":"\\b", "\t":"\\t", "\n":"\\n", "\f":"\\f", "\r":"\\r", "\"":"\\\"", "\\":"\\\\"}, m;
	g.stringify = function (a, b, c) {
		var d;
		i = e = "";
		if ("number" === typeof c) {
			for (d = 0; d < c; d += 1) {
				i += " ";
			}
		} else {
			"string" === typeof c && (i = c);
		}
		if ((m = b) && "function" !== typeof b && ("object" !== typeof b || "number" !== typeof b.length)) {
			throw Error("JSON.stringify");
		}
		return f("", {"":a});
	};
	g.parse = function (a, e) {
		function b(a, i) {
			var c, d, f = a[i];
			if (f && "object" === typeof f) {
				for (c in f) {
					Object.prototype.hasOwnProperty.call(f, c) && (d = b(f, c), void 0 !== d ? f[c] = d : delete f[c]);
				}
			}
			return e.call(a, i, f);
		}
		var i, a = "" + a;
		h.lastIndex = 0;
		h.test(a) && (a = a.replace(h, function (a) {
			return "\\u" + ("0000" + a.charCodeAt(0).toString(16)).slice(-4);
		}));
		if (/^[\],:{}\s]*$/.test(a.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, "@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, "]").replace(/(?:^|:|,)(?:\s*\[)+/g, ""))) {
			return i = eval("(" + a + ")"), "function" === typeof e ? b({"":i}, "") : i;
		}
		throw new SyntaxError("JSON.parse");
	};
})("undefined" != typeof io ? io : module.exports, "undefined" !== typeof JSON ? JSON : void 0);
(function (b, a) {
	var c = b.parser = {}, d = c.packets = "disconnect,connect,heartbeat,message,json,event,ack,error,noop".split(","), f = c.reasons = ["transport not supported", "client not handshaken", "unauthorized"], g = c.advice = ["reconnect"], h = a.JSON, j = a.util.indexOf;
	c.encodePacket = function (a) {
		var e = j(d, a.type), b = a.id || "", c = a.endpoint || "", s = a.ack, p = null;
		switch (a.type) {
		  case "error":
			var u = a.reason ? j(f, a.reason) : "", a = a.advice ? j(g, a.advice) : "";
			if ("" !== u || "" !== a) {
				p = u + ("" !== a ? "+" + a : "");
			}
			break;
		  case "message":
			"" !== a.data && (p = a.data);
			break;
		  case "event":
			p = {name:a.name};
			a.args && a.args.length && (p.args = a.args);
			p = h.stringify(p);
			break;
		  case "json":
			p = h.stringify(a.data);
			break;
		  case "connect":
			a.qs && (p = a.qs);
			break;
		  case "ack":
			p = a.ackId + (a.args && a.args.length ? "+" + h.stringify(a.args) : "");
		}
		e = [e, b + ("data" == s ? "+" : ""), c];
		null !== p && void 0 !== p && e.push(p);
		return e.join(":");
	};
	c.encodePayload = function (a) {
		var e = "";
		if (1 == a.length) {
			return a[0];
		}
		for (var b = 0, c = a.length; b < c; b++) {
			e += "\ufffd" + a[b].length + "\ufffd" + a[b];
		}
		return e;
	};
	var e = /([^:]+):([0-9]+)?(\+)?:([^:]+)?:?([\s\S]*)?/;
	c.decodePacket = function (a) {
		var b = a.match(e);
		if (!b) {
			return {};
		}
		var c = b[2] || "", a = b[5] || "", j = {type:d[b[1]], endpoint:b[4] || ""};
		c && (j.id = c, j.ack = b[3] ? "data" : !0);
		switch (j.type) {
		  case "error":
			b = a.split("+");
			j.reason = f[b[0]] || "";
			j.advice = g[b[1]] || "";
			break;
		  case "message":
			j.data = a || "";
			break;
		  case "event":
			try {
				var s = h.parse(a);
				j.name = s.name;
				j.args = s.args;
			}
			catch (p) {
			}
			j.args = j.args || [];
			break;
		  case "json":
			try {
				j.data = h.parse(a);
			}
			catch (u) {
			}
			break;
		  case "connect":
			j.qs = a || "";
			break;
		  case "ack":
			if (b = a.match(/^([0-9]+)(\+)?(.*)/)) {
				if (j.ackId = b[1], j.args = [], b[3]) {
					try {
						j.args = b[3] ? h.parse(b[3]) : [];
					}
					catch (v) {
					}
				}
			}
		}
		return j;
	};
	c.decodePayload = function (a) {
		if ("\ufffd" == a.charAt(0)) {
			for (var e = [], b = 1, d = ""; b < a.length; b++) {
				"\ufffd" == a.charAt(b) ? (e.push(c.decodePacket(a.substr(b + 1).substr(0, d))), b += Number(d) + 1, d = "") : d += a.charAt(b);
			}
			return e;
		}
		return [c.decodePacket(a)];
	};
})("undefined" != typeof io ? io : module.exports, "undefined" != typeof io ? io : module.parent.exports);
(function (b, a) {
	function c(a, b) {
		this.socket = a;
		this.sessid = b;
	}
	b.Transport = c;
	a.util.mixin(c, a.EventEmitter);
	c.prototype.onData = function (b) {
		this.clearCloseTimeout();
		(this.socket.connected || this.socket.connecting || this.socket.reconnecting) && this.setCloseTimeout();
		if ("" !== b && (b = a.parser.decodePayload(b)) && b.length) {
			for (var c = 0, g = b.length; c < g; c++) {
				this.onPacket(b[c]);
			}
		}
		return this;
	};
	c.prototype.onPacket = function (a) {
		this.socket.setHeartbeatTimeout();
		if ("heartbeat" == a.type) {
			return this.onHeartbeat();
		}
		if ("connect" == a.type && "" == a.endpoint) {
			this.onConnect();
		}
		"error" == a.type && "reconnect" == a.advice && (this.open = !1);
		this.socket.onPacket(a);
		return this;
	};
	c.prototype.setCloseTimeout = function () {
		if (!this.closeTimeout) {
			var a = this;
			this.closeTimeout = setTimeout(function () {
				a.onDisconnect();
			}, this.socket.closeTimeout);
		}
	};
	c.prototype.onDisconnect = function () {
		this.close && this.open && this.close();
		this.clearTimeouts();
		this.socket.onDisconnect();
		return this;
	};
	c.prototype.onConnect = function () {
		this.socket.onConnect();
		return this;
	};
	c.prototype.clearCloseTimeout = function () {
		this.closeTimeout && (clearTimeout(this.closeTimeout), this.closeTimeout = null);
	};
	c.prototype.clearTimeouts = function () {
		this.clearCloseTimeout();
		this.reopenTimeout && clearTimeout(this.reopenTimeout);
	};
	c.prototype.packet = function (b) {
		this.send(a.parser.encodePacket(b));
	};
	c.prototype.onHeartbeat = function () {
		this.packet({type:"heartbeat"});
	};
	c.prototype.onOpen = function () {
		this.open = !0;
		this.clearCloseTimeout();
		this.socket.onOpen();
	};
	c.prototype.onClose = function () {
		this.open = !1;
		this.socket.onClose();
		this.onDisconnect();
	};
	c.prototype.prepareUrl = function () {
		var b = this.socket.options;
		return this.scheme() + "://" + b.host + ":" + b.port + "/" + b.resource + "/" + a.protocol + "/" + this.name + "/" + this.sessid;
	};
	c.prototype.ready = function (a, b) {
		b.call(this);
	};
})("undefined" != typeof io ? io : module.exports, "undefined" != typeof io ? io : module.parent.exports);
(function (b, a, c) {
	function d(b) {
		this.options = {port:80, secure:!1, document:"document" in c ? document : !1, resource:"socket.io", transports:a.transports, "connect timeout":10000, "try multiple transports":!0, reconnect:!0, "reconnection delay":500, "reconnection limit":Infinity, "reopen delay":3000, "max reconnection attempts":10, "sync disconnect on unload":!0, "auto connect":!0, "flash policy port":10843};
		a.util.merge(this.options, b);
		this.reconnecting = this.connecting = this.open = this.connected = !1;
		this.namespaces = {};
		this.buffer = [];
		this.doBuffer = !1;
		if (this.options["sync disconnect on unload"] && (!this.isXDomain() || a.util.ua.hasCORS)) {
			var d = this;
			a.util.on(c, "unload", function () {
				d.disconnectSync();
			}, !1);
		}
		this.options["auto connect"] && this.connect();
	}
	function f() {
	}
	b.Socket = d;
	a.util.mixin(d, a.EventEmitter);
	d.prototype.of = function (b) {
		this.namespaces[b] || (this.namespaces[b] = new a.SocketNamespace(this, b), "" !== b && this.namespaces[b].packet({type:"connect"}));
		return this.namespaces[b];
	};
	d.prototype.publish = function () {
		this.emit.apply(this, arguments);
		var a, b;
		for (b in this.namespaces) {
			this.namespaces.hasOwnProperty(b) && (a = this.of(b), a.$emit.apply(a, arguments));
		}
	};
	d.prototype.handshake = function (b) {
		function c(a) {
			if (a instanceof Error) {
				d.onError(a.message);
			} else {
				b.apply(null, a.split(":"));
			}
		}
		var d = this, e = this.options, e = ["http" + (e.secure ? "s" : "") + ":/", e.host + ":" + e.port, e.resource, a.protocol, a.util.query(this.options.query, "t=" + +new Date)].join("/");
		if (this.isXDomain() && !a.util.ua.hasCORS) {
			var i = document.getElementsByTagName("script")[0], k = document.createElement("script");
			k.src = e + "&jsonp=" + a.j.length;
			i.parentNode.insertBefore(k, i);
			a.j.push(function (a) {
				c(a);
				k.parentNode.removeChild(k);
			});
		} else {
			var m = a.util.request();
			m.open("GET", e, !0);
			m.withCredentials = !0;
			m.onreadystatechange = function () {
				4 == m.readyState && (m.onreadystatechange = f, 200 == m.status ? c(m.responseText) : !d.reconnecting && d.onError(m.responseText));
			};
			m.send(null);
		}
	};
	d.prototype.getTransport = function (b) {
		for (var b = b || this.transports, c = 0, d; d = b[c]; c++) {
			if (a.Transport[d] && a.Transport[d].check(this) && (!this.isXDomain() || a.Transport[d].xdomainCheck())) {
				return new a.Transport[d](this, this.sessionid);
			}
		}
		return null;
	};
	d.prototype.connect = function (b) {
		if (this.connecting) {
			return this;
		}
		var c = this;
		this.handshake(function (d, e, i, f) {
			function m(a) {
				c.transport && c.transport.clearTimeouts();
				c.transport = c.getTransport(a);
				if (!c.transport) {
					return c.publish("connect_failed");
				}
				c.transport.ready(c, function () {
					c.connecting = !0;
					c.publish("connecting", c.transport.name);
					c.transport.open();
					c.options["connect timeout"] && (c.connectTimeoutTimer = setTimeout(function () {
						if (!c.connected && (c.connecting = !1, c.options["try multiple transports"])) {
							c.remainingTransports || (c.remainingTransports = c.transports.slice(0));
							for (var a = c.remainingTransports; 0 < a.length && a.splice(0, 1)[0] != c.transport.name; ) {
							}
							a.length ? m(a) : c.publish("connect_failed");
						}
					}, c.options["connect timeout"]));
				});
			}
			c.sessionid = d;
			c.closeTimeout = 1000 * i;
			c.heartbeatTimeout = 1000 * e;
			c.transports = f ? a.util.intersect(f.split(","), c.options.transports) : c.options.transports;
			c.setHeartbeatTimeout();
			m(c.transports);
			c.once("connect", function () {
				clearTimeout(c.connectTimeoutTimer);
				b && "function" == typeof b && b();
			});
		});
		return this;
	};
	d.prototype.setHeartbeatTimeout = function () {
		clearTimeout(this.heartbeatTimeoutTimer);
		var a = this;
		this.heartbeatTimeoutTimer = setTimeout(function () {
			a.transport.onClose();
		}, this.heartbeatTimeout);
	};
	d.prototype.packet = function (a) {
		this.connected && !this.doBuffer ? this.transport.packet(a) : this.buffer.push(a);
		return this;
	};
	d.prototype.setBuffer = function (a) {
		this.doBuffer = a;
		!a && this.connected && this.buffer.length && (this.transport.payload(this.buffer), this.buffer = []);
	};
	d.prototype.disconnect = function () {
		if (this.connected || this.connecting) {
			this.open && this.of("").packet({type:"disconnect"}), this.onDisconnect("booted");
		}
		return this;
	};
	d.prototype.disconnectSync = function () {
		a.util.request().open("GET", this.resource + "/" + a.protocol + "/" + this.sessionid, !0);
		this.onDisconnect("booted");
	};
	d.prototype.isXDomain = function () {
		var a = c.location.port || ("https:" == c.location.protocol ? 443 : 80);
		return this.options.host !== c.location.hostname || this.options.port != a;
	};
	d.prototype.onConnect = function () {
		this.connected || (this.connected = !0, this.connecting = !1, this.doBuffer || this.setBuffer(!1), this.emit("connect"));
	};
	d.prototype.onOpen = function () {
		this.open = !0;
	};
	d.prototype.onClose = function () {
		this.open = !1;
		clearTimeout(this.heartbeatTimeoutTimer);
	};
	d.prototype.onPacket = function (a) {
		this.of(a.endpoint).onPacket(a);
	};
	d.prototype.onError = function (a) {
		if (a && a.advice && "reconnect" === a.advice && (this.connected || this.connecting)) {
			this.disconnect(), this.options.reconnect && this.reconnect();
		}
		this.publish("error", a && a.reason ? a.reason : a);
	};
	d.prototype.onDisconnect = function (a) {
		var b = this.connected, c = this.connecting;
		this.open = this.connecting = this.connected = !1;
		if (b || c) {
			this.transport.close(), this.transport.clearTimeouts(), b && (this.publish("disconnect", a), "booted" != a && this.options.reconnect && !this.reconnecting && this.reconnect());
		}
	};
	d.prototype.reconnect = function () {
		function a() {
			if (c.connected) {
				for (var e in c.namespaces) {
					c.namespaces.hasOwnProperty(e) && "" !== e && c.namespaces[e].packet({type:"connect"});
				}
				c.publish("reconnect", c.transport.name, c.reconnectionAttempts);
			}
			clearTimeout(c.reconnectionTimer);
			c.removeListener("connect_failed", b);
			c.removeListener("connect", b);
			c.reconnecting = !1;
			delete c.reconnectionAttempts;
			delete c.reconnectionDelay;
			delete c.reconnectionTimer;
			delete c.redoTransports;
			c.options["try multiple transports"] = i;
		}
		function b() {
			if (c.reconnecting) {
				if (c.connected) {
					return a();
				}
				if (c.connecting && c.reconnecting) {
					return c.reconnectionTimer = setTimeout(b, 1000);
				}
				c.reconnectionAttempts++ >= e ? c.redoTransports ? (c.publish("reconnect_failed"), a()) : (c.on("connect_failed", b), c.options["try multiple transports"] = !0, c.transport = c.getTransport(), c.redoTransports = !0, c.connect()) : (c.reconnectionDelay < d && (c.reconnectionDelay *= 2), c.connect(), c.publish("reconnecting", c.reconnectionDelay, c.reconnectionAttempts), c.reconnectionTimer = setTimeout(b, c.reconnectionDelay));
			}
		}
		this.reconnecting = !0;
		this.reconnectionAttempts = 0;
		this.reconnectionDelay = this.options["reconnection delay"];
		var c = this, e = this.options["max reconnection attempts"], i = this.options["try multiple transports"], d = this.options["reconnection limit"];
		this.options["try multiple transports"] = !1;
		this.reconnectionTimer = setTimeout(b, this.reconnectionDelay);
		this.on("connect", b);
	};
})("undefined" != typeof io ? io : module.exports, "undefined" != typeof io ? io : module.parent.exports, this);
(function (b, a) {
	function c(a, b) {
		this.socket = a;
		this.name = b || "";
		this.flags = {};
		this.json = new d(this, "json");
		this.ackPackets = 0;
		this.acks = {};
	}
	function d(a, b) {
		this.namespace = a;
		this.name = b;
	}
	b.SocketNamespace = c;
	a.util.mixin(c, a.EventEmitter);
	c.prototype.$emit = a.EventEmitter.prototype.emit;
	c.prototype.of = function () {
		return this.socket.of.apply(this.socket, arguments);
	};
	c.prototype.packet = function (a) {
		a.endpoint = this.name;
		this.socket.packet(a);
		this.flags = {};
		return this;
	};
	c.prototype.send = function (a, b) {
		var c = {type:this.flags.json ? "json" : "message", data:a};
		"function" == typeof b && (c.id = ++this.ackPackets, c.ack = !0, this.acks[c.id] = b);
		return this.packet(c);
	};
	c.prototype.emit = function (a) {
		var b = Array.prototype.slice.call(arguments, 1), c = b[b.length - 1], d = {type:"event", name:a};
		"function" == typeof c && (d.id = ++this.ackPackets, d.ack = "data", this.acks[d.id] = c, b = b.slice(0, b.length - 1));
		d.args = b;
		return this.packet(d);
	};
	c.prototype.disconnect = function () {
		"" === this.name ? this.socket.disconnect() : (this.packet({type:"disconnect"}), this.$emit("disconnect"));
		return this;
	};
	c.prototype.onPacket = function (b) {
		function c() {
			d.packet({type:"ack", args:a.util.toArray(arguments), ackId:b.id});
		}
		var d = this;
		switch (b.type) {
		  case "connect":
			this.$emit("connect");
			break;
		  case "disconnect":
			if ("" === this.name) {
				this.socket.onDisconnect(b.reason || "booted");
			} else {
				this.$emit("disconnect", b.reason);
			}
			break;
		  case "message":
		  case "json":
			var j = ["message", b.data];
			"data" == b.ack ? j.push(c) : b.ack && this.packet({type:"ack", ackId:b.id});
			this.$emit.apply(this, j);
			break;
		  case "event":
			j = [b.name].concat(b.args);
			"data" == b.ack && j.push(c);
			this.$emit.apply(this, j);
			break;
		  case "ack":
			this.acks[b.ackId] && (this.acks[b.ackId].apply(this, b.args), delete this.acks[b.ackId]);
			break;
		  case "error":
			if (b.advice) {
				this.socket.onError(b);
			} else {
				"unauthorized" == b.reason ? this.$emit("connect_failed", b.reason) : this.$emit("error", b.reason);
			}
		}
	};
	d.prototype.send = function () {
		this.namespace.flags[this.name] = !0;
		this.namespace.send.apply(this.namespace, arguments);
	};
	d.prototype.emit = function () {
		this.namespace.flags[this.name] = !0;
		this.namespace.emit.apply(this.namespace, arguments);
	};
})("undefined" != typeof io ? io : module.exports, "undefined" != typeof io ? io : module.parent.exports);
(function (b, a, c) {
	function d(b) {
		a.Transport.apply(this, arguments);
	}
	b.websocket = d;
	a.util.inherit(d, a.Transport);
	d.prototype.name = "websocket";
	d.prototype.open = function () {
		var b = a.util.query(this.socket.options.query), d = this, h;
		h || (h = c.MozWebSocket || c.WebSocket);
		this.websocket = new h(this.prepareUrl() + b);
		this.websocket.onopen = function () {
			d.onOpen();
			d.socket.setBuffer(!1);
		};
		this.websocket.onmessage = function (a) {
			d.onData(a.data);
		};
		this.websocket.onclose = function () {
			d.onClose();
			d.socket.setBuffer(!0);
		};
		this.websocket.onerror = function (a) {
			d.onError(a);
		};
		return this;
	};
	d.prototype.send = function (a) {
		this.websocket.send(a);
		return this;
	};
	d.prototype.payload = function (a) {
		for (var b = 0, c = a.length; b < c; b++) {
			this.packet(a[b]);
		}
		return this;
	};
	d.prototype.close = function () {
		this.websocket.close();
		return this;
	};
	d.prototype.onError = function (a) {
		this.socket.onError(a);
	};
	d.prototype.scheme = function () {
		return this.socket.options.secure ? "wss" : "ws";
	};
	d.check = function () {
		return "WebSocket" in c && !("__addTask" in WebSocket) || "MozWebSocket" in c;
	};
	d.xdomainCheck = function () {
		return !0;
	};
	a.transports.push("websocket");
})("undefined" != typeof io ? io.Transport : module.exports, "undefined" != typeof io ? io : module.parent.exports, this);
(function (b, a) {
	function c() {
		a.Transport.websocket.apply(this, arguments);
	}
	b.flashsocket = c;
	a.util.inherit(c, a.Transport.websocket);
	c.prototype.name = "flashsocket";
	c.prototype.open = function () {
		var b = this, c = arguments;
		WebSocket.__addTask(function () {
			a.Transport.websocket.prototype.open.apply(b, c);
		});
		return this;
	};
	c.prototype.send = function () {
		var b = this, c = arguments;
		WebSocket.__addTask(function () {
			a.Transport.websocket.prototype.send.apply(b, c);
		});
		return this;
	};
	c.prototype.close = function () {
		WebSocket.__tasks.length = 0;
		a.Transport.websocket.prototype.close.call(this);
		return this;
	};
	c.prototype.ready = function (b, f) {
		function g() {
			var a = b.options, e = a["flash policy port"], i = ["http" + (a.secure ? "s" : "") + ":/", a.host + ":" + a.port, a.resource, "static/flashsocket", "WebSocketMain" + (b.isXDomain() ? "Insecure" : "") + ".swf"];
			c.loaded || ("undefined" === typeof WEB_SOCKET_SWF_LOCATION && (WEB_SOCKET_SWF_LOCATION = i.join("/")), 843 !== e && WebSocket.loadFlashPolicyFile("xmlsocket://" + a.host + ":" + e), WebSocket.__initialize(), c.loaded = !0);
			f.call(h);
		}
		var h = this;
		if (document.body) {
			return g();
		}
		a.util.load(g);
	};
	c.check = function () {
		return "undefined" == typeof WebSocket || !("__initialize" in WebSocket) || !swfobject ? !1 : 10 <= swfobject.getFlashPlayerVersion().major;
	};
	c.xdomainCheck = function () {
		return !0;
	};
	"undefined" != typeof window && (WEB_SOCKET_DISABLE_AUTO_INITIALIZATION = !0);
	a.transports.push("flashsocket");
})("undefined" != typeof io ? io.Transport : module.exports, "undefined" != typeof io ? io : module.parent.exports);
if ("undefined" != typeof window) {
	var swfobject = function () {
		function b() {
			if (!B) {
				try {
					var a = n.getElementsByTagName("body")[0].appendChild(n.createElement("span"));
					a.parentNode.removeChild(a);
				}
				catch (b) {
					return;
				}
				B = !0;
				for (var a = E.length, e = 0; e < a; e++) {
					E[e]();
				}
			}
		}
		function a(a) {
			B ? a() : E[E.length] = a;
		}
		function c(a) {
			if (typeof x.addEventListener != o) {
				x.addEventListener("load", a, !1);
			} else {
				if (typeof n.addEventListener != o) {
					n.addEventListener("load", a, !1);
				} else {
					if (typeof x.attachEvent != o) {
						s(x, "onload", a);
					} else {
						if ("function" == typeof x.onload) {
							var b = x.onload;
							x.onload = function () {
								b();
								a();
							};
						} else {
							x.onload = a;
						}
					}
				}
			}
		}
		function d() {
			var a = n.getElementsByTagName("body")[0], b = n.createElement(r);
			b.setAttribute("type", q);
			var e = a.appendChild(b);
			if (e) {
				var c = 0;
				(function () {
					if (typeof e.GetVariable != o) {
						var i = e.GetVariable("$version");
						i && (i = i.split(" ")[1].split(","), l.pv = [parseInt(i[0], 10), parseInt(i[1], 10), parseInt(i[2], 10)]);
					} else {
						if (10 > c) {
							c++;
							setTimeout(arguments.callee, 10);
							return;
						}
					}
					a.removeChild(b);
					e = null;
					f();
				})();
			} else {
				f();
			}
		}
		function f() {
			var a = z.length;
			if (0 < a) {
				for (var b = 0; b < a; b++) {
					var c = z[b].id, i = z[b].callbackFn, d = {success:!1, id:c};
					if (0 < l.pv[0]) {
						var f = t(c);
						if (f) {
							if (p(z[b].swfVersion) && !(l.wk && 312 > l.wk)) {
								v(c, !0), i && (d.success = !0, d.ref = g(c), i(d));
							} else {
								if (z[b].expressInstall && h()) {
									d = {};
									d.data = z[b].expressInstall;
									d.width = f.getAttribute("width") || "0";
									d.height = f.getAttribute("height") || "0";
									f.getAttribute("class") && (d.styleclass = f.getAttribute("class"));
									f.getAttribute("align") && (d.align = f.getAttribute("align"));
									for (var k = {}, f = f.getElementsByTagName("param"), s = f.length, m = 0; m < s; m++) {
										"movie" != f[m].getAttribute("name").toLowerCase() && (k[f[m].getAttribute("name")] = f[m].getAttribute("value"));
									}
									j(d, k, c, i);
								} else {
									e(f), i && i(d);
								}
							}
						}
					} else {
						if (v(c, !0), i) {
							if ((c = g(c)) && typeof c.SetVariable != o) {
								d.success = !0, d.ref = c;
							}
							i(d);
						}
					}
				}
			}
		}
		function g(a) {
			var b = null;
			if ((a = t(a)) && "OBJECT" == a.nodeName) {
				typeof a.SetVariable != o ? b = a : (a = a.getElementsByTagName(r)[0]) && (b = a);
			}
			return b;
		}
		function h() {
			return !F && p("6.0.65") && (l.win || l.mac) && !(l.wk && 312 > l.wk);
		}
		function j(a, b, e, c) {
			F = !0;
			I = c || null;
			K = {success:!1, id:e};
			var d = t(e);
			if (d) {
				"OBJECT" == d.nodeName ? (D = i(d), G = null) : (D = d, G = e);
				a.id = M;
				if (typeof a.width == o || !/%$/.test(a.width) && 310 > parseInt(a.width, 10)) {
					a.width = "310";
				}
				if (typeof a.height == o || !/%$/.test(a.height) && 137 > parseInt(a.height, 10)) {
					a.height = "137";
				}
				n.title = n.title.slice(0, 47) + " - Flash Player Installation";
				c = l.ie && l.win ? ["Active"].concat("").join("X") : "PlugIn";
				c = "MMredirectURL=" + x.location.toString().replace(/&/g, "%26") + "&MMplayerType=" + c + "&MMdoctitle=" + n.title;
				b.flashvars = typeof b.flashvars != o ? b.flashvars + ("&" + c) : c;
				l.ie && l.win && 4 != d.readyState && (c = n.createElement("div"), e += "SWFObjectNew", c.setAttribute("id", e), d.parentNode.insertBefore(c, d), d.style.display = "none", function () {
					d.readyState == 4 ? d.parentNode.removeChild(d) : setTimeout(arguments.callee, 10);
				}());
				k(a, b, e);
			}
		}
		function e(a) {
			if (l.ie && l.win && 4 != a.readyState) {
				var b = n.createElement("div");
				a.parentNode.insertBefore(b, a);
				b.parentNode.replaceChild(i(a), b);
				a.style.display = "none";
				(function () {
					4 == a.readyState ? a.parentNode.removeChild(a) : setTimeout(arguments.callee, 10);
				})();
			} else {
				a.parentNode.replaceChild(i(a), a);
			}
		}
		function i(a) {
			var b = n.createElement("div");
			if (l.win && l.ie) {
				b.innerHTML = a.innerHTML;
			} else {
				if (a = a.getElementsByTagName(r)[0]) {
					if (a = a.childNodes) {
						for (var e = a.length, c = 0; c < e; c++) {
							!(1 == a[c].nodeType && "PARAM" == a[c].nodeName) && 8 != a[c].nodeType && b.appendChild(a[c].cloneNode(!0));
						}
					}
				}
			}
			return b;
		}
		function k(a, b, e) {
			var c, i = t(e);
			if (l.wk && 312 > l.wk) {
				return c;
			}
			if (i) {
				if (typeof a.id == o && (a.id = e), l.ie && l.win) {
					var d = "", f;
					for (f in a) {
						a[f] != Object.prototype[f] && ("data" == f.toLowerCase() ? b.movie = a[f] : "styleclass" == f.toLowerCase() ? d += " class=\"" + a[f] + "\"" : "classid" != f.toLowerCase() && (d += " " + f + "=\"" + a[f] + "\""));
					}
					f = "";
					for (var k in b) {
						b[k] != Object.prototype[k] && (f += "<param name=\"" + k + "\" value=\"" + b[k] + "\" />");
					}
					i.outerHTML = "<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\"" + d + ">" + f + "</object>";
					H[H.length] = a.id;
					c = t(a.id);
				} else {
					k = n.createElement(r);
					k.setAttribute("type", q);
					for (var g in a) {
						a[g] != Object.prototype[g] && ("styleclass" == g.toLowerCase() ? k.setAttribute("class", a[g]) : "classid" != g.toLowerCase() && k.setAttribute(g, a[g]));
					}
					for (d in b) {
						b[d] != Object.prototype[d] && "movie" != d.toLowerCase() && (a = k, f = d, g = b[d], e = n.createElement("param"), e.setAttribute("name", f), e.setAttribute("value", g), a.appendChild(e));
					}
					i.parentNode.replaceChild(k, i);
					c = k;
				}
			}
			return c;
		}
		function m(a) {
			var b = t(a);
			b && "OBJECT" == b.nodeName && (l.ie && l.win ? (b.style.display = "none", function () {
				if (4 == b.readyState) {
					var e = t(a);
					if (e) {
						for (var c in e) {
							"function" == typeof e[c] && (e[c] = null);
						}
						e.parentNode.removeChild(e);
					}
				} else {
					setTimeout(arguments.callee, 10);
				}
			}()) : b.parentNode.removeChild(b));
		}
		function t(a) {
			var b = null;
			try {
				b = n.getElementById(a);
			}
			catch (e) {
			}
			return b;
		}
		function s(a, b, e) {
			a.attachEvent(b, e);
			C[C.length] = [a, b, e];
		}
		function p(a) {
			var b = l.pv, a = a.split(".");
			a[0] = parseInt(a[0], 10);
			a[1] = parseInt(a[1], 10) || 0;
			a[2] = parseInt(a[2], 10) || 0;
			return b[0] > a[0] || b[0] == a[0] && b[1] > a[1] || b[0] == a[0] && b[1] == a[1] && b[2] >= a[2] ? !0 : !1;
		}
		function u(a, b, e, c) {
			if (!l.ie || !l.mac) {
				var i = n.getElementsByTagName("head")[0];
				if (i) {
					e = e && "string" == typeof e ? e : "screen";
					c && (J = y = null);
					if (!y || J != e) {
						c = n.createElement("style"), c.setAttribute("type", "text/css"), c.setAttribute("media", e), y = i.appendChild(c), l.ie && l.win && typeof n.styleSheets != o && 0 < n.styleSheets.length && (y = n.styleSheets[n.styleSheets.length - 1]), J = e;
					}
					l.ie && l.win ? y && typeof y.addRule == r && y.addRule(a, b) : y && typeof n.createTextNode != o && y.appendChild(n.createTextNode(a + " {" + b + "}"));
				}
			}
		}
		function v(a, b) {
			if (N) {
				var e = b ? "visible" : "hidden";
				B && t(a) ? t(a).style.visibility = e : u("#" + a, "visibility:" + e);
			}
		}
		function w(a) {
			return null != /[\\\"<>\.;]/.exec(a) && typeof encodeURIComponent != o ? encodeURIComponent(a) : a;
		}
		var o = "undefined", r = "object", q = "application/x-shockwave-flash", M = "SWFObjectExprInst", x = window, n = document, A = navigator, O = !1, E = [function () {
			O ? d() : f();
		}], z = [], H = [], C = [], D, G, I, K, B = !1, F = !1, y, J, N = !0, l = function () {
			var a = typeof n.getElementById != o && typeof n.getElementsByTagName != o && typeof n.createElement != o, b = A.userAgent.toLowerCase(), e = A.platform.toLowerCase(), c = e ? /win/.test(e) : /win/.test(b), e = e ? /mac/.test(e) : /mac/.test(b), b = /webkit/.test(b) ? parseFloat(b.replace(/^.*webkit\/(\d+(\.\d+)?).*$/, "$1")) : !1, i = !+"\v1", d = [0, 0, 0], f = null;
			if (typeof A.plugins != o && typeof A.plugins["Shockwave Flash"] == r) {
				if ((f = A.plugins["Shockwave Flash"].description) && !(typeof A.mimeTypes != o && A.mimeTypes[q] && !A.mimeTypes[q].enabledPlugin)) {
					O = !0, i = !1, f = f.replace(/^.*\s+(\S+\s+\S+$)/, "$1"), d[0] = parseInt(f.replace(/^(.*)\..*$/, "$1"), 10), d[1] = parseInt(f.replace(/^.*\.(.*)\s.*$/, "$1"), 10), d[2] = /[a-zA-Z]/.test(f) ? parseInt(f.replace(/^.*[a-zA-Z]+(.*)$/, "$1"), 10) : 0;
				}
			} else {
				if (typeof x[["Active"].concat("Object").join("X")] != o) {
					try {
						var k = new (window[["Active"].concat("Object").join("X")])("ShockwaveFlash.ShockwaveFlash");
						if (k && (f = k.GetVariable("$version"))) {
							i = !0, f = f.split(" ")[1].split(","), d = [parseInt(f[0], 10), parseInt(f[1], 10), parseInt(f[2], 10)];
						}
					}
					catch (g) {
					}
				}
			}
			return {w3:a, pv:d, wk:b, ie:i, win:c, mac:e};
		}();
		(function () {
			l.w3 && ((typeof n.readyState != o && "complete" == n.readyState || typeof n.readyState == o && (n.getElementsByTagName("body")[0] || n.body)) && b(), B || (typeof n.addEventListener != o && n.addEventListener("DOMContentLoaded", b, !1), l.ie && l.win && (n.attachEvent("onreadystatechange", function () {
				"complete" == n.readyState && (n.detachEvent("onreadystatechange", arguments.callee), b());
			}), x == top && function () {
				if (!B) {
					try {
						n.documentElement.doScroll("left");
					}
					catch (a) {
						setTimeout(arguments.callee, 0);
						return;
					}
					b();
				}
			}()), l.wk && function () {
				B || (/loaded|complete/.test(n.readyState) ? b() : setTimeout(arguments.callee, 0));
			}(), c(b)));
		})();
		(function () {
			l.ie && l.win && window.attachEvent("onunload", function () {
				for (var a = C.length, b = 0; b < a; b++) {
					C[b][0].detachEvent(C[b][1], C[b][2]);
				}
				a = H.length;
				for (b = 0; b < a; b++) {
					m(H[b]);
				}
				for (var e in l) {
					l[e] = null;
				}
				l = null;
				for (var c in swfobject) {
					swfobject[c] = null;
				}
				swfobject = null;
			});
		})();
		return {registerObject:function (a, b, e, c) {
			if (l.w3 && a && b) {
				var i = {};
				i.id = a;
				i.swfVersion = b;
				i.expressInstall = e;
				i.callbackFn = c;
				z[z.length] = i;
				v(a, !1);
			} else {
				c && c({success:!1, id:a});
			}
		}, getObjectById:function (a) {
			if (l.w3) {
				return g(a);
			}
		}, embedSWF:function (b, e, c, i, d, f, g, t, s, m) {
			var n = {success:!1, id:e};
			l.w3 && !(l.wk && 312 > l.wk) && b && e && c && i && d ? (v(e, !1), a(function () {
				c += "";
				i += "";
				var a = {};
				if (s && typeof s === r) {
					for (var l in s) {
						a[l] = s[l];
					}
				}
				a.data = b;
				a.width = c;
				a.height = i;
				l = {};
				if (t && typeof t === r) {
					for (var u in t) {
						l[u] = t[u];
					}
				}
				if (g && typeof g === r) {
					for (var q in g) {
						l.flashvars = typeof l.flashvars != o ? l.flashvars + ("&" + q + "=" + g[q]) : q + "=" + g[q];
					}
				}
				if (p(d)) {
					u = k(a, l, e), a.id == e && v(e, !0), n.success = !0, n.ref = u;
				} else {
					if (f && h()) {
						a.data = f;
						j(a, l, e, m);
						return;
					}
					v(e, !0);
				}
				m && m(n);
			})) : m && m(n);
		}, switchOffAutoHideShow:function () {
			N = !1;
		}, ua:l, getFlashPlayerVersion:function () {
			return {major:l.pv[0], minor:l.pv[1], release:l.pv[2]};
		}, hasFlashPlayerVersion:p, createSWF:function (a, b, e) {
			if (l.w3) {
				return k(a, b, e);
			}
		}, showExpressInstall:function (a, b, e, c) {
			l.w3 && h() && j(a, b, e, c);
		}, removeSWF:function (a) {
			l.w3 && m(a);
		}, createCSS:function (a, b, e, c) {
			l.w3 && u(a, b, e, c);
		}, addDomLoadEvent:a, addLoadEvent:c, getQueryParamValue:function (a) {
			var b = n.location.search || n.location.hash;
			if (b) {
				/\?/.test(b) && (b = b.split("?")[1]);
				if (null == a) {
					return w(b);
				}
				for (var b = b.split("&"), e = 0; e < b.length; e++) {
					if (b[e].substring(0, b[e].indexOf("=")) == a) {
						return w(b[e].substring(b[e].indexOf("=") + 1));
					}
				}
			}
			return "";
		}, expressInstallCallback:function () {
			if (F) {
				var a = t(M);
				a && D && (a.parentNode.replaceChild(D, a), G && (v(G, !0), l.ie && l.win && (D.style.display = "block")), I && I(K));
				F = !1;
			}
		}};
	}();
}
(function () {
	if (!("undefined" == typeof window || window.WebSocket)) {
		var b = window.console;
		if (!b || !b.log || !b.error) {
			b = {log:function () {
			}, error:function () {
			}};
		}
		swfobject.hasFlashPlayerVersion("10.0.0") ? ("file:" == location.protocol && b.error("WARNING: web-socket-js doesn't work in file:///... URL unless you set Flash Security Settings properly. Open the page via Web server i.e. http://..."), WebSocket = function (a, b, d, f, g) {
			var h = this;
			h.__id = WebSocket.__nextId++;
			WebSocket.__instances[h.__id] = h;
			h.readyState = WebSocket.CONNECTING;
			h.bufferedAmount = 0;
			h.__events = {};
			b ? "string" == typeof b && (b = [b]) : b = [];
			setTimeout(function () {
				WebSocket.__addTask(function () {
					WebSocket.__flash.create(h.__id, a, b, d || null, f || 0, g || null);
				});
			}, 0);
		}, WebSocket.prototype.send = function (a) {
			if (this.readyState == WebSocket.CONNECTING) {
				throw "INVALID_STATE_ERR: Web Socket connection has not been established";
			}
			a = WebSocket.__flash.send(this.__id, encodeURIComponent(a));
			if (0 > a) {
				return !0;
			}
			this.bufferedAmount += a;
			return !1;
		}, WebSocket.prototype.close = function () {
			this.readyState == WebSocket.CLOSED || this.readyState == WebSocket.CLOSING || (this.readyState = WebSocket.CLOSING, WebSocket.__flash.close(this.__id));
		}, WebSocket.prototype.addEventListener = function (a, b) {
			a in this.__events || (this.__events[a] = []);
			this.__events[a].push(b);
		}, WebSocket.prototype.removeEventListener = function (a, b) {
			if (a in this.__events) {
				for (var d = this.__events[a], f = d.length - 1; 0 <= f; --f) {
					if (d[f] === b) {
						d.splice(f, 1);
						break;
					}
				}
			}
		}, WebSocket.prototype.dispatchEvent = function (a) {
			for (var b = this.__events[a.type] || [], d = 0; d < b.length; ++d) {
				b[d](a);
			}
			(b = this["on" + a.type]) && b(a);
		}, WebSocket.prototype.__handleEvent = function (a) {
			"readyState" in a && (this.readyState = a.readyState);
			"protocol" in a && (this.protocol = a.protocol);
			if ("open" == a.type || "error" == a.type) {
				a = this.__createSimpleEvent(a.type);
			} else {
				if ("close" == a.type) {
					a = this.__createSimpleEvent("close");
				} else {
					if ("message" == a.type) {
						a = this.__createMessageEvent("message", decodeURIComponent(a.message));
					} else {
						throw "unknown event type: " + a.type;
					}
				}
			}
			this.dispatchEvent(a);
		}, WebSocket.prototype.__createSimpleEvent = function (a) {
			if (document.createEvent && window.Event) {
				var b = document.createEvent("Event");
				b.initEvent(a, !1, !1);
				return b;
			}
			return {type:a, bubbles:!1, cancelable:!1};
		}, WebSocket.prototype.__createMessageEvent = function (a, b) {
			if (document.createEvent && window.MessageEvent && !window.opera) {
				var d = document.createEvent("MessageEvent");
				d.initMessageEvent("message", !1, !1, b, null, null, window, null);
				return d;
			}
			return {type:a, data:b, bubbles:!1, cancelable:!1};
		}, WebSocket.CONNECTING = 0, WebSocket.OPEN = 1, WebSocket.CLOSING = 2, WebSocket.CLOSED = 3, WebSocket.__flash = null, WebSocket.__instances = {}, WebSocket.__tasks = [], WebSocket.__nextId = 0, WebSocket.loadFlashPolicyFile = function (a) {
			WebSocket.__addTask(function () {
				WebSocket.__flash.loadManualPolicyFile(a);
			});
		}, WebSocket.__initialize = function () {
			if (!WebSocket.__flash) {
				if (WebSocket.__swfLocation && (window.WEB_SOCKET_SWF_LOCATION = WebSocket.__swfLocation), window.WEB_SOCKET_SWF_LOCATION) {
					var a = document.createElement("div");
					a.id = "webSocketContainer";
					a.style.position = "absolute";
					WebSocket.__isFlashLite() ? (a.style.left = "0px", a.style.top = "0px") : (a.style.left = "-100px", a.style.top = "-100px");
					var c = document.createElement("div");
					c.id = "webSocketFlash";
					a.appendChild(c);
					document.body.appendChild(a);
					swfobject.embedSWF(WEB_SOCKET_SWF_LOCATION, "webSocketFlash", "1", "1", "10.0.0", null, null, {hasPriority:!0, swliveconnect:!0, allowScriptAccess:"always"}, null, function (a) {
						a.success || b.error("[WebSocket] swfobject.embedSWF failed");
					});
				} else {
					b.error("[WebSocket] set WEB_SOCKET_SWF_LOCATION to location of WebSocketMain.swf");
				}
			}
		}, WebSocket.__onFlashInitialized = function () {
			setTimeout(function () {
				WebSocket.__flash = document.getElementById("webSocketFlash");
				WebSocket.__flash.setCallerUrl(location.href);
				WebSocket.__flash.setDebug(!!window.WEB_SOCKET_DEBUG);
				for (var a = 0; a < WebSocket.__tasks.length; ++a) {
					WebSocket.__tasks[a]();
				}
				WebSocket.__tasks = [];
			}, 0);
		}, WebSocket.__onFlashEvent = function () {
			setTimeout(function () {
				try {
					for (var a = WebSocket.__flash.receiveEvents(), c = 0; c < a.length; ++c) {
						WebSocket.__instances[a[c].webSocketId].__handleEvent(a[c]);
					}
				}
				catch (d) {
					b.error(d);
				}
			}, 0);
			return !0;
		}, WebSocket.__log = function (a) {
			b.log(decodeURIComponent(a));
		}, WebSocket.__error = function (a) {
			b.error(decodeURIComponent(a));
		}, WebSocket.__addTask = function (a) {
			WebSocket.__flash ? a() : WebSocket.__tasks.push(a);
		}, WebSocket.__isFlashLite = function () {
			if (!window.navigator || !window.navigator.mimeTypes) {
				return !1;
			}
			var a = window.navigator.mimeTypes["application/x-shockwave-flash"];
			return !a || !a.enabledPlugin || !a.enabledPlugin.filename ? !1 : a.enabledPlugin.filename.match(/flashlite/i) ? !0 : !1;
		}, window.WEB_SOCKET_DISABLE_AUTO_INITIALIZATION || (window.addEventListener ? window.addEventListener("load", function () {
			WebSocket.__initialize();
		}, !1) : window.attachEvent("onload", function () {
			WebSocket.__initialize();
		}))) : b.error("Flash Player >= 10.0.0 is required.");
	}
})();
(function (b, a, c) {
	function d(b) {
		b && (a.Transport.apply(this, arguments), this.sendBuffer = []);
	}
	function f() {
	}
	b.XHR = d;
	a.util.inherit(d, a.Transport);
	d.prototype.open = function () {
		this.socket.setBuffer(!1);
		this.onOpen();
		this.get();
		this.setCloseTimeout();
		return this;
	};
	d.prototype.payload = function (b) {
		for (var c = [], d = 0, e = b.length; d < e; d++) {
			c.push(a.parser.encodePacket(b[d]));
		}
		this.send(a.parser.encodePayload(c));
	};
	d.prototype.send = function (a) {
		this.post(a);
		return this;
	};
	d.prototype.post = function (a) {
		function b() {
			if (4 == this.readyState) {
				if (this.onreadystatechange = f, e.posting = !1, 200 == this.status) {
					e.socket.setBuffer(!1);
				} else {
					e.onClose();
				}
			}
		}
		function d() {
			this.onload = f;
			e.socket.setBuffer(!1);
		}
		var e = this;
		this.socket.setBuffer(!0);
		this.sendXHR = this.request("POST");
		c.XDomainRequest && this.sendXHR instanceof XDomainRequest ? this.sendXHR.onload = this.sendXHR.onerror = d : this.sendXHR.onreadystatechange = b;
		this.sendXHR.send(a);
	};
	d.prototype.close = function () {
		this.onClose();
		return this;
	};
	d.prototype.request = function (b) {
		var c = a.util.request(this.socket.isXDomain()), d = a.util.query(this.socket.options.query, "t=" + +new Date);
		c.open(b || "GET", this.prepareUrl() + d, !0);
		if ("POST" == b) {
			try {
				c.setRequestHeader ? c.setRequestHeader("Content-type", "text/plain;charset=UTF-8") : c.contentType = "text/plain";
			}
			catch (e) {
			}
		}
		return c;
	};
	d.prototype.scheme = function () {
		return this.socket.options.secure ? "https" : "http";
	};
	d.check = function (b, d) {
		try {
			var f = a.util.request(d), e = c.XDomainRequest && f instanceof XDomainRequest, i = (b && b.options && b.options.secure ? "https:" : "http:") != c.location.protocol;
			if (f && (!e || !i)) {
				return !0;
			}
		}
		catch (k) {
		}
		return !1;
	};
	d.xdomainCheck = function () {
		return d.check(null, !0);
	};
})("undefined" != typeof io ? io.Transport : module.exports, "undefined" != typeof io ? io : module.parent.exports, this);
(function (b, a) {
	function c(b) {
		a.Transport.XHR.apply(this, arguments);
	}
	b.htmlfile = c;
	a.util.inherit(c, a.Transport.XHR);
	c.prototype.name = "htmlfile";
	c.prototype.get = function () {
		this.doc = new (window[["Active"].concat("Object").join("X")])("htmlfile");
		this.doc.open();
		this.doc.write("<html></html>");
		this.doc.close();
		this.doc.parentWindow.s = this;
		var b = this.doc.createElement("div");
		b.className = "socketio";
		this.doc.body.appendChild(b);
		this.iframe = this.doc.createElement("iframe");
		b.appendChild(this.iframe);
		var c = this, b = a.util.query(this.socket.options.query, "t=" + +new Date);
		this.iframe.src = this.prepareUrl() + b;
		a.util.on(window, "unload", function () {
			c.destroy();
		});
	};
	c.prototype._ = function (a, b) {
		this.onData(a);
		try {
			var c = b.getElementsByTagName("script")[0];
			c.parentNode.removeChild(c);
		}
		catch (h) {
		}
	};
	c.prototype.destroy = function () {
		if (this.iframe) {
			try {
				this.iframe.src = "about:blank";
			}
			catch (a) {
			}
			this.doc = null;
			this.iframe.parentNode.removeChild(this.iframe);
			this.iframe = null;
			CollectGarbage();
		}
	};
	c.prototype.close = function () {
		this.destroy();
		return a.Transport.XHR.prototype.close.call(this);
	};
	c.check = function () {
		if ("undefined" != typeof window && ["Active"].concat("Object").join("X") in window) {
			try {
				return new (window[["Active"].concat("Object").join("X")])("htmlfile") && a.Transport.XHR.check();
			}
			catch (b) {
			}
		}
		return !1;
	};
	c.xdomainCheck = function () {
		return !1;
	};
	a.transports.push("htmlfile");
})("undefined" != typeof io ? io.Transport : module.exports, "undefined" != typeof io ? io : module.parent.exports);
(function (b, a, c) {
	function d() {
		a.Transport.XHR.apply(this, arguments);
	}
	function f() {
	}
	b["xhr-polling"] = d;
	a.util.inherit(d, a.Transport.XHR);
	a.util.merge(d, a.Transport.XHR);
	d.prototype.name = "xhr-polling";
	d.prototype.open = function () {
		a.Transport.XHR.prototype.open.call(this);
		return !1;
	};
	d.prototype.get = function () {
		function a() {
			if (4 == this.readyState) {
				if (this.onreadystatechange = f, 200 == this.status) {
					e.onData(this.responseText), e.get();
				} else {
					e.onClose();
				}
			}
		}
		function b() {
			this.onerror = this.onload = f;
			e.onData(this.responseText);
			e.get();
		}
		function d() {
			e.onClose();
		}
		if (this.open) {
			var e = this;
			this.xhr = this.request();
			c.XDomainRequest && this.xhr instanceof XDomainRequest ? (this.xhr.onload = b, this.xhr.onerror = d) : this.xhr.onreadystatechange = a;
			this.xhr.send(null);
		}
	};
	d.prototype.onClose = function () {
		a.Transport.XHR.prototype.onClose.call(this);
		if (this.xhr) {
			this.xhr.onreadystatechange = this.xhr.onload = this.xhr.onerror = f;
			try {
				this.xhr.abort();
			}
			catch (b) {
			}
			this.xhr = null;
		}
	};
	d.prototype.ready = function (b, c) {
		var d = this;
		a.util.defer(function () {
			c.call(d);
		});
	};
	a.transports.push("xhr-polling");
})("undefined" != typeof io ? io.Transport : module.exports, "undefined" != typeof io ? io : module.parent.exports, this);
(function (b, a, c) {
	function d(b) {
		a.Transport["xhr-polling"].apply(this, arguments);
		this.index = a.j.length;
		var c = this;
		a.j.push(function (a) {
			c._(a);
		});
	}
	var f = c.document && "MozAppearance" in c.document.documentElement.style;
	b["jsonp-polling"] = d;
	a.util.inherit(d, a.Transport["xhr-polling"]);
	d.prototype.name = "jsonp-polling";
	d.prototype.post = function (b) {
		function c() {
			d();
			e.socket.setBuffer(!1);
		}
		function d() {
			e.iframe && e.form.removeChild(e.iframe);
			try {
				s = document.createElement("<iframe name=\"" + e.iframeId + "\">");
			}
			catch (a) {
				s = document.createElement("iframe"), s.name = e.iframeId;
			}
			s.id = e.iframeId;
			e.form.appendChild(s);
			e.iframe = s;
		}
		var e = this, i = a.util.query(this.socket.options.query, "t=" + +new Date + "&i=" + this.index);
		if (!this.form) {
			var f = document.createElement("form"), m = document.createElement("textarea"), t = this.iframeId = "socketio_iframe_" + this.index, s;
			f.className = "socketio";
			f.style.position = "absolute";
			f.style.top = "0px";
			f.style.left = "0px";
			f.style.display = "none";
			f.target = t;
			f.method = "POST";
			f.setAttribute("accept-charset", "utf-8");
			m.name = "d";
			f.appendChild(m);
			document.body.appendChild(f);
			this.form = f;
			this.area = m;
		}
		this.form.action = this.prepareUrl() + i;
		d();
		this.area.value = a.JSON.stringify(b);
		try {
			this.form.submit();
		}
		catch (p) {
		}
		this.iframe.attachEvent ? s.onreadystatechange = function () {
			"complete" == e.iframe.readyState && c();
		} : this.iframe.onload = c;
		this.socket.setBuffer(!0);
	};
	d.prototype.get = function () {
		var b = this, c = document.createElement("script"), d = a.util.query(this.socket.options.query, "t=" + +new Date + "&i=" + this.index);
		this.script && (this.script.parentNode.removeChild(this.script), this.script = null);
		c.async = !0;
		c.src = this.prepareUrl() + d;
		c.onerror = function () {
			b.onClose();
		};
		d = document.getElementsByTagName("script")[0];
		d.parentNode.insertBefore(c, d);
		this.script = c;
		f && setTimeout(function () {
			var a = document.createElement("iframe");
			document.body.appendChild(a);
			document.body.removeChild(a);
		}, 100);
	};
	d.prototype._ = function (a) {
		this.onData(a);
		this.open && this.get();
		return this;
	};
	d.prototype.ready = function (b, c) {
		var d = this;
		if (!f) {
			return c.call(this);
		}
		a.util.load(function () {
			c.call(d);
		});
	};
	d.check = function () {
		return "document" in c;
	};
	d.xdomainCheck = function () {
		return !0;
	};
	a.transports.push("jsonp-polling");
})("undefined" != typeof io ? io.Transport : module.exports, "undefined" != typeof io ? io : module.parent.exports, this);
var Erizo = Erizo || {};
Erizo.EventDispatcher = function (b) {
	var a = {};
	b.dispatcher = {};
	b.dispatcher.eventListeners = {};
	a.addEventListener = function (a, d) {
		void 0 === b.dispatcher.eventListeners[a] && (b.dispatcher.eventListeners[a] = []);
		b.dispatcher.eventListeners[a].push(d);
	};
	a.removeEventListener = function (a, d) {
		var f;
		f = b.dispatcher.eventListeners[a].indexOf(d);
		-1 !== f && b.dispatcher.eventListeners[a].splice(f, 1);
	};
	a.dispatchEvent = function (a) {
		var d;
		L.Logger.debug("Event: " + a.type);
		for (d in b.dispatcher.eventListeners[a.type]) {
			if (b.dispatcher.eventListeners[a.type].hasOwnProperty(d)) {
				b.dispatcher.eventListeners[a.type][d](a);
			}
		}
	};
	return a;
};
Erizo.LicodeEvent = function (b) {
	var a = {};
	a.type = b.type;
	return a;
};
Erizo.RoomEvent = function (b) {
	var a = Erizo.LicodeEvent(b);
	a.streams = b.streams;
	return a;
};
Erizo.StreamEvent = function (b) {
	var a = Erizo.LicodeEvent(b);
	a.stream = b.stream;
	a.msg = b.msg;
	return a;
};
Erizo.PublisherEvent = function (b) {
	return Erizo.LicodeEvent(b);
};
Erizo = Erizo || {};
Erizo.FcStack = function () {
	return {addStream:function () {
	}};
};
Erizo = Erizo || {};
Erizo.BowserStack = function (b) {
	var a = {}, c = webkitRTCPeerConnection;
	a.pc_config = {iceServers:[]};
	a.con = {optional:[{DtlsSrtpKeyAgreement:!0}]};
	void 0 !== b.stunServerUrl && a.pc_config.iceServers.push({url:b.stunServerUrl});
	(b.turnServer || {}).url && a.pc_config.iceServers.push({username:b.turnServer.username, credential:b.turnServer.password, url:b.turnServer.url});
	void 0 === b.audio && (b.audio = !0);
	void 0 === b.video && (b.video = !0);
	a.mediaConstraints = {offerToReceiveVideo:true, offerToReceiveAudio:b.audio};
	a.peerConnection = new c(a.pc_config, a.con);
	b.remoteDescriptionSet = !1;
	var d = function (a) {
		if (b.maxVideoBW) {
			var c = a.match(/m=video.*\r\n/);
			c == null && (c = a.match(/m=video.*\n/));
			var d = c[0] + "b=AS:" + b.maxVideoBW + "\r\n", a = a.replace(c[0], d);
		}
		if (b.maxAudioBW) {
			c = a.match(/m=audio.*\r\n/);
			c == null && (c = a.match(/m=audio.*\n/));
			d = c[0] + "b=AS:" + b.maxAudioBW + "\r\n";
			a = a.replace(c[0], d);
		}
		return a;
	};
	a.close = function () {
		a.state = "closed";
		a.peerConnection.close();
	};
	b.localCandidates = [];
	a.peerConnection.onicecandidate = function (e) {
		if (e.candidate) {
			if (!e.candidate.candidate.match(/a=/)) {
				e.candidate.candidate = "a=" + e.candidate.candidate;
			}
			b.remoteDescriptionSet ? b.callback({type:"candidate", candidate:e.candidate}) : b.localCandidates.push(e.candidate);
		} else {
			console.log("End of candidates.", a.peerConnection.localDescription);
		}
	};
	a.peerConnection.onaddstream = function (b) {
		if (a.onaddstream) {
			a.onaddstream(b);
		}
	};
	a.peerConnection.onremovestream = function (b) {
		if (a.onremovestream) {
			a.onremovestream(b);
		}
	};
	var f = function (a) {
		console.log("Error in Stack ", a);
	}, g, h = function (e) {
		e.sdp = d(e.sdp);
		console.log("Set local description", e.sdp);
		g = e;
		a.peerConnection.setLocalDescription(g, function () {
			console.log("The final LocalDesc", a.peerConnection.localDescription);
			b.callback(a.peerConnection.localDescription);
		}, f);
	}, j = function (e) {
		e.sdp = d(e.sdp);
		b.callback(e);
		g = e;
		a.peerConnection.setLocalDescription(e);
	};
	a.createOffer = function (b) {
		b === true ? a.peerConnection.createOffer(h, f, a.mediaConstraints) : a.peerConnection.createOffer(h, f);
	};
	a.addStream = function (b) {
		a.peerConnection.addStream(b);
	};
	b.remoteCandidates = [];
	a.processSignalingMessage = function (e) {
		console.log("Process Signaling Message", e);
		if (e.type === "offer") {
			e.sdp = d(e.sdp);
			a.peerConnection.setRemoteDescription(new RTCSessionDescription(e));
			a.peerConnection.createAnswer(j, null, a.mediaConstraints);
			b.remoteDescriptionSet = true;
		} else {
			if (e.type === "answer") {
				console.log("Set remote description", e.sdp);
				e.sdp = d(e.sdp);
				a.peerConnection.setRemoteDescription(new RTCSessionDescription(e), function () {
					b.remoteDescriptionSet = true;
					for (console.log("Candidates to be added: ", b.remoteCandidates.length); b.remoteCandidates.length > 0; ) {
						console.log("Candidate :", b.remoteCandidates[b.remoteCandidates.length - 1]);
						a.peerConnection.addIceCandidate(b.remoteCandidates.shift(), function () {
						}, f);
					}
					for (; b.localCandidates.length > 0; ) {
						b.callback({type:"candidate", candidate:b.localCandidates.shift()});
					}
				}, function () {
					console.log("Error Setting Remote Description");
				});
			} else {
				if (e.type === "candidate") {
					console.log("Message with candidate");
					try {
						var c;
						c = typeof e.candidate === "object" ? e.candidate : JSON.parse(e.candidate);
						c.candidate = c.candidate.replace(/a=/g, "");
						c.sdpMLineIndex = parseInt(c.sdpMLineIndex);
						c.sdpMLineIndex = c.sdpMid == "audio" ? 0 : 1;
						var k = new RTCIceCandidate(c);
						console.log("Remote Candidate", k);
						b.remoteDescriptionSet ? a.peerConnection.addIceCandidate(k, function () {
						}, f) : b.remoteCandidates.push(k);
					}
					catch (g) {
						L.Logger.error("Error parsing candidate", e.candidate);
					}
				}
			}
		}
	};
	return a;
};
Erizo = Erizo || {};
Erizo.FirefoxStack = function (b) {
	var a = {}, c = mozRTCPeerConnection, d = mozRTCSessionDescription, f = mozRTCIceCandidate;
	a.pc_config = {iceServers:[]};
	void 0 !== b.stunServerUrl && a.pc_config.iceServers.push({url:b.stunServerUrl});
	(b.turnServer || {}).url && a.pc_config.iceServers.push({username:b.turnServer.username, credential:b.turnServer.password, url:b.turnServer.url});
	void 0 === b.audio && (b.audio = !0);
	void 0 === b.video && (b.video = !0);
	a.mediaConstraints = {offerToReceiveAudio:b.audio, offerToReceiveVideo:b.video, mozDontOfferDataChannel:!0};
	a.roapSessionId = 103;
	a.peerConnection = new c(a.pc_config, a.con);
	b.localCandidates = [];
	a.peerConnection.onicecandidate = function (a) {
		if (a.candidate) {
			if (!a.candidate.candidate.match(/a=/)) {
				a.candidate.candidate = "a=" + a.candidate.candidate;
			}
			if (b.remoteDescriptionSet) {
				b.callback({type:"candidate", candidate:a.candidate});
			} else {
				b.localCandidates.push(a.candidate);
				console.log("Local Candidates stored: ", b.localCandidates.length, b.localCandidates);
			}
		} else {
			console.log("End of candidates.");
		}
	};
	a.peerConnection.onaddstream = function (b) {
		if (a.onaddstream) {
			a.onaddstream(b);
		}
	};
	a.peerConnection.onremovestream = function (b) {
		if (a.onremovestream) {
			a.onremovestream(b);
		}
	};
	var g = function (a) {
		if (b.video && b.maxVideoBW) {
			var e = a.match(/m=video.*\r\n/);
			e == null && (e = a.match(/m=video.*\n/));
			var c = e[0] + "b=AS:" + b.maxVideoBW + "\r\n", a = a.replace(e[0], c);
		}
		if (b.audio && b.maxAudioBW) {
			e = a.match(/m=audio.*\r\n/);
			e == null && (e = a.match(/m=audio.*\n/));
			c = e[0] + "b=AS:" + b.maxAudioBW + "\r\n";
			a = a.replace(e[0], c);
		}
		return a;
	}, h, j = function (a) {
		a.sdp = g(a.sdp);
		a.sdp = a.sdp.replace(/a=ice-options:google-ice\r\n/g, "");
		b.callback(a);
		h = a;
	}, e = function (e) {
		e.sdp = g(e.sdp);
		e.sdp = e.sdp.replace(/a=ice-options:google-ice\r\n/g, "");
		b.callback(e);
		h = e;
		a.peerConnection.setLocalDescription(h);
	};
	a.createOffer = function () {
		a.peerConnection.createOffer(j, function (a) {
			L.Logger.error("Error", a);
		}, a.mediaConstraints);
	};
	a.addStream = function (b) {
		a.peerConnection.addStream(b);
	};
	b.remoteCandidates = [];
	b.remoteDescriptionSet = !1;
	a.close = function () {
		a.state = "closed";
		a.peerConnection.close();
	};
	a.processSignalingMessage = function (c) {
		if (c.type === "offer") {
			c.sdp = g(c.sdp);
			a.peerConnection.setRemoteDescription(new d(c));
			a.peerConnection.createAnswer(e, function (a) {
				L.Logger.error("Error", a);
			}, a.mediaConstraints);
			b.remoteDescriptionSet = true;
		} else {
			if (c.type === "answer") {
				console.log("Set remote and local description", c.sdp);
				c.sdp = g(c.sdp);
				a.peerConnection.setLocalDescription(h, function () {
					a.peerConnection.setRemoteDescription(new d(c), function () {
						for (b.remoteDescriptionSet = true; b.remoteCandidates.length > 0; ) {
							a.peerConnection.addIceCandidate(b.remoteCandidates.shift());
						}
						for (; b.localCandidates.length > 0; ) {
							L.Logger.info("Sending Candidate");
							b.callback({type:"candidate", candidate:b.localCandidates.shift()});
						}
					});
				});
			} else {
				if (c.type === "candidate") {
					try {
						var k;
						k = typeof c.candidate === "object" ? c.candidate : JSON.parse(c.candidate);
						k.candidate = k.candidate.replace(/ generation 0/g, "");
						k.candidate = k.candidate.replace(/ udp /g, " UDP ");
						k.sdpMLineIndex = parseInt(k.sdpMLineIndex);
						var m = new f(k);
						b.remoteDescriptionSet ? a.peerConnection.addIceCandidate(m) : b.remoteCandidates.push(m);
					}
					catch (t) {
						L.Logger.error("Error parsing candidate", c.candidate, t);
					}
				}
			}
		}
	};
	return a;
};
Erizo = Erizo || {};
Erizo.ChromeStableStack = function (b) {
	var a = {}, c = webkitRTCPeerConnection;
	a.pc_config = {iceServers:[]};
	a.con = {optional:[{DtlsSrtpKeyAgreement:!0}]};
	void 0 !== b.stunServerUrl && a.pc_config.iceServers.push({url:b.stunServerUrl});
	(b.turnServer || {}).url && a.pc_config.iceServers.push({username:b.turnServer.username, credential:b.turnServer.password, url:b.turnServer.url});
	void 0 === b.audio && (b.audio = !0);
	void 0 === b.video && (b.video = !0);
	a.mediaConstraints = {mandatory:{OfferToReceiveVideo:true, OfferToReceiveAudio:b.audio}};
	var d = function (a) {
		console.log("Error in Stack ", a);
	};
	a.peerConnection = new c(a.pc_config, a.con); 
	var f = function (a) {
		if (b.video && b.maxVideoBW) {
			var c = a.match(/m=video.*\r\n/);
			c == null && (c = a.match(/m=video.*\n/));
			var d = c[0] + "b=AS:" + b.maxVideoBW + "\r\n", a = a.replace(c[0], d);
		}
		if (b.audio && b.maxAudioBW) {
			c = a.match(/m=audio.*\r\n/);
			c == null && (c = a.match(/m=audio.*\n/));
			d = c[0] + "b=AS:" + b.maxAudioBW + "\r\n";
			a = a.replace(c[0], d);
		}
		return a;
	};
	a.close = function () {
		a.state = "closed";
		a.peerConnection.close();
	};
	b.localCandidates = [];
	a.peerConnection.onicecandidate = function (a) {
		if (a.candidate) {
			if (!a.candidate.candidate.match(/a=/)) {
				a.candidate.candidate = "a=" + a.candidate.candidate;
			}
			if (b.remoteDescriptionSet) {
				b.callback({type:"candidate", candidate:a.candidate});
			} else {
				b.localCandidates.push(a.candidate);
				console.log("Local Candidates stored: ", b.localCandidates.length, b.localCandidates);
			}
		} else {
			console.log("End of candidates.");
		}
	};
	a.peerConnection.onaddstream = function (b) {
		if (a.onaddstream) {
			a.onaddstream(b);
		}
	};
	a.peerConnection.onremovestream = function (b) {
		if (a.onremovestream) {
			a.onremovestream(b);
		}
	};
	var g, h = function (a) {
		a.sdp = f(a.sdp);
		a.sdp = a.sdp.replace(/a=ice-options:google-ice\r\n/g, "");
		b.callback(a);
		g = a;
	}, j = function (c) {
		c.sdp = f(c.sdp);
		c.sdp = c.sdp.replace(/a=ice-options:google-ice\r\n/g, "");
		b.callback(c);
		g = c;
		a.peerConnection.setLocalDescription(c);
	};
	a.createOffer = function (b) {
		b === true ? a.peerConnection.createOffer(h, d, a.mediaConstraints) : a.peerConnection.createOffer(h, d);
	};
	a.addStream = function (b) {
		a.peerConnection.addStream(b);
	};
	b.remoteCandidates = [];
	b.remoteDescriptionSet = !1;
	a.processSignalingMessage = function (c) {
		if (c.type === "offer") {
			c.sdp = f(c.sdp);
			a.peerConnection.setRemoteDescription(new RTCSessionDescription(c));
			a.peerConnection.createAnswer(j, null, a.mediaConstraints);
			b.remoteDescriptionSet = true;
		} else {
			if (c.type === "answer") {
				console.log("Set remote and local description", c.sdp);
				c.sdp = f(c.sdp);
				a.peerConnection.setLocalDescription(g, function () {
					a.peerConnection.setRemoteDescription(new RTCSessionDescription(c), function () {
						b.remoteDescriptionSet = true;
						for (console.log("Candidates to be added: ", b.remoteCandidates.length, b.remoteCandidates); b.remoteCandidates.length > 0; ) {
							a.peerConnection.addIceCandidate(b.remoteCandidates.shift());
						}
						for (console.log("Local candidates to send:", b.localCandidates.length); b.localCandidates.length > 0; ) {
							b.callback({type:"candidate", candidate:b.localCandidates.shift()});
						}
					});
				});
			} else {
				if (c.type === "candidate") {
					try {
						var d;
						d = typeof c.candidate === "object" ? c.candidate : JSON.parse(c.candidate);
						d.candidate = d.candidate.replace(/a=/g, "");
						d.sdpMLineIndex = parseInt(d.sdpMLineIndex);
						var k = new RTCIceCandidate(d);
						b.remoteDescriptionSet ? a.peerConnection.addIceCandidate(k) : b.remoteCandidates.push(k);
					}
					catch (m) {
						L.Logger.error("Error parsing candidate", c.candidate);
					}
				}
			}
		}
	};
	return a;
};
Erizo = Erizo || {};
Erizo.ChromeCanaryStack = function (b) {
	var a = {}, c = webkitRTCPeerConnection;
	a.pc_config = {iceServers:[]};
	a.con = {optional:[{DtlsSrtpKeyAgreement:!0}]};
	void 0 !== b.stunServerUrl && a.pc_config.iceServers.push({url:b.stunServerUrl});
	(b.turnServer || {}).url && a.pc_config.iceServers.push({username:b.turnServer.username, credential:b.turnServer.password, url:b.turnServer.url});
	if (void 0 === b.audio || b.nop2p) {
		b.audio = !0;
	}
	if (void 0 === b.video || b.nop2p) {
		b.video = !0;
	}
	a.mediaConstraints = {mandatory:{OfferToReceiveVideo:true, OfferToReceiveAudio:b.audio}};
	a.roapSessionId = 103;
	a.peerConnection = new c(a.pc_config, a.con);
	a.peerConnection.onicecandidate = function (c) {
		L.Logger.debug("PeerConnection: ", b.session_id);
		if (c.candidate) {
			a.iceCandidateCount += 1;
		} else {
			if (L.Logger.debug("State: " + a.peerConnection.iceGatheringState), void 0 === a.ices && (a.ices = 0), a.ices += 1, 1 <= a.ices && a.moreIceComing) {
				a.moreIceComing = !1, a.markActionNeeded();
			}
		}
	};
	var d = function (a) {
		if (b.maxVideoBW) {
			var c = a.match(/m=video.*\r\n/), d = c[0] + "b=AS:" + b.maxVideoBW + "\r\n", a = a.replace(c[0], d);
		}
		b.maxAudioBW && (c = a.match(/m=audio.*\r\n/), d = c[0] + "b=AS:" + b.maxAudioBW + "\r\n", a = a.replace(c[0], d));
		return a;
	};
	a.processSignalingMessage = function (b) {
		L.Logger.debug("Activity on conn " + a.sessionId);
		b = JSON.parse(b);
		a.incomingMessage = b;
		"new" === a.state ? "OFFER" === b.messageType ? (b = {sdp:b.sdp, type:"offer"}, a.peerConnection.setRemoteDescription(new RTCSessionDescription(b)), a.state = "offer-received", a.markActionNeeded()) : a.error("Illegal message for this state: " + b.messageType + " in state " + a.state) : "offer-sent" === a.state ? "ANSWER" === b.messageType ? (b = {sdp:b.sdp, type:"answer"}, L.Logger.debug("Received ANSWER: ", b.sdp), b.sdp = d(b.sdp), a.peerConnection.setRemoteDescription(new RTCSessionDescription(b)), a.sendOK(), a.state = "established") : "pr-answer" === b.messageType ? (b = {sdp:b.sdp, type:"pr-answer"}, a.peerConnection.setRemoteDescription(new RTCSessionDescription(b))) : "offer" === b.messageType ? a.error("Not written yet") : a.error("Illegal message for this state: " + b.messageType + " in state " + a.state) : "established" === a.state && ("OFFER" === b.messageType ? (b = {sdp:b.sdp, type:"offer"}, a.peerConnection.setRemoteDescription(new RTCSessionDescription(b)), a.state = "offer-received", a.markActionNeeded()) : a.error("Illegal message for this state: " + b.messageType + " in state " + a.state));
	};
	a.addStream = function (b) {
		a.peerConnection.addStream(b);
		a.markActionNeeded();
	};
	a.removeStream = function () {
		a.markActionNeeded();
	};
	a.close = function () {
		a.state = "closed";
		a.peerConnection.close();
	};
	a.markActionNeeded = function () {
		a.actionNeeded = !0;
		a.doLater(function () {
			a.onstablestate();
		});
	};
	a.doLater = function (a) {
		window.setTimeout(a, 1);
	};
	a.onstablestate = function () {
		var b;
		if (a.actionNeeded) {
			if ("new" === a.state || "established" === a.state) {
				a.peerConnection.createOffer(function (b) {
					b.sdp = d(b.sdp);
					L.Logger.debug("Changed", b.sdp);
					b.sdp !== a.prevOffer ? (a.peerConnection.setLocalDescription(b), a.state = "preparing-offer", a.markActionNeeded()) : L.Logger.debug("Not sending a new offer");
				}, null, a.mediaConstraints);
			} else {
				if ("preparing-offer" === a.state) {
					if (a.moreIceComing) {
						return;
					}
					a.prevOffer = a.peerConnection.localDescription.sdp;
					L.Logger.debug("Sending OFFER: " + a.prevOffer);
					a.sendMessage("OFFER", a.prevOffer);
					a.state = "offer-sent";
				} else {
					if ("offer-received" === a.state) {
						a.peerConnection.createAnswer(function (b) {
							a.peerConnection.setLocalDescription(b);
							a.state = "offer-received-preparing-answer";
							a.iceStarted ? a.markActionNeeded() : (L.Logger.debug((new Date).getTime() + ": Starting ICE in responder"), a.iceStarted = !0);
						}, null, a.mediaConstraints);
					} else {
						if ("offer-received-preparing-answer" === a.state) {
							if (a.moreIceComing) {
								return;
							}
							b = a.peerConnection.localDescription.sdp;
							a.sendMessage("ANSWER", b);
							a.state = "established";
						} else {
							a.error("Dazed and confused in state " + a.state + ", stopping here");
						}
					}
				}
			}
			a.actionNeeded = !1;
		}
	};
	a.sendOK = function () {
		a.sendMessage("OK");
	};
	a.sendMessage = function (b, c) {
		var d = {};
		d.messageType = b;
		d.sdp = c;
		"OFFER" === b ? (d.offererSessionId = a.sessionId, d.answererSessionId = a.otherSessionId, d.seq = a.sequenceNumber += 1, d.tiebreaker = Math.floor(429496723 * Math.random() + 1)) : (d.offererSessionId = a.incomingMessage.offererSessionId, d.answererSessionId = a.sessionId, d.seq = a.incomingMessage.seq);
		a.onsignalingmessage(JSON.stringify(d));
	};
	a.error = function (a) {
		throw "Error in RoapOnJsep: " + a;
	};
	a.sessionId = a.roapSessionId += 1;
	a.sequenceNumber = 0;
	a.actionNeeded = !1;
	a.iceStarted = !1;
	a.moreIceComing = !0;
	a.iceCandidateCount = 0;
	a.onsignalingmessage = b.callback;
	a.peerConnection.onopen = function () {
		if (a.onopen) {
			a.onopen();
		}
	};
	a.peerConnection.onaddstream = function (b) {
		if (a.onaddstream) {
			a.onaddstream(b);
		}
	};
	a.peerConnection.onremovestream = function (b) {
		if (a.onremovestream) {
			a.onremovestream(b);
		}
	};
	a.peerConnection.oniceconnectionstatechange = function (b) {
		if (a.oniceconnectionstatechange) {
			a.oniceconnectionstatechange(b.currentTarget.iceConnectionState);
		}
	};
	a.onaddstream = null;
	a.onremovestream = null;
	a.state = "new";
	a.markActionNeeded();
	return a;
};
Erizo = Erizo || {};
Erizo.sessionId = 103;
Erizo.Connection = function (b) {
	var a = {};
	b.session_id = Erizo.sessionId += 1;
	a.browser = Erizo.getBrowser();
	if ("undefined" !== typeof module && module.exports) {
		L.Logger.error("Publish/subscribe video/audio streams not supported in erizofc yet"), a = Erizo.FcStack(b);
	} else {
		if ("mozilla" === a.browser) {
			L.Logger.debug("Firefox Stack"), a = Erizo.FirefoxStack(b);
		} else {
			if ("bowser" === a.browser) {
				L.Logger.debug("Bowser Stack"), a = Erizo.BowserStack(b);
			} else {
				if ("chrome-stable" === a.browser) {
					L.Logger.debug("Stable!"), a = Erizo.ChromeStableStack(b);
				} else {
					throw L.Logger.debug("None!"), "WebRTC stack not available";
				}
			}
		}
	}
	return a;
};
Erizo.getBrowser = function () {
	var b = "none";
	null !== window.navigator.userAgent.match("Firefox") ? b = "mozilla" : null !== window.navigator.userAgent.match("Bowser") ? b = "bowser" : null !== window.navigator.userAgent.match("Chrome") ? 26 <= window.navigator.appVersion.match(/Chrome\/([\w\W]*?)\./)[1] && (b = "chrome-stable") : null !== window.navigator.userAgent.match("Safari") ? b = "bowser" : null !== window.navigator.userAgent.match("AppleWebKit") && (b = "bowser");
	return b;
};
Erizo.GetUserMedia = function (b, a, c) {
	navigator.getMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;
	if (b.screen) {
		if (L.Logger.debug("Screen access requested"), 34 <= !window.navigator.appVersion.match(/Chrome\/([\w\W]*?)\./)[1]) {
			c({code:"This browser does not support screen sharing"});
		} else {
			var d = "okeephmleflklcdebijnponpabbmmgeo";
			b.extensionId && (L.Logger.debug("extensionId supplied, using " + b.extensionId), d = b.extensionId);
			L.Logger.debug("Screen access on chrome stable, looking for extension");
			try {
				chrome.runtime.sendMessage(d, {getStream:!0}, function (d) {
					if (d == void 0) {
						L.Logger.debug("Access to screen denied");
						c({code:"Access to screen denied"});
					} else {
						b = {video:{mandatory:{chromeMediaSource:"desktop", chromeMediaSourceId:d.streamId}}};
						navigator.getMedia(b, a, c);
					}
				});
			}
			catch (f) {
				L.Logger.debug("Lynckia screensharing plugin is not accessible "), c({code:"no_plugin_present"});
			}
		}
	} else {
		"undefined" !== typeof module && module.exports ? L.Logger.error("Video/audio streams not supported in erizofc yet") : navigator.getMedia(b, a, c);
	}
};
Erizo = Erizo || {};
Erizo.Stream = function (b) {
	var a = Erizo.EventDispatcher(b), c;
	a.stream = b.stream;
	a.url = b.url;
	a.recording = b.recording;
	a.room = void 0;
	a.showing = !1;
	a.local = !1;
	a.video = b.video;
	a.audio = b.audio;
	a.screen = b.screen;
	a.videoSize = b.videoSize;
	a.extensionId = b.extensionId;
	if (void 0 !== a.videoSize && (!(a.videoSize instanceof Array) || 4 != a.videoSize.length)) {
		throw Error("Invalid Video Size");
	}
	if (void 0 === b.local || !0 === b.local) {
		a.local = !0;
	}
	a.getID = function () {
		return b.streamID;
	};
	a.getAttributes = function () {
		return b.attributes;
	};
	a.setAttributes = function () {
		L.Logger.error("Failed to set attributes data. This Stream object has not been published.");
	};
	a.updateLocalAttributes = function (a) {
		b.attributes = a;
	};
	a.hasAudio = function () {
		return b.audio;
	};
	a.hasVideo = function () {
		return b.video;
	};
	a.hasData = function () {
		return b.data;
	};
	a.hasScreen = function () {
		return b.screen;
	};
	a.sendData = function () {
		L.Logger.error("Failed to send data. This Stream object has not that channel enabled.");
	};
	a.init = function () {
		try {
			if ((b.audio || b.video || b.screen) && void 0 === b.url) {
				L.Logger.debug("Requested access to local media");
				var c = b.video;
				!0 == c && void 0 !== a.videoSize && (c = {mandatory:{minWidth:a.videoSize[0], minHeight:a.videoSize[1], maxWidth:a.videoSize[2], maxHeight:a.videoSize[3]}});
				var f = {video:c, audio:b.audio, fake:b.fake, screen:b.screen, extensionId:a.extensionId};
				L.Logger.debug(f);
				Erizo.GetUserMedia(f, function (b) {
					L.Logger.info("User has granted access to local media.");
					a.stream = b;
					b = Erizo.StreamEvent({type:"access-accepted"});
					a.dispatchEvent(b);
				}, function (b) {
					L.Logger.error("Failed to get access to local media. Error code was " + b.code + ".");
					b = Erizo.StreamEvent({type:"access-denied"});
					a.dispatchEvent(b);
				});
			} else {
				var g = Erizo.StreamEvent({type:"access-accepted"});
				a.dispatchEvent(g);
			}
		}
		catch (h) {
			L.Logger.error("Error accessing to local media", h);
		}
	};
	a.close = function () {
		a.local && (void 0 !== a.room && a.room.unpublish(a), a.hide(), void 0 !== a.stream && a.stream.stop(), a.stream = void 0);
	};
	a.play = function (b, c) {
		c = c || {};
		a.elementID = b;
		if (a.hasVideo() || this.hasScreen()) {
			if (void 0 !== b) {
				var g = new Erizo.VideoPlayer({id:a.getID(), stream:a, elementID:b, options:c});
				a.player = g;
				a.showing = !0;
			}
		} else {
			a.hasAudio && (g = new Erizo.AudioPlayer({id:a.getID(), stream:a, elementID:b, options:c}), a.player = g, a.showing = !0);
		}
	};
	a.stop = function () {
		a.showing && void 0 !== a.player && (a.player.destroy(), a.showing = !1);
	};
	a.show = a.play;
	a.hide = a.stop;
	c = function () {
		if (void 0 !== a.player && void 0 !== a.stream) {
			var b = a.player.video, c = document.defaultView.getComputedStyle(b), g = parseInt(c.getPropertyValue("width"), 10), h = parseInt(c.getPropertyValue("height"), 10), j = parseInt(c.getPropertyValue("left"), 10), c = parseInt(c.getPropertyValue("top"), 10), e = document.getElementById(a.elementID), i = document.defaultView.getComputedStyle(e), e = parseInt(i.getPropertyValue("width"), 10), i = parseInt(i.getPropertyValue("height"), 10), k = document.createElement("canvas");
			k.id = "testing";
			k.width = e;
			k.height = i;
			k.setAttribute("style", "display: none");
			k.getContext("2d").drawImage(b, j, c, g, h);
			return k;
		}
		return null;
	};
	a.getVideoFrameURL = function (a) {
		var b = c();
		return null !== b ? a ? b.toDataURL(a) : b.toDataURL() : null;
	};
	a.getVideoFrame = function () {
		var a = c();
		return null !== a ? a.getContext("2d").getImageData(0, 0, a.width, a.height) : null;
	};
	return a;
};
Erizo = Erizo || {};
Erizo.Room = function (b) {
	var a = Erizo.EventDispatcher(b), c, d, f, g, h, j;
	a.remoteStreams = {};
	a.localStreams = {};
	a.roomID = "";
	a.socket = {};
	a.state = 0;
	a.p2p = !1;
	a.addEventListener("room-disconnected", function () {
		var b, c;
		a.state = 0;
		for (b in a.remoteStreams) {
			a.remoteStreams.hasOwnProperty(b) && (c = a.remoteStreams[b], j(c), delete a.remoteStreams[b], c = Erizo.StreamEvent({type:"stream-removed", stream:c}), a.dispatchEvent(c));
		}
		a.remoteStreams = {};
		for (b in a.localStreams) {
			a.localStreams.hasOwnProperty(b) && (c = a.localStreams[b], c.pc.close(), delete a.localStreams[b]);
		}
		try {
			a.socket.disconnect();
		}
		catch (d) {
			L.Logger.debug("Socket already disconnected");
		}
		a.socket = void 0;
	});
	j = function (a) {
		void 0 !== a.stream && (a.hide(), a.pc && a.pc.close(), a.local && a.stream.stop());
	};
	g = function (a, b) {
		a.local ? d("sendDataStream", {id:a.getID(), msg:b}) : L.Logger.error("You can not send data through a remote stream");
	};
	h = function (a, b) {
		a.local ? (a.updateLocalAttributes(b), d("updateStreamAttributes", {id:a.getID(), attrs:b})) : L.Logger.error("You can not update attributes in a remote stream");
	};
	c = function (c, i, k) {
		console.log(c);
		a.socket = io.connect(c.host, {reconnect:!1, secure:c.secure, "force new connection":!0});
		a.socket.on("onAddStream", function (b) {
			var c = Erizo.Stream({streamID:b.id, local:!1, audio:b.audio, video:b.video, data:b.data, screen:b.screen, attributes:b.attributes});
			a.remoteStreams[b.id] = c;
			b = Erizo.StreamEvent({type:"stream-added", stream:c});
			a.dispatchEvent(b);
		});
		a.socket.on("signaling_message_erizo", function (b) {
			var c;
			(c = b.peerId ? a.remoteStreams[b.peerId] : a.localStreams[b.streamId]) && c.pc.processSignalingMessage(b.mess);
		});
		a.socket.on("signaling_message_peer", function (b) {
			var c = a.localStreams[b.streamId];
			c ? c.pc[b.peerSocket].processSignalingMessage(b.msg) : (c = a.remoteStreams[b.streamId], c.pc || g(c, b.peerSocket), c.pc.processSignalingMessage(b.msg));
		});
		a.socket.on("publish_me", function (b) {
			var c = a.localStreams[b.streamId];
			void 0 === c.pc && (c.pc = {});
			c.pc[b.peerSocket] = Erizo.Connection({callback:function (a) {
				f("signaling_message", {streamId:b.streamId, peerSocket:b.peerSocket, msg:a});
			}, audio:c.hasAudio(), video:c.hasVideo(), stunServerUrl:a.stunServerUrl, turnServer:a.turnServer});
			c.pc[b.peerSocket].oniceconnectionstatechange = function (a) {
				if (a === "disconnected") {
					c.pc[b.peerSocket].close();
					delete c.pc[b.peerSocket];
				}
			};
			c.pc[b.peerSocket].addStream(c.stream);
			c.pc[b.peerSocket].createOffer();
		});
		var g = function (c, e) {
			c.pc = Erizo.Connection({callback:function (a) {
				f("signaling_message", {streamId:c.getID(), peerSocket:e, msg:a});
			}, stunServerUrl:a.stunServerUrl, turnServer:a.turnServer, maxAudioBW:b.maxAudioBW, maxVideoBW:b.maxVideoBW});
			c.pc.onaddstream = function (b) {
				L.Logger.info("Stream subscribed");
				c.stream = b.stream;
				b = Erizo.StreamEvent({type:"stream-subscribed", stream:c});
				a.dispatchEvent(b);
			};
		};
		a.socket.on("onDataStream", function (b) {
			var c = a.remoteStreams[b.id], b = Erizo.StreamEvent({type:"stream-data", msg:b.msg, stream:c});
			c.dispatchEvent(b);
		});
		a.socket.on("onUpdateAttributeStream", function (b) {
			var c = a.remoteStreams[b.id], e = Erizo.StreamEvent({type:"stream-attributes-update", attrs:b.attrs, stream:c});
			c.updateLocalAttributes(b.attrs);
			c.dispatchEvent(e);
		});
		a.socket.on("onRemoveStream", function (b) {
			var c = a.remoteStreams[b.id];
			delete a.remoteStreams[b.id];
			j(c);
			b = Erizo.StreamEvent({type:"stream-removed", stream:c});
			a.dispatchEvent(b);
		});
		a.socket.on("disconnect", function () {
			L.Logger.info("Socket disconnected");
			if (0 !== a.state) {
				var b = Erizo.RoomEvent({type:"room-disconnected"});
				a.dispatchEvent(b);
			}
		});
		a.socket.on("connection_failed", function () {
			L.Logger.info("ICE Connection Failed");
			if (0 !== a.state) {
				var b = Erizo.RoomEvent({type:"stream-failed"});
				a.dispatchEvent(b);
			}
		});
		d("token", c, i, k);
	};
	d = function (b, c, d, f) {
		a.socket.emit(b, c, function (a, b) {
			"success" === a ? void 0 !== d && d(b) : "error" === a ? void 0 !== f && f(b) : void 0 !== d && d(a, b);
		});
	};
	f = function (b, c, d, f) {
		a.socket.emit(b, c, d, function (a, b) {
			void 0 !== f && f(a, b);
		});
	};
	a.connect = function () {
		var e = L.Base64.decodeBase64(b.token);
		0 !== a.state && L.Logger.error("Room already connected");
		a.state = 1;
		c(JSON.parse(e), function (c) {
			var e = 0, d = [], f, g, h;
			f = c.streams || [];
			a.p2p = c.p2p;
			g = c.id;
			a.stunServerUrl = c.stunServerUrl;
			a.turnServer = c.turnServer;
			a.state = 2;
			b.defaultVideoBW = c.defaultVideoBW;
			b.maxVideoBW = c.maxVideoBW;
			for (e in f) {
				f.hasOwnProperty(e) && (h = f[e], c = Erizo.Stream({streamID:h.id, local:!1, audio:h.audio, video:h.video, data:h.data, screen:h.screen, attributes:h.attributes}), d.push(c), a.remoteStreams[h.id] = c);
			}
			a.roomID = g;
			L.Logger.info("Connected to room " + a.roomID);
			e = Erizo.RoomEvent({type:"room-connected", streams:d});
			a.dispatchEvent(e);
		}, function (a) {
			L.Logger.error("Not Connected! Error: " + a);
		});
	};
	a.disconnect = function () {
		var b = Erizo.RoomEvent({type:"room-disconnected"});
		a.dispatchEvent(b);
	};
	a.publish = function (c, d, k) {
		d = d || {};
		d.maxVideoBW = d.maxVideoBW || b.defaultVideoBW;
		d.maxVideoBW > b.maxVideoBW && (d.maxVideoBW = b.maxVideoBW);
		if (c.local && void 0 === a.localStreams[c.getID()]) {
			if (c.hasAudio() || c.hasVideo() || c.hasScreen()) {
				if (void 0 !== c.url || void 0 !== c.recording) {
					var m, j;
					c.url ? (m = "url", j = c.url) : (m = "recording", j = c.recording);
					f("publish", {state:m, data:c.hasData(), audio:c.hasAudio(), video:c.hasVideo(), attributes:c.getAttributes()}, j, function (b, d) {
						if (b !== null) {
							L.Logger.info("Stream published");
							c.getID = function () {
								return b;
							};
							c.sendData = function (a) {
								g(c, a);
							};
							c.setAttributes = function (a) {
								h(c, a);
							};
							a.localStreams[b] = c;
							c.room = a;
							k && k(b);
						} else {
							L.Logger.error("Error when publishing the stream", d);
							k && k(void 0, d);
						}
					});
				} else {
					a.p2p ? (b.maxAudioBW = d.maxAudioBW, b.maxVideoBW = d.maxVideoBW, f("publish", {state:"p2p", data:c.hasData(), audio:c.hasAudio(), video:c.hasVideo(), screen:c.hasScreen(), attributes:c.getAttributes()}, void 0, function (b, d) {
						if (b === null) {
							L.Logger.error("Error when publishing the stream", d);
							k && k(void 0, d);
						}
						L.Logger.info("Stream published");
						c.getID = function () {
							return b;
						};
						if (c.hasData()) {
							c.sendData = function (a) {
								g(c, a);
							};
						}
						c.setAttributes = function (a) {
							h(c, a);
						};
						a.localStreams[b] = c;
						c.room = a;
					})) : f("publish", {state:"erizo", data:c.hasData(), audio:c.hasAudio(), video:c.hasVideo(), screen:c.hasScreen(), attributes:c.getAttributes()}, void 0, function (b, h) {
						if (b === null) {
							k && k(void 0, h);
						} else {
							L.Logger.info("Stream published");
							c.getID = function () {
								return b;
							};
							if (c.hasData()) {
								c.sendData = function (a) {
									g(c, a);
								};
							}
							a.localStreams[b] = c;
							c.room = a;
							c.pc = Erizo.Connection({callback:function (a) {
								console.log("Sending message", a);
								f("signaling_message", {streamId:c.getID(), msg:a}, void 0, function () {
								});
							}, stunServerUrl:a.stunServerUrl, turnServer:a.turnServer, maxAudioBW:d.maxAudioBW, maxVideoBW:d.maxVideoBW, audio:c.hasAudio(), video:c.hasVideo()});
							c.pc.addStream(c.stream);
							c.pc.createOffer();
							k && k(b);
						}
					});
				}
			} else {
				c.hasData() && f("publish", {state:"data", data:c.hasData(), audio:!1, video:!1, screen:!1, attributes:c.getAttributes()}, void 0, function (b, d) {
					if (b === null) {
						L.Logger.error("Error publishing stream ", d);
						k && k(void 0, d);
					} else {
						L.Logger.info("Stream published");
						c.getID = function () {
							return b;
						};
						c.sendData = function (a) {
							g(c, a);
						};
						c.setAttributes = function (a) {
							h(c, a);
						};
						a.localStreams[b] = c;
						c.room = a;
						k && k(b);
					}
				});
			}
		}
	};
	a.startRecording = function (a, b) {
		L.Logger.debug("Start Recording streamaa: " + a.getID());
		d("startRecorder", {to:a.getID()}, function (a, c) {
			null === a ? (L.Logger.error("Error on start recording", c), b && b(void 0, c)) : (L.Logger.info("Start recording", a), b && b(a));
		});
	};
	a.stopRecording = function (a, b) {
		d("stopRecorder", {id:a}, function (a, c) {
			null === a ? (L.Logger.error("Error on stop recording", c), b && b(void 0, c)) : (L.Logger.info("Stop recording"), b && b(!0));
		});
	};
	a.unpublish = function (b, c) {
		if (b.local) {
			d("unpublish", b.getID(), function (a, b) {
				null === a ? (L.Logger.error("Error unpublishing stream", b), c && c(void 0, b)) : (L.Logger.info("Stream unpublished"), c && c(!0));
			});
			var f = b.room.p2p;
			b.room = void 0;
			if ((b.hasAudio() || b.hasVideo() || b.hasScreen()) && void 0 === b.url && !f) {
				b.pc.close(), b.pc = void 0;
			}
			delete a.localStreams[b.getID()];
			b.getID = function () {
			};
			b.sendData = function () {
			};
			b.setAttributes = function () {
			};
		}
	};
	a.subscribe = function (b, c, d) {
		c = c || {};
		if (!b.local) {
			if (b.hasVideo() || b.hasAudio() || b.hasScreen()) {
				a.p2p ? (f("subscribe", {streamId:b.getID()}), d && d(!0)) : f("subscribe", {streamId:b.getID(), audio:c.audio, video:c.video, data:c.data, browser:Erizo.getBrowser()}, void 0, function (c, i) {
					null === c ? (L.Logger.error("Error subscribing to stream ", i), d && d(void 0, i)) : (L.Logger.info("Subscriber added"), b.pc = Erizo.Connection({callback:function (a) {
						L.Logger.info("Sending message", a);
						f("signaling_message", {streamId:b.getID(), msg:a, browser:b.pc.browser}, void 0, function () {
						});
					}, nop2p:!0, audio:b.hasAudio(), video:b.hasVideo(), stunServerUrl:a.stunServerUrl, turnServer:a.turnServer}), b.pc.onaddstream = function (c) {
						L.Logger.info("Stream subscribed");
						b.stream = c.stream;
						c = Erizo.StreamEvent({type:"stream-subscribed", stream:b});
						a.dispatchEvent(c);
					}, b.pc.createOffer(!0), d && d(!0));
				});
			} else {
				if (b.hasData() && !1 !== c.data) {
					f("subscribe", {streamId:b.getID(), data:c.data}, void 0, function (c, f) {
						if (null === c) {
							L.Logger.error("Error subscribing to stream ", f), d && d(void 0, f);
						} else {
							L.Logger.info("Stream subscribed");
							var i = Erizo.StreamEvent({type:"stream-subscribed", stream:b});
							a.dispatchEvent(i);
							d && d(!0);
						}
					});
				} else {
					L.Logger.info("Subscribing to anything");
					return;
				}
			}
			L.Logger.info("Subscribing to: " + b.getID());
		}
	};
	a.unsubscribe = function (b, c) {
		void 0 !== a.socket && (b.local || d("unsubscribe", b.getID(), function (a, d) {
			null === a ? c && c(void 0, d) : (j(b), c && c(!0));
		}, function () {
			L.Logger.error("Error calling unsubscribe.");
		}));
	};
	a.getStreamsByAttribute = function (b, c) {
		var d = [], f, g;
		for (f in a.remoteStreams) {
			a.remoteStreams.hasOwnProperty(f) && (g = a.remoteStreams[f], void 0 !== g.getAttributes() && void 0 !== g.getAttributes()[b] && g.getAttributes()[b] === c && d.push(g));
		}
		return d;
	};
	return a;
};
var L = L || {};
L.Logger = function (b) {
	return {DEBUG:0, TRACE:1, INFO:2, WARNING:3, ERROR:4, NONE:5, enableLogPanel:function () {
		b.Logger.panel = document.createElement("textarea");
		b.Logger.panel.setAttribute("id", "licode-logs");
		b.Logger.panel.setAttribute("style", "width: 100%; height: 100%; display: none");
		b.Logger.panel.setAttribute("rows", 20);
		b.Logger.panel.setAttribute("cols", 20);
		b.Logger.panel.setAttribute("readOnly", !0);
		document.body.appendChild(b.Logger.panel);
	}, setLogLevel:function (a) {
		a > b.Logger.NONE ? a = b.Logger.NONE : a < b.Logger.DEBUG && (a = b.Logger.DEBUG);
		b.Logger.logLevel = a;
	}, log:function (a) {
		var c = "";
		if (!(a < b.Logger.logLevel)) {
			a === b.Logger.DEBUG ? c += "DEBUG" : a === b.Logger.TRACE ? c += "TRACE" : a === b.Logger.INFO ? c += "INFO" : a === b.Logger.WARNING ? c += "WARNING" : a === b.Logger.ERROR && (c += "ERROR");
			for (var c = c + ": ", d = [], f = 0; f < arguments.length; f++) {
				d[f] = arguments[f];
			}
			d = d.slice(1);
			d = [c].concat(d);
			if (void 0 !== b.Logger.panel) {
				c = "";
				for (f = 0; f < d.length; f++) {
					c += d[f];
				}
				b.Logger.panel.value = b.Logger.panel.value + "\n" + c;
			} else {
				console.log.apply(console, d);
			}
		}
	}, debug:function () {
		for (var a = [], c = 0; c < arguments.length; c++) {
			a[c] = arguments[c];
		}
		b.Logger.log.apply(b.Logger, [b.Logger.DEBUG].concat(a));
	}, trace:function () {
		for (var a = [], c = 0; c < arguments.length; c++) {
			a[c] = arguments[c];
		}
		b.Logger.log.apply(b.Logger, [b.Logger.TRACE].concat(a));
	}, info:function () {
		for (var a = [], c = 0; c < arguments.length; c++) {
			a[c] = arguments[c];
		}
		b.Logger.log.apply(b.Logger, [b.Logger.INFO].concat(a));
	}, warning:function () {
		for (var a = [], c = 0; c < arguments.length; c++) {
			a[c] = arguments[c];
		}
		b.Logger.log.apply(b.Logger, [b.Logger.WARNING].concat(a));
	}, error:function () {
		for (var a = [], c = 0; c < arguments.length; c++) {
			a[c] = arguments[c];
		}
		b.Logger.log.apply(b.Logger, [b.Logger.ERROR].concat(a));
	}};
}(L);
L = L || {};
L.Base64 = function () {
	var b, a, c, d, f, g, h, j, e;
	b = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,0,1,2,3,4,5,6,7,8,9,+,/".split(",");
	a = [];
	for (f = 0; f < b.length; f += 1) {
		a[b[f]] = f;
	}
	g = function (a) {
		c = a;
		d = 0;
	};
	h = function () {
		var a;
		if (!c || d >= c.length) {
			return -1;
		}
		a = c.charCodeAt(d) & 255;
		d += 1;
		return a;
	};
	j = function () {
		if (!c) {
			return -1;
		}
		for (; ; ) {
			if (d >= c.length) {
				return -1;
			}
			var b = c.charAt(d);
			d += 1;
			if (a[b]) {
				return a[b];
			}
			if ("A" === b) {
				return 0;
			}
		}
	};
	e = function (a) {
		a = a.toString(16);
		1 === a.length && (a = "0" + a);
		return unescape("%" + a);
	};
	return {encodeBase64:function (a) {
		var c, d, e;
		g(a);
		a = "";
		c = Array(3);
		d = 0;
		for (e = !1; !e && -1 !== (c[0] = h()); ) {
			if (c[1] = h(), c[2] = h(), a += b[c[0] >> 2], -1 !== c[1] ? (a += b[c[0] << 4 & 48 | c[1] >> 4], -1 !== c[2] ? (a += b[c[1] << 2 & 60 | c[2] >> 6], a += b[c[2] & 63]) : (a += b[c[1] << 2 & 60], a += "=", e = !0)) : (a += b[c[0] << 4 & 48], a += "=", a += "=", e = !0), d += 4, 76 <= d) {
				a += "\n", d = 0;
			}
		}
		return a;
	}, decodeBase64:function (a) {
		var b, c;
		g(a);
		a = "";
		b = Array(4);
		for (c = !1; !c && -1 !== (b[0] = j()) && -1 !== (b[1] = j()); ) {
			b[2] = j(), b[3] = j(), a += e(b[0] << 2 & 255 | b[1] >> 4), -1 !== b[2] ? (a += e(b[1] << 4 & 255 | b[2] >> 2), -1 !== b[3] ? a += e(b[2] << 6 & 255 | b[3]) : c = !0) : c = !0;
		}
		return a;
	}};
}(L);
(function () {
	function b() {
		(new L.ElementQueries).init();
	}
	this.L = this.L || {};
	this.L.ElementQueries = function () {
		function a(a) {
			a || (a = document.documentElement);
			a = getComputedStyle(a, "fontSize");
			return parseFloat(a) || 16;
		}
		function b(c, d) {
			var f = d.replace(/[0-9]*/, ""), d = parseFloat(d);
			switch (f) {
			  case "px":
				return d;
			  case "em":
				return d * a(c);
			  case "rem":
				return d * a();
			  case "vw":
				return d * document.documentElement.clientWidth / 100;
			  case "vh":
				return d * document.documentElement.clientHeight / 100;
			  case "vmin":
			  case "vmax":
				return d * (0, Math["vmin" === f ? "min" : "max"])(document.documentElement.clientWidth / 100, document.documentElement.clientHeight / 100);
			  default:
				return d;
			}
		}
		function d(a) {
			this.element = a;
			this.options = [];
			var d, f, g, h = 0, j = 0, p, u, v, w, o;
			this.addOption = function (a) {
				this.options.push(a);
			};
			var r = ["min-width", "min-height", "max-width", "max-height"];
			this.call = function () {
				h = this.element.offsetWidth;
				j = this.element.offsetHeight;
				v = {};
				d = 0;
				for (f = this.options.length; d < f; d++) {
					g = this.options[d], p = b(this.element, g.value), u = "width" == g.property ? h : j, o = g.mode + "-" + g.property, w = "", "min" == g.mode && u >= p && (w += g.value), "max" == g.mode && u <= p && (w += g.value), v[o] || (v[o] = ""), w && -1 === (" " + v[o] + " ").indexOf(" " + w + " ") && (v[o] += " " + w);
				}
				for (var a in r) {
					v[r[a]] ? this.element.setAttribute(r[a], v[r[a]].substr(1)) : this.element.removeAttribute(r[a]);
				}
			};
		}
		function f(a, b) {
			a.elementQueriesSetupInformation ? a.elementQueriesSetupInformation.addOption(b) : (a.elementQueriesSetupInformation = new d(a), a.elementQueriesSetupInformation.addOption(b), new ResizeSensor(a, function () {
				a.elementQueriesSetupInformation.call();
			}));
			a.elementQueriesSetupInformation.call();
		}
		function g(a) {
			for (var b, a = a.replace(/'/g, "\""); null !== (b = j.exec(a)); ) {
				if (5 < b.length) {
					var c = b[1] || b[5], d = b[2], g = b[3];
					b = b[4];
					var h = void 0;
					document.querySelectorAll && (h = document.querySelectorAll.bind(document));
					!h && "undefined" !== typeof $$ && (h = $$);
					!h && "undefined" !== typeof jQuery && (h = jQuery);
					if (!h) {
						throw "No document.querySelectorAll, jQuery or Mootools's $$ found.";
					}
					for (var c = h(c), h = 0, p = c.length; h < p; h++) {
						f(c[h], {mode:d, property:g, value:b});
					}
				}
			}
		}
		function h(a) {
			var b = "";
			if (a) {
				if ("string" === typeof a) {
					a = a.toLowerCase(), (-1 !== a.indexOf("min-width") || -1 !== a.indexOf("max-width")) && g(a);
				} else {
					for (var c = 0, d = a.length; c < d; c++) {
						1 === a[c].type ? (b = a[c].selectorText || a[c].cssText, -1 !== b.indexOf("min-height") || -1 !== b.indexOf("max-height") ? g(b) : (-1 !== b.indexOf("min-width") || -1 !== b.indexOf("max-width")) && g(b)) : 4 === a[c].type && h(a[c].cssRules || a[c].rules);
					}
				}
			}
		}
		var j = /,?([^,\n]*)\[[\s\t]*(min|max)-(width|height)[\s\t]*[~$\^]?=[\s\t]*"([^"]*)"[\s\t]*]([^\n\s\{]*)/mgi;
		this.init = function () {
			for (var a = 0, b = document.styleSheets.length; a < b; a++) {
				h(document.styleSheets[a].cssText || document.styleSheets[a].cssRules || document.styleSheets[a].rules);
			}
		};
	};
	window.addEventListener ? window.addEventListener("load", b, !1) : window.attachEvent("onload", b);
	this.L.ResizeSensor = function (a, b) {
		function d(a, b) {
			window.OverflowEvent ? a.addEventListener("overflowchanged", function (a) {
				b.call(this, a);
			}) : (a.addEventListener("overflow", function (a) {
				b.call(this, a);
			}), a.addEventListener("underflow", function (a) {
				b.call(this, a);
			}));
		}
		function f() {
			this.q = [];
			this.add = function (a) {
				this.q.push(a);
			};
			var a, b;
			this.call = function () {
				a = 0;
				for (b = this.q.length; a < b; a++) {
					this.q[a].call();
				}
			};
		}
		function g(a, b) {
			function c() {
				var b = !1, d = a.resizeSensor.offsetWidth, f = a.resizeSensor.offsetHeight;
				h != d && (p.width = d - 1 + "px", u.width = d + 1 + "px", b = !0, h = d);
				j != f && (p.height = f - 1 + "px", u.height = f + 1 + "px", b = !0, j = f);
				return b;
			}
			if (a.resizedAttached) {
				if (a.resizedAttached) {
					a.resizedAttached.add(b);
					return;
				}
			} else {
				a.resizedAttached = new f, a.resizedAttached.add(b);
			}
			var g = function () {
				c() && a.resizedAttached.call();
			};
			a.resizeSensor = document.createElement("div");
			a.resizeSensor.className = "resize-sensor";
			a.resizeSensor.style.cssText = "position: absolute; left: 0; top: 0; right: 0; bottom: 0; overflow: hidden; z-index: -1;";
			a.resizeSensor.innerHTML = "<div class=\"resize-sensor-overflow\" style=\"position: absolute; left: 0; top: 0; right: 0; bottom: 0; overflow: hidden; z-index: -1;\"><div></div></div><div class=\"resize-sensor-underflow\" style=\"position: absolute; left: 0; top: 0; right: 0; bottom: 0; overflow: hidden; z-index: -1;\"><div></div></div>";
			a.appendChild(a.resizeSensor);
			if ("absolute" !== (a.currentStyle ? a.currentStyle.position : window.getComputedStyle ? window.getComputedStyle(a, null).getPropertyValue("position") : a.style.position)) {
				a.style.position = "relative";
			}
			var h = -1, j = -1, p = a.resizeSensor.firstElementChild.firstChild.style, u = a.resizeSensor.lastElementChild.firstChild.style;
			c();
			d(a.resizeSensor, g);
			d(a.resizeSensor.firstElementChild, g);
			d(a.resizeSensor.lastElementChild, g);
		}
		if ("array" === typeof a || "undefined" !== typeof jQuery && a instanceof jQuery || "undefined" !== typeof Elements && a instanceof Elements) {
			for (var h = 0, j = a.length; h < j; h++) {
				g(a[h], b);
			}
		} else {
			g(a, b);
		}
	};
})();
Erizo = Erizo || {};
Erizo.View = function () {
	var b = Erizo.EventDispatcher({});
	b.url = "http://chotis2.dit.upm.es:3000";
	return b;
};
Erizo = Erizo || {};
Erizo.VideoPlayer = function (b) {
	var a = Erizo.View({});
	a.id = b.id;
	a.stream = b.stream.stream;
	a.elementID = b.elementID;
	a.destroy = function () {
		a.video.pause();
		delete a.resizer;
		a.parentNode.removeChild(a.div);
	};
	a.resize = function () {
		var c = a.container.offsetWidth, d = a.container.offsetHeight;
		if (b.stream.screen || !1 === b.options.crop) {
			0.75 * c < d ? (a.video.style.width = c + "px", a.video.style.height = 0.75 * c + "px", a.video.style.top = -(0.75 * c / 2 - d / 2) + "px", a.video.style.left = "0px") : (a.video.style.height = d + "px", a.video.style.width = 4 / 3 * d + "px", a.video.style.left = -(4 / 3 * d / 2 - c / 2) + "px", a.video.style.top = "0px");
		} else {
			if (c !== a.containerWidth || d !== a.containerHeight) {
				0.75 * c > d ? (a.video.style.width = c + "px", a.video.style.height = 0.75 * c + "px", a.video.style.top = -(0.75 * c / 2 - d / 2) + "px", a.video.style.left = "0px") : (a.video.style.height = d + "px", a.video.style.width = 4 / 3 * d + "px", a.video.style.left = -(4 / 3 * d / 2 - c / 2) + "px", a.video.style.top = "0px");
			}
		}
		a.containerWidth = c;
		a.containerHeight = d;
	};
	L.Logger.debug("Creating URL from stream " + a.stream);
	a.stream_url = (window.URL || webkitURL).createObjectURL(a.stream);
	a.div = document.createElement("div");
	a.div.setAttribute("id", "player_" + a.id);
	a.div.setAttribute("style", "width: 100%; height: 100%; position: relative; background-color: black; overflow: hidden;");
	a.loader = document.createElement("img");
	a.loader.setAttribute("style", "width: 16px; height: 16px; position: absolute; top: 50%; left: 50%; margin-top: -8px; margin-left: -8px");
	a.loader.setAttribute("id", "back_" + a.id);
	a.loader.setAttribute("src", a.url + "/assets/loader.gif");
	a.video = document.createElement("video");
	a.video.setAttribute("id", "stream" + a.id);
	a.video.setAttribute("style", "width: 100%; height: 100%; position: absolute");
	a.video.setAttribute("autoplay", "autoplay");
	b.stream.local && (a.video.volume = 0);
	void 0 !== a.elementID ? (document.getElementById(a.elementID).appendChild(a.div), a.container = document.getElementById(a.elementID)) : (document.body.appendChild(a.div), a.container = document.body);
	a.parentNode = a.div.parentNode;
	a.div.appendChild(a.loader);
	a.div.appendChild(a.video);
	a.containerWidth = 0;
	a.containerHeight = 0;
	a.resizer = new L.ResizeSensor(a.container, a.resize);
	a.resize();
	a.bar = new Erizo.Bar({elementID:"player_" + a.id, id:a.id, stream:b.stream, media:a.video, options:b.options});
	a.div.onmouseover = function () {
		a.bar.display();
	};
	a.div.onmouseout = function () {
		a.bar.hide();
	};
	a.video.src = a.stream_url;
	return a;
};
Erizo = Erizo || {};
Erizo.AudioPlayer = function (b) {
	var a = Erizo.View({}), c, d;
	a.id = b.id;
	a.stream = b.stream.stream;
	a.elementID = b.elementID;
	L.Logger.debug("Creating URL from stream " + a.stream);
	a.stream_url = (window.URL || webkitURL).createObjectURL(a.stream);
	a.audio = document.createElement("audio");
	a.audio.setAttribute("id", "stream" + a.id);
	a.audio.setAttribute("style", "width: 100%; height: 100%; position: absolute");
	a.audio.setAttribute("autoplay", "autoplay");
	b.stream.local && (a.audio.volume = 0);
	b.stream.local && (a.audio.volume = 0);
	void 0 !== a.elementID ? (a.destroy = function () {
		a.audio.pause();
		a.parentNode.removeChild(a.div);
	}, c = function () {
		a.bar.display();
	}, d = function () {
		a.bar.hide();
	}, a.div = document.createElement("div"), a.div.setAttribute("id", "player_" + a.id), a.div.setAttribute("style", "width: 100%; height: 100%; position: relative; overflow: hidden;"), document.getElementById(a.elementID).appendChild(a.div), a.container = document.getElementById(a.elementID), a.parentNode = a.div.parentNode, a.div.appendChild(a.audio), a.bar = new Erizo.Bar({elementID:"player_" + a.id, id:a.id, stream:b.stream, media:a.audio, options:b.options}), a.div.onmouseover = c, a.div.onmouseout = d) : (a.destroy = function () {
		a.audio.pause();
		a.parentNode.removeChild(a.audio);
	}, document.body.appendChild(a.audio), a.parentNode = document.body);
	a.audio.src = a.stream_url;
	return a;
};
Erizo = Erizo || {};
Erizo.Bar = function (b) {
	var a = Erizo.View({}), c, d;
	a.elementID = b.elementID;
	a.id = b.id;
	a.div = document.createElement("div");
	a.div.setAttribute("id", "bar_" + a.id);
	a.bar = document.createElement("div");
	a.bar.setAttribute("style", "width: 100%; height: 15%; max-height: 30px; position: absolute; bottom: 0; right: 0; background-color: rgba(255,255,255,0.62)");
	a.bar.setAttribute("id", "subbar_" + a.id);
	a.link = document.createElement("a");
	a.link.setAttribute("href", "http://www.lynckia.com/");
	a.link.setAttribute("target", "_blank");
	a.logo = document.createElement("img");
	a.logo.setAttribute("style", "width: 100%; height: 100%; max-width: 30px; position: absolute; top: 0; left: 2px;");
	a.logo.setAttribute("alt", "Lynckia");
	a.logo.setAttribute("src", a.url + "/assets/star.svg");
	d = function (b) {
		"block" !== b ? b = "none" : clearTimeout(c);
		a.div.setAttribute("style", "width: 100%; height: 100%; position: relative; bottom: 0; right: 0; display:" + b);
	};
	a.display = function () {
		d("block");
	};
	a.hide = function () {
		c = setTimeout(d, 1000);
	};
	document.getElementById(a.elementID).appendChild(a.div);
	a.div.appendChild(a.bar);
	a.bar.appendChild(a.link);
	a.link.appendChild(a.logo);
	if (!b.stream.screen && (void 0 === b.options || void 0 === b.options.speaker || !0 === b.options.speaker)) {
		a.speaker = new Erizo.Speaker({elementID:"subbar_" + a.id, id:a.id, stream:b.stream, media:b.media});
	}
	a.display();
	a.hide();
	return a;
};
Erizo = Erizo || {};
Erizo.Speaker = function (b) {
	var a = Erizo.View({}), c, d, f, g = 50;
	a.elementID = b.elementID;
	a.media = b.media;
	a.id = b.id;
	a.stream = b.stream;
	a.div = document.createElement("div");
	a.div.setAttribute("style", "width: 40%; height: 100%; max-width: 32px; position: absolute; right: 0;z-index:0;");
	a.icon = document.createElement("img");
	a.icon.setAttribute("id", "volume_" + a.id);
	a.icon.setAttribute("src", a.url + "/assets/sound48.png");
	a.icon.setAttribute("style", "width: 80%; height: 100%; position: absolute;");
	a.div.appendChild(a.icon);
	a.stream.local ? (d = function () {
		a.media.muted = !0;
		a.icon.setAttribute("src", a.url + "/assets/mute48.png");
		a.stream.stream.getAudioTracks()[0].enabled = !1;
	}, f = function () {
		a.media.muted = !1;
		a.icon.setAttribute("src", a.url + "/assets/sound48.png");
		a.stream.stream.getAudioTracks()[0].enabled = !0;
	}, a.icon.onclick = function () {
		a.media.muted ? f() : d();
	}) : (a.picker = document.createElement("input"), a.picker.setAttribute("id", "picker_" + a.id), a.picker.type = "range", a.picker.min = 0, a.picker.max = 100, a.picker.step = 10, a.picker.value = g, a.picker.setAttribute("orient", "vertical"), a.div.appendChild(a.picker), a.media.volume = a.picker.value / 100, a.media.muted = !1, a.picker.oninput = function () {
		0 < a.picker.value ? (a.media.muted = !1, a.icon.setAttribute("src", a.url + "/assets/sound48.png")) : (a.media.muted = !0, a.icon.setAttribute("src", a.url + "/assets/mute48.png"));
		a.media.volume = a.picker.value / 100;
	}, c = function (b) {
		a.picker.setAttribute("style", "background: transparent; width: 32px; height: 100px; position: absolute; bottom: 90%; z-index: 1;" + a.div.offsetHeight + "px; right: 0px; -webkit-appearance: slider-vertical; display: " + b);
	}, d = function () {
		a.icon.setAttribute("src", a.url + "/assets/mute48.png");
		g = a.picker.value;
		a.picker.value = 0;
		a.media.volume = 0;
		a.media.muted = !0;
	}, f = function () {
		a.icon.setAttribute("src", a.url + "/assets/sound48.png");
		a.picker.value = g;
		a.media.volume = a.picker.value / 100;
		a.media.muted = !1;
	}, a.icon.onclick = function () {
		a.media.muted ? f() : d();
	}, a.div.onmouseover = function () {
		c("block");
	}, a.div.onmouseout = function () {
		c("none");
	}, c("none"));
	document.getElementById(a.elementID).appendChild(a.div);
	return a;
};


