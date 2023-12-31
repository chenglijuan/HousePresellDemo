/* xlsx.js (C) 2013-present SheetJS -- http://sheetjs.com */
var DO_NOT_EXPORT_CODEPAGE = true;
var DO_NOT_EXPORT_JSZIP = true;
(function(e) { if("object" == typeof exports && "undefined" != typeof module && "undefined" == typeof DO_NOT_EXPORT_JSZIP) module.exports = e();
	else if("function" == typeof define && define.amd) { JSZipSync = e();
		define([], e) } else { var r; "undefined" != typeof window ? r = window : "undefined" != typeof global ? r = global : "undefined" != typeof $ && $.global ? r = $.global : "undefined" != typeof self && (r = self), r.JSZipSync = e() } })(function() {
	var e, r, t;
	return function a(e, r, t) {
		function n(s, f) { if(!r[s]) { if(!e[s]) { var o = typeof require == "function" && require; if(!f && o) return o(s, !0); if(i) return i(s, !0); throw new Error("Cannot find module '" + s + "'") } var l = r[s] = { exports: {} };
				e[s][0].call(l.exports, function(r) { var t = e[s][1][r]; return n(t ? t : r) }, l, l.exports, a, e, r, t) } return r[s].exports } var i = typeof require == "function" && require; for(var s = 0; s < t.length; s++) n(t[s]); return n }({
		1: [function(e, r, t) { "use strict"; var a = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
			t.encode = function(e, r) { var t = ""; var n, i, s, f, o, l, c; var h = 0; while(h < e.length) { n = e.charCodeAt(h++);
					i = e.charCodeAt(h++);
					s = e.charCodeAt(h++);
					f = n >> 2;
					o = (n & 3) << 4 | i >> 4;
					l = (i & 15) << 2 | s >> 6;
					c = s & 63; if(isNaN(i)) { l = c = 64 } else if(isNaN(s)) { c = 64 } t = t + a.charAt(f) + a.charAt(o) + a.charAt(l) + a.charAt(c) } return t };
			t.decode = function(e, r) { var t = ""; var n, i, s; var f, o, l, c; var h = 0;
				e = e.replace(/[^A-Za-z0-9\+\/\=]/g, ""); while(h < e.length) { f = a.indexOf(e.charAt(h++));
					o = a.indexOf(e.charAt(h++));
					l = a.indexOf(e.charAt(h++));
					c = a.indexOf(e.charAt(h++));
					n = f << 2 | o >> 4;
					i = (o & 15) << 4 | l >> 2;
					s = (l & 3) << 6 | c;
					t = t + String.fromCharCode(n); if(l != 64) { t = t + String.fromCharCode(i) } if(c != 64) { t = t + String.fromCharCode(s) } } return t } }, {}],
		2: [function(e, r, t) { "use strict";

			function a() { this.compressedSize = 0;
				this.uncompressedSize = 0;
				this.crc32 = 0;
				this.compressionMethod = null;
				this.compressedContent = null } a.prototype = { getContent: function() { return null }, getCompressedContent: function() { return null } };
			r.exports = a }, {}],
		3: [function(e, r, t) { "use strict";
			t.STORE = { magic: "\0\0", compress: function(e) { return e }, uncompress: function(e) { return e }, compressInputType: null, uncompressInputType: null };
			t.DEFLATE = e("./flate") }, { "./flate": 8 }],
		4: [function(e, r, t) { "use strict"; var a = e("./utils"); var n = [0, 1996959894, 3993919788, 2567524794, 124634137, 1886057615, 3915621685, 2657392035, 249268274, 2044508324, 3772115230, 2547177864, 162941995, 2125561021, 3887607047, 2428444049, 498536548, 1789927666, 4089016648, 2227061214, 450548861, 1843258603, 4107580753, 2211677639, 325883990, 1684777152, 4251122042, 2321926636, 335633487, 1661365465, 4195302755, 2366115317, 997073096, 1281953886, 3579855332, 2724688242, 1006888145, 1258607687, 3524101629, 2768942443, 901097722, 1119000684, 3686517206, 2898065728, 853044451, 1172266101, 3705015759, 2882616665, 651767980, 1373503546, 3369554304, 3218104598, 565507253, 1454621731, 3485111705, 3099436303, 671266974, 1594198024, 3322730930, 2970347812, 795835527, 1483230225, 3244367275, 3060149565, 1994146192, 31158534, 2563907772, 4023717930, 1907459465, 112637215, 2680153253, 3904427059, 2013776290, 251722036, 2517215374, 3775830040, 2137656763, 141376813, 2439277719, 3865271297, 1802195444, 476864866, 2238001368, 4066508878, 1812370925, 453092731, 2181625025, 4111451223, 1706088902, 314042704, 2344532202, 4240017532, 1658658271, 366619977, 2362670323, 4224994405, 1303535960, 984961486, 2747007092, 3569037538, 1256170817, 1037604311, 2765210733, 3554079995, 1131014506, 879679996, 2909243462, 3663771856, 1141124467, 855842277, 2852801631, 3708648649, 1342533948, 654459306, 3188396048, 3373015174, 1466479909, 544179635, 3110523913, 3462522015, 1591671054, 702138776, 2966460450, 3352799412, 1504918807, 783551873, 3082640443, 3233442989, 3988292384, 2596254646, 62317068, 1957810842, 3939845945, 2647816111, 81470997, 1943803523, 3814918930, 2489596804, 225274430, 2053790376, 3826175755, 2466906013, 167816743, 2097651377, 4027552580, 2265490386, 503444072, 1762050814, 4150417245, 2154129355, 426522225, 1852507879, 4275313526, 2312317920, 282753626, 1742555852, 4189708143, 2394877945, 397917763, 1622183637, 3604390888, 2714866558, 953729732, 1340076626, 3518719985, 2797360999, 1068828381, 1219638859, 3624741850, 2936675148, 906185462, 1090812512, 3747672003, 2825379669, 829329135, 1181335161, 3412177804, 3160834842, 628085408, 1382605366, 3423369109, 3138078467, 570562233, 1426400815, 3317316542, 2998733608, 733239954, 1555261956, 3268935591, 3050360625, 752459403, 1541320221, 2607071920, 3965973030, 1969922972, 40735498, 2617837225, 3943577151, 1913087877, 83908371, 2512341634, 3803740692, 2075208622, 213261112, 2463272603, 3855990285, 2094854071, 198958881, 2262029012, 4057260610, 1759359992, 534414190, 2176718541, 4139329115, 1873836001, 414664567, 2282248934, 4279200368, 1711684554, 285281116, 2405801727, 4167216745, 1634467795, 376229701, 2685067896, 3608007406, 1308918612, 956543938, 2808555105, 3495958263, 1231636301, 1047427035, 2932959818, 3654703836, 1088359270, 936918e3, 2847714899, 3736837829, 1202900863, 817233897, 3183342108, 3401237130, 1404277552, 615818150, 3134207493, 3453421203, 1423857449, 601450431, 3009837614, 3294710456, 1567103746, 711928724, 3020668471, 3272380065, 1510334235, 755167117];
			r.exports = function i(e, r) { if(typeof e === "undefined" || !e.length) { return 0 } var t = a.getTypeOf(e) !== "string"; if(typeof r == "undefined") { r = 0 } var i = 0; var s = 0; var f = 0;
				r = r ^ -1; for(var o = 0, l = e.length; o < l; o++) { f = t ? e[o] : e.charCodeAt(o);
					s = (r ^ f) & 255;
					i = n[s];
					r = r >>> 8 ^ i } return r ^ -1 } }, { "./utils": 21 }],
		5: [function(e, r, t) { "use strict"; var a = e("./utils");

			function n(e) { this.data = null;
				this.length = 0;
				this.index = 0 } n.prototype = { checkOffset: function(e) { this.checkIndex(this.index + e) }, checkIndex: function(e) { if(this.length < e || e < 0) { throw new Error("End of data reached (data length = " + this.length + ", asked index = " + e + "). Corrupted zip ?") } }, setIndex: function(e) { this.checkIndex(e);
					this.index = e }, skip: function(e) { this.setIndex(this.index + e) }, byteAt: function(e) {}, readInt: function(e) { var r = 0,
						t;
					this.checkOffset(e); for(t = this.index + e - 1; t >= this.index; t--) { r = (r << 8) + this.byteAt(t) } this.index += e; return r }, readString: function(e) { return a.transformTo("string", this.readData(e)) }, readData: function(e) {}, lastIndexOfSignature: function(e) {}, readDate: function() { var e = this.readInt(4); return new Date((e >> 25 & 127) + 1980, (e >> 21 & 15) - 1, e >> 16 & 31, e >> 11 & 31, e >> 5 & 63, (e & 31) << 1) } };
			r.exports = n }, { "./utils": 21 }],
		6: [function(e, r, t) { "use strict";
			t.base64 = false;
			t.binary = false;
			t.dir = false;
			t.createFolders = false;
			t.date = null;
			t.compression = null;
			t.comment = null }, {}],
		7: [function(e, r, t) { "use strict"; var a = e("./utils");
			t.string2binary = function(e) { return a.string2binary(e) };
			t.string2Uint8Array = function(e) { return a.transformTo("uint8array", e) };
			t.uint8Array2String = function(e) { return a.transformTo("string", e) };
			t.string2Blob = function(e) { var r = a.transformTo("arraybuffer", e); return a.arrayBuffer2Blob(r) };
			t.arrayBuffer2Blob = function(e) { return a.arrayBuffer2Blob(e) };
			t.transformTo = function(e, r) { return a.transformTo(e, r) };
			t.getTypeOf = function(e) { return a.getTypeOf(e) };
			t.checkSupport = function(e) { return a.checkSupport(e) };
			t.MAX_VALUE_16BITS = a.MAX_VALUE_16BITS;
			t.MAX_VALUE_32BITS = a.MAX_VALUE_32BITS;
			t.pretty = function(e) { return a.pretty(e) };
			t.findCompression = function(e) { return a.findCompression(e) };
			t.isRegExp = function(e) { return a.isRegExp(e) } }, { "./utils": 21 }],
		8: [function(e, r, t) { "use strict"; var a = typeof Uint8Array !== "undefined" && typeof Uint16Array !== "undefined" && typeof Uint32Array !== "undefined"; var n = e("pako");
			t.uncompressInputType = a ? "uint8array" : "array";
			t.compressInputType = a ? "uint8array" : "array";
			t.magic = "\b\0";
			t.compress = function(e) { return n.deflateRaw(e) };
			t.uncompress = function(e) { return n.inflateRaw(e) } }, { pako: 24 }],
		9: [function(e, r, t) { "use strict"; var a = e("./base64");

			function n(e, r) { if(!(this instanceof n)) return new n(e, r);
				this.files = {};
				this.comment = null;
				this.root = ""; if(e) { this.load(e, r) } this.clone = function() { var e = new n; for(var r in this) { if(typeof this[r] !== "function") { e[r] = this[r] } } return e } } n.prototype = e("./object");
			n.prototype.load = e("./load");
			n.support = e("./support");
			n.defaults = e("./defaults");
			n.utils = e("./deprecatedPublicUtils");
			n.base64 = { encode: function(e) { return a.encode(e) }, decode: function(e) { return a.decode(e) } };
			n.compressions = e("./compressions");
			r.exports = n }, { "./base64": 1, "./compressions": 3, "./defaults": 6, "./deprecatedPublicUtils": 7, "./load": 10, "./object": 13, "./support": 17 }],
		10: [function(e, r, t) { "use strict"; var a = e("./base64"); var n = e("./zipEntries");
			r.exports = function(e, r) { var t, i, s, f;
				r = r || {}; if(r.base64) { e = a.decode(e) } i = new n(e, r);
				t = i.files; for(s = 0; s < t.length; s++) { f = t[s];
					this.file(f.fileName, f.decompressed, { binary: true, optimizedBinaryString: true, date: f.date, dir: f.dir, comment: f.fileComment.length ? f.fileComment : null, createFolders: r.createFolders }) } if(i.zipComment.length) { this.comment = i.zipComment } return this } }, { "./base64": 1, "./zipEntries": 22 }],
		11: [function(e, r, t) {
			(function(e) { "use strict"; if(typeof e !== "undefined") { if(!e.from) e.from = function(r, t) { return t ? new e(r, t) : new e(r) }; if(!e.alloc) e.alloc = function(r) { return new e(r) } } r.exports = function(r, t) { return typeof r == "number" ? e.alloc(r) : e.from(r, t) };
				r.exports.test = function(r) { return e.isBuffer(r) } }).call(this, typeof Buffer !== "undefined" ? Buffer : undefined) }, {}],
		12: [function(e, r, t) { "use strict"; var a = e("./uint8ArrayReader");

			function n(e) { this.data = e;
				this.length = this.data.length;
				this.index = 0 } n.prototype = new a;
			n.prototype.readData = function(e) { this.checkOffset(e); var r = this.data.slice(this.index, this.index + e);
				this.index += e; return r };
			r.exports = n }, { "./uint8ArrayReader": 18 }],
		13: [function(e, r, t) { "use strict"; var a = e("./support"); var n = e("./utils"); var i = e("./crc32"); var s = e("./signature"); var f = e("./defaults"); var o = e("./base64"); var l = e("./compressions"); var c = e("./compressedObject"); var h = e("./nodeBuffer"); var u = e("./utf8"); var d = e("./stringWriter"); var p = e("./uint8ArrayWriter"); var v = function(e) { if(e._data instanceof c) { e._data = e._data.getContent();
					e.options.binary = true;
					e.options.base64 = false; if(n.getTypeOf(e._data) === "uint8array") { var r = e._data;
						e._data = new Uint8Array(r.length); if(r.length !== 0) { e._data.set(r, 0) } } } return e._data }; var g = function(e) { var r = v(e),
					t = n.getTypeOf(r); if(t === "string") { if(!e.options.binary) { if(a.nodebuffer) { return h(r, "utf-8") } } return e.asBinary() } return r }; var m = function(e) { var r = v(this); if(r === null || typeof r === "undefined") { return "" } if(this.options.base64) { r = o.decode(r) } if(e && this.options.binary) { r = T.utf8decode(r) } else { r = n.transformTo("string", r) } if(!e && !this.options.binary) { r = n.transformTo("string", T.utf8encode(r)) } return r }; var b = function(e, r, t) { this.name = e;
				this.dir = t.dir;
				this.date = t.date;
				this.comment = t.comment;
				this._data = r;
				this.options = t;
				this._initialMetadata = { dir: t.dir, date: t.date } };
			b.prototype = { asText: function() { return m.call(this, true) }, asBinary: function() { return m.call(this, false) }, asNodeBuffer: function() { var e = g(this); return n.transformTo("nodebuffer", e) }, asUint8Array: function() { var e = g(this); return n.transformTo("uint8array", e) }, asArrayBuffer: function() { return this.asUint8Array().buffer } }; var C = function(e, r) { var t = "",
					a; for(a = 0; a < r; a++) { t += String.fromCharCode(e & 255);
					e = e >>> 8 } return t }; var w = function() { var e = {},
					r, t; for(r = 0; r < arguments.length; r++) { for(t in arguments[r]) { if(arguments[r].hasOwnProperty(t) && typeof e[t] === "undefined") { e[t] = arguments[r][t] } } } return e }; var E = function(e) { e = e || {}; if(e.base64 === true && (e.binary === null || e.binary === undefined)) { e.binary = true } e = w(e, f);
				e.date = e.date || new Date; if(e.compression !== null) e.compression = e.compression.toUpperCase(); return e }; var k = function(e, r, t) { var a = n.getTypeOf(r),
					i;
				t = E(t); if(t.createFolders && (i = S(e))) { A.call(this, i, true) } if(t.dir || r === null || typeof r === "undefined") { t.base64 = false;
					t.binary = false;
					r = null } else if(a === "string") { if(t.binary && !t.base64) { if(t.optimizedBinaryString !== true) { r = n.string2binary(r) } } } else { t.base64 = false;
					t.binary = true; if(!a && !(r instanceof c)) { throw new Error("The data of '" + e + "' is in an unsupported format !") } if(a === "arraybuffer") { r = n.transformTo("uint8array", r) } } var s = new b(e, r, t);
				this.files[e] = s; return s }; var S = function(e) { if(e.slice(-1) == "/") { e = e.substring(0, e.length - 1) } var r = e.lastIndexOf("/"); return r > 0 ? e.substring(0, r) : "" }; var A = function(e, r) { if(e.slice(-1) != "/") { e += "/" } r = typeof r !== "undefined" ? r : false; if(!this.files[e]) { k.call(this, e, null, { dir: true, createFolders: r }) } return this.files[e] }; var _ = function(e, r) { var t = new c,
					a; if(e._data instanceof c) { t.uncompressedSize = e._data.uncompressedSize;
					t.crc32 = e._data.crc32; if(t.uncompressedSize === 0 || e.dir) { r = l["STORE"];
						t.compressedContent = "";
						t.crc32 = 0 } else if(e._data.compressionMethod === r.magic) { t.compressedContent = e._data.getCompressedContent() } else { a = e._data.getContent();
						t.compressedContent = r.compress(n.transformTo(r.compressInputType, a)) } } else { a = g(e); if(!a || a.length === 0 || e.dir) { r = l["STORE"];
						a = "" } t.uncompressedSize = a.length;
					t.crc32 = i(a);
					t.compressedContent = r.compress(n.transformTo(r.compressInputType, a)) } t.compressedSize = t.compressedContent.length;
				t.compressionMethod = r.magic; return t }; var B = function(e, r, t, a) { var f = t.compressedContent,
					o = n.transformTo("string", u.utf8encode(r.name)),
					l = r.comment || "",
					c = n.transformTo("string", u.utf8encode(l)),
					h = o.length !== r.name.length,
					d = c.length !== l.length,
					p = r.options,
					v, g, m = "",
					b = "",
					w = "",
					E, k; if(r._initialMetadata.dir !== r.dir) { E = r.dir } else { E = p.dir } if(r._initialMetadata.date !== r.date) { k = r.date } else { k = p.date } v = k.getHours();
				v = v << 6;
				v = v | k.getMinutes();
				v = v << 5;
				v = v | k.getSeconds() / 2;
				g = k.getFullYear() - 1980;
				g = g << 4;
				g = g | k.getMonth() + 1;
				g = g << 5;
				g = g | k.getDate(); if(h) { b = C(1, 1) + C(i(o), 4) + o;
					m += "up" + C(b.length, 2) + b } if(d) { w = C(1, 1) + C(this.crc32(c), 4) + c;
					m += "uc" + C(w.length, 2) + w } var S = "";
				S += "\n\0";
				S += h || d ? "\0\b" : "\0\0";
				S += t.compressionMethod;
				S += C(v, 2);
				S += C(g, 2);
				S += C(t.crc32, 4);
				S += C(t.compressedSize, 4);
				S += C(t.uncompressedSize, 4);
				S += C(o.length, 2);
				S += C(m.length, 2); var A = s.LOCAL_FILE_HEADER + S + o + m; var _ = s.CENTRAL_FILE_HEADER + "\0" + S + C(c.length, 2) + "\0\0" + "\0\0" + (E === true ? "\0\0\0" : "\0\0\0\0") + C(a, 4) + o + m + c; return { fileRecord: A, dirRecord: _, compressedObject: t } }; var T = { load: function(e, r) { throw new Error("Load method is not defined. Is the file jszip-load.js included ?") }, filter: function(e) { var r = [],
						t, a, n, i; for(t in this.files) { if(!this.files.hasOwnProperty(t)) { continue } n = this.files[t];
						i = new b(n.name, n._data, w(n.options));
						a = t.slice(this.root.length, t.length); if(t.slice(0, this.root.length) === this.root && e(a, i)) { r.push(i) } } return r }, file: function(e, r, t) { if(arguments.length === 1) { if(n.isRegExp(e)) { var a = e; return this.filter(function(e, r) { return !r.dir && a.test(e) }) } else { return this.filter(function(r, t) { return !t.dir && r === e })[0] || null } } else { e = this.root + e;
						k.call(this, e, r, t) } return this }, folder: function(e) { if(!e) { return this } if(n.isRegExp(e)) { return this.filter(function(r, t) { return t.dir && e.test(r) }) } var r = this.root + e; var t = A.call(this, r); var a = this.clone();
					a.root = t.name; return a }, remove: function(e) { e = this.root + e; var r = this.files[e]; if(!r) { if(e.slice(-1) != "/") { e += "/" } r = this.files[e] } if(r && !r.dir) { delete this.files[e] } else { var t = this.filter(function(r, t) { return t.name.slice(0, e.length) === e }); for(var a = 0; a < t.length; a++) { delete this.files[t[a].name] } } return this }, generate: function(e) { e = w(e || {}, { base64: true, compression: "STORE", type: "base64", comment: null });
					n.checkSupport(e.type); var r = [],
						t = 0,
						a = 0,
						i, f, c = n.transformTo("string", this.utf8encode(e.comment || this.comment || "")); for(var h in this.files) { if(!this.files.hasOwnProperty(h)) { continue } var u = this.files[h]; var v = u.options.compression || e.compression.toUpperCase(); var g = l[v]; if(!g) { throw new Error(v + " is not a valid compression method !") } var m = _.call(this, u, g); var b = B.call(this, h, u, m, t);
						t += b.fileRecord.length + m.compressedSize;
						a += b.dirRecord.length;
						r.push(b) } var E = "";
					E = s.CENTRAL_DIRECTORY_END + "\0\0" + "\0\0" + C(r.length, 2) + C(r.length, 2) + C(a, 4) + C(t, 4) + C(c.length, 2) + c; var k = e.type.toLowerCase(); if(k === "uint8array" || k === "arraybuffer" || k === "blob" || k === "nodebuffer") { i = new p(t + a + E.length) } else { i = new d(t + a + E.length) } for(f = 0; f < r.length; f++) { i.append(r[f].fileRecord);
						i.append(r[f].compressedObject.compressedContent) } for(f = 0; f < r.length; f++) { i.append(r[f].dirRecord) } i.append(E); var S = i.finalize(); switch(e.type.toLowerCase()) {
						case "uint8array":
							;
						case "arraybuffer":
							;
						case "nodebuffer":
							return n.transformTo(e.type.toLowerCase(), S);
						case "blob":
							return n.arrayBuffer2Blob(n.transformTo("arraybuffer", S));
						case "base64":
							return e.base64 ? o.encode(S) : S;
						default:
							return S; } }, crc32: function(e, r) { return i(e, r) }, utf8encode: function(e) { return n.transformTo("string", u.utf8encode(e)) }, utf8decode: function(e) { return u.utf8decode(e) } };
			r.exports = T }, { "./base64": 1, "./compressedObject": 2, "./compressions": 3, "./crc32": 4, "./defaults": 6, "./nodeBuffer": 11, "./signature": 14, "./stringWriter": 16, "./support": 17, "./uint8ArrayWriter": 19, "./utf8": 20, "./utils": 21 }],
		14: [function(e, r, t) { "use strict";
			t.LOCAL_FILE_HEADER = "PK";
			t.CENTRAL_FILE_HEADER = "PK";
			t.CENTRAL_DIRECTORY_END = "PK";
			t.ZIP64_CENTRAL_DIRECTORY_LOCATOR = "PK";
			t.ZIP64_CENTRAL_DIRECTORY_END = "PK";
			t.DATA_DESCRIPTOR = "PK\b" }, {}],
		15: [function(e, r, t) { "use strict"; var a = e("./dataReader"); var n = e("./utils");

			function i(e, r) { this.data = e; if(!r) { this.data = n.string2binary(this.data) } this.length = this.data.length;
				this.index = 0 } i.prototype = new a;
			i.prototype.byteAt = function(e) { return this.data.charCodeAt(e) };
			i.prototype.lastIndexOfSignature = function(e) { return this.data.lastIndexOf(e) };
			i.prototype.readData = function(e) { this.checkOffset(e); var r = this.data.slice(this.index, this.index + e);
				this.index += e; return r };
			r.exports = i }, { "./dataReader": 5, "./utils": 21 }],
		16: [function(e, r, t) { "use strict"; var a = e("./utils"); var n = function() { this.data = [] };
			n.prototype = { append: function(e) { e = a.transformTo("string", e);
					this.data.push(e) }, finalize: function() { return this.data.join("") } };
			r.exports = n }, { "./utils": 21 }],
		17: [function(e, r, t) {
			(function(e) { "use strict";
				t.base64 = true;
				t.array = true;
				t.string = true;
				t.arraybuffer = typeof ArrayBuffer !== "undefined" && typeof Uint8Array !== "undefined";
				t.nodebuffer = typeof e !== "undefined";
				t.uint8array = typeof Uint8Array !== "undefined"; if(typeof ArrayBuffer === "undefined") { t.blob = false } else { var r = new ArrayBuffer(0); try { t.blob = new Blob([r], { type: "application/zip" }).size === 0 } catch(a) { try { var n = window.BlobBuilder || window.WebKitBlobBuilder || window.MozBlobBuilder || window.MSBlobBuilder; var i = new n;
							i.append(r);
							t.blob = i.getBlob("application/zip").size === 0 } catch(a) { t.blob = false } } } }).call(this, typeof Buffer !== "undefined" ? Buffer : undefined) }, {}],
		18: [function(e, r, t) { "use strict"; var a = e("./dataReader");

			function n(e) { if(e) { this.data = e;
					this.length = this.data.length;
					this.index = 0 } } n.prototype = new a;
			n.prototype.byteAt = function(e) { return this.data[e] };
			n.prototype.lastIndexOfSignature = function(e) { var r = e.charCodeAt(0),
					t = e.charCodeAt(1),
					a = e.charCodeAt(2),
					n = e.charCodeAt(3); for(var i = this.length - 4; i >= 0; --i) { if(this.data[i] === r && this.data[i + 1] === t && this.data[i + 2] === a && this.data[i + 3] === n) { return i } } return -1 };
			n.prototype.readData = function(e) { this.checkOffset(e); if(e === 0) { return new Uint8Array(0) } var r = this.data.subarray(this.index, this.index + e);
				this.index += e; return r };
			r.exports = n }, { "./dataReader": 5 }],
		19: [function(e, r, t) { "use strict"; var a = e("./utils"); var n = function(e) { this.data = new Uint8Array(e);
				this.index = 0 };
			n.prototype = { append: function(e) { if(e.length !== 0) { e = a.transformTo("uint8array", e);
						this.data.set(e, this.index);
						this.index += e.length } }, finalize: function() { return this.data } };
			r.exports = n }, { "./utils": 21 }],
		20: [function(e, r, t) { "use strict"; var a = e("./utils"); var n = e("./support"); var i = e("./nodeBuffer"); var s = new Array(256); for(var f = 0; f < 256; f++) { s[f] = f >= 252 ? 6 : f >= 248 ? 5 : f >= 240 ? 4 : f >= 224 ? 3 : f >= 192 ? 2 : 1 } s[254] = s[254] = 1; var o = function(e) { var r, t, a, i, s, f = e.length,
					o = 0; for(i = 0; i < f; i++) { t = e.charCodeAt(i); if((t & 64512) === 55296 && i + 1 < f) { a = e.charCodeAt(i + 1); if((a & 64512) === 56320) { t = 65536 + (t - 55296 << 10) + (a - 56320);
							i++ } } o += t < 128 ? 1 : t < 2048 ? 2 : t < 65536 ? 3 : 4 } if(n.uint8array) { r = new Uint8Array(o) } else { r = new Array(o) } for(s = 0, i = 0; s < o; i++) { t = e.charCodeAt(i); if((t & 64512) === 55296 && i + 1 < f) { a = e.charCodeAt(i + 1); if((a & 64512) === 56320) { t = 65536 + (t - 55296 << 10) + (a - 56320);
							i++ } } if(t < 128) { r[s++] = t } else if(t < 2048) { r[s++] = 192 | t >>> 6;
						r[s++] = 128 | t & 63 } else if(t < 65536) { r[s++] = 224 | t >>> 12;
						r[s++] = 128 | t >>> 6 & 63;
						r[s++] = 128 | t & 63 } else { r[s++] = 240 | t >>> 18;
						r[s++] = 128 | t >>> 12 & 63;
						r[s++] = 128 | t >>> 6 & 63;
						r[s++] = 128 | t & 63 } } return r }; var l = function(e, r) { var t;
				r = r || e.length; if(r > e.length) { r = e.length } t = r - 1; while(t >= 0 && (e[t] & 192) === 128) { t-- } if(t < 0) { return r } if(t === 0) { return r } return t + s[e[t]] > r ? t : r }; var c = function(e) { var r, t, n, i, f; var o = e.length; var l = new Array(o * 2); for(n = 0, t = 0; t < o;) { i = e[t++]; if(i < 128) { l[n++] = i; continue } f = s[i]; if(f > 4) { l[n++] = 65533;
						t += f - 1; continue } i &= f === 2 ? 31 : f === 3 ? 15 : 7; while(f > 1 && t < o) { i = i << 6 | e[t++] & 63;
						f-- } if(f > 1) { l[n++] = 65533; continue } if(i < 65536) { l[n++] = i } else { i -= 65536;
						l[n++] = 55296 | i >> 10 & 1023;
						l[n++] = 56320 | i & 1023 } } if(l.length !== n) { if(l.subarray) { l = l.subarray(0, n) } else { l.length = n } } return a.applyFromCharCode(l) };
			t.utf8encode = function h(e) { if(n.nodebuffer) { return i(e, "utf-8") } return o(e) };
			t.utf8decode = function u(e) { if(n.nodebuffer) { return a.transformTo("nodebuffer", e).toString("utf-8") } e = a.transformTo(n.uint8array ? "uint8array" : "array", e); var r = [],
					t = 0,
					i = e.length,
					s = 65536; while(t < i) { var f = l(e, Math.min(t + s, i)); if(n.uint8array) { r.push(c(e.subarray(t, f))) } else { r.push(c(e.slice(t, f))) } t = f } return r.join("") } }, { "./nodeBuffer": 11, "./support": 17, "./utils": 21 }],
		21: [function(e, r, t) { "use strict"; var a = e("./support"); var n = e("./compressions"); var i = e("./nodeBuffer");
			t.string2binary = function(e) { var r = ""; for(var t = 0; t < e.length; t++) { r += String.fromCharCode(e.charCodeAt(t) & 255) } return r };
			t.arrayBuffer2Blob = function(e) { t.checkSupport("blob"); try { return new Blob([e], { type: "application/zip" }) } catch(r) { try { var a = window.BlobBuilder || window.WebKitBlobBuilder || window.MozBlobBuilder || window.MSBlobBuilder; var n = new a;
						n.append(e); return n.getBlob("application/zip") } catch(r) { throw new Error("Bug : can't construct the Blob.") } } };

			function s(e) { return e }

			function f(e, r) { for(var t = 0; t < e.length; ++t) { r[t] = e.charCodeAt(t) & 255 } return r }

			function o(e) { var r = 65536; var a = [],
					n = e.length,
					s = t.getTypeOf(e),
					f = 0,
					o = true; try { switch(s) {
						case "uint8array":
							String.fromCharCode.apply(null, new Uint8Array(0)); break;
						case "nodebuffer":
							String.fromCharCode.apply(null, i(0)); break; } } catch(l) { o = false } if(!o) { var c = ""; for(var h = 0; h < e.length; h++) { c += String.fromCharCode(e[h]) } return c } while(f < n && r > 1) { try { if(s === "array" || s === "nodebuffer") { a.push(String.fromCharCode.apply(null, e.slice(f, Math.min(f + r, n)))) } else { a.push(String.fromCharCode.apply(null, e.subarray(f, Math.min(f + r, n)))) } f += r } catch(l) { r = Math.floor(r / 2) } } return a.join("") } t.applyFromCharCode = o;

			function l(e, r) { for(var t = 0; t < e.length; t++) { r[t] = e[t] } return r } var c = {};
			c["string"] = { string: s, array: function(e) { return f(e, new Array(e.length)) }, arraybuffer: function(e) { return c["string"]["uint8array"](e).buffer }, uint8array: function(e) { return f(e, new Uint8Array(e.length)) }, nodebuffer: function(e) { return f(e, i(e.length)) } };
			c["array"] = { string: o, array: s, arraybuffer: function(e) { return new Uint8Array(e).buffer }, uint8array: function(e) { return new Uint8Array(e) }, nodebuffer: function(e) { return i(e) } };
			c["arraybuffer"] = { string: function(e) { return o(new Uint8Array(e)) }, array: function(e) { return l(new Uint8Array(e), new Array(e.byteLength)) }, arraybuffer: s, uint8array: function(e) { return new Uint8Array(e) }, nodebuffer: function(e) { return i(new Uint8Array(e)) } };
			c["uint8array"] = { string: o, array: function(e) { return l(e, new Array(e.length)) }, arraybuffer: function(e) { return e.buffer }, uint8array: s, nodebuffer: function(e) { return i(e) } };
			c["nodebuffer"] = { string: o, array: function(e) { return l(e, new Array(e.length)) }, arraybuffer: function(e) { return c["nodebuffer"]["uint8array"](e).buffer }, uint8array: function(e) { return l(e, new Uint8Array(e.length)) }, nodebuffer: s };
			t.transformTo = function(e, r) { if(!r) { r = "" } if(!e) { return r } t.checkSupport(e); var a = t.getTypeOf(r); var n = c[a][e](r); return n };
			t.getTypeOf = function(e) { if(typeof e === "string") { return "string" } if(Object.prototype.toString.call(e) === "[object Array]") { return "array" } if(a.nodebuffer && i.test(e)) { return "nodebuffer" } if(a.uint8array && e instanceof Uint8Array) { return "uint8array" } if(a.arraybuffer && e instanceof ArrayBuffer) { return "arraybuffer" } };
			t.checkSupport = function(e) { var r = a[e.toLowerCase()]; if(!r) { throw new Error(e + " is not supported by this browser") } };
			t.MAX_VALUE_16BITS = 65535;
			t.MAX_VALUE_32BITS = -1;
			t.pretty = function(e) { var r = "",
					t, a; for(a = 0; a < (e || "").length; a++) { t = e.charCodeAt(a);
					r += "\\x" + (t < 16 ? "0" : "") + t.toString(16).toUpperCase() } return r };
			t.findCompression = function(e) { for(var r in n) { if(!n.hasOwnProperty(r)) { continue } if(n[r].magic === e) { return n[r] } } return null };
			t.isRegExp = function(e) { return Object.prototype.toString.call(e) === "[object RegExp]" } }, { "./compressions": 3, "./nodeBuffer": 11, "./support": 17 }],
		22: [function(e, r, t) { "use strict"; var a = e("./stringReader"); var n = e("./nodeBufferReader"); var i = e("./uint8ArrayReader"); var s = e("./utils"); var f = e("./signature"); var o = e("./zipEntry"); var l = e("./support"); var c = e("./object");

			function h(e, r) { this.files = [];
				this.loadOptions = r; if(e) { this.load(e) } } h.prototype = { checkSignature: function(e) { var r = this.reader.readString(4); if(r !== e) { throw new Error("Corrupted zip or bug : unexpected signature " + "(" + s.pretty(r) + ", expected " + s.pretty(e) + ")") } }, readBlockEndOfCentral: function() { this.diskNumber = this.reader.readInt(2);
					this.diskWithCentralDirStart = this.reader.readInt(2);
					this.centralDirRecordsOnThisDisk = this.reader.readInt(2);
					this.centralDirRecords = this.reader.readInt(2);
					this.centralDirSize = this.reader.readInt(4);
					this.centralDirOffset = this.reader.readInt(4);
					this.zipCommentLength = this.reader.readInt(2);
					this.zipComment = this.reader.readString(this.zipCommentLength);
					this.zipComment = c.utf8decode(this.zipComment) }, readBlockZip64EndOfCentral: function() { this.zip64EndOfCentralSize = this.reader.readInt(8);
					this.versionMadeBy = this.reader.readString(2);
					this.versionNeeded = this.reader.readInt(2);
					this.diskNumber = this.reader.readInt(4);
					this.diskWithCentralDirStart = this.reader.readInt(4);
					this.centralDirRecordsOnThisDisk = this.reader.readInt(8);
					this.centralDirRecords = this.reader.readInt(8);
					this.centralDirSize = this.reader.readInt(8);
					this.centralDirOffset = this.reader.readInt(8);
					this.zip64ExtensibleData = {}; var e = this.zip64EndOfCentralSize - 44,
						r = 0,
						t, a, n; while(r < e) { t = this.reader.readInt(2);
						a = this.reader.readInt(4);
						n = this.reader.readString(a);
						this.zip64ExtensibleData[t] = { id: t, length: a, value: n } } }, readBlockZip64EndOfCentralLocator: function() { this.diskWithZip64CentralDirStart = this.reader.readInt(4);
					this.relativeOffsetEndOfZip64CentralDir = this.reader.readInt(8);
					this.disksCount = this.reader.readInt(4); if(this.disksCount > 1) { throw new Error("Multi-volumes zip are not supported") } }, readLocalFiles: function() { var e, r; for(e = 0; e < this.files.length; e++) { r = this.files[e];
						this.reader.setIndex(r.localHeaderOffset);
						this.checkSignature(f.LOCAL_FILE_HEADER);
						r.readLocalPart(this.reader);
						r.handleUTF8() } }, readCentralDir: function() { var e;
					this.reader.setIndex(this.centralDirOffset); while(this.reader.readString(4) === f.CENTRAL_FILE_HEADER) { e = new o({ zip64: this.zip64 }, this.loadOptions);
						e.readCentralPart(this.reader);
						this.files.push(e) } }, readEndOfCentral: function() { var e = this.reader.lastIndexOfSignature(f.CENTRAL_DIRECTORY_END); if(e === -1) { throw new Error("Corrupted zip : can't find end of central directory") } this.reader.setIndex(e);
					this.checkSignature(f.CENTRAL_DIRECTORY_END);
					this.readBlockEndOfCentral(); if(this.diskNumber === s.MAX_VALUE_16BITS || this.diskWithCentralDirStart === s.MAX_VALUE_16BITS || this.centralDirRecordsOnThisDisk === s.MAX_VALUE_16BITS || this.centralDirRecords === s.MAX_VALUE_16BITS || this.centralDirSize === s.MAX_VALUE_32BITS || this.centralDirOffset === s.MAX_VALUE_32BITS) { this.zip64 = true;
						e = this.reader.lastIndexOfSignature(f.ZIP64_CENTRAL_DIRECTORY_LOCATOR); if(e === -1) { throw new Error("Corrupted zip : can't find the ZIP64 end of central directory locator") } this.reader.setIndex(e);
						this.checkSignature(f.ZIP64_CENTRAL_DIRECTORY_LOCATOR);
						this.readBlockZip64EndOfCentralLocator();
						this.reader.setIndex(this.relativeOffsetEndOfZip64CentralDir);
						this.checkSignature(f.ZIP64_CENTRAL_DIRECTORY_END);
						this.readBlockZip64EndOfCentral() } }, prepareReader: function(e) { var r = s.getTypeOf(e); if(r === "string" && !l.uint8array) { this.reader = new a(e, this.loadOptions.optimizedBinaryString) } else if(r === "nodebuffer") { this.reader = new n(e) } else { this.reader = new i(s.transformTo("uint8array", e)) } }, load: function(e) { this.prepareReader(e);
					this.readEndOfCentral();
					this.readCentralDir();
					this.readLocalFiles() } };
			r.exports = h }, { "./nodeBufferReader": 12, "./object": 13, "./signature": 14, "./stringReader": 15, "./support": 17, "./uint8ArrayReader": 18, "./utils": 21, "./zipEntry": 23 }],
		23: [function(e, r, t) {
			"use strict";
			var a = e("./stringReader");
			var n = e("./utils");
			var i = e("./compressedObject");
			var s = e("./object");

			function f(e, r) { this.options = e;
				this.loadOptions = r } f.prototype = {
				isEncrypted: function() { return(this.bitFlag & 1) === 1 },
				useUTF8: function() { return(this.bitFlag & 2048) === 2048 },
				prepareCompressedContent: function(e, r, t) { return function() { var a = e.index;
						e.setIndex(r); var n = e.readData(t);
						e.setIndex(a); return n } },
				prepareContent: function(e, r, t, a, i) { return function() { var e = n.transformTo(a.uncompressInputType, this.getCompressedContent()); var r = a.uncompress(e); if(r.length !== i) { throw new Error("Bug : uncompressed data size mismatch") } return r } },
				readLocalPart: function(e) { var r, t;
					e.skip(22);
					this.fileNameLength = e.readInt(2);
					t = e.readInt(2);
					this.fileName = e.readString(this.fileNameLength);
					e.skip(t); if(this.compressedSize == -1 || this.uncompressedSize == -1) { throw new Error("Bug or corrupted zip : didn't get enough informations from the central directory " + "(compressedSize == -1 || uncompressedSize == -1)") } r = n.findCompression(this.compressionMethod); if(r === null) { throw new Error("Corrupted zip : compression " + n.pretty(this.compressionMethod) + " unknown (inner file : " + this.fileName + ")") } this.decompressed = new i;
					this.decompressed.compressedSize = this.compressedSize;
					this.decompressed.uncompressedSize = this.uncompressedSize;
					this.decompressed.crc32 = this.crc32;
					this.decompressed.compressionMethod = this.compressionMethod;
					this.decompressed.getCompressedContent = this.prepareCompressedContent(e, e.index, this.compressedSize, r);
					this.decompressed.getContent = this.prepareContent(e, e.index, this.compressedSize, r, this.uncompressedSize); if(this.loadOptions.checkCRC32) { this.decompressed = n.transformTo("string", this.decompressed.getContent()); if(s.crc32(this.decompressed) !== this.crc32) { throw new Error("Corrupted zip : CRC32 mismatch") } } },
				readCentralPart: function(e) { this.versionMadeBy = e.readString(2);
					this.versionNeeded = e.readInt(2);
					this.bitFlag = e.readInt(2);
					this.compressionMethod = e.readString(2);
					this.date = e.readDate();
					this.crc32 = e.readInt(4);
					this.compressedSize = e.readInt(4);
					this.uncompressedSize = e.readInt(4);
					this.fileNameLength = e.readInt(2);
					this.extraFieldsLength = e.readInt(2);
					this.fileCommentLength = e.readInt(2);
					this.diskNumberStart = e.readInt(2);
					this.internalFileAttributes = e.readInt(2);
					this.externalFileAttributes = e.readInt(4);
					this.localHeaderOffset = e.readInt(4); if(this.isEncrypted()) { throw new Error("Encrypted zip are not supported") } this.fileName = e.readString(this.fileNameLength);
					this.readExtraFields(e);
					this.parseZIP64ExtraField(e);
					this.fileComment = e.readString(this.fileCommentLength);
					this.dir = this.externalFileAttributes & 16 ? true : false },
				parseZIP64ExtraField: function(e) { if(!this.extraFields[1]) { return } var r = new a(this.extraFields[1].value); if(this.uncompressedSize === n.MAX_VALUE_32BITS) { this.uncompressedSize = r.readInt(8) } if(this.compressedSize === n.MAX_VALUE_32BITS) { this.compressedSize = r.readInt(8) } if(this.localHeaderOffset === n.MAX_VALUE_32BITS) { this.localHeaderOffset = r.readInt(8) } if(this.diskNumberStart === n.MAX_VALUE_32BITS) { this.diskNumberStart = r.readInt(4) } },
				readExtraFields: function(e) { var r = e.index,
						t, a, n;
					this.extraFields = this.extraFields || {}; while(e.index < r + this.extraFieldsLength) { t = e.readInt(2);
						a = e.readInt(2);
						n = e.readString(a);
						this.extraFields[t] = { id: t, length: a, value: n } } },
				handleUTF8: function() { if(this.useUTF8()) { this.fileName = s.utf8decode(this.fileName);
						this.fileComment = s.utf8decode(this.fileComment) } else { var e = this.findExtraFieldUnicodePath(); if(e !== null) { this.fileName = e } var r = this.findExtraFieldUnicodeComment(); if(r !== null) { this.fileComment = r } } },
				findExtraFieldUnicodePath: function() { var e = this.extraFields[28789]; if(e) { var r = new a(e.value); if(r.readInt(1) !== 1) { return null } if(s.crc32(this.fileName) !== r.readInt(4)) { return null } return s.utf8decode(r.readString(e.length - 5)) } return null },
				findExtraFieldUnicodeComment: function() {
					var e = this.extraFields[25461];
					if(e) { var r = new a(e.value); if(r.readInt(1) !== 1) { return null } if(s.crc32(this.fileComment) !== r.readInt(4)) { return null } return s.utf8decode(r.readString(e.length - 5)) }
					return null
				}
			};
			r.exports = f
		}, { "./compressedObject": 2, "./object": 13, "./stringReader": 15, "./utils": 21 }],
		24: [function(e, r, t) { "use strict"; var a = e("./lib/utils/common").assign; var n = e("./lib/deflate"); var i = e("./lib/inflate"); var s = e("./lib/zlib/constants"); var f = {};
			a(f, n, i, s);
			r.exports = f }, { "./lib/deflate": 25, "./lib/inflate": 26, "./lib/utils/common": 27, "./lib/zlib/constants": 30 }],
		25: [function(e, r, t) { "use strict"; var a = e("./zlib/deflate.js"); var n = e("./utils/common"); var i = e("./utils/strings"); var s = e("./zlib/messages"); var f = e("./zlib/zstream"); var o = 0; var l = 4; var c = 0; var h = 1; var u = -1; var d = 0; var p = 8; var v = function(e) { this.options = n.assign({ level: u, method: p, chunkSize: 16384, windowBits: 15, memLevel: 8, strategy: d, to: "" }, e || {}); var r = this.options; if(r.raw && r.windowBits > 0) { r.windowBits = -r.windowBits } else if(r.gzip && r.windowBits > 0 && r.windowBits < 16) { r.windowBits += 16 } this.err = 0;
				this.msg = "";
				this.ended = false;
				this.chunks = [];
				this.strm = new f;
				this.strm.avail_out = 0; var t = a.deflateInit2(this.strm, r.level, r.method, r.windowBits, r.memLevel, r.strategy); if(t !== c) { throw new Error(s[t]) } if(r.header) { a.deflateSetHeader(this.strm, r.header) } };
			v.prototype.push = function(e, r) { var t = this.strm; var s = this.options.chunkSize; var f, u; if(this.ended) { return false } u = r === ~~r ? r : r === true ? l : o; if(typeof e === "string") { t.input = i.string2buf(e) } else { t.input = e } t.next_in = 0;
				t.avail_in = t.input.length;
				do { if(t.avail_out === 0) { t.output = new n.Buf8(s);
						t.next_out = 0;
						t.avail_out = s } f = a.deflate(t, u); if(f !== h && f !== c) { this.onEnd(f);
						this.ended = true; return false } if(t.avail_out === 0 || t.avail_in === 0 && u === l) { if(this.options.to === "string") { this.onData(i.buf2binstring(n.shrinkBuf(t.output, t.next_out))) } else { this.onData(n.shrinkBuf(t.output, t.next_out)) } } } while ((t.avail_in > 0 || t.avail_out === 0) && f !== h); if(u === l) { f = a.deflateEnd(this.strm);
					this.onEnd(f);
					this.ended = true; return f === c } return true };
			v.prototype.onData = function(e) { this.chunks.push(e) };
			v.prototype.onEnd = function(e) { if(e === c) { if(this.options.to === "string") { this.result = this.chunks.join("") } else { this.result = n.flattenChunks(this.chunks) } } this.chunks = [];
				this.err = e;
				this.msg = this.strm.msg };

			function g(e, r) { var t = new v(r);
				t.push(e, true); if(t.err) { throw t.msg } return t.result }

			function m(e, r) { r = r || {};
				r.raw = true; return g(e, r) }

			function b(e, r) { r = r || {};
				r.gzip = true; return g(e, r) } t.Deflate = v;
			t.deflate = g;
			t.deflateRaw = m;
			t.gzip = b }, { "./utils/common": 27, "./utils/strings": 28, "./zlib/deflate.js": 32, "./zlib/messages": 37, "./zlib/zstream": 39 }],
		26: [function(e, r, t) { "use strict"; var a = e("./zlib/inflate.js"); var n = e("./utils/common"); var i = e("./utils/strings"); var s = e("./zlib/constants"); var f = e("./zlib/messages"); var o = e("./zlib/zstream"); var l = e("./zlib/gzheader"); var c = function(e) { this.options = n.assign({ chunkSize: 16384, windowBits: 0, to: "" }, e || {}); var r = this.options; if(r.raw && r.windowBits >= 0 && r.windowBits < 16) { r.windowBits = -r.windowBits; if(r.windowBits === 0) { r.windowBits = -15 } } if(r.windowBits >= 0 && r.windowBits < 16 && !(e && e.windowBits)) { r.windowBits += 32 } if(r.windowBits > 15 && r.windowBits < 48) { if((r.windowBits & 15) === 0) { r.windowBits |= 15 } } this.err = 0;
				this.msg = "";
				this.ended = false;
				this.chunks = [];
				this.strm = new o;
				this.strm.avail_out = 0; var t = a.inflateInit2(this.strm, r.windowBits); if(t !== s.Z_OK) { throw new Error(f[t]) } this.header = new l;
				a.inflateGetHeader(this.strm, this.header) };
			c.prototype.push = function(e, r) { var t = this.strm; var f = this.options.chunkSize; var o, l; var c, h, u; if(this.ended) { return false } l = r === ~~r ? r : r === true ? s.Z_FINISH : s.Z_NO_FLUSH; if(typeof e === "string") { t.input = i.binstring2buf(e) } else { t.input = e } t.next_in = 0;
				t.avail_in = t.input.length;
				do { if(t.avail_out === 0) { t.output = new n.Buf8(f);
						t.next_out = 0;
						t.avail_out = f } o = a.inflate(t, s.Z_NO_FLUSH); if(o !== s.Z_STREAM_END && o !== s.Z_OK) { this.onEnd(o);
						this.ended = true; return false } if(t.next_out) { if(t.avail_out === 0 || o === s.Z_STREAM_END || t.avail_in === 0 && l === s.Z_FINISH) { if(this.options.to === "string") { c = i.utf8border(t.output, t.next_out);
								h = t.next_out - c;
								u = i.buf2string(t.output, c);
								t.next_out = h;
								t.avail_out = f - h; if(h) { n.arraySet(t.output, t.output, c, h, 0) } this.onData(u) } else { this.onData(n.shrinkBuf(t.output, t.next_out)) } } } } while (t.avail_in > 0 && o !== s.Z_STREAM_END); if(o === s.Z_STREAM_END) { l = s.Z_FINISH } if(l === s.Z_FINISH) { o = a.inflateEnd(this.strm);
					this.onEnd(o);
					this.ended = true; return o === s.Z_OK } return true };
			c.prototype.onData = function(e) { this.chunks.push(e) };
			c.prototype.onEnd = function(e) { if(e === s.Z_OK) { if(this.options.to === "string") { this.result = this.chunks.join("") } else { this.result = n.flattenChunks(this.chunks) } } this.chunks = [];
				this.err = e;
				this.msg = this.strm.msg };

			function h(e, r) { var t = new c(r);
				t.push(e, true); if(t.err) { throw t.msg } return t.result }

			function u(e, r) { r = r || {};
				r.raw = true; return h(e, r) } t.Inflate = c;
			t.inflate = h;
			t.inflateRaw = u;
			t.ungzip = h }, { "./utils/common": 27, "./utils/strings": 28, "./zlib/constants": 30, "./zlib/gzheader": 33, "./zlib/inflate.js": 35, "./zlib/messages": 37, "./zlib/zstream": 39 }],
		27: [function(e, r, t) { "use strict"; var a = typeof Uint8Array !== "undefined" && typeof Uint16Array !== "undefined" && typeof Int32Array !== "undefined";
			t.assign = function(e) { var r = Array.prototype.slice.call(arguments, 1); while(r.length) { var t = r.shift(); if(!t) { continue } if(typeof t !== "object") { throw new TypeError(t + "must be non-object") } for(var a in t) { if(t.hasOwnProperty(a)) { e[a] = t[a] } } } return e };
			t.shrinkBuf = function(e, r) { if(e.length === r) { return e } if(e.subarray) { return e.subarray(0, r) } e.length = r; return e }; var n = { arraySet: function(e, r, t, a, n) { if(r.subarray && e.subarray) { e.set(r.subarray(t, t + a), n); return } for(var i = 0; i < a; i++) { e[n + i] = r[t + i] } }, flattenChunks: function(e) { var r, t, a, n, i, s;
					a = 0; for(r = 0, t = e.length; r < t; r++) { a += e[r].length } s = new Uint8Array(a);
					n = 0; for(r = 0, t = e.length; r < t; r++) { i = e[r];
						s.set(i, n);
						n += i.length } return s } }; var i = { arraySet: function(e, r, t, a, n) { for(var i = 0; i < a; i++) { e[n + i] = r[t + i] } }, flattenChunks: function(e) { return [].concat.apply([], e) } };
			t.setTyped = function(e) { if(e) { t.Buf8 = Uint8Array;
					t.Buf16 = Uint16Array;
					t.Buf32 = Int32Array;
					t.assign(t, n) } else { t.Buf8 = Array;
					t.Buf16 = Array;
					t.Buf32 = Array;
					t.assign(t, i) } };
			t.setTyped(a) }, {}],
		28: [function(e, r, t) { "use strict"; var a = e("./common"); var n = true; var i = true; try { String.fromCharCode.apply(null, [0]) } catch(s) { n = false } try { String.fromCharCode.apply(null, new Uint8Array(1)) } catch(s) { i = false } var f = new a.Buf8(256); for(var o = 0; o < 256; o++) { f[o] = o >= 252 ? 6 : o >= 248 ? 5 : o >= 240 ? 4 : o >= 224 ? 3 : o >= 192 ? 2 : 1 } f[254] = f[254] = 1;
			t.string2buf = function(e) { var r, t, n, i, s, f = e.length,
					o = 0; for(i = 0; i < f; i++) { t = e.charCodeAt(i); if((t & 64512) === 55296 && i + 1 < f) { n = e.charCodeAt(i + 1); if((n & 64512) === 56320) { t = 65536 + (t - 55296 << 10) + (n - 56320);
							i++ } } o += t < 128 ? 1 : t < 2048 ? 2 : t < 65536 ? 3 : 4 } r = new a.Buf8(o); for(s = 0, i = 0; s < o; i++) { t = e.charCodeAt(i); if((t & 64512) === 55296 && i + 1 < f) { n = e.charCodeAt(i + 1); if((n & 64512) === 56320) { t = 65536 + (t - 55296 << 10) + (n - 56320);
							i++ } } if(t < 128) { r[s++] = t } else if(t < 2048) { r[s++] = 192 | t >>> 6;
						r[s++] = 128 | t & 63 } else if(t < 65536) { r[s++] = 224 | t >>> 12;
						r[s++] = 128 | t >>> 6 & 63;
						r[s++] = 128 | t & 63 } else { r[s++] = 240 | t >>> 18;
						r[s++] = 128 | t >>> 12 & 63;
						r[s++] = 128 | t >>> 6 & 63;
						r[s++] = 128 | t & 63 } } return r };

			function l(e, r) { if(r < 65537) { if(e.subarray && i || !e.subarray && n) { return String.fromCharCode.apply(null, a.shrinkBuf(e, r)) } } var t = ""; for(var s = 0; s < r; s++) { t += String.fromCharCode(e[s]) } return t } t.buf2binstring = function(e) { return l(e, e.length) };
			t.binstring2buf = function(e) { var r = new a.Buf8(e.length); for(var t = 0, n = r.length; t < n; t++) { r[t] = e.charCodeAt(t) } return r };
			t.buf2string = function(e, r) { var t, a, n, i; var s = r || e.length; var o = new Array(s * 2); for(a = 0, t = 0; t < s;) { n = e[t++]; if(n < 128) { o[a++] = n; continue } i = f[n]; if(i > 4) { o[a++] = 65533;
						t += i - 1; continue } n &= i === 2 ? 31 : i === 3 ? 15 : 7; while(i > 1 && t < s) { n = n << 6 | e[t++] & 63;
						i-- } if(i > 1) { o[a++] = 65533; continue } if(n < 65536) { o[a++] = n } else { n -= 65536;
						o[a++] = 55296 | n >> 10 & 1023;
						o[a++] = 56320 | n & 1023 } } return l(o, a) };
			t.utf8border = function(e, r) { var t;
				r = r || e.length; if(r > e.length) { r = e.length } t = r - 1; while(t >= 0 && (e[t] & 192) === 128) { t-- } if(t < 0) { return r } if(t === 0) { return r } return t + f[e[t]] > r ? t : r } }, { "./common": 27 }],
		29: [function(e, r, t) { "use strict";

			function a(e, r, t, a) { var n = e & 65535 | 0,
					i = e >>> 16 & 65535 | 0,
					s = 0; while(t !== 0) { s = t > 2e3 ? 2e3 : t;
					t -= s;
					do { n = n + r[a++] | 0;
						i = i + n | 0 } while (--s);
					n %= 65521;
					i %= 65521 } return n | i << 16 | 0 } r.exports = a }, {}],
		30: [function(e, r, t) { r.exports = { Z_NO_FLUSH: 0, Z_PARTIAL_FLUSH: 1, Z_SYNC_FLUSH: 2, Z_FULL_FLUSH: 3, Z_FINISH: 4, Z_BLOCK: 5, Z_TREES: 6, Z_OK: 0, Z_STREAM_END: 1, Z_NEED_DICT: 2, Z_ERRNO: -1, Z_STREAM_ERROR: -2, Z_DATA_ERROR: -3, Z_BUF_ERROR: -5, Z_NO_COMPRESSION: 0, Z_BEST_SPEED: 1, Z_BEST_COMPRESSION: 9, Z_DEFAULT_COMPRESSION: -1, Z_FILTERED: 1, Z_HUFFMAN_ONLY: 2, Z_RLE: 3, Z_FIXED: 4, Z_DEFAULT_STRATEGY: 0, Z_BINARY: 0, Z_TEXT: 1, Z_UNKNOWN: 2, Z_DEFLATED: 8 } }, {}],
		31: [function(e, r, t) { "use strict";

			function a() { var e, r = []; for(var t = 0; t < 256; t++) { e = t; for(var a = 0; a < 8; a++) { e = e & 1 ? 3988292384 ^ e >>> 1 : e >>> 1 } r[t] = e } return r } var n = a();

			function i(e, r, t, a) { var i = n,
					s = a + t;
				e = e ^ -1; for(var f = a; f < s; f++) { e = e >>> 8 ^ i[(e ^ r[f]) & 255] } return e ^ -1 } r.exports = i }, {}],
		32: [function(e, r, t) { "use strict"; var a = e("../utils/common"); var n = e("./trees"); var i = e("./adler32"); var s = e("./crc32"); var f = e("./messages"); var o = 0; var l = 1; var c = 3; var h = 4; var u = 5; var d = 0; var p = 1; var v = -2; var g = -3; var m = -5; var b = -1; var C = 1; var w = 2; var E = 3; var k = 4; var S = 0; var A = 2; var _ = 8; var B = 9; var T = 15; var x = 8; var y = 29; var I = 256; var R = I + 1 + y; var D = 30; var O = 19; var F = 2 * R + 1; var P = 15; var N = 3; var L = 258; var M = L + N + 1; var U = 32; var H = 42; var W = 69; var V = 73; var z = 91; var X = 103; var G = 113; var j = 666; var K = 1; var Y = 2; var $ = 3; var Z = 4; var Q = 3;

			function J(e, r) { e.msg = f[r]; return r }

			function q(e) { return(e << 1) - (e > 4 ? 9 : 0) }

			function ee(e) { var r = e.length; while(--r >= 0) { e[r] = 0 } }

			function re(e) { var r = e.state; var t = r.pending; if(t > e.avail_out) { t = e.avail_out } if(t === 0) { return } a.arraySet(e.output, r.pending_buf, r.pending_out, t, e.next_out);
				e.next_out += t;
				r.pending_out += t;
				e.total_out += t;
				e.avail_out -= t;
				r.pending -= t; if(r.pending === 0) { r.pending_out = 0 } }

			function te(e, r) { n._tr_flush_block(e, e.block_start >= 0 ? e.block_start : -1, e.strstart - e.block_start, r);
				e.block_start = e.strstart;
				re(e.strm) }

			function ae(e, r) { e.pending_buf[e.pending++] = r }

			function ne(e, r) { e.pending_buf[e.pending++] = r >>> 8 & 255;
				e.pending_buf[e.pending++] = r & 255 }

			function ie(e, r, t, n) { var f = e.avail_in; if(f > n) { f = n } if(f === 0) { return 0 } e.avail_in -= f;
				a.arraySet(r, e.input, e.next_in, f, t); if(e.state.wrap === 1) { e.adler = i(e.adler, r, f, t) } else if(e.state.wrap === 2) { e.adler = s(e.adler, r, f, t) } e.next_in += f;
				e.total_in += f; return f }

			function se(e, r) { var t = e.max_chain_length; var a = e.strstart; var n; var i; var s = e.prev_length; var f = e.nice_match; var o = e.strstart > e.w_size - M ? e.strstart - (e.w_size - M) : 0; var l = e.window; var c = e.w_mask; var h = e.prev; var u = e.strstart + L; var d = l[a + s - 1]; var p = l[a + s]; if(e.prev_length >= e.good_match) { t >>= 2 } if(f > e.lookahead) { f = e.lookahead } do { n = r; if(l[n + s] !== p || l[n + s - 1] !== d || l[n] !== l[a] || l[++n] !== l[a + 1]) { continue } a += 2;
					n++;
					do {} while (l[++a] === l[++n] && l[++a] === l[++n] && l[++a] === l[++n] && l[++a] === l[++n] && l[++a] === l[++n] && l[++a] === l[++n] && l[++a] === l[++n] && l[++a] === l[++n] && a < u);
					i = L - (u - a);
					a = u - L; if(i > s) { e.match_start = r;
						s = i; if(i >= f) { break } d = l[a + s - 1];
						p = l[a + s] } } while ((r = h[r & c]) > o && --t !== 0); if(s <= e.lookahead) { return s } return e.lookahead }

			function fe(e) { var r = e.w_size; var t, n, i, s, f;
				do { s = e.window_size - e.lookahead - e.strstart; if(e.strstart >= r + (r - M)) { a.arraySet(e.window, e.window, r, r, 0);
						e.match_start -= r;
						e.strstart -= r;
						e.block_start -= r;
						n = e.hash_size;
						t = n;
						do { i = e.head[--t];
							e.head[t] = i >= r ? i - r : 0 } while (--n);
						n = r;
						t = n;
						do { i = e.prev[--t];
							e.prev[t] = i >= r ? i - r : 0 } while (--n);
						s += r } if(e.strm.avail_in === 0) { break } n = ie(e.strm, e.window, e.strstart + e.lookahead, s);
					e.lookahead += n; if(e.lookahead + e.insert >= N) { f = e.strstart - e.insert;
						e.ins_h = e.window[f];
						e.ins_h = (e.ins_h << e.hash_shift ^ e.window[f + 1]) & e.hash_mask; while(e.insert) { e.ins_h = (e.ins_h << e.hash_shift ^ e.window[f + N - 1]) & e.hash_mask;
							e.prev[f & e.w_mask] = e.head[e.ins_h];
							e.head[e.ins_h] = f;
							f++;
							e.insert--; if(e.lookahead + e.insert < N) { break } } } } while (e.lookahead < M && e.strm.avail_in !== 0) }

			function oe(e, r) { var t = 65535; if(t > e.pending_buf_size - 5) { t = e.pending_buf_size - 5 } for(;;) { if(e.lookahead <= 1) { fe(e); if(e.lookahead === 0 && r === o) { return K } if(e.lookahead === 0) { break } } e.strstart += e.lookahead;
					e.lookahead = 0; var a = e.block_start + t; if(e.strstart === 0 || e.strstart >= a) { e.lookahead = e.strstart - a;
						e.strstart = a;
						te(e, false); if(e.strm.avail_out === 0) { return K } } if(e.strstart - e.block_start >= e.w_size - M) { te(e, false); if(e.strm.avail_out === 0) { return K } } } e.insert = 0; if(r === h) { te(e, true); if(e.strm.avail_out === 0) { return $ } return Z } if(e.strstart > e.block_start) { te(e, false); if(e.strm.avail_out === 0) { return K } } return K }

			function le(e, r) { var t; var a; for(;;) { if(e.lookahead < M) { fe(e); if(e.lookahead < M && r === o) { return K } if(e.lookahead === 0) { break } } t = 0; if(e.lookahead >= N) { e.ins_h = (e.ins_h << e.hash_shift ^ e.window[e.strstart + N - 1]) & e.hash_mask;
						t = e.prev[e.strstart & e.w_mask] = e.head[e.ins_h];
						e.head[e.ins_h] = e.strstart } if(t !== 0 && e.strstart - t <= e.w_size - M) { e.match_length = se(e, t) } if(e.match_length >= N) { a = n._tr_tally(e, e.strstart - e.match_start, e.match_length - N);
						e.lookahead -= e.match_length; if(e.match_length <= e.max_lazy_match && e.lookahead >= N) { e.match_length--;
							do { e.strstart++;
								e.ins_h = (e.ins_h << e.hash_shift ^ e.window[e.strstart + N - 1]) & e.hash_mask;
								t = e.prev[e.strstart & e.w_mask] = e.head[e.ins_h];
								e.head[e.ins_h] = e.strstart } while (--e.match_length !== 0);
							e.strstart++ } else { e.strstart += e.match_length;
							e.match_length = 0;
							e.ins_h = e.window[e.strstart];
							e.ins_h = (e.ins_h << e.hash_shift ^ e.window[e.strstart + 1]) & e.hash_mask } } else { a = n._tr_tally(e, 0, e.window[e.strstart]);
						e.lookahead--;
						e.strstart++ } if(a) { te(e, false); if(e.strm.avail_out === 0) { return K } } } e.insert = e.strstart < N - 1 ? e.strstart : N - 1; if(r === h) { te(e, true); if(e.strm.avail_out === 0) { return $ } return Z } if(e.last_lit) { te(e, false); if(e.strm.avail_out === 0) { return K } } return Y }

			function ce(e, r) { var t; var a; var i; for(;;) { if(e.lookahead < M) { fe(e); if(e.lookahead < M && r === o) { return K } if(e.lookahead === 0) { break } } t = 0; if(e.lookahead >= N) { e.ins_h = (e.ins_h << e.hash_shift ^ e.window[e.strstart + N - 1]) & e.hash_mask;
						t = e.prev[e.strstart & e.w_mask] = e.head[e.ins_h];
						e.head[e.ins_h] = e.strstart } e.prev_length = e.match_length;
					e.prev_match = e.match_start;
					e.match_length = N - 1; if(t !== 0 && e.prev_length < e.max_lazy_match && e.strstart - t <= e.w_size - M) { e.match_length = se(e, t); if(e.match_length <= 5 && (e.strategy === C || e.match_length === N && e.strstart - e.match_start > 4096)) { e.match_length = N - 1 } } if(e.prev_length >= N && e.match_length <= e.prev_length) { i = e.strstart + e.lookahead - N;
						a = n._tr_tally(e, e.strstart - 1 - e.prev_match, e.prev_length - N);
						e.lookahead -= e.prev_length - 1;
						e.prev_length -= 2;
						do { if(++e.strstart <= i) { e.ins_h = (e.ins_h << e.hash_shift ^ e.window[e.strstart + N - 1]) & e.hash_mask;
								t = e.prev[e.strstart & e.w_mask] = e.head[e.ins_h];
								e.head[e.ins_h] = e.strstart } } while (--e.prev_length !== 0);
						e.match_available = 0;
						e.match_length = N - 1;
						e.strstart++; if(a) { te(e, false); if(e.strm.avail_out === 0) { return K } } } else if(e.match_available) { a = n._tr_tally(e, 0, e.window[e.strstart - 1]); if(a) { te(e, false) } e.strstart++;
						e.lookahead--; if(e.strm.avail_out === 0) { return K } } else { e.match_available = 1;
						e.strstart++;
						e.lookahead-- } } if(e.match_available) { a = n._tr_tally(e, 0, e.window[e.strstart - 1]);
					e.match_available = 0 } e.insert = e.strstart < N - 1 ? e.strstart : N - 1; if(r === h) { te(e, true); if(e.strm.avail_out === 0) { return $ } return Z } if(e.last_lit) { te(e, false); if(e.strm.avail_out === 0) { return K } } return Y }

			function he(e, r) { var t; var a; var i, s; var f = e.window; for(;;) { if(e.lookahead <= L) { fe(e); if(e.lookahead <= L && r === o) { return K } if(e.lookahead === 0) { break } } e.match_length = 0; if(e.lookahead >= N && e.strstart > 0) { i = e.strstart - 1;
						a = f[i]; if(a === f[++i] && a === f[++i] && a === f[++i]) { s = e.strstart + L;
							do {} while (a === f[++i] && a === f[++i] && a === f[++i] && a === f[++i] && a === f[++i] && a === f[++i] && a === f[++i] && a === f[++i] && i < s);
							e.match_length = L - (s - i); if(e.match_length > e.lookahead) { e.match_length = e.lookahead } } } if(e.match_length >= N) { t = n._tr_tally(e, 1, e.match_length - N);
						e.lookahead -= e.match_length;
						e.strstart += e.match_length;
						e.match_length = 0 } else { t = n._tr_tally(e, 0, e.window[e.strstart]);
						e.lookahead--;
						e.strstart++ } if(t) { te(e, false); if(e.strm.avail_out === 0) { return K } } } e.insert = 0; if(r === h) { te(e, true); if(e.strm.avail_out === 0) { return $ } return Z } if(e.last_lit) { te(e, false); if(e.strm.avail_out === 0) { return K } } return Y }

			function ue(e, r) { var t; for(;;) { if(e.lookahead === 0) { fe(e); if(e.lookahead === 0) { if(r === o) { return K } break } } e.match_length = 0;
					t = n._tr_tally(e, 0, e.window[e.strstart]);
					e.lookahead--;
					e.strstart++; if(t) { te(e, false); if(e.strm.avail_out === 0) { return K } } } e.insert = 0; if(r === h) { te(e, true); if(e.strm.avail_out === 0) { return $ } return Z } if(e.last_lit) { te(e, false); if(e.strm.avail_out === 0) { return K } } return Y } var de = function(e, r, t, a, n) { this.good_length = e;
				this.max_lazy = r;
				this.nice_length = t;
				this.max_chain = a;
				this.func = n }; var pe;
			pe = [new de(0, 0, 0, 0, oe), new de(4, 4, 8, 4, le), new de(4, 5, 16, 8, le), new de(4, 6, 32, 32, le), new de(4, 4, 16, 16, ce), new de(8, 16, 32, 32, ce), new de(8, 16, 128, 128, ce), new de(8, 32, 128, 256, ce), new de(32, 128, 258, 1024, ce), new de(32, 258, 258, 4096, ce)];

			function ve(e) { e.window_size = 2 * e.w_size;
				ee(e.head);
				e.max_lazy_match = pe[e.level].max_lazy;
				e.good_match = pe[e.level].good_length;
				e.nice_match = pe[e.level].nice_length;
				e.max_chain_length = pe[e.level].max_chain;
				e.strstart = 0;
				e.block_start = 0;
				e.lookahead = 0;
				e.insert = 0;
				e.match_length = e.prev_length = N - 1;
				e.match_available = 0;
				e.ins_h = 0 }

			function ge() { this.strm = null;
				this.status = 0;
				this.pending_buf = null;
				this.pending_buf_size = 0;
				this.pending_out = 0;
				this.pending = 0;
				this.wrap = 0;
				this.gzhead = null;
				this.gzindex = 0;
				this.method = _;
				this.last_flush = -1;
				this.w_size = 0;
				this.w_bits = 0;
				this.w_mask = 0;
				this.window = null;
				this.window_size = 0;
				this.prev = null;
				this.head = null;
				this.ins_h = 0;
				this.hash_size = 0;
				this.hash_bits = 0;
				this.hash_mask = 0;
				this.hash_shift = 0;
				this.block_start = 0;
				this.match_length = 0;
				this.prev_match = 0;
				this.match_available = 0;
				this.strstart = 0;
				this.match_start = 0;
				this.lookahead = 0;
				this.prev_length = 0;
				this.max_chain_length = 0;
				this.max_lazy_match = 0;
				this.level = 0;
				this.strategy = 0;
				this.good_match = 0;
				this.nice_match = 0;
				this.dyn_ltree = new a.Buf16(F * 2);
				this.dyn_dtree = new a.Buf16((2 * D + 1) * 2);
				this.bl_tree = new a.Buf16((2 * O + 1) * 2);
				ee(this.dyn_ltree);
				ee(this.dyn_dtree);
				ee(this.bl_tree);
				this.l_desc = null;
				this.d_desc = null;
				this.bl_desc = null;
				this.bl_count = new a.Buf16(P + 1);
				this.heap = new a.Buf16(2 * R + 1);
				ee(this.heap);
				this.heap_len = 0;
				this.heap_max = 0;
				this.depth = new a.Buf16(2 * R + 1);
				ee(this.depth);
				this.l_buf = 0;
				this.lit_bufsize = 0;
				this.last_lit = 0;
				this.d_buf = 0;
				this.opt_len = 0;
				this.static_len = 0;
				this.matches = 0;
				this.insert = 0;
				this.bi_buf = 0;
				this.bi_valid = 0 }

			function me(e) { var r; if(!e || !e.state) { return J(e, v) } e.total_in = e.total_out = 0;
				e.data_type = A;
				r = e.state;
				r.pending = 0;
				r.pending_out = 0; if(r.wrap < 0) { r.wrap = -r.wrap } r.status = r.wrap ? H : G;
				e.adler = r.wrap === 2 ? 0 : 1;
				r.last_flush = o;
				n._tr_init(r); return d }

			function be(e) { var r = me(e); if(r === d) { ve(e.state) } return r }

			function Ce(e, r) { if(!e || !e.state) { return v } if(e.state.wrap !== 2) { return v } e.state.gzhead = r; return d }

			function we(e, r, t, n, i, s) { if(!e) { return v } var f = 1; if(r === b) { r = 6 } if(n < 0) { f = 0;
					n = -n } else if(n > 15) { f = 2;
					n -= 16 } if(i < 1 || i > B || t !== _ || n < 8 || n > 15 || r < 0 || r > 9 || s < 0 || s > k) { return J(e, v) } if(n === 8) { n = 9 } var o = new ge;
				e.state = o;
				o.strm = e;
				o.wrap = f;
				o.gzhead = null;
				o.w_bits = n;
				o.w_size = 1 << o.w_bits;
				o.w_mask = o.w_size - 1;
				o.hash_bits = i + 7;
				o.hash_size = 1 << o.hash_bits;
				o.hash_mask = o.hash_size - 1;
				o.hash_shift = ~~((o.hash_bits + N - 1) / N);
				o.window = new a.Buf8(o.w_size * 2);
				o.head = new a.Buf16(o.hash_size);
				o.prev = new a.Buf16(o.w_size);
				o.lit_bufsize = 1 << i + 6;
				o.pending_buf_size = o.lit_bufsize * 4;
				o.pending_buf = new a.Buf8(o.pending_buf_size);
				o.d_buf = o.lit_bufsize >> 1;
				o.l_buf = (1 + 2) * o.lit_bufsize;
				o.level = r;
				o.strategy = s;
				o.method = t; return be(e) }

			function Ee(e, r) { return we(e, r, _, T, x, S) }

			function ke(e, r) { var t, a; var i, f; if(!e || !e.state || r > u || r < 0) { return e ? J(e, v) : v } a = e.state; if(!e.output || !e.input && e.avail_in !== 0 || a.status === j && r !== h) { return J(e, e.avail_out === 0 ? m : v) } a.strm = e;
				t = a.last_flush;
				a.last_flush = r; if(a.status === H) { if(a.wrap === 2) { e.adler = 0;
						ae(a, 31);
						ae(a, 139);
						ae(a, 8); if(!a.gzhead) { ae(a, 0);
							ae(a, 0);
							ae(a, 0);
							ae(a, 0);
							ae(a, 0);
							ae(a, a.level === 9 ? 2 : a.strategy >= w || a.level < 2 ? 4 : 0);
							ae(a, Q);
							a.status = G } else { ae(a, (a.gzhead.text ? 1 : 0) + (a.gzhead.hcrc ? 2 : 0) + (!a.gzhead.extra ? 0 : 4) + (!a.gzhead.name ? 0 : 8) + (!a.gzhead.comment ? 0 : 16));
							ae(a, a.gzhead.time & 255);
							ae(a, a.gzhead.time >> 8 & 255);
							ae(a, a.gzhead.time >> 16 & 255);
							ae(a, a.gzhead.time >> 24 & 255);
							ae(a, a.level === 9 ? 2 : a.strategy >= w || a.level < 2 ? 4 : 0);
							ae(a, a.gzhead.os & 255); if(a.gzhead.extra && a.gzhead.extra.length) { ae(a, a.gzhead.extra.length & 255);
								ae(a, a.gzhead.extra.length >> 8 & 255) } if(a.gzhead.hcrc) { e.adler = s(e.adler, a.pending_buf, a.pending, 0) } a.gzindex = 0;
							a.status = W } } else { var g = _ + (a.w_bits - 8 << 4) << 8; var b = -1; if(a.strategy >= w || a.level < 2) { b = 0 } else if(a.level < 6) { b = 1 } else if(a.level === 6) { b = 2 } else { b = 3 } g |= b << 6; if(a.strstart !== 0) { g |= U } g += 31 - g % 31;
						a.status = G;
						ne(a, g); if(a.strstart !== 0) { ne(a, e.adler >>> 16);
							ne(a, e.adler & 65535) } e.adler = 1 } } if(a.status === W) { if(a.gzhead.extra) { i = a.pending; while(a.gzindex < (a.gzhead.extra.length & 65535)) { if(a.pending === a.pending_buf_size) { if(a.gzhead.hcrc && a.pending > i) { e.adler = s(e.adler, a.pending_buf, a.pending - i, i) } re(e);
								i = a.pending; if(a.pending === a.pending_buf_size) { break } } ae(a, a.gzhead.extra[a.gzindex] & 255);
							a.gzindex++ } if(a.gzhead.hcrc && a.pending > i) { e.adler = s(e.adler, a.pending_buf, a.pending - i, i) } if(a.gzindex === a.gzhead.extra.length) { a.gzindex = 0;
							a.status = V } } else { a.status = V } } if(a.status === V) { if(a.gzhead.name) { i = a.pending;
						do { if(a.pending === a.pending_buf_size) { if(a.gzhead.hcrc && a.pending > i) { e.adler = s(e.adler, a.pending_buf, a.pending - i, i) } re(e);
								i = a.pending; if(a.pending === a.pending_buf_size) { f = 1; break } } if(a.gzindex < a.gzhead.name.length) { f = a.gzhead.name.charCodeAt(a.gzindex++) & 255 } else { f = 0 } ae(a, f) } while (f !== 0); if(a.gzhead.hcrc && a.pending > i) { e.adler = s(e.adler, a.pending_buf, a.pending - i, i) } if(f === 0) { a.gzindex = 0;
							a.status = z } } else { a.status = z } } if(a.status === z) { if(a.gzhead.comment) { i = a.pending;
						do { if(a.pending === a.pending_buf_size) { if(a.gzhead.hcrc && a.pending > i) { e.adler = s(e.adler, a.pending_buf, a.pending - i, i) } re(e);
								i = a.pending; if(a.pending === a.pending_buf_size) { f = 1; break } } if(a.gzindex < a.gzhead.comment.length) { f = a.gzhead.comment.charCodeAt(a.gzindex++) & 255 } else { f = 0 } ae(a, f) } while (f !== 0); if(a.gzhead.hcrc && a.pending > i) { e.adler = s(e.adler, a.pending_buf, a.pending - i, i) } if(f === 0) { a.status = X } } else { a.status = X } } if(a.status === X) { if(a.gzhead.hcrc) { if(a.pending + 2 > a.pending_buf_size) { re(e) } if(a.pending + 2 <= a.pending_buf_size) { ae(a, e.adler & 255);
							ae(a, e.adler >> 8 & 255);
							e.adler = 0;
							a.status = G } } else { a.status = G } } if(a.pending !== 0) { re(e); if(e.avail_out === 0) { a.last_flush = -1; return d } } else if(e.avail_in === 0 && q(r) <= q(t) && r !== h) { return J(e, m) } if(a.status === j && e.avail_in !== 0) { return J(e, m) } if(e.avail_in !== 0 || a.lookahead !== 0 || r !== o && a.status !== j) { var C = a.strategy === w ? ue(a, r) : a.strategy === E ? he(a, r) : pe[a.level].func(a, r); if(C === $ || C === Z) { a.status = j } if(C === K || C === $) { if(e.avail_out === 0) { a.last_flush = -1 } return d } if(C === Y) { if(r === l) { n._tr_align(a) } else if(r !== u) { n._tr_stored_block(a, 0, 0, false); if(r === c) { ee(a.head); if(a.lookahead === 0) { a.strstart = 0;
									a.block_start = 0;
									a.insert = 0 } } } re(e); if(e.avail_out === 0) { a.last_flush = -1; return d } } } if(r !== h) { return d } if(a.wrap <= 0) { return p } if(a.wrap === 2) { ae(a, e.adler & 255);
					ae(a, e.adler >> 8 & 255);
					ae(a, e.adler >> 16 & 255);
					ae(a, e.adler >> 24 & 255);
					ae(a, e.total_in & 255);
					ae(a, e.total_in >> 8 & 255);
					ae(a, e.total_in >> 16 & 255);
					ae(a, e.total_in >> 24 & 255) } else { ne(a, e.adler >>> 16);
					ne(a, e.adler & 65535) } re(e); if(a.wrap > 0) { a.wrap = -a.wrap } return a.pending !== 0 ? d : p }

			function Se(e) { var r; if(!e || !e.state) { return v } r = e.state.status; if(r !== H && r !== W && r !== V && r !== z && r !== X && r !== G && r !== j) { return J(e, v) } e.state = null; return r === G ? J(e, g) : d } t.deflateInit = Ee;
			t.deflateInit2 = we;
			t.deflateReset = be;
			t.deflateResetKeep = me;
			t.deflateSetHeader = Ce;
			t.deflate = ke;
			t.deflateEnd = Se;
			t.deflateInfo = "pako deflate (from Nodeca project)" }, { "../utils/common": 27, "./adler32": 29, "./crc32": 31, "./messages": 37, "./trees": 38 }],
		33: [function(e, r, t) { "use strict";

			function a() { this.text = 0;
				this.time = 0;
				this.xflags = 0;
				this.os = 0;
				this.extra = null;
				this.extra_len = 0;
				this.name = "";
				this.comment = "";
				this.hcrc = 0;
				this.done = false } r.exports = a }, {}],
		34: [function(e, r, t) { "use strict"; var a = 30; var n = 12;
			r.exports = function i(e, r) { var t; var i; var s; var f; var o; var l; var c; var h; var u; var d; var p; var v; var g; var m; var b; var C; var w; var E; var k; var S; var A; var _; var B; var T, x;
				t = e.state;
				i = e.next_in;
				T = e.input;
				s = i + (e.avail_in - 5);
				f = e.next_out;
				x = e.output;
				o = f - (r - e.avail_out);
				l = f + (e.avail_out - 257);
				c = t.dmax;
				h = t.wsize;
				u = t.whave;
				d = t.wnext;
				p = t.window;
				v = t.hold;
				g = t.bits;
				m = t.lencode;
				b = t.distcode;
				C = (1 << t.lenbits) - 1;
				w = (1 << t.distbits) - 1;
				e: do { if(g < 15) { v += T[i++] << g;
						g += 8;
						v += T[i++] << g;
						g += 8 } E = m[v & C];
					r: for(;;) { k = E >>> 24;
						v >>>= k;
						g -= k;
						k = E >>> 16 & 255; if(k === 0) { x[f++] = E & 65535 } else if(k & 16) { S = E & 65535;
							k &= 15; if(k) { if(g < k) { v += T[i++] << g;
									g += 8 } S += v & (1 << k) - 1;
								v >>>= k;
								g -= k } if(g < 15) { v += T[i++] << g;
								g += 8;
								v += T[i++] << g;
								g += 8 } E = b[v & w];
							t: for(;;) { k = E >>> 24;
								v >>>= k;
								g -= k;
								k = E >>> 16 & 255; if(k & 16) { A = E & 65535;
									k &= 15; if(g < k) { v += T[i++] << g;
										g += 8; if(g < k) { v += T[i++] << g;
											g += 8 } } A += v & (1 << k) - 1; if(A > c) { e.msg = "invalid distance too far back";
										t.mode = a; break e } v >>>= k;
									g -= k;
									k = f - o; if(A > k) { k = A - k; if(k > u) { if(t.sane) { e.msg = "invalid distance too far back";
												t.mode = a; break e } } _ = 0;
										B = p; if(d === 0) { _ += h - k; if(k < S) { S -= k;
												do { x[f++] = p[_++] } while (--k);
												_ = f - A;
												B = x } } else if(d < k) { _ += h + d - k;
											k -= d; if(k < S) { S -= k;
												do { x[f++] = p[_++] } while (--k);
												_ = 0; if(d < S) { k = d;
													S -= k;
													do { x[f++] = p[_++] } while (--k);
													_ = f - A;
													B = x } } } else { _ += d - k; if(k < S) { S -= k;
												do { x[f++] = p[_++] } while (--k);
												_ = f - A;
												B = x } } while(S > 2) { x[f++] = B[_++];
											x[f++] = B[_++];
											x[f++] = B[_++];
											S -= 3 } if(S) { x[f++] = B[_++]; if(S > 1) { x[f++] = B[_++] } } } else { _ = f - A;
										do { x[f++] = x[_++];
											x[f++] = x[_++];
											x[f++] = x[_++];
											S -= 3 } while (S > 2); if(S) { x[f++] = x[_++]; if(S > 1) { x[f++] = x[_++] } } } } else if((k & 64) === 0) { E = b[(E & 65535) + (v & (1 << k) - 1)]; continue t } else { e.msg = "invalid distance code";
									t.mode = a; break e } break } } else if((k & 64) === 0) { E = m[(E & 65535) + (v & (1 << k) - 1)]; continue r } else if(k & 32) { t.mode = n; break e } else { e.msg = "invalid literal/length code";
							t.mode = a; break e } break } } while (i < s && f < l);
				S = g >> 3;
				i -= S;
				g -= S << 3;
				v &= (1 << g) - 1;
				e.next_in = i;
				e.next_out = f;
				e.avail_in = i < s ? 5 + (s - i) : 5 - (i - s);
				e.avail_out = f < l ? 257 + (l - f) : 257 - (f - l);
				t.hold = v;
				t.bits = g; return } }, {}],
		35: [function(e, r, t) {
			"use strict";
			var a = e("../utils/common");
			var n = e("./adler32");
			var i = e("./crc32");
			var s = e("./inffast");
			var f = e("./inftrees");
			var o = 0;
			var l = 1;
			var c = 2;
			var h = 4;
			var u = 5;
			var d = 6;
			var p = 0;
			var v = 1;
			var g = 2;
			var m = -2;
			var b = -3;
			var C = -4;
			var w = -5;
			var E = 8;
			var k = 1;
			var S = 2;
			var A = 3;
			var _ = 4;
			var B = 5;
			var T = 6;
			var x = 7;
			var y = 8;
			var I = 9;
			var R = 10;
			var D = 11;
			var O = 12;
			var F = 13;
			var P = 14;
			var N = 15;
			var L = 16;
			var M = 17;
			var U = 18;
			var H = 19;
			var W = 20;
			var V = 21;
			var z = 22;
			var X = 23;
			var G = 24;
			var j = 25;
			var K = 26;
			var Y = 27;
			var $ = 28;
			var Z = 29;
			var Q = 30;
			var J = 31;
			var q = 32;
			var ee = 852;
			var re = 592;
			var te = 15;
			var ae = te;

			function ne(e) { return(e >>> 24 & 255) + (e >>> 8 & 65280) + ((e & 65280) << 8) + ((e & 255) << 24) }

			function ie() { this.mode = 0;
				this.last = false;
				this.wrap = 0;
				this.havedict = false;
				this.flags = 0;
				this.dmax = 0;
				this.check = 0;
				this.total = 0;
				this.head = null;
				this.wbits = 0;
				this.wsize = 0;
				this.whave = 0;
				this.wnext = 0;
				this.window = null;
				this.hold = 0;
				this.bits = 0;
				this.length = 0;
				this.offset = 0;
				this.extra = 0;
				this.lencode = null;
				this.distcode = null;
				this.lenbits = 0;
				this.distbits = 0;
				this.ncode = 0;
				this.nlen = 0;
				this.ndist = 0;
				this.have = 0;
				this.next = null;
				this.lens = new a.Buf16(320);
				this.work = new a.Buf16(288);
				this.lendyn = null;
				this.distdyn = null;
				this.sane = 0;
				this.back = 0;
				this.was = 0 }

			function se(e) { var r; if(!e || !e.state) { return m } r = e.state;
				e.total_in = e.total_out = r.total = 0;
				e.msg = ""; if(r.wrap) { e.adler = r.wrap & 1 } r.mode = k;
				r.last = 0;
				r.havedict = 0;
				r.dmax = 32768;
				r.head = null;
				r.hold = 0;
				r.bits = 0;
				r.lencode = r.lendyn = new a.Buf32(ee);
				r.distcode = r.distdyn = new a.Buf32(re);
				r.sane = 1;
				r.back = -1; return p }

			function fe(e) { var r; if(!e || !e.state) { return m } r = e.state;
				r.wsize = 0;
				r.whave = 0;
				r.wnext = 0; return se(e) }

			function oe(e, r) { var t; var a; if(!e || !e.state) { return m } a = e.state; if(r < 0) { t = 0;
					r = -r } else { t = (r >> 4) + 1; if(r < 48) { r &= 15 } } if(r && (r < 8 || r > 15)) { return m } if(a.window !== null && a.wbits !== r) { a.window = null } a.wrap = t;
				a.wbits = r; return fe(e) }

			function le(e, r) { var t; var a; if(!e) { return m } a = new ie;
				e.state = a;
				a.window = null;
				t = oe(e, r); if(t !== p) { e.state = null } return t }

			function ce(e) { return le(e, ae) }
			var he = true;
			var ue, de;

			function pe(e) { if(he) { var r;
					ue = new a.Buf32(512);
					de = new a.Buf32(32);
					r = 0; while(r < 144) { e.lens[r++] = 8 } while(r < 256) { e.lens[r++] = 9 } while(r < 280) { e.lens[r++] = 7 } while(r < 288) { e.lens[r++] = 8 } f(l, e.lens, 0, 288, ue, 0, e.work, { bits: 9 });
					r = 0; while(r < 32) { e.lens[r++] = 5 } f(c, e.lens, 0, 32, de, 0, e.work, { bits: 5 });
					he = false } e.lencode = ue;
				e.lenbits = 9;
				e.distcode = de;
				e.distbits = 5 }

			function ve(e, r, t, n) { var i; var s = e.state; if(s.window === null) { s.wsize = 1 << s.wbits;
					s.wnext = 0;
					s.whave = 0;
					s.window = new a.Buf8(s.wsize) } if(n >= s.wsize) { a.arraySet(s.window, r, t - s.wsize, s.wsize, 0);
					s.wnext = 0;
					s.whave = s.wsize } else { i = s.wsize - s.wnext; if(i > n) { i = n } a.arraySet(s.window, r, t - n, i, s.wnext);
					n -= i; if(n) { a.arraySet(s.window, r, t - n, n, 0);
						s.wnext = n;
						s.whave = s.wsize } else { s.wnext += i; if(s.wnext === s.wsize) { s.wnext = 0 } if(s.whave < s.wsize) { s.whave += i } } } return 0 }

			function ge(e, r) {
				var t;
				var ee, re;
				var te;
				var ae;
				var ie, se;
				var fe;
				var oe;
				var le, ce;
				var he;
				var ue;
				var de;
				var ge = 0;
				var me, be, Ce;
				var we, Ee, ke;
				var Se;
				var Ae;
				var _e = new a.Buf8(4);
				var Be;
				var Te;
				var xe = [16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15];
				if(!e || !e.state || !e.output || !e.input && e.avail_in !== 0) { return m } t = e.state;
				if(t.mode === O) { t.mode = F } ae = e.next_out;
				re = e.output;
				se = e.avail_out;
				te = e.next_in;
				ee = e.input;
				ie = e.avail_in;
				fe = t.hold;
				oe = t.bits;
				le = ie;
				ce = se;
				Ae = p;
				e: for(;;) {
					switch(t.mode) {
						case k:
							if(t.wrap === 0) { t.mode = F; break }
							while(oe < 16) { if(ie === 0) { break e } ie--;
								fe += ee[te++] << oe;
								oe += 8 }
							if(t.wrap & 2 && fe === 35615) { t.check = 0;
								_e[0] = fe & 255;
								_e[1] = fe >>> 8 & 255;
								t.check = i(t.check, _e, 2, 0);
								fe = 0;
								oe = 0;
								t.mode = S; break } t.flags = 0;
							if(t.head) { t.head.done = false }
							if(!(t.wrap & 1) || (((fe & 255) << 8) + (fe >> 8)) % 31) { e.msg = "incorrect header check";
								t.mode = Q; break }
							if((fe & 15) !== E) { e.msg = "unknown compression method";
								t.mode = Q; break } fe >>>= 4;
							oe -= 4;
							Se = (fe & 15) + 8;
							if(t.wbits === 0) { t.wbits = Se } else if(Se > t.wbits) { e.msg = "invalid window size";
								t.mode = Q; break } t.dmax = 1 << Se;
							e.adler = t.check = 1;
							t.mode = fe & 512 ? R : O;
							fe = 0;
							oe = 0;
							break;
						case S:
							while(oe < 16) { if(ie === 0) { break e } ie--;
								fe += ee[te++] << oe;
								oe += 8 } t.flags = fe;
							if((t.flags & 255) !== E) { e.msg = "unknown compression method";
								t.mode = Q; break }
							if(t.flags & 57344) { e.msg = "unknown header flags set";
								t.mode = Q; break }
							if(t.head) { t.head.text = fe >> 8 & 1 }
							if(t.flags & 512) { _e[0] = fe & 255;
								_e[1] = fe >>> 8 & 255;
								t.check = i(t.check, _e, 2, 0) } fe = 0;
							oe = 0;
							t.mode = A;
						case A:
							while(oe < 32) { if(ie === 0) { break e } ie--;
								fe += ee[te++] << oe;
								oe += 8 }
							if(t.head) { t.head.time = fe }
							if(t.flags & 512) { _e[0] = fe & 255;
								_e[1] = fe >>> 8 & 255;
								_e[2] = fe >>> 16 & 255;
								_e[3] = fe >>> 24 & 255;
								t.check = i(t.check, _e, 4, 0) } fe = 0;
							oe = 0;
							t.mode = _;
						case _:
							while(oe < 16) { if(ie === 0) { break e } ie--;
								fe += ee[te++] << oe;
								oe += 8 }
							if(t.head) { t.head.xflags = fe & 255;
								t.head.os = fe >> 8 }
							if(t.flags & 512) { _e[0] = fe & 255;
								_e[1] = fe >>> 8 & 255;
								t.check = i(t.check, _e, 2, 0) } fe = 0;
							oe = 0;
							t.mode = B;
						case B:
							if(t.flags & 1024) { while(oe < 16) { if(ie === 0) { break e } ie--;
									fe += ee[te++] << oe;
									oe += 8 } t.length = fe; if(t.head) { t.head.extra_len = fe } if(t.flags & 512) { _e[0] = fe & 255;
									_e[1] = fe >>> 8 & 255;
									t.check = i(t.check, _e, 2, 0) } fe = 0;
								oe = 0 } else if(t.head) { t.head.extra = null } t.mode = T;
						case T:
							if(t.flags & 1024) { he = t.length; if(he > ie) { he = ie } if(he) { if(t.head) { Se = t.head.extra_len - t.length; if(!t.head.extra) { t.head.extra = new Array(t.head.extra_len) } a.arraySet(t.head.extra, ee, te, he, Se) } if(t.flags & 512) { t.check = i(t.check, ee, he, te) } ie -= he;
									te += he;
									t.length -= he } if(t.length) { break e } } t.length = 0;
							t.mode = x;
						case x:
							if(t.flags & 2048) { if(ie === 0) { break e } he = 0;
								do { Se = ee[te + he++]; if(t.head && Se && t.length < 65536) { t.head.name += String.fromCharCode(Se) } } while (Se && he < ie); if(t.flags & 512) { t.check = i(t.check, ee, he, te) } ie -= he;
								te += he; if(Se) { break e } } else if(t.head) { t.head.name = null } t.length = 0;
							t.mode = y;
						case y:
							if(t.flags & 4096) { if(ie === 0) { break e } he = 0;
								do { Se = ee[te + he++]; if(t.head && Se && t.length < 65536) { t.head.comment += String.fromCharCode(Se) } } while (Se && he < ie); if(t.flags & 512) { t.check = i(t.check, ee, he, te) } ie -= he;
								te += he; if(Se) { break e } } else if(t.head) { t.head.comment = null } t.mode = I;
						case I:
							if(t.flags & 512) { while(oe < 16) { if(ie === 0) { break e } ie--;
									fe += ee[te++] << oe;
									oe += 8 } if(fe !== (t.check & 65535)) { e.msg = "header crc mismatch";
									t.mode = Q; break } fe = 0;
								oe = 0 }
							if(t.head) { t.head.hcrc = t.flags >> 9 & 1;
								t.head.done = true } e.adler = t.check = 0;
							t.mode = O;
							break;
						case R:
							while(oe < 32) { if(ie === 0) { break e } ie--;
								fe += ee[te++] << oe;
								oe += 8 } e.adler = t.check = ne(fe);
							fe = 0;
							oe = 0;
							t.mode = D;
						case D:
							if(t.havedict === 0) { e.next_out = ae;
								e.avail_out = se;
								e.next_in = te;
								e.avail_in = ie;
								t.hold = fe;
								t.bits = oe; return g } e.adler = t.check = 1;
							t.mode = O;
						case O:
							if(r === u || r === d) { break e };
						case F:
							if(t.last) { fe >>>= oe & 7;
								oe -= oe & 7;
								t.mode = Y; break }
							while(oe < 3) { if(ie === 0) { break e } ie--;
								fe += ee[te++] << oe;
								oe += 8 } t.last = fe & 1;
							fe >>>= 1;
							oe -= 1;
							switch(fe & 3) {
								case 0:
									t.mode = P; break;
								case 1:
									pe(t);
									t.mode = W; if(r === d) { fe >>>= 2;
										oe -= 2; break e } break;
								case 2:
									t.mode = M; break;
								case 3:
									e.msg = "invalid block type";
									t.mode = Q; } fe >>>= 2;
							oe -= 2;
							break;
						case P:
							fe >>>= oe & 7;
							oe -= oe & 7;
							while(oe < 32) { if(ie === 0) { break e } ie--;
								fe += ee[te++] << oe;
								oe += 8 }
							if((fe & 65535) !== (fe >>> 16 ^ 65535)) { e.msg = "invalid stored block lengths";
								t.mode = Q; break } t.length = fe & 65535;
							fe = 0;
							oe = 0;
							t.mode = N;
							if(r === d) { break e };
						case N:
							t.mode = L;
						case L:
							he = t.length;
							if(he) { if(he > ie) { he = ie } if(he > se) { he = se } if(he === 0) { break e } a.arraySet(re, ee, te, he, ae);
								ie -= he;
								te += he;
								se -= he;
								ae += he;
								t.length -= he; break } t.mode = O;
							break;
						case M:
							while(oe < 14) { if(ie === 0) { break e } ie--;
								fe += ee[te++] << oe;
								oe += 8 } t.nlen = (fe & 31) + 257;
							fe >>>= 5;
							oe -= 5;
							t.ndist = (fe & 31) + 1;
							fe >>>= 5;
							oe -= 5;
							t.ncode = (fe & 15) + 4;
							fe >>>= 4;
							oe -= 4;
							if(t.nlen > 286 || t.ndist > 30) { e.msg = "too many length or distance symbols";
								t.mode = Q; break } t.have = 0;
							t.mode = U;
						case U:
							while(t.have < t.ncode) { while(oe < 3) { if(ie === 0) { break e } ie--;
									fe += ee[te++] << oe;
									oe += 8 } t.lens[xe[t.have++]] = fe & 7;
								fe >>>= 3;
								oe -= 3 }
							while(t.have < 19) { t.lens[xe[t.have++]] = 0 } t.lencode = t.lendyn;
							t.lenbits = 7;
							Be = { bits: t.lenbits };
							Ae = f(o, t.lens, 0, 19, t.lencode, 0, t.work, Be);
							t.lenbits = Be.bits;
							if(Ae) { e.msg = "invalid code lengths set";
								t.mode = Q; break } t.have = 0;
							t.mode = H;
						case H:
							while(t.have < t.nlen + t.ndist) { for(;;) { ge = t.lencode[fe & (1 << t.lenbits) - 1];
									me = ge >>> 24;
									be = ge >>> 16 & 255;
									Ce = ge & 65535; if(me <= oe) { break } if(ie === 0) { break e } ie--;
									fe += ee[te++] << oe;
									oe += 8 } if(Ce < 16) { fe >>>= me;
									oe -= me;
									t.lens[t.have++] = Ce } else { if(Ce === 16) { Te = me + 2; while(oe < Te) { if(ie === 0) { break e } ie--;
											fe += ee[te++] << oe;
											oe += 8 } fe >>>= me;
										oe -= me; if(t.have === 0) { e.msg = "invalid bit length repeat";
											t.mode = Q; break } Se = t.lens[t.have - 1];
										he = 3 + (fe & 3);
										fe >>>= 2;
										oe -= 2 } else if(Ce === 17) { Te = me + 3; while(oe < Te) { if(ie === 0) { break e } ie--;
											fe += ee[te++] << oe;
											oe += 8 } fe >>>= me;
										oe -= me;
										Se = 0;
										he = 3 + (fe & 7);
										fe >>>= 3;
										oe -= 3 } else { Te = me + 7; while(oe < Te) { if(ie === 0) { break e } ie--;
											fe += ee[te++] << oe;
											oe += 8 } fe >>>= me;
										oe -= me;
										Se = 0;
										he = 11 + (fe & 127);
										fe >>>= 7;
										oe -= 7 } if(t.have + he > t.nlen + t.ndist) { e.msg = "invalid bit length repeat";
										t.mode = Q; break } while(he--) { t.lens[t.have++] = Se } } }
							if(t.mode === Q) { break }
							if(t.lens[256] === 0) { e.msg = "invalid code -- missing end-of-block";
								t.mode = Q; break } t.lenbits = 9;
							Be = { bits: t.lenbits };
							Ae = f(l, t.lens, 0, t.nlen, t.lencode, 0, t.work, Be);
							t.lenbits = Be.bits;
							if(Ae) { e.msg = "invalid literal/lengths set";
								t.mode = Q; break } t.distbits = 6;
							t.distcode = t.distdyn;
							Be = { bits: t.distbits };
							Ae = f(c, t.lens, t.nlen, t.ndist, t.distcode, 0, t.work, Be);
							t.distbits = Be.bits;
							if(Ae) { e.msg = "invalid distances set";
								t.mode = Q; break } t.mode = W;
							if(r === d) { break e };
						case W:
							t.mode = V;
						case V:
							if(ie >= 6 && se >= 258) { e.next_out = ae;
								e.avail_out = se;
								e.next_in = te;
								e.avail_in = ie;
								t.hold = fe;
								t.bits = oe;
								s(e, ce);
								ae = e.next_out;
								re = e.output;
								se = e.avail_out;
								te = e.next_in;
								ee = e.input;
								ie = e.avail_in;
								fe = t.hold;
								oe = t.bits; if(t.mode === O) { t.back = -1 } break } t.back = 0;
							for(;;) { ge = t.lencode[fe & (1 << t.lenbits) - 1];
								me = ge >>> 24;
								be = ge >>> 16 & 255;
								Ce = ge & 65535; if(me <= oe) { break } if(ie === 0) { break e } ie--;
								fe += ee[te++] << oe;
								oe += 8 }
							if(be && (be & 240) === 0) { we = me;
								Ee = be;
								ke = Ce; for(;;) { ge = t.lencode[ke + ((fe & (1 << we + Ee) - 1) >> we)];
									me = ge >>> 24;
									be = ge >>> 16 & 255;
									Ce = ge & 65535; if(we + me <= oe) { break } if(ie === 0) { break e } ie--;
									fe += ee[te++] << oe;
									oe += 8 } fe >>>= we;
								oe -= we;
								t.back += we } fe >>>= me;
							oe -= me;
							t.back += me;
							t.length = Ce;
							if(be === 0) { t.mode = K; break }
							if(be & 32) { t.back = -1;
								t.mode = O; break }
							if(be & 64) { e.msg = "invalid literal/length code";
								t.mode = Q; break } t.extra = be & 15;
							t.mode = z;
						case z:
							if(t.extra) { Te = t.extra; while(oe < Te) { if(ie === 0) { break e } ie--;
									fe += ee[te++] << oe;
									oe += 8 } t.length += fe & (1 << t.extra) - 1;
								fe >>>= t.extra;
								oe -= t.extra;
								t.back += t.extra } t.was = t.length;
							t.mode = X;
						case X:
							for(;;) { ge = t.distcode[fe & (1 << t.distbits) - 1];
								me = ge >>> 24;
								be = ge >>> 16 & 255;
								Ce = ge & 65535; if(me <= oe) { break } if(ie === 0) { break e } ie--;
								fe += ee[te++] << oe;
								oe += 8 }
							if((be & 240) === 0) { we = me;
								Ee = be;
								ke = Ce; for(;;) { ge = t.distcode[ke + ((fe & (1 << we + Ee) - 1) >> we)];
									me = ge >>> 24;
									be = ge >>> 16 & 255;
									Ce = ge & 65535; if(we + me <= oe) { break } if(ie === 0) { break e } ie--;
									fe += ee[te++] << oe;
									oe += 8 } fe >>>= we;
								oe -= we;
								t.back += we } fe >>>= me;
							oe -= me;
							t.back += me;
							if(be & 64) { e.msg = "invalid distance code";
								t.mode = Q; break } t.offset = Ce;
							t.extra = be & 15;
							t.mode = G;
						case G:
							if(t.extra) { Te = t.extra; while(oe < Te) { if(ie === 0) { break e } ie--;
									fe += ee[te++] << oe;
									oe += 8 } t.offset += fe & (1 << t.extra) - 1;
								fe >>>= t.extra;
								oe -= t.extra;
								t.back += t.extra }
							if(t.offset > t.dmax) { e.msg = "invalid distance too far back";
								t.mode = Q; break } t.mode = j;
						case j:
							if(se === 0) { break e } he = ce - se;
							if(t.offset > he) { he = t.offset - he; if(he > t.whave) { if(t.sane) { e.msg = "invalid distance too far back";
										t.mode = Q; break } } if(he > t.wnext) { he -= t.wnext;
									ue = t.wsize - he } else { ue = t.wnext - he } if(he > t.length) { he = t.length } de = t.window } else { de = re;
								ue = ae - t.offset;
								he = t.length }
							if(he > se) { he = se } se -= he;
							t.length -= he;
							do { re[ae++] = de[ue++] } while (--he);
							if(t.length === 0) { t.mode = V }
							break;
						case K:
							if(se === 0) { break e } re[ae++] = t.length;
							se--;
							t.mode = V;
							break;
						case Y:
							if(t.wrap) { while(oe < 32) { if(ie === 0) { break e } ie--;
									fe |= ee[te++] << oe;
									oe += 8 } ce -= se;
								e.total_out += ce;
								t.total += ce; if(ce) { e.adler = t.check = t.flags ? i(t.check, re, ce, ae - ce) : n(t.check, re, ce, ae - ce) } ce = se; if((t.flags ? fe : ne(fe)) !== t.check) { e.msg = "incorrect data check";
									t.mode = Q; break } fe = 0;
								oe = 0 } t.mode = $;
						case $:
							if(t.wrap && t.flags) { while(oe < 32) { if(ie === 0) { break e } ie--;
									fe += ee[te++] << oe;
									oe += 8 } if(fe !== (t.total & 4294967295)) { e.msg = "incorrect length check";
									t.mode = Q; break } fe = 0;
								oe = 0 } t.mode = Z;
						case Z:
							Ae = v;
							break e;
						case Q:
							Ae = b;
							break e;
						case J:
							return C;
						case q:
							;
						default:
							return m;
					}
				}
				e.next_out = ae;
				e.avail_out = se;
				e.next_in = te;
				e.avail_in = ie;
				t.hold = fe;
				t.bits = oe;
				if(t.wsize || ce !== e.avail_out && t.mode < Q && (t.mode < Y || r !== h)) { if(ve(e, e.output, e.next_out, ce - e.avail_out)) { t.mode = J; return C } } le -= e.avail_in;
				ce -= e.avail_out;
				e.total_in += le;
				e.total_out += ce;
				t.total += ce;
				if(t.wrap && ce) { e.adler = t.check = t.flags ? i(t.check, re, ce, e.next_out - ce) : n(t.check, re, ce, e.next_out - ce) } e.data_type = t.bits + (t.last ? 64 : 0) + (t.mode === O ? 128 : 0) + (t.mode === W || t.mode === N ? 256 : 0);
				if((le === 0 && ce === 0 || r === h) && Ae === p) { Ae = w }
				return Ae
			}

			function me(e) { if(!e || !e.state) { return m } var r = e.state; if(r.window) { r.window = null } e.state = null; return p }

			function be(e, r) { var t; if(!e || !e.state) { return m } t = e.state; if((t.wrap & 2) === 0) { return m } t.head = r;
				r.done = false; return p } t.inflateReset = fe;
			t.inflateReset2 = oe;
			t.inflateResetKeep = se;
			t.inflateInit = ce;
			t.inflateInit2 = le;
			t.inflate = ge;
			t.inflateEnd = me;
			t.inflateGetHeader = be;
			t.inflateInfo = "pako inflate (from Nodeca project)"
		}, { "../utils/common": 27, "./adler32": 29, "./crc32": 31, "./inffast": 34, "./inftrees": 36 }],
		36: [function(e, r, t) { "use strict"; var a = e("../utils/common"); var n = 15; var i = 852; var s = 592; var f = 0; var o = 1; var l = 2; var c = [3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 15, 17, 19, 23, 27, 31, 35, 43, 51, 59, 67, 83, 99, 115, 131, 163, 195, 227, 258, 0, 0]; var h = [16, 16, 16, 16, 16, 16, 16, 16, 17, 17, 17, 17, 18, 18, 18, 18, 19, 19, 19, 19, 20, 20, 20, 20, 21, 21, 21, 21, 16, 72, 78]; var u = [1, 2, 3, 4, 5, 7, 9, 13, 17, 25, 33, 49, 65, 97, 129, 193, 257, 385, 513, 769, 1025, 1537, 2049, 3073, 4097, 6145, 8193, 12289, 16385, 24577, 0, 0]; var d = [16, 16, 16, 16, 17, 17, 18, 18, 19, 19, 20, 20, 21, 21, 22, 22, 23, 23, 24, 24, 25, 25, 26, 26, 27, 27, 28, 28, 29, 29, 64, 64];
			r.exports = function p(e, r, t, v, g, m, b, C) { var w = C.bits; var E = 0; var k = 0; var S = 0,
					A = 0; var _ = 0; var B = 0; var T = 0; var x = 0; var y = 0; var I = 0; var R; var D; var O; var F; var P; var N = null; var L = 0; var M; var U = new a.Buf16(n + 1); var H = new a.Buf16(n + 1); var W = null; var V = 0; var z, X, G; for(E = 0; E <= n; E++) { U[E] = 0 } for(k = 0; k < v; k++) { U[r[t + k]]++ } _ = w; for(A = n; A >= 1; A--) { if(U[A] !== 0) { break } } if(_ > A) { _ = A } if(A === 0) { g[m++] = 1 << 24 | 64 << 16 | 0;
					g[m++] = 1 << 24 | 64 << 16 | 0;
					C.bits = 1; return 0 } for(S = 1; S < A; S++) { if(U[S] !== 0) { break } } if(_ < S) { _ = S } x = 1; for(E = 1; E <= n; E++) { x <<= 1;
					x -= U[E]; if(x < 0) { return -1 } } if(x > 0 && (e === f || A !== 1)) { return -1 } H[1] = 0; for(E = 1; E < n; E++) { H[E + 1] = H[E] + U[E] } for(k = 0; k < v; k++) { if(r[t + k] !== 0) { b[H[r[t + k]]++] = k } } if(e === f) { N = W = b;
					M = 19 } else if(e === o) { N = c;
					L -= 257;
					W = h;
					V -= 257;
					M = 256 } else { N = u;
					W = d;
					M = -1 } I = 0;
				k = 0;
				E = S;
				P = m;
				B = _;
				T = 0;
				O = -1;
				y = 1 << _;
				F = y - 1; if(e === o && y > i || e === l && y > s) { return 1 } var j = 0; for(;;) { j++;
					z = E - T; if(b[k] < M) { X = 0;
						G = b[k] } else if(b[k] > M) { X = W[V + b[k]];
						G = N[L + b[k]] } else { X = 32 + 64;
						G = 0 } R = 1 << E - T;
					D = 1 << B;
					S = D;
					do { D -= R;
						g[P + (I >> T) + D] = z << 24 | X << 16 | G | 0 } while (D !== 0);
					R = 1 << E - 1; while(I & R) { R >>= 1 } if(R !== 0) { I &= R - 1;
						I += R } else { I = 0 } k++; if(--U[E] === 0) { if(E === A) { break } E = r[t + b[k]] } if(E > _ && (I & F) !== O) { if(T === 0) { T = _ } P += S;
						B = E - T;
						x = 1 << B; while(B + T < A) { x -= U[B + T]; if(x <= 0) { break } B++;
							x <<= 1 } y += 1 << B; if(e === o && y > i || e === l && y > s) { return 1 } O = I & F;
						g[O] = _ << 24 | B << 16 | P - m | 0 } } if(I !== 0) { g[P + I] = E - T << 24 | 64 << 16 | 0 } C.bits = _; return 0 } }, { "../utils/common": 27 }],
		37: [function(e, r, t) { "use strict";
			r.exports = { 2: "need dictionary", 1: "stream end", 0: "", "-1": "file error", "-2": "stream error", "-3": "data error", "-4": "insufficient memory", "-5": "buffer error", "-6": "incompatible version" } }, {}],
		38: [function(e, r, t) { "use strict"; var a = e("../utils/common"); var n = 4; var i = 0; var s = 1; var f = 2;

			function o(e) { var r = e.length; while(--r >= 0) { e[r] = 0 } } var l = 0; var c = 1; var h = 2; var u = 3; var d = 258; var p = 29; var v = 256; var g = v + 1 + p; var m = 30; var b = 19; var C = 2 * g + 1; var w = 15; var E = 16; var k = 7; var S = 256; var A = 16; var _ = 17; var B = 18; var T = [0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 0]; var x = [0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13]; var y = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 7]; var I = [16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15]; var R = 512; var D = new Array((g + 2) * 2);
			o(D); var O = new Array(m * 2);
			o(O); var F = new Array(R);
			o(F); var P = new Array(d - u + 1);
			o(P); var N = new Array(p);
			o(N); var L = new Array(m);
			o(L); var M = function(e, r, t, a, n) { this.static_tree = e;
				this.extra_bits = r;
				this.extra_base = t;
				this.elems = a;
				this.max_length = n;
				this.has_stree = e && e.length }; var U; var H; var W; var V = function(e, r) { this.dyn_tree = e;
				this.max_code = 0;
				this.stat_desc = r };

			function z(e) { return e < 256 ? F[e] : F[256 + (e >>> 7)] }

			function X(e, r) { e.pending_buf[e.pending++] = r & 255;
				e.pending_buf[e.pending++] = r >>> 8 & 255 }

			function G(e, r, t) { if(e.bi_valid > E - t) { e.bi_buf |= r << e.bi_valid & 65535;
					X(e, e.bi_buf);
					e.bi_buf = r >> E - e.bi_valid;
					e.bi_valid += t - E } else { e.bi_buf |= r << e.bi_valid & 65535;
					e.bi_valid += t } }

			function j(e, r, t) { G(e, t[r * 2], t[r * 2 + 1]) }

			function K(e, r) { var t = 0;
				do { t |= e & 1;
					e >>>= 1;
					t <<= 1 } while (--r > 0); return t >>> 1 }

			function Y(e) { if(e.bi_valid === 16) { X(e, e.bi_buf);
					e.bi_buf = 0;
					e.bi_valid = 0 } else if(e.bi_valid >= 8) { e.pending_buf[e.pending++] = e.bi_buf & 255;
					e.bi_buf >>= 8;
					e.bi_valid -= 8 } }

			function $(e, r) { var t = r.dyn_tree; var a = r.max_code; var n = r.stat_desc.static_tree; var i = r.stat_desc.has_stree; var s = r.stat_desc.extra_bits; var f = r.stat_desc.extra_base; var o = r.stat_desc.max_length; var l; var c, h; var u; var d; var p; var v = 0; for(u = 0; u <= w; u++) { e.bl_count[u] = 0 } t[e.heap[e.heap_max] * 2 + 1] = 0; for(l = e.heap_max + 1; l < C; l++) { c = e.heap[l];
					u = t[t[c * 2 + 1] * 2 + 1] + 1; if(u > o) { u = o;
						v++ } t[c * 2 + 1] = u; if(c > a) { continue } e.bl_count[u]++;
					d = 0; if(c >= f) { d = s[c - f] } p = t[c * 2];
					e.opt_len += p * (u + d); if(i) { e.static_len += p * (n[c * 2 + 1] + d) } } if(v === 0) { return } do { u = o - 1; while(e.bl_count[u] === 0) { u-- } e.bl_count[u]--;
					e.bl_count[u + 1] += 2;
					e.bl_count[o]--;
					v -= 2 } while (v > 0); for(u = o; u !== 0; u--) { c = e.bl_count[u]; while(c !== 0) { h = e.heap[--l]; if(h > a) { continue } if(t[h * 2 + 1] !== u) { e.opt_len += (u - t[h * 2 + 1]) * t[h * 2];
							t[h * 2 + 1] = u } c-- } } }

			function Z(e, r, t) { var a = new Array(w + 1); var n = 0; var i; var s; for(i = 1; i <= w; i++) { a[i] = n = n + t[i - 1] << 1 } for(s = 0; s <= r; s++) { var f = e[s * 2 + 1]; if(f === 0) { continue } e[s * 2] = K(a[f]++, f) } }

			function Q() { var e; var r; var t; var a; var n; var i = new Array(w + 1);
				t = 0; for(a = 0; a < p - 1; a++) { N[a] = t; for(e = 0; e < 1 << T[a]; e++) { P[t++] = a } } P[t - 1] = a;
				n = 0; for(a = 0; a < 16; a++) { L[a] = n; for(e = 0; e < 1 << x[a]; e++) { F[n++] = a } } n >>= 7; for(; a < m; a++) { L[a] = n << 7; for(e = 0; e < 1 << x[a] - 7; e++) { F[256 + n++] = a } } for(r = 0; r <= w; r++) { i[r] = 0 } e = 0; while(e <= 143) { D[e * 2 + 1] = 8;
					e++;
					i[8]++ } while(e <= 255) { D[e * 2 + 1] = 9;
					e++;
					i[9]++ } while(e <= 279) { D[e * 2 + 1] = 7;
					e++;
					i[7]++ } while(e <= 287) { D[e * 2 + 1] = 8;
					e++;
					i[8]++ } Z(D, g + 1, i); for(e = 0; e < m; e++) { O[e * 2 + 1] = 5;
					O[e * 2] = K(e, 5) } U = new M(D, T, v + 1, g, w);
				H = new M(O, x, 0, m, w);
				W = new M(new Array(0), y, 0, b, k) }

			function J(e) { var r; for(r = 0; r < g; r++) { e.dyn_ltree[r * 2] = 0 } for(r = 0; r < m; r++) { e.dyn_dtree[r * 2] = 0 } for(r = 0; r < b; r++) { e.bl_tree[r * 2] = 0 } e.dyn_ltree[S * 2] = 1;
				e.opt_len = e.static_len = 0;
				e.last_lit = e.matches = 0 }

			function q(e) { if(e.bi_valid > 8) { X(e, e.bi_buf) } else if(e.bi_valid > 0) { e.pending_buf[e.pending++] = e.bi_buf } e.bi_buf = 0;
				e.bi_valid = 0 }

			function ee(e, r, t, n) { q(e); if(n) { X(e, t);
					X(e, ~t) } a.arraySet(e.pending_buf, e.window, r, t, e.pending);
				e.pending += t }

			function re(e, r, t, a) { var n = r * 2; var i = t * 2; return e[n] < e[i] || e[n] === e[i] && a[r] <= a[t] }

			function te(e, r, t) { var a = e.heap[t]; var n = t << 1; while(n <= e.heap_len) { if(n < e.heap_len && re(r, e.heap[n + 1], e.heap[n], e.depth)) { n++ } if(re(r, a, e.heap[n], e.depth)) { break } e.heap[t] = e.heap[n];
					t = n;
					n <<= 1 } e.heap[t] = a }

			function ae(e, r, t) { var a; var n; var i = 0; var s; var f; if(e.last_lit !== 0) { do { a = e.pending_buf[e.d_buf + i * 2] << 8 | e.pending_buf[e.d_buf + i * 2 + 1];
						n = e.pending_buf[e.l_buf + i];
						i++; if(a === 0) { j(e, n, r) } else { s = P[n];
							j(e, s + v + 1, r);
							f = T[s]; if(f !== 0) { n -= N[s];
								G(e, n, f) } a--;
							s = z(a);
							j(e, s, t);
							f = x[s]; if(f !== 0) { a -= L[s];
								G(e, a, f) } } } while (i < e.last_lit) } j(e, S, r) }

			function ne(e, r) { var t = r.dyn_tree; var a = r.stat_desc.static_tree; var n = r.stat_desc.has_stree; var i = r.stat_desc.elems; var s, f; var o = -1; var l;
				e.heap_len = 0;
				e.heap_max = C; for(s = 0; s < i; s++) { if(t[s * 2] !== 0) { e.heap[++e.heap_len] = o = s;
						e.depth[s] = 0 } else { t[s * 2 + 1] = 0 } } while(e.heap_len < 2) { l = e.heap[++e.heap_len] = o < 2 ? ++o : 0;
					t[l * 2] = 1;
					e.depth[l] = 0;
					e.opt_len--; if(n) { e.static_len -= a[l * 2 + 1] } } r.max_code = o; for(s = e.heap_len >> 1; s >= 1; s--) { te(e, t, s) } l = i;
				do { s = e.heap[1];
					e.heap[1] = e.heap[e.heap_len--];
					te(e, t, 1);
					f = e.heap[1];
					e.heap[--e.heap_max] = s;
					e.heap[--e.heap_max] = f;
					t[l * 2] = t[s * 2] + t[f * 2];
					e.depth[l] = (e.depth[s] >= e.depth[f] ? e.depth[s] : e.depth[f]) + 1;
					t[s * 2 + 1] = t[f * 2 + 1] = l;
					e.heap[1] = l++;
					te(e, t, 1) } while (e.heap_len >= 2);
				e.heap[--e.heap_max] = e.heap[1];
				$(e, r);
				Z(t, o, e.bl_count) }

			function ie(e, r, t) { var a; var n = -1; var i; var s = r[0 * 2 + 1]; var f = 0; var o = 7; var l = 4; if(s === 0) { o = 138;
					l = 3 } r[(t + 1) * 2 + 1] = 65535; for(a = 0; a <= t; a++) { i = s;
					s = r[(a + 1) * 2 + 1]; if(++f < o && i === s) { continue } else if(f < l) { e.bl_tree[i * 2] += f } else if(i !== 0) { if(i !== n) { e.bl_tree[i * 2]++ } e.bl_tree[A * 2]++ } else if(f <= 10) { e.bl_tree[_ * 2]++ } else { e.bl_tree[B * 2]++ } f = 0;
					n = i; if(s === 0) { o = 138;
						l = 3 } else if(i === s) { o = 6;
						l = 3 } else { o = 7;
						l = 4 } } }

			function se(e, r, t) { var a; var n = -1; var i; var s = r[0 * 2 + 1]; var f = 0; var o = 7; var l = 4; if(s === 0) { o = 138;
					l = 3 } for(a = 0; a <= t; a++) { i = s;
					s = r[(a + 1) * 2 + 1]; if(++f < o && i === s) { continue } else if(f < l) { do { j(e, i, e.bl_tree) } while (--f !== 0) } else if(i !== 0) { if(i !== n) { j(e, i, e.bl_tree);
							f-- } j(e, A, e.bl_tree);
						G(e, f - 3, 2) } else if(f <= 10) { j(e, _, e.bl_tree);
						G(e, f - 3, 3) } else { j(e, B, e.bl_tree);
						G(e, f - 11, 7) } f = 0;
					n = i; if(s === 0) { o = 138;
						l = 3 } else if(i === s) { o = 6;
						l = 3 } else { o = 7;
						l = 4 } } }

			function fe(e) { var r;
				ie(e, e.dyn_ltree, e.l_desc.max_code);
				ie(e, e.dyn_dtree, e.d_desc.max_code);
				ne(e, e.bl_desc); for(r = b - 1; r >= 3; r--) { if(e.bl_tree[I[r] * 2 + 1] !== 0) { break } } e.opt_len += 3 * (r + 1) + 5 + 5 + 4; return r }

			function oe(e, r, t, a) { var n;
				G(e, r - 257, 5);
				G(e, t - 1, 5);
				G(e, a - 4, 4); for(n = 0; n < a; n++) { G(e, e.bl_tree[I[n] * 2 + 1], 3) } se(e, e.dyn_ltree, r - 1);
				se(e, e.dyn_dtree, t - 1) }

			function le(e) { var r = 4093624447; var t; for(t = 0; t <= 31; t++, r >>>= 1) { if(r & 1 && e.dyn_ltree[t * 2] !== 0) { return i } } if(e.dyn_ltree[9 * 2] !== 0 || e.dyn_ltree[10 * 2] !== 0 || e.dyn_ltree[13 * 2] !== 0) { return s } for(t = 32; t < v; t++) { if(e.dyn_ltree[t * 2] !== 0) { return s } } return i } var ce = false;

			function he(e) { if(!ce) { Q();
					ce = true } e.l_desc = new V(e.dyn_ltree, U);
				e.d_desc = new V(e.dyn_dtree, H);
				e.bl_desc = new V(e.bl_tree, W);
				e.bi_buf = 0;
				e.bi_valid = 0;
				J(e) }

			function ue(e, r, t, a) { G(e, (l << 1) + (a ? 1 : 0), 3);
				ee(e, r, t, true) }

			function de(e) { G(e, c << 1, 3);
				j(e, S, D);
				Y(e) }

			function pe(e, r, t, a) { var i, s; var o = 0; if(e.level > 0) { if(e.strm.data_type === f) { e.strm.data_type = le(e) } ne(e, e.l_desc);
					ne(e, e.d_desc);
					o = fe(e);
					i = e.opt_len + 3 + 7 >>> 3;
					s = e.static_len + 3 + 7 >>> 3; if(s <= i) { i = s } } else { i = s = t + 5 } if(t + 4 <= i && r !== -1) { ue(e, r, t, a) } else if(e.strategy === n || s === i) { G(e, (c << 1) + (a ? 1 : 0), 3);
					ae(e, D, O) } else { G(e, (h << 1) + (a ? 1 : 0), 3);
					oe(e, e.l_desc.max_code + 1, e.d_desc.max_code + 1, o + 1);
					ae(e, e.dyn_ltree, e.dyn_dtree) } J(e); if(a) { q(e) } }

			function ve(e, r, t) { e.pending_buf[e.d_buf + e.last_lit * 2] = r >>> 8 & 255;
				e.pending_buf[e.d_buf + e.last_lit * 2 + 1] = r & 255;
				e.pending_buf[e.l_buf + e.last_lit] = t & 255;
				e.last_lit++; if(r === 0) { e.dyn_ltree[t * 2]++ } else { e.matches++;
					r--;
					e.dyn_ltree[(P[t] + v + 1) * 2]++;
					e.dyn_dtree[z(r) * 2]++ } return e.last_lit === e.lit_bufsize - 1 } t._tr_init = he;
			t._tr_stored_block = ue;
			t._tr_flush_block = pe;
			t._tr_tally = ve;
			t._tr_align = de }, { "../utils/common": 27 }],
		39: [function(e, r, t) { "use strict";

			function a() { this.input = null;
				this.next_in = 0;
				this.avail_in = 0;
				this.total_in = 0;
				this.output = null;
				this.next_out = 0;
				this.avail_out = 0;
				this.total_out = 0;
				this.msg = "";
				this.state = null;
				this.data_type = 2;
				this.adler = 0 } r.exports = a }, {}]
	}, {}, [9])(9)
});
var cptable = { version: "1.13.0" };
cptable[437] = function() { var e = "\0\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~脟眉茅芒盲脿氓莽锚毛猫茂卯矛脛脜脡忙脝么枚貌没霉每脰脺垄拢楼鈧捗∶趁好泵懧郝库寪卢陆录隆芦禄鈻戔枓鈻撯攤鈹も暋鈺⑩晼鈺曗暎鈺戔晽鈺濃暅鈺涒攼鈹斺敶鈹敎鈹€鈹尖暈鈺熲暁鈺斺暕鈺︹暊鈺愨暚鈺р暔鈺も暐鈺欌晿鈺掆晸鈺暘鈹樷攲鈻堚杽鈻屸枑鈻€伪脽螕蟺危蟽碌蟿桅螛惟未鈭炏單碘埄鈮÷扁墺鈮も尃鈱∶封増掳鈭櫬封垰鈦柯测枲聽",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[620] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~脟眉茅芒盲脿膮莽锚毛猫茂卯膰脛膭臉臋艂么枚膯没霉艢脰脺垄艁楼艣茠殴呕贸脫艅艃藕偶驴鈱惵铰悸÷烩枒鈻掆枔鈹傗敜鈺♀暍鈺栤晻鈺ｂ晳鈺椻暆鈺溾暃鈹愨敂鈹粹敩鈹溾攢鈹尖暈鈺熲暁鈺斺暕鈺︹暊鈺愨暚鈺р暔鈺も暐鈺欌晿鈺掆晸鈺暘鈹樷攲鈻堚杽鈻屸枑鈻€伪脽螕蟺危蟽碌蟿桅螛惟未鈭炏單碘埄鈮÷扁墺鈮も尃鈱∶封増掳鈭櫬封垰鈦柯测枲聽",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[737] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~螒螔螕螖螘螙螚螛螜螝螞螠螡螢螣螤巍危韦违桅围唯惟伪尾纬未蔚味畏胃喂魏位渭谓尉慰蟺蟻蟽蟼蟿蠀蠁蠂蠄鈻戔枓鈻撯攤鈹も暋鈺⑩晼鈺曗暎鈺戔晽鈺濃暅鈺涒攼鈹斺敶鈹敎鈹€鈹尖暈鈺熲暁鈺斺暕鈺︹暊鈺愨暚鈺р暔鈺も暐鈺欌晿鈺掆晸鈺暘鈹樷攲鈻堚杽鈻屸枑鈻€蠅维苇萎蠆委蠈蠉蠇蠋螁螆螇螉螌螏螐卤鈮モ墹为潍梅鈮埪扳垯路鈭氣伩虏鈻犅�",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[850] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~脟眉茅芒盲脿氓莽锚毛猫茂卯矛脛脜脡忙脝么枚貌没霉每脰脺酶拢脴脳茠谩铆贸煤帽脩陋潞驴庐卢陆录隆芦禄鈻戔枓鈻撯攤鈹っ伱偯€漏鈺ｂ晳鈺椻暆垄楼鈹愨敂鈹粹敩鈹溾攢鈹济Ｃ冣暁鈺斺暕鈺︹暊鈺愨暚陇冒脨脢脣脠谋脥脦脧鈹樷攲鈻堚杽娄脤鈻€脫脽脭脪玫脮碌镁脼脷脹脵媒脻炉麓颅卤鈥椔韭堵仿嘎奥仿孤陈测枲聽",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[852] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~脟眉茅芒盲暖膰莽艂毛艕艖卯殴脛膯脡墓暮么枚慕木艢艣脰脺扭钮艁脳膷谩铆贸煤膭膮沤啪臉臋卢藕膶艧芦禄鈻戔枓鈻撯攤鈹っ伱偰毰炩暎鈺戔晽鈺澟慌尖攼鈹斺敶鈹敎鈹€鈹寄偰冣暁鈺斺暕鈺︹暊鈺愨暚陇膽膼膸脣膹艊脥脦臎鈹樷攲鈻堚杽泞女鈻€脫脽脭艃艅艌艩拧艛脷艜虐媒脻牛麓颅藵藳藝藰搂梅赂掳篓藱疟艠艡鈻犅�",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[857] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~脟眉茅芒盲脿氓莽锚毛猫茂卯谋脛脜脡忙脝么枚貌没霉陌脰脺酶拢脴艦艧谩铆贸煤帽脩臑臒驴庐卢陆录隆芦禄鈻戔枓鈻撯攤鈹っ伱偯€漏鈺ｂ晳鈺椻暆垄楼鈹愨敂鈹粹敩鈹溾攢鈹济Ｃ冣暁鈺斺暕鈺︹暊鈺愨暚陇潞陋脢脣脠锟矫嵜幟忊敇鈹屸枅鈻劼γ屸杸脫脽脭脪玫脮碌锟矫椕毭浢櫭柯绰憋拷戮露搂梅赂掳篓路鹿鲁虏鈻犅�",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[861] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~脟眉茅芒盲脿氓莽锚毛猫脨冒脼脛脜脡忙脝么枚镁没脻媒脰脺酶拢脴鈧捗∶趁好伱嵜撁毬库寪卢陆录隆芦禄鈻戔枓鈻撯攤鈹も暋鈺⑩晼鈺曗暎鈺戔晽鈺濃暅鈺涒攼鈹斺敶鈹敎鈹€鈹尖暈鈺熲暁鈺斺暕鈺︹暊鈺愨暚鈺р暔鈺も暐鈺欌晿鈺掆晸鈺暘鈹樷攲鈻堚杽鈻屸枑鈻€伪脽螕蟺危蟽碌蟿桅螛惟未鈭炏單碘埄鈮÷扁墺鈮も尃鈱∶封増掳鈭櫬封垰鈦柯测枲聽",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[865] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~脟眉茅芒盲脿氓莽锚毛猫茂卯矛脛脜脡忙脝么枚貌没霉每脰脺酶拢脴鈧捗∶趁好泵懧郝库寪卢陆录隆芦陇鈻戔枓鈻撯攤鈹も暋鈺⑩晼鈺曗暎鈺戔晽鈺濃暅鈺涒攼鈹斺敶鈹敎鈹€鈹尖暈鈺熲暁鈺斺暕鈺︹暊鈺愨暚鈺р暔鈺も暐鈺欌晿鈺掆晸鈺暘鈹樷攲鈻堚杽鈻屸枑鈻€伪脽螕蟺危蟽碌蟿桅螛惟未鈭炏單碘埄鈮÷扁墺鈮も尃鈱∶封増掳鈭櫬封垰鈦柯测枲聽",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[866] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~袗袘袙袚袛袝袞袟袠袡袣袥袦袧袨袩袪小孝校肖啸笑效楔些歇蝎鞋协挟携邪斜胁谐写械卸蟹懈泄泻谢屑薪芯锌鈻戔枓鈻撯攤鈹も暋鈺⑩晼鈺曗暎鈺戔晽鈺濃暅鈺涒攼鈹斺敶鈹敎鈹€鈹尖暈鈺熲暁鈺斺暕鈺︹暊鈺愨暚鈺р暔鈺も暐鈺欌晿鈺掆晸鈺暘鈹樷攲鈻堚杽鈻屸枑鈻€褉褋褌褍褎褏褑褔褕褖褗褘褜褝褞褟衼褢袆褦袊褩袔褳掳鈭櫬封垰鈩柭も枲聽",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[874] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~鈧拷锟斤拷锟解€︼拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鈥樷€欌€溾€濃€⑩€撯€旓拷锟斤拷锟斤拷锟斤拷锟铰犩竵喔傕竷喔勦竻喔嗋竾喔堗笁喔娻笅喔屶笉喔庎笍喔愢笐喔掄笓喔斷笗喔栢笚喔樴笝喔氞笡喔溹笣喔炧笩喔犩浮喔⑧福喔む弗喔︵抚喔ㄠ俯喔斧喔腑喔腐喔班副喔侧赋喔脆傅喔多阜喔膏腹喔猴拷锟斤拷锟洁缚喙€喙佮箓喙冟箘喙呧箚喙囙箞喙夃箠喙嬥箤喙嵿箮喙忇箰喙戉箳喙撪箶喙曕箹喙椸箻喙權箽喙涳拷锟斤拷锟�",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[895] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~膶眉茅膹盲膸扭膷臎臍墓脥木仟脛脕脡啪沤么枚脫暖脷媒脰脺艩慕脻艠钮谩铆贸煤艌艊女脭拧艡艜艛录搂芦禄鈻戔枓鈻撯攤鈹も暋鈺⑩晼鈺曗暎鈺戔晽鈺濃暅鈺涒攼鈹斺敶鈹敎鈹€鈹尖暈鈺熲暁鈺斺暕鈺︹暊鈺愨暚鈺р暔鈺も暐鈺欌晿鈺掆晸鈺暘鈹樷攲鈻堚杽鈻屸枑鈻€伪脽螕蟺危蟽碌蟿桅螛惟未鈭炏單碘埄鈮÷扁墺鈮も尃鈱∶封増掳鈭櫬封垰鈦柯测枲聽",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[932] = function() {
	var e = [],
		r = {},
		t = [],
		a;
	t[0] = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤健锝剑锝わ渐锝︼涧锝僵锝将锝江锝蒋锝帮奖锝诧匠锝达降锝讹椒锝革焦锝猴交锝硷浇锝撅娇锞€锞侊緜锞冿緞锞咃締锞囷緢锞夛緤锞嬶緦锞嶏編锞忥緪锞戯緬锞擄緮锞曪緰锞楋緲锞欙練锞涳緶锞濓緸锞燂拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[0].length; ++a)
		if(t[0][a].charCodeAt(0) !== 65533) { r[t[0][a]] = 0 + a;
			e[0 + a] = t[0][a] }
	t[129] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷銆€銆併€傦紝锛庛兓锛氾紱锛燂紒銈涖倻麓锝€篓锛撅浚锛裤兘銉俱倽銈炪€冧粷銆呫€嗐€囥兗鈥曗€愶紡锛硷綖鈭ワ綔鈥︹€モ€樷€欌€溾€濓紙锛夈€斻€曪蓟锛斤經锝濄€堛€夈€娿€嬨€屻€嶃€庛€忋€愩€戯紜锛嵚泵楋拷梅锛濃墵锛滐紴鈮︹墽鈭炩埓鈾傗檧掳鈥测€斥剝锟ワ紕锟狅俊锛咃純锛嗭紛锛犅р槅鈽呪棆鈼忊棊鈼団梿鈻♀枲鈻斥柌鈻解柤鈥汇€掆啋鈫愨啈鈫撱€擄拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鈭堚垕鈯嗏妵鈯傗妰鈭埄锟斤拷锟斤拷锟斤拷锟斤拷鈭р埁锟⑩噿鈬斺垁鈭冿拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鈭犫姤鈱掆垈鈭団墶鈮掆壀鈮垰鈭解垵鈭碘埆鈭拷锟斤拷锟斤拷锟斤拷鈩€扳櫙鈾櫔鈥犫€÷讹拷锟斤拷锟解棷锟斤拷锟�".split("");
	for(a = 0; a != t[129].length; ++a)
		if(t[129][a].charCodeAt(0) !== 65533) { r[t[129][a]] = 33024 + a;
			e[33024 + a] = t[129][a] }
	t[130] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤紣锛戯紥锛擄紨锛曪紪锛楋紭锛欙拷锟斤拷锟斤拷锟斤拷锛★饥锛ｏ激锛ワ鸡锛э绩锛╋吉锛棘锛籍锛及锛憋疾锛筹即锛碉级锛凤几锛癸己锟斤拷锟斤拷锟斤拷锟斤絹锝傦絻锝勶絽锝嗭絿锝堬綁锝婏綃锝岋綅锝庯綇锝愶綉锝掞綋锝旓綍锝栵綏锝橈綑锝氾拷锟斤拷锟姐亖銇傘亙銇勩亝銇嗐亣銇堛亯銇娿亱銇屻亶銇庛亸銇愩亼銇掋亾銇斻仌銇栥仐銇樸仚銇氥仜銇溿仢銇炪仧銇犮仭銇仯銇ゃ仴銇︺仹銇ㄣ仼銇伀銇伃銇伅銇般伇銇层伋銇淬伒銇躲伔銇搞伖銇恒伝銇笺伣銇俱伩銈€銈併倐銈冦倓銈呫倖銈囥倛銈夈倞銈嬨倢銈嶃値銈忋倫銈戙倰銈擄拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[130].length; ++a)
		if(t[130][a].charCodeAt(0) !== 65533) { r[t[130][a]] = 33280 + a;
			e[33280 + a] = t[130][a] }
	t[131] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷銈°偄銈ｃ偆銈ャ偊銈с偍銈┿偑銈偓銈偖銈偘銈便偛銈炽偞銈点偠銈枫偢銈广偤銈汇偧銈姐偩銈裤儉銉併儌銉冦儎銉呫儐銉囥儓銉夈儕銉嬨儗銉嶃儙銉忋儛銉戙儝銉撱償銉曘儢銉椼儤銉欍儦銉涖儨銉濄優銉燂拷銉犮儭銉儯銉ゃ儱銉︺儳銉ㄣ儵銉儷銉儹銉儻銉般儽銉层兂銉淬兊銉讹拷锟斤拷锟斤拷锟斤拷锟轿懳捨撐斘曃栁椢樜櫸毼浳溛澪炍熚犖∥Ｎのノξㄎ╋拷锟斤拷锟斤拷锟斤拷锟轿蔽参澄次滴段肺肝刮何晃嘉轿疚肯€蟻蟽蟿蠀蠁蠂蠄蠅锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[131].length; ++a)
		if(t[131][a].charCodeAt(0) !== 65533) { r[t[131][a]] = 33536 + a;
			e[33536 + a] = t[131][a] }
	t[132] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷袗袘袙袚袛袝衼袞袟袠袡袣袥袦袧袨袩袪小孝校肖啸笑效楔些歇蝎鞋协挟携锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟叫靶毙残承葱笛懶缎沸感剐盒恍夹斤拷芯锌褉褋褌褍褎褏褑褔褕褖褗褘褜褝褞褟锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟解攢鈹傗攲鈹愨敇鈹斺敎鈹敜鈹粹敿鈹佲攦鈹忊敁鈹涒敆鈹ｂ敵鈹敾鈺嬧敔鈹敤鈹封斂鈹濃敯鈹モ敻鈺傦拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[132].length; ++a)
		if(t[132][a].charCodeAt(0) !== 65533) { r[t[132][a]] = 33792 + a;
			e[33792 + a] = t[132][a] }
	t[135] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鈶犫憽鈶⑩懀鈶も懃鈶︹懅鈶ㄢ懇鈶懌鈶懎鈶懐鈶扳懕鈶测懗鈪犫叀鈪⑩叄鈪も叆鈪︹収鈪ㄢ叐锟姐崏銓斻將銔嶃寴銓с寖銓躲崙銔椼實銓︺專銓崐銓汇帨銕濄帪銕庛帍銖勩帯锟斤拷锟斤拷锟斤拷锟斤拷銔伙拷銆濄€熲剸銖嶁劇銑ゃ姤銑︺姧銑ㄣ埍銏层埞銔俱嵔銔尖墥鈮♀埆鈭垜鈭氣姤鈭犫垷鈯库埖鈭┾埅锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[135].length; ++a)
		if(t[135][a].charCodeAt(0) !== 65533) { r[t[135][a]] = 34560 + a;
			e[34560 + a] = t[135][a] }
	t[136] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟戒簻鍞栧▋闃垮搥鎰涙尐濮堕€㈣懙鑼滅鎮彙娓ユ棴钁﹁姦榀垫鍦ф枴鎵卞疀濮愯櫥椋寸耽缍鹃畮鎴栫矡琚峰畨搴垫寜鏆楁闂囬瀺鏉忎互浼婁綅渚濆亯鍥插し濮斿▉灏夋儫鎰忔叞鏄撴鐐虹晱鐣扮Щ缍矾鑳冭悗琛ｈ瑐閬曢伜鍖讳簳浜ュ煙鑲查儊纾竴澹辨孩閫哥ú鑼ㄨ妺榘厑鍗板捊鍝″洜濮诲紩椋叉帆鑳よ敪锟斤拷锟�".split("");
	for(a = 0; a != t[136].length; ++a)
		if(t[136][a].charCodeAt(0) !== 65533) { r[t[136][a]] = 34816 + a;
			e[34816 + a] = t[136][a] }
	t[137] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷闄㈤櫚闅犻熁鍚嬪彸瀹囩儚缇借總闆ㄥ嵂榈滅涓戠鑷兼甫鍢樺攧娆濊敋榘诲Д鍘╂郸鐡滈枏鍣備簯閬嬮洸鑽忛鍙″柖瀣板奖鏄犳洺鏍勬案娉虫穿鐟涚泩绌庨牬鑻辫瑭犻嫮娑茬柅鐩婇鎮﹁瑏瓒婇柌姒庡幁鍐嗭拷鍦掑牥濂勫寤舵€ㄦ帺鎻存部婕旂値鐒旂厵鐕曠尶绺佽壎鑻戣枟閬犻墰榇涘々鏂兼睔鐢ュ嚬澶ゥ寰€蹇滄娂鏃烘í娆ф鐜嬬縼瑗栭船榇庨粍宀℃矕鑽诲剟灞嬫喍鑷嗘《鐗′箼淇哄嵏鎭╂俯绌忛煶涓嬪寲浠綍浼戒尽浣冲姞鍙槈澶忓珌瀹跺绉戞殗鏋滄灦姝屾渤鐏弬绂嶇绋肩畤鑺辫嫑鑼勮嵎鑿彄铦﹁鍢╄波杩﹂亷闇炶殜淇勫敞鎴戠墮鐢昏嚗鑺借浘璩€闆呴椐曚粙浼氳В鍥炲澹婂换蹇€倲鎭㈡噽鎴掓嫄鏀癸拷锟斤拷".split("");
	for(a = 0; a != t[137].length; ++a)
		if(t[137][a].charCodeAt(0) !== 65533) { r[t[137][a]] = 35072 + a;
			e[35072 + a] = t[137][a] }
	t[138] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷榄佹櫐姊版捣鐏扮晫鐨嗙档鑺ヨ煿闁嬮殠璨濆嚤鍔惧鍜冲宕栨叏姒傛动纰嶈搵琛楄┎閹ч娴Θ铔欏灒鏌胯泿閳庡妰鍤囧悇寤撴嫛鎾规牸鏍告鐛茬⒑绌瑙掕但杓冮儹闁ｉ殧闈╁宀虫ソ椤嶉鎺涚瑺妯拷姗挎⒍榘嶆綗鍓插枬鎭版嫭娲绘竾婊戣憶瑜愯絼涓旈肮鍙舵妯洪瀯鏍厹绔冭挷閲滈帉鍣涢川鏍㈣寘钀辩播鍒堣媴鐡︿咕渚冨啝瀵掑垔鍕樺嫥宸诲枤鍫Е瀹屽畼瀵涘共骞规偅鎰熸叄鎲炬彌鏁㈡煈妗撴：娆炬瓝姹楁饥婢楁絽鐠扮敇鐩ｇ湅绔跨绨＄珐缂剁堪鑲濊墻鑾炶Τ璜岃搏閭勯憫闁撻枒闁㈤櫏闊撻え鑸樹父鍚哺宸岀帺鐧岀溂宀╃揩璐嬮泚闋戦椤樹紒浼庡嵄鍠滃櫒鍩哄瀣夊瘎宀愬笇骞惧繉鎻満鏃楁棦鏈熸妫勶拷锟斤拷".split("");
	for(a = 0; a != t[138].length; ++a)
		if(t[138][a].charCodeAt(0) !== 65533) { r[t[138][a]] = 35328 + a;
			e[35328 + a] = t[138][a] }
	t[139] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷姗熷赴姣呮皸姹界暱绁堝绋€绱€寰借瑷樿泊璧疯粚杓濋＂楱庨浜€鍋藉剙濡撳疁鎴妧鎿鐘犵枒绁囩京锜昏璀版幀鑿婇灎鍚夊悆鍠姗樿┌鐮ф澋榛嶅嵈瀹㈣剼铏愰€嗕笜涔呬粐浼戝強鍚稿寮撴€ユ晳锟芥溄姹傛辈娉ｇ伕鐞冪┒绐瑘绱氱尘绲︽棫鐗涘幓灞呭法鎷掓嫚鎸欐笭铏氳ū璺濋嫺婕佺Ζ榄氫酣浜含渚涗緺鍍戝厙绔跺叡鍑跺崝鍖″嵖鍙柆澧冨场寮峰綂鎬亹鎭専鏁欐娉佺媯鐙煰鑳歌剠鑸堣晭閮烽彙闊块椹氫话鍑濆碍鏆佹キ灞€鏇叉サ鐜夋绮佸儏鍕ゅ潎宸鹃對鏂ゆ娆界惔绂佺绛嬬穵鑺硅弻琛胯璎硅繎閲戝悷閵€涔濆€跺彞鍖虹嫍鐜栫煩鑻﹁函椐嗛椐掑叿鎰氳櫈鍠扮┖鍋跺瘬閬囬殔涓叉珱閲у睉灞堬拷锟斤拷".split("");
	for(a = 0; a != t[139].length; ++a)
		if(t[139][a].charCodeAt(0) !== 65533) { r[t[139][a]] = 35584 + a;
			e[35584 + a] = t[139][a] }
	t[140] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鎺樼獰娌撻澊杞＄鐔婇殘绮傛牀绻版閸嫴鍚涜柅瑷撶兢杌嶉儭鍗﹁绁佷總鍌惧垜鍏勫晸鍦彧鍨嬪褰㈠緞鎭垫叾鎱ф啯鎺叉惡鏁櫙妗傛笓鐣︾ń绯荤祵缍欑箣缃寧鑽婅泹瑷堣璀﹁唤闋氶稄鑺歌繋榀拷鍔囨垷鎾冩縺闅欐鍌戞瑺姹烘綌绌寸祼琛€瑷ｆ湀浠跺€瑰€﹀仴鍏煎埜鍓ｅ枾鍦忓爡瀚屽缓鎲叉嚫鎷虫嵅妞滄ī鐗界姮鐚爺纭倒鐪岃偐瑕嬭瑱璩㈣粧閬ｉ嵉闄洪楱撻垢鍏冨師鍘冲够寮︽笡婧愮巹鐝剧祪鑸疯█璜洪檺涔庡€嬪彜鍛煎浐濮戝宸卞韩寮ф埜鏁呮灟婀栫嫄绯婅⒋鑲¤儭鑿拌檸瑾囪法閳烽泧椤ч紦浜斾簰浼嶅崍鍛夊惥濞緦寰℃偀姊ф獛鐟氱瑾炶璀烽啇涔為瘔浜や郊渚€欏€栧厜鍏姛鍔瑰嬀鍘氬彛鍚戯拷锟斤拷".split("");
	for(a = 0; a != t[140].length; ++a)
		if(t[140][a].charCodeAt(0) !== 65533) { r[t[140][a]] = 35840 + a;
			e[35840 + a] = t[140][a] }
	t[141] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍚庡枆鍧戝灑濂藉瓟瀛濆畯宸ュ阀宸峰垢搴冨簹搴峰紭鎭掓厡鎶楁嫎鎺ф敾鏄傛檭鏇存澀鏍℃妲嬫睙娲旦娓簼鐢茬殗纭绯犵磪绱樼禐缍辫€曡€冭偗鑲辫厰鑶忚埅鑽掕琛¤瑳璨㈣臣閮婇叺閴辩牽閶奸枻闄嶏拷闋呴楂橀椿鍓涘姭鍙峰悎澹曟嫹婵犺豹杞熼汗鍏嬪埢鍛婂浗绌€閰烽禒榛掔崉婕夎叞鐢戝拷鎯氶鐙涜炯姝ら爟浠婂洶鍧ゅ⒕濠氭仺鎳囨槒鏄嗘牴姊辨贩鐥曠春鑹瓊浜涗綈鍙夊攩宓乏宸熁娌欑懗鐮傝閹栬鍧愬骇鎸偟鍌啀鏈€鍝夊濡诲褰╂墠鎺℃牻姝虫笀鐏介噰鐘€鐮曠牔绁枎绱拌彍瑁佽級闅涘墹鍦ㄦ潗缃病鍐村潅闃牶姒婅偞鍜插磶鍩肩榉轰綔鍓婂拫鎼炬槰鏈旀煹绐勭瓥绱㈤尟妗滈绗瑰寵鍐婂埛锟斤拷锟�".split("");
	for(a = 0; a != t[141].length; ++a)
		if(t[141][a].charCodeAt(0) !== 65533) { r[t[141][a]] = 36096 + a;
			e[36096 + a] = t[141][a] }
	t[142] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷瀵熸嫸鎾摝鏈钖╅洃鐨愰瘱鎹岄寙楫毧鏅掍笁鍌樺弬灞辨儴鎾掓暎妗熺嚘鐝婄敚绠楃簜铓曡畠璩涢吀椁愭柆鏆畫浠曚粩浼轰娇鍒哄徃鍙插棧鍥涘＋濮嬪濮垮瓙灞嶅競甯織鎬濇寚鏀瓬鏂柦鏃ㄦ灊姝拷姝绘皬鐛呯绉佺掣绱欑传鑲㈣剛鑷宠瑭炶┅瑭﹁獙璜硣璩滈泴椋兼浜嬩技渚嶅厫瀛楀鎱堟寔鏅傛婊嬫不鐖剧捊鐥旂绀鸿€岃€宠嚜钂旇緸姹愰箍寮忚瓨榇杌稿畭闆竷鍙卞煼澶卞珘瀹ゆ倝婀挎紗鐤捐唱瀹熻攢绡犲伈鏌磋姖灞¤晩绺炶垘鍐欏皠鎹ㄨ郸鏂滅叜绀剧礂鑰呰瑵杌婇伄铔囬偑鍊熷嫼灏烘潛鐏肩埖閰岄噲閷嫢瀵傚急鎯逛富鍙栧畧鎵嬫湵娈婄嫨鐝犵ó鑵叮閰掗鍎掑彈鍛鎺堟ü缍渶鍥氬弾鍛拷锟斤拷".split("");
	for(a = 0; a != t[142].length; ++a)
		if(t[142][a].charCodeAt(0) !== 65533) { r[t[142][a]] = 36352 + a;
			e[36352 + a] = t[142][a] }
	t[143] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷瀹楀氨宸炰慨鎰佹嬀娲茬绉嬬祩绻嶇繏鑷垷钂愯瑗茶異韫磋集閫遍厠閰泦閱滀粈浣忓厖鍗佸緭鎴庢煍姹佹笅鐛ｇ甫閲嶉妰鍙斿瀹挎窇绁濈府绮涘【鐔熷嚭琛撹堪淇婂郴鏄ョ灛绔ｈ垳椐垮噯寰棳妤畨娣筹拷婧栨饯鐩剧磾宸￠伒閱囬爢鍑﹀垵鎵€鏆戞洐娓氬憾绶掔讲鏇歌柉钘疯鍔╁彊濂冲簭寰愭仌閶ら櫎鍌峰劅鍕濆尃鍗囧彫鍝ㄥ晢鍞卞槜濂ㄥ濞煎灏嗗皬灏戝皻搴勫簥寤犲桨鎵挎妱鎷涙帉鎹锋槆鏄屾槶鏅舵澗姊㈡妯垫布娑堟笁婀樼劶鐒︾収鐥囩渷纭濈绁ョО绔犵瑧绮х垂鑲栬彇钂嬭晧琛濊３瑷熻瑭旇┏璞¤碁閱ら墻閸鹃悩闅滈灅涓婁笀涓炰箺鍐楀壈鍩庡牬澹屽甯告儏鎿炬潯鏉栨祫鐘剁暢绌ｈ捀璀查喐閷犲槺鍩撮＞锟斤拷锟�".split("");
	for(a = 0; a != t[143].length; ++a)
		if(t[143][a].charCodeAt(0) !== 65533) { r[t[143][a]] = 36608 + a;
			e[36608 + a] = t[143][a] }
	t[144] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鎷娈栫嚟绻旇伔鑹茶Е椋熻潟杈卞盎浼镐俊渚靛攪濞犲瘽瀵╁績鎱庢尟鏂版檵妫娴告繁鐢崇柟鐪熺绉︾闯鑷ｈ姱钖Κ瑷鸿韩杈涢€查嚌闇囦汉浠佸垉濉靛，灏嬬敋灏借厧瑷婅繀闄ｉ澀绗ヨ珡闋堥參鍥冲帹锟介€楀惞鍨傚弗鎺ㄦ按鐐婄潯绮嬬繝琛伴亗閰旈寪閷橀殢鐟為珓宕囧旦鏁版灑瓒ㄩ洓鎹潐妞欒弲闋楅泙瑁炬緞鎽哄涓栫€暆鏄噭鍒跺嫝濮撳緛鎬ф垚鏀挎暣鏄熸櫞妫叉爾姝ｆ竻鐗茬敓鐩涚簿鑱栧０瑁借タ瑾犺獡璜嬮€濋啋闈掗潤鏂夌◣鑴嗛毣甯儨鎴氭枼鏄旀瀽鐭崇绫嶇妇鑴婅铂璧よ贰韫熺ⅸ鍒囨嫏鎺ユ憘鎶樿ō绐冪瘈瑾洩绲惰垖铦変粰鍏堝崈鍗犲灏傚皷宸濇垿鎵囨挵鏍撴牬娉夋祬娲楁煋娼滅厧鐓芥棆绌跨绶氾拷锟斤拷".split("");
	for(a = 0; a != t[144].length; ++a)
		if(t[144][a].charCodeAt(0) !== 65533) { r[t[144][a]] = 36864 + a;
			e[36864 + a] = t[144][a] }
	t[145] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绻婄鲸鑵鸿垱鑸硅枽瑭硯璺甸伕閬烽姯閵戦杻楫墠鍠勬几鐒跺叏绂呯箷鑶崇硯鍣屽宀ㄦ帾鏇炬浗妤氱嫏鐤忕枎绀庣绉熺矖绱犵祫铇囪ù闃婚仭榧犲儳鍓靛弻鍙㈠€夊柂澹鐖藉畫灞ゅ対鎯ｆ兂鎹滄巸鎸挎幓锟芥搷鏃╂浌宸ｆ妲芥紩鐕ヤ簤鐥╃浉绐撶碂绶忕稖鑱¤崏鑽樿懍钂艰椈瑁呰蛋閫侀伃閹楅湝楱掑儚澧楁啂鑷撹數璐堥€犱績鍋村墖鍗虫伅鎹夋潫娓冻閫熶織灞炶硦鏃忕稓鍗掕鍏舵弮瀛樺灏婃悕鏉戦仠浠栧澶卑瑭戝斁鍫曞Ε鎯版墦鏌佽埖妤曢檧椐勯è浣撳爢瀵捐€愬脖甯緟鎬犳厠鎴存浛娉版粸鑳庤吙鑻旇璨搁€€閫殜榛涢瘺浠ｅ彴澶х閱嶉榉规粷鐎у崜鍟勫畢鎵樻姙鎷撴并婵悽瑷楅惛婵佽鑼稿嚙铔稿彧锟斤拷锟�".split("");
	for(a = 0; a != t[145].length; ++a)
		if(t[145][a].charCodeAt(0) !== 65533) { r[t[145][a]] = 37120 + a;
			e[37120 + a] = t[145][a] }
	t[146] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍙╀絾閬旇景濂劚宸界杈挎璋风嫺楸堟ń瑾颁腹鍗樺槅鍧︽媴鎺㈡棪姝庢贰婀涚偔鐭绠痘鑰借儐铔嬭獣閸涘洠澹囧季鏂殩妾€娈电敺璜囧€ょ煡鍦板紱鎭ユ櫤姹犵棿绋氱疆鑷磋湗閬呴Τ绡夌暅绔圭瓚钃勶拷閫愮З绐掕尪瀚＄潃涓徊瀹欏繝鎶芥樇鏌辨敞铏》瑷婚厧閶抽妯楃€︾尓鑻ц憲璨竵鍏嗗噵鍠嬪甯栧赋搴佸紨寮靛将寰存嚥鎸戞殺鏈濇疆鐗掔敽鐪鸿伌鑴硅吀铦惰璜滆秴璺抽姎闀烽爞槌ュ媴鎹楃洿鏈曟矆鐝嶈硟閹櫝娲ュ妞庢杩介帤鐥涢€氬鏍傛幋妲讳絻婕煒杈昏敠缍撮崝妞挎桨鍧７瀣船鐖悐閲ｉ洞浜綆鍋滃伒鍓冭矠鍛堝牑瀹氬笣搴曞涵寤峰紵鎮屾姷鎸烘彁姊眬纰囩绋嬬窢鑹囪▊璜﹁箘閫擄拷锟斤拷".split("");
	for(a = 0; a != t[146].length; ++a)
		if(t[146][a].charCodeAt(0) !== 65533) { r[t[146][a]] = 37376 + a;
			e[37376 + a] = t[146][a] }
	t[147] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閭搁劖閲橀紟娉ユ憳鎿㈡暤婊寸殑绗涢仼閺戞汉鍝插竟鎾よ綅杩墑鍏稿～澶╁睍搴楁坊绾忕敎璨艰虎椤涚偣浼濇婢辩敯闆诲厧鍚愬牭濉楀Μ灞犲緬鏂楁潨娓＄櫥鑿熻抄閫旈兘閸嶇牓鐮哄姫搴﹀湡濂存€掑€掑厷鍐拷鍑嶅垁鍞愬濉樺瀹曞扯宥嬫偧鎶曟惌鏉辨姊兼鐩楁窐婀稕鐏噲褰撶棙绁风瓑绛旂瓛绯栫当鍒拌懀钑╄棨瑷庤瑒璞嗚笍閫冮€忛悪闄堕牠楱伴棙鍍嶅嫊鍚屽爞灏庢啩鎾炴礊鐬崇鑳磋悇閬撻妳宄犻磭鍖垮緱寰虫稖鐗圭潱绂跨姣掔嫭瑾爟姗″嚫绐佹ご灞婇扯鑻瘏閰夌€炲櫢灞儑鏁︽矊璞氶亖闋撳憫鏇囬垗濂堥偅鍐呬箥鍑枡璎庣仒鎹洪崑妤㈤Υ绺勭暦鍗楁杌熼洠姹濅簩灏煎紣杩╁寕璩戣倝铏瑰豢鏃ヤ钩鍏ワ拷锟斤拷".split("");
	for(a = 0; a != t[147].length; ++a)
		if(t[147][a].charCodeAt(0) !== 65533) { r[t[147][a]] = 37632 + a;
			e[37632 + a] = t[147][a] }
	t[148] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷濡傚翱闊换濡婂繊瑾嶆俊绂扮ア瀵ц懕鐚啽骞村康鎹绘挌鐕冪矘涔冨患涔嬪煖鍤㈡偐婵冪磵鑳借劤鑶胯静瑕楄殼宸存妸鎾鏉锋尝娲剧惗鐮村﹩缃佃姯棣砍寤冩嫕鎺掓晽鏉泝鐗岃儗鑲鸿缉閰嶅€嶅煿濯掓锟芥コ鐓ょ嫿璨峰２璩犻櫔閫欒澘绉ょ煣钀╀集鍓ュ崥鎷嶆煆娉婄櫧绠旂矔鑸惰杽杩洕婕犵垎绺涜帿椐侀害鍑界纭茬鑲囩瓐娅ㄥ埂鑲岀晳鐣犲叓閴㈡簩鐧洪啑楂紣缃版姕绛忛枼槌╁櫤濉欒洡闅间即鍒ゅ崐鍙嶅彌甯嗘惉鏂戞澘姘炬睅鐗堢姱鐝晹绻佽埇钘╄博绡勯噯鐓╅爳椋尳鏅╃暘鐩ょ钑冭洰鍖崙鍚﹀搴囧郊鎮叉墘鎵规姭鏂愭瘮娉岀柌鐨绉樼穻缃疯偉琚璨婚伩闈為妯嬬案鍌欏熬寰瀲姣樼惖鐪夌編锟斤拷锟�".split("");
	for(a = 0; a != t[148].length; ++a)
		if(t[148][a].charCodeAt(0) !== 65533) { r[t[148][a]] = 37888 + a;
			e[37888 + a] = t[148][a] }
	t[149] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷榧绘煀绋楀尮鐤嬮褰﹁啙鑿辫倶寮煎繀鐣㈢瓎閫兼¨濮獩绱愮櫨璎康褰姘锋紓鐡㈢エ琛ㄨ璞瑰粺鎻忕梾绉掕嫍閷ㄩ嫴钂滆洯榘搧褰枌娴滅€曡钵璩撻牷鏁忕摱涓嶄粯鍩犲か濠﹀瘜鍐ㄥ竷搴滄€栨壎鏁凤拷鏂ф櫘娴埗绗﹁厫鑶氳姍璀滆矤璩﹁荡闃滈檮渚挮姝﹁垶钁¤暘閮ㄥ皝妤撻ⅷ钁鸿晽浼忓壇寰╁箙鏈嶇鑵硅瑕嗘返寮楁墪娌镐粡鐗╅畳鍒嗗惢鍣村⒊鎲ゆ壆鐒氬ギ绮夌碁绱涢洶鏂囪仦涓欎降鍏靛骞ｅ钩寮婃焺涓﹁斀闁夐櫅绫抽爜鍍诲鐧栫ⅶ鍒ョ灔钄戠畣鍋忓鐗囩瘒绶ㄨ竞杩旈亶渚垮媺濞╁紒闉繚鑸楅嫪鍦冩崟姝╃敨瑁滆紨绌傚嫙澧撴厱鎴婃毊姣嶇翱鑿╁€ｄ扛鍖呭憜鍫卞瀹濆嘲宄穿搴栨姳鎹ф斁鏂规湅锟斤拷锟�".split("");
	for(a = 0; a != t[149].length; ++a)
		if(t[149][a].charCodeAt(0) !== 65533) { r[t[149][a]] = 38144 + a;
			e[38144 + a] = t[149][a] }
	t[150] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷娉曟场鐑圭牪绺優鑺宠悓钃渹瑜掕í璞婇偊閶掗＝槌抽惮涔忎骸鍌嶅墫鍧婂Θ甯藉繕蹇欐埧鏆存湜鏌愭鍐掔础鑲啫璎€璨岃部閴鹃槻鍚犻牞鍖楀儠鍗滃ⅷ鎾叉湸鐗х潶绌嗛嚘鍕冩病娈嗗爛骞屽鏈炕鍑＄泦锟芥懇纾ㄩ瓟楹诲煁濡规槯鏋氭瘞鍝╂骞曡啘鏋曢鏌鹃睊妗濅害淇ｅ張鎶规湯娌縿渚弓楹夸竾鎱㈡簚婕敁鍛虫湭榄呭烦绠曞铂瀵嗚湝婀婅搼绋旇剤濡欑矋姘戠湢鍕欏あ鐒＄墴鐭涢湩榈℃濠垮鍐ュ悕鍛芥槑鐩熻糠閵橀炒濮墲婊呭厤妫夌犊绶潰楹烘懜妯¤寕濡勫瓱姣涚寷鐩茬恫鑰楄挋鍎叉湪榛欑洰鏉㈠嬁椁呭挨鎴荤本璨板晱鎮剁磱闁€鍖佷篃鍐跺鐖鸿€堕噹寮ョ煝鍘勫焦绱勮柆瑷宠簫闈栨煶钖憮鎰夋剤娌圭檼锟斤拷锟�".split("");
	for(a = 0; a != t[150].length; ++a)
		if(t[150][a].charCodeAt(0) !== 65533) { r[t[150][a]] = 38400 + a;
			e[38400 + a] = t[150][a] }
	t[151] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷璜几鍞綉鍎媷鍙嬪骞芥偁鎲傛彇鏈夋煔婀ф秾鐚剁尫鐢辩瑁曡獦閬婇倯閮甸泟铻嶅浜堜綑涓庤獕杓块爯鍌辜濡栧搴告彋鎻烘搧鏇滄妲樻磱婧剁啍鐢ㄧ缇婅€€钁夎搲瑕佽韪婇仴闄介鎱炬姂娆诧拷娌冩荡缈岀考娣€缇呰灪瑁告潵鑾遍牸闆锋礇绲¤惤閰贡鍗靛祼娆勬揩钘嶈槶瑕у埄鍚忓饱鏉庢ⅷ鐞嗙拑鐥㈣瑁￠噷闆㈤櫢寰嬬巼绔嬭憥鎺犵暐鍔夋祦婧滅悏鐣欑～绮掗殕绔滈緧渚舵叜鏃呰櫆浜嗕寒鍍氫浮鍑屽鏂欐娑肩専鐧傜灜绋滅厂鑹珤閬奸噺闄甸牁鍔涚窇鍊帢鏋楁穻鐕愮惓鑷ㄨ吉闅ｉ睏楹熺憼濉佹稒绱浠や级渚嬪喎鍔卞逗鎬滅幉绀艰嫇閳撮毞闆堕湂楹楅舰鏆︽鍒楀姡鐑堣寤夋亱鎲愭迹鐓夌熬绶磋伅锟斤拷锟�".split("");
	for(a = 0; a != t[151].length; ++a)
		if(t[151][a].charCodeAt(0) !== 65533) { r[t[151][a]] = 38656 + a;
			e[38656 + a] = t[151][a] }
	t[152] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷钃€ｉ尙鍛傞娅撶倝璩傝矾闇插姶濠佸粖寮勬湕妤兼娴紡鐗㈢嫾绡€佽伨铦嬮儙鍏簱绂勮倠閷茶珫鍊拰瑭辨璩勮剣鎯戞灎榉蹭簷浜橀皭瑭梺钑ㄦ婀剧鑵曪拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉紝涓愪笗涓副涓朵讣涓夸箓涔栦箻浜備簠璞簥鑸掑紞浜庝簽浜熶籂浜喊浜充憾浠庝粛浠勪粏浠備粭浠炰画浠熶环浼変綒浼颁經浣濅綏浣囦蕉渚堜緩渚樹交浣╀桨渚戜蒋渚嗕緰鍎樹繑淇熶繋淇樹繘淇戜繗淇愪郡淇ュ€氬€ㄥ€斿€€ュ€呬紲淇跺€″€╁€烤淇€戝€嗗亙鍋囨渻鍋曞亹鍋堝仛鍋栧伂鍋稿個鍌氬倕鍌村偛锟斤拷锟�".split("");
	for(a = 0; a != t[152].length; ++a)
		if(t[152][a].charCodeAt(0) !== 65533) { r[t[152][a]] = 38912 + a;
			e[38912 + a] = t[152][a] }
	t[153] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍍夊儕鍌冲儌鍍栧優鍍ュ儹鍍ｅ儺鍍瑰兊鍎夊剚鍎傚剸鍎曞剶鍎氬劇鍎哄劮鍎煎劵鍎垮厐鍏掑厡鍏斿參绔稿叐鍏叜鍐€鍐傚洏鍐屽唹鍐忓啈鍐撳啎鍐栧啢鍐﹀啟鍐╁啰鍐喅鍐卞啿鍐板喌鍐藉噮鍑夊嚊鍑犺檿鍑╁嚟锟藉嚢鍑靛嚲鍒勫垕鍒斿垘鍒у埅鍒埑鍒瑰墢鍓勫墜鍓屽墳鍓斿壀鍓村墿鍓冲壙鍓藉妽鍔斿姃鍓卞妶鍔戣鲸杈у姮鍔娂鍔靛媮鍕嶅嫍鍕炲嫞鍕﹂－鍕犲嫵鍕靛嫺鍕瑰寙鍖堢敻鍖嶅寪鍖忓寱鍖氬專鍖尡鍖冲尭鍗€鍗嗗崊涓楀崏鍗嶅嚃鍗炲崺鍗鍗诲嵎鍘傚帠鍘犲帵鍘ュ幃鍘板幎鍙冪皰闆欏彑鏇肩嚠鍙彣鍙徍鍚佸惤鍛€鍚惌鍚煎惍鍚跺惄鍚濆憥鍜忓懙鍜庡憻鍛卞懛鍛板拻鍛诲拃鍛跺拕鍜愬拞鍝囧挗鍜稿挜鍜搫鍝堝挩锟斤拷锟�".split("");
	for(a = 0; a != t[153].length; ++a)
		if(t[153][a].charCodeAt(0) !== 65533) { r[t[153][a]] = 39168 + a;
			e[39168 + a] = t[153][a] }
	t[154] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍜搨鍜ゅ捑鍜煎摌鍝ュ摝鍞忓敂鍝藉摦鍝摵鍝㈠敼鍟€鍟ｅ晫鍞暅鍟呭晼鍟楀敻鍞冲暆鍠欏杸鍜枈鍠熷暬鍟惧枠鍠炲柈鍟煎杻鍠╁枃鍠ㄥ棜鍡呭棢鍡勫棞鍡ゅ棓鍢斿椃鍢栧椌鍡藉槢鍡瑰檸鍣愮嚐鍢村樁鍢插樃锟藉櫕鍣ゅ槸鍣櫔鍤嗗殌鍤婂殸鍤斿殢鍤ュ毊鍤跺毚鍥傚毤鍥佸泝鍥€鍥堝泿鍥戝洆鍥楀洰鍥瑰渶鍥垮渼鍦夊湀鍦嬪湇鍦撳湗鍦栧棁鍦滃湨鍦峰湼鍧庡溁鍧€鍧忓潻鍩€鍨堝潯鍧垮瀴鍨撳灎鍨冲灓鍨灠鍩冨焼鍩斿煉鍩撳爦鍩栧煟鍫嬪牂鍫濆〔鍫″、濉嬪“姣€濉掑牻濉瑰澧瑰澧⒑澹炲⒒澧稿澹呭澹戝澹欏澹ュ澹ゅ澹：澹瑰；澹煎＝澶傚澶愬姊﹀ぅ澶き澶插じ澶剧珤濂曞濂庡濂樺ア濂犲ェ濂ォ锟斤拷锟�".split("");
	for(a = 0; a != t[154].length; ++a)
		if(t[154][a].charCodeAt(0) !== 65533) { r[t[154][a]] = 39424 + a;
			e[39424 + a] = t[154][a] }
	t[155] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷濂稿濡濅綖渚Γ濡插濮ㄥ濡嶅濮氬ē濞熷☉濞滃▔濞氬﹢濠濞靛ǘ濠㈠┆濯氬濯惧珛瀚傚瀚ｅ珬瀚﹀瀚栧瀚诲瑢瀣嬪瑬瀣插珢瀣瀣惧瓋瀛呭瓈瀛戝瓡瀛氬瓫瀛ュ瀛板瀛靛鏂堝瀹€锟藉畠瀹﹀瀵冨瘒瀵夊瘮瀵愬瀵﹀瀵炲瀵瀵跺灏呭皣灏堝皪灏撳盃灏㈠皑灏稿肮灞佸眴灞庡睋灞愬睆瀛卞爆灞耿灞跺惫宀屽矐宀斿宀不宀跺布宀峰硡宀惧硣宄欏畅宄藉澈宄秾宄磱宕曞礂宓滃礋宕涘磻宕斿储宕氬礄宕樺祵宓掑祹宓嬪惮宓冲刀宥囧秳宥傚盯宥濆冬宥督宥愬斗宥煎穳宸嶅窊宸掑窎宸涘帆宸插返甯嬪笟甯欏笐甯涘付甯峰箘骞冨箑骞庡箺骞斿篃骞㈠工骞囧沟骞跺购楹煎箍搴犲粊寤傚粓寤愬粡锟斤拷锟�".split("");
	for(a = 0; a != t[155].length; ++a)
		if(t[155][a].charCodeAt(0) !== 65533) { r[t[155][a]] = 39680 + a;
			e[39680 + a] = t[155][a] }
	t[156] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷寤栧唬寤濆粴寤涘虎寤″花寤╁滑寤卞怀寤板淮寤稿痪寮冨級褰濆綔寮嬪紤寮栧缉寮几褰佸綀褰屽綆寮綉褰栧綏褰欏健褰匠褰峰緝寰傚娇寰婂緢寰戝緡寰炲緳寰樺緺寰ㄥ经寰煎繓蹇诲郡蹇稿勘蹇濇偝蹇挎€℃仩锟芥€欐€愭€╂€庢€辨€涙€曟€€︽€忔€烘仛鎭佹仾鎭锋仧鎭婃亞鎭嶆仯鎭冩仱鎭傛伂鎭仚鎮佹倣鎯ф們鎮氭倓鎮涙倴鎮楁倰鎮ф倠鎯℃偢鎯犳儞鎮村堪鎮芥儐鎮垫儤鎱嶆剷鎰嗘兌鎯锋剙鎯存兒鎰冩劇鎯绘儽鎰嶆剮鎱囨劸鎰ㄦ劎鎱婃効鎰兼劕鎰存劷鎱傛厔鎱虫叿鎱樻厵鎱氭叓鎱存叝鎱ユ叡鎱熸厺鎱撴叺鎲欐問鎲囨啲鎲旀啔鎲婃啈鎲啴鎳屾噴鎳夋嚪鎳堟噧鎳嗘喓鎳嬬焦鎳嶆嚘鎳ｆ嚩鎳烘嚧鎳挎嚱鎳兼嚲鎴€鎴堟垑鎴嶆垖鎴旀垱锟斤拷锟�".split("");
	for(a = 0; a != t[156].length; ++a)
		if(t[156][a].charCodeAt(0) !== 65533) { r[t[156][a]] = 39936 + a;
			e[39936 + a] = t[156][a] }
	t[157] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鎴炴垺鎴埉鎴版埐鎴虫墎鎵庢墳鎵ｆ墰鎵犳墾鎵兼妭鎶夋壘鎶掓姄鎶栨嫈鎶冩姅鎷楁嫅鎶绘嫃鎷挎媶鎿旀媹鎷滄媽鎷婃媯鎷囨姏鎷夋寣鎷嫳鎸ф寕鎸堟嫰鎷垫崘鎸炬崓鎼滄崗鎺栨帋鎺€鎺嵍鎺ｆ帍鎺夋師鎺垫崼锟芥崺鎺炬彥鎻€鎻嗘彛鎻夋彃鎻舵弰鎼栨惔鎼嗘悡鎼︽惗鏀濇悧鎼ㄦ悘鎽ф懐鎽舵憥鏀挄鎾撴挜鎾╂拡鎾兼摎鎿掓搮鎿囨捇鎿樻搨鎿辨摟鑸夋摖鎿℃姮鎿ｆ摨鏀摱鎿存摬鎿烘攢鎿芥敇鏀滄攨鏀ゆ敚鏀敶鏀垫敺鏀舵敻鐣嬫晥鏁栨晻鏁嶆晿鏁炴暆鏁叉暩鏂傛杻璁婃枦鏂熸柅鏂锋梼鏃嗘梺鏃勬棇鏃掓棝鏃欐棤鏃℃棻鏉叉槉鏄冩椈鏉虫樀鏄舵槾鏄滄檹鏅勬檳鏅佹櫈鏅濇櫎鏅ф櫒鏅熸櫌鏅版殐鏆堟殠鏆夋殑鏆樻殱鏇佹毠鏇夋毦鏆硷拷锟斤拷".split("");
	for(a = 0; a != t[157].length; ++a)
		if(t[157][a].charCodeAt(0) !== 65533) { r[t[157][a]] = 40192 + a;
			e[40192 + a] = t[157][a] }
	t[158] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鏇勬毟鏇栨洑鏇犳樋鏇︽洨鏇版浀鏇锋湉鏈栨湠鏈︽湩闇告湲鏈挎湺鏉佹湼鏈锋潌鏉炴潬鏉欐潱鏉ゆ瀴鏉版灘鏉兼潽鏋屾瀷鏋︽灐鏋呮灧鏌灤鏌灣鏌╂灨鏌ゆ煘鏌濇煝鏌灩鏌庢焼鏌ф獪鏍炴鏍╂妗嶆牪妗庯拷姊虫牜妗欐。妗锋】姊熸姊姊濇姊冩姊规〈姊垫姊烘姊嶆【妞佹妞堟妞㈡う妫℃妫嶆妫ф妞舵妞勬妫ｆぅ妫规　妫え妞妞ｆぁ妫嗘ス妤锋妤告カ妤旀ゾ妤す妤存そ妤欐ぐ妤℃妤濇妤Σ姒姒挎妲撴妲庡妲婃姒绘姒фó姒戞姒滄姒存妲ㄦ▊妯涙Э娆婃Ч妲叉Ё妯呮Ρ妯炴Л妯旀Й妯婃⊕娅佹ǎ妯撴﹦妯屾┎妯舵└姗囨姗欐│姗堟ǜ妯㈡獝妾嶆獱妾勬妾ｏ拷锟斤拷".split("");
	for(a = 0; a != t[158].length; ++a)
		if(t[158][a].charCodeAt(0) !== 65533) { r[t[158][a]] = 40448 + a;
			e[40448 + a] = t[158][a] }
	t[159] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷妾楄槜妾绘珒娅傛妾虫娅炴珣娅熸娅氭娅绘瑓铇栨娆掓瑬楝辨瑹娆告鐩滄椋瓏姝冩瓑姝愭瓩姝旀瓫姝熸姝告姝挎畝娈勬畠娈嶆畼娈曟疄娈ゆ娈娈叉娈虫娈兼瘑姣嬫瘬姣熸姣姣拷楹炬皥姘撴皵姘涙挨姘ｆ睘姹曟雹姹矀娌嶆矚娌佹矝姹炬报姹虫矑娌愭硠娉辨硴娌芥硹娉呮碀娌脖娌炬埠娉涙朝娉欐唱娲熻娲舵传娲芥锤娲欐吹娲虫磼娲屾担娑撴丹娴氭倒娴欐稁娑曟郡娑呮饭娓曟笂娑垫穱娣︽陡娣嗘番娣炴穼娣ㄦ窉娣呮泛娣欐筏娣曟藩娣腑婀府娓欐共婀熸妇娓ｆ公娓苟婀嶆笩婀冩负婀庢袱婊挎笣娓告簜婧簶婊夋悍婊撴航婧粍婧叉粩婊曟簭婧ユ粋婧熸絹婕戠亴婊桓婊炬伎婊叉急婊疾婊岋拷锟斤拷".split("");
	for(a = 0; a != t[159].length; ++a)
		if(t[159][a].charCodeAt(0) !== 65533) { r[t[159][a]] = 40704 + a;
			e[40704 + a] = t[159][a] }
	t[224] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷婕炬紦婊锋締娼烘礁婢佹線娼經婵虫江婢傛郊娼樻編婢戞總娼︽境婢ｆ尽婢ゆ竟婵嗘惊婵熸繒婵繑婵樻勘婵繘鐎夌€嬫亢鐎戠€佺€忔烤鐎涚€氭酱鐎濈€樼€熺€扮€剧€茬亼鐏ｇ倷鐐掔偗鐑辩偓鐐哥偝鐐儫鐑嬬儩锟界儥鐒夌兘鐒滅剻鐓ョ厱鐔堢叇鐓㈢厡鐓栫叕鐔忕嚮鐔勭啎鐔ㄧ啲鐕楃喒鐔剧噿鐕夌嚁鐕庣嚑鐕嚙鐕电嚰鐕圭嚳鐖嶇垚鐖涚埁鐖埇鐖扮埐鐖荤埣鐖跨墍鐗嗙墜鐗樼壌鐗剧妭鐘佺妵鐘掔姈鐘㈢姧鐘圭姴鐙冪媶鐙勭嫀鐙掔嫝鐙犵嫛鐙圭嫹鍊忕寳鐚婄寽鐚栫対鐚寸尟鐚╃尌鐚剧崕鐛忛粯鐛楃崻鐛ㄧ嵃鐛哥嵉鐛荤嵑鐝堢幊鐝庣幓鐝€鐝ョ彯鐝炵挗鐞呯懐鐞ョ徃鐞茬惡鐟曠惪鐟熺憴鐟佺憸鐟╃懓鐟ｇ應鐟剁懢鐠嬬挒鐠х搳鐡忕摂鐝憋拷锟斤拷".split("");
	for(a = 0; a != t[224].length; ++a)
		if(t[224][a].charCodeAt(0) !== 65533) { r[t[224][a]] = 57344 + a;
			e[57344 + a] = t[224][a] }
	t[225] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐡犵摚鐡х摡鐡摬鐡扮摫鐡哥摲鐢勭攦鐢呯攲鐢庣攳鐢曠敁鐢炵敠鐢敿鐣勭晬鐣婄晧鐣涚晢鐣氱暕鐣ょ暓鐣暛鐣哥暥鐤嗙枃鐣寸枈鐤夌杺鐤旂枤鐤濈枼鐤ｇ梻鐤崇梼鐤电柦鐤哥柤鐤辩棈鐥婄棐鐥欑棧鐥炵椌鐥匡拷鐥肩榿鐥扮椇鐥茬棾鐦嬬槏鐦夌槦鐦х槧鐦＄槩鐦ょ槾鐦扮樆鐧囩檲鐧嗙櫆鐧樼櫋鐧㈢櫒鐧╃櫔鐧х櫖鐧扮櫜鐧剁櫢鐧肩殌鐨冪殘鐨嬬殠鐨栫殦鐨欑殮鐨扮毚鐨哥毠鐨虹泜鐩嶇洊鐩掔洖鐩＄洢鐩х洩铇浕鐪堢渿鐪勭湬鐪ょ湠鐪ョ湨鐪涚湻鐪哥潎鐫氱潹鐫潧鐫ョ澘鐫剧澒鐬庣瀷鐬戠灎鐬炵灠鐬剁灩鐬跨灱鐬界灮鐭囩煃鐭楃煔鐭滅煟鐭熂鐮岀爳绀︾牋绀纰庣〈纰嗙〖纰氱纰ｇ⒌纰纾戠纾嬬纰剧⒓纾呯纾拷锟斤拷".split("");
	for(a = 0; a != t[225].length; ++a)
		if(t[225][a].charCodeAt(0) !== 65533) { r[t[225][a]] = 57600 + a;
			e[57600 + a] = t[225][a] }
	t[226] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷纾х纾界４绀囩绀戠绀か绁€绁犵绁熺绁曠绁虹タ绂婄绂ч綃绂Ξ绂崇绂虹绉曠Ё绉А绉ｇ▓绋嶇绋欑绋熺绋辩ɑ绋剧ǚ绌冪绌夌绌㈢┅榫濈┌绌圭┙绐堢獥绐曠獦绐栫绔堢锟界绔呯珓绐块們绔囩珚绔嶇珡绔曠珦绔欑珰绔濈绔㈢绔绗傜瑥绗婄瑔绗崇瑯绗欑瑸绗电绗剁瓙绛虹瑒绛嶇瑡绛岀瓍绛电绛寸绛扮绛绠濈畼绠熺畭绠滅畾绠嬬畳绠忕瓭绠欑瘚绡佺瘜绡忕绡嗙瘽绡╃皯绨旂绡ョ睜绨€绨囩皳绡崇绨楃皪绡剁埃绨х蔼绨熺胺绨敖绫岀眱绫旂睆绫€绫愮睒绫熺堡绫栫饱绫钡绮冪矏绮ょ箔绮㈢搏绮＄波绮崇膊绮辩伯绮圭步绯€绯呯硞绯樼硳绯滅尝楝荤朝绯茬炒绯剁澈绱嗭拷锟斤拷".split("");
	for(a = 0; a != t[226].length; ++a)
		if(t[226][a].charCodeAt(0) !== 65533) { r[t[226][a]] = 57856 + a;
			e[57856 + a] = t[226][a] }
	t[227] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绱傜礈绱曠磰绲呯祴绱床绱跨吹绲嗙党绲栫祹绲茬胆绲祻绲ｇ稉缍夌禌缍忕到缍涚逗缍叮缍电穱缍界东绺界盯缍窚缍哥稛缍扮窐绶濈筏绶炵坊绶茬贰绺呯笂绺ｇ浮绺掔副绺熺笁绺嬬涪绻嗙功绺荤傅绺圭箖绺凤拷绺茬负绻х節绻栫篂绻欑箽绻圭躬绻╃辜绻荤簝绶曠菇杈箍绾堢簤绾岀簰绾愮簱绾旂簴绾庣簺绾滅几缂虹絽缃岀綅缃庣綈缃戠綍缃旂綐缃熺綘缃ㄧ僵缃х礁缇傜締缇冪緢缇囩緦缇旂緸缇濈練缇ｇ警缇茬竟缇径缇歌缈呯繂缈婄繒缈旂俊缈︾咯缈崇抗椋滆€嗚€勮€嬭€掕€樿€欒€滆€¤€ㄨ€胯€昏亰鑱嗚亽鑱樿仛鑱熻仮鑱ㄨ伋鑱茶伆鑱惰伖鑱借伩鑲勮倖鑲呰倹鑲撹倸鑲啇鑲儧鑳ヨ儥鑳濊儎鑳氳儢鑴夎儻鑳辫剾鑴╄劊鑴厠锟斤拷锟�".split("");
	for(a = 0; a != t[227].length; ++a)
		if(t[227][a].charCodeAt(0) !== 65533) { r[t[227][a]] = 58112 + a;
			e[58112 + a] = t[227][a] }
	t[228] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷闅嬭厗鑴捐厯鑵戣兗鑵辫叜鑵ヨ叇鑵磋唭鑶堣唺鑶€鑶傝啝鑶曡啢鑶ｈ厽鑶撹啯鑶拌喌鑶捐喐鑶借噣鑷傝喓鑷夎噸鑷戣嚈鑷樿噲鑷氳嚐鑷犺嚙鑷鸿嚮鑷捐垇鑸傝垍鑸囪垔鑸嶈垚鑸栬埄鑸埜鑸宠墍鑹欒墭鑹濊墯鑹熻墹锟借墷鑹ㄨ壀鑹埉鑹辫壏鑹歌壘鑺嶈姃鑺姛鑺昏姮鑻¤嫞鑻熻嫆鑻磋嫵鑻鸿帗鑼冭嫽鑻硅嫗鑼嗚嫓鑼夎嫏鑼佃尨鑼栬尣鑼辫崁鑼硅崘鑽呰尟鑼寳鑼樿巺鑾氳帾鑾熻帰鑾栬專鑾庤巼鑾婅嵓鑾佃嵆鑽佃帬鑾夎帹鑿磋悡鑿弾鑿借悆鑿樿悑鑿佽彿钀囪彔鑿茶悕钀㈣悹鑾借惛钄嗚徎钁惇钀艰暁钂勮懛钁挱钁拏钁╄憜钀懐钁硅惖钃婅憿钂硅捒钂熻摍钃嶈捇钃氳搻钃佽搯钃栬挕钄¤摽钃磋敆钄樿敩钄熻敃钄旇摷钑€钑ｈ晿钑堬拷锟斤拷".split("");
	for(a = 0; a != t[228].length; ++a)
		if(t[228][a].charCodeAt(0) !== 65533) { r[t[228][a]] = 58368 + a;
			e[58368 + a] = t[228][a] }
	t[229] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷钑佽槀钑嬭晻钖€钖よ枅钖戣枈钖ㄨ暛钖旇枦钘枃钖滆暦钑捐枑钘夎柡钘忚柟钘愯棔钘濊棩钘滆椆铇婅槗铇嬭椌钘鸿槅铇㈣槡铇拌樋铏嶄箷铏旇櫉铏ц櫛铓撹殻铓╄毆铓嬭殞铓惰毌铔勮泦铓拌泬锠ｈ毇铔旇洖铔╄洭锟借洘铔涜洴铚掕渾铚堣渶铚冭浕铚戣湁铚嶈浌铚婅湸铚胯湻铚昏湧铚╄湚铦犺潫铦歌潓铦庤澊铦楄潹铦潤铦撹潱铦爡铻㈣灍铻傝灟锜嬭灲锜€锜愰洊铻焺铻宠焽锜嗚灮锜煵锜犺爮锠嶈熅锜惰煼锠庤煉锠戣爾锠曡牏锠¤牨锠惰牴锠ц牷琛勮琛掕琛炶、琛琛捐琛佃〗琚佃〔琚傝琚掕琚欒ⅱ琚嶈ⅳ琚拌⒖琚辫瑁勮瑁樿瑁濊９瑜傝＜瑁磋（瑁茶瑜岃瑜撹瑜炶ぅ瑜か瑗佽瑜昏ざ瑜歌瑜濊瑗烇拷锟斤拷".split("");
	for(a = 0; a != t[229].length; ++a)
		if(t[229][a].charCodeAt(0) !== 65533) { r[t[229][a]] = 58624 + a;
			e[58624 + a] = t[229][a] }
	t[230] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷瑗﹁イ瑗オ瑗ゴ瑗疯ゾ瑕冭瑕婅瑕樿Α瑕╄Ζ瑕Ο瑕茶瑕借瑙€瑙氳瑙濊Ё瑙磋Ц瑷冭瑷愯▽瑷涜瑷ヨǘ瑭佽瑭掕﹩瑭堣┘瑭┈瑭㈣獏瑾傝獎瑾ㄨ瑾戣瑾﹁獨瑾ｈ珓璜嶈珎璜氳璜宠锟借璜辫瑪璜犺璜疯珵璜涜瑢璎囪瑲璜¤瑬璎愯瑮璎犺闉璎璎ㄨ瓉璀岃瓘璀庤瓑璀栬瓫璀氳璀熻璀璀借畝璁岃畮璁掕畵璁栬畽璁氳昂璞佽翱璞堣睂璞庤睈璞曡雹璞备璞鸿矀璨夎矃璨婅矋璨庤矓璞艰矘鎴濊箔璨步璨茶渤璨捕璩堣硜璩よ常璩氳辰璩鸿郴璐勮磪璐婅磭璐忚磵璐愰綆璐撹硩璐旇礀璧ц淡璧辫党瓒佽稒璺傝毒瓒鸿窂璺氳窎璺岃窙璺嬭藩璺窡璺ｈ芳韪堣笁璺胯笣韪炶笎韪熻箓韪佃赴韪磋箠锟斤拷锟�".split("");
	for(a = 0; a != t[230].length; ++a)
		if(t[230][a].charCodeAt(0) !== 65533) { r[t[230][a]] = 58880 + a;
			e[58880 + a] = t[230][a] }
	t[231] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷韫囪箟韫岃箰韫堣箼韫よ範韪梗韫曡苟韫茶辜韬佽簢韬呰簞韬嬭簥韬撹簯韬旇簷韬骸韬喊杌嗚罕韬捐粎杌堣粙杌涜唬杌艰换杌痪杓婅紖杓曡紥杓欒紦杓滆紵杓涜紝杓﹁汲杓昏脊杞呰絺杓捐綄杞夎絾杞庤綏杞滐拷杞㈣剑杞よ緶杈熻荆杈警杈疯繗杩ヨ竣杩刊閭囪看閫呰抗杩洪€戦€曢€￠€嶉€為€栭€嬮€ч€堕€甸€硅扛閬忛亹閬戦亽閫庨亯閫鹃仏閬橀仦閬ㄩ伅閬堕毃閬查倐閬介倎閭€閭婇倝閭忛偍閭偙閭甸儮閮ゆ増閮涢剛閯掗剻閯查劙閰婇厲閰橀叄閰ラ叐閰抽叢閱嬮唹閱傞啟閱啹閱喌閱撮喓閲€閲侀噳閲嬮噽閲栭嚐閲￠嚊閲奸嚨閲堕垶閲块垟閳垥閳戦墳閴楅墔閴夐墹閴堥姇閳块墜閴愰姕閵栭姄閵涢墯閶忛姽閵烽嫨閷忛嫼閸勯尞锟斤拷锟�".split("");
	for(a = 0; a != t[231].length; ++a)
		if(t[231][a].charCodeAt(0) !== 65533) { r[t[231][a]] = 59136 + a;
			e[59136 + a] = t[231][a] }
	t[232] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閷欓將閷氶專閷洪尩閷婚崪閸犻嵓閸崠閹伴幀閹帞閹归彇閺楅彣閺ラ彉閺冮彎閺愰張閺ら悮閻旈悡閻冮悋閻愰惗閻惖閻￠惡閼侀憭閼勯憶閼犻憿閼為應閳╅懓閼甸懛閼介憵閼奸懢閽侀懣闁傞枃闁婇枖闁栭枠闁欙拷闁犻枿闁ч柇闁奸柣闁归柧闂婃慷闂冮棈闂岄棔闂旈棖闂滈棥闂ラ棦闃￠槰闃槸闄傞檶闄忛檵闄烽櫆闄為櫇闄熼櫐闄查櫖闅嶉殬闅曢殫闅毀闅遍毑闅伴毚闅堕毟闅归泿闆嬮泬闆嶈闆滈湇闆曢浌闇勯渾闇堥湏闇庨湋闇忛湒闇欓湦闇湴闇归溄闇鹃潉闈嗛潏闈傞潐闈滈潬闈ら潶闈ㄥ嫆闈澅闈归瀰闈奸瀬闈洪瀱闉嬮瀼闉愰灉闉ㄩ灕闉ｉ灣闉撮焹闊嗛焾闊嬮煖闊綇闊茬珶闊堕煹闋忛爩闋搁牑闋￠牱闋介椤忛椤’椤帮拷锟斤拷".split("");
	for(a = 0; a != t[232].length; ++a)
		if(t[232][a].charCodeAt(0) !== 65533) { r[t[232][a]] = 59392 + a;
			e[59392 + a] = t[232][a] }
	t[233] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷椤遍〈椤抽ⅹ棰⒈棰堕椋冮椋╅＋椁冮椁掗椁橀ぁ椁濋椁ら椁ぎ椁介ぞ楗傞楗呴楗嬮楗掗楗曢棣橀Ε棣Ξ棣奸椐涢椐橀椐М椐遍Р椐婚Ц楱侀◤楱呴Б楱欓ǐ楱烽﹨椹傞﹢椹冿拷楱鹃椹嶉椹楅椹㈤━椹ら┅椹┆楠楠奸珋楂忛珣楂撻珨楂為珶楂㈤楂﹂楂楂撮楂烽楝嗛瑯楝氶瑹楝㈤楝ラ楝ㄩ楝楝榄勯瓋榄忛瓖榄庨瓚榄橀楫撻畠楫戦畺楫楅疅楫犻楫撮瘈榀婇榀嗛瘡榀戦瘨榀ｉ榀ら瘮榀￠昂榀查榀伴皶榘旈皦榘撻皩榘嗛皥榘掗皧榘勯爱榘涢哎榘ら啊榘伴眹榘查眴榘鹃睔楸犻抱楸堕备槌ч超槌伴磯榇堥倡榇冮磫榇处槎矗榇熼祫榇曢磼榈侀纯榇鹃祮榈堬拷锟斤拷".split("");
	for(a = 0; a != t[233].length; ++a)
		if(t[233][a].charCodeAt(0) !== 65533) { r[t[233][a]] = 59648 + a;
			e[59648 + a] = t[233][a] }
	t[234] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷榈濋禐榈ら祽榈愰禉榈查秹槎囬东榈岛槎氶钉槎╅恫榉勯穪槎婚陡槎洪穯榉忛穫榉欓窊榉搁乏榉矾榉介笟楦涢笧楣甸构楣介簛楹堥簨楹岄簰楹曢簯楹濋亥楹╅焊楹涵闈￠粚榛庨粡榛愰粩榛滈粸榛濋粻榛ラ花榛拷榛撮欢榛烽还榛婚患榛介紘榧堢毞榧曢肌榧季榻婇綊榻旈剑榻熼綘榻￠溅榻ч浆榻椒榻查蕉榫曢緶榫犲牤妲囬仚鐟ゅ嚋鐔欙拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[234].length; ++a)
		if(t[234][a].charCodeAt(0) !== 65533) { r[t[234][a]] = 59904 + a;
			e[59904 + a] = t[234][a] }
	t[237] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绾婅閸堥妶钃滀繅鐐绘槺妫堥嫻鏇诲絽涓ㄤ弧浠间紑浼冧脊浣栦緬渚婁練渚斾繊鍋€鍊靠鍊炲亞鍋板亗鍌斿兇鍍樺厞鍏ゅ啙鍐惧嚞鍒曞姕鍔﹀媭鍕涘寑鍖囧尋鍗插帗鍘插彎铷庡挏鍜婂挬鍝垮枂鍧欏潵鍨焾鍩囷◤锟斤◥澧炲⒉澶嬪濂涘濂ｅΔ濡哄瓥瀵€鐢瘶瀵盀宀﹀埠宄靛揣宓擄☉宓傚淡宥稿豆宸愬肌寮村涧寰峰繛鎭濇倕鎮婃優鎯曟劆鎯叉剳鎰锋劙鎲樻垞鎶︽彽鎽犳挐鎿庢晭鏄€鏄曟樆鏄夋槷鏄炴槫鏅ユ櫁鏅欙⊕鏅虫殭鏆犳毑鏆挎浐鏈庯ぉ鏉︽灮妗掓焵鏍佹妫忥〒妤〝姒樻Б妯版┇姗嗘┏姗炬娅ゆ瘱姘挎睖娌嗘悲娉氭磩娑囨弹娑栨冬娣忔犯娣叉芳娓规箿娓ф讣婧挎緢婢垫康鐎呯€囩€ㄧ倕鐐剰鐒勭厹鐓嗙厙铷曠噥鐕剧姳锟斤拷锟�".split("");
	for(a = 0; a != t[237].length; ++a)
		if(t[237][a].charCodeAt(0) !== 65533) { r[t[237][a]] = 60672 + a;
			e[60672 + a] = t[237][a] }
	t[238] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐘剧尋铷栫嵎鐜界弶鐝栫彛鐝掔悋鐝电惁鐞惄鐞憿鐠夌挓鐢佺暞鐨傜殰鐨炵殯鐨︼鐫嗗姱鐮＄纭ょ『绀帮铷欙绂旓绂涚珣绔э绔疄铷濈祱绲滅斗缍犵窎绻掔絿缇★鑼佽崲鑽胯弴鑿惰憟钂磋晸钑欙拷钑钖帮铷¤爣瑁佃⊕瑷疯┕瑾ц璜燂á璜惰瓝璀胯嘲璩磋磼璧讹ǎ杌忥à铷ラ仹閮烇é閯曢劎閲氶嚄閲為嚟閲嚖閲ラ垎閳愰垔閳洪墍閳奸墡閴欓墤閳归墽閵ч壏閴搁嫥閶楅嫏閶愶ě閶曢嫚閶撻尌閷￠嫽铷ㄩ尀閶块対閷傞嵃閸楅帳閺嗛彏閺搁惐閼呴憟闁掞铷╅殱闅湷闇婚潈闈嶉潖闈戦潟椤楅ˉ铷ǐ椁эì棣為楂欓珳榄甸楫忛楫婚皜榈伴但铷笝榛戯拷锟解叞鈪扁叢鈪斥叴鈪碘叾鈪封吀鈪癸竣锟わ紘锛傦拷锟斤拷".split("");
	for(a = 0; a != t[238].length; ++a)
		if(t[238][a].charCodeAt(0) !== 65533) { r[t[238][a]] = 60928 + a;
			e[60928 + a] = t[238][a] }
	t[250] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鈪扳叡鈪测叧鈪粹叺鈪垛叿鈪糕吂鈪犫叀鈪⑩叄鈪も叆鈪︹収鈪ㄢ叐锟郡锛囷紓銏扁剸鈩♀埖绾婅閸堥妶钃滀繅鐐绘槺妫堥嫻鏇诲絽涓ㄤ弧浠间紑浼冧脊浣栦緬渚婁練渚斾繊鍋€鍊靠鍊炲亞鍋板亗鍌斿兇鍍樺厞锟藉叅鍐濆喚鍑垥鍔滃姦鍕€鍕涘寑鍖囧尋鍗插帗鍘插彎铷庡挏鍜婂挬鍝垮枂鍧欏潵鍨焾鍩囷◤铷愬澧插濂撳濂濆ィ濡ゅ瀛栧瘈鐢瘶瀵盀宀﹀埠宄靛揣宓擄☉宓傚淡宥稿豆宸愬肌寮村涧寰峰繛鎭濇倕鎮婃優鎯曟劆鎯叉剳鎰锋劙鎲樻垞鎶︽彽鎽犳挐鎿庢晭鏄€鏄曟樆鏄夋槷鏄炴槫鏅ユ櫁鏅欙⊕鏅虫殭鏆犳毑鏆挎浐鏈庯ぉ鏉︽灮妗掓焵鏍佹妫忥〒妤〝姒樻Б妯版┇姗嗘┏姗炬娅ゆ瘱姘挎睖娌嗘悲娉氭磩娑囨弹锟斤拷锟�".split("");
	for(a = 0; a != t[250].length; ++a)
		if(t[250][a].charCodeAt(0) !== 65533) { r[t[250][a]] = 64e3 + a;
			e[64e3 + a] = t[250][a] }
	t[251] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷娑栨冬娣忔犯娣叉芳娓规箿娓ф讣婧挎緢婢垫康鐎呯€囩€ㄧ倕鐐剰鐒勭厹鐓嗙厙铷曠噥鐕剧姳鐘剧尋铷栫嵎鐜界弶鐝栫彛鐝掔悋鐝电惁鐞惄鐞憿鐠夌挓鐢佺暞鐨傜殰鐨炵殯鐨︼鐫嗗姱鐮＄纭ょ『绀帮铷欙拷铷氱铷涚绔戠铷滅绠烇绲堢禍缍风稜绶栫箳缃囩尽铷炶寔鑽㈣嵖鑿囪彾钁堣挻钑撹暀钑钖帮铷¤爣瑁佃⊕瑷疯┕瑾ц璜燂á璜惰瓝璀胯嘲璩磋磼璧讹ǎ杌忥à铷ラ仹閮烇é閯曢劎閲氶嚄閲為嚟閲嚖閲ラ垎閳愰垔閳洪墍閳奸墡閴欓墤閳归墽閵ч壏閴搁嫥閶楅嫏閶愶ě閶曢嫚閶撻尌閷￠嫽铷ㄩ尀閶块対閷傞嵃閸楅帳閺嗛彏閺搁惐閼呴憟闁掞铷╅殱闅湷闇婚潈闈嶉潖闈戦潟椤楅ˉ铷ǐ椁эì棣為楂欙拷锟斤拷".split("");
	for(a = 0; a != t[251].length; ++a)
		if(t[251][a].charCodeAt(0) !== 65533) { r[t[251][a]] = 64256 + a;
			e[64256 + a] = t[251][a] }
	t[252] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷楂滈榄查畯楫遍榘€榈伴但铷笝榛戯拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[252].length; ++a)
		if(t[252][a].charCodeAt(0) !== 65533) { r[t[252][a]] = 64512 + a;
			e[64512 + a] = t[252][a] }
	return { enc: r, dec: e }
}();
cptable[936] = function() {
	var e = [],
		r = {},
		t = [],
		a;
	t[0] = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~鈧拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[0].length; ++a)
		if(t[0][a].charCodeAt(0) !== 65533) { r[t[0][a]] = 0 + a;
			e[0 + a] = t[0][a] }
	t[129] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷涓備竸涓呬竼涓忎笒涓椾笩涓犱浮涓ｄ甫涓╀府涓副涓充傅涓蜂讣涔€涔佷箓涔勪箚涔婁箲涔曚箺涔氫箾涔梗涔や攻涔т龚涔公涔弓涔汞涔蹭勾涔典苟涔蜂垢涔逛购涔讳辜涔戒箍浜€浜佷簜浜冧簞浜呬簢浜婏拷浜愪簴浜椾簷浜滀簼浜炰海浜函浜颁罕浜翠憾浜蜂焊浜逛杭浜戒壕浠堜粚浠忎粣浠掍粴浠涗粶浠犱虎浠︿户浠╀画浠化浠变淮浠镐还浠轰患浠句紑浼備純浼勪紖浼嗕紘浼堜紜浼屼紥浼撲紨浼曚紪浼滀紳浼′迹浼ㄤ缉浼辑浼急浼充嫉浼蜂脊浼讳季浼夸絸浣佷絺浣勪絽浣囦綀浣変綂浣嬩綄浣掍綌浣栦健浣溅浣ㄤ姜浣江浣奖浣蹭降浣蜂礁浣逛胶浣戒線渚佷緜渚呬締渚囦緤渚屼編渚愪緬渚撲緯渚栦緲渚欎練渚滀緸渚熶尽渚拷".split("");
	for(a = 0; a != t[129].length; ++a)
		if(t[129][a].charCodeAt(0) !== 65533) { r[t[129][a]] = 33024 + a;
			e[33024 + a] = t[129][a] }
	t[130] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷渚や精渚景渚变静渚充敬渚朵痉渚镐竟渚轰净渚间窘渚句縺淇佷總淇嗕繃淇堜繅淇嬩繉淇嶄繏淇撲繑淇曚繓淇欎繘淇犱竣淇や骏淇т揩淇堪淇蹭看淇典慷淇蜂抗淇讳考淇戒靠鍊€鍊佸€傚€冨€勫€呭€嗗€囧€堝€夊€婏拷鍊嬪€庡€愬€戝€撳€曞€栧€楀€涘€濆€炲€犲€㈠€ｅ€ゅ€у€€€板€卞€插€冲€村€靛€跺€峰€稿€瑰€诲€藉€垮亐鍋佸亗鍋勫亝鍋嗗亯鍋婂亱鍋嶅亹鍋戝亽鍋撳仈鍋栧仐鍋樺仚鍋涘仢鍋炲仧鍋犲仭鍋㈠仯鍋ゅ仸鍋у仺鍋╁仾鍋伃鍋伅鍋板伇鍋插伋鍋村伒鍋稿伖鍋哄伡鍋藉倎鍌傚們鍌勫倖鍌囧倝鍌婂倠鍌屽値鍌忓倫鍌戝倰鍌撳倲鍌曞倴鍌楀倶鍌欏倸鍌涘倻鍌濆倿鍌熷偁鍌″偄鍌ゅ偊鍌偒鍌偖鍌偘鍌卞偝鍌村偟鍌跺偡鍌稿偣鍌硷拷".split("");
	for(a = 0; a != t[130].length; ++a)
		if(t[130][a].charCodeAt(0) !== 65533) { r[t[130][a]] = 33280 + a;
			e[33280 + a] = t[130][a] }
	t[131] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍌藉偩鍌垮儉鍍佸儌鍍冨儎鍍呭儐鍍囧儓鍍夊儕鍍嬪儗鍍嶅儙鍍愬儜鍍掑儞鍍斿儠鍍楀儤鍍欏儧鍍滃儩鍍炲儫鍍犲儭鍍㈠儯鍍ゅ儱鍍ㄥ儵鍍儷鍍儼鍍卞儾鍍村兌鍍峰兏鍍瑰兒鍍煎兘鍍惧兛鍎€鍎佸剛鍎冨剟鍎呭剤锟藉剦鍎婂剬鍎嶅剮鍎忓剱鍎戝創鍎斿剷鍎栧剹鍎樺剻鍎氬剾鍎滃劃鍎炲劅鍎犲劉鍎ｅ劋鍎ュ劍鍎у劏鍎╁劒鍎劕鍎劗鍎劙鍎卞劜鍎冲劥鍎靛劧鍎峰劯鍎瑰労鍎诲劶鍎藉劸鍏傚厙鍏婂厡鍏庡厪鍏愬厭鍏撳厳鍏樺厵鍏涘厺鍏炲厽鍏犲叀鍏ｅ叅鍏﹀収鍏╁叒鍏叢鍏哄吘鍏垮唭鍐勫唵鍐囧唺鍐嬪啂鍐忓啇鍐戝啌鍐斿啒鍐氬啙鍐炲啛鍐″啠鍐﹀啩鍐ㄥ啯鍐啳鍐喆鍐稿喒鍐哄喚鍐垮噥鍑傚噧鍑呭噲鍑婂噸鍑庡噽鍑掑嚀鍑斿嚂鍑栧嚄锟�".split("");
	for(a = 0; a != t[131].length; ++a)
		if(t[131][a].charCodeAt(0) !== 65533) { r[t[131][a]] = 33536 + a;
			e[33536 + a] = t[131][a] }
	t[132] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍑樺嚈鍑氬嚋鍑炲嚐鍑㈠嚕鍑ュ嚘鍑у嚚鍑╁嚜鍑嚠鍑卞嚥鍑村嚪鍑惧垊鍒呭垑鍒嬪垖鍒忓垚鍒撳垟鍒曞垳鍒炲垷鍒″垻鍒ｅ垾鍒﹀埀鍒埇鍒埍鍒插埓鍒靛埣鍒惧墑鍓呭墕鍓囧増鍓夊墜鍓庡墢鍓掑墦鍓曞墬鍓橈拷鍓欏墯鍓涘墲鍓熷墵鍓㈠墸鍓ゅ墻鍓ㄥ壂鍓壄鍓壈鍓卞壋鍓村壍鍓跺壏鍓稿壒鍓哄壔鍓煎壘鍔€鍔冨妱鍔呭妴鍔囧妷鍔婂妺鍔屽妽鍔庡姀鍔戝姃鍔斿姇鍔栧姉鍔樺姍鍔氬姕鍔ゅ姤鍔﹀姧鍔姱鍔板姶鍔靛姸鍔峰姼鍔瑰姾鍔诲娂鍔藉媭鍕佸媯鍕勫媴鍕嗗媹鍕婂媽鍕嶅嫀鍕忓嫅鍕撳嫈鍕曞嫍鍕欏嫐鍕涘嫓鍕濆嫗鍕犲嫛鍕㈠嫞鍕ュ嫤鍕у嫧鍕╁嫪鍕嫭鍕嫯鍕嫳鍕插嫵鍕村嫷鍕跺嫹鍕稿嫽鍕煎嫿鍖佸寕鍖冨寗鍖囧寜鍖婂寢鍖屽寧锟�".split("");
	for(a = 0; a != t[132].length; ++a)
		if(t[132][a].charCodeAt(0) !== 65533) { r[t[132][a]] = 33792 + a;
			e[33792 + a] = t[132][a] }
	t[133] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍖戝寬鍖撳寯鍖樺寷鍖滃尀鍖熷將鍖ゅ尌鍖у尐鍖╁尗鍖尛鍖尠鍖卞尣鍖冲尨鍖靛尪鍖峰尭鍖煎尳鍗€鍗傚崉鍗嗗崑鍗屽崓鍗愬崝鍗樺崣鍗涘崫鍗ュ崹鍗崿鍗嵅鍗跺嵐鍗诲嵓鍗藉嵕鍘€鍘佸巸鍘囧巿鍘婂帋鍘忥拷鍘愬帒鍘掑帗鍘斿帠鍘楀帣鍘涘帨鍘炲帬鍘″帳鍘у帾鍘幀鍘幆鍘板幈鍘插幊鍘村幍鍘峰幐鍘瑰幒鍘煎幗鍘惧弨鍙冨弰鍙呭弳鍙囧弾鍙忓彁鍙掑彄鍙曞彋鍙滃彎鍙炲彙鍙㈠彠鍙村徍鍙惧徔鍚€鍚傚悈鍚囧悑鍚斿悩鍚欏悮鍚滃悽鍚ゅ惀鍚惏鍚冲惗鍚峰惡鍚藉惪鍛佸憘鍛勫憛鍛囧憠鍛屽憤鍛庡憦鍛戝憵鍛濆憺鍛熷憼鍛″懀鍛ュ懅鍛╁應鍛懍鍛懏鍛懓鍛村懝鍛哄懢鍛垮拋鍜冨拝鍜囧拡鍜夊拪鍜嶅拺鍜撳挆鍜樺挏鍜炲挓鍜犲挕锟�".split("");
	for(a = 0; a != t[133].length; ++a)
		if(t[133][a].charCodeAt(0) !== 65533) { r[t[133][a]] = 34048 + a;
			e[34048 + a] = t[133][a] }
	t[134] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍜㈠挜鍜挵鍜插挼鍜跺挿鍜瑰捄鍜煎捑鍝冨搮鍝婂搵鍝栧摌鍝涘摖鍝″摙鍝ｅ摛鍝摤鍝摪鍝卞摯鍝靛摱鍝峰摳鍝瑰摶鍝惧攢鍞傚攦鍞勫攨鍞堝攰鍞嬪攲鍞嶅攷鍞掑敁鍞曞敄鍞楀敇鍞欏敋鍞滃敐鍞炲敓鍞″敟鍞︼拷鍞ㄥ敥鍞敪鍞插敶鍞靛敹鍞稿敼鍞哄敾鍞藉晙鍟傚晠鍟囧晥鍟嬪晫鍟嶅晭鍟忓晳鍟掑晸鍟斿晽鍟樺暀鍟氬暃鍟濆暈鍟熷暊鍟㈠暎鍟ㄥ暕鍟暞鍟板暠鍟插暢鍟村暪鍟哄暯鍟垮枀鍠嗗枌鍠嶅枎鍠愬枓鍠撳枙鍠栧枟鍠氬枦鍠炲枲鍠″枹鍠ｅ枻鍠ュ枽鍠ㄥ柀鍠柅鍠柇鍠柉鍠板柌鍠村柖鍠稿柡鍠煎柨鍡€鍡佸梻鍡冨梿鍡囧棃鍡婂棆鍡庡棌鍡愬棔鍡楀棙鍡欏棜鍡涘棡鍡犲棦鍡у棭鍡棶鍡板棻鍡村椂鍡稿椆鍡哄椈鍡煎椏鍢傚槂鍢勫槄锟�".split("");
	for(a = 0; a != t[134].length; ++a)
		if(t[134][a].charCodeAt(0) !== 65533) { r[t[134][a]] = 34304 + a;
			e[34304 + a] = t[134][a] }
	t[135] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍢嗗槆鍢婂構鍢嶅槓鍢戝槖鍢撳様鍢曞槚鍢楀槞鍢氬槣鍢濆槧鍢″槩鍢ュ槮鍢ㄥ槱鍢槴鍢槸鍢板槼鍢靛樂鍢稿樅鍢煎樈鍢惧檧鍣佸檪鍣冨檮鍣呭檰鍣囧檲鍣夊檴鍣嬪檹鍣愬檻鍣掑檽鍣曞櫀鍣氬櫅鍣濆櫈鍣熷櫊鍣★拷鍣ｅ櫏鍣﹀櫑鍣櫘鍣櫚鍣插櫝鍣村櫟鍣峰櫢鍣瑰櫤鍣藉櫨鍣垮殌鍤佸殏鍤冨殑鍤囧殘鍤夊殜鍤嬪殞鍤嶅殣鍤戝殥鍤斿殨鍤栧殫鍤樺殭鍤氬殯鍤滃殱鍤炲殶鍤犲殹鍤㈠殼鍤ュ殾鍤у毃鍤╁毆鍤毈鍤毊鍤板毐鍤插毘鍤村毜鍤跺毟鍤瑰毢鍤诲毥鍤惧毧鍥€鍥佸泜鍥冨泟鍥呭泦鍥囧泩鍥夊泲鍥屽泹鍥庡洀鍥愬洃鍥掑洆鍥曞洊鍥樺洐鍥滃洠鍥ュ洣鍥у洦鍥╁洩鍥洰鍥洸鍥冲浂鍥峰浉鍥诲浖鍦€鍦佸渹鍦呭渿鍦嬪湆鍦嶅湈鍦忓湊鍦戯拷".split("");
	for(a = 0; a != t[135].length; ++a)
		if(t[135][a].charCodeAt(0) !== 65533) { r[t[135][a]] = 34560 + a;
			e[34560 + a] = t[135][a] }
	t[136] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍦掑湏鍦斿湑鍦栧湕鍦樺湙鍦氬湜鍦濆湠鍦犲湣鍦㈠湦鍦ュ湨鍦у湯鍦卞湶鍦村湹鍦跺湻鍦稿溂鍦藉溈鍧佸潈鍧勫潊鍧嗗潏鍧夊潒鍧掑潛鍧斿潟鍧栧潣鍧欏潰鍧ｅ潵鍧у潿鍧澃鍧卞澆鍧村澋鍧稿澒鍧哄澖鍧惧澘鍨€锟藉瀬鍨囧瀳鍨夊瀶鍨嶅瀻鍨忓瀽鍨戝灁鍨曞灃鍨楀灅鍨欏灇鍨滃灊鍨炲灍鍨ュ灗鍨灛鍨灠鍨卞灣鍨靛灦鍨峰灩鍨哄灮鍨煎灲鍨惧灴鍩€鍩佸焺鍩呭焼鍩囧焾鍩夊煀鍩屽煃鍩愬煈鍩撳煐鍩楀煕鍩滃煘鍩″煝鍩ｅ煡鍩﹀煣鍩ㄥ煩鍩煫鍩煯鍩板煴鍩插煶鍩靛煻鍩峰熁鍩煎熅鍩垮爜鍫冨爠鍫呭爤鍫夊爦鍫屽爭鍫忓爯鍫掑爴鍫斿爾鍫楀牁鍫氬牄鍫滃牆鍫熷牏鍫ｅ牓鍫﹀牕鍫ㄥ牘鍫牞鍫牣鍫牨鍫插牫鍫村牰鍫峰牳鍫瑰牶鍫诲牸鍫斤拷".split("");
	for(a = 0; a != t[136].length; ++a)
		if(t[136][a].charCodeAt(0) !== 65533) { r[t[136][a]] = 34816 + a;
			e[34816 + a] = t[136][a] }
	t[137] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍫惧牽濉€濉佸濉冨濉嗗濉堝濉婂濉庡濉愬濉撳濉栧濉欏濉涘濉濆濉犲　濉㈠。濉ゅˇ濉у〃濉╁—濉‘濉“濉卞〔濉冲〈濉靛《濉峰「濉瑰『濉诲〖濉藉】澧傚澧嗗澧堝澧嬪锟藉澧庡澧愬澧斿澧栧澧樺澧滃澧犲ⅰ澧㈠ⅲ澧ゅⅴ澧﹀ⅶ澧澧澧澧板⒈澧插⒊澧村⒌澧跺⒎澧稿⒐澧哄⒒澧藉⒕澧垮澹傚澹勫澹囧澹夊澹嬪澹嶅澹忓澹掑澹斿澹楀澹欏澹涘澹濆澹熷　澹″＂澹ｅ％澹﹀＇澹ㄥ）澹－澹１澹插４澹靛７澹稿：澹诲＜澹藉＞澹垮澶佸澶呭澶堝澶婂澶屽澶愬澶掑澶楀澶涘澶炲澶″あ澶ｅう澶ㄥが澶板げ澶冲さ澶跺せ锟�".split("");
	for(a = 0; a != t[137].length; ++a)
		if(t[137][a].charCodeAt(0) !== 65533) { r[t[137][a]] = 35072 + a;
			e[35072 + a] = t[137][a] }
	t[138] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷澶藉ぞ澶垮濂冨濂嗗濂屽濂愬濂撳濂涘濂濆濂熷ァ濂ｅイ濂﹀ェ濂ㄥォ濂カ濂キ濂ク濂板ケ濂插サ濂峰ズ濂诲ゼ濂惧タ濡€濡呭濡嬪濡庡濡愬濡斿濡樺濡涘濡濆濡犲Α濡㈠Ζ锟藉Η濡Ν濡板Ρ濡冲Υ濡靛Χ濡峰Ω濡哄濡藉濮€濮佸濮冨濮呭濮堝濮屽濮庡濮曞濮欏濮炲濮犲А濮㈠Г濮﹀Ё濮╁И濮Л濮Н濮板П濮插С濮村У濮跺Х濮稿Ш濮煎Ы濮惧█濞傚▕濞嬪◢濞庡◤濞愬⊕濞斿〞濞栧濞欏濞涘濞炲ā濞㈠à濞﹀ě濞ㄥí濞ì濞ó濞ò濞冲ǖ濞峰ǜ濞瑰ê濞诲ń濞惧濠佸﹤濠冨﹦濠呭﹪濠堝濠屽濠庡濠愬濠掑濠斿〇濠楀濠欏濠滃濠炲濠狅拷".split("");
	for(a = 0; a != t[138].length; ++a)
		if(t[138][a].charCodeAt(0) !== 65533) { r[t[138][a]] = 35328 + a;
			e[35328 + a] = t[138][a] }
	t[139] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷濠″濠ゅ━濠﹀┄濠╁┇濠┉濠┋濠板┍濠插┏濠稿┕濠诲┘濠藉┚濯€濯佸獋濯冨獎濯呭獑濯囧獔濯夊獖濯嬪獙濯嶅獛濯忓獝濯戝獡濯斿獣濯栧獥濯樺獧濯滃獫濯炲獰濯犲濯㈠濯ゅ濯﹀濯ㄥ濯锟藉濯濯板濯村濯峰濯哄濯煎濯垮珋瀚冨珓瀚呭珕瀚囧珗瀚婂珛瀚嶅珟瀚忓珢瀚戝珦瀚曞珬瀚欏珰瀚涘珴瀚炲珶瀚㈠瀚ュ瀚ㄥ瀚瀚瀚板瀚冲瀚靛瀚峰瀚瑰瀚诲瀚藉瀚垮瑎瀣佸瑐瀣冨瑒瀣呭瑔瀣囧瑘瀣婂瑡瀣屽瑣瀣庡瑥瀣愬瑧瀣掑瑩瀣斿瑫瀣樺瑱瀣氬瑳瀣滃瑵瀣炲瑹瀣犲瀣㈠瀣ゅ瀣﹀瀣ㄥ瀣瀣瀣瀣板瀣冲瀣跺瀣瑰瀣诲瀣藉瀣垮瓉瀛傚瓋瀛勫瓍瀛嗗瓏锟�".split("");
	for(a = 0; a != t[139].length; ++a)
		if(t[139][a].charCodeAt(0) !== 65533) { r[t[139][a]] = 35584 + a;
			e[35584 + a] = t[139][a] }
	t[140] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷瀛堝瓑瀛婂瓔瀛屽瓖瀛庡瓘瀛掑瓥瀛炲瓲瀛″瀛ㄥ瀛瀛瀛村瀛峰瀛瑰瀛煎瀛垮畟瀹嗗畩瀹嶅畮瀹愬畱瀹掑當瀹栧疅瀹у瀹╁瀹瀹瀹插瀹哄瀹煎瘈瀵佸瘍瀵堝瘔瀵婂瘚瀵嶅瘞瀵忥拷瀵戝瘮瀵曞瘱瀵楀瘶瀵欏瘹瀵涘瘻瀵犲瀵ｅ瀵у瀵瀵瀵瀵插瀵村瀵跺瀵藉灏€灏傚皟灏呭皣灏堝皨灏屽皪灏庡皭灏掑皳灏楀皺灏涘盀灏熷盃灏″埃灏﹀皑灏╁蔼灏碍灏隘灏板安灏冲暗灏跺胺灞冨眲灞嗗眹灞屽睄灞掑睋灞斿睎灞楀睒灞氬睕灞滃睗灞熷雹灞ゅ抱灞ㄥ暴灞鲍灞杯灞板辈灞冲贝灞靛倍灞峰备灞诲奔灞藉本宀€宀冨矂宀呭矄宀囧矇宀婂矉宀庡矎宀掑矒宀曞矟宀炲矡宀犲病宀ゅ播宀﹀钵宀拷".split("");
	for(a = 0; a != t[140].length; ++a)
		if(t[140][a].charCodeAt(0) !== 65533) { r[t[140][a]] = 35840 + a;
			e[35840 + a] = t[140][a] }
	t[141] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷宀伯宀舶宀插泊宀跺补宀哄不宀煎簿宄€宄傚硟宄呭硢宄囧硤宄夊硦宄屽硩宄庡硰宄愬硲宄撳硵宄曞硸宄楀硺宄氬硾宄滃碀宄炲碂宄犲尝宄ｅ厂宄╁倡宄钞宄潮宄插吵宄村车宄跺撤宄稿彻宄哄臣宄藉尘宄垮磤锟藉磥宕勫磪宕堝磯宕婂磱宕屽磵宕忓磹宕戝磼宕撳磿宕楀礃宕欏礆宕滃礉宕熷礌宕″储宕ｅ触宕ㄥ椽宕船宕窗宕卞床宕冲吹宕跺捶宕稿垂宕哄椿宕煎纯宓€宓佸祩宓冨祫宓呭祮宓堝祲宓嶅祹宓忓祼宓戝祾宓撳禂宓曞禆宓楀禉宓氬禍宓炲禑宓犲怠宓㈠担宓ゅ单宓﹀掸宓ㄥ氮宓诞宓板当宓插党宓靛刀宓峰蹈宓瑰岛宓诲导宓藉稻宓垮秬宥佸秲宥勫秴宥嗗秶宥堝秹宥婂秼宥屽秿宥庡稄宥愬稇宥掑稉宥斿稌宥栧稐宥樺稓宥涘稖宥炲稛宥狅拷".split("");
	for(a = 0; a != t[141].length; ++a)
		if(t[141][a].charCodeAt(0) !== 65533) { r[t[141][a]] = 36096 + a;
			e[36096 + a] = t[141][a] }
	t[142] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷宥″盯宥ｅ钉宥ュ鼎宥у定宥╁丢宥冬宥懂宥栋宥卞恫宥冲洞宥靛抖宥稿豆宥哄痘宥煎督宥惧犊宸€宸佸穫宸冨穭宸嗗穱宸堝穳宸婂穻宸屽穾宸忓窅宸戝窉宸撳窋宸曞窎宸楀窐宸欏窔宸滃窡宸犲罚宸ゅ藩宸翻锟藉钒宸靛范宸稿饭宸哄坊宸煎房甯€甯勫竾甯夊笂甯嬪笉甯庡笒甯撳笚甯炲笩甯犲浮甯㈠福甯ゅ弗甯ㄥ俯甯斧甯腐甯板覆甯冲复甯靛付甯瑰负甯惧缚骞€骞佸箖骞嗗箛骞堝箟骞婂箣骞嶅箮骞忓箰骞戝箳骞撳箹骞楀箻骞欏箽骞滃節骞熷範骞ｅ工骞ュ功骞у龚骞╁躬骞宫骞巩骞拱骞卞沟骞峰构骞惧簛搴傚簝搴呭簣搴夊簩搴嶅簬搴掑簶搴涘簼搴″孩搴ｅ氦搴ㄥ憨搴韩搴寒搴喊搴卞翰搴村汉搴诲杭搴藉嚎寤€寤佸粋寤冨粍寤咃拷".split("");
	for(a = 0; a != t[142].length; ++a)
		if(t[142][a].charCodeAt(0) !== 65533) { r[t[142][a]] = 36352 + a;
			e[36352 + a] = t[142][a] }
	t[143] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷寤嗗粐寤堝粙寤屽粛寤庡粡寤愬粩寤曞粭寤樺粰寤氬粶寤濆粸寤熷粻寤″虎寤ｅ护寤ュ沪寤у哗寤滑寤划寤话寤卞徊寤冲坏寤稿还寤诲患寤藉紖寮嗗紘寮夊紝寮嶅紟寮愬紥寮斿紪寮欏細寮滃紳寮炲肌寮㈠迹寮わ拷寮ㄥ极寮籍寮板疾寮冲即寮靛级寮峰几寮诲冀寮惧伎褰佸絺褰冨絼褰呭絾褰囧綀褰夊綂褰嬪綄褰嶅綆褰忓綉褰斿綑褰氬經褰滃綖褰熷綘褰ｅ渐褰у建褰疆褰讲褰村降褰跺礁褰哄浇褰惧娇寰冨締寰嶅編寰忓緫寰撳緮寰栧練寰涘緷寰炲緹寰犲劲寰ｅ兢寰ュ睛寰у京寰粳寰景寰卞静寰冲敬寰跺靖寰瑰竞寰诲揪寰垮縺蹇佸總蹇囧繄蹇婂繈蹇庡繐蹇斿繒蹇氬繘蹇滃繛蹇熷竣蹇ｅ骏蹇﹀卡蹇╁楷蹇堪蹇插砍蹇村慷蹇峰抗蹇哄考鎬囷拷".split("");
	for(a = 0; a != t[143].length; ++a)
		if(t[143][a].charCodeAt(0) !== 65533) { r[t[143][a]] = 36608 + a;
			e[36608 + a] = t[143][a] }
	t[144] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鎬堟€夋€嬫€屾€愭€戞€撴€楁€樻€氭€炴€熸€㈡€ｆ€ゆ€€€€版€辨€叉€虫€存€舵€锋€告€规€烘€芥€炬亐鎭勬亝鎭嗘亣鎭堟亯鎭婃亴鎭庢亸鎭戞亾鎭旀仏鎭楁仒鎭涙仠鎭炴仧鎭犳仭鎭ユ仸鎭伇鎭叉伌鎭垫伔鎭炬個锟芥倎鎮傛倕鎮嗘倗鎮堟倞鎮嬫値鎮忔倫鎮戞倱鎮曟倵鎮樻倷鎮滄倿鎮℃偄鎮ゆ偉鎮ф偐鎮偖鎮版偝鎮垫偠鎮锋偣鎮烘偨鎮炬偪鎯€鎯佹儌鎯冩儎鎯囨儓鎯夋儗鎯嶆儙鎯忔儛鎯掓儞鎯旀儢鎯楁儥鎯涙優鎯℃儮鎯ｆ儰鎯ユ儶鎯辨儾鎯垫兎鎯告兓鎯兼兘鎯炬兛鎰傛剝鎰勬剠鎰囨剨鎰嬫剬鎰愭剳鎰掓創鎰旀剸鎰楁剺鎰欐剾鎰滄劃鎰炴劇鎰㈡劌鎰ㄦ劑鎰劕鎰劗鎰劙鎰辨劜鎰虫劥鎰垫劧鎰锋劯鎰规労鎰绘劶鎰芥劸鎱€鎱佹厒鎱冩厔鎱呮厗锟�".split("");
	for(a = 0; a != t[144].length; ++a)
		if(t[144][a].charCodeAt(0) !== 65533) { r[t[144][a]] = 36864 + a;
			e[36864 + a] = t[144][a] }
	t[145] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鎱囨厜鎱嬫厤鎱忔厫鎱掓厯鎱旀厲鎱楁厴鎱欐厷鎱涙厹鎱炴厽鎱犳叀鎱ｆ叅鎱ユ叇鎱╂叒鎱叕鎱叜鎱叡鎱叉叧鎱存叾鎱告吂鎱烘吇鎱兼吔鎱炬吙鎲€鎲佹唫鎲冩唲鎲呮唵鎲囨唸鎲夋唺鎲屾啀鎲忔啇鎲戞啋鎲撴啎锟芥問鎲楁啒鎲欐啔鎲涙啘鎲炴啛鎲犳啞鎲㈡啠鎲ゆ啣鎲︽啰鎲啳鎲啹鎲版啽鎲叉喅鎲存喌鎲舵喐鎲规喓鎲绘喖鎲芥喛鎳€鎳佹噧鎳勬噮鎳嗘噰鎳夋噷鎳嶆噹鎳忔噽鎳撴嚂鎳栨嚄鎳樻嚈鎳氭嚊鎳滄嚌鎳炴嚐鎳犳嚒鎳㈡嚕鎳ゆ嚗鎳ф嚚鎳╂嚜鎳嚞鎳嚠鎳嚢鎳辨嚥鎳虫嚧鎳舵嚪鎳告嚬鎳烘嚮鎳兼嚱鎳炬垁鎴佹垈鎴冩垊鎴呮垏鎴夋垞鎴旀垯鎴滄垵鎴炴垹鎴ｆ垿鎴ф埁鎴╂埆鎴埊鎴版埍鎴叉埖鎴舵埜鎴规埡鎴绘埣鎵傛墑鎵呮墕鎵婏拷".split("");
	for(a = 0; a != t[145].length; ++a)
		if(t[145][a].charCodeAt(0) !== 65533) { r[t[145][a]] = 37120 + a;
			e[37120 + a] = t[145][a] }
	t[146] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鎵忔墣鎵曟墫鎵楁墮鎵氭墱鎵濇墳鎵熸墵鎵℃墷鎵ゆ墺鎵ㄦ壉鎵叉壌鎵垫壏鎵告壓鎵绘壗鎶佹妭鎶冩妳鎶嗘妵鎶堟妺鎶屾妽鎶庢姀鎶愭姅鎶欐姕鎶濇姙鎶ｆ姦鎶ф姪鎶姯鎶姱鎶版姴鎶虫姶鎶舵姺鎶告姾鎶炬媭鎷侊拷鎷冩媼鎷忔嫅鎷曟嫕鎷炴嫚鎷℃嫟鎷嫬鎷版嫴鎷垫嫺鎷规嫼鎷绘寑鎸冩寗鎸呮寙鎸婃寢鎸屾實鎸忔寪鎸掓寭鎸旀寱鎸楁寴鎸欐寽鎸︽導鎸╂尙鎸尞鎸版尡鎸虫尨鎸垫尪鎸锋尭鎸绘尲鎸炬尶鎹€鎹佹崉鎹囨崍鎹婃崙鎹掓崜鎹旀崠鎹楁崢鎹欐崥鎹涙崪鎹濇崰鎹ゆ崶鎹︽崹鎹崼鎹嵂鎹版嵅鎹虫嵈鎹垫嵏鎹规嵓鎹芥嵕鎹挎巵鎺冩巹鎺呮巻鎺嬫帊鎺戞帗鎺旀帟鎺楁帣鎺氭帥鎺滄帩鎺炴師鎺℃帳鎺︽帿鎺幈鎺叉幍鎺舵幑鎺绘幗鎺挎弨锟�".split("");
	for(a = 0; a != t[146].length; ++a)
		if(t[146][a].charCodeAt(0) !== 65533) { r[t[146][a]] = 37376 + a;
			e[37376 + a] = t[146][a] }
	t[147] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鎻佹弬鎻冩弲鎻囨張鎻婃弸鎻屾彂鎻撴彅鎻曟彈鎻樻彊鎻氭彌鎻滄彎鎻熸彚鎻ゆ彞鎻︽彠鎻ㄦ彨鎻彯鎻彴鎻辨彸鎻垫彿鎻规徍鎻绘徏鎻炬悆鎼勬悊鎼囨悎鎼夋悐鎼嶆悗鎼戞悞鎼曟悥鎼楁悩鎼欐悮鎼濇悷鎼㈡悾鎼わ拷鎼ユ惂鎼ㄦ惄鎼惍鎼惏鎼辨惒鎼虫惖鎼舵惙鎼告惞鎼绘惣鎼炬憖鎽傛憙鎽夋憢鎽屾憤鎽庢憦鎽愭憫鎽撴憰鎽栨憲鎽欐憵鎽涙憸鎽濇憻鎽犳憽鎽㈡懀鎽ゆ懃鎽︽懆鎽懌鎽懏鎽懓鎽辨懖鎽虫懘鎽垫懚鎽锋懟鎽兼懡鎽炬懣鎾€鎾佹拑鎾嗘拡鎾夋拪鎾嬫拰鎾嶆拵鎾忔拹鎾撴挃鎾楁挊鎾氭挍鎾滄挐鎾熸挔鎾℃挗鎾ｆ挜鎾︽挧鎾ㄦ挭鎾挴鎾辨挷鎾虫挻鎾舵捁鎾绘捊鎾炬捒鎿佹搩鎿勬搯鎿囨搱鎿夋搳鎿嬫搶鎿忔搼鎿撴摂鎿曟摉鎿欐摎锟�".split("");
	for(a = 0; a != t[147].length; ++a)
		if(t[147][a].charCodeAt(0) !== 65533) { r[t[147][a]] = 37632 + a;
			e[37632 + a] = t[147][a] }
	t[148] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鎿涙摐鎿濇摕鎿犳摗鎿ｆ摜鎿ф摠鎿╂摢鎿摤鎿摦鎿摪鎿辨摬鎿虫摯鎿垫摱鎿锋摳鎿规摵鎿绘摷鎿芥摼鎿挎攣鏀傛攦鏀勬攨鏀嗘攪鏀堟攰鏀嬫攲鏀嶆攷鏀忔攼鏀戞敁鏀旀敃鏀栨敆鏀欐敋鏀涙敎鏀濇敒鏀熸敔鏀★拷鏀㈡敚鏀ゆ敠鏀ф敤鏀╂敧鏀敪鏀版敱鏀叉敵鏀锋敽鏀兼斀鏁€鏁佹晜鏁冩晞鏁嗘晣鏁婃晪鏁嶆晭鏁愭晵鏁撴晹鏁楁晿鏁氭暅鏁熸暊鏁℃暏鏁ユ暓鏁ㄦ暕鏁暛鏁暞鏁辨暢鏁垫暥鏁告暪鏁烘暬鏁兼暯鏁炬暱鏂€鏂佹杺鏂冩杽鏂呮枂鏂堟枆鏂婃枍鏂庢枏鏂掓枖鏂曟枛鏂樻枤鏂濇枮鏂犳枹鏂ｆ枽鏂ㄦ柂鏂柈鏂辨柌鏂虫柎鏂垫柖鏂锋柛鏂烘柣鏂炬柨鏃€鏃傛棁鏃堟棄鏃婃棈鏃愭棏鏃撴棓鏃曟棙鏃欐棜鏃涙棞鏃濇棡鏃熸棥鏃ｆ棨鏃棲锟�".split("");
	for(a = 0; a != t[148].length; ++a)
		if(t[148][a].charCodeAt(0) !== 65533) { r[t[148][a]] = 37888 + a;
			e[37888 + a] = t[148][a] }
	t[149] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鏃叉棾鏃存椀鏃告椆鏃绘椉鏃芥椌鏃挎榿鏄勬槄鏄囨槇鏄夋構鏄嶆槓鏄戞槖鏄栨槜鏄樻槡鏄涙槣鏄炴槨鏄㈡槪鏄ゆ槮鏄╂槳鏄槵鏄槹鏄叉槼鏄锋樃鏄规樅鏄绘樈鏄挎檧鏅傛檮鏅呮檰鏅囨檲鏅夋檴鏅嶆檸鏅愭檻鏅橈拷鏅欐櫅鏅滄櫇鏅炴櫊鏅㈡櫍鏅ユ櫑鏅╂櫔鏅櫖鏅櫛鏅叉櫝鏅垫櫢鏅规櫥鏅兼櫧鏅挎殌鏆佹殐鏆呮殕鏆堟殙鏆婃殝鏆嶆殠鏆忔殣鏆掓殦鏆旀殨鏆樻殭鏆氭殯鏆滄殲鏆熸殸鏆℃殺鏆ｆ殼鏆ユ殾鏆╂毆鏆毈鏆毌鏆版毐鏆叉毘鏆垫毝鏆锋毟鏆烘毣鏆兼毥鏆挎泙鏇佹泜鏇冩泟鏇呮泦鏇囨泩鏇夋泭鏇嬫泴鏇嶆泿鏇忔洂鏇戞洅鏇撴洈鏇曟洊鏇楁洏鏇氭洖鏇熸洜鏇℃洟鏇ｆ洡鏇ユ洤鏇ㄦ洩鏇洭鏇洰鏇洷鏇垫浂鏇告浐鏇绘浗鏈佹渹鏈冿拷".split("");
	for(a = 0; a != t[149].length; ++a)
		if(t[149][a].charCodeAt(0) !== 65533) { r[t[149][a]] = 38144 + a;
			e[38144 + a] = t[149][a] }
	t[150] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鏈勬渽鏈嗘渿鏈屾湈鏈忔湋鏈掓湏鏈栨湗鏈欐湚鏈滄湠鏈犳湣鏈㈡湥鏈ゆ湧鏈ф湬鏈湴鏈叉湷鏈舵湻鏈告湽鏈绘溂鏈炬溈鏉佹潉鏉呮潎鏉婃潒鏉嶆潚鏉旀潟鏉楁潣鏉欐潥鏉涙潩鏉㈡潱鏉ゆ潶鏉ф潾鏉澁鏉辨澊鏉讹拷鏉告澒鏉烘澔鏉芥瀫鏋傛瀮鏋呮瀱鏋堟瀶鏋屾瀺鏋庢瀼鏋戞瀿鏋撴灁鏋栨灆鏋涙灍鏋犳灐鏋ゆ灕鏋╂灛鏋灡鏋叉灤鏋规灪鏋绘灱鏋芥灳鏋挎焵鏌傛焻鏌嗘焽鏌堟焿鏌婃煁鏌屾煃鏌庢煏鏌栨煑鏌涙煙鏌℃煟鏌ゆ煢鏌ф煥鏌煫鏌煯鏌叉煹鏌舵煼鏌告煿鏌烘熁鏌兼熅鏍佹爞鏍冩爠鏍嗘爫鏍愭爳鏍旀爼鏍樻牂鏍氭牄鏍滄牉鏍熸牋鏍㈡牐鏍ゆ牓鏍︽牕鏍ㄦ牜鏍牠鏍牤鏍版牨鏍存牭鏍舵牶鏍绘牽妗囨妗嶆妗掓妗楁妗欐妗涳拷".split("");
	for(a = 0; a != t[150].length; ++a)
		if(t[150][a].charCodeAt(0) !== 65533) { r[t[150][a]] = 38400 + a;
			e[38400 + a] = t[150][a] }
	t[151] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷妗滄妗炴妗‖妗‘妗“妗辨〔妗虫〉妗告」妗烘』妗兼〗妗炬】姊€姊傛姊囨姊夋姊嬫姊嶆姊愭姊掓姊曟姊樻姊氭姊滄姊炴姊犳ⅰ姊ｆⅳ姊ユⅸ姊姊姊辨⒉姊存⒍姊锋⒏锟芥⒐姊烘⒒姊兼⒔姊炬⒖妫佹妫勬妫嗘妫堟妫屾妫忔妫戞妫旀妫楁妫涙妫濇妫熸！妫㈡￥妫ユ＆妫ф（妫╂＊妫，妫／妫叉３妫存６妫锋８妫绘＝妫炬？妞€妞傛妞勬妞囨妞夋妞屾妞戞妞旀妞栨妞樻妞氭妞滄妞炴ぁ妞㈡ぃ妞ユう妞фえ妞╂お妞が妞く妞辨げ妞虫さ妞舵し妞告ず妞绘ぜ妞炬妤佹妤勬妤嗘妤堟妤婃妤屾妤庢妤愭妤掓妤曟妤樻妤涙妤燂拷".split("");
	for(a = 0; a != t[151].length; ++a)
		if(t[151][a].charCodeAt(0) !== 65533) { r[t[151][a]] = 38656 + a;
			e[38656 + a] = t[151][a] }
	t[152] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷妤℃ア妤ゆゥ妤фエ妤╂オ妤キ妤グ妤叉コ妤存サ妤舵ズ妤绘ソ妤炬タ姒佹姒呮姒嬫姒庢姒愭姒掓姒栨姒欐姒濇姒熸姒℃Β姒ｆΔ姒ユΖ姒╂Κ姒Ξ姒Π姒叉Τ姒垫Χ姒告姒烘姒斤拷姒炬妲€妲傛妲勬妲嗘妲堟妲嬫妲忔妲掓妲曟妲楁妲欐妲滄妲炴А妲㈡В妲ゆД妲︽Ё妲ㄦЗ妲Й妲М妲О妲辨С妲存У妲舵Х妲告Ч妲烘Щ妲兼Ь妯€妯佹▊妯冩▌妯呮▎妯囨▓妯夋▼妯屾◢妯庢◤妯愭☉妯掓〒妯旀〞妯栨妯氭妯滄妯炴妯㈡ǎ妯ゆē妯︽ě妯╂ǐ妯ō妯ò妯叉ǔ妯存ǘ妯锋ǜ妯规ê妯绘妯挎﹢姗佹﹤姗冩﹨姗嗘﹫姗夋姗嬫姗嶆姗忔姗掓姗旀姗栨姗氾拷".split("");
	for(a = 0; a != t[152].length; ++a)
		if(t[152][a].charCodeAt(0) !== 65533) { r[t[152][a]] = 38912 + a;
			e[38912 + a] = t[152][a] }
	t[153] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷姗滄姗炴姗犳姗ｆ─姗︽┃姗ㄦ┅姗┇姗┉姗┋姗版┎姗虫┐姗垫┒姗锋└姗烘┗姗芥┚姗挎獊妾傛獌妾呮獑妾囨獔妾夋獖妾嬫獙妾嶆獜妾掓獡妾旀獣妾栨獦妾欐獨妾涙獪妾濇獮妾熸妾㈡妾ゆ妾︼拷妾ф妾妾妾版妾叉妾存妾舵妾告妾烘妾兼妾炬娅€娅佹珎娅冩珓娅呮珕娅囨珗娅夋珚娅嬫珜娅嶆珟娅忔珢娅戞珤娅撴珨娅曟珫娅楁珮娅欐珰娅涙珳娅濇珵娅熸珷娅℃娅ｆ娅ユ娅ф娅╂娅娅娅娅辨娅虫娅垫娅锋娅规娅绘娅芥娅挎瑎娆佹瑐娆冩瑒娆呮瑔娆囨瑘娆夋瑠娆嬫瑢娆嶆瑤娆忔瑦娆戞瑨娆撴瑪娆曟瑬娆楁瑯娆欐瑲娆涙瑴娆濇瑸娆熸娆︽娆╂娆娆锟�".split("");
	for(a = 0; a != t[153].length; ++a)
		if(t[153][a].charCodeAt(0) !== 65533) { r[t[153][a]] = 39168 + a;
			e[39168 + a] = t[153][a] }
	t[154] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷娆娆辨娆存娆舵娆绘娆芥姝€姝佹瓊姝勬瓍姝堟瓓姝嬫瓖姝庢瓘姝愭瓚姝掓瓝姝旀瓡姝栨瓧姝樻瓪姝涙瓬姝濇瓰姝熸瓲姝℃姝╂姝姝姝版姝叉姝存姝舵姝告姝芥姝挎畝娈呮畧锟芥畬娈庢畯娈愭畱娈旀畷娈楁畼娈欐疁娈濇疄娈熸疇娈㈡娈ゆ娈︽娈ㄦ娈娈娈娈辨娈舵娈规娈绘娈芥姣€姣冩瘎姣嗘瘒姣堟瘔姣婃瘜姣庢瘣姣戞瘶姣氭瘻姣濇癁姣熸癄姣㈡姣ゆ姣︽姣ㄦ姣姣姣辨姣存姣锋姣烘姣兼姣挎皜姘佹皞姘冩皠姘堟皦姘婃皨姘屾皫姘掓皸姘滄皾姘炴盃姘ｆ哎姘艾姘氨姘虫岸姘锋肮姘烘盎姘兼熬姘挎眱姹勬眳姹堟眿姹屾睄姹庢睆姹戞睊姹撴睎姹橈拷".split("");
	for(a = 0; a != t[154].length; ++a)
		if(t[154][a].charCodeAt(0) !== 65533) { r[t[154][a]] = 39424 + a;
			e[39424 + a] = t[154][a] }
	t[155] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷姹欐睔姹㈡保姹ユ宝姹ф鲍姹杯姹悲姹辨背姹垫狈姹告焙姹绘奔姹挎瞼娌勬矅娌婃矉娌嶆矌娌戞矑娌曟矕娌楁矘娌氭矞娌濇矠娌犳并娌ㄦ铂娌舶娌存驳娌舵卜娌烘硛娉佹硞娉冩硢娉囨硤娉嬫硩娉庢硰娉戞硳娉橈拷娉欐硽娉滄碀娉熸长娉︽厂娉╂超娉巢娉存彻娉挎磤娲傛磧娲呮磫娲堟磯娲婃磵娲忔磹娲戞磽娲旀磿娲栨礃娲滄礉娲熸礌娲℃储娲ｆ搐娲︽川娲╂船娲疮娲版创娲舵捶娲告春娲挎祤娴傛祫娴夋祵娴愭禃娴栨禇娴樻禌娴濇禑娴℃耽娴ゆ单娴ф胆娴惮娴蛋娴辨挡娴虫档娴舵倒娴烘祷娴芥稻娴挎秬娑佹秲娑勬秵娑囨秺娑嬫秿娑忔稅娑掓稏娑楁稑娑欐稓娑滄盯娑ユ冬娑栋娑辨冻娑存抖娑锋豆娑烘痘娑兼督娑炬穪娣傛穬娣堟穳娣婏拷".split("");
	for(a = 0; a != t[155].length; ++a)
		if(t[155][a].charCodeAt(0) !== 65533) { r[t[155][a]] = 39680 + a;
			e[39680 + a] = t[155][a] }
	t[156] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷娣嶆穾娣忔窅娣掓窊娣旀窌娣楁窔娣涙窚娣熸发娣ｆ伐娣ф法娣╂藩娣矾娣版凡娣存返娣舵犯娣烘方娣炬房娓€娓佹競娓冩竸娓嗘竾娓堟笁娓嬫笍娓掓笓娓曟笜娓欐笡娓滄笧娓熸涪娓︽抚娓ㄦ釜娓府娓版副娓虫傅锟芥付娓锋腹娓绘讣娓芥妇娓挎箑婀佹箓婀呮箚婀囨箞婀夋箠婀嬫箤婀忔箰婀戞箳婀曟箺婀欐箽婀滄節婀炴範婀℃耿婀ｆ工婀ユ功婀ф龚婀╂躬婀弓婀拱婀辨共婀虫勾婀垫苟婀锋垢婀规购婀绘辜婀芥簚婧佹簜婧勬簢婧堟簥婧嬫簩婧嶆簬婧戞簰婧撴簲婧曟簴婧楁簷婧氭簺婧濇簽婧犳骸婧ｆ氦婧︽酣婧╂韩婧涵婧喊婧虫旱婧告汗婧兼壕婧挎粈婊冩粍婊呮粏婊堟粔婊婃粚婊嶆粠婊愭粧婊栨粯婊欐粵婊滄粷婊ｆ户婊猾婊画婊化锟�".split("");
	for(a = 0; a != t[156].length; ++a)
		if(t[156][a].charCodeAt(0) !== 65533) { r[t[156][a]] = 39936 + a;
			e[39936 + a] = t[156][a] }
	t[157] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷婊版槐婊叉怀婊垫欢婊锋桓婊烘换婊兼唤婊炬豢婕€婕佹純婕勬紖婕囨紙婕婃紜婕屾紞婕庢紣婕戞紥婕栨紬婕樻紮婕氭紱婕滄紳婕炴紵婕℃饥婕ｆ讥婕︽姬婕ㄦ棘婕及婕叉即婕垫挤婕告脊婕烘蓟婕兼冀婕挎絸娼佹絺锟芥絻娼勬絽娼堟綁娼婃綄娼庢綇娼愭綉娼掓綋娼旀綍娼栨綏娼欐綒娼涙綕娼熸綘娼℃剑娼ゆ渐娼ф建娼╂姜娼浆娼桨娼辨匠娼垫蕉娼锋焦娼绘浇娼炬娇婢€婢佹緜婢冩緟婢嗘緡婢婃緥婢忔緪婢戞緬婢撴緮婢曟緰婢楁緲婢欐練婢涙緷婢炴緹婢犳劲婢ｆ兢婢ユ睛婢ㄦ京婢精婢经婢警婢版颈婢叉敬婢垫痉婢告竞婢绘炯婢芥揪婢挎縼婵冩縿婵呮繂婵囨繄婵婃繈婵屾繊婵庢繌婵愭繐婵旀繒婵栨織婵樻繖婵氭繘婵滄繚婵熸竣婵ｆ郡婵ワ拷".split("");
	for(a = 0; a != t[157].length; ++a)
		if(t[157][a].charCodeAt(0) !== 65533) { r[t[157][a]] = 40192 + a;
			e[40192 + a] = t[157][a] }
	t[158] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷婵︽咖婵ㄦ咯婵揩婵凯婵版勘婵叉砍婵存康婵舵糠婵告抗婵烘炕婵兼拷婵炬靠鐎€鐎佺€傜€冪€勭€呯€嗙€囩€堢€夌€婄€嬬€岀€嶇€庣€忕€愮€掔€撶€旂€曠€栫€楃€樼€欑€滅€濈€炵€熺€犵€＄€㈢€ょ€ョ€︾€х€ㄧ€╃€拷鐎€€€€€扮€辩€茬€崇€寸€剁€风€哥€虹€荤€肩€界€剧€跨亐鐏佺亗鐏冪亜鐏呯亞鐏囩亪鐏夌亰鐏嬬亶鐏庣亹鐏戠亽鐏撶仈鐏曠仏鐏楃仒鐏欑仛鐏涚仠鐏濈仧鐏犵仭鐏㈢仯鐏ょ仴鐏︾仹鐏ㄧ仼鐏伄鐏辩伈鐏崇伌鐏风伖鐏虹伝鐏界倎鐐傜們鐐勭倖鐐囩倛鐐嬬倢鐐嶇倧鐐愮倯鐐撶倵鐐樼倸鐐涚倿鐐熺偁鐐＄偄鐐ｇ偆鐐ョ偊鐐х偍鐐╃偑鐐扮偛鐐寸偟鐐剁偤鐐剧偪鐑勭儏鐑嗙儑鐑夌儖鐑岀儘鐑庣儚鐑愮儜鐑掔儞鐑旂儠鐑栫儣鐑氾拷".split("");
	for(a = 0; a != t[158].length; ++a)
		if(t[158][a].charCodeAt(0) !== 65533) { r[t[158][a]] = 40448 + a;
			e[40448 + a] = t[158][a] }
	t[159] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐑滅儩鐑炵儬鐑＄儮鐑ｇ儱鐑儺鐑扮儽鐑茬兂鐑寸兊鐑剁兏鐑虹兓鐑肩兙鐑跨剙鐒佺剛鐒冪剟鐒呯剢鐒囩剤鐒嬬剬鐒嶇剮鐒忕剳鐒掔剶鐒楃剾鐒滅劃鐒炵劅鐒犵劇鐒㈢劊鐒ょ劌鐒х劏鐒╃劒鐒劕鐒劗鐒茬劤鐒达拷鐒电劮鐒哥劰鐒虹劵鐒肩劷鐒剧効鐓€鐓佺厒鐓冪厔鐓嗙厙鐓堢厜鐓嬬厤鐓忕厫鐓戠厭鐓撶厰鐓曠厲鐓楃厴鐓欑厷鐓涚厺鐓熺厾鐓＄參鐓ｇ叆鐓╃叒鐓叕鐓叝鐓扮叡鐓寸叺鐓剁叿鐓圭吇鐓肩吘鐓跨唨鐔佺唫鐔冪唴鐔嗙唶鐔堢唹鐔嬬唽鐔嶇啂鐔愮啈鐔掔啌鐔曠問鐔楃啔鐔涚啘鐔濈啚鐔＄啟鐔ｇ啢鐔ョ啨鐔х啯鐔啱鐔啴鐔啺鐔辩啿鐔寸喍鐔风喐鐔虹喕鐔肩喗鐔剧喛鐕€鐕佺噦鐕勭噮鐕嗙噰鐕堢噳鐕婄噵鐕岀噸鐕忕噽鐕戠噿鐕擄拷".split("");
	for(a = 0; a != t[159].length; ++a)
		if(t[159][a].charCodeAt(0) !== 65533) { r[t[159][a]] = 40704 + a;
			e[40704 + a] = t[159][a] }
	t[160] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐕栫嚄鐕樼嚈鐕氱嚊鐕滅嚌鐕炵嚐鐕＄嚔鐕ｇ嚖鐕︾嚚鐕╃嚜鐕嚞鐕嚡鐕扮嚤鐕茬嚦鐕寸嚨鐕剁嚪鐕哥嚭鐕荤嚰鐕界嚲鐕跨垁鐖佺垈鐖冪垊鐖呯垏鐖堢垑鐖婄垕鐖岀垗鐖庣垙鐖愮垜鐖掔垞鐖旂垥鐖栫垪鐖樼垯鐖氾拷鐖涚垳鐖炵垷鐖犵垺鐖㈢垼鐖ょ垾鐖︾埀鐖╃埆鐖埉鐖埐鐖崇埓鐖虹埣鐖剧墍鐗佺墏鐗冪墑鐗呯墕鐗夌墛鐗嬬墡鐗忕墣鐗戠墦鐗旂墪鐗楃墭鐗氱墱鐗炵墵鐗ｇ墹鐗ョ墾鐗壂鐗壄鐗扮壉鐗崇壌鐗剁壏鐗哥壔鐗肩壗鐘傜妰鐘呯妴鐘囩妶鐘夌妼鐘庣姁鐘戠姄鐘旂姇鐘栫姉鐘樼姍鐘氱姏鐘滅姖鐘炵姞鐘＄姠鐘ｇ姢鐘ョ姦鐘х姩鐘╃姫鐘姰鐘辩姴鐘崇姷鐘虹娀鐘肩娊鐘剧娍鐙€鐙呯媶鐙囩媺鐙婄媼鐙岀嫃鐙戠嫇鐙旂嫊鐙栫嫎鐙氱嫑锟�".split("");
	for(a = 0; a != t[160].length; ++a)
		if(t[160][a].charCodeAt(0) !== 65533) { r[t[160][a]] = 40960 + a;
			e[40960 + a] = t[160][a] }
	t[161] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟姐€€銆併€偮匪壦嚶ㄣ€冦€呪€旓綖鈥栤€︹€樷€欌€溾€濄€斻€曘€堛€夈€娿€嬨€屻€嶃€庛€忋€栥€椼€愩€懧泵椕封埗鈭р埁鈭戔垙鈭埄鈭堚埛鈭氣姤鈭モ垹鈱掆姍鈭埉鈮♀墝鈮堚埥鈭濃墵鈮壇鈮も墺鈭炩埖鈭粹檪鈾€掳鈥测€斥剝锛劼わ繝锟♀€奥р剸鈽嗏槄鈼嬧棌鈼庘棁鈼嗏枴鈻犫柍鈻测€烩啋鈫愨啈鈫撱€擄拷".split("");
	for(a = 0; a != t[161].length; ++a)
		if(t[161][a].charCodeAt(0) !== 65533) { r[t[161][a]] = 41216 + a;
			e[41216 + a] = t[161][a] }
	t[162] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟解叞鈪扁叢鈪斥叴鈪碘叾鈪封吀鈪癸拷锟斤拷锟斤拷锟解拡鈷夆拪鈷嬧拰鈷嶁拵鈷忊拹鈷戔拻鈷撯挃鈷曗挅鈷椻挊鈷欌挌鈷涒懘鈶碘懚鈶封懜鈶光懞鈶烩懠鈶解懢鈶库拃鈷佲拏鈷冣拕鈷呪拞鈷団憼鈶♀憿鈶ｂ懁鈶モ懄鈶р懆鈶╋拷锟姐垹銏°垻銏ｃ垽銏ャ垿銏с埁銏╋拷锟解厾鈪♀參鈪ｂ叅鈪モ叇鈪р叏鈪┾叒鈪拷锟斤拷".split("");
	for(a = 0; a != t[162].length; ++a)
		if(t[162][a].charCodeAt(0) !== 65533) { r[t[162][a]] = 41472 + a;
			e[41472 + a] = t[162][a] }
	t[163] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤紒锛傦純锟ワ紖锛嗭紘锛堬級锛婏紜锛岋紞锛庯紡锛愶紤锛掞紦锛旓紩锛栵紬锛橈紮锛氾紱锛滐紳锛烇紵锛狅肌锛迹锛わ讥锛︼姬锛缉锛极锛辑锛集锛帮急锛诧汲锛达嫉锛讹挤锛革脊锛猴蓟锛硷冀锛撅伎锝€锝侊絺锝冿絼锝咃絾锝囷綀锝夛綂锝嬶綄锝嶏綆锝忥綈锝戯綊锝擄綌锝曪綎锝楋綐锝欙綒锝涳綔锝濓浚锟�".split("");
	for(a = 0; a != t[163].length; ++a)
		if(t[163][a].charCodeAt(0) !== 65533) { r[t[163][a]] = 41728 + a;
			e[41728 + a] = t[163][a] }
	t[164] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟姐亖銇傘亙銇勩亝銇嗐亣銇堛亯銇娿亱銇屻亶銇庛亸銇愩亼銇掋亾銇斻仌銇栥仐銇樸仚銇氥仜銇溿仢銇炪仧銇犮仭銇仯銇ゃ仴銇︺仹銇ㄣ仼銇伀銇伃銇伅銇般伇銇层伋銇淬伒銇躲伔銇搞伖銇恒伝銇笺伣銇俱伩銈€銈併倐銈冦倓銈呫倖銈囥倛銈夈倞銈嬨倢銈嶃値銈忋倫銈戙倰銈擄拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[164].length; ++a)
		if(t[164][a].charCodeAt(0) !== 65533) { r[t[164][a]] = 41984 + a;
			e[41984 + a] = t[164][a] }
	t[165] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟姐偂銈偅銈ゃ偉銈︺偋銈ㄣ偐銈偒銈偔銈偗銈般偙銈层偝銈淬偟銈躲偡銈搞偣銈恒偦銈笺偨銈俱偪銉€銉併儌銉冦儎銉呫儐銉囥儓銉夈儕銉嬨儗銉嶃儙銉忋儛銉戙儝銉撱償銉曘儢銉椼儤銉欍儦銉涖儨銉濄優銉熴儬銉°儮銉ｃ儰銉ャ儲銉с儴銉┿儶銉儸銉儺銉儼銉便儾銉炽兇銉点兌锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[165].length; ++a)
		if(t[165][a].charCodeAt(0) !== 65533) { r[t[165][a]] = 42240 + a;
			e[42240 + a] = t[165][a] }
	t[166] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟轿懳捨撐斘曃栁椢樜櫸毼浳溛澪炍熚犖∥Ｎのノξㄎ╋拷锟斤拷锟斤拷锟斤拷锟轿蔽参澄次滴段肺肝刮何晃嘉轿疚肯€蟻蟽蟿蠀蠁蠂蠄蠅锟斤拷锟斤拷锟斤拷锟斤傅锔讹腹锔猴缚锕€锔斤妇锕侊箓锕冿箘锟斤拷锔伙讣锔凤父锔憋拷锔筹复锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[166].length; ++a)
		if(t[166][a].charCodeAt(0) !== 65533) { r[t[166][a]] = 42496 + a;
			e[42496 + a] = t[166][a] }
	t[167] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟叫愋懶捫撔斝曅佇栃椥樞櫺毿浶溞澬炐熜犘⌒⑿Ｐばバπㄐ┬拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷邪斜胁谐写械褢卸蟹懈泄泻谢屑薪芯锌褉褋褌褍褎褏褑褔褕褖褗褘褜褝褞褟锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[167].length; ++a)
		if(t[167][a].charCodeAt(0) !== 65533) { r[t[167][a]] = 42752 + a;
			e[42752 + a] = t[167][a] }
	t[168] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷藠藡藱鈥撯€曗€モ€碘剠鈩夆問鈫椻啒鈫欌垥鈭熲垼鈮掆墻鈮р娍鈺愨晳鈺掆晸鈺斺晻鈺栤晽鈺樷暀鈺氣暃鈺溾暆鈺炩暉鈺犫暋鈺⑩暎鈺も暐鈺︹暓鈺ㄢ暕鈺暙鈺暛鈺暞鈺扳暠鈺测暢鈻佲杺鈻冣杽鈻呪枂鈻囷拷鈻堚枆鈻娾枊鈻屸枍鈻庘枏鈻撯枖鈻曗柤鈻解棦鈼ｂ棨鈼モ槈鈯曘€掋€濄€烇拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷膩谩菐脿膿茅臎猫墨铆菒矛艒贸菕貌奴煤菙霉菛菢菤菧眉锚蓱锟脚勁堬拷伞锟斤拷锟斤拷銊呫剢銊囥剤銊夈剨銊嬨剬銊嶃剮銊忋剱銊戙剴銊撱剶銊曘剸銊椼剺銊欍剼銊涖劀銊濄劄銊熴劆銊°劉銊ｃ劋銊ャ劍銊с劏銊╋拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[168].length; ++a)
		if(t[168][a].charCodeAt(0) !== 65533) { r[t[168][a]] = 43008 + a;
			e[43008 + a] = t[168][a] }
	t[169] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷銆°€€ｃ€ゃ€ャ€︺€с€ㄣ€┿姡銕庛帍銕溿帩銕炪帯銖勩弾銖戙彃銖曪赴锟郡锟解劇銏憋拷鈥愶拷锟斤拷銉笺倹銈溿兘銉俱€嗐倽銈烇箟锕婏箣锕岋箥锕庯箯锕愶箲锕掞箶锕曪箹锕楋箼锕氾箾锕滐節锕烇篃锕狅埂锟斤耿锕ｏ工锕ワ功锕供锕公锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟姐€囷拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鈹€鈹佲攤鈹冣攧鈹呪攩鈹団攬鈹夆攰鈹嬧攲鈹嶁攷鈹忊攼鈹戔敀鈹撯敂鈹曗敄鈹椻敇鈹欌敋鈹涒敎鈹濃敒鈹熲敔鈹♀敘鈹ｂ敜鈹モ敠鈹р敤鈹┾敧鈹敩鈹敭鈹敯鈹扁敳鈹斥敶鈹碘敹鈹封敻鈹光敽鈹烩敿鈹解斁鈹库晙鈺佲晜鈺冣晞鈺呪晢鈺団晥鈺夆晩鈺嬶拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[169].length; ++a)
		if(t[169][a].charCodeAt(0) !== 65533) { r[t[169][a]] = 43264 + a;
			e[43264 + a] = t[169][a] }
	t[170] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐙滅嫕鐙熺嫝鐙ｇ嫟鐙ョ嫤鐙х嫪鐙嫷鐙剁嫻鐙界嬀鐙跨寑鐚傜寗鐚呯寙鐚囩寛鐚夌寢鐚岀實鐚忕寪鐚戠寬鐚旂寴鐚欑寶鐚熺尃鐚ｇ尋鐚︾導鐚ㄧ尛鐚尠鐚茬尦鐚电尪鐚虹尰鐚肩尳鐛€鐛佺崅鐛冪崉鐛呯崋鐛囩崍锟界崏鐛婄崑鐛岀崕鐛忕崙鐛撶崝鐛曠崠鐛樼崣鐛氱崨鐛滅崫鐛炵崯鐛＄崲鐛ｇ崵鐛ョ崷鐛х崹鐛╃崻鐛嵁鐛扮嵄锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[170].length; ++a)
		if(t[170][a].charCodeAt(0) !== 65533) { r[t[170][a]] = 43520 + a;
			e[43520 + a] = t[170][a] }
	t[171] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐛茬嵆鐛寸嵉鐛剁嵎鐛哥嵐鐛虹嵒鐛肩嵔鐛跨巰鐜佺巶鐜冪巺鐜嗙巿鐜婄帉鐜嶇帍鐜愮帓鐜撶帞鐜曠帡鐜樼帣鐜氱帨鐜濈帪鐜犵帯鐜ｇ帳鐜ョ帵鐜х帹鐜幀鐜幈鐜寸幍鐜剁幐鐜圭幖鐜界幘鐜跨弫鐝冪弰鐝呯弳鐝囷拷鐝嬬弻鐝庣彃鐝撶彅鐝曠彇鐝楃彉鐝氱彌鐝滅彎鐝熺彙鐝㈢彛鐝ょ彟鐝ㄧ彧鐝彫鐝彲鐝扮彵鐝崇彺鐝电彾鐝凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[171].length; ++a)
		if(t[171][a].charCodeAt(0) !== 65533) { r[t[171][a]] = 43776 + a;
			e[43776 + a] = t[171][a] }
	t[172] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐝哥徆鐝虹徎鐝肩徑鐝剧徔鐞€鐞佺悅鐞勭悋鐞堢悑鐞岀悕鐞庣悜鐞掔悡鐞旂悤鐞栫悧鐞樼悪鐞滅悵鐞炵悷鐞犵悺鐞ｇ悿鐞х惄鐞惌鐞惐鐞茬惙鐞哥惞鐞虹惢鐞界惥鐞跨憖鐟傜憙鐟勭憛鐟嗙憞鐟堢憠鐟婄憢鐟岀憤锟界憥鐟忕憪鐟戠憭鐟撶憯鐟栫憳鐟濈憼鐟＄憿鐟ｇ懁鐟ョ懄鐟х懆鐟╃應鐟懍鐟懐鐟辩懖鐟崇懘鐟电懜鐟圭懞锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[172].length; ++a)
		if(t[172][a].charCodeAt(0) !== 65533) { r[t[172][a]] = 44032 + a;
			e[44032 + a] = t[172][a] }
	t[173] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐟荤懠鐟界懣鐠傜拕鐠呯拞鐠堢拤鐠婄拰鐠嶇拸鐠戠拻鐠撶挃鐠曠挅鐠楃挊鐠欑挌鐠涚挐鐠熺挔鐠＄挗鐠ｇ挙鐠ョ挦鐠挮鐠挱鐠挴鐠扮挶鐠茬挸鐠寸挼鐠剁挿鐠哥捁鐠荤捈鐠界捑鐠跨搥鐡佺搨鐡冪搫鐡呯搯鐡囷拷鐡堢搲鐡婄搵鐡岀搷鐡庣搹鐡愮搼鐡撶摂鐡曠摉鐡楃摌鐡欑摎鐡涚摑鐡熺摗鐡ョ摟鐡ㄧ摡鐡摣鐡摥鐡扮摫鐡诧拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[173].length; ++a)
		if(t[173][a].charCodeAt(0) !== 65533) { r[t[173][a]] = 44288 + a;
			e[44288 + a] = t[173][a] }
	t[174] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐡崇摰鐡哥摴鐡虹摶鐡肩摻鐡剧攢鐢佺攤鐢冪攨鐢嗙攪鐢堢攭鐢婄攱鐢岀攷鐢愮敀鐢旂敃鐢栫敆鐢涚敐鐢炵敔鐢＄敘鐢ｇ敜鐢︾敡鐢敭鐢寸敹鐢圭敿鐢界斂鐣佺晜鐣冪晞鐣嗙晣鐣夌晩鐣嶇晲鐣戠晵鐣撶晻鐣栫晽鐣橈拷鐣濈暈鐣熺暊鐣＄暍鐣ｇ暏鐣х暔鐣╃暙鐣暛鐣暞鐣扮暠鐣崇暤鐣剁暦鐣虹暬鐣肩暯鐣剧杸鐤佺杺鐤勭枀鐤囷拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[174].length; ++a)
		if(t[174][a].charCodeAt(0) !== 65533) { r[t[174][a]] = 44544 + a;
			e[44544 + a] = t[174][a] }
	t[175] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐤堢枆鐤婄枌鐤嶇枎鐤愮枔鐤曠枠鐤涚枩鐤炵枹鐤︾枾鐤ㄧ柀鐤柇鐤剁柗鐤虹柣鐤跨梹鐥佺梿鐥嬬棇鐥庣棌鐥愮棏鐥撶棗鐥欑棜鐥滅棟鐥熺棤鐥＄棩鐥╃棳鐥棶鐥棽鐥崇椀鐥剁椃鐥哥椇鐥荤椊鐥剧槀鐦勭槅鐦囷拷鐦堢槈鐦嬬槏鐦庣槒鐦戠槖鐦撶様鐦栫槡鐦滅槤鐦炵槨鐦ｇ槯鐦ㄧ槵鐦槸鐦辩槻鐦剁樂鐦圭樅鐦荤樈鐧佺檪鐧勶拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[175].length; ++a)
		if(t[175][a].charCodeAt(0) !== 65533) { r[t[175][a]] = 44800 + a;
			e[44800 + a] = t[175][a] }
	t[176] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐧呯檰鐧囩檲鐧夌檴鐧嬬檸鐧忕檺鐧戠檼鐧撶檿鐧楃櫂鐧欑櫄鐧涚櫇鐧熺櫊鐧＄櫌鐧ょ櫏鐧︾櫑鐧ㄧ櫓鐧櫖鐧櫘鐧扮櫛鐧茬櫝鐧寸櫟鐧剁櫡鐧圭櫤鐧肩櫩鐨€鐨佺殐鐨呯殙鐨婄殞鐨嶇殢鐨愮殥鐨旂殨鐨楃殬鐨氱殯锟界殰鐨濈殲鐨熺殸鐨＄殺鐨ｇ殽鐨︾毀鐨ㄧ毄鐨毇鐨毉鐨毎鐨崇毜鐨剁毞鐨哥毠鐨虹毣鐨肩毥鐨剧泙鐩佺泝鍟婇樋鍩冩尐鍝庡攭鍝€鐨戠檶钄肩煯鑹剧鐖遍殬闉嶆皑瀹変亢鎸夋殫宀歌兒妗堣偖鏄傜泿鍑规晼鐔勘琚勫偛濂ユ噴婢宠姯鎹屾墥鍙惂绗嗗叓鐤ゅ反鎷旇穻闈舵妸鑰欏潩闇哥舰鐖哥櫧鏌忕櫨鎽嗕桨璐ユ嫓绋楁枒鐝惉鎵宠埇棰佹澘鐗堟壆鎷屼即鐡ｅ崐鍔炵粖閭﹀府姊嗘鑶€缁戞纾呰殞闀戝倣璋よ嫗鑳炲寘瑜掑墺锟�".split("");
	for(a = 0; a != t[176].length; ++a)
		if(t[176][a].charCodeAt(0) !== 65533) { r[t[176][a]] = 45056 + a;
			e[45056 + a] = t[176][a] }
	t[177] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐩勭泧鐩夌泲鐩岀洆鐩曠洐鐩氱洔鐩濈洖鐩犵洝鐩㈢洠鐩ょ洣鐩х洦鐩╃洩鐩洭鐩洶鐩崇浀鐩剁浄鐩虹浕鐩界浛鐪€鐪傜渻鐪呯渾鐪婄湆鐪庣湉鐪愮湋鐪掔湏鐪旂湑鐪栫湕鐪樼湜鐪滅湞鐪炵湣鐪ｇ湦鐪ョ湩鐪湯锟界湰鐪湴鐪辩湶鐪崇湸鐪圭溁鐪界溇鐪跨潅鐫勭潊鐫嗙潏鐫夌潑鐫嬬潓鐫嶇潕鐫忕潚鐫撶潝鐫曠潠鐫楃潣鐫欑潨钖勯浌淇濆牎楗卞疂鎶辨姤鏆磋惫椴嶇垎鏉鎮插崙鍖楄緢鑳岃礉閽″€嶇媹澶囨儷鐒欒濂旇嫰鏈宕╃环鐢车韫﹁扛閫奸蓟姣旈剻绗斿郊纰ц摉钄芥瘯姣欐瘱甯佸簢鐥归棴鏁濆紛蹇呰緹澹佽噦閬块櫅闉竟缂栬船鎵佷究鍙樺崬杈ㄨ京杈亶鏍囧姜鑶樿〃槌栨唻鍒槳褰枌婵掓花瀹炬憟鍏靛啺鏌勪笝绉夐ゼ鐐筹拷".split("");
	for(a = 0; a != t[177].length; ++a)
		if(t[177][a].charCodeAt(0) !== 65533) { r[t[177][a]] = 45312 + a;
			e[45312 + a] = t[177][a] }
	t[178] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐫濈潪鐫熺潬鐫ょ潷鐫╃潽鐫澁鐫澃鐫辩澆鐫崇澊鐫电澏鐫风澑鐫虹澔鐫肩瀬鐬傜瀮鐬嗙瀲鐬堢瀴鐬婄瀷鐬忕瀽鐬撶灁鐬曠灃鐬楃灅鐬欑灇鐬涚灉鐬濈灋鐬＄灒鐬ょ灕鐬ㄧ灚鐬灝鐬灡鐬茬灤鐬剁灧鐬哥灩鐬猴拷鐬肩灳鐭€鐭佺焸鐭冪焺鐭呯焼鐭囩焾鐭夌煀鐭嬬煂鐭庣煆鐭愮煈鐭掔煋鐭旂煏鐭栫煒鐭欑煔鐭濈煘鐭熺煚鐭＄煠鐥呭苟鐜昏彔鎾嫧閽垫尝鍗氬媰鎼忛搨绠斾集甯涜埗鑴栬唺娓ゆ硦椹虫崟鍗滃摵琛ュ煚涓嶅竷姝ョ翱閮ㄦ€栨摝鐚滆鏉愭墠璐㈢潿韪╅噰褰╄彍钄￠鍙傝殨娈嬫儹鎯ㄧ伩鑻嶈埍浠撴钵钘忔搷绯欐Ы鏇硅崏鍘曠瓥渚у唽娴嬪眰韫彃鍙夎尙鑼舵煡纰存惤瀵熷矓宸鎷嗘煷璞烘悁鎺鸿潐棣嬭皸缂犻摬浜ч槓棰ゆ槍鐚栵拷".split("");
	for(a = 0; a != t[178].length; ++a)
		if(t[178][a].charCodeAt(0) !== 65533) { r[t[178][a]] = 45568 + a;
			e[45568 + a] = t[178][a] }
	t[179] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐭︾煥鐭煰鐭扮煴鐭茬煷鐭电煼鐭圭熀鐭荤熂鐮冪爠鐮呯爢鐮囩爤鐮婄爧鐮庣爮鐮愮爴鐮曠牂鐮涚牉鐮犵牎鐮㈢牑鐮ㄧ牚鐮牣鐮牨鐮茬牫鐮电牰鐮界牽纭佺纭冪纭嗙纭夌纭嬬纭忕纭撶纭樼纭氾拷纭涚纭炵纭犵　纭㈢。纭ょˉ纭︾¨纭ㄧ々纭“纭辩〔纭崇〈纭电《纭哥」纭虹』纭界【纭跨纰佺纰冨満灏濆父闀垮伩鑲犲巶鏁炵晠鍞卞€¤秴鎶勯挒鏈濆槻娼发鍚电倰杞︽壇鎾ゆ帲褰绘緢閮磋嚕杈板皹鏅ㄥ勘娌夐檲瓒佽‖鎾戠О鍩庢鎴愬憟涔樼▼鎯╂緞璇氭壙閫為獘绉ゅ悆鐥存寔鍖欐睜杩熷紱椹拌€婚娇渚堝昂璧ょ繀鏂ョ偨鍏呭啿铏磭瀹犳娊閰暣韪岀鎰佺浠囩桓鐬呬笐鑷垵鍑烘┍鍘ㄨ簢閿勯洀婊侀櫎妤氾拷".split("");
	for(a = 0; a != t[179].length; ++a)
		if(t[179][a].charCodeAt(0) !== 65533) { r[t[179][a]] = 45824 + a;
			e[45824 + a] = t[179][a] }
	t[180] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷纰勭纰嗙纰婄纰忕纰掔纰曠纰欑纰炵纰㈢ⅳ纰︾ⅷ纰╃ⅹ纰纰纰⒌纰剁⒎纰哥⒑纰荤⒓纰界⒖纾€纾傜纾勭纾囩纾岀纾庣纾戠纾撶纾楃纾氱纾滅纾炵纾犵！纾㈢＃锟界￥纾ョ＆纾х）纾＋纾．纾０纾辩３纾电６纾哥９纾荤＜纾界＞纾跨绀傜绀勭绀囩绀夌绀嬬纭€鍌ㄧ煑鎼愯Е澶勬彛宸濈┛妞戒紶鑸瑰枠涓茬柈绐楀耿搴婇棷鍒涘惞鐐婃嵍閿ゅ瀭鏄ユた閱囧攪娣崇函锠㈡埑缁扮柕鑼ㄧ闆岃緸鎱堢摲璇嶆鍒鸿祼娆¤仾钁卞洷鍖嗕粠涓涘噾绮楅唻绨囦績韫跨绐滄懅宕斿偓鑴嗙榿绮规番缈犳潙瀛樺纾嬫挳鎼撴帾鎸敊鎼揪绛旂槱鎵撳ぇ鍛嗘鍌ｆ埓甯︽畣浠ｈ捶琚嬪緟閫拷".split("");
	for(a = 0; a != t[180].length; ++a)
		if(t[180][a].charCodeAt(0) !== 65533) { r[t[180][a]] = 46080 + a;
			e[46080 + a] = t[180][a] }
	t[181] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绀嶇绀忕绀戠绀旂绀栫绀樼绀氱绀滅绀熺绀＄あ绀ｇぅ绀︾ぇ绀ㄧぉ绀か绀き绀く绀扮け绀茬こ绀电ざ绀风じ绀圭そ绀跨绁冪绁呯绁婄绁岀绁庣绁愮绁掔绁曠绁欑ァ绁ｏ拷绁ょウ绁╃オ绁ガ绁グ绁辩ゲ绁崇ゴ绁电ザ绁圭セ绁肩ソ绁剧タ绂傜绂嗙绂堢绂嬬绂嶇绂愮绂掓€犺€芥媴涓瑰崟閮告幐鑳嗘棪姘絾鎯贰璇炲脊铔嬪綋鎸″厷鑽℃。鍒€鎹ｈ箞鍊掑矝绁峰鍒扮ɑ鎮奸亾鐩楀痉寰楃殑韫伅鐧荤瓑鐬嚦閭撳牑浣庢淮杩晫绗涚媱娑ょ繜瀚℃姷搴曞湴钂傜甯濆紵閫掔紨棰犳巶婊囩鐐瑰吀闈涘灚鐢典絻鐢稿簵鎯﹀娣€娈跨鍙奸洉鍑嬪垇鎺夊悐閽撹皟璺岀埞纰熻澏杩皪鍙狅拷".split("");
	for(a = 0; a != t[181].length; ++a)
		if(t[181][a].charCodeAt(0) !== 65533) { r[t[181][a]] = 46336 + a;
			e[46336 + a] = t[181][a] }
	t[182] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绂撶绂曠绂楃绂欑绂滅绂炵绂犵Α绂㈢Γ绂ょΕ绂︾Θ绂╃Κ绂Μ绂Ξ绂Π绂辩Σ绂寸Φ绂剁Ψ绂哥绂跨绉勭绉囩绉婄绉庣绉愮绉旂绉楃绉氱绉滅绉炵绉＄Б绉ョЖ绉拷绉М绉辩Р绉崇Т绉电Ф绉风Ч绉虹Ъ绉剧Э绋佺▌绋呯▏绋堢▔绋婄▽绋忕◥绋戠⊕绋撶〞绋栫绋欑绋滀竵鐩彯閽夐《榧庨敪瀹氳涓笢鍐懀鎳傚姩鏍嬩緱鎭喕娲炲厹鎶栨枟闄¤眴閫楃棙閮界潱姣掔妸鐙鍫电澒璧屾潨闀€鑲氬害娓″绔煭閿绘鏂紟鍫嗗厬闃熷澧╁惃韫叉暒椤垮洡閽濈浘閬佹巼鍝嗗澶哄灈韬叉湹璺鸿埖鍓佹儼鍫曡浘宄ㄩ箙淇勯璁瑰ē鎭跺巹鎵奸亸閯傞タ鎭╄€屽効鑰冲皵楗垫幢浜岋拷".split("");
	for(a = 0; a != t[182].length; ++a)
		if(t[182][a].charCodeAt(0) !== 65533) { r[t[182][a]] = 46592 + a;
			e[46592 + a] = t[182][a] }
	t[183] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绋濈绋＄á绋ょē绋︾ě绋ㄧī绋ǐ绋ō绋ǒ绋扮ū绋茬ù绋电ǘ绋哥ê绋剧﹢绌佺﹤绌冪﹦绌呯﹪绌堢绌婄绌岀绌庣绌愮绌撶绌曠〇绌樼绌氱绌滅绌炵绌犵绌㈢绌ょ━绌︾┃绌拷绌╃┆绌┈绌┊绌┍绌茬┏绌电┗绌肩┙绌剧獋绐呯獓绐夌獖绐嬬獙绐庣獜绐愮獡绐旂獧绐氱獩绐炵绐㈣窗鍙戠綒绛忎紣涔忛榾娉曠彁钘╁竼鐣炕妯婄熅閽掔箒鍑＄儲鍙嶈繑鑼冭穿鐘キ娉涘潑鑺虫柟鑲埧闃插Θ浠胯绾烘斁鑿查潪鍟￠鑲ュ尓璇藉悹鑲哄簾娌歌垂鑺厷鍚╂皼鍒嗙悍鍧熺剼姹剧矇濂嬩唤蹇挎劋绮赴灏佹灚铚傚嘲閿嬮鐤兘閫㈠啹缂濊濂夊嚖浣涘惁澶暦鑲ゅ鎵舵媯杈愬箙姘熺浼忎繕鏈嶏拷".split("");
	for(a = 0; a != t[183].length; ++a)
		if(t[183][a].charCodeAt(0) !== 65533) { r[t[183][a]] = 46848 + a;
			e[46848 + a] = t[183][a] }
	t[184] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绐ｇ绐х绐绐绐扮绐茬绐电绐风绐圭绐荤绐界绔€绔佺珎绔冪珓绔呯珕绔囩珗绔夌珚绔岀珝绔庣珡绔愮珣绔掔珦绔旂珪绔楃珮绔氱珱绔滅珴绔＄绔ょ绔ㄧ绔绔绔扮绔茬锟界绔电绔风绔荤绔剧瑎绗佺瑐绗呯瑖绗夌瑢绗嶇瑤绗愮瑨绗撶瑬绗楃瑯绗氱瑴绗濈瑹绗＄绗ｇ绗╃娴丢绂忚⒈寮楃敨鎶氳緟淇嚋鏂ц劘鑵戝簻鑵愯荡鍓璧嬪鍌呬粯闃滅埗鑵硅礋瀵岃闄勫缂氬拹鍣跺槑璇ユ敼姒傞挋鐩栨簤骞茬敇鏉嗘煈绔胯倽璧舵劅绉嗘暍璧ｅ唸鍒氶挗缂歌倹绾插矖娓潬绡欑殝楂樿啅缇旂硶鎼為晲绋垮憡鍝ユ瓕鎼佹垐楦借兂鐤欏壊闈╄憶鏍艰洡闃侀殧閾釜鍚勭粰鏍硅窡鑰曟洿搴氱竟锟�".split("");
	for(a = 0; a != t[184].length; ++a)
		if(t[184][a].charCodeAt(0) !== 65533) { r[t[184][a]] = 47104 + a;
			e[47104 + a] = t[184][a] }
	t[185] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绗绗茬绗电绗风绗荤绗跨瓈绛佺瓊绛冪瓌绛嗙瓐绛婄瓖绛庣瓝绛曠瓧绛欑瓬绛炵瓱绛＄绛ょ绛︾绛ㄧ绛绛绛绛崇绛剁绛虹绛界绠佺畟绠冪畡绠嗙畤绠堢畨绠婄畫绠岀畮绠忥拷绠戠畳绠撶畺绠樼畽绠氱疀绠炵疅绠犵绠ょ绠绠扮绠崇绠剁绠圭绠荤绠界绠跨瘈绡傜瘍绡勫焸鑰挎宸ユ敾鍔熸伃榫氫緵韬叕瀹紦宸╂睘鎷辫础鍏遍挬鍕炬矡鑻熺嫍鍨㈡瀯璐杈滆弴鍜曠畭浼版步瀛ゅ榧撳彜铔婇璋疯偂鏁呴【鍥洪泧鍒摐鍓愬鎸傝涔栨嫄鎬：鍏冲畼鍐犺绠￠缃愭儻鐏岃疮鍏夊箍閫涚懓瑙勫湱纭呭綊榫熼椇杞ㄩ璇＄櫢妗傛煖璺吹鍒借緤婊氭閿呴儹鍥芥灉瑁硅繃鍝堬拷".split("");
	for(a = 0; a != t[185].length; ++a)
		if(t[185][a].charCodeAt(0) !== 65533) { r[t[185][a]] = 47360 + a;
			e[47360 + a] = t[185][a] }
	t[186] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绡呯瘓绡夌瘖绡嬬瘝绡庣瘡绡愮瘨绡旂瘯绡栫瘲绡樼瘺绡滅癁绡熺癄绡㈢绡ょ绡ㄧ绡绡绡扮绡崇绡电绡哥绡虹绡界绨€绨佺皞绨冪皠绨呯皢绨堢皦绨婄皪绨庣皭绨戠皰绨撶皵绨曠皸绨樼皺锟界皻绨涚皽绨濈盀绨犵啊绨㈢埃绨ょ哎绨ㄧ癌绨艾绨爱绨鞍绨辩安绨崇按绨电岸绨风肮绨虹盎绨肩敖绨剧眰楠稿娴锋唉浜ュ楠囬叄鎲ㄩ偗闊╁惈娑靛瘨鍑藉枈缃曠堪鎾兼崓鏃辨喚鎮嶇剨姹楁眽澶澀鑸鍤庤豹姣儩濂借€楀彿娴╁懙鍠濊嵎鑿忔牳绂惧拰浣曞悎鐩掕矇闃傛渤娑歌但瑜愰工璐哄樋榛戠棔寰堢嫚鎭ㄥ摷浜ㄦí琛℃亽杞板搫鐑樿櫣楦挎椽瀹忓紭绾㈠枆渚尨鍚煎帤鍊欏悗鍛间箮蹇界憵澹惰懌鑳¤澊鐙愮硦婀栵拷".split("");
	for(a = 0; a != t[186].length; ++a)
		if(t[186][a].charCodeAt(0) !== 65533) { r[t[186][a]] = 47616 + a;
			e[47616 + a] = t[186][a] }
	t[187] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绫冪眲绫呯眴绫囩眻绫夌眾绫嬬睂绫庣睆绫愮睉绫掔睋绫旂睍绫栫睏绫樼睓绫氱睕绫滅睗绫炵睙绫犵薄绫㈢保绫ょ饱绫︾抱绫ㄧ暴绫鲍绫杯绫悲绫扮北绫茬钡绫剁狈绫哥惫绫虹本绫跨瞼绮佺矀绮冪矂绮呯矄绮囷拷绮堢矈绮嬬矊绮嶇矌绮忕矏绮撶矓绮栫矙绮氱矝绮犵病绮ｇ拨绮х波绮╃搏绮箔绮舶绮寸驳绮剁卜绮哥埠绮诲姬铏庡敩鎶や簰娌埛鑺卞摋鍗庣尵婊戠敾鍒掑寲璇濇寰婃€€娣潖娆㈢幆妗撹繕缂撴崲鎮ｅ敜鐥雹鐒曟叮瀹﹀够鑽掓厡榛勭：铦楃哀鐨囧嚢鎯剁厡鏅冨箤鎭嶈皫鐏版尌杈夊窘鎭㈣洈鍥炴瘉鎮旀収鍗夋儬鏅﹁纯绉戒細鐑╂眹璁宠缁樿崵鏄忓榄傛祽娣疯眮娲讳紮鐏幏鎴栨儜闇嶈揣绁稿嚮鍦惧熀鏈虹暩绋界Н绠曪拷".split("");
	for(a = 0; a != t[187].length; ++a)
		if(t[187][a].charCodeAt(0) !== 65533) { r[t[187][a]] = 47872 + a;
			e[47872 + a] = t[187][a] }
	t[188] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绮跨硛绯傜硟绯勭硢绯夌硧绯庣硰绯愮硲绯掔硴绯旂硺绯氱硾绯濈碁绯＄尝绯ｇ长绯ョ肠绯х畅绯倡绯抄绯嘲绯辩巢绯崇炒绯电扯绯风彻绯虹臣绯界尘绯跨磤绱佺磦绱冪磩绱呯磫绱囩磮绱夌磱绱岀磵绱庣磸绱愶拷绱戠磼绱撶磾绱曠礀绱楃礃绱欑礆绱涚礈绱濈礊绱熺础绱ｇ搐绱ョ处绱ㄧ穿绱船绱串绱扮幢绱茬闯绱寸吹绱惰倢楗ヨ抗婵€璁ラ浮濮哗缂夊悏鏋佹杈戠睄闆嗗強鎬ョ柧姹插嵆瀚夌骇鎸ゅ嚑鑴婂繁钃熸妧鍐€瀛ｄ紟绁墏鎮告祹瀵勫瘋璁¤鏃㈠繉闄呭缁х邯鍢夋灧澶逛匠瀹跺姞鑽氶璐剧敳閽惧亣绋间环鏋堕┚瀚佹鐩戝潥灏栫闂寸厧鍏艰偐鑹板ジ缂勮導妫€鏌⒈纭锋嫞鎹＄畝淇壀鍑忚崘妲涢壌璺佃幢瑙侀敭绠欢锟�".split("");
	for(a = 0; a != t[188].length; ++a)
		if(t[188][a].charCodeAt(0) !== 65533) { r[t[188][a]] = 48128 + a;
			e[48128 + a] = t[188][a] }
	t[189] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绱风锤绱圭春绱荤醇绱界淳绱跨祤绲佺祩绲冪祫绲呯祮绲囩祱绲夌祳绲嬬祵绲嶇祹绲忕祼绲戠祾绲撶禂绲曠禆绲楃禈绲欑禋绲涚禍绲濈禐绲熺禒绲＄耽绲ｇ丹绲ョ郸绲х胆绲╃氮绲惮绲弹绲扮当绲茬党绲寸档绲讹拷绲哥倒绲虹祷绲肩到绲剧悼缍€缍佺秱缍冪秳缍呯秵缍囩秷缍夌秺缍嬬秾缍嶇稁缍忕稅缍戠稈缍撶稊缍曠稏缍楃稑鍋ヨ埌鍓戦ク娓愭簠娑у缓鍍靛灏嗘祮姹熺枂钂嬫〃濂栬鍖犻叡闄嶈晧妞掔鐒﹁兌浜ら儕娴囬獎濞囧毤鎼呴摪鐭茎鑴氱嫛瑙掗ズ缂寸粸鍓挎暀閰佃娇杈冨彨绐栨彮鎺ョ殕绉歌闃舵埅鍔妭妗旀澃鎹风潾绔磥缁撹В濮愭垝钘夎姤鐣屽€熶粙鐤ヨ灞婂肪绛嬫枻閲戜粖娲ヨ绱ч敠浠呰皑杩涢澇鏅嬬杩戠儸娴革拷".split("");
	for(a = 0; a != t[189].length; ++a)
		if(t[189][a].charCodeAt(0) !== 65533) { r[t[189][a]] = 48384 + a;
			e[48384 + a] = t[189][a] }
	t[190] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷缍欑稓缍涚稖缍濈稙缍熺稜缍＄盯缍ｇ钉缍ョ锭缍ㄧ订缍东缍董缍栋缍辩恫缍崇洞缍电抖缍风陡缍圭逗缍荤都缍界毒缍跨穩绶佺穫绶冪穭绶呯穯绶囩穲绶夌穵绶嬬穼绶嶇穾绶忕窅绶戠窉绶撶窋绶曠窎绶楃窐绶欙拷绶氱窙绶滅窛绶炵窡绶犵贰绶㈢罚绶ょ伐绶︾阀绶ㄧ珐绶帆绶翻绶矾绶扮繁绶茬烦绶寸返绶剁贩绶哥饭绶哄敖鍔茶崋鍏㈣寧鐫涙櫠椴镐含鎯婄簿绮崇粡浜曡鏅闈欏鏁暅寰勭棄闈栫珶绔炲噣鐐獦鎻┒绾犵帠闊箙鐏镐節閰掑帺鏁戞棫鑷艰垍鍜庡氨鐤氶灎鎷樼嫏鐤藉眳椹硅強灞€鍜€鐭╀妇娌仛鎷掓嵁宸ㄥ叿璺濊笧閿勘鍙ユ儳鐐墽鎹愰箖濞熷€︾湻鍗风虎鎾呮敨鎶夋帢鍊旂埖瑙夊喅璇€缁濆潎鑿岄挧鍐涘悰宄伙拷".split("");
	for(a = 0; a != t[190].length; ++a)
		if(t[190][a].charCodeAt(0) !== 65533) { r[t[190][a]] = 48640 + a;
			e[48640 + a] = t[190][a] }
	t[191] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绶荤芳绶界肪绶跨竴绺佺競绺冪竸绺呯竼绺囩笀绺夌笂绺嬬笇绺嶇笌绺忕笎绺戠笒绺撶笖绺曠笘绺楃笜绺欑笟绺涚笢绺濈笧绺熺笭绺＄涪绺ｇ袱绺ョ甫绺х辅绺╃釜绺脯绺府绺赴绺辩覆绺崇复绺电付绺风父绺癸拷绺虹讣绺界妇绺跨箑绻傜箖绻勭箙绻嗙箞绻夌箠绻嬬箤绻嶇箮绻忕箰绻戠箳绻撶箶绻曠箹绻楃箻绻欑箽绻涚箿绻濅繆绔ｆ禋閮￠獜鍠€鍜栧崱鍜紑鎻╂シ鍑叏鍒婂牚鍕樺潕鐮嶇湅搴锋叿绯犳墰鎶椾孩鐐曡€冩嫹鐑ら潬鍧疯嫑鏌５纾曢绉戝３鍜冲彲娓村厠鍒诲璇捐偗鍟冨灕鎭冲潙鍚┖鎭愬瓟鎺ф姞鍙ｆ墸瀵囨灟鍝獰鑻﹂叿搴撹￥澶稿灝鎸庤法鑳潡绛蜂京蹇娆惧尅绛愮媯妗嗙熆鐪舵椃鍐典簭鐩斿部绐ヨ懙濂庨瓉鍌€锟�".split("");
	for(a = 0; a != t[191].length; ++a)
		if(t[191][a].charCodeAt(0) !== 65533) { r[t[191][a]] = 48896 + a;
			e[48896 + a] = t[191][a] }
	t[192] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绻炵篃绻犵埂绻㈢梗绻ょ攻绻︾恭绻ㄧ供绻公绻弓绻汞绻扮贡绻茬钩绻寸沟绻剁狗绻哥构绻虹够绻肩菇绻剧箍绾€绾佺簝绾勭簠绾嗙簢绾堢簤绾婄簨绾岀簫绾庣簭绾愮簯绾掔簱绾旂簳绾栫簵绾樼簷绾氱簻绾濈簽锟界寒绾寸夯绾肩粬缁ょ滑缁圭紛缂愮紴缂风脊缂荤技缂界季缂跨絸缃佺絻缃嗙絿缃堢綁缃婄綃缃岀綅缃庣綇缃掔綋棣堟劎婧冨潳鏄嗘崋鍥版嫭鎵╁粨闃斿瀮鎷夊枃铚¤厞杈ｅ暒鑾辨潵璧栬摑濠爮鎷︾闃戝叞婢滆鞍鎻借鎳掔紗鐑傛互鐞呮鐙煎粖閮庢湕娴崬鍔崇墷鑰佷浆濮ラ叒鐑欐稘鍕掍箰闆烽暛钑剧绱劇鍨掓搨鑲嬬被娉１妤炲喎鍘樻ⅷ鐘侀粠绡辩嫺绂绘紦鐞嗘潕閲岄菠绀艰帀鑽斿悘鏍椾附鍘夊姳鐮惧巻鍒╁倛渚嬩繍锟�".split("");
	for(a = 0; a != t[192].length; ++a)
		if(t[192][a].charCodeAt(0) !== 65533) { r[t[192][a]] = 49152 + a;
			e[49152 + a] = t[192][a] }
	t[193] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷缃栫綑缃涚綔缃濈綖缃犵剑缃ょ渐缃︾涧缃浆缃蒋缃扮匠缃电蕉缃风礁缃虹交缃肩浇缃跨線缇傜緝缇勭緟缇嗙緡缇堢緣缇嬬緧缇忕緪缇戠緬缇撶緯缇栫緱缇樼緳缇涚緶缇犵劲缇ｇ茎缇︾鲸缇╃惊缇粳缇井缇憋拷缇崇敬缇电径缇风竞缇荤揪缈€缈傜績缈勭繂缈囩繄缈夌繈缈嶇繌缈愮繎缈掔繐缈栫織缈欑繗缈涚繙缈濈繛缈㈢浚鐥㈢珛绮掓播闅跺姏鐠冨摡淇╄仈鑾茶繛闀板粔鎬滄稛甯樻暃鑴搁摼鎭嬬偧缁冪伯鍑夋绮辫壇涓よ締閲忔櫨浜皡鎾╄亰鍍氱枟鐕庡杈芥溅浜嗘拏闀ｅ粬鏂欏垪瑁傜儓鍔ｇ寧鐞虫灄纾烽湒涓撮偦槌炴穻鍑涜祦鍚濇嫀鐜茶彵闆堕緞閾冧级缇氬噷鐏甸櫟宀鍙︿护婧滅悏姒寸～棣忕暀鍒樼槫娴佹煶鍏緳鑱嬪挋绗肩锟�".split("");
	for(a = 0; a != t[193].length; ++a)
		if(t[193][a].charCodeAt(0) !== 65533) { r[t[193][a]] = 49408 + a;
			e[49408 + a] = t[193][a] }
	t[194] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷缈ょ咖缈ㄧ开缈楷缈刊缈茬看缈电慷缈风扛缈圭亢缈界烤缈胯€傝€囪€堣€夎€婅€庤€忚€戣€撹€氳€涜€濊€炶€熻€¤€ｈ€よ€€€€€€拌€茶€磋€硅€鸿€艰€捐亐鑱佽亜鑱呰亣鑱堣亯鑱庤亸鑱愯亼鑱撹仌鑱栬仐锟借仚鑱涜仠鑱濊仦鑱熻仩鑱¤仮鑱ｈ仱鑱ヨ仸鑱ц仺鑱伂鑱伄鑱伆鑱茶伋鑱磋伒鑱惰伔鑱歌伖鑱鸿伝鑱艰伣闅嗗瀯鎷㈤檱妤煎▌鎼傜瘬婕忛檵鑺﹀崲棰呭簮鐐夋幊鍗よ檹椴侀簱纰岄湶璺祩楣挎綖绂勫綍闄嗘埉椹村悤閾濅荆鏃呭饱灞＄紩铏戞隘寰嬬巼婊ょ豢宄︽寷瀛沪鍗典贡鎺犵暐鎶¤疆浼︿粦娌︾憾璁鸿悵铻虹綏閫婚敚绠╅瑁歌惤娲涢獑缁滃楹荤帥鐮佽殏椹獋鍢涘悧鍩嬩拱楹﹀崠杩堣剦鐬掗铔弧钄撴浖鎱㈡极锟�".split("");
	for(a = 0; a != t[194].length; ++a)
		if(t[194][a].charCodeAt(0) !== 65533) { r[t[194][a]] = 49664 + a;
			e[49664 + a] = t[194][a] }
	t[195] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑱捐倎鑲傝倕鑲堣倞鑲嶈値鑲忚倫鑲戣倰鑲旇倳鑲楄倷鑲炶偅鑲﹁偋鑲ㄨ偓鑲拌偝鑲佃偠鑲歌偣鑲昏儏鑳囪儓鑳夎儕鑳嬭儚鑳愯儜鑳掕儞鑳旇儠鑳樿儫鑳犺儮鑳ｈ儲鑳兊鑳疯児鑳昏兙鑳胯剙鑴佽剝鑴勮剠鑴囪剤鑴嬶拷鑴岃剷鑴楄剻鑴涜劀鑴濊劅鑴犺劇鑴㈣劊鑴よ劌鑴﹁劎鑴ㄨ劑鑴劔鑴劗鑴拌劤鑴磋劦鑴疯劰鑴鸿劵鑴艰劷鑴胯癌鑺掕尗鐩叉皳蹇欒幗鐚寘閿氭瘺鐭涢搯鍗寕鍐掑附璨岃锤涔堢帿鏋氭閰堕湁鐓ゆ病鐪夊獟闀佹瘡缇庢槯瀵愬濯氶棬闂蜂滑钀岃挋妾洘閿扮寷姊﹀瓱鐪啔闈＄硿杩疯皽寮ョ背绉樿娉岃湝瀵嗗箓妫夌湢缁靛啎鍏嶅媺濞╃紖闈㈣嫍鎻忕瀯钘愮娓哄簷濡欒攽鐏皯鎶跨毧鏁忔偗闂芥槑铻熼福閾悕鍛借艾鎽革拷".split("");
	for(a = 0; a != t[195].length; ++a)
		if(t[195][a].charCodeAt(0) !== 65533) { r[t[195][a]] = 49920 + a;
			e[49920 + a] = t[195][a] }
	t[196] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑵€鑵佽厒鑵冭厔鑵呰厙鑵夎厤鑵庤厪鑵掕厲鑵楄厴鑵涜厹鑵濊厼鑵熻叀鑵㈣叄鑵よ叇鑵ㄨ叒鑵叕鑵叢鑵宠叺鑵惰叿鑵歌唩鑶冭唲鑶呰唵鑶囪唹鑶嬭唽鑶嶈啂鑶愯啋鑶撹啍鑶曡問鑶楄啓鑶氳啚鑶熻啝鑶¤啟鑶よ啣锟借啩鑶╄啱鑶啳鑶啹鑶拌啽鑶茶喆鑶佃喍鑶疯喐鑶硅喖鑶借喚鑶胯噭鑷呰噰鑷堣噳鑷嬭噸鑷庤噺鑷愯噾鑷掕嚀鎽硅槕妯¤啘纾ㄦ懇榄旀姽鏈帿澧ㄩ粯娌紶瀵為檶璋嬬墴鏌愭媷鐗′憨濮嗘瘝澧撴毊骞曞嫙鎱曟湪鐩潶鐗х﹩鎷垮摢鍛愰挔閭ｅ绾虫皷涔冨ザ鑰愬鍗楃敺闅惧泭鎸犺剳鎭奸椆娣栧憿棣佸唴瀚╄兘濡湏鍊偿灏兼嫙浣犲尶鑵婚€嗘汉钄媹骞寸⒕鎾垫嵒蹇靛閰块笩灏挎崗鑱傚鍟晩闀嶆秴鎮ㄦ煚鐙炲嚌瀹侊拷".split("");
	for(a = 0; a != t[196].length; ++a)
		if(t[196][a].charCodeAt(0) !== 65533) { r[t[196][a]] = 50176 + a;
			e[50176 + a] = t[196][a] }
	t[197] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑷旇嚂鑷栬嚄鑷樿嚈鑷氳嚊鑷滆嚌鑷炶嚐鑷犺嚒鑷㈣嚖鑷ヨ嚘鑷ㄨ嚛鑷嚠鑷嚢鑷辫嚥鑷佃嚩鑷疯嚫鑷硅嚭鑷借嚳鑸冭垏鑸堣垑鑸婅垕鑸庤垙鑸戣垞鑸曡垨鑸楄垬鑸欒垰鑸濊垹鑸よ垾鑸﹁埀鑸╄埉鑸茶埡鑸艰埥鑸匡拷鑹€鑹佽墏鑹冭墔鑹嗚増鑹婅墝鑹嶈墡鑹愯墤鑹掕墦鑹旇墪鑹栬墬鑹欒墰鑹滆墲鑹炶墵鑹¤墷鑹ｈ墹鑹ヨ墻鑹ц墿鎷ф碁鐗涙壄閽航鑴撴祿鍐滃紕濂村姫鎬掑コ鏆栬檺鐤熸尓鎳︾朝璇哄摝娆ч弗娈磋棔鍛曞伓娌ゅ暘瓒寸埇甯曟€曠惗鎷嶆帓鐗屽緲婀冩淳鏀€娼樼洏纾愮浖鐣斿垽鍙涗箵搴炴梺鑰儢鎶涘拞鍒ㄧ偖琚嶈窇娉″懜鑳氬煿瑁磋禂闄厤浣╂矝鍠风泦鐮版姩鐑规編褰摤妫氱〖绡疯啫鏈嬮箯鎹х鍧爳闇规壒鎶妶鐞垫瘲锟�".split("");
	for(a = 0; a != t[197].length; ++a)
		if(t[197][a].charCodeAt(0) !== 65533) { r[t[197][a]] = 50432 + a;
			e[50432 + a] = t[197][a] }
	t[198] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑹壂鑹壄鑹辫壍鑹惰壏鑹歌壔鑹艰妧鑺佽妰鑺呰妴鑺囪妷鑺岃姁鑺撹姅鑺曡姈鑺氳姏鑺炶姞鑺㈣姡鑺ц姴鑺佃姸鑺鸿娀鑺艰娍鑻€鑻傝媰鑻呰媶鑻夎嫄鑻栬嫏鑻氳嫕鑻㈣嫥鑻ㄨ嫨鑻嫭鑻嫯鑻拌嫴鑻宠嫷鑻惰嫺锟借嫼鑻艰嫿鑻捐嬁鑼€鑼婅寢鑼嶈寪鑼掕寭鑼栬寴鑼欒対鑼炶専鑼犺尅鑼㈣專鑼よ尌鑼﹁尒鑼尞鑼拌尣鑼疯尰鑼藉暏鑴剧柌鐨尮鐥炲兓灞佽绡囧亸鐗囬獥椋樻紓鐡㈢エ鎾囩灔鎷奸璐搧鑱樹箳鍧嫻钀嶅钩鍑摱璇勫睆鍧℃臣棰囧﹩鐮撮瓌杩矔鍓栨墤閾轰粏鑾嗚憽鑿╄挷鍩旀湸鍦冩櫘娴﹁氨鏇濈€戞湡娆烘爾鎴氬涓冨噭婕嗘煉娌忓叾妫嬪姝х暒宕庤剱榻愭棗绁堢楠戣捣宀備篂浼佸惎濂戠爩鍣ㄦ皵杩勫純姹芥常璁帎锟�".split("");
	for(a = 0; a != t[198].length; ++a)
		if(t[198][a].charCodeAt(0) !== 65533) { r[t[198][a]] = 50688 + a;
			e[50688 + a] = t[198][a] }
	t[199] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑼捐尶鑽佽崅鑽勮崊鑽堣崐鑽嬭崒鑽嶈崕鑽撹崟鑽栬崡鑽樿崣鑽濊崲鑽拌嵄鑽茶嵆鑽磋嵉鑽惰嵐鑽鸿嵕鑽胯巰鑾佽巶鑾冭巹鑾囪巿鑾婅帇鑾岃帊鑾忚帎鑾戣帞鑾曡帠鑾楄帣鑾氳帩鑾熻帯鑾㈣帲鑾よ帴鑾﹁帶鑾幁鑾拷鑾幍鑾昏幘鑾胯弬鑿冭弰鑿嗚張鑿夎弸鑿嶈弾鑿愯彂鑿掕彄鑿曡彈鑿欒彋鑿涜彏鑿㈣彛鑿よ彟鑿ц彣鑿彫鑿伆娲界壍鎵﹂拵閾呭崈杩佺浠熻唉涔鹃粩閽遍挸鍓嶆綔閬ｆ祬璋村爲宓屾瑺姝夋灙鍛涜厰缇屽钄峰己鎶㈡﹪閿规暡鎮勬ˉ鐬т箶渚ㄥ阀闉樻挰缈樺抄淇忕獚鍒囪寗涓旀€獌閽︿镜浜茬Е鐞村嫟鑺规搾绂藉瘽娌侀潚杞绘阿鍊惧嵖娓呮搸鏅存鞍鎯呴》璇峰簡鐞肩┓绉嬩笜閭辩悆姹傚洑閰嬫硡瓒嬪尯铔嗘洸韬眻椹辨笭锟�".split("");
	for(a = 0; a != t[199].length; ++a)
		if(t[199][a].charCodeAt(0) !== 65533) { r[t[199][a]] = 50944 + a;
			e[50944 + a] = t[199][a] }
	t[200] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑿彲鑿宠彺鑿佃彾鑿疯徍鑿昏徏鑿捐徔钀€钀傝悈钀囪悎钀夎悐钀愯悞钀撹悢钀曡悥钀楄悪钀氳悰钀炶悷钀犺悺钀㈣悾钀╄惇钀惉钀惍钀惏钀茶惓钀磋惖钀惰惙钀硅惡钀昏惥钀胯憖钁佽憘钁冭憚钁呰憞钁堣憠锟借憡钁嬭憣钁嶈憥钁忚憪钁掕憮钁旇憰钁栬憳钁濊憺钁熻憼钁㈣懁钁ヨ懄钁ц懆钁懏钁懓钁茶懘钁疯懝钁昏懠鍙栧ǘ榫嬭叮鍘诲湀棰ф潈閱涙硥鍏ㄧ棅鎷崇姮鍒稿姖缂虹倲鐦稿嵈楣婃Ψ纭泙瑁欑兢鐒剁噧鍐夋煋鐡ゅ￥鏀樺毞璁╅ザ鎵扮粫鎯圭儹澹粊浜哄繊闊т换璁ゅ垉濡婄韩鎵斾粛鏃ユ垘鑼歌搲鑽ｈ瀺鐔旀憾瀹圭粧鍐楁弶鏌旇倝鑼硅爼鍎掑濡傝颈涔虫睗鍏ヨぅ杞槷钑婄憺閿愰棸娑﹁嫢寮辨拻娲掕惃鑵硟濉炶禌涓夊弫锟�".split("");
	for(a = 0; a != t[200].length; ++a)
		if(t[200][a].charCodeAt(0) !== 65533) { r[t[200][a]] = 51200 + a;
			e[51200 + a] = t[200][a] }
	t[201] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷钁借懢钁胯拃钂佽拑钂勮拝钂嗚拪钂嶈拸钂愯拺钂掕挀钂旇挄钂栬挊钂氳挍钂濊挒钂熻挔钂㈣挘钂よ挜钂﹁挧钂ㄨ挬钂挮钂挱钂挵钂辫挸钂佃挾钂疯捇钂艰捑钃€钃傝搩钃呰搯钃囪搱钃嬭搶钃庤搹钃掕摂钃曡摋锟借摌钃欒摎钃涜摐钃炶摗钃㈣摛钃ц摠钃╄摢钃摥钃摨钃辫摬钃宠摯钃佃摱钃疯摳钃硅摵钃昏摻钃捐攢钄佽攤浼炴暎妗戝棑涓ф悢楠氭壂瀚傜憻鑹叉订妫儳鑾庣爞鏉€鍒规矙绾卞偦鍟ョ厼绛涙檼鐝婅嫬鏉夊北鍒犵吔琛棯闄曟搮璧¤喅鍠勬睍鎵囩籍澧掍激鍟嗚祻鏅屼笂灏氳３姊㈡崕绋嶇儳鑺嶅嫼闊跺皯鍝ㄩ偟缁嶅ア璧婅泧鑸岃垗璧︽憚灏勬厬娑夌ぞ璁剧牱鐢冲懟浼歌韩娣卞缁呯娌堝濠剁敋鑲炬厧娓楀０鐢熺敟鐗插崌缁筹拷".split("");
	for(a = 0; a != t[201].length; ++a)
		if(t[201][a].charCodeAt(0) !== 65533) { r[t[201][a]] = 51456 + a;
			e[51456 + a] = t[201][a] }
	t[202] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷钄冭攧钄呰攩钄囪攬钄夎攰钄嬭攳钄庤攺钄愯敀钄旇敃钄栬敇钄欒敍钄滆敐钄炶敔钄㈣敚钄よ敟钄﹁敡钄ㄨ敥钄敪钄敮钄拌敱钄茶敵钄磋數钄惰斁钄胯晙钑佽晜钑勮晠钑嗚晣钑嬭晫钑嶈晭钑忚晲钑戣晵钑撹晹钑曪拷钑楄晿钑氳暃钑滆暆钑熻暊钑¤暍钑ｈ暐钑﹁暓钑╄暘钑暚钑暜钑暟钑辫暢钑佃暥钑疯暩钑艰暯钑胯杸钖佺渷鐩涘墿鑳滃湥甯堝け鐙柦婀胯瘲灏歌櫛鍗佺煶鎷炬椂浠€椋熻殌瀹炶瘑鍙茬煝浣垮睅椹跺寮忕ず澹笘鏌夸簨鎷獡閫濆娍鏄棞鍣€備粫渚嶉噴楗版皬甯傛亙瀹よ璇曟敹鎵嬮瀹堝鎺堝敭鍙楃槮鍏借敩鏋㈡⒊娈婃姃杈撳彅鑸掓窇鐤忎功璧庡鐔熻柉鏆戞洐缃茶渶榛嶉紶灞炴湳杩版爲鏉熸垗绔栧搴舵暟婕憋拷".split("");
	for(a = 0; a != t[202].length; ++a)
		if(t[202][a].charCodeAt(0) !== 65533) { r[t[202][a]] = 51712 + a;
			e[51712 + a] = t[202][a] }
	t[203] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷钖傝杻钖嗚枅钖夎枈钖嬭枌钖嶈枎钖愯枒钖掕枔钖旇枙钖栬枟钖樿枡钖氳枬钖炶枱钖犺枴钖㈣枺钖ヨ枽钖ц柀钖柆钖柋钖茶柍钖磋柕钖惰柛钖鸿柣钖艰柦钖捐柨钘€钘傝梼钘勮梾钘嗚棁钘堣棅钘嬭棇钘嶈棊钘戣棐锟借棓钘栬棗钘樿棛钘氳棝钘濊棡钘熻棤钘¤棦钘ｈ棩钘﹁棫钘ㄨ棯钘棳钘棶钘棸钘辫棽钘宠棿钘佃椂钘疯椄鎭曞埛鑰嶆憯琛扮敥甯呮爴鎷撮湝鍙岀埥璋佹按鐫＄◣鍚灛椤鸿垳璇寸鏈旂儊鏂挄鍢舵€濈鍙镐笣姝昏倖瀵哄棧鍥涗己浼奸ゲ宸虫澗鑰告€傞閫佸畫璁艰鎼滆墭鎿炲椊鑻忛叆淇楃礌閫熺矡鍍冲婧璇夎們閰歌挏绠楄櫧闅嬮殢缁ラ珦纰庡瞾绌楅亗闅х瀛欐崯绗嬭搼姊攩缂╃悙绱㈤攣鎵€濉屼粬瀹冨ス濉旓拷".split("");
	for(a = 0; a != t[203].length; ++a)
		if(t[203][a].charCodeAt(0) !== 65533) { r[t[203][a]] = 51968 + a;
			e[51968 + a] = t[203][a] }
	t[204] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷钘硅椇钘艰椊钘捐榾铇佽槀铇冭槃铇嗚槆铇堣槈铇婅構铇岃槏铇庤槒铇愯槖铇撹様铇曡槜铇樿槞铇氳槢铇滆槤铇炶槦铇犺槨铇㈣槪铇よ槬铇﹁槰铇槴铇槶铇槸铇拌槺铇茶槼铇磋樀铇惰樂铇硅樅铇昏樈铇捐樋铏€锟借檨铏傝檭铏勮檯铏嗚檱铏堣檳铏婅檵铏岃檼铏撹檿铏栬櫁铏樿櫃铏涜櫆铏濊櫉铏犺櫋铏ｈ櫎铏ヨ櫐铏ц櫒铏╄櫔鐛尀韫嬭笍鑳庤嫈鎶彴娉伴厼澶€佹卑鍧嶆憡璐槴婊╁潧妾€鐥版江璋皥鍧︽琚掔⒊鎺㈠徆鐐堡濉樻惇鍫傛　鑶涘攼绯栧€樿汉娣岃稛鐑帍娑涙粩缁﹁悇妗冮€冩窐闄惰濂楃壒钘よ吘鐤艰獖姊墧韪㈤攽鎻愰韫勫暭浣撴浛鍤忔儠娑曞墐灞夊ぉ娣诲～鐢扮敎鎭垟鑵嗘寫鏉¤竣鐪鸿烦璐撮搧甯栧巺鍚儍锟�".split("");
	for(a = 0; a != t[204].length; ++a)
		if(t[204][a].charCodeAt(0) !== 65533) { r[t[204][a]] = 52224 + a;
			e[52224 + a] = t[204][a] }
	t[205] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷铏櫙铏拌櫜铏宠櫞铏佃櫠铏疯櫢铓冭殑铓呰殕铓囪殘铓夎殠铓忚殣铓戣殥铓旇殩铓楄殬铓欒殮铓涜殲铓熻殸铓¤殺铓ヨ殾铓毉铓毑铓宠毞铓歌毠铓昏毤铓借毦铓胯泚铔傝泝铔呰泩铔岃泹铔掕洆铔曡洊铔楄洑铔滐拷铔濊洜铔¤洟铔ｈ洢铔﹁洤铔ㄨ洩铔洭铔浀铔惰浄铔鸿浕铔艰浗铔胯渷铚勮渽铚嗚湅铚岃湈铚忚湊铚戣湐铚栨眬寤峰仠浜涵鎸鸿墖閫氭閰灣鍚岄摐褰ょ妗舵崊绛掔粺鐥涘伔鎶曞ご閫忓嚫绉冪獊鍥惧緬閫旀秱灞犲湡鍚愬厰婀嶅洟鎺ㄩ鑵胯湑瑜€€鍚炲悲鑷€鎷栨墭鑴遍傅闄€椹┘妞Ε鎷撳斁鎸栧搰铔欐醇濞冪摝琚滄澶栬睂寮咕鐜╅〗涓哥兎瀹岀鎸芥櫄鐨栨儖瀹涘涓囪厱姹帇浜℃瀴缃戝線鏃烘湜蹇樺濞侊拷".split("");
	for(a = 0; a != t[205].length; ++a)
		if(t[205][a].charCodeAt(0) !== 65533) { r[t[205][a]] = 52480 + a;
			e[52480 + a] = t[205][a] }
	t[206] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷铚欒湜铚濊湡铚犺湦铚﹁湩铚ㄨ湭铚湰铚湳铚拌湶铚宠湹铚惰湼铚硅満铚艰溄铦€铦佽潅铦冭潉铦呰潌铦婅潒铦嶈潖铦愯潙铦掕潝铦曡潠铦樿潥铦涜潨铦濊潪铦熻潯铦㈣潶铦ц潹铦╄潽铦潿铦澂铦辫澆铦宠澋锟借澐铦歌澒铦鸿澘铻€铻佽瀯铻嗚瀲铻夎瀶铻岃瀻铻忚瀽铻戣瀿铻旇灂铻栬灅铻欒灇铻涜灉铻濊灋铻犺灐铻㈣灒铻ゅ穽寰嵄闊﹁繚妗呭洿鍞儫涓烘綅缁磋媷钀庡浼熶吉灏剧含鏈敋鍛崇晱鑳冨杺榄忎綅娓皳灏夋叞鍗槦娓╄殜鏂囬椈绾瑰惢绋崇磰闂棥缈佺摦鎸濊湕娑＄獫鎴戞枴鍗ф彙娌冨帆鍛滈挩涔屾薄璇眿鏃犺姕姊у惥鍚存瘚姝︿簲鎹傚崍鑸炰紞渚潪鎴婇浘鏅ょ墿鍕垮姟鎮熻鏄旂啓鏋愯タ纭掔熃鏅板樆鍚搁敗鐗猴拷".split("");
	for(a = 0; a != t[206].length; ++a)
		if(t[206][a].charCodeAt(0) !== 65533) { r[t[206][a]] = 52736 + a;
			e[52736 + a] = t[206][a] }
	t[207] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷铻ヨ灕铻ц灘铻灝铻拌灡铻茶灤铻惰灧铻歌灩铻昏灱铻捐灴锜佽焸锜冭焺锜呰焽锜堣焿锜岃煃锜庤煆锜愯煍锜曡煐锜楄煒锜欒煔锜滆煗锜炶煙锜¤煝锜ｈ煠锜﹁煣锜ㄨ煩锜煬锜煰锜拌煴锜茶煶锜磋煹锜惰煼锜革拷锜鸿熁锜艰熃锜胯爛锠佽爞锠勮爡锠嗚爣锠堣爥锠嬭爩锠嶈爭锠忚爯锠戣爳锠旇牀锠樿牂锠氳牅锠濊牉锠熻牋锠ｇ█鎭笇鎮夎啙澶曟儨鐔勭儻婧睈鐘€妾勮甯範濯冲枩閾ｆ礂绯婚殭鎴忕粏鐬庤櫨鍖ｉ湠杈栨殗宄′緺鐙笅鍘﹀鍚撴巰閿ㄥ厛浠欓矞绾ゅ捀璐よ鑸烽棽娑庡鸡瀚屾樉闄╃幇鐚幙鑵洪缇″闄烽檺绾跨浉鍘㈤暥棣欑瑗勬箻涔＄繑绁ヨ鎯冲搷浜」宸锋鍍忓悜璞¤惂纭濋渼鍓婂摦鍤ｉ攢娑堝娣嗘檽锟�".split("");
	for(a = 0; a != t[207].length; ++a)
		if(t[207][a].charCodeAt(0) !== 65533) { r[t[207][a]] = 52992 + a;
			e[52992 + a] = t[207][a] }
	t[208] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锠よ牓锠﹁牕锠ㄨ牘锠牜锠牠锠牤锠拌牨锠宠牬锠佃牰锠疯牳锠鸿牷锠借牼锠胯琛傝琛嗚琛堣琛婅琛庤琛愯琛掕琛曡琛樿琛涜琛濊琛熻琛﹁¨琛…琛”琛宠〈琛佃《琛歌」琛猴拷琛昏〖琚€琚冭琚囪琚婅琚庤琚愯琚撹琚曡琚樿琚氳琚濊琚熻琚¤ⅲ琚ヨⅵ琚цⅷ琚╄ⅹ灏忓瓭鏍¤倴鍟哥瑧鏁堟浜涙瓏铦庨瀷鍗忔専鎼洪偑鏂滆儊璋愬啓姊板嵏锜规噲娉勬郴璋㈠睉钖姱閿屾杈涙柊蹇诲績淇¤鏄熻叆鐚╂兒鍏村垜鍨嬪舰閭㈣閱掑垢鏉忔€у鍏勫嚩鑳稿寛姹归泟鐔婁紤淇緸鏈藉梾閿堢琚栫唬澧熸垖闇€铏氬槝椤诲緪璁歌搫閰楀彊鏃簭鐣滄仱绲┛缁画杞╁枾瀹ｆ偓鏃嬬巹锟�".split("");
	for(a = 0; a != t[208].length; ++a)
		if(t[208][a].charCodeAt(0) !== 65533) { r[t[208][a]] = 53248 + a;
			e[53248 + a] = t[208][a] }
	t[209] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷琚琚琚茶⒊琚磋⒌琚惰⒏琚硅⒑琚昏⒔琚捐⒖瑁€瑁冭瑁囪瑁婅瑁岃瑁忚瑁戣瑁栬瑁氳瑁滆瑁炶　瑁¤＆瑁ц）瑁＋瑁－瑁／瑁茶５瑁惰７瑁鸿；瑁借？瑜€瑜佽瑜勮瑜嗚瑜堬拷瑜夎瑜岃瑜庤瑜戣瑜曡瑜楄瑜滆瑜炶瑜犺あ瑜ｈい瑜﹁ぇ瑜ㄨぉ瑜き瑜く瑜辫げ瑜宠さ瑜烽€夌櫍鐪╃粴闈磋枦瀛︾┐闆鍕嬬啅寰棳璇㈠椹贰娈夋睕璁閫婅繀鍘嬫娂楦﹂腑鍛€涓娊鐗欒殰宕栬娑泤鍝戜簹璁剁剦鍜介槈鐑熸饭鐩愪弗鐮旇湌宀╁欢瑷€棰滈槑鐐庢部濂勬帺鐪艰婕旇壋鍫扮嚂鍘岀牃闆佸攣褰︾劙瀹磋皻楠屾畠澶腐绉ф潹鎵蒋鐤＄緤娲嬮槼姘т话鐥掑吇鏍锋季閭€鑵板鐟讹拷".split("");
	for(a = 0; a != t[209].length; ++a)
		if(t[209][a].charCodeAt(0) !== 65533) { r[t[209][a]] = 53504 + a;
			e[53504 + a] = t[209][a] }
	t[210] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷瑜歌す瑜鸿せ瑜艰そ瑜捐た瑗€瑗傝瑗呰瑗囪瑗夎瑗嬭瑗嶈瑗忚瑗戣瑗撹瑗曡瑗楄瑗欒瑗涜瑗濊瑗¤ア瑗ｈイ瑗ヨェ瑗ㄨォ瑗カ瑗キ瑗ク瑗拌ケ瑗茶コ瑗磋サ瑗惰シ瑗歌ス瑗鸿ゼ锟借ソ瑗捐瑕傝瑕呰瑕堣瑕婅瑕岃瑕庤瑕愯瑕掕瑕旇瑕栬瑕樿瑕氳瑕滆瑕炶瑕犺Α鎽囧哀閬ョ獞璋ｅ鍜垁鑽鑰€妞板檸鑰剁埛閲庡喍涔熼〉鎺栦笟鍙舵洺鑵嬪娑蹭竴澹瑰尰鎻栭摫渚濅紛琛ｉ澶烽仐绉讳华鑳扮枒娌傚疁濮ㄥ綕妞呰殎鍊氬凡涔欑煟浠ヨ壓鎶戞槗閭戝惫浜垮焦鑷嗛€歌倓鐤害瑁旀剰姣呭繂涔夌泭婧㈣璁皧璇戝紓缈肩繉缁庤尩鑽洜娈烽煶闃村Щ鍚熼摱娣瘏楗肮寮曢殣锟�".split("");
	for(a = 0; a != t[210].length; ++a)
		if(t[210][a].charCodeAt(0) !== 65533) { r[t[210][a]] = 53760 + a;
			e[53760 + a] = t[210][a] }
	t[211] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷瑕㈣Γ瑕よΕ瑕﹁Η瑕ㄨΙ瑕Λ瑕Ν瑕Ο瑕拌Ρ瑕茶Τ瑕磋Φ瑕惰Ψ瑕歌瑕鸿瑕艰瑕捐瑙€瑙冭瑙撹瑙曡瑙樿瑙涜瑙熻瑙¤Б瑙よЁ瑙ㄨЗ瑙К瑙М瑙拌П瑙茶Т瑙佃Ф瑙疯Ц瑙硅Ш锟借Щ瑙艰Ы瑙捐Э瑷佽▊瑷冭▌瑷呰▎瑷堣▔瑷婅▼瑷岃◢瑷庤◤瑷愯☉瑷掕〒瑷旇〞瑷栬瑷樿瑷氳瑷滆鍗拌嫳妯卞┐楣板簲缂ㄨ幑钀よ惀鑽ц潎杩庤耽鐩堝奖棰栫‖鏄犲摕鎷ヤ剑鑷冪棃搴搁泹韪婅浌鍜忔吵娑屾案鎭垮媷鐢ㄥ菇浼樻偁蹇у挨鐢遍偖閾€鐘规补娓搁厜鏈夊弸鍙充綉閲夎鍙堝辜杩傛筏浜庣泜姒嗚櫈鎰氳垎浣欎繛閫鹃奔鎰夋笣娓旈殔浜堝ū闆ㄤ笌灞跨瀹囪缇界帀鍩熻妺閮佸悂閬囧柣宄尽鎰堟鐙辫偛瑾夛拷".split("");
	for(a = 0; a != t[211].length; ++a)
		if(t[211][a].charCodeAt(0) !== 65533) { r[t[211][a]] = 54016 + a;
			e[54016 + a] = t[211][a] }
	t[212] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷瑷炶瑷犺ā瑷㈣ǎ瑷よē瑷﹁ě瑷ㄨī瑷ǐ瑷ō瑷ǒ瑷拌ū瑷茶ǔ瑷磋ǖ瑷惰ǚ瑷歌ü瑷鸿ɑ瑷艰ń瑷胯﹢瑭佽﹤瑭冭﹦瑭呰﹩瑭囪瑭婅瑭岃瑭庤瑭愯瑭掕瑭旇瑭栬瑭樿瑭氳瑭滆瑭烇拷瑭熻瑭¤瑭ｈ─瑭ヨ│瑭ц┄瑭╄┆瑭┈瑭┊瑭┌瑭辫┎瑭宠┐瑭佃┒瑭疯└瑭鸿┗瑭艰┙瑭捐┛瑾€娴村瘬瑁曢璞┉楦虫笂鍐ゅ厓鍨ｈ鍘熸彺杈曞洯鍛樺渾鐚挎簮缂樿繙鑻戞効鎬ㄩ櫌鏇扮害瓒婅穬閽ュ渤绮ゆ湀鎮﹂槄鑰樹簯閮у寑闄ㄥ厑杩愯暣閰濇檿闊靛瓡鍖濈牳鏉傛牻鍝夌伨瀹拌浇鍐嶅湪鍜辨敀鏆傝禐璧冭剰钁伃绯熷嚳钘绘灒鏃╂尽铓よ簛鍣€犵殏鐏剁嚗璐ｆ嫨鍒欐辰璐兼€庡鎲庢浘璧犳墡鍠虫福鏈涧锟�".split("");
	for(a = 0; a != t[212].length; ++a)
		if(t[212][a].charCodeAt(0) !== 65533) { r[t[212][a]] = 54272 + a;
			e[54272 + a] = t[212][a] }
	t[213] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷瑾佽獋瑾冭獎瑾呰獑瑾囪獔瑾嬭獙瑾嶈獛瑾忚獝瑾戣獟瑾旇獣瑾栬獥瑾樿獧瑾氳獩瑾滆獫瑾炶獰瑾犺瑾㈣瑾よ瑾﹁瑾ㄨ瑾瑾瑾瑾拌瑾茶瑾磋瑾惰瑾歌瑾鸿瑾艰瑾捐璜€璜佽珎锟借珒璜勮珔璜嗚珖璜堣珘璜婅珛璜岃珝璜庤珡璜愯珣璜掕珦璜旇珪璜栬珬璜樿珯璜氳珱璜滆珴璜炶珶璜犺璜㈣閾￠椄鐪ㄦ爡姒ㄥ拫涔嶇偢璇堟憳鏂嬪畢绐勫€哄鐬绘瑭圭矘娌剧洀鏂╄緱宕睍铇告爤鍗犳垬绔欐箾缁芥绔犲桨婕冲紶鎺屾定鏉栦笀甯愯处浠楄儉鐦撮殰鎷涙槶鎵炬布璧电収缃╁厗鑲囧彫閬姌鍝茶洶杈欒€呴敆钄楄繖娴欑弽鏂熺湡鐢勭牕鑷昏礊閽堜睛鏋曠柟璇婇渿鎸晣闃佃捀鎸ｇ潄寰佺嫲浜夋€旀暣鎷鏀匡拷".split("");
	for(a = 0; a != t[213].length; ++a)
		if(t[213][a].charCodeAt(0) !== 65533) { r[t[213][a]] = 54528 + a;
			e[54528 + a] = t[213][a] }
	t[214] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷璜よ璜﹁璜ㄨ璜璜璜璜拌璜茶璜磋璜惰璜歌璜鸿璜艰璜捐璎€璎佽瑐璎冭瑒璎呰瑔璎堣瑝璎婅瑡璎岃瑣璎庤瑥璎愯瑧璎掕瑩璎旇瑫璎栬瑮璎樿瑱璎氳瑳璎滆瑵璎炶瑹璎犺璎㈣锟借璎ヨ璎ㄨ璎璎璎璎拌璎茶璎磋璎惰璎歌璎鸿璎艰璎捐璀€璀佽瓊璀冭瓌璀呭抚鐥囬儜璇佽姖鏋濇敮鍚辫湗鐭ヨ偄鑴傛眮涔嬬粐鑱岀洿妞嶆畺鎵у€间緞鍧€鎸囨瓒惧彧鏃ㄧ焊蹇楁寶鎺疯嚦鑷寸疆甯滃硻鍒舵櫤绉╃璐ㄧ倷鐥旀粸娌荤獟涓泤蹇犻挓琛风粓绉嶈偪閲嶄徊浼楄垷鍛ㄥ窞娲茶瘜绮ヨ酱鑲樺笟鍜掔毐瀹欐樇楠ょ彔鏍洓鏈辩尓璇歌瘺閫愮鐑涚叜鎷勭灘鍢变富钁楁煴鍔╄泙璐摳绛戯拷".split("");
	for(a = 0; a != t[214].length; ++a)
		if(t[214][a].charCodeAt(0) !== 65533) { r[t[214][a]] = 54784 + a;
			e[54784 + a] = t[214][a] }
	t[215] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷璀嗚瓏璀堣瓑璀婅瓔璀岃瓖璀庤瓘璀愯瓚璀掕瓝璀旇瓡璀栬瓧璀樿瓩璀氳瓫璀滆瓭璀炶瓱璀犺璀㈣璀よ璀ц璀╄璀璀璀拌璀茶璀磋璀惰璀歌璀鸿璀艰璀捐璁€璁佽畟璁冭畡璁呰畣锟借畤璁堣畨璁婅畫璁岃畭璁庤畯璁愯畱璁掕畵璁旇畷璁栬畻璁樿畽璁氳疀璁滆疂璁炶疅璁璁昏瘒璇愯璋夎盀浣忔敞绁濋┗鎶撶埅鎷戒笓鐮栬浆鎾拌禋绡嗘々搴勮濡嗘挒澹姸妞庨敟杩借禈鍧犵紑璋嗗噯鎹夋嫏鍗撴鐞㈣寔閰屽晞鐫€鐏兼祳鍏瑰挩璧勫Э婊嬫穭瀛滅传浠旂苯婊撳瓙鑷笉瀛楅瑑妫曡釜瀹楃患鎬荤旱閭硅蛋濂忔弽绉熻冻鍗掓棌绁栬瘏闃荤粍閽荤簜鍢撮唹鏈€缃皧閬垫槰宸︿綈鏌炲仛浣滃潗搴э拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[215].length; ++a)
		if(t[215][a].charCodeAt(0) !== 65533) { r[t[215][a]] = 55040 + a;
			e[55040 + a] = t[215][a] }
	t[216] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷璋歌肮璋鸿盎璋艰敖璋捐翱璞€璞傝眱璞勮眳璞堣眾璞嬭睄璞庤睆璞愯睉璞掕睋璞旇睎璞楄睒璞欒睕璞滆睗璞炶睙璞犺保璞よ饱璞﹁抱璞ㄨ暴璞杯璞悲璞拌北璞茶贝璞佃倍璞疯被璞艰苯璞捐笨璨€璨佽矁璨勮矄璨囷拷璨堣矉璨嶈矌璨忚矏璨戣矑璨撹矔璨栬矖璨欒矚璨涜矞璨濊矠璨熻矤璨¤并璨ｈ菠璨ヨ拨璨ц波璨╄勃璨铂璨簫涓屽厐涓愬豢鍗呬笗浜樹笧楝插鍣╀辅绂轰缚鍖曚箛澶埢鍗皭鍥熻儰棣楁瘬鐫鹃紬涓朵簾榧愪箿涔╀簱鑺堝瓫鍟槒浠勫帊鍘濆帲鍘ュ幃闈ヨ禎鍖氬彽鍖﹀尞鍖捐禍鍗﹀崳鍒傚垐鍒庡埈鍒冲埧鍓€鍓屽墳鍓″墱钂壗鍔傚妬鍔愬姄鍐傜綌浜讳粌浠変粋浠ㄤ弧浠粸浼涗怀浼饯浠典讥浼т級浼綖浣ф敻浣氫綕锟�".split("");
	for(a = 0; a != t[216].length; ++a)
		if(t[216][a].charCodeAt(0) !== 65533) { r[t[216][a]] = 55296 + a;
			e[55296 + a] = t[216][a] }
	t[217] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷璨帛璨拌脖璨茶渤璨磋驳璨惰卜璨歌补璨鸿不璨艰步璨捐部璩€璩佽硞璩冭硠璩呰硢璩囪硤璩夎硦璩嬭硨璩嶈硯璩忚硱璩戣硳璩撹硵璩曡硸璩楄硺璩欒硽璩涜硿璩濊碁璩熻碃璩¤尝璩ｈ长璩ヨ肠璩ц敞璩╄唱璩超锟借抄璩朝璩拌潮璩茶吵璩磋车璩惰撤璩歌彻璩鸿郴璩艰辰璩捐晨璐€璐佽磦璐冭磩璐呰磫璐囪磮璐夎磰璐嬭磳璐嶄綗浣椾疾浼戒蕉浣翠緫渚変緝渚忎骄浣讳惊浣间粳渚斾喀淇ㄤ开淇呬繗淇ｄ繙淇戜繜淇稿€╁亴淇冲€€忓€€烤鍊滃€屽€ュ€ㄥ伨鍋冨仌鍋堝亷鍋伝鍌ュ偋鍌╁偤鍍栧剢鍍儸鍍﹀儺鍎囧剫浠濇敖浣樹渐淇庨緺姹嗙贝鍏方榛夐鍐佸鍕瑰實瑷囧寪鍑鍏曚籂鍏栦撼琛ⅳ浜佃剶瑁掔瀣磋爟缇稿啱鍐卞喗鍐硷拷".split("");
	for(a = 0; a != t[217].length; ++a)
		if(t[217][a].charCodeAt(0) !== 65533) { r[t[217][a]] = 55552 + a;
			e[55552 + a] = t[217][a] }
	t[218] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷璐庤磸璐愯磻璐掕磽璐旇磿璐栬礂璐樿礄璐氳礇璐滆礌璧戣祾璧楄禑璧ヨ胆璧╄氮璧诞璧当璧茶蹈璧硅岛璧昏导璧借稻璧胯秬瓒傝秲瓒嗚秶瓒堣秹瓒岃秿瓒庤稄瓒愯稈瓒撹稌瓒栬稐瓒樿稒瓒氳稕瓒滆稘瓒炶稜瓒★拷瓒㈣钉瓒ヨ鼎瓒ц定瓒╄丢瓒冬瓒懂瓒栋瓒茶抖瓒疯豆瓒昏督璺€璺佽穫璺呰穱璺堣穳璺婅穽璺愯窉璺撹窋鍑囧問鍐㈠啣璁犺璁ц璁磋璁疯瘋璇冭瘚璇忚瘞璇掕瘬璇旇瘱璇樿瘷璇滆療璇犺璇ㄨ璇璇宠璇硅璇胯皜璋傝皠璋囪皩璋忚皯璋掕皵璋曡皷璋欒皼璋樿皾璋熻盃璋¤哎璋ц蔼璋爱璋安璋宠暗璋跺崺鍗洪槤闃㈤槨闃遍槳闃介樇闄傞檳闄旈櫉闄ч櫖闄查櫞闅堥殟闅楅毎閭楅倹閭濋倷閭偂閭撮偝閭堕偤锟�".split("");
	for(a = 0; a != t[218].length; ++a)
		if(t[218][a].charCodeAt(0) !== 65533) { r[t[218][a]] = 55808 + a;
			e[55808 + a] = t[218][a] }
	t[219] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷璺曡窐璺欒窚璺犺贰璺㈣伐璺﹁阀璺╄翻璺钒璺辫凡璺磋范璺艰肪璺胯竴韪佽競韪冭竸韪嗚竾韪堣笅韪嶈笌韪愯笐韪掕笓韪曡笘韪楄笜韪欒笟韪涜笢韪犺浮韪よ弗韪﹁抚韪ㄨ斧韪赴韪茶赋韪磋付韪疯父韪昏讣韪撅拷韪胯箖韫呰箚韫岃箥韫庤箯韫愯箵韫旇箷韫栬箺韫樿箽韫涜箿韫濊篂韫熻範韫¤耿韫ｈ工韫ヨ恭韫ㄨ躬韫巩韫遍偢閭伴儚閮呴偩閮愰儎閮囬儞閮﹂儮閮滈儣閮涢儷閮兙閯勯劉閯為劊閯遍劘閯归厓閰嗗垗濂傚姠鍔姯鍔惧摽鍕愬嫋鍕板彑鐕煃寤村嚨鍑奸鍘跺紒鐣氬矾鍧屽灘鍨″【澧煎澹戝湬鍦湭鍦冲湽鍦湳鍧滃溁鍧傚潻鍨呭潾鍨嗗澕鍧诲潹鍧澏鍧冲灜鍨ゅ瀸鍨插煆鍨у灤鍨撳灎鍩曞煒鍩氬煓鍩掑灨鍩村煰鍩稿煠鍩濓拷".split("");
	for(a = 0; a != t[219].length; ++a)
		if(t[219][a].charCodeAt(0) !== 65533) { r[t[219][a]] = 56064 + a;
			e[56064 + a] = t[219][a] }
	t[220] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷韫宠沟韫疯垢韫硅购韫昏菇韫捐簚韬傝簝韬勮簡韬堣簤韬婅簨韬岃簫韬庤簯韬掕簱韬曡簴韬楄簶韬欒簹韬涜簼韬熻籂韬¤孩韬ｈ氦韬ヨ害韬ц酣韬╄邯韬寒韬拌罕韬宠捍韬佃憾韬疯焊韬硅夯韬艰航韬捐嚎杌€杌佽粋锟借粌杌勮粎杌嗚粐杌堣粔杌婅粙杌岃粛杌忚粣杌戣粧杌撹粩杌曡粬杌楄粯杌欒粴杌涜粶杌濊粸杌熻粻杌¤虎杌ｈ护鍫嬪爫鍩藉煭鍫€鍫炲牂濉勫牋濉ュ‖澧佸澧氬棣ㄩ紮鎳胯壒鑹借壙鑺忚妸鑺ㄨ妱鑺庤姂鑺楄姍鑺姼鑺捐姲鑻堣媻鑻ｈ姌鑺疯姰鑻嬭媽鑻佽姪鑺磋姟鑺姛鑻勮嫀鑺よ嫛鑼夎嫹鑻よ審鑼囪嫓鑻磋嫆鑻樿寣鑻昏嫇鑼戣寶鑼嗚寯鑼曡嫚鑻曡寽鑽戣崨鑽滆寛鑾掕尲鑼磋尡鑾涜崬鑼崗鑽囪崈鑽熻崁鑼楄崰鑼尯鑼宠崷鑽ワ拷".split("");
	for(a = 0; a != t[220].length; ++a)
		if(t[220][a].charCodeAt(0) !== 65533) { r[t[220][a]] = 56320 + a;
			e[56320 + a] = t[220][a] }
	t[221] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷杌ヨ沪杌ц花杌╄华杌滑杌划杌话杌辫徊杌宠淮杌佃欢杌疯桓杌硅缓杌昏患杌借痪杌胯紑杓佽紓杓冭紕杓呰紗杓囪紙杓夎紛杓嬭紝杓嶈紟杓忚紣杓戣紥杓撹紨杓曡紪杓楄紭杓欒細杓涜紲杓濊紴杓熻紶杓¤饥杓ｏ拷杓よ讥杓﹁姬杓ㄨ缉杓极杓辑杓集杓拌急杓茶汲杓磋嫉杓惰挤杓歌脊杓鸿蓟杓艰冀杓捐伎杞€杞佽絺杞冭絼鑽ㄨ寷鑽╄崿鑽嵀鑽幇鑽歌幊鑾磋帬鑾帗鑾滆巺鑽艰幎鑾╄嵔鑾歌嵒鑾樿帪鑾ㄨ幒鑾艰弫钀佽彞鑿樺爣钀樿悑鑿濊徑鑿栬悳钀歌悜钀嗚彅鑿熻悘钀冭徃鑿硅彧鑿呰弨钀﹁彴鑿¤憸钁戣憵钁欒懗钂囪拡钁鸿拤钁歌惣钁嗚懇钁惰拰钂庤惐钁搧钃嶈搻钃﹁捊钃撹搳钂胯捄钃犺挕钂硅挻钂楄摜钃ｈ攲鐢嶈敻钃拌敼钄熻敽锟�".split("");
	for(a = 0; a != t[221].length; ++a)
		if(t[221][a].charCodeAt(0) !== 65533) { r[t[221][a]] = 56576 + a;
			e[56576 + a] = t[221][a] }
	t[222] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷杞呰絾杞囪綀杞夎綂杞嬭綄杞嶈綆杞忚綈杞戣綊杞撹綌杞曡綎杞楄綐杞欒綒杞涜綔杞濊綖杞熻綘杞¤舰杞ｈ饯杞ヨ姜杈€杈岃緬杈濊緺杈¤劲杈よ茎杈﹁晶杈粳杈井杈静杈宠敬杈佃痉杈歌竞杈昏炯杈胯縺杩冭繂锟借繅杩婅繈杩岃繊杩忚繏杩栬織杩氳繝杩¤浚杩ц楷杩勘杩茶看杩佃慷杩鸿炕杩艰烤杩块€囬€堥€岄€庨€撻€曢€樿晼钄昏摽钃艰暀钑堣暔钑よ暈钑虹灑钑冭暡钑昏枻钖ㄨ枃钖忚暪钖枩钖呰柟钖疯柊钘撹梺钘滆椏铇ц槄铇╄槚铇煎痪寮堝ぜ濂佽€峰濂氬鍖忓阿灏ュ艾灏存墝鎵姛鎶绘媻鎷氭嫍鎷將鎷舵尮鎹嬫崈鎺彾鎹辨嵑鎺庢幋鎹幀鎺婃崺鎺幖鎻叉徃鎻犳徔鎻勬彏鎻庢憭鎻嗘幘鎽呮憗鎼嬫悰鎼犳悓鎼︽悺鎽炴拕鎽挅锟�".split("");
	for(a = 0; a != t[222].length; ++a)
		if(t[222][a].charCodeAt(0) !== 65533) { r[t[222][a]] = 56832 + a;
			e[56832 + a] = t[222][a] }
	t[223] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閫欓€滈€ｉ€ら€ラ€ч€ㄩ€╅€€€€伴€遍€查€抽€撮€烽€归€洪€介€块亐閬冮亝閬嗛亪閬夐亰閬嬮亴閬庨仈閬曢仏閬欓仛閬滈仢閬為仧閬犻仭閬ら仸閬ч仼閬伀閬伅閬伴伇閬查伋閬堕伔閬搁伖閬洪伝閬奸伨閭侊拷閭勯倕閭嗛倗閭夐倞閭岄倣閭庨倧閭愰倰閭旈倴閭橀倸閭滈倿閭熼偁閭ら偉閭ч偍閭╅偒閭偛閭烽偧閭介偪閮€鎽烘挿鎾告挋鎾烘搥鎿愭摋鎿ゆ摙鏀夋敟鏀紜蹇掔敊寮戝崯鍙卞徑鍙╁彣鍙诲悞鍚栧悊鍛嬪憭鍛撳憯鍛栧憙鍚″憲鍛欏悾鍚插拏鍜斿懛鍛卞懁鍜氬挍鍜勫懚鍛﹀挐鍝愬挱鍝傚挻鍝掑挧鍜﹀摀鍝斿懖鍜ｅ摃鍜诲捒鍝屽摍鍝氬摐鍜╁挭鍜ゅ摑鍝忓摓鍞涘摟鍞犲摻鍞斿摮鍞㈠敚鍞忓攽鍞у敧鍟у枏鍠靛晧鍟晛鍟曞斂鍟愬敿锟�".split("");
	for(a = 0; a != t[223].length; ++a)
		if(t[223][a].charCodeAt(0) !== 65533) { r[t[223][a]] = 57088 + a;
			e[57088 + a] = t[223][a] }
	t[224] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閮傞儍閮嗛儓閮夐儖閮岄儘閮掗償閮曢儢閮橀儥閮氶優閮熼儬閮ｉ儰閮ラ儵閮儸閮儼閮遍儾閮抽兊閮堕兎閮归兒閮婚兗閮块剙閯侀剝閯呴剢閯囬剤閯夐剨閯嬮剬閯嶉剮閯忛剱閯戦剴閯撻剶閯曢剸閯楅剺閯氶剾閯滐拷閯濋劅閯犻劇閯ら劌閯﹂劎閯ㄩ劑閯劔閯劖閯劙閯查劤閯撮劦閯堕劮閯搁労閯婚劶閯介劸閯块厐閰侀厒閰勫敺鍟栧暤鍟跺暦鍞冲敯鍟滃枊鍡掑杻鍠卞柟鍠堝杹鍠熷暰鍡栧枒鍟诲棢鍠藉柧鍠斿枡鍡椃鍡夊槦鍡戝棲鍡棓鍡﹀棟鍡勫棷鍡ュ棽鍡冲棇鍡嶅棬鍡靛棨杈斿槥鍢堝槍鍢佸槫鍢ｅ椌鍢€鍢у槶鍣樺樄鍣楀槵鍣嶅櫌鍣欏櫆鍣屽檾鍤嗗櫎鍣卞櫕鍣诲櫦鍤呭殦鍤洈鍥楀洕鍥″浀鍥浌鍥垮渼鍦婂湁鍦滃笍甯欏笖甯戝副甯诲讣锟�".split("");
	for(a = 0; a != t[224].length; ++a)
		if(t[224][a].charCodeAt(0) !== 65533) { r[t[224][a]] = 57344 + a;
			e[57344 + a] = t[224][a] }
	t[225] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閰呴厙閰堥厬閰撻厰閰曢厲閰橀厵閰涢厹閰熼厾閰﹂収閰ㄩ叓閰叧閰洪吇閰奸唨閱侀唫閱冮唲閱嗛唸閱婇啂閱忛啌閱旈啎閱栭啑閱橀啓閱滈啙閱為啛閱犻啞閱ら啣閱﹂啩閱ㄩ啯閱啲閱伴啽閱查喅閱堕喎閱搁喒閱伙拷閱奸喗閱鹃喛閲€閲侀噦閲冮噭閲呴噯閲堥噵閲愰噿閲撻嚁閲曢嚃閲楅嚇閲欓嚉閲涢嚌閲為嚐閲犻嚒閲㈤嚕閲ら嚗甯峰箘骞斿箾骞炲埂宀屽焙宀嶅矏宀栧矆宀樺矙宀戝矚宀滃驳宀㈠步宀搏宀卞玻宄佸卜宄勫硳宄ゅ硧宄ュ磦宕冨揣宕﹀串宕ゅ礊宕嗗礇宓樺淳宕村唇宓禌宓禎宓祴宓婂旦宓村秱宥欏稘璞冲斗宸呭匠褰峰緜寰囧緣寰屽緯寰欏緶寰ㄥ经寰靛炯琛㈠健鐘姲鐘寸姺鐘哥媰鐙佺嫀鐙嶇嫆鐙ㄧ嫰鐙╃嫴鐙寸嫹鐚佺嫵鐚冪嫼锟�".split("");
	for(a = 0; a != t[225].length; ++a)
		if(t[225][a].charCodeAt(0) !== 65533) { r[t[225][a]] = 57600 + a;
			e[57600 + a] = t[225][a] }
	t[226] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閲﹂嚙閲ㄩ嚛閲嚝閲嚟閲嚡閲伴嚤閲查嚦閲撮嚨閲堕嚪閲搁嚬閲洪嚮閲奸嚱閲鹃嚳閳€閳侀垈閳冮垊閳呴垎閳囬垐閳夐垔閳嬮垖閳嶉垘閳忛垚閳戦垝閳撻垟閳曢垨閳楅垬閳欓垰閳涢垳閳濋垶閳熼垹閳￠垻閳ｉ垽锟介垾閳﹂埀閳ㄩ埄閳埆閳埈閳埊閳伴埍閳查埑閳撮埖閳堕埛閳搁埞閳洪埢閳奸埥閳鹃埧閴€閴侀墏閴冮墑閴呯嫽鐚楃寭鐚＄寠鐚炵対鐚曠將鐚圭尌鐚尭鐚辩崘鐛嶇崡鐛犵崿鐛嵕鑸涘ぅ椋уい澶傞ィ楗чエ楗╅オ楗ガ楗撮シ楗介棣勯棣婇棣愰棣撻棣曞簚搴戝簨搴栧亥搴犲汗搴靛壕搴宠祿寤掑粦寤涘花寤喓蹇勫繅蹇栧繌鎬冨慨鎬勫俊蹇ゅ烤鎬呮€嗗开蹇扛鎬欐€垫€︽€涙€忔€嶆€╂€€婃€挎€℃伕鎭规伝鎭烘亗锟�".split("");
	for(a = 0; a != t[226].length; ++a)
		if(t[226][a].charCodeAt(0) !== 65533) { r[t[226][a]] = 57856 + a;
			e[57856 + a] = t[226][a] }
	t[227] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閴嗛墖閴堥墘閴婇墜閴岄墠閴庨墢閴愰墤閴掗墦閴旈墪閴栭墬閴橀墮閴氶墰閴滈墲閴為墴閴犻墶閴㈤墸閴ら墺閴﹂墽閴ㄩ墿閴壂閴壄閴壇閴伴壉閴查壋閴甸壎閴烽壐閴归壓閴婚壖閴介壘閴块妧閵侀妭閵冮妱閵咃拷閵嗛妵閵堥妷閵婇妺閵岄妽閵忛姁閵戦姃閵撻姅閵曢姈閵楅姌閵欓姎閵涢姕閵濋姙閵熼姞閵￠姠閵ｉ姢閵ラ姦閵ф仾鎭芥倴鎮氭偔鎮濇們鎮掓倢鎮涙儸鎮绘偙鎯濇儤鎯嗘儦鎮存劆鎰︽剷鎰ｆ兇鎰€鎰庢劔鎱婃叺鎲啍鎲ф喎鎳旀嚨蹇濋毘闂╅棲闂遍棾闂甸椂闂奸椌闃冮槃闃嗛槇闃婇構闃岄槏闃忛槖闃曢槚闃楅槞闃氫脯鐖挎垥姘垫睌姹滄眾娌ｆ矃娌愭矓娌屾报姹╂贝姹舵矄娌╂硱娉旀箔娉锋掣娉辨硹娌叉碃娉栨澈娉钞娌辨硴娉尘锟�".split("");
	for(a = 0; a != t[227].length; ++a)
		if(t[227][a].charCodeAt(0) !== 65533) { r[t[227][a]] = 58112 + a;
			e[58112 + a] = t[227][a] }
	t[228] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閵ㄩ姪閵姭閵姯閵姲閵遍姴閵抽姶閵甸姸閵烽姼閵归姾閵婚娂閵介娋閵块媭閶侀媯閶冮媱閶呴媶閶囬媺閶婇媼閶岄媿閶庨嫃閶愰嫅閶掗嫇閶旈嫊閶栭嫍閶橀嫏閶氶嫑閶滈嫕閶為嫙閶犻嫛閶㈤嫞閶ら嫢閶﹂嫥閶拷閶╅嫪閶嫭閶嫯閶嫲閶遍嫴閶抽嫶閶甸嫸閶烽嫺閶归嫼閶婚嫾閶介嬀閶块寑閷侀寕閷冮寗閷呴寙閷囬寛閷夋垂娲ф磳娴冩祱娲囨磩娲欐磶娲祶娲吹娲氭祻娴掓禂娲虫稇娴稙娑犳禐娑撴稊娴滄禒娴兼担娓氭穱娣呮窞娓庢犊娣犳笐娣︽窛娣欐笘娑笇娑斧婀箮婀翰婀熸簡婀撴箶娓叉弗婀勬粺婧辨簶婊犳辑婊㈡亥婧ф航婧绘悍婊楁捍婊忔簭婊傛簾娼㈡絾娼囨激婕曟还婕级娼嬫酱婕級婕╂緣婢嶆緦娼告讲娼兼胶婵戯拷".split("");
	for(a = 0; a != t[228].length; ++a)
		if(t[228][a].charCodeAt(0) !== 65533) { r[t[228][a]] = 58368 + a;
			e[58368 + a] = t[228][a] }
	t[229] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閷婇寢閷岄實閷庨審閷愰寫閷掗寭閷旈寱閷栭寳閷橀寵閷氶寷閷滈対閷為専閷犻尅閷㈤專閷ら尌閷﹂導閷ㄩ尒閷尗閷尛閷尟閷伴尡閷查尦閷撮尩閷堕尫閷搁尮閷洪尰閷奸尳閷块崁閸侀崅閸冮崉閸呴崋閸囬崍閸夛拷閸婇崑閸岄崓閸庨崗閸愰崙閸掗崜閸旈崟閸栭崡閸橀崣閸氶崨閸滈崫閸為崯閸犻崱閸㈤崳閸ら崶閸﹂崸閸ㄩ崺閸繅婢ф竟婢舵總婵℃慨婵炴繝婵€氱€ｇ€涚€圭€电亸鐏炲畝瀹勫畷瀹撳瀹哥敮楠炴惔瀵ゅ瑜板韫囪瑖杈惰繐杩曡骏杩郡杩╄喀杩宠卡閫呴€勯€嬮€﹂€戦€嶉€栭€￠€甸€堕€€亜閬戦亽閬愰仺閬橀仮閬涙毠閬撮伣閭傞倛閭冮倠褰愬綏褰栧綐灏诲挮灞愬睓瀛卞保灞︾炯寮缉寮壌寮奸灞濡冨濡╁Κ濡ｏ拷".split("");
	for(a = 0; a != t[229].length; ++a)
		if(t[229][a].charCodeAt(0) !== 65533) { r[t[229][a]] = 58624 + a;
			e[58624 + a] = t[229][a] }
	t[230] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閸嵀閸嵂閸伴嵄閸查嵆閸撮嵉閸堕嵎閸搁嵐閸洪嵒閸奸嵔閸鹃嵖閹€閹侀巶閹冮巹閹呴巻閹囬巿閹夐帄閹嬮帉閹嶉帋閹愰帒閹掗帗閹旈帟閹栭帡閹橀帣閹氶帥閹滈帩閹為師閹犻帯閹㈤帲閹ら帴閹﹂帶閹ㄩ帺閹帿锟介幀閹幃閹幇閹遍幉閹抽幋閹甸幎閹烽幐閹归幒閹婚幖閹介幘閹块弨閺侀弬閺冮弰閺呴弳閺囬張閺夐弸閺岄弽濡楀濡濡ゅ濡插Ο濮楀濞呭▎濮濆▓濮ｅ濮瑰▽濞夊ú濞村☉濞ｅ〒濠€濠у濠曞濠㈠┑鑳濯涘┓濠哄瀚瀚掑珨濯稿珷瀚ｅ瀚栧瀚樺珳瀣夊瑮瀣栧瀣峰瓈灏曞皽瀛氬瀛冲瓚瀛撳椹甸┓椹搁┖椹块┙楠€楠侀獏楠堥獖楠愰獟楠撻獤楠橀獩楠滈獫楠熼獱楠㈤楠ラ绾熺骸绾ｇ亥绾ㄧ憨锟�".split("");
	for(a = 0; a != t[230].length; ++a)
		if(t[230][a].charCodeAt(0) !== 65533) { r[t[230][a]] = 58880 + a;
			e[58880 + a] = t[230][a] }
	t[231] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閺庨弿閺愰彂閺掗彄閺旈彆閺楅彉閺欓彋閺涢彍閺濋彏閺熼彔閺￠彚閺ｉ彜閺ラ彟閺ч彣閺╅彧閺彫閺彯閺彴閺遍彶閺抽彺閺甸彾閺烽徃閺归徍閺婚徏閺介従閺块悁閻侀悅閻冮悇閻呴悊閻囬悎閻夐悐閻嬮悓閻嶏拷閻庨悘閻愰悜閻掗悡閻旈悤閻栭悧閻橀悪閻氶悰閻滈悵閻為悷閻犻悺閻㈤悾閻ら惀閻﹂惂閻ㄩ惄閻惈閻惌閻涵绾扮壕缁€缁佺粋缁夌粙缁岀粣缁旂粭缁涚粻缁＄花缁划缁槐缁茬紞缁剁缓缁荤痪缂佺紓缂冪紘缂堢紜缂岀紡缂戠紥缂楃紮缂滅紱缂熺肌缂㈢迹缂ょ讥缂︾姬缂极缂辑缂及缂辩疾缂崇嫉骞虹暱宸涚斁閭曠帋鐜戠幃鐜㈢師鐝忕弬鐝戠幏鐜崇弨鐝夌張鐝ョ彊椤肩悐鐝╃彠鐝炵幒鐝茬悘鐞憶鐞︾惀鐞ㄧ惏鐞惉锟�".split("");
	for(a = 0; a != t[231].length; ++a)
		if(t[231][a].charCodeAt(0) !== 65533) { r[t[231][a]] = 59136 + a;
			e[59136 + a] = t[231][a] }
	t[232] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閻惏閻遍惒閻抽惔閻甸惗閻烽惛閻归惡閻婚惣閻介惪閼€閼侀憘閼冮憚閼呴憜閼囬憟閼夐憡閼嬮憣閼嶉憥閼忛憪閼戦憭閼撻憯閼曢憱閼楅憳閼欓憵閼涢憸閼濋憺閼熼憼閼￠憿閼ｉ懁閼ラ懄閼ч懆閼╅應閼懎閼懐锟介懓閼遍懖閼抽懘閼甸懚閼烽懜閼归懞閼婚懠閼介懢閼块拃閽侀拏閽冮拕閽戦挅閽橀搰閾忛摀閾旈摎閾﹂摶閿滈敔鐞涚悮鐟佺憸鐟楃憰鐟欑懛鐟懢鐠滅拵鐠€鐠佺拠鐠嬬挒鐠ㄧ挬鐠愮挧鐡掔捄闊煫闊潓鏉撴潪鏉堟潻鏋ユ瀲鏉澇鏋樻灖鏉垫灗鏋炴灜鏋嬫澐鏉兼煱鏍夋煒鏍婃煩鏋版爩鏌欐灥鏌氭灣鏌濇爛鏌冩灨鏌㈡爭鏌佹熃鏍叉牫妗犳　妗庢、妗勬·姊冩牆妗曟ˇ妗佹¨妗€鏍炬妗夋牘姊垫妗存》姊撴～妫傛ギ妫兼妞犳９锟�".split("");
	for(a = 0; a != t[232].length; ++a)
		if(t[232][a].charCodeAt(0) !== 65533) { r[t[232][a]] = 59392 + a;
			e[59392 + a] = t[232][a] }
	t[233] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閿ч敵閿介晝闀堥晪闀曢暁闀犻暜闀撮暤闀烽暩闀归暫闀婚暭闀介暰闁€闁侀杺闁冮杽闁呴枂闁囬枅闁夐枈闁嬮枌闁嶉枎闁忛枑闁戦枓闁撻枖闁曢枛闁楅枠闁欓枤闁涢枩闁濋枮闁熼枲闁￠枹闁ｉ枻闁ラ枽闁ч枿闁╅柂锟介柅闁柇闁柉闁伴柋闁查柍闁撮柕闁堕柗闁搁柟闁洪柣闁奸柦闁鹃柨闂€闂侀梻闂冮梽闂呴梿闂囬棃闂夐棅闂嬫い妫版妞佹妫ｆ妤辨す妤犳妤濇妤姒樻ジ妞存姒囨妲庢妤︽ィ妤规姒ф姒Ν妲旀Ρ妲佹妲熸妲犳妲挎ǒ妲妯樻━妲叉﹦妯炬獱姗愭妯垫獛姗规ń妯ㄦ姗兼獞妾愭妾楁鐚风崚娈佹畟娈囨畡娈掓畵娈嶆畾娈涙娈将杞奖杞茶匠杞佃蕉杞歌椒杞硅胶杞艰骄杈佽緜杈勮緡杈嬶拷".split("");
	for(a = 0; a != t[233].length; ++a)
		if(t[233][a].charCodeAt(0) !== 65533) { r[t[233][a]] = 59648 + a;
			e[59648 + a] = t[233][a] }
	t[234] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷闂岄棈闂庨棌闂愰棏闂掗棑闂旈棔闂栭棗闂橀棛闂氶棝闂滈棟闂為棢闂犻棥闂㈤棧闂ら棩闂﹂棫闂椏闃囬槗闃橀槢闃為槧闃ｉ槫闃ラ槮闃ч槰闃╅槴闃槶闃槹闃烽樃闃归樅闃鹃檨闄冮檴闄庨檹闄戦檼闄撻櫀闄楋拷闄橀櫃闄氶櫆闄濋櫈闄犻櫍闄ラ櫐闄櫗闄櫙闄伴櫛闄抽櫢闄归櫤闄婚櫦闄介櫨闄块殌闅侀殏闅冮殑闅囬殙闅婅緧杈庤緩杈樿練杌庢垕鎴楁垱鎴熸垻鎴℃垾鎴ゆ埇鑷х摨鐡寸摽鐢忕攽鐢撴敶鏃棷鏃版槉鏄欐澆鏄冩槙鏄€鐐呮浄鏄濇槾鏄辨樁鏄佃€嗘櫉鏅旀檨鏅忔櫀鏅℃櫁鏅锋殑鏆屾毀鏆濇毦鏇涙洔鏇︽洨璐茶闯璐惰椿璐借祤璧呰祮璧堣祲璧囪祶璧曡禉瑙囪瑙嬭瑙庤瑙愯鐗姛鐗濈墻鐗壘鐗跨妱鐘嬬妽鐘忕姃鎸堟尣鎺帮拷".split("");
	for(a = 0; a != t[234].length; ++a)
		if(t[234][a].charCodeAt(0) !== 65533) { r[t[234][a]] = 59904 + a;
			e[59904 + a] = t[234][a] }
	t[235] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷闅岄殠闅戦殥闅撻殨闅栭殮闅涢殱闅為殶闅犻殹闅㈤殻闅ら殽闅﹂毃闅╅毆闅毈闅毊闅毐闅查毚闅甸毞闅搁毢闅婚毧闆傞泝闆堥泭闆嬮洂闆戦洆闆旈洊闆楅洏闆欓洑闆涢洔闆濋洖闆熼洝闆㈤洠闆ら洢闆﹂洤闆拷闆洯闆洶闆遍洸闆撮浀闆搁浐闆婚浖闆介浛闇傞渻闇呴湂闇嬮湆闇愰湋闇掗湐闇曢湕闇橀湙闇氶湜闇濋湡闇犳惪鎿樿€勬姣虫姣垫姘呮皣姘嗘皪姘曟皹姘欐皻姘℃癌姘ゆ蔼姘叉數鏁曟暙鐗嶇墥鐗栫埌铏㈠垨鑲熻倻鑲撹偧鏈婅偨鑲辫偒鑲偞鑲疯儳鑳ㄨ儵鑳儧鑳傝儎鑳欒儘鑳楁湊鑳濊儷鑳辫兇鑳剭鑴庤儾鑳兼湑鑴掕睔鑴惰劄鑴剺鑴茶厛鑵岃厯鑵磋厵鑵氳叡鑵犺叐鑵艰吔鑵収濉嶅鑶堣唫鑶戞粫鑶ｈ啰鑷屾湨鑷婅喕锟�".split("");
	for(a = 0; a != t[235].length; ++a)
		if(t[235][a].charCodeAt(0) !== 65533) { r[t[235][a]] = 60160 + a;
			e[60160 + a] = t[235][a] }
	t[236] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷闇￠湤闇ｉ湦闇ラ湨闇ч湪闇╅湯闇湲闇湵闇抽湸闇甸湺闇烽満闇婚溂闇介溈闈€闈侀潅闈冮潉闈呴潌闈囬潏闈夐潑闈嬮潓闈嶉潕闈忛潗闈戦潝闈曢潡闈橀潥闈滈潩闈熼潱闈ら潶闈ч潹闈潾闈澀闈澂闈伴澅锟介澆闈甸澐闈搁澒闈洪澔闈介澗闈块瀫闉侀瀭闉冮瀯闉嗛瀲闉堥瀴闉婇瀸闉庨瀼闉愰灀闉曢灃闉楅灆闉氶灈闉滈灊鑷佽啨娆ゆ娆规瓋姝嗘瓩椋戦椋撻椋欓娈冲絸姣傝С鏂愰綉鏂撴柤鏃嗘梽鏃冩棇鏃庢棐鏃栫個鐐滅倴鐐濈偦鐑€鐐风偒鐐辩儴鐑婄剱鐒撶剸鐒劚鐓崇厹鐓ㄧ厖鐓茬厞鐓哥吅鐔樼喅鐔电啫鐔犵嚑鐕旂嚙鐕圭垵鐖ㄧ伂鐒樼叇鐔规埦鎴芥墐鎵堟墘绀荤绁嗙绁涚绁撶绁㈢绁犵ク绁хズ绂呯绂氱Η绂冲繎蹇愶拷".split("");
	for(a = 0; a != t[236].length; ++a)
		if(t[236][a].charCodeAt(0) !== 65533) { r[t[236][a]] = 60416 + a;
			e[60416 + a] = t[236][a] }
	t[237] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷闉為灍闉￠灑闉ら灔闉﹂灖闉ㄩ灘闉灛闉灠闉遍灣闉甸灦闉烽灨闉归灪闉婚灱闉介灳闉块焵闊侀焸闊冮焺闊呴焼闊囬焾闊夐煀闊嬮煂闊嶉煄闊忛煇闊戦煉闊撻煍闊曢煐闊楅煒闊欓煔闊涢煖闊濋煘闊熼煚闊￠煝闊ｏ拷闊ら煡闊ㄩ煯闊煱闊遍煵闊撮煼闊搁煿闊洪熁闊奸熃闊鹃熆闋€闋侀爞闋冮爠闋呴爢闋囬爤闋夐爦闋嬮爩闋嶉爭鎬兼仢鎭氭仹鎭佹仚鎭ｆ偒鎰嗘剭鎱濇啯鎲濇噵鎳戞垎鑲€鑱挎矒娉舵芳鐭剁煾鐮€鐮夌牀鐮樼爲鏂牠鐮滅牆鐮圭牶鐮荤牊鐮肩牓鐮牐鐮╃纭纭楃牔纭愮纭岀—纰涚纰氱纰滅ⅰ纰ｇ⒉纰圭ⅴ纾旂纾夌，纾茬纾寸绀ょ绀撮緵榛归换榛肩洷鐪勭湇鐩圭渿鐪堢湚鐪㈢湙鐪湨鐪电湼鐫愮潙鐫囩潈鐫氱潹锟�".split("");
	for(a = 0; a != t[237].length; ++a)
		if(t[237][a].charCodeAt(0) !== 65533) { r[t[237][a]] = 60672 + a;
			e[60672 + a] = t[237][a] }
	t[238] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷闋忛爯闋戦爳闋撻爺闋曢爾闋楅牁闋欓牃闋涢牅闋濋牉闋熼牋闋￠牏闋ｉ牑闋ラ牔闋ч牗闋╅牚闋牞闋牣闋牥闋遍牪闋抽牬闋甸牰闋烽牳闋归牶闋婚牸闋介牼闋块椤侀椤冮椤呴椤囬椤夐椤嬮椤嶏拷椤庨椤愰椤掗椤旈椤栭椤橀椤氶椤滈椤為椤犻　椤㈤。椤らˉ椤﹂¨椤ㄩ々椤～椤…椤潰鐫ョ澘鐬嶇澖鐬€鐬岀瀾鐬熺灎鐬扮灥鐬界敽鐣€鐣庣晪鐣堢暃鐣茬暪鐤冪綐缃＄綗瑭堢建缃寸奖缃圭緛缃剧泹鐩ヨ牪閽呴拞閽囬拫閽婇拰閽嶉拸閽愰挃閽楅挄閽氶挍閽滈挘閽ら挮閽挱閽挴閽伴挷閽撮挾閽烽捀閽归捄閽奸捊閽块搫閾堥搲閾婇搵閾岄搷閾庨搻閾戦搾閾曢摉閾楅摍閾橀摏閾為摕閾犻摙閾ら摜閾ч摠閾拷".split("");
	for(a = 0; a != t[238].length; ++a)
		if(t[238][a].charCodeAt(0) !== 65533) { r[t[238][a]] = 60928 + a;
			e[60928 + a] = t[238][a] }
	t[239] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷椤“椤遍〔椤抽〈棰嬮棰掗棰欓ⅲ棰ㄩⅸ棰棰棰棰伴⒈棰查⒊棰撮⒌棰堕⒎棰搁⒐棰洪⒒棰奸⒔棰鹃⒖椋€椋侀椋冮椋呴椋囬椋夐椋嬮椋嶉椋愰椋栭椋涢椋濋　椋￠＂椋ｉ￥锟介％椋﹂）椋＋椋－椋／椋伴１椋查３椋撮５椋堕７椋搁９椋洪；椋奸＝椋鹃？椁€椁侀椁冮椁呴椁囬摡閾摦閾摮閾撮摰閾烽摴閾奸摻閾块攦閿傞攩閿囬攭閿婇攳閿庨攺閿掗敁閿旈敃閿栭敇閿涢敐閿為敓閿㈤敧閿敥閿敱閿查敶閿堕敺閿搁敿閿鹃斂闀傞數闀勯晠闀嗛晧闀岄晭闀忛晵闀撻晹闀栭晽闀橀暀闀涢暈闀熼暆闀￠暍闀ら暐闀﹂暓闀ㄩ暕闀暙闀暞闀遍暡闀抽敽鐭х煬闆夌绉В绉▎宓囩▋绋傜绋旓拷".split("");
	for(a = 0; a != t[239].length; ++a)
		if(t[239][a].charCodeAt(0) !== 65533) { r[t[239][a]] = 61184 + a;
			e[61184 + a] = t[239][a] }
	t[240] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷椁堥椁婇椁岄椁忛椁掗椁旈椁栭椁橀椁氶椁滈椁為椁犻ぁ椁㈤ぃ椁らぅ椁﹂ぇ椁ㄩぉ椁か椁き椁ぐ椁遍げ椁抽ご椁甸ざ椁烽じ椁归ず椁婚ぜ椁介ぞ椁块楗侀楗冮楗呴楗囬楗夛拷楗婇楗岄楗庨楗愰楗掗楗栭楗橀楗氶楗滈楗為楗犻ァ楗㈤イ楗﹂コ楗搁ス楗婚ゾ棣傞棣夌ü绋风榛忛Ε绌扮殘鐨庣殦鐨欑殼鐡炵摖鐢笭楦㈤辅楦╅釜楦脯楦查副楦堕父楦烽腹楦洪妇楣侀箓楣勯箚楣囬箞楣夐箣楣岄箮楣戦箷楣楅箽楣涢箿楣為梗楣﹂恭楣ㄩ供楣公楣贡楣钩鐤掔枖鐤栫枲鐤濈柆鐤ｇ柍鐤寸柛鐥勭柋鐤扮梼鐥傜棖鐥嶇棧鐥ㄧ棪鐥ょ棲鐥х槂鐥辩椉鐥跨槓鐦€鐦呯槍鐦楃槉鐦ョ槝鐦曠槞锟�".split("");
	for(a = 0; a != t[240].length; ++a)
		if(t[240][a].charCodeAt(0) !== 65533) { r[t[240][a]] = 61440 + a;
			e[61440 + a] = t[240][a] }
	t[241] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷棣岄棣氶棣滈棣為棣犻Α棣㈤Γ棣らΖ棣чΙ棣Λ棣Ν棣Ο棣伴Ρ棣查Τ棣撮Φ棣堕Ψ棣搁棣洪棣奸棣鹃椐€椐侀椐冮椐呴椐囬椐夐椐嬮椐嶉椐忛椐戦椐撻椐曢椐楅锟介椐氶椐滈椐為椐犻А椐㈤В椐らД椐﹂Ё椐ㄩЗ椐Й椐Л椐Н椐伴П椐查С椐撮У椐堕Х椐搁Ч鐦涚樇鐦㈢槧鐧€鐦槹鐦跨樀鐧冪樉鐦崇檷鐧炵檾鐧滅櫀鐧櫙缈婄绌哥┕绐€绐嗙獔绐曠绐犵绐ㄧ绐宠·琛╄〔琛借】琚傝ⅱ瑁嗚⒎琚艰瑁㈣瑁ｈ％瑁辫瑁艰（瑁捐０瑜¤瑜撹瑜婅ご瑜ざ瑗佽ウ瑗荤枊鑳ョ毑鐨寸煖鑰掕€旇€栬€滆€犺€㈣€ヨ€﹁€ц€╄€ㄨ€辫€嬭€佃亙鑱嗚亶鑱掕仼鑱辫椤搁棰冿拷".split("");
	for(a = 0; a != t[241].length; ++a)
		if(t[241][a].charCodeAt(0) !== 65533) { r[t[241][a]] = 61696 + a;
			e[61696 + a] = t[241][a] }
	t[242] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷椐洪Щ椐奸Ы椐鹃Э楱€楱侀▊楱冮▌楱呴▎楱囬▓楱夐▕楱嬮▽楱嶉◣楱忛◥楱戦⊕楱撻〝楱曢楱楅楱欓楱涢楱濋楱熼楱￠á楱ｉà楱ラé楱чè楱╅í楱ì楱ó楱ò楱遍ú楱抽ù楱甸ǘ楱烽ǜ锟介ü楱洪ɑ楱奸ń楱鹃椹€椹侀﹤椹冮﹦椹呴﹩椹囬﹫椹夐椹嬮椹嶉椹忛椹戦椹撻椹曢〇椹楅椹欓棰岄棰忛棰氶棰為棰￠ⅱ棰ラⅵ铏嶈檾铏櫘铏胯櫤铏艰櫥铓ㄨ殟铓嬭毈铓濊毀铓ｈ毆铓撹毄铓惰泟铓佃泿铓拌毢铓辫毌铔夎洀铓磋洨铔辫洸铔洺铔愯湏铔炶洿铔熻洏铔戣渻铚囪浉铚堣湂铚嶈湁铚ｈ溁铚炶湧铚湚铚捐潏铚磋湵铚╄湻铚胯瀭铚㈣澖铦捐澔铦犺澃铦岃澁铻嬭潛铦ｈ澕铦よ潤铦ヨ灀铻灗锜掞拷".split("");
	for(a = 0; a != t[242].length; ++a)
		if(t[242][a].charCodeAt(0) !== 65533) { r[t[242][a]] = 61952 + a;
			e[61952 + a] = t[242][a] }
	t[243] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷椹氶椹滈椹為椹犻椹㈤椹ら━椹﹂┃椹ㄩ┅椹┇椹查獌楠夐獚楠庨獢楠曢獧楠﹂楠楠楠楠查楠撮楠归楠介楠块珒楂勯珕楂囬珗楂夐珚楂嶉珟楂忛珢楂掗珨楂曢珫楂楅珯楂氶珱楂滐拷楂濋珵楂犻楂ｉ楂ラ楂ㄩ楂楂楂遍楂抽楂甸楂烽楂洪楂介楂块瑎楝侀瑐楝勯瑓楝嗚焼铻堣瀰铻灄铻冭灚锜ヨ灛铻佃灣锜嬭煋铻借煈锜€锜婅煕锜煚锜爾锠撹熅锠婅牄锠¤牴锠肩级缃傜絼缃呰垚绔虹绗堢瑑绗勭瑫绗婄绗忕瓏绗哥绗欑绗辩瑺绗ョ绗崇绗炵瓨绛氱瓍绛电瓕绛濈瓲绛绛㈢绛辩異绠︾绠哥绠濈绠呯绠滅绠绡戠瘉绡岀瘽绡氱绡︾绨岀绡肩皬绨栫皨锟�".split("");
	for(a = 0; a != t[243].length; ++a)
		if(t[243][a].charCodeAt(0) !== 65533) { r[t[243][a]] = 62208 + a;
			e[62208 + a] = t[243][a] }
	t[244] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷楝囬瑝楝婇瑡楝岄瑣楝庨瑦楝戦瑨楝旈瑫楝栭瑮楝橀瑱楝氶瑳楝滈瑵楝為瑺楝￠楝ら楝﹂楝ㄩ楝楝楝楝遍楝撮楝堕楝搁楝洪楝鹃榄€榄嗛瓓榄嬮瓕榄庨瓙榄掗瓝榄曢瓥榄楅瓨榄欓瓪锟介瓫榄滈瓭榄為瓱榄犻榄㈤榄ら榄﹂榄ㄩ榄榄榄榄伴榄查榄撮榄堕榄搁榄洪绨熺蔼绨︾案绫佺眬鑷捐垇鑸傝垊鑷鑸¤垻鑸ｈ埈鑸埁鑸埜鑸昏埑鑸磋埦鑹勮墘鑹嬭墢鑹氳墴鑹ㄨ【琚呰瑁樿瑗炵緷缇熺晶缇景缇茬奔鏁夌矐绮濈矞绮炵并绮茬布绮界硜绯囩硨绯嶇硤绯呯硹绯ㄨ壆鏆ㄧ究缈庣繒缈ョ俊缈︾咯缈砍绯哥捣缍︾懂绻囩簺楹搁捍璧宠秳瓒旇稇瓒辫掸璧眹璞夐厞閰愰厧閰忛叅锟�".split("");
	for(a = 0; a != t[244].length; ++a)
		if(t[244][a].charCodeAt(0) !== 65533) { r[t[244][a]] = 62464 + a;
			e[62464 + a] = t[244][a] }
	t[245] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷榄奸榄鹃楫€楫侀畟楫冮畡楫呴畣楫囬畧楫夐畩楫嬮畬楫嶉畮楫忛異楫戦畳楫撻當楫曢畺楫楅畼楫欓畾楫涢疁楫濋疄楫熼疇楫￠楫ｉ楫ラ楫ч楫╅楫楫楫楫遍楫抽楫甸楫烽楫归锟介楫奸楫鹃榀€榀侀瘋榀冮瘎榀呴瘑榀囬瘓榀夐瘖榀嬮瘜榀嶉瘞榀忛瘣榀戦瘨榀撻瘮榀曢瘱榀楅瘶榀欓瘹榀涢參閰￠叞閰╅叝閰介吘閰查叴閰归唽閱呴啇閱嶉啈閱㈤啠閱啳閱啹閱甸喆閱鸿睍楣捐陡璺竻韫欒供瓒佃犊瓒艰逗璺勮窎璺楄窔璺炶穾璺忚窙璺嗚番璺疯犯璺ｈ饭璺昏筏韪夎方韪旇笣韪熻脯韪福韪负韫€韪硅傅韪借副韫夎箒韫傝箲韫掕箠韫拌苟韫艰汞韫磋簠韬忚簲韬愯簻韬炶备璨傝矈璨呰矘璨旀枦瑙栬瑙氳锟�".split("");
	for(a = 0; a != t[245].length; ++a)
		if(t[245][a].charCodeAt(0) !== 65533) { r[t[245][a]] = 62720 + a;
			e[62720 + a] = t[245][a] }
	t[246] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷榀滈瘽榀為療榀犻榀㈤榀ら榀﹂榀ㄩ榀榀榀榀伴榀查榀撮榀堕榀搁榀洪榀奸榀鹃榘€榘侀皞榘冮皠榘呴皢榘囬皥榘夐皧榘嬮皩榘嶉皫榘忛皭榘戦皰榘撻皵榘曢皷榘楅皹榘欓皻锟介皼榘滈皾榘為盁榘犻啊榘㈤埃榘ら哎榘﹂哀榘ㄩ癌榘矮榘碍榘隘榘伴氨榘查俺榘撮暗榘堕胺榘搁肮榘洪盎瑙ヨЙ瑙ň璎﹂潛闆╅洺闆渾闇侀湀闇忛湈闇湱闇伴溇榫€榫冮緟榫嗛緡榫堥緣榫婇緦榛鹃紜榧嶉毠闅奸毥闆庨洅鐬块洜閵庨姰閶堥尵閸強閹忛惥閼笨椴傞矃椴嗛矅椴堢ǎ椴嬮矌椴愰矐椴掗矓椴曢矚椴涢矠椴熼矤椴￠并椴ｉ播椴﹂钵椴ㄩ博椴箔椴舶椴遍膊椴抽泊椴甸捕椴烽埠椴婚布椴介硠槌呴硢槌囬硦槌嬶拷".split("");
	for(a = 0; a != t[246].length; ++a)
		if(t[246][a].charCodeAt(0) !== 65533) { r[t[246][a]] = 62976 + a;
			e[62976 + a] = t[246][a] }
	t[247] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷榘奸敖榘鹃翱楸€楸侀眰楸冮眲楸呴眴楸囬眻楸夐眾楸嬮睂楸嶉睅楸忛睈楸戦睊楸撻睌楸曢睎楸楅睒楸欓睔楸涢睖楸濋睘楸熼睜楸￠雹楸ｉ堡楸ラ宝楸ч报楸╅豹楸爆楸碑楸卑楸遍辈楸抽贝楸甸倍楸烽备楸归焙锟介被楸介本椴€椴冮矂椴夐矈椴岄矎椴撻矕椴楅矘椴欓矟椴铂椴补椴鹃部槌€槌侀硞槌堥硥槌戦硳槌氶硾槌犻场槌岄硩槌庨硰槌愰硴槌旈硶槌楅硺槌欓硿槌濋碂槌㈤澕闉呴瀾闉掗灁闉灚闉ｉ灢闉撮楠伴楣橀楠洪楂侀珋楂呴珎楂嬮珜楂戦瓍榄冮瓏榄夐瓐榄嶉瓚椋ㄩ椁楗旈珶楂￠楂楂婚楂归瑘楝忛瑩楝熼楹介壕绺婚簜楹囬簣楹嬮簰閺栭簼楹熼粵榛滈粷榛犻粺榛㈤哗榛ч互榛化榧㈤棘榧脊榧烽冀榧鹃絼锟�".split("");
	for(a = 0; a != t[247].length; ++a)
		if(t[247][a].charCodeAt(0) !== 65533) { r[t[247][a]] = 63232 + a;
			e[63232 + a] = t[247][a] }
	t[248] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷槌ｉ长槌ラ肠槌ч敞槌╅唱槌超槌钞槌嘲槌遍巢槌抽炒槌甸扯槌烽掣槌归澈槌婚臣槌介尘槌块磤榇侀磦榇冮磩榇呴磫榇囬磮榇夐磰榇嬮磳榇嶉磶榇忛磹榇戦磼榇撻磾榇曢礀榇楅礃榇欓礆榇涢礈榇濋礊榇熼礌榇★拷榇㈤矗榇ら触榇﹂揣榇ㄩ穿榇传榇喘榇疮榇伴幢榇查闯榇撮吹榇堕捶榇搁垂榇洪椿榇奸唇榇鹃纯榈€榈侀祩锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[248].length; ++a)
		if(t[248][a].charCodeAt(0) !== 65533) { r[t[248][a]] = 63488 + a;
			e[63488 + a] = t[248][a] }
	t[249] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷榈冮祫榈呴祮榈囬祱榈夐祳榈嬮祵榈嶉祹榈忛祼榈戦祾榈撻禂榈曢禆榈楅禈榈欓禋榈涢禍榈濋禐榈熼禒榈￠耽榈ｉ丹榈ラ郸榈ч胆榈╅氮榈惮榈诞榈蛋榈遍挡榈抽荡榈甸刀榈烽蹈榈归岛榈婚导榈介稻榈块秬槎侊拷槎傞秲槎勯秴槎嗛秶槎堥秹槎婇秼槎岄秿槎庨稄槎愰稇槎掗稉槎旈稌槎栭稐槎橀稒槎氶稕槎滈稘槎為稛槎犻丁槎拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[249].length; ++a)
		if(t[249][a].charCodeAt(0) !== 65533) { r[t[249][a]] = 63744 + a;
			e[63744 + a] = t[249][a] }
	t[250] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷槎ｉ钉槎ラ鼎槎ч定槎╅丢槎冬槎懂槎栋槎遍恫槎抽洞槎甸抖槎烽陡槎归逗槎婚都槎介毒槎块穩榉侀穫榉冮穭榉呴穯榉囬穲榉夐穵榉嬮穼榉嶉穾榉忛窅榉戦窉榉撻窋榉曢窎榉楅窐榉欓窔榉涢窚榉濋窞榉熼窢榉★拷榉㈤罚榉ら伐榉﹂阀榉ㄩ珐榉帆榉翻榉矾榉伴繁榉查烦榉撮返榉堕贩榉搁饭榉洪坊榉奸方榉鹃房楦€楦侀競锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[250].length; ++a)
		if(t[250][a].charCodeAt(0) !== 65533) { r[t[250][a]] = 64e3 + a;
			e[64e3 + a] = t[250][a] }
	t[251] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷楦冮竸楦呴竼楦囬笀楦夐笂楦嬮笇楦嶉笌楦忛笎楦戦笒楦撻笖楦曢笘楦楅笜楦欓笟楦涢笢楦濋笧楦ら抚楦赴楦撮富楦奸箑楣嶉箰楣掗箵楣旈箹楣欓節楣熼範楣￠耿楣ラ巩楣共楣撮沟楣堕狗楣搁构楣洪够楣奸菇楹€锟介簛楹冮簞楹呴簡楹夐簥楹岄簫楹庨簭楹愰簯楹旈簳楹栭簵楹橀簷楹氶簺楹滈簽楹犻骸楹㈤海楹ら亥楹ч酣楹╅邯锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[251].length; ++a)
		if(t[251][a].charCodeAt(0) !== 65533) { r[t[251][a]] = 64256 + a;
			e[64256 + a] = t[251][a] }
	t[252] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷楹含楹寒楹喊楹遍翰楹抽旱楹堕悍楹归汉楹奸嚎榛€榛侀粋榛冮粎榛嗛粐榛堥粖榛嬮粚榛愰粧榛撻粫榛栭粭榛欓粴榛為弧榛ｉ护榛﹂花榛滑榛划榛伴槐榛查怀榛撮坏榛堕环榛搁缓榛介豢榧€榧侀紓榧冮紕榧咃拷榧嗛紘榧堥級榧婇紝榧忛紤榧掗紨榧曢紪榧橀細榧涢紲榧濋紴榧熼肌榧ｉ激榧ラ鸡榧ч绩榧╅吉榧辑榧及榧憋拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[252].length; ++a)
		if(t[252][a].charCodeAt(0) !== 65533) { r[t[252][a]] = 64512 + a;
			e[64512 + a] = t[252][a] }
	t[253] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷榧查汲榧撮嫉榧堕几榧洪技榧块絸榻侀絺榻冮絽榻嗛絿榻堥綁榻婇綃榻岄綅榻庨綇榻掗綋榻旈綍榻栭綏榻橀綑榻氶經榻滈綕榻為綗榻犻健榻㈤剑榻ら渐榻﹂涧榻ㄩ僵榻将榻江榻蒋榻伴奖榻查匠榻撮降榻堕椒榻革拷榻归胶榻婚郊榻介骄榫侀緜榫嶉編榫忛緪榫戦緬榫撻緮榫曢緰榫楅緲榫滈緷榫為尽榫㈤荆榫ら茎铯ス铴曪Ё铵憋拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[253].length; ++a)
		if(t[253][a].charCodeAt(0) !== 65533) { r[t[253][a]] = 64768 + a;
			e[64768 + a] = t[253][a] }
	t[254] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷铷岋◢铷庯◤铷戯〒铷旓铷燂铷★ǎ铷わě铷ī锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[254].length; ++a)
		if(t[254][a].charCodeAt(0) !== 65533) { r[t[254][a]] = 65024 + a;
			e[65024 + a] = t[254][a] }
	return { enc: r, dec: e }
}();
cptable[949] = function() {
	var e = [],
		r = {},
		t = [],
		a;
	t[0] = "\0\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[0].length; ++a)
		if(t[0][a].charCodeAt(0) !== 65533) { r[t[0][a]] = 0 + a;
			e[0 + a] = t[0][a] }
	t[129] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟疥皞臧冴皡臧嗞皨臧岅皪臧庩皬臧橁盀臧熽啊臧㈥埃臧リ唉臧ш皑臧╆蔼臧爱臧碴俺臧达拷锟斤拷锟斤拷锟疥暗臧蛾胺臧宏盎臧疥熬臧筷眮瓯傟眱瓯勱眳瓯嗞眹瓯堦眽瓯婈睂瓯庩睆瓯愱睉瓯掙睋瓯曪拷锟斤拷锟斤拷锟疥睎瓯楆睓瓯氷睕瓯濌睘瓯熽睜瓯￡雹瓯ｊ堡瓯リ宝瓯ш报瓯╆豹瓯爆瓯碑瓯辈瓯酬钡瓯蛾惫瓯魂奔瓯疥本瓯筷矀瓴囮矆瓴嶊矌瓴応矐瓴掙矒瓴曣矕瓴楆矘瓴欔矚瓴涥矠瓴㈥玻瓴り播瓴﹃钵瓴箔瓴脖瓴碴渤瓴搓驳瓴蛾卜瓴宏簿瓴筷硛瓿傟硟瓿呹硢瓿囮硥瓿婈硧瓿嶊硯瓿応硱瓿戧硳瓿撽硵瓿栮硺瓿欔硽瓿涥硿瓿濌碁瓿熽尝瓿ｊ偿瓿﹃畅瓿抄瓿巢瓿搓撤瓿戈彻瓿宏郴瓿娟晨甏侁磦甏冴磪甏囮磮甏夑磰甏嬯磶甏愱磼甏擄拷".split("");
	for(a = 0; a != t[129].length; ++a)
		if(t[129][a].charCodeAt(0) !== 65533) { r[t[129][a]] = 33024 + a;
			e[33024 + a] = t[129][a] }
	t[130] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟疥磾甏曣礀甏楆礄甏氷礇甏濌礊甏熽础甏㈥矗甏り触甏﹃揣甏椽甏串甏窗甏标床甏筹拷锟斤拷锟斤拷锟疥炊甏逢垂甏宏椿甏疥淳甏筷祤甑侁祩甑冴祮甑堦祳甑嬯祵甑嶊祹甑応祽甑掙祿甑曣禆甑楋拷锟斤拷锟斤拷锟疥禉甑氷禌甑滉禎甑炾禑甑犼耽甑り单甑﹃掸甑旦甑但甑弹甑标挡甑逢蹈甑龟岛甑娟秬甓冴秳甓呹秵甓囮秺甓嬯秿甓庩稄甓戧稈甓撽稊甓曣稏甓楆稑甓欔稓甓涥稙甓熽稜甓￡盯甓ｊ顶甓﹃锭甓订甓东甓董甓动甓瓣侗甓碴冻甓搓兜甓蛾陡甓龟逗甓魂都甓疥毒甓筷穫攴冴穮攴嗞穱攴夑穵攴嬯穼攴嶊穾攴応窉攴旉窌攴栮窏攴橁窓攴氷窙攴濌窞攴熽贰攴㈥罚攴リ乏攴ш法攴╆藩攴番攴樊攴钒攴标凡攴酬反攴店范攴凤拷".split("");
	for(a = 0; a != t[130].length; ++a)
		if(t[130][a].charCodeAt(0) !== 65533) { r[t[130][a]] = 33280 + a;
			e[33280 + a] = t[130][a] }
	t[131] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟疥泛攴魂方攴娟競旮冴竸旮呹竼旮囮笂旮岅笌旮応笎旮戧笒旮撽笗旮栮笚旮橁笝旮氷笡旮滐拷锟斤拷锟斤拷锟疥笣旮炾笩旮犼浮旮㈥福旮り弗旮﹃抚旮俯旮斧旮腑旮腐旮碴赋旮店付旮龟富旮硷拷锟斤拷锟斤拷锟疥附旮娟缚旯傟箘旯囮箞旯夑箣旯応箲旯掙箵旯曣箺旯橁箼旯氷箾旯炾耿旯ｊ工旯﹃恭旯公旯巩旯贡旯碴钩旯搓沟旯蛾狗旯宏咕旯筷簚旰侁簜旰冴簡旰囮簣旰夑簥旰嬯簫旰庩簭旰愱簯旰掙簱旰旉簳旰栮簵旰橁簷旰氷簺旰滉簼旰炾簾旰犼骸旰㈥海旰り亥旰﹃骇旰憨旰韩旰涵旰函旰瓣罕旰碴撼旰搓旱旰蛾悍旰戈汗旰宏夯旰筷粊昊傟粌昊呹粏昊囮粓昊夑粖昊嬯粠昊掙粨昊旉粫昊栮粭昊氷粵昊濌粸昊熽粻昊￡虎昊ｊ护昊ワ拷".split("");
	for(a = 0; a != t[131].length; ++a)
		if(t[131][a].charCodeAt(0) !== 65533) { r[t[131][a]] = 33536 + a;
			e[33536 + a] = t[131][a] }
	t[132] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟疥沪昊ш哗昊滑昊化昊瓣槐昊碴怀昊店欢昊逢还昊宏换昊疥痪昊筷紑昙侁紓昙冴紕昙咃拷锟斤拷锟斤拷锟疥紗昙夑紛昙嬯紝昙庩紡昙戧紥昙撽紨昙曣紪昙楆紭昙欔細昙涥紲昙濌紴昙熽紶昙￡饥昙ｏ拷锟斤拷锟斤拷锟疥激昙リ鸡昙ш绩昙╆吉昙籍昙急昙酬嫉昙蛾挤昙戈脊昙宏蓟昙娟絸杲勱絽杲嗞絿杲婈綃杲岅綅杲庩綇杲戧綊杲撽綌杲曣綎杲楆綐杲欔綒杲涥綖杲熽綘杲￡舰杲ｊ溅杲ш建杲╆姜杲浆杲疆杲桨杲标讲杲酬酱杲店蕉杲逢礁杲宏交杲缄浇杲娟娇昃侁緜昃冴緟昃嗞緡昃夑緤昃嬯緦昃嶊編昃応緬昃撽緮昃栮緱昃橁緳昃氷緵昃濌緸昃熽緺昃￡劲昃ｊ兢昃リ睛昃ш鲸昃╆惊昃粳昃井昃景昃标静昃酬敬昃店径昃逢竞昃魂窘昃撅拷".split("");
	for(a = 0; a != t[132].length; ++a)
		if(t[132][a].charCodeAt(0) !== 65533) { r[t[132][a]] = 33792 + a;
			e[33792 + a] = t[132][a] }
	t[133] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟疥究昕侁總昕冴縿昕呹繂昕婈繉昕応繍昕戧繏昕撽繒昕栮織昕橁繖昕氷繘昕濌繛昕熽繝昕★拷锟斤拷锟斤拷锟疥竣昕ｊ郡昕リ喀昕ш开昕楷昕慨昕坎昕酬康昕蛾糠昕龟亢昕魂考昕疥烤昕侩€傠€冿拷锟斤拷锟斤拷锟诫€呺€嗠€囯€堧€夒€婋€嬰€嶋€庪€忞€戨€掚€撾€曤€栯€楇€橂€欕€氹€涬€炿€熾€犽€‰€㈦€ｋ€る€ル€﹄€щ€╇€€€€€€€半€彪€搽€畴€措€惦€峨€冯€鸽€闺€弘€浑€茧€诫€倦€侩亐雭侂亗雭冸亞雭囯亯雭嬰亶雭忞亹雭戨亽雭栯仒雭氹仜雭滊仦雭熾仩雭‰仮雭ｋ仱雭ル仸雭щ仺雭╇仾雭伂雭伄雭伆雭彪伈雭畴伌雭惦伓雭冯伕雭闺伜雭浑伨雭侩倎雮傠們雮呺倖雮囯倛雮夒倞雮嬰値雮愲倰雮撾倲雮曤倴雮楇倹雮濍倿雮ｋ偆锟�".split("");
	for(a = 0; a != t[133].length; ++a)
		if(t[133][a].charCodeAt(0) !== 65533) { r[t[133][a]] = 34048 + a;
			e[34048 + a] = t[133][a] }
	t[134] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫偉雮﹄偋雮偘雮搽偠雮冯偣雮弘偦雮诫偩雮侩儉雰侂儌雰冸儐雰婋儖雰岆儘雰庪儚雰掞拷锟斤拷锟斤拷锟诫儞雰曤儢雰楇儥雰氹儧雰滊儩雰炿儫雰‰儮雰ｋ儰雰﹄儳雰儵雰儷雰儹雰儻雰帮拷锟斤拷锟斤拷锟诫儽雰搽兂雰措兊雰峨兎雰鸽児雰弘兓雰茧兘雰倦兛雱€雱侂剛雱冸剟雱呺剢雱囯剨雱嶋剮雱忞剳雱旊剷雱栯剹雱氹劄雱熾劆雱‰劉雱﹄劎雱╇劒雱劖雱劘雱半劚雱搽劤雱峨労雱浑劶雱诫劸雱侩厒雲冸厖雲嗠厙雲夒厞雲嬰厡雲嶋厧雲忞厭雲撾厲雲楇厵雲氹厸雲濍厼雲熾叀雲㈦叄雲る叆雲﹄収雲叐雲叓雲叚雲叝雲半叡雲搽叧雲措叺雲峨叿雲弘吇雲诫吘雲侩唩雴冸唲雴呺唵雴囯唺雴岆啂雴忞啇雴戨啎雴栯啑雴欕啔雴涬啙锟�".split("");
	for(a = 0; a != t[134].length; ++a)
		if(t[134][a].charCodeAt(0) !== 65533) { r[t[134][a]] = 34304 + a;
			e[34304 + a] = t[134][a] }
	t[135] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫啚雴熾啝雴‰啟雴ｋ啢雴ル啨雴щ啯雴啱雴啳雴啹雴半啽雴搽喅雴措喌雴峨喎雴革拷锟斤拷锟斤拷锟诫喒雴弘喕雴茧喗雴倦喛雵€雵侂噦雵冸噭雵呺噯雵囯噲雵夒噴雵嬰噸雵庪噺雵戨噿雵撾嚂锟斤拷锟斤拷锟斤拷雵栯嚄雵橂嚈雵氹嚊雵炿嚑雵‰嚔雵ｋ嚖雵ル嚘雵щ嚜雵嚟雵嚡雵彪嚥雵畴嚧雵惦嚩雵冯嚫雵弘嚰雵倦嚳雸€雸侂垈雸冸垎雸囯垑雸婋垗雸庪垙雸愲垜雸掚垞雸栯垬雸氹垱雸滊垵雸炿垷雸‰垻雸ｋ垽雸ル垿雸щ埁雸╇埅雸埇雸埉雸埌雸彪埐雸畴埖雸峨埛雸鸽埞雸弘埢雸诫埦雸侩墍雺侂墏雺冸墑雺呺墕雺囯増雺夒墛雺嬰墝雺嶋墡雺忞墣雺戨墥雺撾墧雺曤墫雺楇墮雺氹墰雺濍墳雺熾墶雺㈦墸雺る墺雺﹄墽雺壂雺壄雺拷".split("");
	for(a = 0; a != t[135].length; ++a)
		if(t[135][a].charCodeAt(0) !== 65533) { r[t[135][a]] = 34560 + a;
			e[34560 + a] = t[135][a] }
	t[136] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫壇雺半壉雺搽壋雺峨壏雺鸽壒雺弘壔雺诫壘雺侩妧電侂妭電冸妴電囯妶電婋妺電岆妽電庯拷锟斤拷锟斤拷锟诫姀電掚姄電曤姈電楇姏電滊姖電炿姛電㈦姢電щ姩電╇姭電姰電姳電搽姵電惦姸電凤拷锟斤拷锟斤拷锟诫姼電闺姾電浑娂電诫娋電侩媭雼侂媯雼冸媱雼呺媶雼囯媻雼嬰媿雼庪嫃雼戨嫇雼旊嫊雼栯嫍雼氹嫓雼炿嫙雼犽嫛雼ｋ嫥雼╇嫪雼半嫳雼搽嫸雼茧嫿雼倦寕雽冸寘雽嗠寚雽夒寠雽嬰寣雽嶋寧雽忞寬雽栯寳雽橂寵雽氹寷雽濍尀雽熾尃雽‰將雽ｋ尋雽ル對雽щ尐雽╇尓雽尙雽尞雽尠雽彪尣雽畴尨雽惦尪雽冯尭雽闺尯雽浑尲雽诫尵雽侩崁雿侂崅雿冸崉雿呺崋雿囯崍雿夒崐雿嬰崒雿嶋崕雿忞崘雿戨崚雿撾崡雿欕崥雿濍崰雿‰崲雿ｏ拷".split("");
	for(a = 0; a != t[136].length; ++a)
		if(t[136][a].charCodeAt(0) !== 65533) { r[t[136][a]] = 34816 + a;
			e[34816 + a] = t[136][a] }
	t[137] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫崷雿崻雿嵀雿嵅雿畴嵉雿峨嵎雿闺嵑雿浑嵓雿诫嵕雿侩巶霂嗠巼霂堧帀霂婋帇霂嶏拷锟斤拷锟斤拷锟诫帋霂忞帒霂掚帗霂曤帠霂楇帢霂欕帤霂涬帨霂濍帪霂熾帰霂ｋ帳霂ル帵霂щ帺霂帿霂拷锟斤拷锟斤拷锟诫幃霂幇霂彪幉霂畴幋霂惦幎霂冯幐霂闺幒霂浑幖霂诫幘霂侩弨霃侂弬霃冸弳霃囯弶霃婋弽霃忞彂霃掚彄霃栯彉霃氹彍霃炿彑霃‰彚霃ｋ彞霃﹄彠霃╇彧霃彫霃彯霃彴霃彪彶霃畴彺霃惦彾霃冯徃霃闺徍霃浑徑霃倦徔霅€霅侂悅霅冸悇霅呺悊霅囯悎霅夒悐霅嬰悓霅嶋悗霅忞悜霅掚悡霅旊悤霅栯悧霅欕悮霅涬悵霅炿悷霅‰悽霅ｋ悿霅ル惁霅щ惇霅惌霅惎霅半惐霅搽惓霅惦惗霅冯惛霅闺惡霅浑惣霅诫惥霅侩憖霊侂憘霊冸憚锟�".split("");
	for(a = 0; a != t[137].length; ++a)
		if(t[137][a].charCodeAt(0) !== 65533) { r[t[137][a]] = 35072 + a;
			e[35072 + a] = t[137][a] }
	t[138] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫憛霊嗠憞霊堧憠霊婋憢霊岆憤霊庪憦霊掚憮霊曤憱霊楇憴霊氹憶霊滊憹霊炿憻霊㈦懁霊︼拷锟斤拷锟斤拷锟诫懅霊懇霊懌霊懏霊懓霊彪懖霊畴懘霊惦懚霊冯懜霊闺懞霊浑懠霊诫懢霊侩拋霋傦拷锟斤拷锟斤拷锟诫拑霋勲拝霋嗠拠霋夒拪霋嬰拰霋嶋拵霋忞拹霋戨拻霋撾挃霋曤挅霋楇挊霋欕挌霋涬挏霋炿挓霋犽挕霋㈦挘霋ル挦霋щ挬霋挮霋挳霋挵霋彪挷霋畴挻霋峨捀霋弘捇霋茧捊霋倦捒霌侂搨霌冸搮霌嗠搰霌夒搳霌嬰搶霌嶋搸霌忞搼霌掚摀霌旊摉霌楇摌霌欕摎霌涬摓霌熾摗霌㈦摜霌щ摠霌╇摢霌摦霌半摬霌畴摯霌惦摱霌冯摴霌弘摶霌茧摻霌倦摽霐€霐侂攤霐冸攧霐呺攩霐囯攬霐夒攰霐嬰攲霐嶋攷霐忞攼霐戨敀霐撾敄霐楇敊霐氹敐锟�".split("");
	for(a = 0; a != t[138].length; ++a)
		if(t[138][a].charCodeAt(0) !== 65533) { r[t[138][a]] = 35328 + a;
			e[35328 + a] = t[138][a] }
	t[139] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫敒霐熾敔霐‰敘霐ｋ敠霐敩霐敭霐敳霐畴數霐峨敺霐闺敽霐浑敿霐诫斁霐侩晜霑嗭拷锟斤拷锟斤拷锟诫晣霑堧晧霑婋晭霑忞晳霑掚晸霑曤晼霑楇晿霑欕暁霑涬暈霑㈦暎霑る暐霑﹄暓霑暕霑拷锟斤拷锟斤拷锟诫暙霑暛霑暞霑半暠霑搽暢霑措暤霑峨暦霑鸽暪霑弘暬霑茧暯霑倦暱霒€霒侂杺霒冸杽霒呺枂霒囯枅霒夒枈霒嬰枌霒嶋枎霒忞枑霒戨枓霒撾枖霒曤枛霒楇枠霒欕枤霒涬枩霒濍枮霒熾枹霒ｋ枼霒﹄枾霒╇柆霒柈霒柌霒峨柗霒鸽柟霒弘柧霒侩梺霔傠梼霔呺梿霔囯棃霔夒棅霔嬰棊霔掚棑霔旊棔霔栯棗霔欕棜霔涬棞霔濍棡霔熾棤霔‰棦霔ｋ棨霔ル棪霔щ棬霔╇棯霔棴霔棷霔半棻霔搽棾霔措椀霔峨椃霔鸽椆霔弘椈霔茧椊霔倦椏锟�".split("");
	for(a = 0; a != t[139].length; ++a)
		if(t[139][a].charCodeAt(0) !== 65533) { r[t[139][a]] = 35584 + a;
			e[35584 + a] = t[139][a] }
	t[140] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫榾霕侂槀霕冸槃霕呺槅霕囯槇霕夒槉霕嬰槍霕嶋槑霕忞槖霕撾槙霕栯槜霕欕槡霕涬槣霕濓拷锟斤拷锟斤拷锟诫槥霕熾槧霕‰槩霕ｋ槫霕﹄槯霕槱霕槴霕槷霕槹霕彪槻霕畴樀霕峨樂霕鸽樄霕猴拷锟斤拷锟斤拷锟诫樆霕茧樈霕倦樋霗€霗侂檪霗冸檮霗呺檰霗囯檳霗婋檵霗岆檷霗庪檹霗愲檻霗掚檽霗旊檿霗栯櫁霗橂櫃霗氹櫅霗滊櫇霗炿櫉霗犽櫋霗㈦櫍霗ル櫐霗щ櫓霗櫕霗櫗霗櫙霗半櫛霗搽櫝霗措櫟霗峨櫡霗鸽櫣霗弘櫥霗茧櫧霗倦櫩霘€霘侂殏霘冸殑霘呺殕霘囯殘霘夒殜霘嬰殞霘嶋殠霘忞殣霘戨殥霘撾殧霘曤殩霘楇殬霘欕殮霘涬殲霘熾殹霘㈦殻霘ル殾霘щ毃霘╇毆霘毊霘毎霘搽毘霘措毜霘峨毞霘鸽毠霘弘毣霘茧毥霘倦毧霙€霙侂泜锟�".split("");
	for(a = 0; a != t[140].length; ++a)
		if(t[140][a].charCodeAt(0) !== 65533) { r[t[140][a]] = 35840 + a;
			e[35840 + a] = t[140][a] }
	t[141] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫泝霙勲泤霙嗠泧霙堧泬霙婋泲霙岆泹霙庪洀霙愲洃霙掚洆霙曤洊霙楇洏霙欕洑霙涬洔霙濓拷锟斤拷锟斤拷锟诫洖霙熾洜霙‰洟霙ｋ洡霙ル洣霙щ洦霙╇洩霙洭霙洰霙洷霙搽洺霙惦浂霙冯浌霙猴拷锟斤拷锟斤拷锟诫浕霙茧浗霙倦浛霚傠渻霚勲渾霚囯湀霚夒湂霚嬰湆霚嶋湈霚忞湊霚戨湌霚撾湐霚曤湒霚楇湗霚欕湚霚涬湝霚濍湠霚熾湢霚‰湤霚ｋ湦霚ル湨霚щ湭霚湱霚湵霚搽湷霚措湹霚峨湻霚弘溂霚诫溇霚侩潃霛侂潅霛冸潊霛嗠潎霛夒潑霛嬰潔霛庪潖霛愲潙霛掚潛霛栯潡霛橂潤霛氹潧霛滊潩霛炿潫霛‰潰霛ｋ潵霛﹄潷霛╇潽霛潿霛澁霛澆霛措澏霛冯澑霛闺澓霛浑澗霛侩瀬霝傠瀮霝呺瀱霝囯瀳霝夒瀶霝嬰瀻霝撾灁霝曤灇霝涬灊霝烇拷".split("");
	for(a = 0; a != t[141].length; ++a)
		if(t[141][a].charCodeAt(0) !== 65533) { r[t[141][a]] = 36096 + a;
			e[36096 + a] = t[141][a] }
	t[142] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫灍霝‰灑霝ｋ灓霝ル灕霝щ灙霝灟霝半灡霝搽灣霝峨灧霝闺灪霝浑灱霝诫灳霝侩焵霟侊拷锟斤拷锟斤拷锟诫焸霟冸焺霟呺焼霟堧煀霟嬰煂霟嶋煄霟忞煇霟戨煉霟撾煍霟曤煐霟楇煒霟欕煔霟涬煖霟濓拷锟斤拷锟斤拷锟诫煘霟熾煚霟‰煝霟ｋ煠霟ル煢霟щ煥霟╇煪霟煯霟煴霟搽煶霟惦煻霟冯煾霟闺熀霟浑熅霠傠爟霠勲爡霠嗠爦霠嬰爫霠庪爮霠戨爳霠撾爺霠曤爾霠楇牃霠滊牉霠熾牋霠‰牏霠ｋ牔霠щ牘霠牜霠牣霠牥霠彪牪霠畴牰霠弘牷霠茧牻霠倦牽搿侂搿冸搿嗠搿堧搿婋搿岆搿庪搿愲搿旊搿栯搿橂搿氹搿炿搿‰、搿ｋˉ搿﹄¨搿々搿～搿“搿搽〕搿措〉搿峨》搿闺『搿浑〗搿倦】擘€擘侂擘冸锟�".split("");
	for(a = 0; a != t[142].length; ++a)
		if(t[142][a].charCodeAt(0) !== 65533) { r[t[142][a]] = 36352 + a;
			e[36352 + a] = t[142][a] }
	t[143] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫擘嗠擘堧擘婋擘岆擘忞擘戨擘撾擘曤擘楇擘欕擘涬擘濍擘燂拷锟斤拷锟斤拷锟诫擘‰ⅱ擘ｋⅳ擘ルⅵ擘щⅸ擘擘擘擘彪⒉擘畴⒌擘峨⒎擘闺⒑擘浑⒓擘斤拷锟斤拷锟斤拷锟诫⒕擘侩耄勲耄囯耄夒耄嬰耄庪耄戨耄撾耄栯耄橂耄氹耄滊耄犽＂耄ｋ￥耄ル＆耄щ＊耄－耄／耄彪２耄畴４耄惦６耄冯：耄茧＞耄侩毪侂毪冸毪嗠毪堧毪婋毪岆毪庪毪愲毪掚毪旊毪栯毪欕毪涬毪濍毪熾ぁ毪㈦ぃ毪るぅ毪﹄ぇ毪ぉ毪か毪き毪く毪半け毪搽こ毪措さ毪峨し毪鸽す毪弘せ毪倦た毳侂毳冸毳嗠毳堧毳婋毳嶋毳愲毳撾毳曤毳楋拷".split("");
	for(a = 0; a != t[143].length; ++a)
		if(t[143][a].charCodeAt(0) !== 65533) { r[t[143][a]] = 36608 + a;
			e[36608 + a] = t[143][a] }
	t[144] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫毳涬毳炿毳‰ア毳ｋイ毳ルウ毳щオ毳ギ毳グ毳彪ゲ毳畴ザ毳冯ス毳弘セ毳斤拷锟斤拷锟斤拷锟诫ゾ毳侩毽侂毽冸毽堧毽岆毽愲毽掚毽旊毽栯毽橂毽氹毽滊毽烇拷锟斤拷锟斤拷锟诫毽犽Α毽㈦Γ毽るΕ毽﹄Η毽Ι毽Λ毽Ο毽彪Σ毽畴Φ毽峨Ψ毽鸽毽弘毽倦毵傠毵勲毵嗠毵婋毵嶋毵旊毵栯毵氹毵熾毵㈦Е毵щЗ毵Й毵М毵О毵彪Р毵畴Ф毵浑Ъ毵诫Ь毵侩▊毹冸▌毹呺▎毹囯▔毹婋▼毹岆◢毹庪◤毹愲☉毹掚〒毹旊毹楇毹欕毹涬毹濍毹熾毹‰á毹ｋà毹ルé毹щè毹╇í毹ì毹ó毹ò毹彪ú毹畴ù毹惦ǘ毹冯ê毹浑ń毹倦氅侂﹥氅勲﹨氅嗭拷".split("");
	for(a = 0; a != t[144].length; ++a)
		if(t[144][a].charCodeAt(0) !== 65533) { r[t[144][a]] = 36864 + a;
			e[36864 + a] = t[144][a] }
	t[145] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫﹪氅婋氅忞氅戨氅栯氅欕氅涬氅炿氅犽氅㈦氅﹄┆氅┈氅┊氅拷锟斤拷锟斤拷锟诫┎氅畴┑氅峨┓氅闺┖氅浑┘氅诫┚氅侩獉氇侂獋氇嗠獔氇夒獖氇嬰獚氇庪獜氇愲獞氇掞拷锟斤拷锟斤拷锟诫獡氇旊獣氇栯獥氇橂獧氇氹獩氇滊獫氇炿獰氇犽氇㈦氇る氇﹄氇氇氇彪氇措氇峨氇弘氇倦氆€氆侂珎氆冸珔氆嗠珖氆夒珚氆嬰珜氆嶋珟氆忞珢氆戨珤氆撾珨氆曤珫氆楇珰氆涬珳氆濍珵氆熾珷氆‰氆ｋ氆ル氆щ氆╇氆氆氆氆彪氆畴氆惦氆冯氆闺氆浑氆倦氍侂瑐氍冸瑓氍嗠瑖氍堧瑝氍婋瑡氍岆瑤氍愲瑨氍撾瑪氍曤瑬氍楇瑱氍氹瑳氍濍瑸氍熾氍㈦氍る氍﹄锟�".split("");
	for(a = 0; a != t[145].length; ++a)
		if(t[145][a].charCodeAt(0) !== 65533) { r[t[145][a]] = 37120 + a;
			e[37120 + a] = t[145][a] }
	t[146] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫氍氍氍氍彪氍畴氍闺氍侩瓈氕侂瓊氕冸瓎氕堧瓓氕嬰瓕氕庪瓚氕掞拷锟斤拷锟斤拷锟诫瓝氕曤瓥氕楇瓩氕氹瓫氕滊瓭氕炿瓱氕犽氕る氕﹄氕氕氕氕氕憋拷锟斤拷锟斤拷锟诫氕畴氕惦氕冯氕闺氕浑氕诫氕侩畝氘侂畟氘冸畡氘呺畣氘囯畨氘婋畫氘嶋畮氘忞畱氘掚畵氘旊畷氘栯畻氘橂畽氘氹疀氘滊疂氘炿疅氘犽氘㈦氘ル氘щ氘氘氘氘彪氘畴氘峨氘闺氘浑氘诫氘侩瘉氙傠瘍氙呺瘑氙囯瘔氙婋瘚氙岆瘝氙庪瘡氙戨瘨氙旊瘯氙栯瘲氙橂瘷氙氹瘺氙滊瘽氙炿療氙犽氙㈦氙る氙﹄氙氙氙氙氙半氙搽氙措氙峨氙弘氙诫氚侊拷".split("");
	for(a = 0; a != t[146].length; ++a)
		if(t[146][a].charCodeAt(0) !== 65533) { r[t[146][a]] = 37376 + a;
			e[37376 + a] = t[146][a] }
	t[147] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫皟氚勲皡氚嗠皣氚婋皫氚愲皰氚撾皺氚氹盃氚‰阿氚ｋ唉氚蔼氚艾氚隘氚搽俺氚碉拷锟斤拷锟斤拷锟诫岸氚冯肮氚弘盎氚茧敖氚倦翱氡傠眴氡囯眻氡婋眿氡庪睆氡戨睊氡撾睌氡曤睎氡楇睒氡欙拷锟斤拷锟斤拷锟诫睔氡涬睖氡炿睙氡犽薄氡㈦保氡る饱氡﹄抱氡暴氡鲍氡杯氡悲氡半北氡搽背氡措钡氡峨狈氡鸽惫氡弘被氡茧苯氡倦笨氩€氩侂矀氩冸矄氩囯矇氩婋矋氩忞矏氩戨矑氩撾矕氩橂矝氩滊矟氩炿矡氩㈦玻氩ル拨氩╇勃氩铂氩伯氩膊氩峨卜氩鸽补氩弘不氩倦部氤侂硞氤冸硡氤嗠硣氤堧硥氤婋硧氤岆硯氤掚硴氤旊硸氤楇硻氤氹硾氤濍碁氤熾碃氤‰尝氤ｋ长氤ル肠氤щ敞氤╇唱氤超氤钞氤嘲氤彪巢氤畴撤氤闺澈氤浑辰锟�".split("");
	for(a = 0; a != t[147].length; ++a)
		if(t[147][a].charCodeAt(0) !== 65533) { r[t[147][a]] = 37632 + a;
			e[37632 + a] = t[147][a] }
	t[148] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫尘氤侩磤氪侂磦氪冸磫氪堧磰氪嬰磳氪嶋磶氪忞磻氪掚磽氪曤礀氪楇礃氪欕礆氪涬礈氪濓拷锟斤拷锟斤拷锟诫礊氪熾礌氪‰储氪ｋ触氪﹄揣氪穿氪传氪串氪窗氪彪床氪畴创氪惦炊氪冯锤氪癸拷锟斤拷锟斤拷锟诫春氪浑醇氪诫淳氪侩祦氲傠祪氲勲祬氲嗠祰氲婋祴氲嶋祹氲忞祽氲掚祿氲旊禃氲栯禇氲氹禌氲滊禎氲炿禑氲犽怠氲㈦担氲ル郸氲щ旦氲但氲淡氲弹氲半当氲搽党氲措档氲峨捣氲鸽倒氲弘祷氲茧到氲倦悼攵傠秲攵呺秵攵嬰秾攵嶋稁攵忞稈攵旊稏攵楇稑攵涬稘攵炿稛攵犽丁攵㈦叮攵ル鼎攵щ定攵╇丢攵冬攵懂攵侗攵搽冻攵措兜攵峨斗攵闺逗攵浑都攵诫毒攵侩穩敕侂穫敕冸穭敕呺穯敕囯穲敕夒穵敕嬰穼敕嶋穾敕忞窅敕戯拷".split("");
	for(a = 0; a != t[148].length; ++a)
		if(t[148][a].charCodeAt(0) !== 65533) { r[t[148][a]] = 37888 + a;
			e[37888 + a] = t[148][a] }
	t[149] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫窉敕撾窎敕楇窓敕氹窙敕濍窞敕熾窢敕‰发敕ｋ筏敕ル乏敕щ法敕帆敕翻敕矾敕憋拷锟斤拷锟斤拷锟诫凡敕畴返敕峨贩敕闺泛敕浑芳敕诫肪敕侩竵敫傠竸敫嗠竾敫堧笁敫婋笅敫庪笍敫戨笒敫擄拷锟斤拷锟斤拷锟诫笗敫栯笚敫橂笝敫氹笡敫炿笭敫‰涪敫ｋ袱敫ル甫敫щ辅敫╇釜敫脯敫府敫赴敫彪覆敫畴复敫惦付敫冯父敫闺负敫浑讣敫诫妇敫侩箑牍侂箓牍冸箚牍囯箟牍婋箣牍嶋箯牍愲箲牍掚箵牍栯箻牍滊節牍炿篃牍㈦梗牍ル功牍щ供牍宫牍巩牍共牍峨狗牍鸽构牍弘咕牍侩簛牒傠簝牒呺簡牒囯簣牒夒簥牒嬰簬牒掚簱牒旊簳牒栯簵牒氹簺牒滊簼牒炿簾牒犽骸牒㈦海牒る亥牒﹄骇牒╇邯牒含牒寒牒喊牒彪翰牒畴捍牒惦憾牒凤拷".split("");
	for(a = 0; a != t[149].length; ++a)
		if(t[149][a].charCodeAt(0) !== 65533) { r[t[149][a]] = 38144 + a;
			e[38144 + a] = t[149][a] }
	t[150] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫焊牒闺汉牒浑杭牒诫壕牒侩粈牖侂粋牖冸粍牖呺粏牖囯粓牖夒粖牖嬰粚牖嶋粠牖忞粧牖擄拷锟斤拷锟斤拷锟诫粫牖栯粰牖氹粵牖滊粷牖炿粺牖‰虎牖﹄户牖哗牖猾牖划牖话牖彪徊牖畴淮牖碉拷锟斤拷锟斤拷锟诫欢牖冯桓牖闺缓牖浑患牖诫痪牖侩紑爰傠純爰勲紖爰嗠紘爰婋紜爰岆紞爰庪紡爰愲紤爰掚紦爰旊紩爰栯紬爰氹紴爰熾紶爰‰饥爰ｋ激爰ル鸡爰щ绩爰╇吉爰棘爰籍爰及爰彪疾爰畴即爰惦级爰冯几爰闺己爰浑技爰诫季爰侩絺虢冸絽虢嗠絿虢夒綂虢嬰綄虢嶋綆虢忞綊虢撾綌虢栯綏虢橂綑虢氹經虢滊綕虢炿綗虢犽健虢㈦剑虢る渐虢﹄涧虢僵虢将虢江虢蒋虢半奖虢搽匠虢措降虢峨椒虢鸽焦虢弘交虢茧浇虢倦娇刖€刖侂緜锟�".split("");
	for(a = 0; a != t[150].length; ++a)
		if(t[150][a].charCodeAt(0) !== 65533) { r[t[150][a]] = 38400 + a;
			e[38400 + a] = t[150][a] }
	t[151] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫緝刖勲緟刖嗠緡刖堧緣刖婋緥刖岆緧刖庪緩刖愲緫刖掚緭刖曤緰刖楇緲刖欕練刖涬緶刖濓拷锟斤拷锟斤拷锟诫緸刖熾緺刖‰劲刖ｋ兢刖ル睛刖щ鲸刖╇惊刖粳刖井刖颈刖搽境刖措镜刖峨痉刖革拷锟斤拷锟斤拷锟诫竟刖弘净刖茧窘刖倦究肟€肟侂總肟冸縿肟嗠繃肟堧繅肟婋繈肟庪繌肟戨繏肟撾繒肟栯織肟橂繖肟氹繘肟濍繛肟犽竣肟ｋ郡肟ル喀肟щ卡肟╇开肟楷肟慨肟堪肟彪坎肟畴看肟惦慷肟冯扛肟闺亢肟浑考肟诫烤肟快€€靲侅€傡€冹€勳€呾€嗢€囲€堨€夓€婌€嬱€岇€嶌€庫€忟€愳€戩€掛€撿€旍€曥€栰€楈€橃€欖€氺€涭€滌€濎€烄€熿€犾€§€㈧€ｌ€れ€レ€€ъ€€╈€€€€€€€办€膘€察€踌€挫€奠€鹅€缝€胳€轨€红€混€届€眷€匡拷".split("");
	for(a = 0; a != t[151].length; ++a)
		if(t[151][a].charCodeAt(0) !== 65533) { r[t[151][a]] = 38656 + a;
			e[38656 + a] = t[151][a] }
	t[152] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届亐靵侅亗靵冹亜靵呾亞靵囲亪靵夓亰靵嬱亴靵嶌亷靵忟亹靵掛亾靵旍仌靵栰仐靵欖仛靵涳拷锟斤拷锟斤拷锟届仢靵烄仧靵§仮靵ｌ仱靵レ仸靵ъ仾靵伂靵伄靵伆靵膘伈靵踌伌靵奠伓靵缝伕靵癸拷锟斤拷锟斤拷锟届伜靵混伡靵届伨靵快個靷侅倐靷冹倓靷呾倖靷囲倛靷夓倞靷嬱倢靷嶌値靷忟倰靷撿倳靷栰倵靷欖倸靷涭倻靷濎倿靷熿偄靷れ偊靷ъ偍靷╈偑靷偖靷膘偛靷缝偢靷轨偤靷混偩靸傡儍靸勳儐靸囲儕靸嬱儘靸庫儚靸戩儝靸撿償靸曥儢靸楈儦靸烄儫靸犾儭靸㈧儯靸儳靸╈儶靸儹靸儻靸办儽靸察兂靸鹅兏靸红兓靸检兘靸眷兛靹侅剛靹冹剠靹嗢剣靹夓剨靹嬱剬靹嶌剮靹忟剳靹掛創靹旍剸靹楈剺靹欖剼靹涭劇靹㈧劌靹劑靹劔靹拷".split("");
	for(a = 0; a != t[152].length; ++a)
		if(t[152][a].charCodeAt(0) !== 65533) { r[t[152][a]] = 38912 + a;
			e[38912 + a] = t[152][a] }
	t[153] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届劜靹踌劥靹奠劮靹红劵靹届劸靹快厑靺傡厓靺勳厖靺嗢厙靺婌厧靺忟厫靺戩厭靺撿厲靺楋拷锟斤拷锟斤拷锟届厵靺氺厸靺濎厼靺熿厾靺§參靺ｌ叇靺叓靺叚靺叝靺膘叢靺踌叺靺鹅叿靺轨吅靺伙拷锟斤拷锟斤拷锟届吋靺届吘靺快唨靻侅唫靻冹唲靻嗢唶靻堨唹靻婌唻靻忟啈靻掛啌靻曥啑靻橃啓靻氺啗靻烄啝靻㈧啠靻れ啨靻ъ啰靻啳靻啹靻膘啿靻踌喆靻奠喍靻缝喐靻轨喓靻混喖靻眷喛靽€靽侅噦靽冹噮靽嗢噰靽夓噴靽嬱噸靽庫噺靽愳噾靽掛嚀靽曥嚃靽欖嚉靽涭嚋靽濎嚍靽熿嚒靽㈧嚕靽レ嚘靽ъ嚛靽嚝靽嚟靽嚡靽察嚧靽奠嚩靽缝嚫靽轨嚭靽混嚲靽快垇靾傡垉靾呾垎靾囲垐靾夓垔靾嬱垘靾愳垝靾撿垟靾曥垨靾楈垰靾涭垵靾烄垺靾㈧垼锟�".split("");
	for(a = 0; a != t[153].length; ++a)
		if(t[153][a].charCodeAt(0) !== 65533) { r[t[153][a]] = 39168 + a;
			e[39168 + a] = t[153][a] }
	t[154] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届垽靾レ垿靾ъ埅靾埉靾办埑靾奠埗靾缝埜靾轨埡靾混埣靾届埦靾快墍靿侅墏靿冹墑靿咃拷锟斤拷锟斤拷锟届墕靿囲墘靿婌墜靿岇墠靿庫墢靿掛墦靿曥墫靿楈墮靿氺墰靿滌墲靿烄墴靿§墷靿ｌ墹靿︼拷锟斤拷锟斤拷锟届墽靿墿靿壂靿壇靿膘壊靿踌壍靿鹅壏靿胳壒靿红壔靿眷妧鞀傡妰鞀勳妳鞀嗢妵鞀婌妺鞀岇妽鞀庫姀鞀戩姃鞀撿姅鞀曥姈鞀楈姍鞀氺姕鞀烄姛鞀犾姟鞀㈧姡鞀姧鞀╈姫鞀姰鞀姲鞀膘姴鞀踌姸鞀胳姾鞀混娂鞀届娋鞀快媭鞁侅媯鞁冹媱鞁呾媶鞁囲媹鞁夓媻鞁嬱媽鞁嶌嫀鞁忟嫄鞁戩嫆鞁撿嫈鞁曥嫋鞁楈嫎鞁欖嫐鞁涭嫗鞁熿嫛鞁㈧嫢鞁嫥鞁嫨鞁嫯鞁办嫴鞁踌嫶鞁奠嫹鞁红嫿鞁眷嬁鞂侅寕鞂冹寗鞂呾寙鞂囲寠鞂嬱寧鞂忥拷".split("");
	for(a = 0; a != t[154].length; ++a)
		if(t[154][a].charCodeAt(0) !== 65533) { r[t[154][a]] = 39424 + a;
			e[39424 + a] = t[154][a] }
	t[155] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届寪鞂戩寬鞂栰寳鞂欖寶鞂涭対鞂烄専鞂犾尅鞂㈧專鞂導鞂尗鞂尛鞂尟鞂办尡鞂诧拷锟斤拷锟斤拷锟届尦鞂挫尩鞂鹅尫鞂胳尮鞂红尰鞂检尳鞂眷尶鞃€鞃侅崅鞃冹崉鞃嗢崌鞃堨崏鞃婌崑鞃岇崓锟斤拷锟斤拷锟斤拷鞃庫崗鞃愳崙鞃掛崜鞃旍崟鞃栰崡鞃橃崣鞃氺崨鞃滌崫鞃烄崯鞃犾崱鞃㈧崳鞃れ崶鞃崸鞃崼鞃嵁鞃嵄鞃踌嵈鞃奠嵍鞃缝嵑鞃混嵕鞃快巰鞄侅巶鞄冹巺鞄嗢巼鞄夓帄鞄嬱帊鞄庫帍鞄愳帒鞄掛帗鞄旍帟鞄栰帡鞄橃帣鞄氺帥鞄滌帩鞄烄師鞄犾帯鞄㈧帲鞄れ帴鞄帶鞄帺鞄帿鞄幁鞄幆鞄办幈鞄察幊鞄挫幍鞄鹅幏鞄胳幑鞄红幓鞄检幗鞄眷幙鞆侅弬鞆冹弰鞆呾弳鞆囲張鞆夓強鞆嬱弻鞆嶌弾鞆忟彁鞆戩彃鞆撿彅鞆曥彇鞆楈彋锟�".split("");
	for(a = 0; a != t[155].length; ++a)
		if(t[155][a].charCodeAt(0) !== 65533) { r[t[155][a]] = 39680 + a;
			e[39680 + a] = t[155][a] }
	t[156] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届彌鞆濎彏鞆§彛鞆れ彞鞆彠鞆彨鞆彯鞆彴鞆膘彶鞆踌彾鞆缝徆鞆红徎鞆检徑鞆撅拷锟斤拷锟斤拷锟届徔鞇€鞇侅悅鞇冹悇鞇呾悊鞇囲悏鞇婌悑鞇岇悕鞇庫悘鞇戩悞鞇撿悢鞇曥悥鞇楈悩鞇欖悮锟斤拷锟斤拷锟斤拷鞇涭悳鞇濎悶鞇熿悹鞇§悽鞇ｌ惀鞇惂鞇惄鞇惈鞇惍鞇惐鞇察惓鞇奠惗鞇缝惛鞇轨惡鞇混惥鞇快憖鞈侅憘鞈冹憚鞈呾憜鞈囲憠鞈婌憢鞈岇憤鞈庫憦鞈愳憫鞈掛憮鞈旍憰鞈栰憲鞈橃憴鞈氺憶鞈滌憹鞈烄憻鞈犾憽鞈㈧懀鞈懅鞈╈應鞈懎鞈懐鞈办懕鞈察懗鞈鹅懛鞈胳懞鞈混懠鞈届懢鞈快拋鞉傡拑鞉勳拝鞉嗢拠鞉堨拤鞉婌拫鞉岇拲鞉庫拸鞉愳拺鞉掛挀鞉曥挅鞉楈挊鞉欖挌鞉涭挐鞉烄挓鞉犾挕鞉㈧挘鞉れ挜鞉挧鞉挬锟�".split("");
	for(a = 0; a != t[156].length; ++a)
		if(t[156][a].charCodeAt(0) !== 65533) { r[t[156][a]] = 39936 + a;
			e[39936 + a] = t[156][a] }
	t[157] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届挭鞉挰鞉挳鞉挵鞉膘挷鞉踌挻鞉奠挾鞉缝捁鞉红捇鞉届捑鞉快搥鞊侅搨鞊冹搫鞊咃拷锟斤拷锟斤拷锟届搯鞊囲搱鞊夓搳鞊嬱搶鞊嶌搸鞊忟搻鞊戩搾鞊撿摂鞊曥摉鞊楈摌鞊欖摎鞊涭摐鞊濎摓鞊燂拷锟斤拷锟斤拷锟届摖鞊§摙鞊ｌ摛鞊レ摝鞊ъ摠鞊摣鞊摥鞊摨鞊察摮鞊奠摱鞊缝摴鞊混摷鞊届摼鞌傡攦鞌勳攨鞌嗢攪鞌堨攭鞌婌攱鞌嶌攷鞌忟攽鞌掛敁鞌曥敄鞌楈敇鞌欖敋鞌涭敐鞌烄敓鞌犾敗鞌㈧敚鞌れ敟鞌敡鞌敨鞌敭鞌敱鞌察敵鞌挫數鞌鹅敺鞌红敿鞌眷斂鞎€鞎侅晜鞎冹晢鞎囲晪鞎忟晲鞎戩晵鞎栰暁鞎涭暅鞎熿暍鞎ｌ暐鞎暓鞎╈暘鞎暚鞎暜鞎暡鞎鹅暦鞎胳暪鞎红暬鞎眷暱鞏侅杺鞏冹枀鞏嗢枅鞏夓枈鞏嬱枎鞏愳枓鞏撿枖锟�".split("");
	for(a = 0; a != t[157].length; ++a)
		if(t[157][a].charCodeAt(0) !== 65533) { r[t[157][a]] = 40192 + a;
			e[40192 + a] = t[157][a] }
	t[158] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届枛鞏欖枤鞏涭枬鞏烄枱鞏§枹鞏ｌ枻鞏レ枽鞏ъ枿鞏柅鞏柇鞏柉鞏办柋鞏察柍鞏讹拷锟斤拷锟斤拷锟届柗鞏红柨鞐€鞐侅梻鞐冹棆鞐嶌棌鞐掛棑鞐曥棖鞐楈棛鞐氺棝鞐滌棟鞐烄棢鞐㈧棨鞐棫锟斤拷锟斤拷锟斤拷鞐棭鞐棲鞐棻鞐察棾鞐奠椄鞐轨椇鞐混槀鞓冹槃鞓夓槉鞓嬱槏鞓庫槒鞓戩槖鞓撿様鞓曥槚鞓楈槡鞓濎槥鞓熿槧鞓§槩鞓ｌ槮鞓ъ槱鞓槴鞓槺鞓察樁鞓胳樅鞓检樈鞓眷樋鞕傡檭鞕呾檰鞕囲檳鞕婌檵鞕岇檷鞕庫檹鞕掛櫀鞕楈櫂鞕欖櫄鞕涭櫈鞕熿櫋鞕㈧櫍鞕れ櫏鞕櫑鞕櫓鞕櫕鞕櫘鞕办櫜鞕踌櫞鞕奠櫠鞕缝櫤鞕混櫧鞕眷櫩鞖侅殏鞖冹殑鞖呾殕鞖囲殜鞖岇殠鞖忟殣鞖戩殥鞖撿殩鞖楈殭鞖氺殯鞖濎殲鞖熿殸鞖§殺鞖ｌ殾锟�".split("");
	for(a = 0; a != t[158].length; ++a)
		if(t[158][a].charCodeAt(0) !== 65533) { r[t[158][a]] = 40448 + a;
			e[40448 + a] = t[158][a] }
	t[159] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届毃鞖毇鞖毉鞖毌鞖察毘鞖奠毝鞖缝毣鞖检毥鞖眷毧鞗傡泟鞗嗢泧鞗堨泬鞗婌泲鞗庯拷锟斤拷锟斤拷锟届洀鞗戩洅鞗撿洉鞗栰洍鞗橃洐鞗氺洓鞗烄洘鞗㈧洠鞗れ洢鞗洤鞗洬鞗洰鞗洷鞗诧拷锟斤拷锟斤拷锟届洺鞗挫浀鞗鹅浄鞗红浕鞗检浘鞗快渶鞙侅渹鞙冹渾鞙囲湁鞙婌湅鞙嶌湈鞙忟湊鞙戩湌鞙撿湒鞙橃湚鞙涭湝鞙濎湠鞙熿湤鞙ｌ湧鞙湩鞙╈湭鞙湰鞙湲鞙湶鞙挫湺鞙胳湽鞙红溁鞙眷溈鞚侅潅鞚冹潊鞚嗢潎鞚堨潐鞚嬱潕鞚愳潤鞚氺潧鞚濎潪鞚熿潯鞚㈧潱鞚れ潵鞚潷鞚╈潽鞚澀鞚澂鞚办澅鞚察澇鞚鹅澐鞚轨澓鞚混澘鞛€鞛侅瀭鞛嗢瀷鞛岇瀺鞛忟瀿鞛撿灂鞛欖灈鞛滌灊鞛烄灍鞛㈧灖鞛灘鞛灚鞛灟鞛膘灢鞛踌灥鞛鹅灧锟�".split("");
	for(a = 0; a != t[159].length; ++a)
		if(t[159][a].charCodeAt(0) !== 65533) { r[t[159][a]] = 40704 + a;
			e[40704 + a] = t[159][a] }
	t[160] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届灨鞛轨灪鞛混灳鞜傡焹鞜勳焻鞜嗢焽鞜婌煁鞜嶌煆鞜戩煉鞜撿煍鞜曥煐鞜楈煓鞜氺煕鞜滐拷锟斤拷锟斤拷锟届煘鞜熿煚鞜§煝鞜ｌ煡鞜煣鞜╈煪鞜煭鞜煰鞜办煴鞜察煶鞜挫煹鞜鹅煼鞜胳煿鞜猴拷锟斤拷锟斤拷锟届熁鞜检熃鞜眷熆鞝傡爟鞝呾爢鞝囲爥鞝嬱爩鞝嶌爭鞝忟爳鞝旍牀鞝橃牂鞝氺牄鞝烄牊鞝§牏鞝ｌ牓鞝牕鞝牘鞝牜鞝牥鞝察牫鞝挫牭鞝鹅牱鞝轨牶鞝混牻鞝眷牽臁侅臁冹臁呾臁囲臁嬱臁忟臁戩臁撿臁栰臁橃臁氺臁滌臁烄臁犾　臁㈧。臁れˉ臁¨臁々臁～臁…臁’臁察〕臁奠《臁缝」臁混〖臁届【臁快膦勳膦夓膦庫膦愳膦掛膦曥膦楈膦欖膦涭膦烄膦㈧ⅲ膦わ拷".split("");
	for(a = 0; a != t[160].length; ++a)
		if(t[160][a].charCodeAt(0) !== 65533) { r[t[160][a]] = 40960 + a;
			e[40960 + a] = t[160][a] }
	t[161] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届ⅴ膦ⅶ膦╈ⅹ膦膦膦膦膘⒉膦踌⒋膦奠⒍膦缝⒏膦轨⒑膦混⒕膦快欤侊拷锟斤拷锟斤拷锟届欤冹欤嗢欤夓欤嬱欤庫欤愳欤掛欤栰欤氺欤滌欤烄欤㈧＃欤ワ拷锟斤拷锟斤拷锟届＆欤ъ（欤╈＊欤，欤．欤０欤膘２欤踌４欤鹅７欤胳９欤红；欤眷？欷侅欷冹欷堨欷婌欷庛€€銆併€偮封€モ€βㄣ€兟€曗垾锛尖埣鈥樷€欌€溾€濄€斻€曘€堛€夈€娿€嬨€屻€嶃€庛€忋€愩€懧泵椕封墵鈮も墺鈭炩埓掳鈥测€斥剝鈩繝锟★骏鈾傗檧鈭犫姤鈱掆垈鈭団墶鈮捖р€烩槅鈽呪棆鈼忊棊鈼団梿鈻♀枲鈻斥柌鈻解柤鈫掆啇鈫戔啌鈫斻€撯壀鈮垰鈭解垵鈭碘埆鈭垐鈭嬧妴鈯団妭鈯冣埅鈭┾埀鈭竣锟�".split("");
	for(a = 0; a != t[161].length; ++a)
		if(t[161][a].charCodeAt(0) !== 65533) { r[t[161][a]] = 41216 + a;
			e[41216 + a] = t[161][a] }
	t[162] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届欷掛欷旍欷栰欷欖欷涭欷濎欷熿欷§あ欷ｌい欷レう欷ъえ欷╈お欷拷锟斤拷锟斤拷锟届き欷く欷办け欷察こ欷奠ざ欷缝じ欷轨ず欷混ぜ欷届ぞ欷快欹侅欹冹欹呾欹囷拷锟斤拷锟斤拷锟届欹夓欹嬱欹嶌欹忟欹撿欹栰欹欖欹涭欹濎欹熿ア欹れゥ欹ェ欹ォ欹カ欹ギ欹噿鈬斺垁鈭兟达綖藝藰藵藲藱赂藳隆驴藧鈭垜鈭徛も剦鈥扳梺鈼€鈻封柖鈾も櫊鈾♀櫏鈾р櫍鈯欌棃鈻ｂ棎鈼戔枓鈻も枼鈻ㄢ枾鈻︹柀鈾ㄢ槒鈽庘槣鈽灺垛€犫€♀啎鈫椻啓鈫栤啒鈾櫓鈾櫖銐裤垳鈩栥弴鈩弬銖樷劇鈧拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[162].length; ++a)
		if(t[162][a].charCodeAt(0) !== 65533) { r[t[162][a]] = 41472 + a;
			e[41472 + a] = t[162][a] }
	t[163] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届ケ欹察コ欹奠ザ欹缝ジ欹轨ズ欹混ソ欹眷タ歃€歃侅歃冹歃呾歃囲歃嬱歃庫锟斤拷锟斤拷锟斤拷歃戩歃撿歃曥歃楈歃滌歃熿歃§Β歃ｌΔ歃レΖ歃ъΘ歃╈Κ歃Μ歃Ξ锟斤拷锟斤拷锟斤拷歃Π歃膘Σ歃踌Υ歃奠Χ歃缝Ω歃轨歃混歃届歃快歆冹歆嗢歆嬱歆嶌歆忟歆旍歆橃锛侊紓锛冿紕锛咃紗锛囷紙锛夛紛锛嬶紝锛嶏紟锛忥紣锛戯紥锛擄紨锛曪紪锛楋紭锛欙細锛涳紲锛濓紴锛燂紶锛★饥锛ｏ激锛ワ鸡锛э绩锛╋吉锛棘锛籍锛及锛憋疾锛筹即锛碉级锛凤几锛癸己锛伙喀锛斤季锛匡絸锝侊絺锝冿絼锝咃絾锝囷綀锝夛綂锝嬶綄锝嶏綆锝忥綈锝戯綊锝擄綌锝曪綎锝楋綐锝欙綒锝涳綔锝濓浚锟�".split("");
	for(a = 0; a != t[163].length; ++a)
		if(t[163][a].charCodeAt(0) !== 65533) { r[t[163][a]] = 41728 + a;
			e[41728 + a] = t[163][a] }
	t[164] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届歆熿А歆ｌД歆Ж歆╈И歆М歆察С歆挫У歆鹅Х歆红Щ歆届Ь歆快▉歙傡▋歙勶拷锟斤拷锟斤拷锟届▍歙嗢▏歙婌◣歙忟◥歙戩⊕歙撿〞歙栰歙欖歙涭歙濎歙熿歙§á歙ｌà歙ワ拷锟斤拷锟斤拷锟届é歙ъè歙ǐ歙ō歙ǒ歙办ū歙察ǔ歙挫ǖ歙鹅ǚ歙胳ü歙红ɑ歙检ń歙眷飑€飑侅﹤飑冹﹦飑呾﹩銊便劜銊炽劥銊点劧銊枫劯銊广労銊汇劶銊姐劸銊裤厐銋併厒銋冦厔銋呫厗銋囥厛銋夈厞銋嬨厡銋嶃厧銋忋厫銋戙厭銋撱厰銋曘厲銋椼厴銋欍厷銋涖厹銋濄厼銋熴厾銋°參銋ｃ叅銋ャ叇銋с叏銋┿叒銋叕銋叜銋叞銋便叢銋炽叴銋点叾銋枫吀銋广吅銋汇吋銋姐吘銋裤唨銌併唫銌冦唲銌呫唵銌囥唸銌夈唺銌嬨唽銌嶃啂锟�".split("");
	for(a = 0; a != t[164].length; ++a)
		if(t[164][a].charCodeAt(0) !== 65533) { r[t[164][a]] = 41984 + a;
			e[41984 + a] = t[164][a] }
	t[165] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届﹪飑堨飑婌飑庫飑戩飑撿飑栰飑橃飑氺飑烄飑ｌ─飑レ│飑ъ┅飑拷锟斤拷锟斤拷锟届┇飑┉飑┋飑办┍飑察┏飑挫┑飑鹅┓飑胳┕飑红┗飑检┚飑快獉飒侅獋飒冹獏飒嗭拷锟斤拷锟斤拷锟届獓飒堨獕飒婌獘飒岇獚飒庫獜飒愳獞飒掛獡飒旍獣飒栰獥飒欖獨飒涭獪飒濎獮飒熿獱飒§飒ｌ飒レ飒р叞鈪扁叢鈪斥叴鈪碘叾鈪封吀鈪癸拷锟斤拷锟斤拷鈪犫叀鈪⑩叄鈪も叆鈪︹収鈪ㄢ叐锟斤拷锟斤拷锟斤拷锟轿懳捨撐斘曃栁椢樜櫸毼浳溛澪炍熚犖∥Ｎのノξㄎ╋拷锟斤拷锟斤拷锟斤拷锟轿蔽参澄次滴段肺肝刮何晃嘉轿疚肯€蟻蟽蟿蠀蠁蠂蠄蠅锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[165].length; ++a)
		if(t[165][a].charCodeAt(0) !== 65533) { r[t[165][a]] = 42240 + a;
			e[42240 + a] = t[165][a] }
	t[166] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届飒╈飒飒飒飒膘飒踌飒奠飒缝飒轨飒混飒快珌飓傡珒飓咃拷锟斤拷锟斤拷锟届珕飓囲珗飓夓珚飓嬱珟飓愳珤飓旍珪飓栰珬飓氺珱飓滌珴飓烄珶飓§飓ｌ飓レ飓э拷锟斤拷锟斤拷锟届飓╈飓飓飓办飓察飓奠飓缝飓轨飓混飓届飓快瑎飕侅瑐飕冹瑒飕呾瑔飕囲瑝飕娾攢鈹傗攲鈹愨敇鈹斺敎鈹敜鈹粹敿鈹佲攦鈹忊敁鈹涒敆鈹ｂ敵鈹敾鈺嬧敔鈹敤鈹封斂鈹濃敯鈹モ敻鈺傗敀鈹戔敋鈹欌敄鈹曗攷鈹嶁敒鈹熲敗鈹⑩敠鈹р敥鈹敪鈹敱鈹测數鈹垛敼鈹衡斀鈹锯晙鈺佲晝鈺勨晠鈺嗏晣鈺堚晧鈺婏拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[166].length; ++a)
		if(t[166][a].charCodeAt(0) !== 65533) { r[t[166][a]] = 42496 + a;
			e[42496 + a] = t[166][a] }
	t[167] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届瑡飕岇瑣飕庫瑥飕戩瑨飕撿瑫飕栰瑮飕欖瑲飕涭瑴飕濎瑸飕熿飕ｌ飕レ飕ъ飕╋拷锟斤拷锟斤拷锟届飕飕飕飕膘飕踌飕奠飕缝飕轨飕混飕届飕快瓈飙傡瓋飙勶拷锟斤拷锟斤拷锟届瓍飙嗢瓏飙婌瓔飙嶌瓗飙忟瓚飙掛瓝飙旍瓡飙栰瓧飙氺瓫飙滌瓰飙熿瓲飙§飙ｌ飙飙飙飙帟銕栥帡鈩撱帢銖勩帲銕ゃ帴銕︺帣銕氥帥銕溿帩銕炪師銕犮帯銕強銕嶃帋銕忋弿銕堛帀銖堛帶銕ㄣ幇銕便幉銕炽幋銕点幎銕枫幐銕广巰銕併巶銕冦巹銕恒幓銕笺幗銕俱幙銕愩帒銕掋帗銕斺劍銖€銖併帄銕嬨帉銖栥弲銕幃銕彌銕┿帾銕幀銖濄彁銖撱弮銖夈彍銖嗭拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[167].length; ++a)
		if(t[167][a].charCodeAt(0) !== 65533) { r[t[167][a]] = 42752 + a;
			e[42752 + a] = t[167][a] }
	t[168] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届飙飙办飙察飙挫飙鹅飙红飙检飙眷飚€飚侅畟飚冹畡飚呾畣飚囲畧锟斤拷锟斤拷锟斤拷飚夓畩飚嬱畬飚嶌畮飚忟異飚戩畳飚撿當飚曥畺飚楈畼飚欖畾飚涭疂飚烄疅飚犾飚㈧锟斤拷锟斤拷锟斤拷飚れ飚飚飚飚飚飚办飚察飚挫飚鹅飚轨飚混飚届飚快瘈殳侅瘋殳冹瘎脝脨陋摩锟侥诧拷目艁脴艗潞脼纽艎锟姐墵銐°墷銐ｃ墹銐ャ墻銐с墾銐┿壀銐壃銐壆銐壈銐便壊銐炽壌銐点壎銐枫壐銐广壓銐烩搻鈸戔搾鈸撯摂鈸曗摉鈸椻摌鈸欌摎鈸涒摐鈸濃摓鈸熲摖鈸♀摙鈸ｂ摛鈸モ摝鈸р摠鈸┾憼鈶♀憿鈶ｂ懁鈶モ懄鈶р懆鈶┾應鈶懍鈶懏陆鈪撯厰录戮鈪涒厹鈪濃厼锟�".split("");
	for(a = 0; a != t[168].length; ++a)
		if(t[168][a].charCodeAt(0) !== 65533) { r[t[168][a]] = 43008 + a;
			e[43008 + a] = t[168][a] }
	t[169] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届瘏殳嗢瘒殳堨瘔殳婌瘚殳岇瘝殳庫瘡殳愳瘧殳掛瘬殳曥瘱殳楈瘶殳欖瘹殳涭瘻殳濎癁殳燂拷锟斤拷锟斤拷锟届癄殳§殳ｌ殳殳殳殳殳办殳察殳挫殳鹅殳胳殳红殳硷拷锟斤拷锟斤拷锟届殳眷彀€彀侅皞彀冹皠彀呾皢彀囲皥彀夓皧彀嬱皫彀忟皯彀掛皳彀曥皷彀楈皹彀欖皻彀涭盀彀熿盃彀ｌ挨忙膽冒魔谋某母艀艂酶艙脽镁脓艐艍銏€銏併垈銏冦垊銏呫垎銏囥垐銏夈垔銏嬨垖銏嶃垘銏忋垚銏戙垝銏撱垟銏曘垨銏椼垬銏欍垰銏涒挏鈷濃挒鈷熲挔鈷♀挗鈷ｂ挙鈷モ挦鈷р挩鈷┾挭鈷挰鈷挳鈷挵鈷扁挷鈷斥挻鈷碘懘鈶碘懚鈶封懜鈶光懞鈶烩懠鈶解懢鈶库拃鈷佲拏鹿虏鲁鈦粹伩鈧佲倐鈧冣倓锟�".split("");
	for(a = 0; a != t[169].length; ++a)
		if(t[169][a].charCodeAt(0) !== 65533) { r[t[169][a]] = 43264 + a;
			e[43264 + a] = t[169][a] }
	t[170] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届哎彀蔼彀碍彀氨彀察俺彀挫暗彀鹅胺彀红翱毂€毂侅眰毂冹眴毂囲眽毂婌眿毂嶌睅锟斤拷锟斤拷锟斤拷毂忟睈毂戩睊毂撿睎毂氺睕毂滌睗毂烄睙毂§雹毂ｌ饱毂ъ暴毂鲍毂杯毂悲毂膘辈锟斤拷锟斤拷锟斤拷毂踌贝毂鹅狈毂胳惫毂红被毂检苯毂眷笨觳€觳侅矀觳冹矂觳呾矄觳囲矆觳夓矈觳嬱矊觳嶌矌觳忟矏觳戩矑觳撱亖銇傘亙銇勩亝銇嗐亣銇堛亯銇娿亱銇屻亶銇庛亸銇愩亼銇掋亾銇斻仌銇栥仐銇樸仚銇氥仜銇溿仢銇炪仧銇犮仭銇仯銇ゃ仴銇︺仹銇ㄣ仼銇伀銇伃銇伅銇般伇銇层伋銇淬伒銇躲伔銇搞伖銇恒伝銇笺伣銇俱伩銈€銈併倐銈冦倓銈呫倖銈囥倛銈夈倞銈嬨倢銈嶃値銈忋倫銈戙倰銈擄拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[170].length; ++a)
		if(t[170][a].charCodeAt(0) !== 65533) { r[t[170][a]] = 43520 + a;
			e[43520 + a] = t[170][a] }
	t[171] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届矓觳曥矕觳楈矚觳涭矟觳烄矡觳§并觳ｌ菠觳レ拨觳ъ勃觳帛觳办脖觳察渤觳鹅卜觳癸拷锟斤拷锟斤拷锟届埠觳混步觳眷部斐€斐侅硞斐冹硢斐堨硦斐嬱硨斐嶌硯斐忟硲斐掛硴斐曥硸斐楈硺斐欖硽锟斤拷锟斤拷锟斤拷斐涭硿斐濎碁斐熿碃斐§尝斐ｌ偿斐厂斐畅斐倡斐钞斐潮斐察吵斐挫车斐鹅撤斐胳彻斐红郴斐检辰銈°偄銈ｃ偆銈ャ偊銈с偍銈┿偑銈偓銈偖銈偘銈便偛銈炽偞銈点偠銈枫偢銈广偤銈汇偧銈姐偩銈裤儉銉併儌銉冦儎銉呫儐銉囥儓銉夈儕銉嬨儗銉嶃儙銉忋儛銉戙儝銉撱償銉曘儢銉椼儤銉欍儦銉涖儨銉濄優銉熴儬銉°儮銉ｃ儰銉ャ儲銉с儴銉┿儶銉儸銉儺銉儼銉便儾銉炽兇銉点兌锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[171].length; ++a)
		if(t[171][a].charCodeAt(0) !== 65533) { r[t[171][a]] = 43776 + a;
			e[43776 + a] = t[171][a] }
	t[172] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届尘斐快磤齑傡磧齑勳磪齑嗢磭齑婌磱齑嶌磶齑忟磻齑掛磽齑旍磿齑栰礂齑氺礈齑烄礋齑狅拷锟斤拷锟斤拷锟届础齑㈧矗齑レ处齑ъ穿齑传齑串齑窗齑膘床齑踌创齑奠炊齑缝锤齑红椿齑检唇齑撅拷锟斤拷锟斤拷锟届纯斓€斓侅祩斓冹祫斓呾祮斓囲祱斓夓祳斓嬱祵斓嶌祹斓忟祼斓戩祾斓撿禂斓曥禆斓楈禈斓欖禋斓涭禎斓烄禑袗袘袙袚袛袝衼袞袟袠袡袣袥袦袧袨袩袪小孝校肖啸笑效楔些歇蝎鞋协挟携锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟叫靶毙残承葱笛懶缎沸感剐盒恍夹叫拘垦€褋褌褍褎褏褑褔褕褖褗褘褜褝褞褟锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[172].length; ++a)
		if(t[172][a].charCodeAt(0) !== 65533) { r[t[172][a]] = 44032 + a;
			e[44032 + a] = t[172][a] }
	t[173] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届怠斓㈧担斓レ郸斓ъ胆斓╈氮斓诞斓办挡斓踌荡斓奠刀斓缝倒斓红祷斓检到斓眷悼於€锟斤拷锟斤拷锟斤拷於侅秱於冹秳於呾秵於囲秹於婌秼於岇秿於庫稄於愳稇於掛稉於栰稐於欖稓於涭稘於烄稛锟斤拷锟斤拷锟斤拷於犾丁於㈧叮於定於东於董於动於膘恫於踌洞於奠抖於缝陡於轨逗於混都於届毒於快穩旆侅穫旆冹穮锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[173].length; ++a)
		if(t[173][a].charCodeAt(0) !== 65533) { r[t[173][a]] = 44288 + a;
			e[44288 + a] = t[173][a] }
	t[174] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届穯旆囲穲旆夓穵旆嬱穽旆庫窂旆戩窉旆撿窋旆曥窎旆楈窐旆欖窔旆涭窚旆濎窞旆熿窢旆★拷锟斤拷锟斤拷锟届发旆ｌ筏旆レ乏旆ъ珐旆帆旆樊旆繁旆察烦旆挫返旆鹅贩旆红芳旆眷房旄€旄侅競锟斤拷锟斤拷锟斤拷旄冹竻旄嗢竾旄夓笂旄嬱笉旄庫笍旄愳笐旄掛笓旄曥笘旄楈笜旄氺笡旄滌笣旄烄笩旄㈧福旄レ甫旄ъ俯旄斧锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[174].length; ++a)
		if(t[174][a].charCodeAt(0) !== 65533) { r[t[174][a]] = 44544 + a;
			e[44544 + a] = t[174][a] }
	t[175] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届脯旄府旄覆旄挫付旄缝父旄轨负旄混讣旄届妇旄快箑旃侅箓旃冹箘旃呾箚旃囲箞旃夛拷锟斤拷锟斤拷锟届箠旃嬱箤旃嶌箮旃忟箰旃戩箳旃撿箶旃曥箹旃楈箽旃涭節旃烄耿旃ｌ工旃レ功旃ъ躬旃拷锟斤拷锟斤拷锟届巩旃拱旃膘共旃踌苟旃缝构旃红够旃届咕旃快簚旌侅簜旌冹簡旌堨簥旌嬱簩旌嶌簬旌忟簰旌撿簳旌栰簵旌欙拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[175].length; ++a)
		if(t[175][a].charCodeAt(0) !== 65533) { r[t[175][a]] = 44800 + a;
			e[44800 + a] = t[175][a] }
	t[176] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届簹旌涭簻旌濎簽旌熿孩旌骇旌憨旌韩旌函旌办罕旌察撼旌挫旱旌鹅悍旌胳汗旌猴拷锟斤拷锟斤拷锟届夯旌检航旌眷嚎旎€旎傡粌旎勳粎旎嗢粐旎堨粔旎婌粙旎岇粛旎庫粡旎愳粦旎掛粨旎旍粫锟斤拷锟斤拷锟斤拷旎栰粭旎橃粰旎氺粵旎滌粷旎烄粺旎犾弧旎㈧唬旎户旎╈华旎划旎话旎膘徊旎踌欢旎红换旎检唤旎眷豢臧€臧侁皠臧囮皥臧夑皧臧愱皯臧掙皳臧旉皶臧栮皸臧欔皻臧涥皽臧濌盃臧り艾臧隘臧瓣氨臧戈肮臧缄眬瓯嬯睄瓯旉睒瓯滉卑瓯标贝瓯逢备瓯宏瞼瓴侁矁瓴勱矃瓴嗞矇瓴婈矉瓴岅矏瓴旉矞瓴濌矡瓴犼病瓴博瓴铂瓴舶瓴戈补瓴魂布瓴疥硜瓿勱硤瓿岅硶瓿楆碃瓿￡长瓿ш敞瓿超瓿嘲瓿标吵瓿店扯瓿缄辰甏€甏勱磫锟�".split("");
	for(a = 0; a != t[176].length; ++a)
		if(t[176][a].charCodeAt(0) !== 65533) { r[t[176][a]] = 45056 + a;
			e[45056 + a] = t[176][a] }
	t[177] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届紓旒冹紖旒嗢紘旒夓紛旒嬱紝旒嶌紟旒忟紥旒旍紪旒楈紭旒欖細旒涭紳旒烄紵旒§饥旒ｏ拷锟斤拷锟斤拷锟届讥旒姬旒缉旒极旒疾旒踌即旒奠级旒缝脊旒红蓟旒检冀旒眷伎旖€旖侅絺旖冹絼锟斤拷锟斤拷锟斤拷旖呾絾旖囲綀旖夓綂旖嬱綄旖嶌綆旖忟綈旖戩綊旖撿綎旖楈綑旖氺經旖濎綖旖熿綘旖§舰旖ｌ溅旖姜旖浆甏岅磵甏応磻甏橁礈甏犼穿甏喘甏搓吹甏戈醇甑勱祬甑囮祲甑愱禂甑橁怠甑ｊ惮甑蛋甑酬荡甑店刀甑魂导甑疥悼甓侁秱甓堦秹甓岅稅甓滉稘甓り斗攴€攴侁穭攴堦窅攴戧窊攴滉窢攴り犯攴龟芳攴筷竴旮侁笀旮夑笅旮嶊笖旮瓣副旮搓阜旮戈负旯€旯侁箖旯呹箚旯婈箤旯嶊箮旯愱箶旯栮箿旯濌篃旯犼埂旯リ龚旯╆宫旯瓣垢锟�".split("");
	for(a = 0; a != t[177].length; ++a)
		if(t[177][a].charCodeAt(0) !== 65533) { r[t[177][a]] = 45312 + a;
			e[45312 + a] = t[177][a] }
	t[178] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届江旖蒋旖察匠旖奠蕉旖缝焦旖红交旖检浇旖眷娇炀侅緜炀冹緞炀嗢緡炀堨緣炀婌緥炀嶏拷锟斤拷锟斤拷锟届編炀忟緪炀戩緬炀撿緮炀曥緰炀楈緲炀欖練炀涭緶炀濎緸炀熿緺炀㈧荆炀れ茎炀晶炀╋拷锟斤拷锟斤拷锟届惊炀粳炀井炀颈炀察境炀挫镜炀鹅痉炀胳竟炀红净炀检窘炀眷究炜€炜侅總炜冹繀炜嗢繃炜堨繅炜婌繈旯龟够旯缄菇旰勱簠旰岅杭旰疥壕昊€昊勱粚昊嶊粡昊愱粦昊橁粰昊滉花昊画昊搓桓昊缄紘昙堦紞昙愱棘昙及昙碴即昙缄冀昙筷絹杲傟絻杲堦綁杲愱綔杲濌饯杲リ焦昃€昃勱緢昃愱緫昃曣緶昃戈竟昃缄縺昕囮繄昕夑繈昕嶊繋昕旉繙昕咯昕瓣勘昕搓扛雬€雬侂€勲€岆€愲€旊€滊€濍€亜雭呺亪雭婋亴雭庪亾雭旊仌雭楇仚锟�".split("");
	for(a = 0; a != t[178].length; ++a)
		if(t[178][a].charCodeAt(0) !== 65533) { r[t[178][a]] = 45568 + a;
			e[45568 + a] = t[178][a] }
	t[179] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟届繉炜嶌繋炜忟繍炜戩繏炜撿繑炜曥繓炜楈繕炜欖繗炜涭繙炜濎繛炜熿竣炜ｌ骏炜咖炜╋拷锟斤拷锟斤拷锟届开炜楷炜慨炜坎炜挫慷炜缝扛炜轨亢炜混拷炜眷靠韤來€傢€冺€呿€嗧€図€堩€夗€婏拷锟斤拷锟斤拷锟巾€嬳€岉€嶍€庬€忢€愴€掜€擁€旐€曧€栱€楉€欗€氻€涰€滍€濏€烅€燀€犿€№€㈨€ｍ€ろ€ロ€€ы€€╉€€€仢雭茧伣雮€雮勲倢雮嶋倧雮戨倶雮欕倸雮滊偀雮犽偂雮㈦偍雮╇偒雮偔雮偗雮彪偝雮措偟雮鸽偧雰勲儏雰囯儓雰夒儛雰戨償雰橂儬雰ル剤雱夒剫雱岆剱雱掚創雱橂剻雱涬劀雱濍劊雱る劌雱劕雱措劦雱冯劯雱闺厐雲侂厔雲堧厫雲戨厰雲曤厴雲滊厾雲鸽吂雲茧唨雴傠唸雴夒唻雴嶋啋雴撾啍雴橂啘雴噷雵愲嚁雵滊嚌锟�".split("");
	for(a = 0; a != t[179].length; ++a)
		if(t[179][a].charCodeAt(0) !== 65533) { r[t[179][a]] = 45824 + a;
			e[45824 + a] = t[179][a] }
	t[180] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾€€€绊€表€岔€稠€俄€讽€鬼€喉€豁€巾€卷€宽亐韥來亗韥冺亞韥堩亰韥嬳亴韥嶍亷韥忥拷锟斤拷锟斤拷锟巾亼韥掜亾韥曧仏韥楉仚韥氻仜韥滍仢韥烅仧韥№仮韥ｍ仱韥ロ仸韥ы仺韥╉仾韥伄韥拷锟斤拷锟斤拷锟巾伇韥岔伋韥淀伓韥讽伕韥鬼伜韥豁伨韥宽個韨傢們韨勴倕韨嗧倗韨堩倝韨婍倠韨岉倣韨庬倧韨愴倯韨掜倱韨旊嚐雵嚛雵嚢雵闺嚮雵诫垊雸呺垐雸嬰垖雸旊垥雸楇垯雸犽埓雸茧墭雺滊墵雺墿雺措壍雺茧妱電呺妷電愲姂電旊姌電欕姎電犽姟電ｋ姤電﹄姫電姲電措媹雼夒媽雼愲嫆雼橂嫏雼涬嫕雼㈦嫟雼ル嫤雼嫬雼嫮雼嫰雼畴嫶雼惦嫹雼鸽嫻雼弘嫽雼侩寑雽侂寗雽堧寪雽戨寭雽旊寱雽滊崝雿曤崠雿橂崨雿滊崬雿熾崵雿ワ拷".split("");
	for(a = 0; a != t[180].length; ++a)
		if(t[180][a].charCodeAt(0) !== 65533) { r[t[180][a]] = 46080 + a;
			e[46080 + a] = t[180][a] }
	t[181] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾倳韨栱倵韨橅倷韨氻倹韨滍倽韨烅偀韨犿偂韨㈨偅韨偋韨╉偑韨偔韨偗韨绊偙韨诧拷锟斤拷锟斤拷锟巾偝韨俄偢韨喉偦韨柬偨韨卷偪韮傢儍韮呿儐韮図儕韮嬳儗韮嶍儙韮忢儝韮栱儣韮橅儥韮氾拷锟斤拷锟斤拷锟巾儧韮烅儫韮№儮韮ｍ儱韮儳韮儵韮儷韮儾韮稠兇韮淀兌韮讽児韮喉兓韮柬兘韮卷兛韯€韯來剛韯冺剟雿щ崺雿嵁雿半嵄雿措嵏霂€霂侂巸霂勲巺霂岆帎霂旊帬霂‰帹霂弰霃呺張霃嬰弻霃庪彁霃旊彆霃楇彊霃涬彎霃犽彜霃徏霅愲悩霅滊悹霅惄霅惔霊愲憫霊旊憳霊犽憽霊ｋ懃霊拃霋堧挐霋る挩霋挼霋冯捁霌€霌勲搱霌愲摃霌滊摑霌犽摚霌る摝霌摥霌摫霌鸽敂霐曤敇霐涬敎霐る敟霐щ敤霐╇敧霐半敱霐措敻锟�".split("");
	for(a = 0; a != t[181].length; ++a)
		if(t[181][a].charCodeAt(0) !== 65533) { r[t[181][a]] = 46336 + a;
			e[46336 + a] = t[181][a] }
	t[182] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾剠韯嗧剣韯堩剦韯婍剫韯岉剮韯忢剱韯戫剴韯擁剶韯曧剸韯楉剺韯欗剼韯涰劀韯濏劄韯燂拷锟斤拷锟斤拷锟巾劆韯№劉韯ｍ劋韯ロ劍韯ы劏韯╉劒韯劕韯劗韯劜韯稠劦韯俄劮韯鬼劵韯柬劷韯撅拷锟斤拷锟斤拷锟巾効韰傢厗韰図厛韰夗厞韰嬳厧韰忢厬韰掜厯韰曧厲韰楉厴韰欗厷韰涰厼韰犿參韰ｍ叅韰ロ叇韰ы叐韰叓韰晙霑侂晝霑勲晠霑嬰晫霑嶋晲霑旊暅霑濍暉霑犽暋霒犽枴霒る枿霒柅霒半柋霒畴柎霒惦柣霒茧柦霔€霔勲棇霔嶋棌霔愲棏霔橂棳霕愲槕霕旊槝霕ル槵霕措檲霗る櫒霘滊殱霘犽殼霘毈霘彪洈霙半洿霙鸽渶霚侂渽霚湬霚湳霚半湼霚闺溁霛勲潏霛岆潝霛曤潬霛る潹霛半澅霛畴澋霛茧澖霝€霝勲瀸霝嶋瀼霝愲瀾霝掚灃霝楋拷".split("");
	for(a = 0; a != t[182].length; ++a)
		if(t[182][a].charCodeAt(0) !== 65533) { r[t[182][a]] = 46592 + a;
			e[46592 + a] = t[182][a] }
	t[183] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾叜韰叞韰表叢韰稠叴韰淀叾韰讽吀韰鬼吅韰豁吔韰卷吙韱€韱來唫韱冺唴韱嗧唶韱夗唺锟斤拷锟斤拷锟斤拷韱嬳唽韱嶍啂韱忢啇韱戫啋韱擁啍韱曧問韱楉啒韱欗啔韱涰啘韱濏啚韱燀啟韱ｍ啣韱啩锟斤拷锟斤拷锟斤拷韱╉啰韱啲韱啴韱啿韱错喍韱讽喐韱鬼喕韱巾喚韱宽噥韲傢噧韲勴噮韲嗧噰韲堩噳韲婍噵韲岉噸韲庬噺霝橂灆霝滊灎霝灘霝灛霝灤霝惦灨霟囯焿霟煭霟半煷霟茧熃霟侩爛霠侂爣霠堧爥霠岆爯霠橂牂霠涬牆霠る牓霠牞霠措牭霠冯牳霠闺搿勲搿撾搿濍搿る‖搿’搿彪「搿茧擘擘措⒏耄€耄侂耄呺耄愲耄濍耄‰（耄╇，耄半８耄闺；耄诫毪橂毪茧そ毳€毳勲毳忞毳橂毳滊毳ォ锟�".split("");
	for(a = 0; a != t[183].length; ++a)
		if(t[183][a].charCodeAt(0) !== 65533) { r[t[183][a]] = 46848 + a;
			e[46848 + a] = t[183][a] }
	t[184] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾噽韲戫噿韲擁嚁韲曧嚃韲楉嚈韲氻嚊韲滍嚌韲烅嚐韲犿嚒韲㈨嚕韲ろ嚗韲嚙韲嚛韲拷锟斤拷锟斤拷锟巾嚝韲嚟韲嚡韲绊嚤韲岔嚦韲淀嚩韲讽嚬韲喉嚮韲柬嚱韲卷嚳韴€韴來垈韴冺垊韴呿垎锟斤拷锟斤拷锟斤拷韴堩垔韴嬳垖韴嶍垘韴忢垜韴掜垞韴旐垥韴栱垪韴橅垯韴氻垱韴滍垵韴烅垷韴犿垺韴㈨垼韴ろ垾韴埀韴埄毳キ毳措サ毳鸽ゼ毽勲毽囯毽婋毽庪Μ毽Π毽措毽诫毵侂毵夒毵庪毵愲毵掚毵欕毵濍毵‰В毵るД毵К毵措У毵冯Ц毵闺Ш毹€毹侂▓毹曤ǜ毹闺氅€氅傠﹫氅夒氅嶋氅撾氅曤氅滊─氅ル┃氅┅氅半┍氅措└氇冸獎氇呺獓氇岆氇╇氇氇搽氇闺氇诫珓氆堧珮氆欕锟�".split("");
	for(a = 0; a != t[184].length; ++a)
		if(t[184][a].charCodeAt(0) !== 65533) { r[t[184][a]] = 47104 + a;
			e[47104 + a] = t[184][a] }
	t[185] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾埅韴埉韴埍韴岔埑韴淀埗韴讽埜韴鬼埡韴豁埦韷€韷傢墐韷勴墔韷嗧墖韷夗墛韷嬳墝锟斤拷锟斤拷锟斤拷韷嶍墡韷忢墣韷戫墥韷擁墧韷曧墫韷楉墭韷欗墯韷涰墲韷烅墴韷犿墶韷㈨墸韷ロ墻韷ы墾锟斤拷锟斤拷锟斤拷韷╉壀韷壃韷壆韷壈韷表壊韷稠壌韷淀壎韷讽壐韷鬼壓韷豁壖韷巾壘韷宽妭韸冺妳韸嗧妵韸夗妸韸嬳妼氍€氍勲瑣氍忞瑧氍橂瑴氍犽氍氍惦氍鸽氍茧氍倦瓌氕呺瓏氕夒瓖氕忞瓙氕旊瓨氕‰氕畧氘岆異氘る氘氘冯瘈氙勲瘓氙愲瘬氙鸽氙茧氚€氚傠皥氚夒皨氚岆皪氚忞皯氚旊皶氚栯皸氚橂皼氚滊皾氚炿盁氚る哎氚щ癌氚鞍氚彪按氚鸽眬氡侂眱氡勲眳氡夒睂氡嶋睈氡濍矂氩呺矆氩嬰矊氩庪矓氩曤矖锟�".split("");
	for(a = 0; a != t[185].length; ++a)
		if(t[185][a].charCodeAt(0) !== 65533) { r[t[185][a]] = 47360 + a;
			e[47360 + a] = t[185][a] }
	t[186] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾妽韸庬姀韸掜姄韸旐姈韸楉姌韸欗姎韸涰姖韸烅姛韸№姠韸ｍ姤韸姧韸姪韸姭韸拷锟斤拷锟斤拷锟巾姰韸姲韸岔姵韸错姷韸俄姺韸喉娀韸巾娋韹來媰韹勴媴韹嗧媷韹婍媽韹嶍嫀韹忢嫄韹戯拷锟斤拷锟斤拷锟巾嫆韹擁嫊韹栱嫍韹欗嫐韹涰嫕韹烅嫙韹犿嫛韹㈨嫞韹嫥韹嫨韹嫬韹嫮韹嫰韹岔嫵韹淀嫸韹讽嫻韹弘矙氩氹矤氩‰菠氩щ波氩半脖氩畴泊氩惦布氩诫硛氤勲硩氤忞硱氤戨硶氤橂硿氤措车氤峨掣氤茧磩氪呺磭氪夒磹氪旊搐氪祤氲堧祲氲岆祼氲橂禉氲る胆攵€攵侂秳攵囯秷攵夒秺攵愲稇攵撾稌攵欕稓攵滊钉攵半陡敕旊窌敕橂窚敕╇钒敕措犯敫€敫冸竻敫岆笉敫愲笖敫滊笣敫熾箘牍呺箞牍岆箮牍旊箷牍楇箼牍氹箾牍犽埂牍わ拷".split("");
	for(a = 0; a != t[186].length; ++a)
		if(t[186][a].charCodeAt(0) !== 65533) { r[t[186][a]] = 47616 + a;
			e[47616 + a] = t[186][a] }
	t[187] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾嫽韹柬嫿韹卷嬁韺傢寗韺嗧寚韺堩寜韺婍寢韺忢寫韺掜寭韺曧寳韺橅寵韺氻寷韺烅將韺ｏ拷锟斤拷锟斤拷锟巾尋韺導韺尗韺尞韺尡韺岔尦韺错尩韺俄尫韺喉尵韺宽崁韻來崅韻冺崋韻図崍韻夛拷锟斤拷锟斤拷锟巾崐韻嬳崒韻嶍崕韻忢崘韻戫崚韻擁崝韻曧崠韻楉崢韻欗崥韻涰崪韻濏崬韻燀崰韻№崲韻ｍ崵韻ロ崷韻ы崹韻╇龚牍拱牍彪钩牍措沟牍浑辜牍诫簚牒勲簩牒嶋簭牒愲簯牒橂簷牒粣牖戨粩牖楇粯牖犽唬牖る互牖紒爰堧級爰橂紮爰涬紲爰濍絸虢侂絼虢堧綈虢戨綍刖旊景肟呺繉肟嶋繍肟旊繙肟熾俊靲检亼靵橃仠靵犾仺靵╈倫靷戩倲靷橃偁靷§偅靷レ偓靷偗靷办偝靷挫偟靷鹅偧靷届偪靸€靸侅儏靸堨儔靸岇儛靸橃儥靸涭儨靸濎儰锟�".split("");
	for(a = 0; a != t[187].length; ++a)
		if(t[187][a].charCodeAt(0) !== 65533) { r[t[187][a]] = 47872 + a;
			e[47872 + a] = t[187][a] }
	t[188] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾崻韻崿韻嵁韻嵃韻表嵅韻稠嵈韻淀嵍韻讽嵏韻鬼嵑韻豁嵕韻宽巵韼傢巸韼呿巻韼囷拷锟斤拷锟斤拷锟巾巿韼夗帄韼嬳帋韼掜帗韼旐帟韼栱帡韼氻帥韼濏帪韼燀帯韼㈨帲韼ろ帴韼帶韼幀韼拷锟斤拷锟斤拷锟巾幆韼绊幈韼岔幊韼淀幎韼讽幑韼喉幓韼巾幘韼宽弨韽來弬韽冺弳韽図強韽嬳弻韽嶍弾韽忢彂韽掜彄韽旐彆韽栰儱靸儸靸挫兊靸缝児靹€靹勳剤靹愳剷靹滌劃靹烄劅靹犾劊靹れ劍靹ъ劕靹劘靹办劚靹鹅劯靹轨劶靺€靺堨厜靺嬱厡靺嶌厰靺曥厴靺滌叅靺レ収靺叐靺办叴靺胳唴靻岇啀靻庫啇靻旍問靻滌啙靻熿啞靻レ啫靻╈啲靻办喗靽勳噲靽岇嚁靽楈嚇靽犾嚖靽嚢靽膘嚦靽检嚱靾€靾勳垖靾嶌垙靾戩垬靾欖垳靾熿垹靾埄靾埈锟�".split("");
	for(a = 0; a != t[188].length; ++a)
		if(t[188][a].charCodeAt(0) !== 65533) { r[t[188][a]] = 48128 + a;
			e[48128 + a] = t[188][a] }
	t[189] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾彈韽欗彋韽涰彍韽濏彏韽燀彔韽㈨彜韽ロ彟韽ы彣韽╉彧韽彯韽彵韽岔彸韽淀彾韽凤拷锟斤拷锟斤拷锟巾徃韽鬼徍韽豁従韾€韾傢悆韾勴悈韾嗧悋韾夗悐韾嬳悓韾嶍悗韾忢悙韾戫悞韾擁悢韾曧悥锟斤拷锟斤拷锟斤拷韾楉悩韾欗悮韾涰悳韾烅悷韾犿悺韾㈨悾韾ろ惀韾惂韾惄韾惈韾惌韾惎韾绊惐韾岔惓韾错惖韾俄惙靾埍靾察埓靿堨墣靿戩墧靿橃墵靿レ壃靿壈靿挫壖靿届壙鞀侅妶鞀夓姁鞀橃姏鞀濎姢鞀レ姩鞀姯鞀挫姷鞀缝姽鞁滌嫕鞁犾嫞鞁れ嫬鞁嫮鞁嫳鞁鹅嫺鞁轨嫽鞁检寑鞂堨寜鞂岇實鞂撿寯鞂曥寴鞂滌尋鞂レ尐鞂╈崊鞃崺鞃嵃鞃察嵏鞃轨嵓鞃届巹鞄堨帉鞆€鞆橃彊鞆滌彑鞆犾彚鞆彥鞆彺鞆奠徃鞇堨悙鞇れ惉鞇帮拷".split("");
	for(a = 0; a != t[189].length; ++a)
		if(t[189][a].charCodeAt(0) !== 65533) { r[t[189][a]] = 48384 + a;
			e[48384 + a] = t[189][a] }
	t[190] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾惛韾鬼惡韾豁惣韾巾惥韾宽憗響傢憙響呿憜響図憟響夗憡響嬳憣響嶍憥響忢憪響戫憭響擄拷锟斤拷锟斤拷锟巾憯響曧憱響楉憳響欗憵響涰憹響烅憻響№憿響ｍ懃響懅響懇響懌響懏響绊懕響诧拷锟斤拷锟斤拷锟巾懗響错懙響俄懛響喉懟響巾懢頀來拑頀勴拝頀嗧拠頀婍拰頀庬拸頀愴拺頀掜挀頀曧挅頀楉挊頀欗挌頀涰挏頀濎惔鞇检惤鞈堨懁鞈レ懆鞈懘鞈奠懝鞉€鞉旍挏鞉胳捈鞊╈摪鞊膘摯鞊胳摵鞊快攢鞌侅攲鞌愳敂鞌滌敤鞌╈敩鞌办敻鞌轨敾鞌届晞鞎呾晥鞎夓晩鞎岇晬鞎庫晸鞎旍晻鞎楈晿鞎欖暆鞎烄暊鞎§暏鞎暟鞎膘暢鞎挫暤鞎检暯鞏€鞏勳枃鞏岇枍鞏忟枒鞏曥枟鞏橃枩鞏犾柀鞏挫柕鞏胳柟鞏混柤鞏届柧鞐勳梾鞐嗢棁鞐堨棄鞐婌棇鞐庯拷".split("");
	for(a = 0; a != t[190].length; ++a)
		if(t[190][a].charCodeAt(0) !== 65533) { r[t[190][a]] = 48640 + a;
			e[48640 + a] = t[190][a] }
	t[191] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾挒頀燀挔頀№挗頀ｍ挙頀ロ挦頀ы挩頀挮頀挱頀挴頀绊挶頀岔挸頀错挼頀俄挿頀革拷锟斤拷锟斤拷锟巾捁頀喉捇頀柬捊頀卷捒頁€頁來搨頁冺搫頁呿搯頁図搱頁夗搳頁嬳搷頁庬搹頁戫搾頁擁摃锟斤拷锟斤拷锟斤拷頁栱摋頁橅摍頁氻摏頁濏摓頁犿摗頁㈨摚頁ろ摜頁摟頁╉摢頁摥頁摨頁表摬頁稠摯頁淀摱頁讽摴頁喉摷鞐愳棏鞐旍棙鞐犾棥鞐ｌ棩鞐棴鞐棸鞐挫椂鞐缝椉鞐届椌鞐快榾鞓侅槄鞓嗢槆鞓堨槍鞓愳槝鞓欖槢鞓滌槫鞓レ槰鞓槶鞓槹鞓踌槾鞓奠樂鞓轨樆鞕€鞕侅檮鞕堨檺鞕戩檽鞕旍檿鞕滌櫇鞕犾櫖鞕櫛鞕胳櫣鞕检殌鞖堨殙鞖嬱殟鞖旍殨鞖橃殰鞖れ殽鞖ъ毄鞖办毐鞖挫毟鞖轨毢鞗€鞗侅泝鞗呾泴鞗嶌洂鞗旍洔鞗濎洜鞗§洦锟�".split("");
	for(a = 0; a != t[191].length; ++a)
		if(t[191][a].charCodeAt(0) !== 65533) { r[t[191][a]] = 48896 + a;
			e[48896 + a] = t[191][a] }
	t[192] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾摼頁宽攢頂來攤頂冺攨頂嗧攪頂夗攰頂嬳攳頂庬攺頂愴攽頂掜敁頂栱敇頂欗敋頂涰敎頂濓拷锟斤拷锟斤拷锟巾敒頂燀敔頂№敘頂ｍ敜頂ロ敠頂ы敤頂╉敧頂敩頂敭頂敯頂表敳頂稠敶頂淀敹頂凤拷锟斤拷锟斤拷锟巾敻頂鬼敽頂豁斁頂宽晛頃傢晝頃呿晢頃図晥頃夗晩頃嬳晭頃愴晵頃擁晹頃曧晼頃楉暁頃涰暆頃烅暉頃№暍頃ｌ洨鞗洶鞗胳浌鞗届渼鞙呾湀鞙岇湐鞙曥湕鞙欖湢鞙§湦鞙湴鞙膘湷鞙奠湻鞙检溄鞚€鞚勳潑鞚岇潔鞚忟潙鞚掛潛鞚旍潟鞚栰潡鞚橃潨鞚犾潹鞚澊鞚奠澑鞚检澖鞚眷瀮鞛勳瀰鞛囲瀳鞛夓瀶鞛庫瀽鞛戩灁鞛栰灄鞛橃灇鞛犾灐鞛ｌ灓鞛レ灕鞛灜鞛办灤鞛检灲鞛快焵鞜侅焾鞜夓煂鞜庫煇鞜橃煗鞜れ煥鞜爛鞝侅爠鞝堨爦锟�".split("");
	for(a = 0; a != t[192].length; ++a)
		if(t[192][a].charCodeAt(0) !== 65533) { r[t[192][a]] = 49152 + a;
			e[49152 + a] = t[192][a] }
	t[193] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾暏頃暓頃暚頃暞頃绊暠頃岔暢頃俄暦頃鬼暫頃豁暯頃卷暱頄€頄來杺頄冺枂頄婍枊锟斤拷锟斤拷锟斤拷頄岉枍頄庬枏頄戫枓頄擁枖頄曧枛頄楉枠頄欗枤頄涰枩頄濏枮頄燀枲頄№枹頄ｍ枻頄枾锟斤拷锟斤拷锟斤拷頄柀頄柅頄柇頄柉頄绊柋頄岔柍頄错柕頄俄柗頄疙柟頄喉柣頄柬柦頄卷柨項€項來梻項冺梽項呿梿項囲爯鞝戩爴鞝曥爾鞝滌牆鞝犾牑鞝牠鞝牨鞝胳牸臁€臁堨臁岇臁旍“臁膘〈臁胳『膦€膦侅膦呾膦囲膦岇膦旍膦熿ⅰ膦⒓膦届欤堨欤旍欤楈欤犾！欤れ５欤检＝欷€欷勳欷嗢欷嶌欷戩欷ご欹愳欹旍欹犾ァ欹ｌガ欹办ゴ欹检歃夓歃愳歃欖歃濎歆侅歆囲歆婌歆戩锟�".split("");
	for(a = 0; a != t[193].length; ++a)
		if(t[193][a].charCodeAt(0) !== 65533) { r[t[193][a]] = 49408 + a;
			e[49408 + a] = t[193][a] }
	t[194] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾棅項嬳棈項庬棌項戫棑項旐棔項栱棗項氻棞項烅棢項犿棥項㈨棧項棫項╉棯項棴項拷锟斤拷锟斤拷锟巾棷項绊棻項岔棾項俄椄項喉椈項柬椊項卷椏順傢槂順呿槅順図槈順婍構順岉槏順庬槒順掞拷锟斤拷锟斤拷锟巾槚順楉槝順欗槡順涰槤順烅槦順№槩順ｍ槬順槯順槱順槴順槷順槹順表槻順稠槾順淀樁順讽樅順混歆栰歆氺歆濎歆㈧Г歆ъК歆Н歆办П歆胳Ч歆检█歙堨▔歙嬱▽歙嶌〝歙橃ī飑岇飑愳飑滌飑熿飑§┄飑届獎飒橃飒届珋飓勳珜飓嶌珡飓戩珦飓橃珯飓犾飓挫瑘飕愳瑪飕橃瑺飕§瓉飙堨瓑飙岇瓙飙橃瓩飙濎飙胳飚滌殳旍殳ъ彀岇皪彀愳皵彀滌皾彀§阿彀ъ皑彀╈艾彀鞍彀胳肮彀伙拷".split("");
	for(a = 0; a != t[194].length; ++a)
		if(t[194][a].charCodeAt(0) !== 65533) { r[t[194][a]] = 49664 + a;
			e[49664 + a] = t[194][a] }
	t[195] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾樈順卷樋頇來檪頇冺檮頇嗧檱頇婍檶頇庬檹頇愴檼頇擁櫀頇楉櫃頇氻櫅頇濏櫈頇燀櫊頇★拷锟斤拷锟斤拷锟巾櫌頇ｍ櫎頇ロ櫐頇櫔頇櫖頇櫘頇櫜頇稠櫟頇俄櫡頇疙櫣頇喉櫥頇柬櫧頇卷櫩須€锟斤拷锟斤拷锟斤拷須來殏須勴殕須図殘須夗殜須嬳殠須忢殤須掜殦須曧殩須楉殬須欗殮須涰殰須烅殸須㈨殻須ろ殽須毀須╉毆彀检敖彀眷眲毂呾眻毂岇睌毂曥睏毂橃睓毂犾堡毂报毂办钡觳橃矙觳滌矤觳博觳铂觳泊觳奠哺觳检硠斐呾硣斐夓硱斐旍长斐嘲齑侅磮齑夓磳齑愳礃齑欖礇齑濎搐齑船齑轨禍斓犾丹斓淡斓当斓胳秷於旍稌於橃稖於れ顶於ъ订於办穭旆岇窅旆番旆办犯旆轨坊旆届竸旄堨笇旄旍笝旄犾浮旄れ辅旄办副旄踌傅锟�".split("");
	for(a = 0; a != t[195].length; ++a)
		if(t[195][a].charCodeAt(0) !== 65533) { r[t[195][a]] = 49920 + a;
			e[49920 + a] = t[195][a] }
	t[196] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾毇須毊須毐須岔毘須错毜須俄毞須疙毢須柬毥須卷毧頉€頉來泜頉冺泦頉図泬頉婍泲锟斤拷锟斤拷锟斤拷頉嶍泿頉忢洂頉掜洆頉曧洊頉橅洑頉涰洔頉濏洖頉燀洝頉㈨洠頉ロ洣頉ы洨頉洬頉洯锟斤拷锟斤拷锟斤拷頉洴頉表洸頉稠洿頉俄浄頉疙浌頉喉浕頉卷浛頊來渹頊冺渽頊嗧渿頊堩湁頊婍湅頊岉湇頊庬湉頊愴湌頊擁湐旃橃箼旃滌篃旃犾埂旃供旃弓旃挫沟旃胳辜旌勳簠旌囲簤旌愳簯旌旍簶旌犾骸旌ｌ氦旌レ含旌粊旎れ互旎猾旎淮旎奠环旎胳还旒€旒侅紕旒堨紣旒戩紦旒曥紲旒犾激旒辑旒及旒膘几旖旍綍旖橃綔旖れ渐旖ъ僵旖办奖旖挫礁炀€炀呾緦炀§鲸炀办縿炜犾俊炜れ卡炜办勘炜踌康炜柬€€韤勴€戫€橅€€错€淀€疙€硷拷".split("");
	for(a = 0; a != t[196].length; ++a)
		if(t[196][a].charCodeAt(0) !== 65533) { r[t[196][a]] = 50176 + a;
			e[50176 + a] = t[196][a] }
	t[197] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾湑頊栱湕頊氻湜頊濏湠頊燀湣頊㈨湥頊ろ湧頊湩頊湰頊湳頊绊湵頊岔湷頊俄湻頊癸拷锟斤拷锟斤拷锟巾満頊豁溄頊卷溈頋€頋來潅頋冺潊頋嗧潏頋婍潒頋岉潔頋庬潖頋掜潛頋曧潥頋涰潨頋濏潪锟斤拷锟斤拷锟斤拷頋燀潰頋ろ潶頋ы潹頋潾頋澁頋澅頋岔澇頋淀澏頋讽澑頋鬼澓頋豁澗頋宽瀫頌傢瀮頌勴瀰頌嗧瀲頌婍瀷韥勴亝韥図亯韥愴仈韥橅仩韥伃韥绊伌韥柬伣韨來偆韨ロ偍韨偞韨淀偡韨鬼儉韮來儎韮堩儔韮愴儜韮擁償韮曧儨韮濏儬韮ろ儸韮儻韮绊儽韮疙剭韯绊劚韯错劯韯喉厐韰來厓韰勴厖韰岉厤韰愴厰韰滍厺韰燀叀韰叕韰柬唲韱堩啝韱№啢韱啺韱表喅韱淀喓韱柬噣韲橅嚧韲疙垏韴夗垚韴埈韴绊埓韴柬埥韴宽墎韷堩墱锟�".split("");
	for(a = 0; a != t[197].length; ++a)
		if(t[197][a].charCodeAt(0) !== 65533) { r[t[197][a]] = 50432 + a;
			e[50432 + a] = t[197][a] }
	t[198] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾瀺頌庬瀼頌戫瀿頌擁灁頌曧灃頌楉灇頌滍灋頌燀灎頌№灑頌ｏ拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾墹韸€韸來妱韸堩姁韸戫姇韸滍姞韸ろ姮韸表姼韸鬼娂韸宽媭韹傢媹韹夗媼韹旐嫎韹滍嫟韹ロ嫲韹表嫶韹疙寑韺來寖韺呿寣韺嶍寧韺愴寯韺栱寽韺濏専韺犿尅韺ロ尐韺╉尙韺绊尭韺鬼尰韺柬尳韻勴崊韻柬嵔韼€韼勴帉韼嶍帍韼愴帒韼橅帣韼滍帬韼帺韼幁韼错幐韼柬弰韽呿張韽夗彁韽橅彙韽ｍ彫韽彴韽错徏韽巾徔韾侊拷".split("");
	for(a = 0; a != t[198].length; ++a)
		if(t[198][a].charCodeAt(0) !== 65533) { r[t[198][a]] = 50688 + a;
			e[50688 + a] = t[198][a] }
	t[199] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾悎韾濏憖響勴憸響犿懁響懐響疙懝響柬懣頀€頀傢拡頀夗拫頀嶍挃頀╉搶頁愴摂頁滍摕頁摤頁绊摳頁豁摻頂勴攬頂岉敂頂曧敆頂柬斀頃€頃勴晫頃嶍晱頃戫晿頃欗暅頃犿暐頃暕頃暛頃错暤頃疙暭頄勴枀頄図枅頄夗枑頄ロ棃項夗棇項愴棐項橅棛項涰棟項ろ棩項棳項错椀項讽椆順€順來槃順堩槓順戫槗順旐槙順滍槧锟�".split("");
	for(a = 0; a != t[199].length; ++a)
		if(t[199][a].charCodeAt(0) !== 65533) { r[t[199][a]] = 50944 + a;
			e[50944 + a] = t[199][a] }
	t[200] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟巾槫順樃順鬼樇頇€頇呿檲頇夗檵頇嶍檻頇旐檿頇橅櫆頇ы櫓頇绊櫛頇错殐須呿殞須嶍殣須旐殱須燀殹須毈須绊毠須豁泟頉呿泩頉岉洃頉旐洍頉欗洜頉ろ洦頉绊浀頉柬浗頊€頊勴湋頊橅湙頊滍湢頊湬頊湱頊错湹頊疙溂頋勴潎頋夗潗頋戫潝頋栱潡頋橅潤頋犿潯頋ｍ潵頋╉潿頋绊澊頋柬澖頌來瀳頌夗瀸頌愴灅頌欗灈頌濓拷".split("");
	for(a = 0; a != t[200].length; ++a)
		if(t[200][a].charCodeAt(0) !== 65533) { r[t[200][a]] = 51200 + a;
			e[51200 + a] = t[200][a] }
	t[202] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟戒冀浣冲亣鍍瑰姞鍙懙鍝ュ槈瀚佸鏆囨灦鏋锋煰姝岀弬鐥傜鑻涜寗琛楄瑷惰硤璺忚换杩﹂鍒诲嵈鍚勬仾鎱ゆ鐝忚剼瑕鸿闁ｄ緝鍒婂⒕濂稿Е骞插构鎳囨弨鏉嗘煬妗挎緱鐧庣湅纾电▓绔跨啊鑲濊壆鑹辫闁撲公鍠濇浄娓寸ⅲ绔憶瑜愯潕闉ㄥ嫎鍧庡牚宓屾劅鎲炬垺鏁㈡煈姗勬笡鐢樼柍鐩ｇ灠绱洪偗閼戦憭榫曪拷".split("");
	for(a = 0; a != t[202].length; ++a)
		if(t[202][a].charCodeAt(0) !== 65533) { r[t[202][a]] = 51712 + a;
			e[51712 + a] = t[202][a] }
	t[203] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉專宀敳鑳涢墍闁樺墰鍫堝宀″礂搴峰己褰婃叿姹熺暫鐤嗙碃绲崇侗缇岃厰鑸¤枒瑗佽瑳閶奸檷楸囦粙浠峰€嬪嚤濉忔劮鎰炬叏鏀规И婕戠枼鐨嗙洊绠囪姤钃嬶閹ч枊鍠€瀹㈠潙铯佺渤缇归喌鍊ㄥ幓灞呭法鎷掓嵁鎿氭摟娓犵偓绁涜窛韪烇閬介墔閶镐咕浠跺仴宸惧缓鎰嗘鑵辫檾韫囬嵉楱篂鍌戞澃妗€鍎夊妽鍔掓锟�".split("");
	for(a = 0; a != t[203].length; ++a)
		if(t[203][a].charCodeAt(0) !== 65533) { r[t[203][a]] = 51968 + a;
			e[51968 + a] = t[203][a] }
	t[204] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界灱閳愰粩鍔€坎鍋堟啯鎻搳鏍兼獎婵€鑶堣Α闅斿爡鐗界姮鐢勭倒绻偐瑕嬭閬ｉ祽鎶夋焙娼旂祼缂鸿ǎ鍏兼厞绠濊瑱閴楅帉浜繐鍊炲偩鍎嗗媮鍕嶅嵖鍧板搴氬緫鎱舵啲鎿庢暚鏅毣鏇存娑囩倕鐑辩挓鐠ョ搳鐥欑‖纾珶绔剁祬缍撹€曡€胯剾鑾栬杓曢€曢彙闋冮牳椹氶淇傚晸鍫哄瀛ｅ眴鎮告垝妗傛锟�".split("");
	for(a = 0; a != t[204].length; ++a)
		if(t[204][a].charCodeAt(0) !== 65533) { r[t[204][a]] = 52224 + a;
			e[52224 + a] = t[204][a] }
	t[205] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥（婧晫鐧哥绋界郴绻辜瑷堣璋块殠榉勫彜鍙╁憡鍛卞浐濮戝灏诲韩鎷锋敺鏁呮暡鏆犳灟妲佹步鐥肩殣鐫剧缇旇€冭偂鑶忚嫤鑻借彴钘佽牨琚磋铯冭緶閷泧椤ч珮榧撳摥鏂涙洸姊忕﹢璋烽禒鍥板潳宕戞槅姊辨婊剧惃琚為姹楠ㄤ緵鍏叡鍔熷瓟宸ユ亹鎭嫳鎺ф敾鐝欑┖铓ｈ并闉忎覆瀵℃垐鏋滅摐锟�".split("");
	for(a = 0; a != t[205].length; ++a)
		if(t[205][a].charCodeAt(0) !== 65533) { r[t[205][a]] = 52480 + a;
			e[52480 + a] = t[205][a] }
	t[206] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界鑿撹獓瑾茶法閬庨崑椤嗗粨妲ㄨ椏閮鍐犲畼瀵叄妫烘鐏岀惎鐡樼缃愯弲瑙€璨棞椁ㄥ埉鎭濇嫭閫備緤鍏夊尅澹欏唬鏇犳锤鐐氱媯鐝栫瓙鑳遍憶鍗︽帥缃箹鍌€濉婂鎬劎鎷愭榄佸畯绱樿偙杞熶氦鍍戝挰鍠瑢宥犲阀鏀晭鏍℃鐙＄殠鐭禐缈硅啝钑庤洘杓冭綆閮婇椹曢涓樹箙涔濅粐淇卞叿鍕撅拷".split("");
	for(a = 0; a != t[206].length; ++a)
		if(t[206][a].charCodeAt(0) !== 65533) { r[t[206][a]] = 52736 + a;
			e[52736 + a] = t[206][a] }
	t[207] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉崁鍙ｅ彞鍜庡様鍧靛灑瀵囧秶寤愭嚰鎷樻晳鏋告煩妲嬫瓙姣嗘姹傛簼鐏哥嫍鐜栫悆鐬跨煩绌剁悼鑰夎嚰鑸呰垔鑻熻、璎宠臣杌€閫戦偙閴ら姸椐掗﹨槌╅窏榫滃湅灞€鑿婇灎闉捍鍚涚獦缇よ杌嶉儭鍫€灞堟帢绐熷寮撶┕绐妿韬€﹀埜鍕稿嵎鍦堟嫵鎹叉瑠娣冪湻鍘ョ崡钑ㄨ苟闂曟満娅冩桨瑭粚楗嬶鏅锋璨达拷".split("");
	for(a = 0; a != t[207].length; ++a)
		if(t[207][a].charCodeAt(0) !== 65533) { r[t[207][a]] = 52992 + a;
			e[52992 + a] = t[207][a] }
	t[208] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介铯囧彨鍦鎻嗘Щ鐝绐虹珔绯捐懙瑕忚党閫甸枿鍕诲潎鐣囩瓲鑿岄垶铯堟鍏嬪墜鍔囨垷妫樻サ闅欏儏鍔ゅ嫟鎳冩枻鏍规Э鐟剧瓔鑺硅彨瑕茶杩戦铯変粖濡楁搾鏄戞獛鐞寸绂借姪琛捐】瑗燂閷︿紜鍙婃€ユ壉姹茬礆绲︿簶鍏㈢煖鑲紒浼庡叾鍐€鍡滃櫒鍦诲熀鍩煎濂囧瀵勫矏宕庡繁骞惧繉鎶€鏃楁棧锟�".split("");
	for(a = 0; a != t[208].length; ++a)
		if(t[208][a].charCodeAt(0) !== 65533) { r[t[208][a]] = 53248 + a;
			e[53248 + a] = t[208][a] }
	t[209] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥湠鏈熸潪妫嬫姗熸姘ｆ苯娌傛穱鐜樼惁鐞拏鐠ｇ暩鐣跨纾绁囩绁虹畷绱€缍虹緢鑰嗚€倢瑷樿瓘璞堣捣閷￠尋椋㈤楱庨◤椹ラ簰绶婁蕉鍚夋嫯妗旈噾鍠労铯嬶濞滄嚘铯嶆嫃鎷匡铯忥铯戯铯撻偅铯旓铯栵铯樿铯欙铯涳鏆栵鐓栵铯熼洠铯犳崗鎹哄崡铯℃瀼妤犳钩铯㈢敺铯ｏい铯ワ拷".split("");
	for(a = 0; a != t[209].length; ++a)
		if(t[209][a].charCodeAt(0) !== 65533) { r[t[209][a]] = 53504 + a;
			e[53504 + a] = t[209][a] }
	t[210] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界磵铯︼ぇ琛插泭濞橈え铯╋お铯が涔冿き鍏у鏌拌€愶ぎ濂冲勾鎾氱蹇垫伂鎷堟嵒瀵у瘲鍔く濂村缉鎬掞ぐ铯憋げ鐟欙こ铯达さ铯讹し铯搁铯癸ず铯伙ぜ铯斤ぞ铯匡铳侊铳冩績铳勶鑶胯静鎯憋铳囪叇铳堬灏匡铳嬶铳嶏铳忥铳戝瑷ユ澔绱愶铳擄铳曪铳楄兘铳橈灏兼偿鍖挎汉澶氳尪锟�".split("");
	for(a = 0; a != t[210].length; ++a)
		if(t[210][a].charCodeAt(0) !== 65533) { r[t[210][a]] = 53760 + a;
			e[53760 + a] = t[210][a] }
	t[211] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟戒腹浜朵絾鍠湗澹囧綎鏂锋棪妾€娈垫箥鐭绨炵窞铔嬭閯查崨鎾绘揪鐛虹柛閬斿晼鍧嶆喓鎿旀泧娣℃箾娼竟鐥拌亙鑶借晛瑕冭珖璀氶専娌撶晸绛旇笍閬濆攼鍫傚骞㈡垏鎾炴　鐣剁硸铻抽花浠ｅ瀳鍧ぇ灏嶅脖甯跺緟鎴存摗鐜宠嚭琚嬭哺闅婇粵瀹呭痉鎮冲€掑垁鍒板湒鍫靛灏庡睜宄跺秼搴﹀緬鎮兼寫鎺夋悧妗冿拷".split("");
	for(a = 0; a != t[211].length; ++a)
		if(t[211][a].charCodeAt(0) !== 65533) { r[t[211][a]] = 54016 + a;
			e[54016 + a] = t[211][a] }
	t[212] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥９娅傛窐娓℃粩婵ょ嚲鐩滅澒绂辩ɑ钀勮Ι璩烦韫堥€冮€旈亾閮介崓闄堕煖姣掔€嗙墭鐘㈢崹鐫ｇ绡ょ簺璁€澧╂儑鏁︽椊鏆炬矊鐒炵噳璞氶爴涔獊浠濆啲鍑嶅嫊鍚屾啩鏉辨妫熸礊娼肩柤鐬崇鑳磋懀閵呭厹鏂楁潨鏋撶棙绔囪嵆铳氳眴閫楅牠灞噣鑺氶亖閬垗寰楀稘姗欑噲鐧荤瓑钘よ瑒閯чò鍠囨嚩铳涚櫓缇咃拷".split("");
	for(a = 0; a != t[212].length; ++a)
		if(t[212][a].charCodeAt(0) !== 65533) { r[t[212][a]] = 54272 + a;
			e[54272 + a] = t[212][a] }
	t[213] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借樋铻鸿８閭忥娲涚儥鐝炵怠钀斤閰П铳炰簜鍗垫瑒娆掔€剧垱铇笧鍓岃荆宓愭摜鏀瑬婵眱绾滆棈瑗よ鎷夎嚇锠熷粖鏈楁氮鐙肩悈鐟瀭閮炰締宕嶅緺钀婂喎鎺犵暐浜€嗗叐鍑夋妯戠伯绮辩厂鑹珤杓涢噺渚跺劮鍕靛憘寤叜鎴炬梾娅氭烤绀棞锠ｉ柇椹㈤┆楹楅粠鍔涙泦姝风€濈か杞㈤潅鎲愭垁鏀ｆ迹锟�".split("");
	for(a = 0; a != t[213].length; ++a)
		if(t[213][a].charCodeAt(0) !== 65533) { r[t[213][a]] = 54528 + a;
			e[54528 + a] = t[213][a] }
	t[214] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界厜鐠夌反鑱摦杓﹂€ｉ崐鍐藉垪鍔ｆ磳鐑堣寤夋杺娈總绨剧嵉浠や级鍥癸宀哄逗鎬滅幉绗練缈庤亞閫為埓闆堕潏闋橀健渚嬫晶绂喆闅峰嫗铳犳拡鎿勬珦娼炵€樼垚鐩ц€佽槅铏滆矾杓呴湶榄泛楣电绁跨稜鑿夐寗楣块簱璜栧寮勬湩鐎х搹绫犺伨鍎＄€ㄧ墷纾婅硞璩氳炒闆蜂簡鍍氬寤栨枡鐕庣檪鐬亰钃硷拷".split("");
	for(a = 0; a != t[214].length; ++a)
		if(t[214][a].charCodeAt(0) !== 65533) { r[t[214][a]] = 54784 + a;
			e[54784 + a] = t[214][a] }
	t[215] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介伡楝ч緧澹樺﹣灞㈡〒娣氭紡鐦荤疮绺疯敒瑜搁彜闄嬪妷鏃掓煶姒存祦婧滅€忕悏鐟犵暀鐦ょ～璎鍏埉闄镐緰鍊礄娣陡杓緥鎱勬牀铳￠殕鍕掕倠鍑滃噷妤炵缍捐彵闄典繗鍒╁帢鍚忓攷灞ユ偋鏉庢ⅷ娴妬鐙哥悊鐠冿ア鐥㈢爆缃圭靖鑾夎瑁￠噷閲愰洟榀夊悵娼剧噽鐠樿椇韬殻楸楅簾鏋楁穻鐞宠嚚闇栫牞锟�".split("");
	for(a = 0; a != t[215].length; ++a)
		if(t[215][a].charCodeAt(0) !== 65533) { r[t[215][a]] = 55040 + a;
			e[55040 + a] = t[215][a] }
	t[216] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界珛绗犵矑鎽╃應鐥茬⒓纾ㄩΜ榄旈夯瀵炲箷婕犺啘鑾倛涓囧崓濞╁窉褰庢參鎸芥櫓鏇兼豢婕仯鐬炶惉钄撹牷杓撻榘诲敎鎶规湯娌寜瑗澓浜″蹇樺繖鏈涚恫缃旇姃鑼幗杓為倷鍩嬪濯掑瘣鏄ф灇姊呮瘡鐓ょ降璨疯常閭侀瓍鑴堣矈闄岄﹢楹ュ瓱姘撶寷鐩茬洘钀屽啰瑕撳厤鍐曞媺妫夋矓鐪勭湢缍跨番闈㈤旱婊咃拷".split("");
	for(a = 0; a != t[216].length; ++a)
		if(t[216][a].charCodeAt(0) !== 65533) { r[t[216][a]] = 55296 + a;
			e[55296 + a] = t[216][a] }
	t[217] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借攽鍐ュ悕鍛芥槑鏆濇ぇ婧熺毧鐬戣寳钃傝灍閰╅姌槌磋渚啋鍕熷甯芥厱鎽告懝鏆煇妯℃瘝姣涚墴鐗＄憗鐪哥煕鑰楄娂鑼呰瑎璎ㄨ矊鏈ㄦ矏鐗х洰鐫︾﹩槎╂娌掑あ鏈﹁挋鍗濡欏粺鎻忔槾鏉虫负鐚珬鑻楅尐鍕欏帆鎲噵鎴婃媷鎾棤妤欐姣嬬劇鐝风暆绻嗚垶鑼傝暘瑾ｈ部闇ч怠澧ㄩ粯鍊戝垘鍚诲晱鏂囷拷".split("");
	for(a = 0; a != t[217].length; ++a)
		if(t[217][a].charCodeAt(0) !== 65533) { r[t[217][a]] = 55552 + a;
			e[55552 + a] = t[217][a] }
	t[218] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥倍绱婄磱鑱炶殜闁€闆嬁娌曠墿鍛冲獨灏惧祴褰屽井鏈⒍妤ｆ讣婀勭湁绫崇編钖囪瑤杩烽潯榛村卜鎮舵剭鎲晱鏃绘椉姘戞朝鐜熺弶绶￠枖瀵嗚湝璎愬墲鍗氭媿鎼忔挷鏈存ǜ娉婄弨鐠炵當绮曠笡鑶婅埗钖勮揩闆归浼村崐鍙嶅彌鎷屾惉鏀€鏂戞娉綐鐝晹鐦㈢洡鐩肩纾荤が绲嗚埇锜犺繑闋掗／鍕冩嫈鎾ユ袱娼戯拷".split("");
	for(a = 0; a != t[218].length; ++a)
		if(t[218][a].charCodeAt(0) !== 65533) { r[t[218][a]] = 55808 + a;
			e[55808 + a] = t[218][a] }
	t[219] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界櫦璺嬮啽閴㈤榄冨€ｅ倣鍧婂Θ灏ㄥ箛褰锋埧鏀炬柟鏃佹槈鏋嬫婊傜绱¤偑鑶€鑸姵钂¤殞瑷瑮閭﹂槻榫愬€嶄砍铳ｅ煿寰樻嫓鎺掓澂婀冪剻鐩冭儗鑳氳４瑁佃璩犺缉閰嶉櫔浼桨甯涙煆鏍㈢櫧鐧鹃瓌骞℃▕鐓╃嚁鐣イ绻佽晝钘╅浼愮瓘缃伴枼鍑″竼姊垫熬姹庢硾鐘瘎鑼冩硶鐞哄兓鍔堝鎿樻獥鐠х櫀锟�".split("");
	for(a = 0; a != t[219].length; ++a)
		if(t[219][a].charCodeAt(0) !== 65533) { r[t[219][a]] = 56064 + a;
			e[56064 + a] = t[219][a] }
	t[220] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界ⅶ铇楅棦闇癸ゥ鍗炲紒璁婅鲸杈倞鍒ョ灔楸夐紙涓欏€傚叺灞涘狗鏄炴樅鏌勬鐐崇攣鐥呯绔濊姬椁犻▓淇濆牎鍫卞鏅娲戞购娼界彜鐢彥瑁滆璀滆紨浼忓儠鍖愬崪瀹撳京鏈嶇鑵硅尟钄旇瑕嗚脊杓婚Ε榘掓湰涔朵扛濂夊皝宄嘲鎹ф鐑界啟鐞斧钃渹閫㈤嫆槌充笉浠樹刊鍌呭墫鍓惁鍜愬煚澶│锟�".split("");
	for(a = 0; a != t[220].length; ++a)
		if(t[220][a].charCodeAt(0) !== 65533) { r[t[220][a]] = 56320 + a;
			e[56320 + a] = t[220][a] }
	t[221] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉瓪瀛靛瘜搴滐ウ鎵舵暦鏂ф诞婧ョ埗绗︾翱缂惰厫鑵戣啔鑹€鑺欒帺瑷冭矤璩﹁郴璧磋逗閮ㄩ嚋闃滈檮椐欓厂鍖楀垎鍚╁櫞澧冲濂靠鎲ゆ壆鏄愭本鐒氱泦绮夌碁绱涜姮璩侀洶铳т經寮楀娇鎷傚穿鏈嬫纭肩箖榈笗鍌欏寱鍖崙濡冨搴囨偛鎲婃墘鎵规枑鏋囨Η姣旀瘱姣楁瘶娌革エ鐞电椇鐮掔绉曠绮冪穻缈¤偉锟�".split("");
	for(a = 0; a != t[221].length; ++a)
		if(t[221][a].charCodeAt(0) !== 65533) { r[t[221][a]] = 56576 + a;
			e[56576 + a] = t[221][a] }
	t[222] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借劸鑷傝彶铚氳（瑾硅璨婚剻闈為榧诲毈瀣浆鏂屾娈禍婵辩€曠墲鐜钵璩撻牷鎲戞胺鑱橀▉涔嶄簨浜涗粫浼轰技浣夸繜鍍垮彶鍙稿攩鍡ｅ洓澹ア濞戝瀵哄皠宸冲斧寰欐€濇崹鏂滄柉鏌舵熁姊娌欐硹娓ｇ€夌崊鐮傜ぞ绁€绁犵绡╃礂绲茶倖鑸嶈帋钃戣泧瑁熻瑭炶瑵璩滆郸杈偑椋奸楹濆墛铳╂湐铳拷".split("");
	for(a = 0; a != t[222].length; ++a)
		if(t[222][a].charCodeAt(0) !== 65533) { r[t[222][a]] = 56832 + a;
			e[56832 + a] = t[222][a] }
	t[223] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉倶鍒北鏁ｆ睍鐝婄敚鐤濈畻钂滈吀闇颁狗鎾掓鐓炶柀涓夛カ鏉夋．娓楄姛钄樿～鎻锋緛閳掗涓婂偡鍍忓劅鍟嗗柂鍢楀瓈灏欏碃甯稿簥搴犲粋鎯虫姗℃箻鐖界墍鐙€鐩哥ゥ绠辩繑瑁宠Т瑭宠薄璩為湝濉炵捊璩藉棁铳绱㈣壊鐗茬敓鐢ワキ绗欏澹诲都搴忓憾寰愭仌鎶掓嵖鏁嶆殤鏇欐浉鏍栨２鐘€鐟炵绲窎缃诧拷".split("");
	for(a = 0; a != t[223].length; ++a)
		if(t[223][a].charCodeAt(0) !== 65533) { r[t[223][a]] = 57088 + a;
			e[57088 + a] = t[223][a] }
	t[224] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借儱鑸掕柉瑗胯獡閫濋嫟榛嶉紶澶曞キ甯儨鏄旀櫝鏋愭睈娣呮綗鐭崇ⅸ钃嗛噵閷粰鍍婂厛鍠勫瑡瀹ｆ墖鏁炬棆娓茬吔鐞佺憚鐠囩捒鐧Κ绶氱箷缇ㄨ吅鑶宠埞铇氳煬瑭佃罚閬搁姂閻ラ楫崹灞戞娉勬穿娓垖钖涜せ瑷闆涧鍓℃毠娈茬簴锜捐磵闁冮櫇鏀濇秹鐕ギ鍩庡瀹€ф兒鎴愭槦鏅熺尒鐝圭洓鐪佺锟�".split("");
	for(a = 0; a != t[224].length; ++a)
		if(t[224][a].charCodeAt(0) !== 65533) { r[t[224][a]] = 57344 + a;
			e[57344 + a] = t[224][a] }
	t[225] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借仏鑱茶叆瑾犻啋涓栧嫝姝叉礂绋呯绱帮ク璨板彫鍢瀹靛皬灏戝发鎵€鎺冩悢鏄⒊娌兼秷婧€熺偆鐕掔敠鐤忕枎鐦欑瑧绡犵矮绱犵垂钄暛铇囪ù閫嶉仭閭甸姺闊堕ǚ淇楀爆鏉熸稇绮熺簩璎栬礀閫熷宸芥悕钃€閬滈！鐜囧畫鎮氭澗娣炶瑾﹂€侀爩鍒凤グ鐏戠閹栬“閲椾慨鍙楀椊鍥氬瀭澹藉珎瀹堝搏宄€甯ユ剚锟�".split("");
	for(a = 0; a != t[225].length; ++a)
		if(t[225][a].charCodeAt(0) !== 65533) { r[t[225][a]] = 57600 + a;
			e[57600 + a] = t[225][a] }
	t[226] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥垗鎵嬫巿鎼滄敹鏁告ü娈婃按娲欐急鐕х嫨鐛哥悋鐠茬槮鐫＄绌楃绮圭稄缍埂缇炶劑鑼辫拹钃氳棯琚栬璁愯几閬傞們閰姈閵归殝闅ч毃闆栭渶闋堥楂撻瑲鍙斿【澶欏瀹挎窇娼氱啛鐞＄捁鑲呰徑宸″緡寰亗鏃爳妤娈夋吹娣崇彛鐩剧灛绛嶇磾鑴ｈ垳鑽€钃磋暎瑭㈣珓閱囬尀闋嗛Υ鎴岃杩伴墺宕囧揣锟�".split("");
	for(a = 0; a != t[226].length; ++a)
		if(t[226][a].charCodeAt(0) !== 65533) { r[t[226][a]] = 57856 + a;
			e[57856 + a] = t[226][a] }
	t[227] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉旦鐟熻啙铦ㄦ繒鎷剧繏瑜惰ゲ涓炰箻鍍у嫕鍗囨壙鏄囩供锠呴櫈渚嶅寵鍢跺濯ゅ案灞庡睄甯傚紤鎭冩柦鏄檪鏋炬煷鐚滅煝绀虹繀钂旇搷瑕栬│瑭╄璞曡焙鍩村瘮寮忔伅鎷娈栨箿鐔勭瘨铦曡瓨杌鹃椋句几渚佷俊鍛诲瀹告劶鏂版櫒鐕肩敵绁炵闯鑵庤嚕鑾樿柂钘庤渻瑷婅韩杈涳ケ杩呭け瀹ゅ鎮夊灏嬪績娌侊拷".split("");
	for(a = 0; a != t[227].length; ++a)
		if(t[227][a].charCodeAt(0) !== 65533) { r[t[227][a]] = 58112 + a;
			e[58112 + a] = t[227][a] }
	t[228] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤ゲ娣辩€嬬敋鑺浠€鍗侊コ闆欐皬浜炰縿鍏掑暈濞ュ敞鎴戠墮鑺借帾铔捐瑷濋樋闆呴榇夐禎鍫婂渤宥藉箘鎯℃剷鎻℃▊娓ラ剛閸旈榘愰椒瀹夊哺鎸夋檹妗堢溂闆侀瀺椤旈疅鏂¤瑏杌嬮柤鍞靛博宸栧旱鏆楃檶鑿撮棁澹撴娂鐙庨川浠板ぎ鎬忔樆娈冪Ё榇﹀帗鍝€鍩冨礀鎰涙洊娑鑹鹃殬闈勫巹鎵兼帠娑茬笂鑵嬮锟�".split("");
	for(a = 0; a != t[228].length; ++a)
		if(t[228][a].charCodeAt(0) !== 65533) { r[t[228][a]] = 58368 + a;
			e[58368 + a] = t[228][a] }
	t[229] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥缃岄动楦氫篃鍊诲喍澶滄児鎻舵ぐ鐖鸿€讹ゴ閲庡急铳碉ザ绱勮嫢钁捇钘ヨ簫铳蜂蒋铳革ス澹ゅ瓋鎭欐彋鏀樻暛鏆橈ズ妤婃ǎ娲嬬€佺叕鐥掔槏绂崇┌铳荤緤铳艰铳借畵閲€闄斤ゾ椁婂渼寰℃柤婕佺榾绂﹁獮棣瓪榻剟鎲舵姂妾嶈噯鍋冨牥褰︾剦瑷€璜哄铇栦亢鍎煎毚濂勬帺娣瑰丢妤唵浜堜綑铳匡铴佸铴傦拷".split("");
	for(a = 0; a != t[229].length; ++a)
		if(t[229][a].charCodeAt(0) !== 65533) { r[t[229][a]] = 58624 + a;
			e[58624 + a] = t[229][a] }
	t[230] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤姝熸睗铴勭挼绀栵鑸囪墔鑼硅伎杞濓椁橈铴堬浜︼鍩熷焦鏄擄铴岀柅绻硅铴嶉€嗛鍤ュ牕濮稿瀹达寤讹铴愭崘鎸伙妞芥矅娌挎稁娑撴返婕旓鐑熺劧鐓欙鐕冪嚂铴旂纭绛电罚铴栫腐铴楄杌燂铴欙閴涳槌讹铴濓鎮呮秴铴熺啽铴狅Α闁卞幁铴Γ铴ゆ煋铴ョ値鐒扮惏鑹惰嫆锟�".split("");
	for(a = 0; a != t[230].length; ++a)
		if(t[230][a].charCodeAt(0) !== 65533) { r[t[230][a]] = 58880 + a;
			e[58880 + a] = t[230][a] }
	t[231] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤Ζ闁婚楣芥泟铴х噥钁夛Θ铴╁铴Λ宥稿奖铴槧鏆庢ス姒案娉虫付娼佹繗鐎涚€厫鐕熺嵃铴憶铴摂鐩堢绾擄Ο铴拌嫳瑭犺繋铴遍崍铴查湙铴筹Υ涔傚€Φ鍒堝彙鏇虫杯婵婄寠鐫跨鑺棟铇傦Χ瑁旇璀借鲍铴烽姵铴搁湏闋愪簲浼嶄繅鍌插崍鍚惧惓鍡氬、澧哄ェ濞涘鎮燂鎳婃晼鏃挎櫎姊ф睔婢筹拷".split("");
	for(a = 0; a != t[231].length; ++a)
		if(t[231][a].charCodeAt(0) !== 65533) { r[t[231][a]] = 59136 + a;
			e[59136 + a] = t[231][a] }
	t[232] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界儚鐔崚绛借湀瑾ら安榧囧眿娌冪崉鐜夐埡婧懃鐦熺┅绺曡槉鍏€澹呮搧鐡敃鐧扮縼閭曢泹楗旀甫鐡︾绐嚗铔欒澑瑷涘瀹屽疀姊℃娴ｇ帺鐞撶惉纰楃珐缈剺鑵曡帪璞岄槷闋戞洶寰€鏃烘瀴姹帇鍊▋姝煯澶栧惮宸嶇尌鐣忥铴诲儱鍑瑰牤澶濮氬铴硷宥㈡嫍鎼栨挀鎿撅鏇滐姗堬鐕跨懁铵侊拷".split("");
	for(a = 0; a != t[232].length; ++a)
		if(t[232][a].charCodeAt(0) !== 65533) { r[t[232][a]] = 59392 + a;
			e[59392 + a] = t[232][a] }
	t[233] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界獔绐箛绻炶€€鑵帮锜璎犻仚铵冮個楗掓吘娆叉荡绺熻ぅ杈变繎鍌啑鍕囧焽澧夊搴告厒姒曟秾婀ф憾鐔旂憿鐢ㄧ敩鑱宠尭钃夎笂閹旈彏铵勪簬浣戝伓鍎張鍙嬪彸瀹囧瘬灏ゆ剼鎲傛棿鐗涚帡鐟€鐩傜绂戠绱嗙窘鑺嬭棔铏炶總閬囬兊閲殔闆ㄩ洨鍕栧涧鏃槺鏍厹绋堕儊闋婁簯铵呮娈炴緪鐔夎€樿姼钑擄拷".split("");
	for(a = 0; a != t[233].length; ++a)
		if(t[233][a].charCodeAt(0) !== 65533) { r[t[233][a]] = 59648 + a;
			e[59648 + a] = t[233][a] }
	t[234] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介亱闅曢洸闊昏敋楝变簮鐔婇泟鍏冨師鍝″湏鍦掑灒濯涘珓瀵冩€ㄦ効鎻存矃娲规共婧愮埌鐚跨憲鑻戣杞呴仩铵嗛櫌椤橀礇鏈堣秺閴炰綅鍋夊優鍗卞湇濮斿▉灏夋叞鏆愭腑鐖茬憢绶儍钀庤懄钄胯潫琛涜璎傞仌闊嬮瓘涔充緫鍎掑叒铵囧敮鍠╁瀹ュ辜骞藉壕鎮犳儫鎰堟剦鎻勬敻鏈夛鏌旀煔铵夋ァ妤㈡补娲э娓革锟�".split("");
	for(a = 0; a != t[234].length; ++a)
		if(t[234][a].charCodeAt(0) !== 65533) { r[t[234][a]] = 59904 + a;
			e[59904 + a] = t[234][a] }
	t[235] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥俊鐚剁尫铵岀憸鐢憋鐧掞铵忕董鑷捐惛瑁曡獦璜涜韪拌箓閬婇€鹃伜閰夐噳閸铵戝爥铵掓瘬鑲夎偛铵擄鍏佸カ灏癸铵栨饯鐜ц儰璐囷閳楅枏铵橈铵氾鑱挎垘鐎滅胆铻嶏鍨犳仼鎱囨瑾鹃妧闅变箼鍚熸帆钄櫚闊抽．鎻栨常閭戝嚌鎳夎喓榉逛緷鍊氬剙瀹滄剰鎳挎摤妞呮瘏鐤戠煟缇╄墹钖忚熁琛ｈ锟�".split("");
	for(a = 0; a != t[235].length; ++a)
		if(t[235][a].charCodeAt(0) !== 65533) { r[t[235][a]] = 60160 + a;
			e[60160 + a] = t[235][a] }
	t[236] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借閱簩浠ヤ紛铵濓澶峰Ж铵熷凡寮涘經鎬★铵★Б铵ｇ埦鐝ワГ鐣扮棈铵ョЩ铵﹁€岃€宠倓鑻¤崙铵эЖ璨借渤閭囷З铵４椁岋Й铵€风泭缈婄繉缈艰瑲浜轰粊鍒冨嵃铵捊鍥犲Щ瀵呭紩蹇嶆巩铵Н绲尩铵拌殦瑾嶏П闈澐铵诧С涓€浣氫骄澹规棩婧㈤€搁幇棣逛换澹濮欐亖铵达У绋旓Ф鑽忚硟鍏ュ崉锟�".split("");
	for(a = 0; a != t[236].length; ++a)
		if(t[236][a].charCodeAt(0) !== 65533) { r[t[236][a]] = 60416 + a;
			e[60416 + a] = t[236][a] }
	t[237] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤Х铵革Ч浠嶅墿瀛曡娍浠斿埡鍜ㄥ濮垮瓙瀛楀瓬鎭ｆ厛婊嬬倷鐓巻鐡风柕纾佺传鑰呰嚜鑼ㄨ敆钘夎璩囬泴浣滃嫼鍤兼柅鏄ㄧ伡鐐哥埖缍借妽閰岄泙榈插妫ф畼娼虹洖宀戞毇娼涚绨牰闆滀笀浠楀尃鍫村⒒澹ガ灏囧赋搴勫嫉鎺屾毑鏉栨妾ｆ瑢婕跨墕铵虹崘鐠嬬珷绮ц吀鑷熻嚙鑾婅懍钄ｈ枖钘忚璐撻啲闀凤拷".split("");
	for(a = 0; a != t[237].length; ++a)
		if(t[237][a].charCodeAt(0) !== 65533) { r[t[237][a]] = 60672 + a;
			e[60672 + a] = t[237][a] }
	t[238] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介殰鍐嶅搲鍦ㄥ鎵嶆潗鏍芥娓芥粨鐏界浮瑁佽病杓夐綃榻庣埈绠忚珝閷氫絿浣庡劜鍜€濮愬簳鎶垫澋妤娌笟鐙欑尓鐤界绱佃嫥鑿硅憲钘疯璨簢閫欓偢闆庨綗鍕ｅ悐瀚″瘋鎽樻暤婊寸媱铵荤殑绌嶇瑳绫嶇妇缈熻嵒璎硦璧よ贰韫熻开杩归仼閺戜絻浣哄偝鍏ㄥ吀鍓嶅壀濉″〖濂犲皥灞曞粵鎮涙埌鏍撴姘堟颈锟�".split("");
	for(a = 0; a != t[238].length; ++a)
		if(t[238][a].charCodeAt(0) !== 65533) { r[t[238][a]] = 60928 + a;
			e[60928 + a] = t[238][a] }
	t[239] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界厧鐞犵敯鐢哥晳鐧茬瓕绠嬬绡嗙簭瑭季杞夐埧閵撻將閻浕椤氶～椁炲垏鎴姌娴欑櫎绔婄瘈绲跺崰宀惧簵婕哥偣绮橀湋楫庨粸鎺ユ懞铦朵竵浜曚涵鍋滃伒鍛堝瀹氬箑搴环寰佹儏鎸烘斂鏁存棇鏅舵櫢鏌炬エ妾夋姹€娣€娣ㄦ笩婀炵€炵偂鐜庣徑鐢虹潧纰囩绋嬬┙绮剧稁鑹囪▊璜矠閯厞閲橀墻閶岄尃闇嗛潠锟�".split("");
	for(a = 0; a != t[239].length; ++a)
		if(t[239][a].charCodeAt(0) !== 65533) { r[t[239][a]] = 61184 + a;
			e[61184 + a] = t[239][a] }
	t[240] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介潨闋傞紟鍒跺姂鍟煎牑甯濆紵鎮屾彁姊繜绁鑷嶈柡瑁借韫勯啀闄ら殯闇介榻婁繋鍏嗗噵鍔╁槻寮斿将鎺搷鏃╂檨鏇烘浌鏈濇妫楁Ы婕曟疆鐓х嚗鐖挭鐪虹绁氱绋犵獣绮楃碂绲勭拱鑲囪椈铓よ瑾胯稒韬侀€犻伃閲ｉ樆闆曢偿鏃忕皣瓒抽弮瀛樺皧鍗掓嫏鐚濆€у畻寰炴偘鎱娣欑惍绋祩缍滅副鑵拷".split("");
	for(a = 0; a != t[240].length; ++a)
		if(t[240][a].charCodeAt(0) !== 65533) { r[t[240][a]] = 61440 + a;
			e[61440 + a] = t[240][a] }
	t[241] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借釜韪甸嵕閻樹綈鍧愬乏搴ф尗缃富浣忎緩鍋氬鑳勫應鍛ㄥ椌濂忓畽宸炲粴鏅濇湵鏌辨牚娉ㄦ床婀婃緧鐐风彔鐤囩睂绱傜船缍㈣垷铔涜ɑ瑾呰蛋韬婅汲閫遍厧閰掗憚椐愮绮ヤ繆鍎佸噯鍩堝宄绘櫃妯芥禋婧栨楷鐒岀暞绔ｈ牏閫￠伒闆嬮Э鑼佷腑浠茶閲嶅嵔娅涙カ姹佽懞澧炴啂鏇炬嫰鐑濈攽鐥囩箳钂歌瓑璐堜箣鍙拷".split("");
	for(a = 0; a != t[241].length; ++a)
		if(t[241][a].charCodeAt(0) !== 65533) { r[t[241][a]] = 61696 + a;
			e[61696 + a] = t[241][a] }
	t[242] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉挮鍦板潃蹇楁寔鎸囨懐鏀棬鏅烘灊鏋虫姹犳矚婕煡鐮ョ绁楃礄鑲㈣剛鑷宠姖鑺疯湗瑾岋Ъ璐勮毒閬茬洿绋欑ǚ绻旇伔鍞囧棓濉垫尟鎼㈡檳鏅嬫…姒涙畡娲ユ罕鐝嶇懆鐠＄暃鐤圭洝鐪炵瀷绉︾笁绺濊嚮钄瑷鸿硲杌景閫查幁闄ｉ櫝闇囦緞鍙卞И瀚夊笝妗庣搯鐤剧З绐掕啠铔唱璺岃凯鏂熸湑铵藉煼娼楃窛杓拷".split("");
	for(a = 0; a != t[242].length; ++a)
		if(t[242][a].charCodeAt(0) !== 65533) { r[t[242][a]] = 61952 + a;
			e[61952 + a] = t[242][a] }
	t[243] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介彾闆嗗镜鎳叉緞涓斾緲鍊熷弶鍡熷弹宸姝ょ绠氾Ь韫夎粖閬崏鎼剧潃绐勯尟閼块姜鎾版警鐕︾挩鐡氱珓绨掔簜绮茬簶璁氳磰閼介楗屽埞瀵熸摝鏈串鍍弮濉规厴鎱欐嚭鏂珯璁掕畺鍊夊€″壍鍞卞寤犲桨鎰存暈鏄屾樁鏆㈡婊勬疾鐚栫槨绐撹劰鑹欒彇钂煎偟鍩板瘈瀵ㄥ僵鎺＄牔缍佃彍钄￠噰閲靛唺鏌电瓥锟�".split("");
	for(a = 0; a != t[243].length; ++a)
		if(t[243][a].charCodeAt(0) !== 65533) { r[t[243][a]] = 62208 + a;
			e[62208 + a] = t[243][a] }
	t[244] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借铂鍑勫鎮借檿鍊滐Э鍓斿昂鎱芥垰鎷撴摬鏂ユ粚鐦犺剨韫犻櫉闅讳粺鍗冨枠澶╁窛鎿呮硥娣虹帞绌胯垱钖﹁长韪愰伔閲ч棥闃￠焼鍑稿摬鍠嗗竟鎾ゆ緢缍磋紵杞嶉惖鍍夊皷娌炬坊鐢涚灮绨界堡瑭硅珎鍫炲甯栨嵎鐗掔枈鐫珳璨艰紥寤虫櫞娣歌伣鑿佽珛闈戦瘱铷€鍓冩浛娑曟化绶犺閫仦楂斿垵鍓垮摠鎲旀妱鎷涙ⅱ锟�".split("");
	for(a = 0; a != t[244].length; ++a)
		if(t[244][a].charCodeAt(0) !== 65533) { r[t[244][a]] = 62464 + a;
			e[62464 + a] = t[244][a] }
	t[245] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥妤氭ǖ鐐掔劍纭濈绀庣绋嶈倴鑹歌嫊鑽夎晧璨傝秴閰㈤唻閱績鍥戠嚟鐭楄渶瑙稿蹇栨潙閭ㄥ彚濉氬鎮ゆ唩鎽犵附鑱拌敟閵冩挳鍌磾鏈€澧滄娊鎺ㄦ妤告婀毢绉嬭娀钀╄珡瓒ㄨ拷閯掗厠閱滈寪閷橀帤闆涢ǘ榘嶄笐鐣滅绔虹瓚绡夌府钃勮箼韫磋桓閫愭槬妞跨憙鍑烘湲榛滃厖蹇犳矕锜茶琛锋偞鑶佃悆锟�".split("");
	for(a = 0; a != t[245].length; ++a)
		if(t[245][a].charCodeAt(0) !== 65533) { r[t[245][a]] = 62720 + a;
			e[62720 + a] = t[245][a] }
	t[246] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借磪鍙栧惞鍢村ǘ灏辩倞缈犺仛鑴嗚嚟瓒ｉ唹椹熼凡鍋翠粍鍘犳兓娓堡渚堝€ゅ棨宄欏篃鎭ユ娌绘穭鐔剧棓鐥寸櫋绋氱绶囩坊缃嚧铓╄紲闆夐Τ榻掑墖鍕呴－瑕竷鏌掓紗渚靛鏋曟矆娴哥悰鐮ч嚌閸艰焺绉ょū蹇粬鍜ゅ斁澧Ε鎯版墦鎷栨湺妤曡埖闄€棣遍鍊崜鍟勫澕铷佹墭铷傛摙鏅煗婵佹刊鐞㈢惛瑷楋拷".split("");
	for(a = 0; a != t[246].length; ++a)
		if(t[246][a].charCodeAt(0) !== 65533) { r[t[246][a]] = 62976 + a;
			e[62976 + a] = t[246][a] }
	t[247] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介惛鍛戝槅鍧﹀綀鎲氭瓗鐏樼偔缍昏獣濂劔鎺㈢湀鑰借勃濉旀惌姒诲畷甯戞汞铷冭暕鍏屽彴澶€犳厠娈嗘卑娉扮瑸鑳庤嫈璺嗛偘棰憋▌鎿囨兢鎾戞攧鍏庡悙鍦熻◣鎱熸《铷呯棝绛掔当閫氬爢妲岃吙瑜€€闋瑰伕濂楀Μ鎶曢€忛鎱濈壒闂栧潯濠嗗反鎶婃挱鎿烘澐娉㈡淳鐖惗鐮寸椒鑺窙闋楀垽鍧傛澘鐗堢摚璨╄睛閳戯拷".split("");
	for(a = 0; a != t[247].length; ++a)
		if(t[247][a].charCodeAt(0) !== 65533) { r[t[247][a]] = 63232 + a;
			e[63232 + a] = t[247][a] }
	t[248] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介槳鍏彮鎹屼僵鍞勬倴鏁楁矝娴跨墝鐙界瑕囪矟褰編鐑硅啫鎰庝究鍋忔墎鐗囩瘒绶ㄧ咯閬嶉灜楱欒捕鍧钩鏋拌悕瑭曞悹瀣栧梗寤㈠紛鏂冭偤钄介枆闄涗綀鍖呭實鍖忓拞鍝哄渻甯冩€栨姏鎶辨崟铷嗘场娴︾柋鐮茶優鑴嫗钁¤挷琚嶈閫嬮嫪椋介畱骞呮毚鏇濈€戠垎铷囦康鍓藉姜鎱撴潛妯欐紓鐡㈢エ琛ㄨ惫椋囬椹冿拷".split("");
	for(a = 0; a != t[248].length; ++a)
		if(t[248][a].charCodeAt(0) !== 65533) { r[t[248][a]] = 63488 + a;
			e[63488 + a] = t[248][a] }
	t[249] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉搧绋熸璜疯眾棰ㄩΞ褰兼姭鐤茬毊琚伩闄傚尮寮煎繀娉岀弻鐣㈢枊绛嗚嬀棣濅箯閫间笅浣曞帵澶忓粓鏄版渤鐟曡嵎铦﹁硛閬愰湠榘曞瀛歌檺璎旈洞瀵掓仺鎮嶆棻姹楁饥婢ｇ€氱綍缈伴枒闁掗檺闊撳壊杞勫嚱鍚捀鍟ｅ枈妾绘兜绶樿墻閵滈櫡楣瑰悎鍝堢洅铔ら枻闂旈櫆浜級濮宸锋亽鎶楁澀妗佹矄娓几鑲涜埅锟�".split("");
	for(a = 0; a != t[249].length; ++a)
		if(t[249][a].charCodeAt(0) !== 65533) { r[t[249][a]] = 63744 + a;
			e[63744 + a] = t[249][a] }
	t[250] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤▓铷夐爡浜ュ仌鍜冲灀濂氬瀹虫噲妤锋捣鐎ｈ煿瑙ｈ┎璜ч倐椐鍔炬牳鍊栧垢鏉忚崌琛屼韩鍚戝毊鐝﹂剷闊块楗楅鍣撳铏涜ū鎲叉鐛昏粧姝囬毆椹楀鐖€璧潻淇斿炒寮︽嚫鏅涙倡鐐巹鐜圭従鐪╃潔绲冪耽绺ｈ埛琛掞▕璩㈤墘椤瓚绌磋闋佸珜淇犲崝澶惧辰鎸炬倒鐙硅剠鑴囪帰閶忛牥浜ㄥ厔鍒戝瀷锟�".split("");
	for(a = 0; a != t[250].length; ++a)
		if(t[250][a].charCodeAt(0) !== 65533) { r[t[250][a]] = 64e3 + a;
			e[64e3 + a] = t[250][a] }
	t[251] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉舰娉傛粠鐎呯亹鐐啋鐝╃懇鑽婅灑琛￠€堥偄閹ｉΘ鍏綏鎯犳収鏆宠暀韫婇啹闉嬩箮浜掑懠澹曞：濂藉驳寮ф埗鎵堟槉鏅ф娴╂窂婀栨桓婢旀繝婵╃仢鐙愮惀鐟氱摖鐨撶绯婄笧鑳¤姦钁捒铏庤櫉铦磋璞幀闋€椤ユ儜鎴栭叿濠氭槒娣锋妇鐞块瓊蹇芥儦绗忓搫寮樻睘娉撴椽鐑樼磪铏硅▽榇诲寲鍜屽瑓妯虹伀鐣碉拷".split("");
	for(a = 0; a != t[251].length; ++a)
		if(t[251][a].charCodeAt(0) !== 65533) { r[t[251][a]] = 64256 + a;
			e[64256 + a] = t[251][a] }
	t[252] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界绂捐姳鑿┍璀佽波闈达▼鎿存敨纰虹⒒绌父鍠氬瀹﹀够鎮ｆ彌姝℃櫏妗撴笝鐓ョ挵绱堥倓椹╅哎娲绘粦鐚捐眮闂婂嚢骞屽鲸鎭嶆兌鎰版厡鏅冩檮姒ユ硜婀熸粔娼㈢厡鐠滅殗绡佺哀鑽掕潡閬戦殟榛冨尟鍥炲换寰婃仮鎮旀嚪鏅︽渻妾滄樊婢伆鐛躬鑶捐尨铔旇璩勫妰鐛插畺姗悇鍝殕瀛濇晥鏂呮泬姊熸秿娣嗭拷".split("");
	for(a = 0; a != t[252].length; ++a)
		if(t[252][a].charCodeAt(0) !== 65533) { r[t[252][a]] = 64512 + a;
			e[64512 + a] = t[252][a] }
	t[253] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界埢鑲撮叺椹嶄警鍊欏帤鍚庡惣鍠夊梾甯垮緦鏈界叇鐝濋€呭嫑鍕冲·澹庣剟鐔忕嚮钖拌〒鏆堣枿鍠ф殑鐓婅惐鍗夊枡姣佸綑寰芥彯鏆夌厙璜辫紳楹句紤鎼虹儖鐣﹁櫑鎭よ瓗榉稿厙鍑跺寛娲惰兏榛戞槙娆ｇ倶鐥曞悆灞圭磭瑷栨瑺娆芥瓎鍚告伆娲界繒鑸堝儢鍑炲枩鍣泹濮瑝甯屾啓鎲樻埍鏅炴洣鐔欑喒鐔虹姧绂х█缇茶┌锟�".split("");
	for(a = 0; a != t[253].length; ++a)
		if(t[253][a].charCodeAt(0) !== 65533) { r[t[253][a]] = 64768 + a;
			e[64768 + a] = t[253][a] }
	return { enc: r, dec: e }
}();
cptable[950] = function() {
	var e = [],
		r = {},
		t = [],
		a;
	t[0] = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[0].length; ++a)
		if(t[0][a].charCodeAt(0) !== 65533) { r[t[0][a]] = 0 + a;
			e[0 + a] = t[0][a] }
	t[161] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷銆€锛屻€併€傦紟鈥э紱锛氾紵锛侊赴鈥︹€ワ箰锕戯箳路锕旓箷锕栵箺锝溾€擄副鈥旓赋鈺达复锕忥紙锛夛傅锔讹經锝濓阜锔搞€斻€曪腹锔恒€愩€戯富锔笺€娿€嬶附锔俱€堛€夛缚锕€銆屻€嶏箒锕傘€庛€忥箖锕勶箼锕氾拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤箾锕滐節锕炩€樷€欌€溾€濄€濄€炩€碘€诧純锛嗭紛鈥宦с€冣棆鈼忊柍鈻测棊鈽嗏槄鈼団梿鈻♀枲鈻解柤銑ｂ剠炉锟ｏ伎藣锕夛箠锕嶏箮锕嬶箤锕燂範锕★紜锛嵜椕仿扁垰锛滐紴锛濃墻鈮р墵鈭炩墥鈮★耿锕ｏ工锕ワ功锝炩埄鈭姤鈭犫垷鈯裤彃銖戔埆鈭埖鈭粹檧鈾傗姇鈯欌啈鈫撯啇鈫掆問鈫椻啓鈫樷垾鈭ｏ紡锟�".split("");
	for(a = 0; a != t[161].length; ++a)
		if(t[161][a].charCodeAt(0) !== 65533) { r[t[161][a]] = 41216 + a;
			e[41216 + a] = t[161][a] }
	t[162] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锛尖垥锕紕锟ャ€掞繝锟★紖锛犫剝鈩夛供锕公銖曘帨銕濄帪銖庛帯銕庛帍銖劼板厵鍏涘厼鍏濆叀鍏ｅ棫鐡╃硯鈻佲杺鈻冣杽鈻呪枂鈻団枅鈻忊枎鈻嶁枌鈻嬧枈鈻夆敿鈹粹敩鈹も敎鈻斺攢鈹傗枙鈹屸攼鈹斺敇鈺拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟解暜鈺扳暞鈺愨暈鈺暋鈼⑩棧鈼モ棨鈺扁暡鈺筹紣锛戯紥锛擄紨锛曪紪锛楋紭锛欌厾鈪♀參鈪ｂ叅鈪モ叇鈪р叏鈪┿€°€€ｃ€ゃ€ャ€︺€с€ㄣ€╁崄鍗勫崊锛★饥锛ｏ激锛ワ鸡锛э绩锛╋吉锛棘锛籍锛及锛憋疾锛筹即锛碉级锛凤几锛癸己锝侊絺锝冿絼锝咃絾锝囷綀锝夛綂锝嬶綄锝嶏綆锝忥綈锝戯綊锝擄綌锝曪綎锟�".split("");
	for(a = 0; a != t[162].length; ++a)
		if(t[162][a].charCodeAt(0) !== 65533) { r[t[162][a]] = 41472 + a;
			e[41472 + a] = t[162][a] }
	t[163] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锝楋綐锝欙綒螒螔螕螖螘螙螚螛螜螝螞螠螡螢螣螤巍危韦违桅围唯惟伪尾纬未蔚味畏胃喂魏位渭谓尉慰蟺蟻蟽蟿蠀蠁蠂蠄蠅銊呫剢銊囥剤銊夈剨銊嬨剬銊嶃剮銊忥拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟姐剱銊戙剴銊撱剶銊曘剸銊椼剺銊欍剼銊涖劀銊濄劄銊熴劆銊°劉銊ｃ劋銊ャ劍銊с劏銊┧櫵壦娝囁嬶拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鈧拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[163].length; ++a)
		if(t[163][a].charCodeAt(0) !== 65533) { r[t[163][a]] = 41728 + a;
			e[41728 + a] = t[163][a] }
	t[164] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷涓€涔欎竵涓冧箖涔濅簡浜屼汉鍎垮叆鍏嚑鍒€鍒佸姏鍖曞崄鍗滃張涓変笅涓堜笂涓父鍑′箙涔堜篃涔炰簬浜″厐鍒冨嫼鍗冨弶鍙ｅ湡澹澶уコ瀛愬瓚瀛撳灏忓阿灏稿北宸濆伐宸卞凡宸冲肪骞插痪寮嬪紦鎵嶏拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟戒笐涓愪笉涓赴涓逛箣灏逛簣浜戜簳浜掍簲浜粊浠€浠冧粏浠囦粛浠婁粙浠勫厓鍏佸収鍏叜鍏啑鍑跺垎鍒囧垐鍕诲嬀鍕垮寲鍖瑰崍鍗囧崊鍗炲巹鍙嬪強鍙嶅，澶╁か澶き瀛斿皯灏ゅ昂灞反骞诲豢寮斿紩蹇冩垐鎴舵墜鎵庢敮鏂囨枟鏂ゆ柟鏃ユ洶鏈堟湪娆犳姝规瘚姣旀瘺姘忔按鐏埅鐖剁埢鐗囩墮鐗涚姮鐜嬩笝锟�".split("");
	for(a = 0; a != t[164].length; ++a)
		if(t[164][a].charCodeAt(0) !== 65533) { r[t[164][a]] = 41984 + a;
			e[41984 + a] = t[164][a] }
	t[165] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷涓栦笗涓斾笜涓讳箥涔忎箮浠ヤ粯浠斾粫浠栦粭浠ｄ护浠欎粸鍏呭厔鍐夊唺鍐嚬鍑哄嚫鍒婂姞鍔熷寘鍖嗗寳鍖濅粺鍗婂崏鍗″崰鍗嵁鍘诲彲鍙ゅ彸鍙彯鍙╁彣鍙煎徃鍙靛彨鍙﹀彧鍙插彵鍙板彞鍙徎鍥涘洑澶栵拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉ぎ澶卞ゴ濂跺瓡瀹冨凹宸ㄥ阀宸﹀競甯冨钩骞煎紒寮樺紬蹇呮垔鎵撴墧鎵掓墤鏂ユ棪鏈湰鏈湯鏈姣嶆皯姘愭案姹佹眬姘剧姱鐜勭帀鐡滅摝鐢樼敓鐢ㄧ敥鐢扮敱鐢茬敵鐤嬬櫧鐨毧鐩煕鐭㈢煶绀虹绌寸珛涓炰笩涔掍箵涔╀簷浜や害浜ヤ豢浼変紮浼婁紩浼嶄紣浼戜紡浠蹭欢浠讳话浠充唤浼佷紜鍏夊厙鍏嗗厛鍏拷".split("");
	for(a = 0; a != t[165].length; ++a)
		if(t[165][a].charCodeAt(0) !== 65533) { r[t[165][a]] = 42240 + a;
			e[42240 + a] = t[165][a] }
	t[166] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍏卞啀鍐板垪鍒戝垝鍒庡垨鍔ｅ寛鍖″尃鍗板嵄鍚夊悘鍚屽悐鍚愬悂鍚嬪悇鍚戝悕鍚堝悆鍚庡悊鍚掑洜鍥炲洕鍦冲湴鍦ㄥ湱鍦湳鍦╁澶氬し澶稿濂稿濂藉ス濡傚瀛楀瓨瀹囧畧瀹呭畨瀵哄皷灞瑰窞甯嗗苟骞达拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉紡寮涘繖蹇栨垘鎴屾垗鎴愭墸鎵涙墭鏀舵棭鏃ㄦ棳鏃洸鏇虫湁鏈芥湸鏈辨湹娆℃姝绘皷姹濇睏姹欐睙姹犳睈姹曟薄姹涙睄姹庣伆鐗熺墲鐧剧绫崇掣缂剁緤缇借€佽€冭€岃€掕€宠伩鑲夎倠鑲岃嚕鑷嚦鑷艰垖鑸涜垷鑹壊鑹捐櫕琛€琛岃。瑗块槨涓蹭酣浣嶄綇浣囦綏浣炰即浣涗綍浼颁綈浣戜冀浼轰几浣冧綌浼间絾浣ｏ拷".split("");
	for(a = 0; a != t[166].length; ++a)
		if(t[166][a].charCodeAt(0) !== 65533) { r[t[166][a]] = 42496 + a;
			e[42496 + a] = t[166][a] }
	t[167] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷浣滀綘浼綆浼朵綑浣濅綀浣氬厡鍏嬪厤鍏靛喍鍐峰垾鍒ゅ埄鍒埁鍔姪鍔姮鍖ｅ嵆鍗靛悵鍚悶鍚惧惁鍛庡惂鍛嗗憙鍚冲憟鍛傚悰鍚╁憡鍚瑰惢鍚稿惍鍚靛惗鍚犲惣鍛€鍚卞惈鍚熷惉鍥洶鍥ゅ洬鍧婂潙鍧€鍧嶏拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉潎鍧庡溇鍧愬潖鍦诲／澶惧濡掑Θ濡炲Γ濡欏濡嶅Δ濡撳濡ュ瓭瀛滃瓪瀛涘畬瀹嬪畯灏眬灞佸翱灏惧矏宀戝矓宀屽帆甯屽簭搴囧簥寤峰紕寮熷饯褰㈠椒褰瑰繕蹇屽織蹇嶅勘蹇扛蹇垝鎴戞妱鎶楁姈鎶€鎵舵妷鎵妸鎵兼壘鎵规壋鎶掓壇鎶樻壆鎶曟姄鎶戞妴鏀规敾鏀告棻鏇存潫鏉庢潖鏉愭潙鏉滄潠鏉炴潐鏉嗘潬锟�".split("");
	for(a = 0; a != t[167].length; ++a)
		if(t[167][a].charCodeAt(0) !== 65533) { r[t[167][a]] = 42752 + a;
			e[42752 + a] = t[167][a] }
	t[168] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鏉撴潡姝ユ瘡姹傛睘娌欐瞾娌堟矇娌呮矝姹焙娌愭卑娌屾报娌栨矑姹芥矁姹叉本姹存矄姹舵矋娌旀矘娌傜伓鐏肩伣鐏哥墷鐗＄墵鐙勭媯鐜栫敩鐢敺鐢哥殏鐩煟绉佺绂跨┒绯荤綍鑲栬倱鑲濊倶鑲涜倸鑲茶壇鑺掞拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借妺鑺嶈瑙掕█璋疯眴璞曡矟璧よ蛋瓒宠韩杌婅緵杈拌總杩嗚繀杩勫贰閭戦偄閭偊閭ｉ厜閲嗛噷闃查槷闃遍槳闃甫涔栦钩浜嬩簺浜炰韩浜蒋渚濅緧浣充娇浣緵渚嬩締渚冧桨浣典緢浣╀交渚栦骄渚忎緫浣哄厰鍏掑厱鍏╁叿鍏跺吀鍐藉嚱鍒诲埜鍒峰埡鍒板埉鍒跺墎鍔惧娀鍗掑崝鍗撳崙鍗﹀嵎鍗稿嵐鍙栧彅鍙楀懗鍛碉拷".split("");
	for(a = 0; a != t[168].length; ++a)
		if(t[168][a].charCodeAt(0) !== 65533) { r[t[168][a]] = 43008 + a;
			e[43008 + a] = t[168][a] }
	t[169] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍜栧懜鍜曞拃鍛诲懛鍜勫拻鍜嗗懠鍜愬懕鍛跺拰鍜氬憿鍛ㄥ拫鍛藉拵鍥哄瀮鍧峰潽鍧╁潯鍧﹀潳鍧煎濂夊濂堝濂斿濡诲濡瑰Ξ濮戝濮愬濮嬪濮婂Ο濡冲濮呭瓱瀛ゅ瀹楀畾瀹樺疁瀹欏疀灏氬眻灞咃拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉眴宀峰病宀稿博宀脖宀冲笜甯氬笘甯曞笡甯戝垢搴氬簵搴滃簳搴栧欢寮﹀姬寮╁線寰佸娇褰煎繚蹇犲拷蹇靛靠鎬忔€旀€€垫€栨€€曟€℃€ф€╂€€涙垨鎴曟埧鎴炬墍鎵挎媺鎷屾媱鎶挎媯鎶规嫆鎷涙姭鎷撴嫈鎷嬫媹鎶ㄦ娊鎶兼嫄鎷欐媷鎷嶆姷鎷氭姳鎷樻嫋鎷楁媶鎶嫀鏀炬枾鏂兼椇鏄旀槗鏄屾槅鏄傛槑鏄€鏄忔槙鏄婏拷".split("");
	for(a = 0; a != t[169].length; ++a)
		if(t[169][a].charCodeAt(0) !== 65533) { r[t[169][a]] = 43264 + a;
			e[43264 + a] = t[169][a] }
	t[170] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鏄囨湇鏈嬫澀鏋嬫灂鏉辨灉鏉虫澐鏋囨灊鏋楁澂鏉版澘鏋夋澗鏋愭澋鏋氭灀鏉兼潽鏉叉姝︽姝挎皳姘涙常娉ㄦ吵娌辨硨娉ユ渤娌芥簿娌兼尝娌硶娉撴哺娉勬补娉佹伯娉楁硡娉辨部娌绘场娉涙硦娌朝娉滄硸娉狅拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界倳鐐庣倰鐐婄倷鐖埈鐖哥増鐗х墿鐙€鐙庣嫏鐙楃嫄鐜╃帹鐜熺帿鐜ョ斀鐤濈枡鐤氱殑鐩傜洸鐩寸煡鐭界ぞ绁€绁佺绉堢┖绌圭绯剧綌缇岀緥鑰呰偤鑲ヨ偄鑲辫偂鑲偐鑲磋偑鑲嚗鑷捐垗鑺宠姖鑺欒姯鑺借姛鑺硅姳鑺姤鑺姼鑺ｈ姲鑺捐姺铏庤櫛鍒濊〃杌嬭繋杩旇繎閭甸偢閭遍偠閲囬噾闀烽杸闃滈檧闃块樆闄勶拷".split("");
	for(a = 0; a != t[170].length; ++a)
		if(t[170][a].charCodeAt(0) !== 65533) { r[t[170][a]] = 43520 + a;
			e[43520 + a] = t[170][a] }
	t[171] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷闄傞毠闆ㄩ潚闈炰簾浜寒淇′镜渚究淇犱繎淇忎繚淇冧径淇樹繜淇婁織渚繍淇勪總淇氫繋淇炰痉鍏楀啋鍐戝啝鍓庡墐鍓婂墠鍓屽墜鍓囧媷鍕夊媰鍕佸實鍗楀嵒鍘氬彌鍜搥鍜ㄥ搸鍝夊捀鍜﹀挸鍝囧搨鍜藉挭鍝侊拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉搫鍝堝挴鍜挶鍜诲挬鍜у捒鍥垮瀭鍨嬪灎鍨ｅ灑鍩庡灝鍨撳濂戝濂庡濮滃濮垮В濮ㄥ▋濮ュИ濮氬Е濞佸Щ瀛╁瀹﹀瀹㈠灏佸睅灞忓睄灞嬪硻宄掑贩甯濆弗甯熷菇搴犲害寤哄紙寮渐寰堝緟寰婂緥寰囧緦寰夋€掓€濇€犳€ユ€庢€ㄦ亶鎭版仺鎭㈡亞鎭冩伂鎭仾鎭ゆ墎鎷滄寲鎸夋嫾鎷寔鎷嫿鎸囨嫳鎷凤拷".split("");
	for(a = 0; a != t[171].length; ++a)
		if(t[171][a].charCodeAt(0) !== 65533) { r[t[171][a]] = 43776 + a;
			e[43776 + a] = t[171][a] }
	t[172] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鎷嫭鎷炬嫶鎸戞寕鏀挎晠鏂柦鏃㈡槬鏄槧鏄ф槸鏄熸槰鏄辨槫鏇锋熆鏌撴煴鏌旀煇鏌灦鏋煹鏌╂煰鏌勬煈鏋存煔鏌ユ灨鏌忔煘鏌虫灠鏌欐煝鏌濇煉姝畠娈嗘姣掓瘲姘熸硥娲嬫床娲祦娲ユ磳娲辨礊娲楋拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥椿娲芥淳娲舵礇娉垫垂娲ф锤娲╂串娲垫磶娲偒鐐虹偝鐐偗鐐偢鐐偆鐖扮壊鐗壌鐙╃嫚鐙＄幏鐝婄幓鐜茬弽鐝€鐜崇敋鐢晱鐣岀晭鐣嬬柅鐤ょ枼鐤㈢枺鐧哥殕鐨囩殘鐩堢泦鐩冪泤鐪佺浌鐩哥湁鐪嬬浘鐩肩渿鐭滅爞鐮旂爩鐮嶇绁夌绁囩绂虹绉掔绌跨獊绔跨绫界磦绱呯磤绱夌磭绱勭磫缂哥編缇胯€勶拷".split("");
	for(a = 0; a != t[172].length; ++a)
		if(t[172][a].charCodeAt(0) !== 65533) { r[t[172][a]] = 44032 + a;
			e[44032 + a] = t[172][a] }
	t[173] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑰愯€嶈€戣€惰儢鑳ヨ儦鑳冭儎鑳岃儭鑳涜儙鑳炶儰鑳濊嚧鑸㈣嫥鑼冭寘鑻ｈ嫑鑻﹁寗鑻ヨ寕鑼夎嫆鑻楄嫳鑼佽嫓鑻旇嫅鑻炶嫇鑻熻嫰鑼嗚檺铏硅櫥铏鸿琛瑙旇▓瑷傝▋璨炶矤璧磋党瓒磋粛杌岃堪杩﹁竣杩骏锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷杩揩杩よ卡閮婇儙閮侀儍閰嬮厞閲嶉杺闄愰檵闄岄檷闈㈤潻闊嬮煭闊抽爜棰ㄩ椋熼棣欎箻浜冲€屽€嶅€ｄ刊鍊﹀€ヤ扛鍊╁€栧€嗗€煎€熷€氬€掑€戜亢鍊€鍊斿€ㄤ勘鍊″€嬪€欏€樹砍淇€€烤鍊€夊吋鍐ゅ啣鍐㈠噸鍑屽噯鍑嬪墫鍓滃墧鍓涘墲鍖嵖鍘熷帩鍙熷摠鍞愬攣鍞峰摷鍝ュ摬鍞嗗摵鍞斿摡鍝摗鍞夊摦鍝拷".split("");
	for(a = 0; a != t[173].length; ++a)
		if(t[173][a].charCodeAt(0) !== 65533) { r[t[173][a]] = 44288 + a;
			e[44288 + a] = t[173][a] }
	t[174] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍝﹀敡鍞囧摻鍞忓渻鍦勫焸鍩斿煁鍩冨爥澶忓濂樺濞戝濞滃濞涘〒濮濞ｅī濞ュ▽濞夊灞樺瀹冲瀹村瀹靛瀹稿皠灞戝睍灞愬抄宄藉郴宄敞宄板扯宕佸炒宸腑甯韩搴骇寮卞緬寰戝緪鎭欙拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥仯鎭ユ亹鎭曟伃鎭╂伅鎮勬偀鎮氭倣鎮旀倢鎮呮倴鎵囨嫵鎸堟嬁鎹庢尵鎸崟鎹傛崋鎹忔崏鎸烘崘鎸芥尓鎸尐鎹嶆崒鏁堟晧鏂欐梺鏃呮檪鏅夋檹鏅冩檼鏅屾檯鏅佹浉鏈旀湑鏈楁牎鏍告妗嗘鏍规妗旀牘姊虫牀妗屾鏍芥煷妗愭鏍兼鏍鏍撴牁妗佹畩娈夋姘ｆ哀姘ㄦ唉姘ゆ嘲娴稌娑堟秶娴︽蹈娴锋禉娑擄拷".split("");
	for(a = 0; a != t[174].length; ++a)
		if(t[174][a].charCodeAt(0) !== 65533) { r[t[174][a]] = 44544 + a;
			e[44544 + a] = t[174][a] }
	t[175] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷娴秹娴禋娴存旦娑屾秺娴规秴娴ユ稊鐑婄儤鐑ょ儥鐑堢儚鐖圭壒鐙肩嫻鐙界嫺鐙风巻鐝悏鐝彔鐝彏鐣旂暆鐣滅暁鐣欑柧鐥呯棁鐤茬柍鐤界柤鐤圭梻鐤哥殝鐨扮泭鐩嶇泿鐪╃湡鐪犵湪鐭╃牥鐮х牳鐮濈牬鐮凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界牓鐮牋鐮熺牪绁曠绁犵绁栫绁濈绁氱Г绉ｇЁ绉熺Е绉╃绐勭獔绔欑瑔绗戠矇绱＄礂绱嬬磰绱犵储绱旂磹绱曠礆绱滅磵绱欑礇缂虹綗缇旂繀缈佽€嗚€樿€曡€欒€楄€借€胯儽鑴傝儼鑴呰儹鑳磋剢鑳歌兂鑴堣兘鑴婅兗鑳嚟鑷垁鑸愯埅鑸埁鑸娀鑼崚鑽旇崐鑼歌崘鑽夎尩鑼磋崗鑼茶尮鑼惰寳鑽€鑼辫尐鑽冿拷".split("");
	for(a = 0; a != t[175].length; ++a)
		if(t[175][a].charCodeAt(0) !== 65533) { r[t[175][a]] = 44800 + a;
			e[44800 + a] = t[175][a] }
	t[176] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷铏旇殜铓殦铓よ毄铓岃殻铓滆“琛疯琚傝〗琛硅瑷愯◣瑷岃〞瑷婅瑷撹瑷忚☉璞堣焙璞硅病璨㈣捣韬粧杌旇粡杈遍€侀€嗚糠閫€杩鸿看閫冭拷閫呰扛閭曢儭閮濋儮閰掗厤閰岄嚇閲濋嚄閲滈嚈闁冮櫌闄ｉ櫋锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷闄涢櫇闄ら櫂闄為毣椋㈤Μ楠ㄩ珮楝ラ楝间咕鍋哄伣鍋滃亣鍋冨亴鍋氬亯鍋ュ伓鍋庡仌鍋靛伌鍋峰亸鍊忓伅鍋厹鍐曞嚢鍓壇鍕掑嫏鍕樺嫊鍖愬審鍖欏尶鍗€鍖惧弮鏇煎晢鍟暒鍟勫暈鍟″晝鍟婂敱鍟栧晱鍟曞敮鍟ゅ敻鍞暅鍞暎鍞冲晛鍟楀湀鍦嬪湁鍩熷爡鍫婂爢鍩犲煠鍩哄爞鍫靛煼鍩瑰濂㈠ǘ濠佸濠﹀┆濠€锟�".split("");
	for(a = 0; a != t[176].length; ++a)
		if(t[176][a].charCodeAt(0) !== 65533) { r[t[176][a]] = 45056 + a;
			e[45056 + a] = t[176][a] }
	t[177] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷濞煎濠氬﹩濠婂瀵囧瘏瀵勫瘋瀹垮瘑灏夊皥灏囧睜灞滃睗宕囧磫宕庡礇宕栧储宕戝穿宕斿礄宕ゅ揣宕楀发甯稿付甯冲阜搴峰焊搴跺旱搴惧嫉寮峰綏褰僵褰緱寰欏緸寰樺尽寰犲緶鎭挎偅鎮夋偁鎮ㄦ儖鎮存儲鎮斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥儏鎮绘偟鎯滄偧鎯樻儠鎯嗘儫鎮告儦鎯囨垰鎴涙増鎺犳帶鎹叉帠鎺㈡帴鎹锋崸鎺樻帾鎹辨帺鎺夋巸鎺涙崼鎺ㄦ巹鎺堟帣鎺℃幀鎺掓帍鎺€鎹绘崺鎹ㄦ嵑鏁濇晼鏁戞暀鏁楀暉鏁忔晿鏁曟晹鏂滄枦鏂棌鏃嬫棇鏃庢櫇鏅氭櫎鏅ㄦ櫐鏅炴浌鍕楁湜姊佹姊㈡姊垫】妗舵⒈姊ф姊版妫勬姊嗘姊旀姊ㄦ姊℃娆叉锟�".split("");
	for(a = 0; a != t[177].length; ++a)
		if(t[177][a].charCodeAt(0) !== 65533) { r[t[177][a]] = 45312 + a;
			e[45312 + a] = t[177][a] }
	t[178] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷姣姘稁娑兼烦娣欐恫娣℃穼娣ゆ坊娣烘竻娣囨穻娑窇娑窞娣规陡娣锋返娣呮窉娓氭兜娣氭帆娣樻藩娣辨樊娣ㄦ穯娣勬丢娣犊娣︾児鐒夌剨鐑界儻鐖界壗鐘佺寽鐚涚寲鐚撶寵鐜囩悈鐞婄悆鐞嗙従鐞嶇摖鐡讹拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界摲鐢滅敘鐣ョ暒鐣㈢暟鐤忕棓鐥曠柕鐥婄棈鐨庣洈鐩掔洓鐪风溇鐪肩湺鐪哥満纭纭庣ゥ绁ㄧキ绉荤獟绐曠瑺绗ㄧ瑳绗绗欑瑸绗矑绮楃矔绲嗙祪绲辩串绱圭醇绲€绱扮闯绲勭疮绲傜床绱辩冀缇炵練缈岀繋缈掕€滆亰鑱嗚劘鑴栬劊鑴劑鑴拌劋鑸傝埖鑸疯埗鑸硅帋鑾炶帢鑽歌帰鑾栬幗鑾帓鑾婅帗鑾夎帬鑽疯嵒鑽硷拷".split("");
	for(a = 0; a != t[178].length; ++a)
		if(t[178][a].charCodeAt(0) !== 65533) { r[t[178][a]] = 45568 + a;
			e[45568 + a] = t[178][a] }
	t[179] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑾嗚帶铏曞姜铔囪泙铓惰泟铓佃泦铔嬭毐铓泬琛撹琚堣琚掕琚嶈瑕撹瑷瑷ｈē瑷辫ō瑷熻瑷㈣眽璞氳博璨搏璨ㄨ勃璨ц掸璧﹁毒瓒鸿粵杌熼€欓€嶉€氶€楅€ｉ€熼€濋€愰€曢€為€犻€忛€㈤€栭€涢€旓拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介儴閮兘閰楅噹閲甸嚘閲ｉ嚙閲嚛闁夐櫔闄甸櫝闄搁櫚闄撮櫠闄烽櫖闆€闆洨绔犵珶闋傞爟榄氶偿楣甸箍楹ラ夯鍌㈠倣鍌呭倷鍌戝個鍌栧倶鍌氭渶鍑卞壊鍓村壍鍓╁嫗鍕濆嫑鍗氬帴鍟诲杸鍠у暭鍠婂枬鍠樺杺鍠滃柂鍠斿枃鍠嬪杻鍠冲柈鍠熷斁鍠插枤鍠诲柆鍠卞暰鍠夊柅鍠欏湇鍫牚鍫村牑鍫板牨鍫″牆鍫犲９澹哄锟�".split("");
	for(a = 0; a != t[179].length; ++a)
		if(t[179][a].charCodeAt(0) !== 65533) { r[t[179][a]] = 45824 + a;
			e[45824 + a] = t[179][a] }
	t[180] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷濠峰獨濠垮獟濯涘瀛冲瀵掑瘜瀵撳瘣灏婂皨灏卞祵宓愬创宓囧方骞呭附骞€骞冨咕寤婂粊寤傚粍寮煎江寰╁惊寰ㄦ儜鎯℃偛鎮舵儬鎰滄劊鎯烘剷鎯版兓鎯存叏鎯辨剮鎯舵剦鎰€鎰掓垷鎵夋帲鎺屾弿鎻€鎻╂弶鎻嗘弽锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鎻掓彛鎻愭彙鎻栨彮鎻嵍鎻存彧鎻涙憭鎻氭徆鏁炴暒鏁㈡暎鏂戞枑鏂櫘鏅版櫞鏅舵櫙鏆戞櫤鏅炬櫡鏇炬浛鏈熸湞妫烘妫犳妫楁妫熸５妫＇妫规妫叉＃妫嬫妞嶆妞庢妫氭ギ妫绘娆烘娈樻畺娈兼姘隘姘腐娓告箶娓℃覆婀ф箠娓犳弗娓ｆ笡婀涙箻娓ゆ箹婀腑娓︽汞娓存箥娓烘脯婀冩笣娓炬粙锟�".split("");
	for(a = 0; a != t[180].length; ++a)
		if(t[180][a].charCodeAt(0) !== 65533) { r[t[180][a]] = 46080 + a;
			e[46080 + a] = t[180][a] }
	t[181] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷婧夋笝婀庢梗婀勬共婀╂篃鐒欑剼鐒︾劙鐒＄劧鐓劀鐗岀妱鐘€鐚剁尌鐚寸尒鐞虹惇鐞崇悽鐞ョ惖鐞剁惔鐞悰鐞︾惃鐢ョ敠鐣暘鐥㈢棝鐥ｇ棛鐥樼棡鐥犵櫥鐧肩殩鐨撶毚鐩滅潖鐭纭’绋嶇▓绋嬬▍绋€绐橈拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界獥绐栫绔ｇ瓑绛栫瓎绛愮瓛绛旂瓖绛嬬瓘绛戠矡绮ョ禐绲愮胆绲曠传绲挡绲＄郸绲㈢蛋绲冲杽缈旂繒鑰嬭亽鑲呰厱鑵旇厠鑵戣厧鑴硅厗鑴捐厡鑵撹叴鑸掕垳鑿╄悆鑿歌悕鑿犺弲钀嬭弫鑿彵鑿磋憲钀婅彴钀岃弻鑿借彶鑿婅惛钀庤悇鑿滆悋鑿旇彑铏涜洘铔欒洯铔旇洓铔よ洂铔炶瑁佽琚辫瑕栬ɑ瑭犺瑭炶瑭侊拷".split("");
	for(a = 0; a != t[181].length; ++a)
		if(t[181][a].charCodeAt(0) !== 65533) { r[t[181][a]] = 46336 + a;
			e[46336 + a] = t[181][a] }
	t[182] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷瑭旇瑭愯﹩瑷磋ê瑷惰〇璞¤矀璨布璨宠步璩佽不璩€璨磋卜璨惰部璨歌秺瓒呰秮璺庤窛璺嬭窔璺戣穼璺涜穯杌昏桓杌艰緶閫€甸€遍€搁€查€堕剛閮甸剦閮鹃叄閰ラ噺閳旈垥閳ｉ垑閳為垗閳愰垏閳戦枖闁忛枊闁戯拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介枔闁掗枎闅婇殠闅嬮櫧闅呴殕闅嶉櫜闅勯泚闆呴泟闆嗛泧闆洸闊岄爡闋嗛爤椋ч＊椋）椋查－棣Ν榛冮粛榛戜簜鍌偟鍌插偝鍍呭偩鍌偡鍌诲偗鍍囧壙鍓峰壗鍕熷嫤鍕ゅ嫝鍕ｅ尟鍡熷棬鍡撳棪鍡庡棞鍡囧棏鍡ｅ棨鍡棜鍡″梾鍡嗗棩鍡夊湌鍦撳濉戝濉楀濉斿～濉屽…濉婂、濉掑濂у珌瀚夊珜濯惧濯硷拷".split("");
	for(a = 0; a != t[182].length; ++a)
		if(t[182][a].charCodeAt(0) !== 65533) { r[t[182][a]] = 46592 + a;
			e[46592 + a] = t[182][a] }
	t[183] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷濯冲珎濯插旦宓箤骞瑰粔寤堝紥褰欏粳寰剼鎰忔厛鎰熸兂鎰涙児鎰佹剤鎱庢厡鎱勬厤鎰炬劥鎰ф剭鎰嗘劮鎴℃垻鎼撴惥鎼炴惇鎼惤鎼悘鎼滄悢鎼嶆惗鎼栨悧鎼嗘暚鏂熸柊鏆楁殙鏆囨殘鏆栨殑鏆樻殟鏈冩妤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥妤锋妤旀サ妞版妤婃エ妤妤撴ス姒嗘妤ｆ姝囨姣€娈挎瘬姣芥孩婧粨婧舵粋婧愭簼婊囨粎婧ユ簶婧兼汉婧粦婧栨簻婊勬粩婧骇婧寸厧鐓欑叐鐓ょ厜鐓х厹鐓叇鐓岀叆鐓炵厗鐓ㄧ厲鐖虹墥鐚风崊鐚跨尵鐟憵鐟曠憻鐟炵憗鐞跨憴鐟涚憸鐣剁暩鐦€鐥扮榿鐥茬棻鐥虹椏鐥寸棾鐩炵洘鐫涚潾鐫︾潪鐫ｏ拷".split("");
	for(a = 0; a != t[183].length; ++a)
		if(t[183][a].charCodeAt(0) !== 65533) { r[t[183][a]] = 46848 + a;
			e[46848 + a] = t[183][a] }
	t[184] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐫圭潽鐫潨鐫ョ潹鐫㈢煯纰庣纰楃纰岀纭肩纰撶】绁虹タ绂佽惉绂界绋氱绋旂绋炵獰绐犵绡€绛犵绛х脖绮崇驳缍撶倒缍戠秮缍忕禌缃僵缃讲缇╃鲸缇よ仏鑱樿倖鑲勮叡鑵拌吀鑵ヨ叜鑵宠叓锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑵硅吅鑵﹁垍鑹囪拏钁疯惤钀辫懙钁﹁懌钁夎懍钁涜惣钀佃憽钁ｈ懇钁憜铏炶櫆铏熻浌铚撹湀铚囪渶铔捐浕铚傝渻铚嗚湂琛欒瑁旇瑁滆瑁濊！瑁婅瑁掕瑙ｈ┇瑭茶┏瑭﹁┅瑭拌獓瑭艰瑾犺┍瑾呰┉瑭㈣┊瑭┕瑭昏ň瑭ㄨ雹璨婅矇璩婅硣璩堣硠璨茶硟璩傝硡璺¤窡璺ㄨ矾璺宠泛璺筏璺﹁翰杓冭級杌捐紛锟�".split("");
	for(a = 0; a != t[184].length; ++a)
		if(t[184][a].charCodeAt(0) !== 65533) { r[t[184][a]] = 47104 + a;
			e[47104 + a] = t[184][a] }
	t[185] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷杈熻静閬嬮亰閬撻亗閬旈€奸仌閬愰亣閬忛亷閬嶉亼閫鹃亖閯掗剹閰叒閰╅噳閳烽墬閳搁埥閴€閳鹃墰閴嬮墹閴戦埓閴夐墠閴呴埞閳块墯闁橀殬闅旈殨闆嶉泲闆夐泭闆烽浕闆归浂闈栭澊闈堕爯闋戦爴闋婇爳闋岄＜椋达拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介＝椋鹃Τ棣遍Υ楂￠畅楹傞紟榧撻紶鍍у儺鍍ュ儢鍍儦鍍曞儚鍍戝儽鍍庡儵鍏㈠嚦鍔冨妭鍖卞幁鍡惧榾鍢涘槜鍡藉様鍢嗗槈鍢嶅槑鍡峰槚鍢熷槇鍢愬椂鍦樺湒濉靛【澧冨澧婂」澧呭〗澹藉ぅ澶㈠い濂ォ瀚″瀚╁珬瀚栧珮瀚ｅ瀵炲瀵″瀵﹀瀵㈠瀵熷皪灞㈠秳宥囧箾骞ｅ箷骞楀箶寤撳粬寮婂絾褰板竟鎱囷拷".split("");
	for(a = 0; a != t[185].length; ++a)
		if(t[185][a].charCodeAt(0) !== 65533) { r[t[185][a]] = 47360 + a;
			e[47360 + a] = t[185][a] }
	t[186] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鎰挎厠鎱锋參鎱ｆ厽鎱氭厴鎱垫埅鎾囨憳鎽旀挙鎽告憻鎽烘憫鎽ф惔鎽懟鏁叉枴鏃楁棖鏆㈡毃鏆濇姒ㄦ妲佹Ξ妲撴姒涙Ψ姒绘Λ姒存妲嶆Ν妲屾Ζ妲冩Γ姝夋瓕姘虫汲婕旀痪婕撴淮婕╂季婕犳棘婕忔紓婕拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥豢婊紗婕辨几婕叉迹婕曟极婕緢婕滑婕佹徊婊屾环鐔旂啓鐓界唺鐔勭啋鐖剧姃鐘栫崉鐛愮懁鐟ｇ應鐟扮懎鐢勭枒鐦х槏鐦嬬槈鐦撶洝鐩ｇ瀯鐫界澘鐫＄纰熺ⅶ纰崇ⅸ纰ｇ绂忕绋ū绐绔绠＄畷绠嬬绠楃疂绠旂畯绠哥畤绠勭补绮界簿缍荤栋缍滅督缍剧稜绶婄洞缍茬侗缍虹盯缍跨兜缍哥董绶掔穱缍拷".split("");
	for(a = 0; a != t[186].length; ++a)
		if(t[186][a].charCodeAt(0) !== 65533) { r[t[186][a]] = 47616 + a;
			e[47616 + a] = t[186][a] }
	t[187] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷缃扮繝缈＄繜鑱炶仛鑲囪厫鑶€鑶忚唸鑶婅吙鑶傝嚙鑷鸿垏鑸旇垶鑹嬭搲钂胯搯钃勮挋钂炶挷钂滆搵钂歌搥钃撹拹钂艰搼钃婅溈铚滆溁铚㈣湧铚磋湗铦曡湻铚╄３瑜傝４瑁硅８瑁借（瑜氳／瑾﹁獙瑾炶瑾嶈瑾撹锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷瑾瑾ㄨ獦瑾戣獨瑾ц豹璨嶈矊璩撹硲璩掕但瓒欒稌璺艰紨杓掕紩杓撹荆閬犻仒閬滈仯閬欓仦閬㈤仢閬涢剻閯橀劄閰甸吀閰烽叴閴搁妧閵呴姌閵栭壔閵撻姕閵ㄩ壖閵戦枴闁ㄩ柀闁ｉ枼闁ら殭闅滈殯闆岄洅闇€闈奸瀰闊堕牀闋橀棰遍椁呴椁夐楠楂﹂瓉榄傞炒槌堕吵楹奸蓟榻婂剟鍎€鍍诲兊鍍瑰剛鍎堝剦鍎呭嚋锟�".split("");
	for(a = 0; a != t[187].length; ++a)
		if(t[187][a].charCodeAt(0) !== 65533) { r[t[187][a]] = 47872 + a;
			e[47872 + a] = t[187][a] }
	t[188] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍔囧妶鍔夊妽鍔婂嫲鍘插槷鍢诲樄鍢插樋鍢村槱鍣撳檸鍣楀櫞鍢跺槸鍢板澧熷澧冲澧ⅸ澧﹀キ瀣夊瀣嬪瀣屽瑘瀵瀵╁灞ゅ饱宥濆稊骞㈠篃骞″虎寤氬粺寤濆唬寤犲綀褰卞痉寰垫叾鎱ф叜鎱濇厱鎲傦拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥吋鎱版叓鎱炬啩鎲愭啱鎲庢啲鎲氭啢鎲旀啴鎴懇鎽懝鎾炴挷鎾堟拹鎾版挜鎾撴挄鎾╂拻鎾挱鎾挌鎾挋鎾㈡挸鏁垫暦鏁告毊鏆毚鏆辨ǎ妯熸Ж妯佹妯欐Ы妯℃〒妯婃С妯傛▍妲☉姝愭瓗娈ゆ瘏姣嗘伎娼兼緞娼戞溅娼旀締娼經娼告疆婢庢胶娼版饯婢楁綐婊曟蒋娼犳綗鐔熺啲鐔辩啫鐗栫姏鐛庣崡鐟╃拫鐠冿拷".split("");
	for(a = 0; a != t[188].length; ++a)
		if(t[188][a].charCodeAt(0) !== 65533) { r[t[188][a]] = 48128 + a;
			e[48128 + a] = t[188][a] }
	t[189] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐟剧拃鐣跨槧鐦╃槦鐦ょ槮鐦＄槩鐨氱毢鐩ょ瀻鐬囩瀸鐬戠瀷纾嬬纰虹纰剧纰肩绋跨绌€绋界ǚ绋荤绐绠辩瘎绠寸瘑绡囩瘉绠犵瘜绯婄窢绶寸矾绶荤窐绶窛绶ㄧ罚绶氱窞绶╃稙绶欑凡绶圭降缃风警锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷缈╄€﹁啗鑶滆啙鑶犺啔鑶樿敆钄借敋钃敩钄敁钄戣敚钄¤敂钃敟钃胯攩铻傝澊铦惰潬铦﹁澑铦ㄨ潤铦楄潓铦撹琛濊瑜囪瑜撹瑜婅璜掕珖璜勮獣璜嬭瑾茶珘璜傝瑾拌珫璜嶈瑾硅珱璞岃睅璞碃璩炶肠璩よ超璩尝璩ｈ硿璩场璧稛瓒ｈ斧韪愯笣韪㈣笍韪╄笩韪¤笧韬鸿紳杓涜紵杓╄鸡杓紲杓烇拷".split("");
	for(a = 0; a != t[189].length; ++a)
		if(t[189][a].charCodeAt(0) !== 65533) { r[t[189][a]] = 48384 + a;
			e[48384 + a] = t[189][a] }
	t[190] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷杓ラ仼閬仺閬伔閯伴劖閯ч劚閱囬唹閱嬮唭閶呴娀閵烽嫪閵嫟閶侀姵閵奸嫆閶囬嫲閵查柇闁遍渼闇嗛渿闇夐潬闉嶉瀷闉忛牎闋牅棰抽椁撻椁橀椐愰椐涢椐曢椐欓楂楝ч瓍榄勯榄磫榇夛拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介磧楹╅壕榛庡ⅷ榻掑剴鍎樺剶鍎愬剷鍐€鍐嚌鍔戝姄鍕冲櫃鍣櫣鍣╁櫎鍣稿櫔鍣ㄥ櫏鍣卞櫙鍣櫌鍣跺澧惧澹呭ギ瀣濆瀛稿灏庡綂鎲叉啈鎲╂唺鎳嶆喍鎲炬噴鎳堟埌鎿呮搧鎿嬫捇鎾兼摎鎿勬搰鎿傛搷鎾挎搾鎿旀捑鏁存泦鏇夋毠鏇勬泧鏆告ń妯告ê姗欐┇姗樻ü姗勬姗℃姗囨ǖ姗熸﹫姝欐姘呮總婢辨尽锟�".split("");
	for(a = 0; a != t[190].length; ++a)
		if(t[190][a].charCodeAt(0) !== 65533) { r[t[190][a]] = 48640 + a;
			e[48640 + a] = t[190][a] }
	t[191] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷婵冩兢婵佹晶婢虫縺婢规径婢︽緺婢寸喚鐕夌噽鐕掔噲鐕曠喒鐕庣嚈鐕滅噧鐕勭崹鐠滅挘鐠樼挓鐠炵摙鐢岀攳鐦寸樃鐦虹洤鐩ョ灎鐬炵灍鐬ョ（纾氱，纾хΖ绌嶇绌嗙绌嬬绡欑皯绡夌绡涚绡╃绯曠硸绺婏拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界笐绺堢笡绺ｇ笧绺濈笁绺愮焦缇茬堪缈辩慨鑰ㄨ喅鑶╄啫鑷昏垐鑹樿墮钑婅暀钑堣暔钑╄晝钑夎暛钑暈铻冭灍铻炶灑铻嶈　瑜げ瑜ヨか瑜¤Κ瑕﹁璜鸿璜辫瑎璜滆璜璎佽瑐璜疯璜宠璜艰鲍璞矒璩磋箘韪辫复韫傝腹韪佃蓟杓几杓宠鲸杈﹂伒閬撮伕閬查伡閬洪劥閱掗尃閷堕嫺閷抽尟閷㈤嫾閷寗閷氾拷".split("");
	for(a = 0; a != t[191].length; ++a)
		if(t[191][a].charCodeAt(0) !== 65533) { r[t[191][a]] = 48896 + a;
			e[48896 + a] = t[191][a] }
	t[192] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閷愰對閷￠寱閷寵闁婚毀闅ㄩ毆闆曢湈闇戦湒闇嶉湏闇忛潧闈滈潶闉橀牥闋搁牷闋烽牠闋归牑椁愰え椁為椁￠椐Б椐遍楠奸楂楫戦磿榇ｉ处榇ㄩ磼榇涢粯榛旈緧榫滃劒鍎熷劇鍎插嫷鍤庡殌鍤愬殔鍤囷拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉殢澹曞澹戝瀣板瀣ゅ灏峰报宥煎逗宥藉陡骞綄寰芥噳鎳傛噰鎳︽噵鎴叉埓鎿庢搳鎿樻摖鎿版摝鎿摫鎿㈡摥鏂傛杻鏇欐洊妾€妾旀獎妾㈡獪娅涙姗炬獥妾愭獱姝滄姣氭皥婵樻勘婵熸繝婵涙郡婵刊婢€婵俊婵╂繒婵堪鐕х嚐鐕嚘鐕ョ嚟鐕嚧鐕犵埖鐗嗙嵃鐛茬挬鐠扮挦鐠ㄧ檰鐧傜檶鐩灣鐬灠鐬拷".split("");
	for(a = 0; a != t[192].length; ++a)
		if(t[192][a].charCodeAt(0) !== 65533) { r[t[192][a]] = 49152 + a;
			e[49152 + a] = t[192][a] }
	t[193] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐬х灜鐭７纾虹４纾绂хΚ绌楃绨囩皪绡剧绨岀癄绯犵硿绯炵尝绯熺硻绯濈府绺剧箚绺风覆绻冪斧绺界副绻呯箒绺寸腹绻堢傅绺跨腐缃勭砍缈艰伇鑱茶伆鑱伋鑷嗚噧鑶鸿噦鑷€鑶胯喗鑷夎喚鑷ㄨ垑鑹辫柂锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷钖勮暰钖滆枒钖旇柉钖涜枃钖ㄨ枈铏ц焵锜戣灣锜掕焼铻灮铻鸿焾锜嬭せ瑜惰瑜歌そ瑕瑤璎楄瑱璎涜瑠璎犺瑵璎勮瑦璞佽翱璞宠澈璩借臣璩歌郴瓒ㄨ箟韫嬭箞韫婅絼杓捐絺杞呰伎閬块伣閭勯倎閭傞個閯归啠閱為啘閸嶉巶閷ㄩ嵉閸婇崶閸嬮寴閸鹃崿閸涢嵃閸氶崝闂婇棆闂岄棃闂嗛毐闅搁洊闇滈湠闉犻煋椤嗛⒍椁甸▉锟�".split("");
	for(a = 0; a != t[193].length; ++a)
		if(t[193][a].charCodeAt(0) !== 65533) { r[t[193][a]] = 49408 + a;
			e[49408 + a] = t[193][a] }
	t[194] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷椐块楫楫椿榇块簨榛忛粸榛滈粷榛涢季榻嬪彚鍤曞毊澹欏瀣稿綕鎳ｆ埑鎿存摬鎿炬攩鎿烘摶鎿锋柗鏇滄湨妾虫娅冩妾告珎妾姝熸娈€夌€嬫烤鐎嗘亢鐎戠€忕嚮鐕肩嚲鐕哥嵎鐛电挧鐠跨敃鐧栫櫂锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐧掔灲鐬跨灮鐬肩绂绌㈢绔勭珔绨哀绨盀绨ｇ啊绯х箶绻曠篂绻氱埂绻掔箼缃堢抗缈昏伔鑱惰噸鑷忚垔钘忚柀钘嶈棎钘夎柊钖鸿柟钖﹁煰锜煵锜犺瑕茶Т璎ㄨ璎璞愯磪韫欒梗韫﹁工韫熻箷杌€杞夎綅閭囬們閭堥啱閱噽閹旈帄閹栭帰閹抽幃閹幇閹橀帤閹楅棓闂栭棎闂曢洟闆滈洐闆涢洖闇ら灒闉︼拷".split("");
	for(a = 0; a != t[194].length; ++a)
		if(t[194][a].charCodeAt(0) !== 65533) { r[t[194][a]] = 49664 + a;
			e[49664 + a] = t[194][a] }
	t[195] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷闉煿椤嶉椤岄椤撻⒑椁鹃た椁介ぎ棣ラ◣楂侀瑑楝嗛瓘榄庨瓖榀婇瘔榀介瘓榀€榈戦禎榈犻粻榧曢棘鍎冲殽澹炲澹㈠榫愬滑鎳叉嚪鎳舵嚨鏀€鏀忔洜鏇濇娅濇珰娅撶€涚€熺€ㄧ€氱€濈€曠€樼垎鐖嶇墭鐘㈢嵏锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐛虹捊鐡婄摚鐤囩枂鐧熺櫋鐭囩绂辩┇绌╃熬绨跨案绨界胺绫€绻弓绻圭供绻緟绻崇径缇圭靖鑷樿棭钘濊棯钘曡棨钘ヨ椃锜昏爡锠嶈煿锜捐瑗熻瑗炶瓉璀滆瓨璀夎瓪璀庤瓘璀嗚瓩璐堣磰韫艰共韬囪苟韫购韫磋綌杞庤经閭婇倠閱遍啴閺￠彂閺熼弮閺堥彍閺濋彇閺㈤弽閺橀彜閺楅彣闂滈毚闆ｉ湭闇ч潯闊滈熁椤烇拷".split("");
	for(a = 0; a != t[195].length; ++a)
		if(t[195][a].charCodeAt(0) !== 65533) { r[t[195][a]] = 49920 + a;
			e[49920 + a] = t[195][a] }
	t[196] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷椤橀棰奸楗夐楱欓瑣榀ㄩ榀栭瘺槎夐怠榈查氮榈簰楹楅簱楹村嫺鍤ㄥ毞鍤跺毚鍤煎￥瀛€瀛冨瀵跺穳鎳告嚭鏀樻敂鏀欐洣鏈ф鐎剧€扮€茬垚鐛荤搹鐧㈢櫏绀︾お绀か绔囩绫岀眱绫嶇朝绯拌井绻界辜锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绾傜綄鑰€鑷氳墻钘昏椆铇戣椇铇嗚構铇囪槉锠旇爼瑗よ瑙歌璀璀瓱璀磸璐嶈簤韬佽簠韬傞喆閲嬮悩閻冮徑闂￠湴椋勯楗戦Θ楱ò楱烽ǖ榘撻皪楣归旱榛ㄩ集榻熼剑榻″劮鍎稿泚鍥€鍥傚灞穽鎳兼嚲鏀濇敎鏂曟洨娅绘瑒娅烘鐏岀垱鐘х摉鐡旂櫓鐭撶睈绾忕簩缇艰槜铇槡锠ｈ牏锠¤牊瑗ガ瑕借锟�".split("");
	for(a = 0; a != t[196].length; ++a)
		if(t[196][a].charCodeAt(0) !== 65533) { r[t[196][a]] = 50176 + a;
			e[50176 + a] = t[196][a] }
	t[197] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷璀疯璐撹簥韬嶈簨杞熻警閱洪惍閻抽惖閻洪惛閻查惈闂㈤湼闇归湶闊块¨椤ラ椹呴﹥椹€楱鹃珡榄旈瓚榘哎槎洞榉傞陡楹濋化榧欓綔榻﹂涧鍎煎劵鍥堝泭鍥夊宸斿窉褰庢嚳鏀ゆ瑠姝＄亼鐏樼巰鐡ょ枈鐧櫖锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绂崇睜绫熻伨鑱借嚐瑗茶ク瑙艰畝璐栬礂韬戣簱杞￠厛閼勯憫閼掗溄闇鹃焹闊侀～楗曢椹嶉珤楝氶眽榘遍熬榘婚窊榉楅即榻姜榫斿泴宸栨垁鏀ｆ敨鏀洭娆愮摎绔婄堡绫ｇ饱绾撶簴绾旇嚔铇歌樋锠辫畩閭愰倧閼ｉ憼閼ら潹椤椹氶椹楅珦楂旈珣楸旈睏楸栭伐楹熼淮鍥戝）鏀仦鐧辩櫜鐭楃綈缇堣牰锠硅、璁撹畳锟�".split("");
	for(a = 0; a != t[197].length; ++a)
		if(t[197][a].charCodeAt(0) !== 65533) { r[t[197][a]] = 50432 + a;
			e[50432 + a] = t[197][a] }
	t[198] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷璁栬壏璐涢噣閼潅闈堥潉闊嗛“椹熼榄橀睙榉归泛楣奸菇榧囬椒榻插怀娆栫仯绫碑锠昏韬￠噥閼查懓椤遍楂栭榛岀仱鐭氳畾閼烽焿椹㈤━绾滆疁韬噮閼介懢閼奸狈楸搁环璞旈懣楦氱埁椹楦涢笧绫诧拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[198].length; ++a)
		if(t[198][a].charCodeAt(0) !== 65533) { r[t[198][a]] = 50688 + a;
			e[50688 + a] = t[198][a] }
	t[201] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷涔備箿鍑靛寶鍘備竾涓屼箛浜嶅洍铷屽碑褰充笍鍐囦笌涓簱浠備粔浠堝啒鍕煎崿鍘瑰湢澶冨が灏愬房鏃℃姣屾皵鐖夸副涓间花浠滀哗浠′粷浠氬垖鍖滃崒鍦㈠湥澶楀く瀹佸畡灏掑盎灞村背甯勫簚搴傚繅鎴夋墣姘曪拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥岸姹冩翱姘荤姰鐘扮帄绂歌倞闃炰紟浼樹棘浠典紨浠变紑浠蜂紙浼濅紓浼呬饥浼撲紕浠翠紥鍐卞垞鍒夊垚鍔﹀將鍖熷崓鍘婂悋鍥″洘鍦湭鍦村ぜ濡€濂煎濂诲ゾ濂峰タ瀛栧皶灏ュ奔灞哄被灞惧窡骞靛簞寮傚細褰村繒蹇斿繌鎵滄墳鎵ゆ墶鎵︽墷鎵欐墵鎵氭墺鏃棶鏈炬湽鏈告溁鏈烘溈鏈兼湷姘樻眴姹掓睖姹忔眾姹旀眿锟�".split("");
	for(a = 0; a != t[201].length; ++a)
		if(t[201][a].charCodeAt(0) !== 65533) { r[t[201][a]] = 51456 + a;
			e[51456 + a] = t[201][a] }
	t[202] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷姹岀伇鐗炵姶鐘电帋鐢櫩绌电綉鑹歌壖鑺€鑹借壙铏嶈ゾ閭欓倵閭橀倹閭旈槩闃ら槧闃ｄ綎浼讳舰浣変綋浣や季浣т綊浣熶絹浣樹辑浼充伎浣″啅鍐瑰垳鍒炲垺鍔姰鍖夊崳鍗插帋鍘忓惏鍚峰惇鍛斿憛鍚欏悳鍚ュ悩锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍚藉憦鍛佸惃鍚ゅ憞鍥洤鍥ュ潄鍧呭潓鍧夊潒鍧掑濂€濡﹀濡犲濡庡Β濡愬濡уΑ瀹庡畳灏ㄥ蔼宀嶅矎宀堝矉宀夊矑宀婂矄宀撳矔宸犲笂甯庡簨搴夊簩搴堝簫寮呭紳褰稿蕉蹇掑繎蹇愬凯蹇ㄥ慨蹇冲俊蹇ゅ浚蹇哄刊蹇峰炕鎬€蹇存埡鎶冩妼鎶庢姀鎶旀妵鎵辨壔鎵烘壈鎶佹妶鎵锋壗鎵叉壌鏀锋棸鏃存棾鏃叉椀鏉呮潎锟�".split("");
	for(a = 0; a != t[202].length; ++a)
		if(t[202][a].charCodeAt(0) !== 65533) { r[t[202][a]] = 51712 + a;
			e[51712 + a] = t[202][a] }
	t[203] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鏉欐潟鏉屾潏鏉濇潔鏉氭潒姣愭皺姘氭备姹ф鲍娌勬矉娌忔北姹暴娌氭杯娌囨矔娌滄宝姹虫饱姹绘矌鐏寸伜鐗ｇ娍鐘界媰鐙嗙媮鐘虹媴鐜曠帡鐜撶帞鐜掔敽鐢圭枖鐤曠殎绀借€磋倳鑲欒倫鑲掕倻鑺愯姀鑺呰妿鑺戣姄锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑺婅妰鑺勮备杩夎究閭熼偂閭ラ倿閭ч偁闃伴槰闃槶涓充緲浣间緟浣戒線渚囦蕉浣翠緣渚勪椒浣屼緱浣練浣逛緛浣镐緪渚滀緮渚炰緬渚備緯浣疆鍐炲喖鍐惧埖鍒插埑鍓嗗埍鍔煎寠鍖嬪尲鍘掑帞鍜囧懣鍜佸拺鍜傚拡鍛懞鍛惧懃鍛懘鍛﹀拲鍛憽鍛犲挊鍛ｅ懅鍛ゅ浄鍥瑰澂鍧插澀鍧澅鍧板澏鍨€鍧靛澔鍧冲澊鍧拷".split("");
	for(a = 0; a != t[203].length; ++a)
		if(t[203][a].charCodeAt(0) !== 65533) { r[t[203][a]] = 51968 + a;
			e[51968 + a] = t[203][a] }
	t[204] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍧ㄥ澖澶屽濡靛濮忓濡插濮佸Χ濡煎濮栧Ρ濡藉濮堝Υ濮囧瀛ュ畵瀹曞眲灞囧伯宀ゅ矤宀靛帛宀ㄥ铂宀熷玻宀并宀钵宀濆播宀跺舶宀﹀笚甯斿笝寮ㄥ饥寮ｅ激褰斿緜褰惧浇蹇炲骏鎬€︽€欐€叉€嬶拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥€存€婃€楁€虫€氭€炴€€㈡€嶆€愭€€撴€戞€屾€夋€滄垟鎴芥姯鎶存嫅鎶炬姫鎶舵媻鎶姵鎶娀鎶╂姲鎶告斀鏂ㄦ柣鏄夋椉鏄勬槖鏄堟椈鏄冩構鏄嶆槄鏃芥槕鏄愭浂鏈婃瀰鏉瀻鏋掓澏鏉绘灅鏋嗘瀯鏉存瀺鏋屾澓鏋熸瀾鏋欐瀮鏉芥瀬鏉告澒鏋旀娈€姝炬癁姘濇矒娉倡娉硻娌舵硵娌厂娌锋硱娉傛埠娉冩硢娉巢锟�".split("");
	for(a = 0; a != t[204].length; ++a)
		if(t[204][a].charCodeAt(0) !== 65533) { r[t[204][a]] = 52224 + a;
			e[52224 + a] = t[204][a] }
	t[205] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷娉掓碀娌存矈娌濇瞼娉炴硛娲版硩娉囨舶娉规硰娉╂硲鐐旂倶鐐呯倱鐐嗙倓鐐戠倴鐐傜倸鐐冪壀鐙栫媼鐙樼媺鐙滅嫆鐙旂嫐鐙岀嫅鐜ょ帯鐜帵鐜㈢帬鐜帩鐡濈摠鐢跨晙鐢剧枌鐤樼毌鐩崇洷鐩扮浀鐭哥熂鐭圭熁鐭猴拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界煼绁傜た绉呯└绌荤绫电辰鑰佃倧鑲偅鑲歌偟鑲垹鑺犺媭鑺姎鑺樿姏鑺佃姧鑺娂鑺炶姾鑺磋姩鑺¤姪鑻傝姢鑻冭姸鑺㈣櫚铏櫗铏睎杩掕繈杩撹繊杩栬繒杩楅偛閭撮偗閭抽偘闃归樈闃奸樅闄冧繊淇呬繐渚蹭繅淇嬩縼淇斾繙淇欎净渚充繘淇囦繓渚轰縺渚逛楷鍓勫墘鍕€鍕傚尳鍗煎帡鍘栧帣鍘樺捄鍜″挱鍜ュ搹锟�".split("");
	for(a = 0; a != t[205].length; ++a)
		if(t[205][a].charCodeAt(0) !== 65533) { r[t[205][a]] = 52480 + a;
			e[52480 + a] = t[205][a] }
	t[206] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍝冭實鍜峰挳鍝栧挾鍝呭搯鍜犲懓鍜煎挗鍜惧懖鍝炲挵鍨靛灋鍨熷灓鍨屽灄鍨濆灈鍨斿灅鍨忓灆鍨ュ灇鍨曞４澶嶅濮″濮█濮卞濮哄Ы濮煎Ф濮ゅР濮峰濮╁С濮靛濮惧Т濮灞屽硱宄樺硨宄楀硧宄涳拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉碁宄氬硥宄囧硦宄栧硴宄斿硰宄堝硢宄庡碂宄稿饭甯″涪甯ｅ笭甯ゅ喊搴ゅ孩搴涘海搴ュ紘寮綎寰嗘€锋€规仈鎭叉仦鎭呮亾鎭囨亯鎭涙亴鎭€鎭傛仧鎬ゆ亜鎭樻仸鎭墏鎵冩嫃鎸嶆寢鎷垫寧鎸冩嫬鎷规審鎸屾嫺鎷舵寑鎸撴寯鎷烘寱鎷绘嫲鏁佹晝鏂柨鏄舵槨鏄叉樀鏄滄槮鏄㈡槼鏄樅鏄濇槾鏄规槷鏈忔湊鏌佹煵鏌堟灪锟�".split("");
	for(a = 0; a != t[206].length; ++a)
		if(t[206][a].charCodeAt(0) !== 65533) { r[t[206][a]] = 52736 + a;
			e[52736 + a] = t[206][a] }
	t[207] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鏌滄灮鏌告煒鏌€鏋锋焻鏌煠鏌熸灥鏌嶆灣鏌锋煻鏌煟鏌傛灩鏌庢煣鏌版灢鏌兼焼鏌煂鏋煢鏌涙熀鏌夋煀鏌冩煪鏌嬫娈傛畡娈舵瘱姣樻癄姘犳啊娲ㄦ创娲礋娲兼纯娲掓磰娉氭闯娲勬礄娲烘礆娲戞磤娲濇祩锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷娲佹礃娲锋磧娲忔祤娲囨礌娲磮娲㈡磯娲愮偡鐐熺偩鐐辩偘鐐＄偞鐐电偐鐗佺墘鐗婄壃鐗扮壋鐗媻鐙ょ嫧鐙嫙鐙嫤鐙ｇ巺鐝岀弬鐝堢弲鐜圭幎鐜电幋鐝幙鐝囩幘鐝冪弳鐜哥弸鐡摦鐢晣鐣堢枾鐤櫣鐩勭湀鐪冪渼鐪呯湂鐩风浕鐩虹煣鐭ㄧ爢鐮戠爳鐮呯爯鐮忕爭鐮夌爟鐮撶绁岀绁呯绉曠绉忕绉庣獉锟�".split("");
	for(a = 0; a != t[207].length; ++a)
		if(t[207][a].charCodeAt(0) !== 65533) { r[t[207][a]] = 52992 + a;
			e[52992 + a] = t[207][a] }
	t[208] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绌剧珣绗€绗佺焙绫哥惫绫跨瞼绮佺磧绱堢磥缃樼緫缇嶇揪鑰囪€庤€忚€旇€疯儤鑳囪儬鑳戣儓鑳傝儛鑳呰儯鑳欒儨鑳婅儠鑳夎儚鑳楄儲鑳嶈嚳鑸¤姅鑻欒嬀鑻硅寚鑻ㄨ寑鑻曡尯鑻嫋鑻磋嫭鑻¤嫴鑻佃寣鑻昏嫸鑻拌嫪锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑻よ嫚鑻鸿嫵鑻櫡铏磋櫦铏宠琛庤¨琛々瑙撹▌瑷囪挡杩ｈ俊杩繝閮遍偨閭块儠閮呴偩閮囬儖閮堥嚁閲撻檾闄忛檻闄撻檴闄庡€炲€呭€囧€撳€㈠€板€涗康淇村€冲€峰€慷淇峰€楀€滃€犲€у€靛€€卞€庡厷鍐斿啌鍑婂噭鍑呭噲鍑庡墶鍓氬墥鍓炲墴鍓曞墷鍕嶅寧鍘炲敠鍝㈠敆鍞掑摟鍝冲摛鍞氬摽鍞勫攬鍝攽鍞呭摫锟�".split("");
	for(a = 0; a != t[208].length; ++a)
		if(t[208][a].charCodeAt(0) !== 65533) { r[t[208][a]] = 53248 + a;
			e[53248 + a] = t[208][a] }
	t[209] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍞婂摶鍝峰摳鍝犲攷鍞冨攱鍦佸渹鍩屽牪鍩曞煉鍨哄焼鍨藉灱鍨稿灦鍨垮焽鍩愬灩鍩佸濂婂濞栧ō濞〞濞忓濞婂濞冲瀹у瀹皟灞栧睌宄晨宄潮宄峰磤宄瑰俯甯ㄥ酣搴邯搴汲寮板涧鎭濇仛鎭э拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥亖鎮㈡倛鎮€鎮掓倎鎮濇們鎮曟倹鎮楁倗鎮滄値鎴欐墕鎷叉寪鎹栨尙鎹勬崊鎸舵崈鎻ゆ尮鎹嬫崐鎸兼尒鎹佹尨鎹樻崝鎹欐尛鎹囨尦鎹氭崙鎸告崡鎹€鎹堟晩鏁嗘梿鏃冩梽鏃傛檴鏅熸檱鏅戞湌鏈撴牊鏍氭鏍叉牫鏍绘妗忔爾鏍辨牅鏍垫牜鏍牤妗庢鏍存牆鏍掓爺鏍︽牗鏍鏍烘牓鏍犳娆娆辨姝倐娈堟姣わ拷".split("");
	for(a = 0; a != t[209].length; ++a)
		if(t[209][a].charCodeAt(0) !== 65533) { r[t[209][a]] = 53504 + a;
			e[53504 + a] = t[209][a] }
	t[210] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷姣ㄦ姣㈡姘ユ岛娴ｆ丹娴舵磵娴℃稈娴樻耽娴弹娑戞秿娣悼娑嗘禐娴ф禒娑楁蛋娴兼禑娑傛稑娲胆娑嬫稻娑€娑勬礀娑冩祷娴芥档娑愮儨鐑撶儜鐑濈儖缂圭儮鐑楃儝鐑炵儬鐑旂儘鐑呯儐鐑囩儦鐑庣儭鐗傜壐锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐗风壎鐚€鐙虹嫶鐙剧嫸鐙崇嫽鐚佺彄鐝欑彞鐝栫幖鐝х彛鐝╃彍鐝掔彌鐝旂彎鐝氱彈鐝樼彣鐡炵摕鐡寸摰鐢＄暃鐣熺柊鐥佺柣鐥勭梹鐤跨柖鐤虹殜鐩夌湞鐪涚湊鐪撶湌鐪ｇ湋鐪曠湙鐪氱湤鐪х牐鐮牏鐮电牤鐮ㄧ牣鐮牎鐮╃牫鐮牨绁旂绁忕绁撶绁戠Й绉绉Л绉绉炵绐嗙獕绐呯獘绐岀獖绐囩珮绗愶拷".split("");
	for(a = 0; a != t[210].length; ++a)
		if(t[210][a].charCodeAt(0) !== 65533) { r[t[210][a]] = 53760 + a;
			e[53760 + a] = t[210][a] }
	t[211] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绗勭瑩绗呯瑥绗堢瑠绗庣瑝绗掔矂绮戠矈绮岀矆绮嶇矃绱炵礉绱戠磶绱樼礀绱撶礋绱掔磸绱岀綔缃＄綖缃犵綕缃涚緰缇掔績缈傜縺鑰栬€捐€硅兒鑳茶児鑳佃剚鑳昏剙鑸佽埊鑸ヨ尦鑼崉鑼欒崙鑼ヨ崠鑼胯崄鑼﹁寽鑼拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借崅鑽庤寷鑼寛鑼艰崓鑼栬尋鑼犺尫鑼尒鑽囪崊鑽岃崜鑼炶尙鑽嬭導鑽堣檽铏掕殺铓ㄨ殩铓嶈殤铓炶殗铓楄殕铓嬭殮铓呰殽铓欒殹铓ц殨铓樿殠铓濊殣铓旇琛勮…琛佃《琛茶琛辫】琛琛捐〈琛艰⊕璞囪睏璞昏菠璨ｈ刀璧歌兜瓒疯抖杌戣粨杩捐康閫傝靠杩婚€勮考杩堕儢閮犻儥閮氶儯閮熼儱閮橀儧閮楅儨閮ら厫锟�".split("");
	for(a = 0; a != t[211].length; ++a)
		if(t[211][a].charCodeAt(0) !== 65533) { r[t[211][a]] = 54016 + a;
			e[54016 + a] = t[211][a] }
	t[212] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閰庨厪閲曢嚔閲氶櫆闄熼毤椋ｉ珶楝箍鍋板仾鍋″仦鍋犲亾鍋嬪仢鍋插亪鍋嶅亖鍋涘亰鍋㈠€曞亝鍋熷仼鍋仯鍋ゅ亞鍋€鍋伋鍋楀亼鍑愬壂鍓壃鍓嫋鍕撳尛鍘滃暤鍟跺敿鍟嶅晲鍞村敧鍟戝暍鍞跺數鍞板晵鍟咃拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉攲鍞插暐鍟庡敼鍟堝敪鍞诲晙鍟嬪湂鍦囧熁鍫斿煝鍩跺煖鍩村爛鍩熃鍫堝煾鍫嬪煶鍩忓爣鍩煟鍩插煡鍩煛鍫庡熂鍫愬煣鍫佸爩鍩卞煩鍩板爫鍫勫濠犲濠曞┃濠炲ǜ濞靛┉濠愬濠ュ┈濠撳─濠楀﹥濠濆濠勫濠堝獛濞惧濞瑰濠板┅濠囧濠栧﹤濠滃瀛瘉瀵€灞欏礊宕嬪礉宕氬礌宕屽川宕嶅处宕ュ磸锟�".split("");
	for(a = 0; a != t[212].length; ++a)
		if(t[212][a].charCodeAt(0) !== 65533) { r[t[212][a]] = 54272 + a;
			e[54272 + a] = t[212][a] }
	t[213] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷宕板磼宕ｅ礋宕妇甯村罕搴村汗搴插撼寮跺几寰涘緰寰熸倞鎮愭倖鎮炬偘鎮烘儞鎯旀儚鎯ゆ儥鎯濇儓鎮辨儧鎮锋儕鎮挎儍鎯嶆儉鎸叉崶鎺婃巶鎹芥幗鎺炴幁鎺濇帡鎺帋鎹巼鎺愭嵁鎺嵉鎺滄嵀鎺嵓鎺ゆ尰鎺燂拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥嵏鎺呮巵鎺戞帊鎹版晸鏃嶆櫏鏅℃櫅鏅欐櫆鏅㈡湗妗规姊愭妗‘姊妤栨’姊ｆ姊╂〉妗存⒉姊忔》姊掓〖妗〔姊妗辨【姊涙姊嬫姊夋ⅳ妗告』姊戞姊婃〗娆舵娆锋娈戞畯娈嶆畮娈屾蔼娣€娑洞娑虫勾娑珐娣㈡斗娣舵窋娓€娣堟窢娣熸窎娑炬伐娣滄窛娣涙反娣婃督娣钒娑烘窌娣傛窂娣夛拷".split("");
	for(a = 0; a != t[213].length; ++a)
		if(t[213][a].charCodeAt(0) !== 65533) { r[t[213][a]] = 54528 + a;
			e[54528 + a] = t[213][a] }
	t[214] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷娣愭凡娣撴方娣楁穽娣ｆ痘鐑虹剭鐑风剹鐑寸剬鐑扮剟鐑崇剱鐑肩兛鐒嗙創鐒€鐑哥兌鐒嬬剛鐒庣壘鐗荤壖鐗跨対鐚楃寚鐚戠寴鐚婄寛鐙跨審鐚炵巿鐝剁徃鐝电悇鐞佺徑鐞囩悁鐝虹徏鐝跨悓鐞嬬彺鐞堢暏鐣ｇ棊鐥掔棌锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐥嬬棇鐥戠棎鐨忕殙鐩撶湽鐪湱鐪辩湶鐪寸湷鐪界湧鐪荤湹纭堢纭夌纭婄鐮︾纭愮イ绁хォ绁ィ绁ァ绂荤Ш绉哥Ф绉风獜绐旂獝绗电瓏绗寸绗扮绗ょ绗樼绗濈绗绗绗哥瑲绗ｇ矓绮樼矕绮ｇ吹绱界锤绱剁春绲呯船绱╃祦绲囩淳绱跨祳绱荤川缃ｇ緯缇滅緷缇涚繆缈嬬繊缈愮繎缈囩繌缈夎€燂拷".split("");
	for(a = 0; a != t[214].length; ++a)
		if(t[214][a].charCodeAt(0) !== 65533) { r[t[214][a]] = 54784 + a;
			e[54784 + a] = t[214][a] }
	t[215] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑰炶€涜亣鑱冭亪鑴樿劌鑴欒剾鑴劅鑴劄鑴¤剷鑴ц劃鑴㈣垜鑸歌埑鑸鸿埓鑸茶壌鑾愯帲鑾ㄨ帊鑽鸿嵆鑾よ嵈鑾忚巵鑾曡帣鑽佃帞鑾╄嵔鑾冭帉鑾濊帥鑾帇鑽捐帴鑾巿鑾楄幇鑽胯帵鑾囪幃鑽惰帤铏欒櫀铓胯毞锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷铔傝泚铔呰毢铓拌泩铓硅毘铓歌泴铓磋毣铓艰泝铓借毦琛掕琚曡ⅷ琚㈣ⅹ琚氳琚¤琚樿ⅶ琚欒琚楄ⅳ琚琚撹瑕傝瑙欒瑷拌ě瑷璋硅盎璞滆睗璞借播璧借祷璧硅都璺傝豆瓒胯穪杌樿粸杌濊粶杌楄粻杌￠€ら€嬮€戦€滈€岄€￠儻閮儼閮撮儾閮抽償閮儸閮╅厲閰橀厷閰撻厱閲嚧閲遍嚦閲搁嚖閲归嚜锟�".split("");
	for(a = 0; a != t[215].length; ++a)
		if(t[215][a].charCodeAt(0) !== 65533) { r[t[215][a]] = 55040 + a;
			e[55040 + a] = t[215][a] }
	t[216] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閲嚪閲ㄩ嚠闀洪枂闁堥櫦闄櫕闄遍櫙闅块潽闋勯％棣楀倹鍌曞倲鍌炲倠鍌ｅ們鍌屽値鍌濆仺鍌滃倰鍌傚倗鍏熷嚁鍖掑寫鍘ゅ帶鍠戝枿鍠ュ柇鍟峰檯鍠㈠枔鍠堝枏鍠靛杹鍠ｅ枓鍠ゅ暯鍠屽枽鍟垮枙鍠″枎鍦屽牘鍫凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉牂鍫炲牕鍫ｅ牗鍩靛鍫ュ牅鍫涘牫鍫垮牰鍫牴鍫稿牠鍫牷濂″濯斿獰濠哄濯炲└濯﹀┘濯ュ濯曞濞峰獎濯婂獥濯冨獘濯╁┗濠藉獙濯滃獜濯撳獫瀵瘝瀵嬪瘮瀵戝瘖瀵庡皩灏板捶宓冨但宓佸祴宕垮吹宓戝祹宓曞闯宕哄祾宕藉幢宓欏祩宕瑰祲宕稿醇宕插炊宓€宓呭箘骞佸綐寰﹀茎寰儔鎮规儗鎯㈡儙鎯勬剶锟�".split("");
	for(a = 0; a != t[216].length; ++a)
		if(t[216][a].charCodeAt(0) !== 65533) { r[t[216][a]] = 55296 + a;
			e[55296 + a] = t[216][a] }
	t[217] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鎯叉剨鎰栨剠鎯垫創鎯告兗鎯炬儊鎰冩剺鎰濇剱鎯挎剟鎰嬫墛鎺旀幈鎺版弾鎻ユ彣鎻弮鎾濇彸鎻婃彔鎻舵彆鎻叉彽鎽℃彑鎺炬彎鎻滄弰鎻樻彄鎻傛弴鎻屾弸鎻堟彴鎻楁彊鏀叉暓鏁暏鏁滄暔鏁ユ枌鏂濇枮鏂棎鏃掞拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥櫦鏅櫥鏆€鏅辨櫣鏅櫜鏈佹妫撴妫滄お妫＊妫辨妫栨７妫￥妫舵妞愭３妫℃妫屾妤版⒋妞戞／妫嗘妫告妫芥＜妫ㄦ妞婃妫庢妫濇妫︽４妫戞妫旀）妞曟ぅ妫囨娆绘娆兼當娈楁畽娈曟姣版姣虫鞍娣兼箚婀囨笩婀夋簣娓兼附婀呮耿娓缚婀佹節婀虫笢娓虫箣婀€婀戞富娓冩府婀烇拷".split("");
	for(a = 0; a != t[217].length; ++a)
		if(t[217][a].charCodeAt(0) !== 65533) { r[t[217][a]] = 55552 + a;
			e[55552 + a] = t[217][a] }
	t[218] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷婀ㄦ箿婀℃副娓ㄦ範婀辨公娓规涪娓版箵婀ユ抚婀告工婀锋箷婀规箳婀︽傅娓舵箽鐒犵劄鐒兓鐒劚鐒ｇ劌鐒㈢劜鐒熺劏鐒虹剾鐗嬬墯鐘堢妷鐘嗙妳鐘嬬寬鐚嬬尠鐚㈢尡鐚崇導鐚茬尛鐚︾專鐚电寣鐞惉鐞扮惈鐞栵拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界悮鐞＄惌鐞辩悿鐞ｇ悵鐞╃悹鐞茬摶鐢暞鐣棫鐥氱棥鐥︾棟鐥熺棨鐥楃殨鐨掔洑鐫嗙潎鐫勭潔鐫呯潑鐫庣潒鐫岀煘鐭纭ょˉ纭滅…纭辩—纭“纭╃〃纭炵、绁寸コ绁茬グ绋傜▕绋冪▽绋勭獧绔︾绛婄绛勭瓐绛岀瓗绛€绛樼瓍绮㈢矠绮ㄧ病绲樼弹绲ｇ祿绲栫掸绲祻绲禍绲祾绲旂旦绲戠禑绲庣季缂跨渐锟�".split("");
	for(a = 0; a != t[218].length; ++a)
		if(t[218][a].charCodeAt(0) !== 65533) { r[t[218][a]] = 55808 + a;
			e[55808 + a] = t[218][a] }
	t[219] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷缃︾劲缇犵尽缈楄亼鑱忚亹鑳捐償鑵冭厞鑵掕厪鑵囪劷鑵嶈労鑷﹁嚠鑷疯嚫鑷硅垊鑸艰埥鑸胯壍鑼昏弿鑿硅悾鑿€鑿ㄨ悞鑿ц彜鑿艰彾钀愯弳鑿堣彨鑿ｈ幙钀佽彎鑿ヨ彉鑿胯彙鑿嬭弾鑿栬彽鑿夎悏钀忚彏钀戣悊鑿傝彸锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑿曡徍鑿囪彂鑿悡鑿冭彫鑿弰鑿昏彈鑿㈣悰鑿涜従铔樿洟铔﹁洆铔ｈ洑铔洕铔洔铔洨铔楄洦铔戣琛栬琚鸿琚硅⒏瑁€琚捐⒍琚艰⒎琚借⒉瑜佽瑕曡瑕楄瑙氳瑭庤瑷硅瑭€瑭楄瑭勮﹨瑭掕﹫瑭戣瑭岃璞熻瞾璨€璨鸿簿璨拌补璨佃秳瓒€瓒夎窐璺撹穽璺囪窎璺滆窂璺曡窓璺堣窏璺呰化杌疯缓锟�".split("");
	for(a = 0; a != t[219].length; ++a)
		if(t[219][a].charCodeAt(0) !== 65533) { r[t[219][a]] = 56064 + a;
			e[56064 + a] = t[219][a] }
	t[220] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷杌硅沪杌互杌佃户杌ㄨ欢杌槐杌淮杌╅€€撮€剢閯剟閮块兗閯堥児閮婚剚閯€閯囬剠閯冮叀閰ら厽閰㈤厾閳侀垔閳ラ垉閳氶垿閳忛垖閳€閳掗嚳閲介垎閳勯埀閳傞垳閳ら垯閳楅垍閳栭暬闁嶉枌闁愰殗闄鹃殘锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷闅夐殐闅€闆傞泩闆冮洷闆伴潿闈伴澁闋囬ⅸ椋肠榛逛簝浜勪憾鍌藉偪鍍嗗偖鍍勫儕鍌村儓鍍傚偘鍍佸偤鍌卞儖鍍夊偠鍌稿嚄鍓哄壐鍓诲壖鍡冨棝鍡屽棎鍡嬪棅鍡濆梹鍡斿梽鍡╁柨鍡掑枍鍡忓棔鍡㈠棖鍡堝棽鍡嶅棛鍡傚湐濉撳〃濉ゅ濉嶅濉濉庡濉欏ˉ濉涘牻濉ｅ”澹煎珖瀚勫珛濯哄濯卞濯板瀚堝瀚嗭拷".split("");
	for(a = 0; a != t[220].length; ++a)
		if(t[220][a].charCodeAt(0) !== 65533) { r[t[220][a]] = 56320 + a;
			e[56320 + a] = t[220][a] }
	t[221] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷濯峰珋瀚婂濯跺珝濯瑰獝瀵栧瘶瀵欏盁灏冲当宓ｅ祳宓ュ挡宓禐宓ㄥ掸宓㈠钒骞忓箮骞婂箥骞嬪粎寤屽粏寤嬪粐褰€寰经鎯锋厜鎱婃劔鎱呮劧鎰叉劗鎱嗘劘鎱忔劑鎱€鎴犻叏鎴ｆ垾鎴ゆ弲鎻辨彨鎼愭悞鎼夋悹鎼わ拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥惓鎽冩悷鎼曟悩鎼规惙鎼㈡悾鎼屾惁鎼版惃鎽佹惖鎼悐鎼氭憖鎼ユ惂鎼嬫彠鎼涙惍鎼℃悗鏁枓鏃撴殕鏆屾殨鏆愭殝鏆婃殭鏆旀櫢鏈犳ウ妤熸じ妤庢ア妤辨た妤呮オ妞规妤楁妤烘妤夋さ妤こ妞芥ゥ妫版ジ妞存ォ妤€妤妤舵妤佹ゴ妤屾せ妤嬫し妤滄妤戞げ妤掓く妤绘ぜ姝嗘瓍姝冩瓊姝堟瓉娈涳◢姣绘锟�".split("");
	for(a = 0; a != t[221].length; ++a)
		if(t[221][a].charCodeAt(0) !== 65533) { r[t[221][a]] = 56576 + a;
			e[56576 + a] = t[221][a] }
	t[222] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷姣规姣告簺婊栨粓婧忔粈婧熸簱婧旀籂婧辨汗婊嗘粧婧芥粊婧炴粔婧锋喊婊嶆害婊忔翰婧炬粌婊滄粯婧欐簰婧庢簫婧ゆ骸婧挎撼婊愭粖婧楁寒婧ｇ厙鐓旂厭鐓ｇ厾鐓佺厺鐓㈢叢鐓哥叒鐓＄厒鐓樼厓鐓嬬叞鐓熺厫鐓擄拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界厔鐓嶇厷鐗忕妽鐘岀姂鐘愮妿鐚肩崅鐚荤尯鐛€鐛婄崏鐟勭憡鐟嬬憭鐟戠憲鐟€鐟忕憪鐟庣憘鐟嗙憤鐟旂摗鐡跨摼鐡界敐鐣圭暦姒冪棷鐦忕槂鐥风椌鐥肩椆鐥哥槓鐥荤椂鐥椀鐥界殭鐨电洕鐫曠潫鐫犵潚鐫栫潥鐫╃潷鐫旂潤鐫煚纰囩纰旂纰勭纰呯纰＄纭圭纰€纰栫』绁肩绁界ス绋戠绋欑⊕绋楃〞绋㈢〒锟�".split("");
	for(a = 0; a != t[222].length; ++a)
		if(t[222][a].charCodeAt(0) !== 65533) { r[t[222][a]] = 56832 + a;
			e[56832 + a] = t[222][a] }
	t[223] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绋涚◥绐ｇ绐炵绛︾绛绛╃绛ョ绛辩绛＄绛剁绮茬泊绮秷缍嗙秬缍嶇悼缍呯岛缍庣祷缍冪导缍岀稊缍勭到缍掔江缃涧缃ㄧ浆缇︾茎缇х繘缈滆€¤叅鑵犺叿鑵滆叐鑵涜參鑵叉湣鑵炶叾鑵ц叝锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑵勮叀鑸濊墘鑹勮墍鑹傝墔钃辫惪钁栬懚钁硅拸钂嶈懃钁戣憖钂嗚懅钀拌憤钁借憵钁欒懘钁宠憹钄囪憺钀疯惡钀磋懞钁冭懜钀茶憛钀╄彊钁嬭惎钁傝惌钁熻懓钀硅憥钁岃憭钁搮钂庤惢钁囪惗钀宠懆钁捐憚钀憼钁旇懏钁愯湅铚勮浄铚岃浐铔栬浀铦嶈浉铚庤湁铚佽浂铚嶈渽瑁栬瑁嶈瑁炶瑁氳瑁愯瑕涜瑙ヨГ锟�".split("");
	for(a = 0; a != t[223].length; ++a)
		if(t[223][a].charCodeAt(0) !== 65533) { r[t[223][a]] = 57088 + a;
			e[57088 + a] = t[223][a] }
	t[224] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷瑙¤瑙㈣瑙﹁┒瑾嗚┛瑭¤瑭疯獋瑾勮┑瑾冭獊瑭磋┖璋艰眿璞婅饱璞よ宝璨嗚矂璨呰硨璧ㄨ旦瓒戣秾瓒庤稄瓒嶈稉瓒旇稅瓒掕钒璺犺番璺辫樊璺愯珐璺ｈ发璺ц凡璺反杓嗚豢杓佽紑杓呰紘杓堣紓杓嬮亽閫匡拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介亜閬夐€介剱閯嶉剰閯戦剸閯旈剫閯庨叜閰増閴掗埌閳洪墻閳抽墺閴為妰閳墛閴嗛壄閴墢閴犻墽閴埗閴￠壈閳遍墧閴ｉ墣閴查墡閴撻墝閴栭埐闁熼枩闁為枦闅掗殦闅戦殫闆庨浐闆介浉闆甸澇闈烽澑闈查爮闋嶉爭棰６椋归Ο棣查Π棣甸楠瓫槌抄槌ч簚榛藉儲鍍斿儣鍍ㄥ兂鍍涘儶鍍濆儰鍍撳儸鍍板儻鍍ｅ儬锟�".split("");
	for(a = 0; a != t[224].length; ++a)
		if(t[224][a].charCodeAt(0) !== 65533) { r[t[224][a]] = 57344 + a;
			e[57344 + a] = t[224][a] }
	t[225] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍑樺妧鍔佸嫨鍕尠鍘槯鍢曞槍鍢掑椉鍢忓槣鍢佸槗鍢傚椇鍢濆槃鍡垮椆澧夊〖澧愬澧嗗濉垮〈澧嬪『澧囧澧庡《澧傚濉诲澧忓＞濂珳瀚瀚曞瀚氬瀚瀚㈠珷瀚涘瀚炲珴瀚欏瀚熷瀵狅拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉灞ｅ秱宥€宓藉秵宓哄秮宓峰秺宥夊秷宓惧导宥嶅倒宓垮箻骞欏箵寤樺粦寤楀粠寤滃粫寤欏粧寤斿絼褰冨蒋寰舵劕鎰ㄦ厑鎱炴叡鎱虫厭鎱撴叢鎱唨鎱存厰鎱烘厸鎱ユ劵鎱叀鎱栨埄鎴ф埆鎼憤鎽涙憹鎽存懚鎽叉懗鎽芥懙鎽︽挦鎽庢拏鎽炴憸鎽嬫憮鎽犳憪鎽挎惪鎽懌鎽欐懃鎽锋暢鏂犳殹鏆犳殶鏈呮渼鏈㈡Ρ姒舵锟�".split("");
	for(a = 0; a != t[225].length; ++a)
		if(t[225][a].charCodeAt(0) !== 65533) { r[t[225][a]] = 57600 + a;
			e[57600 + a] = t[225][a] }
	t[226] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷姒犳姒栨Π姒姒戞姒庢Η姒嶆Ι姒炬Ο姒挎姒芥Δ妲旀妲婃妲忔Τ姒撴Κ姒℃妲欐姒愭姒垫Ε妲嗘瓓姝嶆瓔娈炴疅娈犳瘍姣勬婊庢坏婊辨純婕ユ桓婕锋换婕級娼庢紮婕氭姬婕樻蓟婕掓画婕婏拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥级娼虫还婊辑娼€婕版技婕垫猾婕囨紟娼冩紖婊芥欢婕规紲婊兼己婕熸紞婕炴紙婕＄唶鐔愮唹鐔€鐔呯唫鐔忕吇鐔嗙唩鐔楃墑鐗撶姉鐘曠姄鐛冪崓鐛戠崒鐟㈢懗鐟辩懙鐟茬懅鐟攢鐢傜攦鐣界枑鐦栫槇鐦岀槙鐦戠槉鐦旂毟鐬佺澕鐬呯瀭鐫瀫鐫澗鐬冪⒉纰⒋纰ⅷ纭剧纰炵ⅴ纰犵纰㈢ⅳ绂樼绂嬬绂曠绂擄拷".split("");
	for(a = 0; a != t[226].length; ++a)
		if(t[226][a].charCodeAt(0) !== 65533) { r[t[226][a]] = 57856 + a;
			e[57856 + a] = t[226][a] }
	t[227] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绂楃绂掔绋绋扮ǒ绋ㄧé绐ㄧ绐绠堢疁绠婄畱绠愮畺绠嶇畬绠涚畮绠呯畼鍔勭畽绠ょ畟绮荤部绮肩埠缍х斗绶傜叮缍穪绶€绶呯稘绶庣穭绶嗙穻绶岀动缍圭稏缍肩稛缍︾懂缍╃丁绶夌匠缈㈢浚缈ョ繛锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑰よ仢鑱滆唹鑶嗚唭鑶囪啀鑶岃唻鑸曡挆钂よ挕钂熻捄钃庤搨钂挳钂捁钂磋搧钃嶈挭钂氳挶钃愯挐钂ц捇钂㈣挃钃囪搶钂涜挬钂挩钃栬挊钂惰搹钂犺摋钃旇搾钃涜挵钂戣櫋铚宠湥铚ㄨ潾铦€铚湠铚¤湙铚涜潈铚潄铚捐潌铚犺湶铚湱铚艰湌铚鸿湵铚佃潅铚﹁湩铚歌湦铚氳湴铚戣７瑁ц１瑁茶：瑁捐．瑁艰６瑁伙拷".split("");
	for(a = 0; a != t[227].length; ++a)
		if(t[227][a].charCodeAt(0) !== 65533) { r[t[227][a]] = 58112 + a;
			e[58112 + a] = t[227][a] }
	t[228] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷瑁拌，瑁瑕¤瑕炶З瑙Ж瑾獧瑾嬭獟瑾忚獤璋借报璞╄硶璩忚硹瓒栬笁韪傝房韪嶈方韪婅竷韪囪竼韪呰肪韪€韪勮紣杓戣紟杓嶉劊閯滈劆閯㈤劅閯濋剼閯ら劇閯涢吅閰查吂閰抽姤閵ら壎閵涢壓閵犻姅閵妽锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閵﹂姎閵壒閵楅壙閵ｉ嫯閵庨妭閵曢姠閴介妶閵￠妸閵嗛妼閵欓姧閴鹃妵閵╅姖閵嬮埈闅為殹闆块潣闈介澓闈鹃瀮闉€闉傞澔闉勯瀬闈块煄闊嶉爾棰椁傞椁囬棣滈棣归棣洪棣介楠遍楂ч楝块瓲榄￠瓱槌遍巢槌甸骇鍍垮剝鍎板兏鍎嗗剣鍍跺兙鍎嬪剬鍍藉剨鍔嬪妼鍕卞嫰鍣堝檪鍣屽樀鍣佸檴鍣夊檰鍣橈拷".split("");
	for(a = 0; a != t[228].length; ++a)
		if(t[228][a].charCodeAt(0) !== 65533) { r[t[228][a]] = 58368 + a;
			e[58368 + a] = t[228][a] }
	t[229] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍣氬檧鍢冲樈鍢樉鍢稿槳鍢哄湚澧澧卞澧ｅ澧ⅴ澧″？瀚垮瀚藉瀚跺瑑瀚稿瑐瀚瑰瑏瀣囧瑓瀣忓抱宥欏稐宥熷稈宥㈠稉宥曞稜宥滃丁宥氬稙骞╁節骞犲箿绶冲粵寤炲弧褰夊静鎲嬫唭鎱规啽鎲版啟鎲夛拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥啗鎲撴啹鎲啛鎲掓啰鎲℃啀鎱︽喅鎴懏鎽版挅鎾犳拝鎾楁挏鎾忔拫鎾婃拰鎾ｆ挓鎽ㄦ挶鎾樻暥鏁烘暪鏁绘柌鏂虫毜鏆版毄鏆叉毞鏆毌妯€妯嗘妲ユЦ妯曟П妲ゆ妲挎К妲㈡妯濇Ь妯фР妲〝妲锋Ё姗€妯堟Е妲绘◢妲兼Й妯夋▌妯樻ē妯忔Ф妯︽▏妲存姝戞娈ｆ娈︽皝姘€姣挎皞娼佹鸡娼炬緡婵嗘緬锟�".split("");
	for(a = 0; a != t[229].length; ++a)
		if(t[229][a].charCodeAt(0) !== 65533) { r[t[229][a]] = 58624 + a;
			e[58624 + a] = t[229][a] }
	t[230] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷婢嶆緣婢屾舰娼忔緟娼氭緰娼舵浆婢傛綍娼叉綊娼愭綏婢旀緭娼濇紑娼℃将娼芥涧婢愭綋婢嬫僵娼挎緯娼ｆ椒娼交鐔茬啹鐔涚啺鐔犵啔鐔╃喌鐔濈啣鐔炵啢鐔＄啰鐔滅啩鐔崇姌鐘氱崢鐛掔崬鐛熺崰鐛濈崨鐛＄崥鐛欙拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界崲鐠囩拤鐠婄拞鐠佺懡鐠呯拡鐟肩懝鐢堢攪鐣剧槬鐦炵槞鐦濈槣鐦ｇ槡鐦ㄧ槢鐨滅殱鐨炵殯鐬嶇瀼鐬夌瀳纾嶇⒒纾忕纾戠纾旂纾冪纾夌绂＄绂滅Β绂涙绋圭绐寸绠风瘚绠剧绡庣绠圭瘖绠电硡绯堢硨绯嬬贩绶涚藩绶х窏绶＄竷绶虹乏绶剁繁绶扮樊绶熺蕉缇景缇凯缈开缈喀缈ㄨ仱鑱ц啠鑶燂拷".split("");
	for(a = 0; a != t[230].length; ++a)
		if(t[230][a].charCodeAt(0) !== 65533) { r[t[230][a]] = 58880 + a;
			e[58880 + a] = t[230][a] }
	t[231] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鑶炶啎鑶㈣啓鑶楄垨鑹忚墦鑹掕墣鑹庤墤钄よ敾钄忚攢钄╄攷钄夎攳钄熻攰钄ц敎钃昏敨钃鸿攬钄岃摯钄摬钄曡摲钃摮钃艰敀钃摡钄栬摼钄ㄨ敐钄攤钃借敒钃惰敱钄﹁摟钃ㄨ摪钃摴钄樿敔钄拌攱钄欒敮铏拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借潠铦ｈ潳铦疯煛铦宠潣铦旇潧铦掕潯铦氳潙铦炶澀铦潗铦庤潫铦濊澂铦澓铦潨铦ヨ潖铦昏澋铦㈣潷铦╄瑜呰瑜旇瑜楄瑜欒瑜栬瑜庤瑕㈣Δ瑕ｈЛ瑙拌К璜忚珕瑾歌珦璜戣珨璜曡璜楄璜€璜呰珮璜冭瑾借珯璋捐睄璨忚偿璩熻硻璩ㄨ硽璩濊厂瓒犺稖瓒¤稕韪犺福韪ヨ袱韪笗韪涜笘韪戣笝韪﹁抚锟�".split("");
	for(a = 0; a != t[231].length; ++a)
		if(t[231][a].charCodeAt(0) !== 65533) { r[t[231][a]] = 59136 + a;
			e[59136 + a] = t[231][a] }
	t[232] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷韪旇笒韪樿笓韪滆笚韪氳棘杓よ紭杓氳紶杓ｈ紪杓楅伋閬伴伅閬ч伀閯劔閯╅劒閯查劍閯唴閱嗛唺閱侀唫閱勯唨閶愰媰閶勯媭閶欓姸閶忛嫳閶熼嫎閶╅嫍閶濋媽閶媯閶ㄩ媻閶堥嫀閶﹂媿閶曢媺閶犻嫗閶ч嫅閶擄拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介姷閶￠媶閵撮暭闁柅闁柊闅ら殺闆撻渽闇堥渹闈氶瀶闉庨瀳闊愰煆闋為牆闋﹂牘闋ㄩ牋闋涢牕棰查椋洪椁旈椁楅椐滈椐忛椐旈椐夐椐橀椐楅楠抽楂楂查榄嗛瓋榄ч榄遍榄堕榄伴榄ら槌奸澈槌介晨槌烽磭榇€槌归郴榇堥磪榇勯簝榛撻紡榧愬劀鍎撳剹鍎氬剳鍑炲尨鍙″櫚鍣犲櫘锟�".split("");
	for(a = 0; a != t[232].length; ++a)
		if(t[232][a].charCodeAt(0) !== 65533) { r[t[232][a]] = 59392 + a;
			e[59392 + a] = t[232][a] }
	t[233] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍣冲櫐鍣ｅ櫗鍣插櫈鍣峰湝鍦涘澧藉澧垮⒑澹傚⒓澹嗗瑮瀣欏瑳瀣″瑪瀣撳瑦瀣栧瀣氬瑺瀣炲宥侗宥╁锭宥靛栋宥丢宥ㄥ恫宥动宥村恭骞ㄥ功骞哗寤у沪寤ㄥ互褰嬪炯鎲濇啫鎲栨噮鎲存噯鎳佹噷鎲猴拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥喛鎲告唽鎿楁摉鎿愭搹鎿夋捊鎾夋搩鎿涙摮鎿欐敵鏁挎暭鏂㈡泩鏆炬泙鏇婃泲鏇忔毥鏆绘毢鏇屾湥妯存│姗夋┃妯叉┄妯炬姗┒姗涙妯ㄦ妯绘姗佹┆姗ゆ姗忔姗┅姗犳姗炴〇姗曟姗庢﹩姝曟瓟姝栨娈姣堟瘒姘勬皟姘嗘经婵嬫荆婵囨炯婵庢繄娼炴縿婢芥緸婵婃鲸鐎勬茎婢竞婢惊婵忔究婢革拷".split("");
	for(a = 0; a != t[233].length; ++a)
		if(t[233][a].charCodeAt(0) !== 65533) { r[t[233][a]] = 59648 + a;
			e[59648 + a] = t[233][a] }
	t[234] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷婢㈡繅婢繊婢静婢扮噮鐕傜喛鐔哥嚃鐕€鐕佺噵鐕旂噴鐕囩噺鐔界嚇鐔肩噯鐕氱嚊鐘濈姙鐛╃崷鐛х崿鐛ョ崼鐛懣鐠氱挔鐠旂拻鐠曠挕鐢嬬杸鐦槶鐦辩樈鐦崇樇鐦电槻鐦扮毣鐩︾灇鐬濈灐鐬滅灈鐬㈢灒鐬曠灆锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐬楃纾╃％纾纾ｇ纾＄＂纾纾犵Δ绌勭﹫绌囩绐哥绐辩绡炵绡х瘽绡曠绡氱绡圭瘮绡绡滅绡樼療绯掔硵绯楃硱绯戠笒绺＄笚绺岀笩绺犵笓绺庣笢绺曠笟绺㈢笅绺忕笘绺嶇笖绺ョ袱缃冪交缃肩胶缇辩刊鑰€╄伂鑶辫啨鑶喒鑶佃啱鑶拌啲鑶磋啿鑶疯啩鑷茶墪鑹栬墬钑栬晠钑晬钑撹暋钑橈拷".split("");
	for(a = 0; a != t[234].length; ++a)
		if(t[234][a].charCodeAt(0) !== 65533) { r[t[234][a]] = 59904 + a;
			e[59904 + a] = t[234][a] }
	t[235] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷钑€钑嗚暏钑佽暍钑勮晳钑囪暎钄捐暃钑辫晭钑暤钑曡暓钑犺枌钑﹁暆钑旇暐钑櫍铏ヨ櫎铻涜瀼铻楄灀铻掕瀳铻佽灃铻樿澒铻囪灒铻呰瀽铻戣灊铻勮灁铻滆灇铻夎瑜﹁ぐ瑜ぎ瑜цけ瑜㈣ぉ瑜ｈく瑜瑙辫珷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷璜㈣璜磋璜濊瑪璜よ珶璜拌珗璜炶璜ㄨ璜璨戣矑璨愯车璩潮璩拌吵璧诞瓒ヨ锭韪宠妇韪歌箑韫呰付韪艰附韫佽赴韪胯航杓惰籍杓佃疾杓硅挤杓撮伓閬归伝閭嗛兒閯抽劦閯堕啌閱愰啈閱嶉啅閷ч尀閷堥専閷嗛審閸洪尭閷奸寷閷ｉ寬閷侀崋閷寧閷嶉媼閷濋嫼閷ラ寭閶归嫹閷撮寕閷ら嬁閷╅尮閷甸尓閷旈寣锟�".split("");
	for(a = 0; a != t[235].length; ++a)
		if(t[235][a].charCodeAt(0) !== 65533) { r[t[235][a]] = 60160 + a;
			e[60160 + a] = t[235][a] }
	t[236] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閷嬮嬀閷夐寑閶婚寲闁奸棈闁鹃柟闁洪柖闁块柕闁介毄闆旈湅闇掗湊闉欓灄闉旈煱闊搁牭闋牪椁ら椁чぉ棣為М椐Д椐らО椐ｉИ椐╅Ё楠归楠撮楂堕楂归楝抽畝楫呴畤榄奸榄婚畟楫撻畳楫愰楫曪拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介楫堥触榇楅礌榇為磾榇╅礉榇橀储榇愰礄榇熼簣楹嗛簢楹涵榛曢粬榛洪紥榧藉劍鍎ュ劉鍎ゅ劆鍎╁嫶鍤撳殞鍤嶅殕鍤勫殐鍣惧殏鍣垮殎澹栧澹忓瀣瀣插瀣瀣﹀瀣瀵卞宥峰宫骞揪寰绘噧鎲垫喖鎳ф嚑鎳ユ嚖鎳ㄦ嚍鎿摡鎿ｆ摣鎿ゆ摠鏂佹杸鏂舵棜鏇掓獚妾栨獊妾ユ獕妾熸獩妾℃獮妾囨獡妾庯拷".split("");
	for(a = 0; a != t[236].length; ++a)
		if(t[236][a].charCodeAt(0) !== 65533) { r[t[236][a]] = 60416 + a;
			e[60416 + a] = t[236][a] }
	t[237] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷妾曟獌妾ㄦ妾戞┛妾︽獨妾呮獙妾掓瓫娈皦婵屾京婵存繑婵ｆ繙婵咖婵︽繛婵叉繚婵㈡卡鐕＄嚤鐕ㄧ嚥鐕ょ嚢鐕㈢嵆鐛嵂鐠楃挷鐠拹鐠挱鐠辩挜鐠攼鐢戠敀鐢忕杽鐧冪檲鐧夌檱鐨ょ洨鐬电灚鐬茬灧鐬讹拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界灤鐬辩灗鐭扮３纾界纾荤＜纾茬纾圭＞绀勭Λ绂ㄧ绌涚〇绌樼绌氱绔€绔佺皡绨忕绨€绡跨绨庣绨嬬绨傜皦绨冪皝绡哥绨嗙绡辩皭绨婄敞绺讣绻傜赋椤堢父绺箟绻€绻囩俯绻岀赴绺荤付绻勭负缃呯娇缃剧浇缈寸坎鑰喕鑷勮噷鑷婅噮鑷囪喖鑷╄墰鑹氳墱钖冭杸钖忚枾钖曡枲钖嬭枺钑昏枻钖氳枮锟�".split("");
	for(a = 0; a != t[237].length; ++a)
		if(t[237][a].charCodeAt(0) !== 65533) { r[t[237][a]] = 60672 + a;
			e[60672 + a] = t[237][a] }
	t[238] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷钑疯暭钖夎枴钑鸿暩钑楄枎钖栬枂钖嶈枡钖濊杹钖㈣杺钖堣枀钑硅暥钖樿枑钖熻櫒铻捐灙铻焻铻拌灛铻硅灥铻艰灝锜夎焹锜傝煂铻疯灟锜勮煀铻磋灦铻胯灨铻借煘铻茶さ瑜宠ぜ瑜捐瑗掕し瑗傝Ν瑕Ξ瑙茶С璎烇拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借瑯璎栬瑧璎呰瑡璎㈣瑥璎掕瑫璎囪瑣璎堣瑔璎滆瑩璎氳睆璞拌辈璞辫悲璨曡矓璩硅弹韫庤箥韫撹箰韫岃箛杞冭絸閭呴伨閯搁啔閱㈤啗閱欓啛閱￠啙閱犻帯閹冮幆閸ら崠閸囬嵓閸橀崪閸堕崏閸愰崙閸犻嵀閹忛崒閸嵐閸楅崟閸掗崗閸遍嵎閸婚崱閸為崳閸ч巰閸庨崣闂囬梹闂夐梼闂呴柗闅毎闅湢闇熼湗闇濋湙闉氶灐闉滐拷".split("");
	for(a = 0; a != t[238].length; ++a)
		if(t[238][a].charCodeAt(0) !== 65533) { r[t[238][a]] = 60928 + a;
			e[60928 + a] = t[238][a] }
	t[239] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷闉為灊闊曢煍闊遍椤勯椤夐椤冮ぅ椁が椁こ椁查く椁け椁伴棣ｉΑ楱傞Ш椐撮Х椐归Ц椐堕Щ椐介Ь椐奸▋楠鹃楂介瑏楂奸瓐楫氶楫為疀楫﹂楫ラ楫嗛楫犻榇抽祦榈ч炊榇疮榇遍锤榇帮拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介祬榈傞祪榇鹃捶榈€榇界康榇簥楹夐簫楹伴粓榛氶换榛块激榧ｉ饥榻旈緺鍎卞劖鍎殬鍤滃殫鍤氬殱鍤欏グ瀣煎暴灞穩骞巩鎳樻嚐鎳嚠鎳辨嚜鎳版嚝鎳栨嚛鎿挎攧鎿芥摳鏀佹攦鎿兼枖鏃涙洑鏇涙洏娅呮妾芥娅嗘妾舵娅囨妾瓰姣夋皨鐎囩€岀€嶇€佺€呯€旂€庢靠鐎€婵荤€︽考婵风€婄垇鐕跨嚬鐖冪嚱鐛讹拷".split("");
	for(a = 0; a != t[239].length; ++a)
		if(t[239][a].charCodeAt(0) !== 65533) { r[t[239][a]] = 61184 + a;
			e[61184 + a] = t[239][a] }
	t[240] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鐠哥搥鐠电搧鐠剧挾鐠荤搨鐢旂敁鐧滅櫎鐧欑檺鐧撶櫁鐧氱殾鐨界洭鐭傜灪纾跨绀撶绀夌绀掔绂Μ绌熺皽绨╃皺绨犵盁绨皾绨︾皑绨㈢哎绨扮箿绻愮箹绻ｇ箻绻㈢篃绻戠範绻楃箵缇电境缈风扛鑱佃噾鑷掞拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借噽鑹熻墳钖磋梿钘€钘冭梻钖宠柕钖借棁钘勮柨钘嬭棊钘堣梾钖辫柖钘掕槫钖歌柗钖捐櫓锜ц煢锜㈣煕锜煪锜ヨ煙锜宠煠锜旇煖锜撹煭锜樿煟铻よ煑锜欒爜锜磋煥锜濊瑗嬭瑗岃瑗愯瑗夎璎ц璎宠璎佃瓏璎璎捐璎ヨ璎﹁璎璎昏璎鸿眰璞佃矙璨樿矖璩捐磩璐傝磤韫滆耿韫犺箺韫栬篂韫ヨ恭锟�".split("");
	for(a = 0; a != t[240].length; ++a)
		if(t[240][a].charCodeAt(0) !== 65533) { r[t[240][a]] = 61440 + a;
			e[61440 + a] = t[240][a] }
	t[241] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷韫涜箽韫¤節韫╄箶杞嗚絿杞堣綃閯ㄩ労閯婚劸閱ㄩ啣閱ч啹閱幍閹岄帓閹烽帥閹濋帀閹ч帋閹帪閹﹂帟閹堥帣閹熼帊閹遍帒閹查帳閹ㄩ幋閹ｉ帴闂掗棑闂戦毘闆楅洑宸傞洘闆橀洕闇ｉ湤闇ラ灛闉灗闉灓闉拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介灑闉ラ煑闊欓煐闊橀熀椤愰椤掗⒏楗侀ぜ椁洪◤楱嬮▔楱嶉▌楱戦▕楱呴▏楱嗛珋楂滈瑘楝勯瑓楝╅榄婇瓕榄嬮瘒榀嗛瘍楫块瘉楫甸榀撻榀勯楫介禍榈撻祻榈婇禌榈嬮禉榈栭祵榈楅祾榈旈禑榈橀禋楹庨簩榛熼紒榧€榧栭讥榧吉榧╅绩榻岄綍鍎村劦鍔栧嫹鍘村毇鍤殾鍤у毆鍤澹濆澶掑瀣惧宸冨拱锟�".split("");
	for(a = 0; a != t[241].length; ++a)
		if(t[241][a].charCodeAt(0) !== 65533) { r[t[241][a]] = 61696 + a;
			e[61696 + a] = t[241][a] }
	t[242] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷寰挎嚮鏀囨攼鏀嶆攭鏀屾攷鏂勬棡鏃濇洖娅ф珷娅屾珣娅欐珛娅熸珳娅愭娅忔珝娅炴瓲娈版皩鐎欑€х€犵€栫€€＄€㈢€ｇ€╃€楃€ょ€滅€垖鐖婄垏鐖傜垍鐘ョ姦鐘ょ姡鐘＄搵鐡呯挿鐡冪敄鐧犵焿鐭婄焺鐭辩绀涳拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界ぁ绀滅绀炵Π绌х┄绨崇凹绨圭艾绨荤超绯苟绻电垢绻扮狗绻购绻茬勾绻ㄧ綃缃婄緝缇嗙痉缈界烤鑱歌嚄鑷曡墹鑹¤墸钘棻钘棛钘¤棬钘氳棗钘棽钘歌棙钘熻棧钘滆棏钘拌棪钘棡钘㈣爛锜鸿爟锜惰煼锠夎爩锠嬭爢锜艰爤锜胯爦锠傝ア瑗氳瑗楄ァ瑗滆瑗濊瑕堣Ψ瑕惰Ф璀愯瓐璀婅瓈璀撹瓥璀旇瓔璀曪拷".split("");
	for(a = 0; a != t[242].length; ++a)
		if(t[242][a].charCodeAt(0) !== 65533) { r[t[242][a]] = 61952 + a;
			e[61952 + a] = t[242][a] }
	t[243] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷璀戣瓊璀掕瓧璞冭狈璞惰矚璐嗚磭璐夎冬瓒董瓒弓韫歌钩韫汞韫昏粋杞掕綉杞忚綈杞撹敬閰€閯块啺閱彏閺囬弿閺傞彋閺愰徆閺弻閺欓帺閺﹂強閺旈彯閺ｉ彆閺勯弾閺€閺掗彠闀介棜闂涢洝闇╅湯闇湪闇︼拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介灣闉烽灦闊濋煘闊熼椤欓椤楅⒖棰介⒒棰鹃楗囬棣﹂Η楱氶〞楱ラ楱ら楱㈤楱чǎ楱為楱旈珎楝嬮瑠楝庨瑢楝烽榀癄榀為榀﹂榀伴瘮榀楅榀滈瘷榀ラ瘯榀￠瘹榈烽秮槎婇秳槎堥当槎€榈搁秵槎嬮秾榈介但榈撮档榈伴旦槎呴党榈婚秱榈倒榈块秶榈ㄩ簲楹戦粈榛奸辑榻€榻侀綅榻栭綏榻樺尫鍤诧拷".split("");
	for(a = 0; a != t[243].length; ++a)
		if(t[243][a].charCodeAt(0) !== 65533) { r[t[243][a]] = 62208 + a;
			e[62208 + a] = t[243][a] }
	t[244] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鍤靛毘澹ｅ瓍宸嗗穱寤化蹇€蹇佹嚬鏀楁敄鏀曟敁鏃熸洦鏇ｆ洡娅虫娅娅规娅鐎肩€电€€风€寸€辩亗鐎哥€跨€虹€圭亐鐎荤€崇亖鐖撶垟鐘ㄧ嵔鐛肩捄鐨毆鐨剧洯鐭岀煄鐭忕煃鐭茬ぅ绀ｇぇ绀ㄧい绀╋拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界Σ绌┈绌绫夌眻绫婄眹绫呯钞绻荤咕绾佺簚缇虹靠鑱硅嚊鑷欒垕鑹ㄨ墿铇㈣椏铇佽椌铇涜榾钘惰槃铇夎槄铇岃椊锠欒爯锠戣牀锠撹爾瑗ｈウ瑕硅Х璀犺璀濊璀ｈ璀ц瓒簡韬堣簞杞欒綎杞楄綍杞樿綒閭嶉厓閰侀喎閱甸啿閱抽悑閻撻徎閻犻悘閻旈従閻曢悙閻ㄩ悪閻嶉彽閻€閺烽悋閻庨悥閻掗徍閻夐徃閻婇徔锟�".split("");
	for(a = 0; a != t[244].length; ++a)
		if(t[244][a].charCodeAt(0) !== 65533) { r[t[244][a]] = 62464 + a;
			e[62464 + a] = t[244][a] }
	t[245] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷閺奸悓閺堕悜閻嗛棡闂犻棢闇湳闉归灮闊介熅椤犻、椤ｉ椋侀楗愰楗欓楗嬮楱查ù楱遍ì楱ǘ楱╅ó楱搁ō楂囬珚楂嗛瑦楝掗瑧榘嬮皥榀烽皡榘掗楸€榘囬皫榘嗛皸榘旈皦槎熼稒槎ら稘槎掗稑槎愰稕锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷槎犻稊槎滈丢槎楅丁槎氶盯槎ㄩ稙槎ｉ犊槎╅稏槎﹂锭楹欓簺楹氶互榛ら户榛﹂及榧經榻犻綖榻濋綑榫戝労鍎瑰姌鍔楀泝鍤藉毦瀛堝瓏宸嬪窂寤辨嚱鏀涙瑐娅兼瑑娅告瑎鐏冪亜鐏婄亪鐏夌亝鐏嗙垵鐖氱垯鐛剧敆鐧煇绀け绀睌绫撶巢绾婄簢绾堢簨绾嗙簫缃嶇净鑰拌嚌铇樿槳铇﹁槦铇ｈ槣铇欒槯铇槨铇犺槱铇炶槬锟�".split("");
	for(a = 0; a != t[245].length; ++a)
		if(t[245][a].charCodeAt(0) !== 65533) { r[t[245][a]] = 62720 + a;
			e[62720 + a] = t[245][a] }
	t[246] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锠╄牆锠涜牋锠よ牅锠瑗ォ瑗カ瑙鸿璀歌瓍璀鸿璐愯磾瓒簬韬岃綖杞涜綕閰嗛厔閰呴喒閻块惢閻堕惄閻介惣閻伴惞閻惙閻憖閻遍棩闂ら棧闇甸満闉块煛椤ら椋嗛楗橀楱归ń椹嗛﹦椹傞﹣楱猴拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介楂嶉瑫楝楅瑯楝栭榄掗矮榘濋皽榘埃榘ㄩ癌榘ら啊槎烽抖槎奸穪榉囬穵榉忛毒榉呴穬槎婚兜榉庨豆槎洪冬榉堥侗槎穼槎抽穽槎查购楹滈猾榛画榧涢紭榧氶急榻庨渐榻ら緬浜瑰泦鍥呭泲濂卞瓔瀛屽窌宸戝徊鏀℃敔鏀︽敘娆嬫瑘娆夋皪鐏曠仏鐏楃亽鐖炵垷鐘╃嵖鐡樼摃鐡欑摋鐧毉绀电Υ绌扮┍绫楃睖绫欑睕绫氾拷".split("");
	for(a = 0; a != t[246].length; ++a)
		if(t[246][a].charCodeAt(0) !== 65533) { r[t[246][a]] = 62976 + a;
			e[62976 + a] = t[246][a] }
	t[247] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绯寸潮绾戠綇缇囪嚍鑹槾铇佃槼铇槻铇惰牞锠ㄨ牔锠牓瑗辫瑕捐Щ璀捐畡璁傝畣璁呰璐曡簳韬旇簹韬掕簮韬栬簵杞犺舰閰囬憣閼愰憡閼嬮憦閼囬憛閼堥憠閼嗛溈闊ｉ—椤╅楗旈椹庨椹旈椹忛﹫椹婏拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介椹掗楂愰瑱楝榄栭瓡楸嗛眻榘块眲榘归俺楸侀凹榘烽按榘查敖榘堕窙榉掗窞榉氶穻榉愰窚榉戦窡榉╅窓榉橀窎榉甸窌榉濋憾榛伴嫉榧抽疾榻傞将榫曢劲鍎藉姍澹ㄥ＇濂插瓖宸樿牤褰忔垇鎴冩垊鏀╂敟鏂栨洬娆戞瑨娆忔瘖鐏涚仛鐖㈢巶鐜佺巸鐧扮煍绫х宝绾曡壃铇鸿檧铇硅樇铇辫樆铇捐牥锠茶牣锠宠ザ瑗磋コ瑙撅拷".split("");
	for(a = 0; a != t[247].length; ++a)
		if(t[247][a].charCodeAt(0) !== 65533) { r[t[247][a]] = 63232 + a;
			e[63232 + a] = t[247][a] }
	t[248] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷璁岃畮璁嬭畧璞呰礄韬樿饯杞ｉ喖閼㈤憰閼濋憲閼為焺闊呴爛椹栭楝為瑹楝犻睊楸橀睈楸婇睄楸嬮睍楸欓睂楸庨坊榉烽矾榉ｉ帆榉搁筏榉堕贰榉乏榉查钒榉㈤番榉撮烦榉ㄩ翻榛傞粣榛查怀榧嗛紲榧搁挤榧堕絻榻忥拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介奖榻伴疆榻洆鍥嶅瓗灞敪鏇洰娆撶仧鐏＄仢鐏犵垼鐡涚摜鐭曠じ绂风Χ绫簵缇夎壄铏冭牳锠疯牭琛嬭當璁曡簽韬熻籂韬濋喚閱介噦閼懆閼╅洢闈嗛潈闈囬焽闊ラ楂曢瓩楸ｉ抱楸﹂雹楸為睜楦傞肪楦囬竷楦嗛竻楦€楦侀笁榉块方楦勯籂榧為絾榻撮降榻跺洈鏀柛娆樻瑱娆楁瑲鐏㈢垿鐘煒鐭欑す绫╃鲍绯剁簹锟�".split("");
	for(a = 0; a != t[248].length; ++a)
		if(t[248][a].charCodeAt(0) !== 65533) { r[t[248][a]] = 63488 + a;
			e[63488 + a] = t[248][a] }
	t[249] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷绾樼簺绾欒嚑鑷¤檰铏囪檲瑗硅ズ瑗艰セ瑙胯畼璁欒亥韬よ海閼懎閼懕閼抽潐椤查楸ㄩ碑楸笅楦嶉笎楦忛笒楦戦骸榛甸級榻囬礁榻婚胶榻瑰湠鐏︾悲锠艰恫韬﹂噧閼撮懜閼堕懙椹犻贝楸抽北楸甸笖楦撻欢榧婏拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介兢鐏ㄧ仴绯疯櫔锠捐牻锠胯疄璨滆憨杌夐潒椤抽〈椋岄ァ棣─椹﹂┃楝ら笗楦楅綀鎴囨瑸鐖ц檶韬ㄩ拏閽€閽侀┅椹ㄩ楦欑埄铏嬭疅閽冮惫楹风櫟椹焙楦濈仼鐏氦榻鹃綁榫樼閵硅澧绘亽绮у鈺斺暒鈺椻暊鈺暎鈺氣暕鈺濃晵鈺も晻鈺炩暘鈺♀晿鈺р暃鈺撯暐鈺栤暉鈺暍鈺欌暔鈺溾晳鈺愨暛鈺暟鈺枔锟�".split("");
	for(a = 0; a != t[249].length; ++a)
		if(t[249][a].charCodeAt(0) !== 65533) { r[t[249][a]] = 63744 + a;
			e[63744 + a] = t[249][a] }
	return { enc: r, dec: e }
}();
cptable[1250] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~鈧拷鈥氾拷鈥炩€︹€犫€★拷鈥芭犫€古毰づ脚癸拷鈥樷€欌€溾€濃€⑩€撯€旓拷鈩⑴♀€号浥ヅ九郝犓囁樑伮つ劼β┡灺宦奥彼浥偮绰德堵仿改吪熉荒剿澞九寂斆伱偰偯勀鼓喢嚹屆壞樏嬆毭嵜幠幠惻兣嚸撁斉惷柮椗樑毰懊溍澟⒚熍暶∶⒛兠つ耗嚸嵜┠櫭浢從懪勁埫趁磁懨睹放櫯号泵济脚Ｋ�",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[1251] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~袀袃鈥氀撯€炩€︹€犫€♀偓鈥靶夆€剐娦屝嬓徰掆€樷€欌€溾€濃€⑩€撯€旓拷鈩⒀欌€貉氀溠浹熉犘幯炐埪ひ惵β伮┬劼嚶奥毙喲栆懧德堵费戔剸褦禄褬袇褧褩袗袘袙袚袛袝袞袟袠袡袣袥袦袧袨袩袪小孝校肖啸笑效楔些歇蝎鞋协挟携邪斜胁谐写械卸蟹懈泄泻谢屑薪芯锌褉褋褌褍褎褏褑褔褕褖褗褘褜褝褞褟",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[1252] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~鈧拷鈥毱掆€炩€︹€犫€∷嗏€芭犫€古掞拷沤锟斤拷鈥樷€欌€溾€濃€⑩€撯€斔溾劉拧鈥号擄拷啪鸥聽隆垄拢陇楼娄搂篓漏陋芦卢颅庐炉掳卤虏鲁麓碌露路赂鹿潞禄录陆戮驴脌脕脗脙脛脜脝脟脠脡脢脣脤脥脦脧脨脩脪脫脭脮脰脳脴脵脷脹脺脻脼脽脿谩芒茫盲氓忙莽猫茅锚毛矛铆卯茂冒帽貌贸么玫枚梅酶霉煤没眉媒镁每",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[1253] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~鈧拷鈥毱掆€炩€︹€犫€★拷鈥帮拷鈥癸拷锟斤拷锟斤拷鈥樷€欌€溾€濃€⑩€撯€旓拷鈩拷鈥猴拷锟斤拷锟铰犖呂喡Ｂぢヂβ╋拷芦卢颅庐鈥暵奥甭猜澄劼德堵肺埼壩娐晃屄轿幬徫愇懳捨撐斘曃栁椢樜櫸毼浳溛澪炍熚犖★拷危韦违桅围唯惟为潍维苇萎委伟伪尾纬未蔚味畏胃喂魏位渭谓尉慰蟺蟻蟼蟽蟿蠀蠁蠂蠄蠅蠆蠇蠈蠉蠋锟�",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[1254] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~鈧拷鈥毱掆€炩€︹€犫€∷嗏€芭犫€古掞拷锟斤拷锟解€樷€欌€溾€濃€⑩€撯€斔溾劉拧鈥号擄拷锟脚嘎犅÷⒙Ｂぢヂβ┞奥甭猜陈绰德堵仿嘎孤郝宦悸铰韭棵€脕脗脙脛脜脝脟脠脡脢脣脤脥脦脧臑脩脪脫脭脮脰脳脴脵脷脹脺陌艦脽脿谩芒茫盲氓忙莽猫茅锚毛矛铆卯茂臒帽貌贸么玫枚梅酶霉煤没眉谋艧每",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[1255] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~鈧拷鈥毱掆€炩€︹€犫€∷嗏€帮拷鈥癸拷锟斤拷锟斤拷鈥樷€欌€溾€濃€⑩€撯€斔溾劉锟解€猴拷锟斤拷锟铰犅÷⒙ｂ偑楼娄搂篓漏脳芦卢颅庐炉掳卤虏鲁麓碌露路赂鹿梅禄录陆戮驴职直植殖执值侄址指止锟街恢贾街局孔€讈讉變装妆撞壮状锟斤拷锟斤拷锟斤拷锟阶愖懽捵撟斪曌栕椬樧欁氉涀溩澴炞熥犠∽⒆Ｗぷプψёㄗ┳拷锟解€庘€忥拷",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[1256] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~鈧锯€毱掆€炩€︹€犫€∷嗏€百光€古捼嗂樬堏€樷€欌€溾€濃€⑩€撯€斱┾劉趹鈥号撯€屸€嵹郝犡屄⒙Ｂぢヂβ┶韭奥甭猜陈绰德堵仿嘎关浡宦悸铰矩熪佖∝⒇Ｘへヘωжㄘ┴柏必藏池簇地睹椮坟肛关嘿€賮賯賰脿賱芒賲賳賴賵莽猫茅锚毛賶賷卯茂賸賹賺賻么購賽梅賾霉賿没眉鈥庘€徾�",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[1257] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~鈧拷鈥氾拷鈥炩€︹€犫€★拷鈥帮拷鈥癸拷篓藝赂锟解€樷€欌€溾€濃€⑩€撯€旓拷鈩拷鈥猴拷炉藳锟铰狅拷垄拢陇锟铰β樎┡柭喡奥甭猜陈绰德堵访嘎古椔宦悸铰久δ勀€膯脛脜臉膾膶脡殴臇蘑亩莫幕艩艃艆脫艑脮脰脳挪艁艢弄脺呕沤脽膮寞膩膰盲氓臋膿膷茅藕臈模姆墨募拧艅艈贸艒玫枚梅懦艂艣奴眉偶啪藱",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[1258] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~鈧拷鈥毱掆€炩€︹€犫€∷嗏€帮拷鈥古掞拷锟斤拷锟解€樷€欌€溾€濃€⑩€撯€斔溾劉锟解€号擄拷锟脚嘎犅÷⒙Ｂぢヂβ┞奥甭猜陈绰德堵仿嘎孤郝宦悸铰韭棵€脕脗膫脛脜脝脟脠脡脢脣虁脥脦脧膼脩虊脫脭茽脰脳脴脵脷脹脺漂虄脽脿谩芒膬盲氓忙莽猫茅锚毛虂铆卯茂膽帽蹋贸么啤枚梅酶霉煤没眉瓢鈧�",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[1e4] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~脛脜脟脡脩脰脺谩脿芒盲茫氓莽茅猫锚毛铆矛卯茂帽贸貌么枚玫煤霉没眉鈥犅奥⒙Ｂр€⒙睹熉┾劉麓篓鈮犆喢樷垶卤鈮も墺楼碌鈭傗垜鈭徬€鈭衡劍忙酶驴隆卢鈭毱掆増鈭喡烩€β犆€脙脮艗艙鈥撯€斺€溾€濃€樷€櫭封棅每鸥鈦劼も€光€猴瑏铿傗€÷封€氣€炩€懊偯娒伱嬅埫嵜幟徝屆撁旓拷脪脷脹脵谋藛藴炉藰藱藲赂藵藳藝",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[10006] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~脛鹿虏脡鲁脰脺螀脿芒盲蝿篓莽茅猫锚毛拢鈩⒚€⒙解€懊疵堵β姑幻尖€犖撐斘樜浳炍犆熉┪Ｎр墵掳螄螒卤鈮も墺楼螔螘螙螚螜螝螠桅潍唯惟维螡卢螣巍鈮埼ぢ烩€β犖ノ單埮撯€撯€曗€溾€濃€樷€櫭肺壩娢屛幬屛徬嵨蔽蚕埼次迪單澄肺刮疚何晃嘉轿肯€蠋蟻蟽蟿胃蠅蟼蠂蠀味蠆蠇螑伟锟�",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[10007] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~袗袘袙袚袛袝袞袟袠袡袣袥袦袧袨袩袪小孝校肖啸笑效楔些歇蝎鞋协挟携鈥犅奥⒙Ｂр€⒙缎喡┾劉袀褣鈮犘冄撯垶卤鈮も墺褨碌鈭傂埿勓斝囇椥壯櫺娧氀樞吢垰茠鈮堚垎芦禄鈥β犘嬔浶屟溠曗€撯€斺€溾€濃€樷€櫭封€炐幯炐徰熲剸衼褢褟邪斜胁谐写械卸蟹懈泄泻谢屑薪芯锌褉褋褌褍褎褏褑褔褕褖褗褘褜褝褞陇",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[10008] = function() {
	var e = [],
		r = {},
		t = [],
		a;
	t[0] = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~聙铮橈铮氾铮滐铮烇铮狅！铮＃铮わ％铮︼＇铮）铮＋铮－铮／铮帮１铮诧３铮达５铮讹７锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤８铮癸：铮伙＜铮斤＞铮�".split("");
	for(a = 0; a != t[0].length; ++a)
		if(t[0][a].charCodeAt(0) !== 65533) { r[t[0][a]] = 0 + a;
			e[0 + a] = t[0][a] }
	t[161] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟姐€€銆併€傘兓藟藝篓銆冦€呪€曪綖锟解€︹€樷€欌€溾€濄€斻€曘€堛€夈€娿€嬨€屻€嶃€庛€忋€栥€椼€愩€懧泵椕封埗鈭р埁鈭戔垙鈭埄鈭堚埛鈭氣姤鈭モ垹鈱掆姍鈭埉鈮♀墝鈮堚埥鈭濃墵鈮壇鈮も墺鈭炩埖鈭粹檪鈾€掳鈥测€斥剝锛劼わ繝锟♀€奥р剸鈽嗏槄鈼嬧棌鈼庘棁鈼嗏枴鈻犫柍鈻测€烩啋鈫愨啈鈫撱€擄拷".split("");
	for(a = 0; a != t[161].length; ++a)
		if(t[161][a].charCodeAt(0) !== 65533) { r[t[161][a]] = 41216 + a;
			e[41216 + a] = t[161][a] }
	t[162] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟解拡鈷夆拪鈷嬧拰鈷嶁拵鈷忊拹鈷戔拻鈷撯挃鈷曗挅鈷椻挊鈷欌挌鈷涒懘鈶碘懚鈶封懜鈶光懞鈶烩懠鈶解懢鈶库拃鈷佲拏鈷冣拕鈷呪拞鈷団憼鈶♀憿鈶ｂ懁鈶モ懄鈶р懆鈶╋拷锟姐垹銏°垻銏ｃ垽銏ャ垿銏с埁銏╋拷锟解厾鈪♀參鈪ｂ叅鈪モ叇鈪р叏鈪┾叒鈪拷锟斤拷".split("");
	for(a = 0; a != t[162].length; ++a)
		if(t[162][a].charCodeAt(0) !== 65533) { r[t[162][a]] = 41472 + a;
			e[41472 + a] = t[162][a] }
	t[163] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤紒锛傦純锟ワ紖锛嗭紘锛堬級锛婏紜锛岋紞锛庯紡锛愶紤锛掞紦锛旓紩锛栵紬锛橈紮锛氾紱锛滐紳锛烇紵锛狅肌锛迹锛わ讥锛︼姬锛缉锛极锛辑锛集锛帮急锛诧汲锛达嫉锛讹挤锛革脊锛猴蓟锛硷冀锛撅伎锝€锝侊絺锝冿絼锝咃絾锝囷綀锝夛綂锝嬶綄锝嶏綆锝忥綈锝戯綊锝擄綌锝曪綎锝楋綐锝欙綒锝涳綔锝濓浚锟�".split("");
	for(a = 0; a != t[163].length; ++a)
		if(t[163][a].charCodeAt(0) !== 65533) { r[t[163][a]] = 41728 + a;
			e[41728 + a] = t[163][a] }
	t[164] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟姐亖銇傘亙銇勩亝銇嗐亣銇堛亯銇娿亱銇屻亶銇庛亸銇愩亼銇掋亾銇斻仌銇栥仐銇樸仚銇氥仜銇溿仢銇炪仧銇犮仭銇仯銇ゃ仴銇︺仹銇ㄣ仼銇伀銇伃銇伅銇般伇銇层伋銇淬伒銇躲伔銇搞伖銇恒伝銇笺伣銇俱伩銈€銈併倐銈冦倓銈呫倖銈囥倛銈夈倞銈嬨倢銈嶃値銈忋倫銈戙倰銈擄拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[164].length; ++a)
		if(t[164][a].charCodeAt(0) !== 65533) { r[t[164][a]] = 41984 + a;
			e[41984 + a] = t[164][a] }
	t[165] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟姐偂銈偅銈ゃ偉銈︺偋銈ㄣ偐銈偒銈偔銈偗銈般偙銈层偝銈淬偟銈躲偡銈搞偣銈恒偦銈笺偨銈俱偪銉€銉併儌銉冦儎銉呫儐銉囥儓銉夈儕銉嬨儗銉嶃儙銉忋儛銉戙儝銉撱償銉曘儢銉椼儤銉欍儦銉涖儨銉濄優銉熴儬銉°儮銉ｃ儰銉ャ儲銉с儴銉┿儶銉儸銉儺銉儼銉便儾銉炽兇銉点兌锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[165].length; ++a)
		if(t[165][a].charCodeAt(0) !== 65533) { r[t[165][a]] = 42240 + a;
			e[42240 + a] = t[165][a] }
	t[166] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟轿懳捨撐斘曃栁椢樜櫸毼浳溛澪炍熚犖∥Ｎのノξㄎ╋拷锟斤拷锟斤拷锟斤拷锟轿蔽参澄次滴段肺肝刮何晃嘉轿疚肯€蟻蟽蟿蠀蠁蠂蠄蠅锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[166].length; ++a)
		if(t[166][a].charCodeAt(0) !== 65533) { r[t[166][a]] = 42496 + a;
			e[42496 + a] = t[166][a] }
	t[167] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟叫愋懶捫撔斝曅佇栃椥樞櫺毿浶溞澬炐熜犘⌒⑿Ｐばバπㄐ┬拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷邪斜胁谐写械褢卸蟹懈泄泻谢屑薪芯锌褉褋褌褍褎褏褑褔褕褖褗褘褜褝褞褟锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[167].length; ++a)
		if(t[167][a].charCodeAt(0) !== 65533) { r[t[167][a]] = 42752 + a;
			e[42752 + a] = t[167][a] }
	t[168] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟侥伱∏幟犇撁┠浢惷嵜城捗才呵斆骨柷樓毲溍济拷锟斤拷锟斤拷锟斤拷锟斤拷锟姐剠銊嗐剣銊堛剦銊娿剫銊屻剭銊庛剰銊愩剳銊掋創銊斻剷銊栥剹銊樸剻銊氥剾銊溿劃銊炪劅銊犮劇銊劊銊ゃ劌銊︺劎銊ㄣ劑锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷".split("");
	for(a = 0; a != t[168].length; ++a)
		if(t[168][a].charCodeAt(0) !== 65533) { r[t[168][a]] = 43008 + a;
			e[43008 + a] = t[168][a] }
	t[169] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷鈹€鈹佲攤鈹冣攧鈹呪攩鈹団攬鈹夆攰鈹嬧攲鈹嶁攷鈹忊攼鈹戔敀鈹撯敂鈹曗敄鈹椻敇鈹欌敋鈹涒敎鈹濃敒鈹熲敔鈹♀敘鈹ｂ敜鈹モ敠鈹р敤鈹┾敧鈹敩鈹敭鈹敯鈹扁敳鈹斥敶鈹碘敹鈹封敻鈹光敽鈹烩敿鈹解斁鈹库晙鈺佲晜鈺冣晞鈺呪晢鈺団晥鈺夆晩鈺嬶拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[169].length; ++a)
		if(t[169][a].charCodeAt(0) !== 65533) { r[t[169][a]] = 43264 + a;
			e[43264 + a] = t[169][a] }
	t[176] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉晩闃垮焹鎸ㄥ搸鍞夊搥鐨戠檶钄肩煯鑹剧鐖遍殬闉嶆皑瀹変亢鎸夋殫宀歌兒妗堣偖鏄傜泿鍑规晼鐔勘琚勫偛濂ユ噴婢宠姯鎹屾墥鍙惂绗嗗叓鐤ゅ反鎷旇穻闈舵妸鑰欏潩闇哥舰鐖哥櫧鏌忕櫨鎽嗕桨璐ユ嫓绋楁枒鐝惉鎵宠埇棰佹澘鐗堟壆鎷屼即鐡ｅ崐鍔炵粖閭﹀府姊嗘鑶€缁戞纾呰殞闀戝倣璋よ嫗鑳炲寘瑜掑墺锟�".split("");
	for(a = 0; a != t[176].length; ++a)
		if(t[176][a].charCodeAt(0) !== 65533) { r[t[176][a]] = 45056 + a;
			e[45056 + a] = t[176][a] }
	t[177] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借杽闆逛繚鍫￠ケ瀹濇姳鎶ユ毚璞归矋鐖嗘澂纰戞偛鍗戝寳杈堣儗璐濋挕鍊嶇媹澶囨儷鐒欒濂旇嫰鏈宕╃环鐢车韫﹁扛閫奸蓟姣旈剻绗斿郊纰ц摉钄芥瘯姣欐瘱甯佸簢鐥归棴鏁濆紛蹇呰緹澹佽噦閬块櫅闉竟缂栬船鎵佷究鍙樺崬杈ㄨ京杈亶鏍囧姜鑶樿〃槌栨唻鍒槳褰枌婵掓花瀹炬憟鍏靛啺鏌勪笝绉夐ゼ鐐筹拷".split("");
	for(a = 0; a != t[177].length; ++a)
		if(t[177][a].charCodeAt(0) !== 65533) { r[t[177][a]] = 45312 + a;
			e[45312 + a] = t[177][a] }
	t[178] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界梾骞剁幓鑿犳挱鎷ㄩ挼娉㈠崥鍕冩悘閾傜當浼笡鑸惰剸鑶婃袱娉婇┏鎹曞崪鍝鸿ˉ鍩犱笉甯冩绨块儴鎬栨摝鐚滆鏉愭墠璐㈢潿韪╅噰褰╄彍钄￠鍙傝殨娈嬫儹鎯ㄧ伩鑻嶈埍浠撴钵钘忔搷绯欐Ы鏇硅崏鍘曠瓥渚у唽娴嬪眰韫彃鍙夎尙鑼舵煡纰存惤瀵熷矓宸鎷嗘煷璞烘悁鎺鸿潐棣嬭皸缂犻摬浜ч槓棰ゆ槍鐚栵拷".split("");
	for(a = 0; a != t[178].length; ++a)
		if(t[178][a].charCodeAt(0) !== 65533) { r[t[178][a]] = 45568 + a;
			e[45568 + a] = t[178][a] }
	t[179] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉満灏濆父闀垮伩鑲犲巶鏁炵晠鍞卞€¤秴鎶勯挒鏈濆槻娼发鍚电倰杞︽壇鎾ゆ帲褰绘緢閮磋嚕杈板皹鏅ㄥ勘娌夐檲瓒佽‖鎾戠О鍩庢鎴愬憟涔樼▼鎯╂緞璇氭壙閫為獘绉ゅ悆鐥存寔鍖欐睜杩熷紱椹拌€婚娇渚堝昂璧ょ繀鏂ョ偨鍏呭啿铏磭瀹犳娊閰暣韪岀鎰佺浠囩桓鐬呬笐鑷垵鍑烘┍鍘ㄨ簢閿勯洀婊侀櫎妤氾拷".split("");
	for(a = 0; a != t[179].length; ++a)
		if(t[179][a].charCodeAt(0) !== 65533) { r[t[179][a]] = 45824 + a;
			e[45824 + a] = t[179][a] }
	t[180] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界鍌ㄧ煑鎼愯Е澶勬彛宸濈┛妞戒紶鑸瑰枠涓茬柈绐楀耿搴婇棷鍒涘惞鐐婃嵍閿ゅ瀭鏄ユた閱囧攪娣崇函锠㈡埑缁扮柕鑼ㄧ闆岃緸鎱堢摲璇嶆鍒鸿祼娆¤仾钁卞洷鍖嗕粠涓涘噾绮楅唻绨囦績韫跨绐滄懅宕斿偓鑴嗙榿绮规番缈犳潙瀛樺纾嬫挳鎼撴帾鎸敊鎼揪绛旂槱鎵撳ぇ鍛嗘鍌ｆ埓甯︽畣浠ｈ捶琚嬪緟閫拷".split("");
	for(a = 0; a != t[180].length; ++a)
		if(t[180][a].charCodeAt(0) !== 65533) { r[t[180][a]] = 46080 + a;
			e[46080 + a] = t[180][a] }
	t[181] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥€犺€芥媴涓瑰崟閮告幐鑳嗘棪姘絾鎯贰璇炲脊铔嬪綋鎸″厷鑽℃。鍒€鎹ｈ箞鍊掑矝绁峰鍒扮ɑ鎮奸亾鐩楀痉寰楃殑韫伅鐧荤瓑鐬嚦閭撳牑浣庢淮杩晫绗涚媱娑ょ繜瀚℃姷搴曞湴钂傜甯濆紵閫掔紨棰犳巶婊囩鐐瑰吀闈涘灚鐢典絻鐢稿簵鎯﹀娣€娈跨鍙奸洉鍑嬪垇鎺夊悐閽撹皟璺岀埞纰熻澏杩皪鍙狅拷".split("");
	for(a = 0; a != t[181].length; ++a)
		if(t[181][a].charCodeAt(0) !== 65533) { r[t[181][a]] = 46336 + a;
			e[46336 + a] = t[181][a] }
	t[182] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟戒竵鐩彯閽夐《榧庨敪瀹氳涓笢鍐懀鎳傚姩鏍嬩緱鎭喕娲炲厹鎶栨枟闄¤眴閫楃棙閮界潱姣掔妸鐙鍫电澒璧屾潨闀€鑲氬害娓″绔煭閿绘鏂紟鍫嗗厬闃熷澧╁惃韫叉暒椤垮洡閽濈浘閬佹巼鍝嗗澶哄灈韬叉湹璺鸿埖鍓佹儼鍫曡浘宄ㄩ箙淇勯璁瑰ē鎭跺巹鎵奸亸閯傞タ鎭╄€屽効鑰冲皵楗垫幢浜岋拷".split("");
	for(a = 0; a != t[182].length; ++a)
		if(t[182][a].charCodeAt(0) !== 65533) { r[t[182][a]] = 46592 + a;
			e[46592 + a] = t[182][a] }
	t[183] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借窗鍙戠綒绛忎紣涔忛榾娉曠彁钘╁竼鐣炕妯婄熅閽掔箒鍑＄儲鍙嶈繑鑼冭穿鐘キ娉涘潑鑺虫柟鑲埧闃插Θ浠胯绾烘斁鑿查潪鍟￠鑲ュ尓璇藉悹鑲哄簾娌歌垂鑺厷鍚╂皼鍒嗙悍鍧熺剼姹剧矇濂嬩唤蹇挎劋绮赴灏佹灚铚傚嘲閿嬮鐤兘閫㈠啹缂濊濂夊嚖浣涘惁澶暦鑲ゅ鎵舵媯杈愬箙姘熺浼忎繕鏈嶏拷".split("");
	for(a = 0; a != t[183].length; ++a)
		if(t[183][a].charCodeAt(0) !== 65533) { r[t[183][a]] = 46848 + a;
			e[46848 + a] = t[183][a] }
	t[184] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥诞娑琚卞紬鐢姎杈呬刊閲滄枾鑴厬搴滆厫璧村壇瑕嗚祴澶嶅倕浠橀槣鐖惰吂璐熷瘜璁ｉ檮濡囩細鍜愬櫠鍢庤鏀规閽欑洊婧夊共鐢樻潌鏌戠鑲濊刀鎰熺鏁㈣担鍐堝垰閽㈢几鑲涚翰宀楁腐鏉犵瘷鐨嬮珮鑶忕緮绯曟悶闀愮鍛婂摜姝屾悂鎴堥附鑳崇枡鍓查潻钁涙牸铔ら榿闅旈摤涓悇缁欐牴璺熻€曟洿搴氱竟锟�".split("");
	for(a = 0; a != t[184].length; ++a)
		if(t[184][a].charCodeAt(0) !== 65533) { r[t[184][a]] = 47104 + a;
			e[47104 + a] = t[184][a] }
	t[185] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉焸鑰挎宸ユ敾鍔熸伃榫氫緵韬叕瀹紦宸╂睘鎷辫础鍏遍挬鍕炬矡鑻熺嫍鍨㈡瀯璐杈滆弴鍜曠畭浼版步瀛ゅ榧撳彜铔婇璋疯偂鏁呴【鍥洪泧鍒摐鍓愬鎸傝涔栨嫄鎬：鍏冲畼鍐犺绠￠缃愭儻鐏岃疮鍏夊箍閫涚懓瑙勫湱纭呭綊榫熼椇杞ㄩ璇＄櫢妗傛煖璺吹鍒借緤婊氭閿呴儹鍥芥灉瑁硅繃鍝堬拷".split("");
	for(a = 0; a != t[185].length; ++a)
		if(t[185][a].charCodeAt(0) !== 65533) { r[t[185][a]] = 47360 + a;
			e[47360 + a] = t[185][a] }
	t[186] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介瀛╂捣姘︿亥瀹抽獓閰ｆ啫閭煩鍚兜瀵掑嚱鍠婄綍缈版捈鎹嶆棻鎲炬倣鐒婃睏姹夊く鏉埅澹曞殠璞閮濆ソ鑰楀彿娴╁懙鍠濊嵎鑿忔牳绂惧拰浣曞悎鐩掕矇闃傛渤娑歌但瑜愰工璐哄樋榛戠棔寰堢嫚鎭ㄥ摷浜ㄦí琛℃亽杞板搫鐑樿櫣楦挎椽瀹忓紭绾㈠枆渚尨鍚煎帤鍊欏悗鍛间箮蹇界憵澹惰懌鑳¤澊鐙愮硦婀栵拷".split("");
	for(a = 0; a != t[186].length; ++a)
		if(t[186][a].charCodeAt(0) !== 65533) { r[t[186][a]] = 47616 + a;
			e[47616 + a] = t[186][a] }
	t[187] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉姬铏庡敩鎶や簰娌埛鑺卞摋鍗庣尵婊戠敾鍒掑寲璇濇寰婃€€娣潖娆㈢幆妗撹繕缂撴崲鎮ｅ敜鐥雹鐒曟叮瀹﹀够鑽掓厡榛勭：铦楃哀鐨囧嚢鎯剁厡鏅冨箤鎭嶈皫鐏版尌杈夊窘鎭㈣洈鍥炴瘉鎮旀収鍗夋儬鏅﹁纯绉戒細鐑╂眹璁宠缁樿崵鏄忓榄傛祽娣疯眮娲讳紮鐏幏鎴栨儜闇嶈揣绁稿嚮鍦惧熀鏈虹暩绋界Н绠曪拷".split("");
	for(a = 0; a != t[187].length; ++a)
		if(t[187][a].charCodeAt(0) !== 65533) { r[t[187][a]] = 47872 + a;
			e[47872 + a] = t[187][a] }
	t[188] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借倢楗ヨ抗婵€璁ラ浮濮哗缂夊悏鏋佹杈戠睄闆嗗強鎬ョ柧姹插嵆瀚夌骇鎸ゅ嚑鑴婂繁钃熸妧鍐€瀛ｄ紟绁墏鎮告祹瀵勫瘋璁¤鏃㈠繉闄呭缁х邯鍢夋灧澶逛匠瀹跺姞鑽氶璐剧敳閽惧亣绋间环鏋堕┚瀚佹鐩戝潥灏栫闂寸厧鍏艰偐鑹板ジ缂勮導妫€鏌⒈纭锋嫞鎹＄畝淇壀鍑忚崘妲涢壌璺佃幢瑙侀敭绠欢锟�".split("");
	for(a = 0; a != t[188].length; ++a)
		if(t[188][a].charCodeAt(0) !== 65533) { r[t[188][a]] = 48128 + a;
			e[48128 + a] = t[188][a] }
	t[189] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉仴鑸板墤楗笎婧呮锭寤哄兊濮滃皢娴嗘睙鐤嗚拫妗ㄥ璁插尃閰遍檷钑夋绀佺劍鑳朵氦閮婃祰楠勫▏鍤兼悈閾扮煫渚ヨ剼鐙¤楗虹即缁炲壙鏁欓叺杞胯緝鍙獤鎻帴鐨嗙Ц琛楅樁鎴姭鑺傛鏉版嵎鐫娲佺粨瑙ｅ鎴掕棄鑺ョ晫鍊熶粙鐤ヨ灞婂肪绛嬫枻閲戜粖娲ヨ绱ч敠浠呰皑杩涢澇鏅嬬杩戠儸娴革拷".split("");
	for(a = 0; a != t[189].length; ++a)
		if(t[189][a].charCodeAt(0) !== 65533) { r[t[189][a]] = 48384 + a;
			e[48384 + a] = t[189][a] }
	t[190] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉敖鍔茶崋鍏㈣寧鐫涙櫠椴镐含鎯婄簿绮崇粡浜曡鏅闈欏鏁暅寰勭棄闈栫珶绔炲噣鐐獦鎻┒绾犵帠闊箙鐏镐節閰掑帺鏁戞棫鑷艰垍鍜庡氨鐤氶灎鎷樼嫏鐤藉眳椹硅強灞€鍜€鐭╀妇娌仛鎷掓嵁宸ㄥ叿璺濊笧閿勘鍙ユ儳鐐墽鎹愰箖濞熷€︾湻鍗风虎鎾呮敨鎶夋帢鍊旂埖瑙夊喅璇€缁濆潎鑿岄挧鍐涘悰宄伙拷".split("");
	for(a = 0; a != t[190].length; ++a)
		if(t[190][a].charCodeAt(0) !== 65533) { r[t[190][a]] = 48640 + a;
			e[48640 + a] = t[190][a] }
	t[191] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟戒繆绔ｆ禋閮￠獜鍠€鍜栧崱鍜紑鎻╂シ鍑叏鍒婂牚鍕樺潕鐮嶇湅搴锋叿绯犳墰鎶椾孩鐐曡€冩嫹鐑ら潬鍧疯嫑鏌５纾曢绉戝３鍜冲彲娓村厠鍒诲璇捐偗鍟冨灕鎭冲潙鍚┖鎭愬瓟鎺ф姞鍙ｆ墸瀵囨灟鍝獰鑻﹂叿搴撹￥澶稿灝鎸庤法鑳潡绛蜂京蹇娆惧尅绛愮媯妗嗙熆鐪舵椃鍐典簭鐩斿部绐ヨ懙濂庨瓉鍌€锟�".split("");
	for(a = 0; a != t[191].length; ++a)
		if(t[191][a].charCodeAt(0) !== 65533) { r[t[191][a]] = 48896 + a;
			e[48896 + a] = t[191][a] }
	t[192] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介鎰ф簝鍧ゆ槅鎹嗗洶鎷墿寤撻様鍨冩媺鍠囪湣鑵婅荆鍟﹁幈鏉ヨ禆钃濆┆鏍忔嫤绡槕鍏版緶璋版徑瑙堟噿缂嗙儌婊ョ悈姒旂嫾寤婇儙鏈楁氮鎹炲姵鐗㈣€佷浆濮ラ叒鐑欐稘鍕掍箰闆烽暛钑剧绱劇鍨掓搨鑲嬬被娉１妤炲喎鍘樻ⅷ鐘侀粠绡辩嫺绂绘紦鐞嗘潕閲岄菠绀艰帀鑽斿悘鏍椾附鍘夊姳鐮惧巻鍒╁倛渚嬩繍锟�".split("");
	for(a = 0; a != t[192].length; ++a)
		if(t[192][a].charCodeAt(0) !== 65533) { r[t[192][a]] = 49152 + a;
			e[49152 + a] = t[192][a] }
	t[193] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界棦绔嬬矑娌ラ毝鍔涚拑鍝╀咯鑱旇幉杩為暟寤夋€滄稛甯樻暃鑴搁摼鎭嬬偧缁冪伯鍑夋绮辫壇涓よ締閲忔櫨浜皡鎾╄亰鍍氱枟鐕庡杈芥溅浜嗘拏闀ｅ粬鏂欏垪瑁傜儓鍔ｇ寧鐞虫灄纾烽湒涓撮偦槌炴穻鍑涜祦鍚濇嫀鐜茶彵闆堕緞閾冧级缇氬噷鐏甸櫟宀鍙︿护婧滅悏姒寸～棣忕暀鍒樼槫娴佹煶鍏緳鑱嬪挋绗肩锟�".split("");
	for(a = 0; a != t[193].length; ++a)
		if(t[193][a].charCodeAt(0) !== 65533) { r[t[193][a]] = 49408 + a;
			e[49408 + a] = t[193][a] }
	t[194] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介殕鍨勬嫝闄囨ゼ濞勬悅绡撴紡闄嬭姦鍗㈤搴愮倝鎺冲崵铏忛瞾楹撶闇茶矾璧傞箍娼炵褰曢檰鎴┐鍚曢摑渚ｆ梾灞ュ薄缂曡檻姘緥鐜囨护缁垮肠鎸涘婊﹀嵉涔辨帬鐣ユ姟杞鸡浠戞拨绾惰钀濊灪缃楅€婚敚绠╅瑁歌惤娲涢獑缁滃楹荤帥鐮佽殏椹獋鍢涘悧鍩嬩拱楹﹀崠杩堣剦鐬掗铔弧钄撴浖鎱㈡极锟�".split("");
	for(a = 0; a != t[194].length; ++a)
		if(t[194][a].charCodeAt(0) !== 65533) { r[t[194][a]] = 49664 + a;
			e[49664 + a] = t[194][a] }
	t[195] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借癌鑺掕尗鐩叉皳蹇欒幗鐚寘閿氭瘺鐭涢搯鍗寕鍐掑附璨岃锤涔堢帿鏋氭閰堕湁鐓ゆ病鐪夊獟闀佹瘡缇庢槯瀵愬濯氶棬闂蜂滑钀岃挋妾洘閿扮寷姊﹀瓱鐪啔闈＄硿杩疯皽寮ョ背绉樿娉岃湝瀵嗗箓妫夌湢缁靛啎鍏嶅媺濞╃紖闈㈣嫍鎻忕瀯钘愮娓哄簷濡欒攽鐏皯鎶跨毧鏁忔偗闂芥槑铻熼福閾悕鍛借艾鎽革拷".split("");
	for(a = 0; a != t[195].length; ++a)
		if(t[195][a].charCodeAt(0) !== 65533) { r[t[195][a]] = 49920 + a;
			e[49920 + a] = t[195][a] }
	t[196] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥懝铇戞ā鑶滅（鎽╅瓟鎶规湯鑾ⅷ榛樻搏婕犲癁闄岃皨鐗熸煇鎷囩墶浜╁姣嶅鏆箷鍕熸厱鏈ㄧ洰鐫︾墽绌嗘嬁鍝憪閽犻偅濞滅撼姘栦箖濂惰€愬鍗楃敺闅惧泭鎸犺剳鎭奸椆娣栧憿棣佸唴瀚╄兘濡湏鍊偿灏兼嫙浣犲尶鑵婚€嗘汉钄媹骞寸⒕鎾垫嵒蹇靛閰块笩灏挎崗鑱傚鍟晩闀嶆秴鎮ㄦ煚鐙炲嚌瀹侊拷".split("");
	for(a = 0; a != t[196].length; ++a)
		if(t[196][a].charCodeAt(0) !== 65533) { r[t[196][a]] = 50176 + a;
			e[50176 + a] = t[196][a] }
	t[197] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥嫥娉炵墰鎵挳绾借創娴撳啘寮勫ゴ鍔€掑コ鏆栬檺鐤熸尓鎳︾朝璇哄摝娆ч弗娈磋棔鍛曞伓娌ゅ暘瓒寸埇甯曟€曠惗鎷嶆帓鐗屽緲婀冩淳鏀€娼樼洏纾愮浖鐣斿垽鍙涗箵搴炴梺鑰儢鎶涘拞鍒ㄧ偖琚嶈窇娉″懜鑳氬煿瑁磋禂闄厤浣╂矝鍠风泦鐮版姩鐑规編褰摤妫氱〖绡疯啫鏈嬮箯鎹х鍧爳闇规壒鎶妶鐞垫瘲锟�".split("");
	for(a = 0; a != t[197].length; ++a)
		if(t[197][a].charCodeAt(0) !== 65533) { r[t[197][a]] = 50432 + a;
			e[50432 + a] = t[197][a] }
	t[198] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉暏鑴剧柌鐨尮鐥炲兓灞佽绡囧亸鐗囬獥椋樻紓鐡㈢エ鎾囩灔鎷奸璐搧鑱樹箳鍧嫻钀嶅钩鍑摱璇勫睆鍧℃臣棰囧﹩鐮撮瓌杩矔鍓栨墤閾轰粏鑾嗚憽鑿╄挷鍩旀湸鍦冩櫘娴﹁氨鏇濈€戞湡娆烘爾鎴氬涓冨噭婕嗘煉娌忓叾妫嬪姝х暒宕庤剱榻愭棗绁堢楠戣捣宀備篂浼佸惎濂戠爩鍣ㄦ皵杩勫純姹芥常璁帎锟�".split("");
	for(a = 0; a != t[198].length; ++a)
		if(t[198][a].charCodeAt(0) !== 65533) { r[t[198][a]] = 50688 + a;
			e[50688 + a] = t[198][a] }
	t[199] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥伆娲界壍鎵﹂拵閾呭崈杩佺浠熻唉涔鹃粩閽遍挸鍓嶆綔閬ｆ祬璋村爲宓屾瑺姝夋灙鍛涜厰缇屽钄峰己鎶㈡﹪閿规暡鎮勬ˉ鐬т箶渚ㄥ阀闉樻挰缈樺抄淇忕獚鍒囪寗涓旀€獌閽︿镜浜茬Е鐞村嫟鑺规搾绂藉瘽娌侀潚杞绘阿鍊惧嵖娓呮搸鏅存鞍鎯呴》璇峰簡鐞肩┓绉嬩笜閭辩悆姹傚洑閰嬫硡瓒嬪尯铔嗘洸韬眻椹辨笭锟�".split("");
	for(a = 0; a != t[199].length; ++a)
		if(t[199][a].charCodeAt(0) !== 65533) { r[t[199][a]] = 50944 + a;
			e[50944 + a] = t[199][a] }
	t[200] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉彇濞堕緥瓒ｅ幓鍦堥ⅶ鏉冮啗娉夊叏鐥婃嫵鐘埜鍔濈己鐐旂樃鍗撮箠姒风‘闆€瑁欑兢鐒剁噧鍐夋煋鐡ゅ￥鏀樺毞璁╅ザ鎵扮粫鎯圭儹澹粊浜哄繊闊т换璁ゅ垉濡婄韩鎵斾粛鏃ユ垘鑼歌搲鑽ｈ瀺鐔旀憾瀹圭粧鍐楁弶鏌旇倝鑼硅爼鍎掑濡傝颈涔虫睗鍏ヨぅ杞槷钑婄憺閿愰棸娑﹁嫢寮辨拻娲掕惃鑵硟濉炶禌涓夊弫锟�".split("");
	for(a = 0; a != t[200].length; ++a)
		if(t[200][a].charCodeAt(0) !== 65533) { r[t[200][a]] = 51200 + a;
			e[51200 + a] = t[200][a] }
	t[201] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟戒紴鏁ｆ鍡撲抚鎼旈獨鎵珎鐟熻壊娑╂．鍍ц帋鐮傛潃鍒规矙绾卞偦鍟ョ厼绛涙檼鐝婅嫬鏉夊北鍒犵吔琛棯闄曟搮璧¤喅鍠勬睍鎵囩籍澧掍激鍟嗚祻鏅屼笂灏氳３姊㈡崕绋嶇儳鑺嶅嫼闊跺皯鍝ㄩ偟缁嶅ア璧婅泧鑸岃垗璧︽憚灏勬厬娑夌ぞ璁剧牱鐢冲懟浼歌韩娣卞缁呯娌堝濠剁敋鑲炬厧娓楀０鐢熺敟鐗插崌缁筹拷".split("");
	for(a = 0; a != t[201].length; ++a)
		if(t[201][a].charCodeAt(0) !== 65533) { r[t[201][a]] = 51456 + a;
			e[51456 + a] = t[201][a] }
	t[202] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界渷鐩涘墿鑳滃湥甯堝け鐙柦婀胯瘲灏歌櫛鍗佺煶鎷炬椂浠€椋熻殌瀹炶瘑鍙茬煝浣垮睅椹跺寮忕ず澹笘鏌夸簨鎷獡閫濆娍鏄棞鍣€備粫渚嶉噴楗版皬甯傛亙瀹よ璇曟敹鎵嬮瀹堝鎺堝敭鍙楃槮鍏借敩鏋㈡⒊娈婃姃杈撳彅鑸掓窇鐤忎功璧庡鐔熻柉鏆戞洐缃茶渶榛嶉紶灞炴湳杩版爲鏉熸垗绔栧搴舵暟婕憋拷".split("");
	for(a = 0; a != t[202].length; ++a)
		if(t[202][a].charCodeAt(0) !== 65533) { r[t[202][a]] = 51712 + a;
			e[51712 + a] = t[202][a] }
	t[203] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥仌鍒疯€嶆憯琛扮敥甯呮爴鎷撮湝鍙岀埥璋佹按鐫＄◣鍚灛椤鸿垳璇寸鏈旂儊鏂挄鍢舵€濈鍙镐笣姝昏倖瀵哄棧鍥涗己浼奸ゲ宸虫澗鑰告€傞閫佸畫璁艰鎼滆墭鎿炲椊鑻忛叆淇楃礌閫熺矡鍍冲婧璇夎們閰歌挏绠楄櫧闅嬮殢缁ラ珦纰庡瞾绌楅亗闅х瀛欐崯绗嬭搼姊攩缂╃悙绱㈤攣鎵€濉屼粬瀹冨ス濉旓拷".split("");
	for(a = 0; a != t[203].length; ++a)
		if(t[203][a].charCodeAt(0) !== 65533) { r[t[203][a]] = 51968 + a;
			e[51968 + a] = t[203][a] }
	t[204] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界嵀鎸炶箣韪忚儙鑻旀姮鍙版嘲閰炲お鎬佹卑鍧嶆憡璐槴婊╁潧妾€鐥版江璋皥鍧︽琚掔⒊鎺㈠徆鐐堡濉樻惇鍫傛　鑶涘攼绯栧€樿汉娣岃稛鐑帍娑涙粩缁﹁悇妗冮€冩窐闄惰濂楃壒钘よ吘鐤艰獖姊墧韪㈤攽鎻愰韫勫暭浣撴浛鍤忔儠娑曞墐灞夊ぉ娣诲～鐢扮敎鎭垟鑵嗘寫鏉¤竣鐪鸿烦璐撮搧甯栧巺鍚儍锟�".split("");
	for(a = 0; a != t[204].length; ++a)
		if(t[204][a].charCodeAt(0) !== 65533) { r[t[204][a]] = 52224 + a;
			e[52224 + a] = t[204][a] }
	t[205] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥眬寤峰仠浜涵鎸鸿墖閫氭閰灣鍚岄摐褰ょ妗舵崊绛掔粺鐥涘伔鎶曞ご閫忓嚫绉冪獊鍥惧緬閫旀秱灞犲湡鍚愬厰婀嶅洟鎺ㄩ鑵胯湑瑜€€鍚炲悲鑷€鎷栨墭鑴遍傅闄€椹┘妞Ε鎷撳斁鎸栧搰铔欐醇濞冪摝琚滄澶栬睂寮咕鐜╅〗涓哥兎瀹岀鎸芥櫄鐨栨儖瀹涘涓囪厱姹帇浜℃瀴缃戝線鏃烘湜蹇樺濞侊拷".split("");
	for(a = 0; a != t[205].length; ++a)
		if(t[205][a].charCodeAt(0) !== 65533) { r[t[205][a]] = 52480 + a;
			e[52480 + a] = t[205][a] }
	t[206] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉穽寰嵄闊﹁繚妗呭洿鍞儫涓烘綅缁磋媷钀庡浼熶吉灏剧含鏈敋鍛崇晱鑳冨杺榄忎綅娓皳灏夋叞鍗槦娓╄殜鏂囬椈绾瑰惢绋崇磰闂棥缈佺摦鎸濊湕娑＄獫鎴戞枴鍗ф彙娌冨帆鍛滈挩涔屾薄璇眿鏃犺姕姊у惥鍚存瘚姝︿簲鎹傚崍鑸炰紞渚潪鎴婇浘鏅ょ墿鍕垮姟鎮熻鏄旂啓鏋愯タ纭掔熃鏅板樆鍚搁敗鐗猴拷".split("");
	for(a = 0; a != t[206].length; ++a)
		if(t[206][a].charCodeAt(0) !== 65533) { r[t[206][a]] = 52736 + a;
			e[52736 + a] = t[206][a] }
	t[207] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界█鎭笇鎮夎啙澶曟儨鐔勭儻婧睈鐘€妾勮甯範濯冲枩閾ｆ礂绯婚殭鎴忕粏鐬庤櫨鍖ｉ湠杈栨殗宄′緺鐙笅鍘﹀鍚撴巰閿ㄥ厛浠欓矞绾ゅ捀璐よ鑸烽棽娑庡鸡瀚屾樉闄╃幇鐚幙鑵洪缇″闄烽檺绾跨浉鍘㈤暥棣欑瑗勬箻涔＄繑绁ヨ鎯冲搷浜」宸锋鍍忓悜璞¤惂纭濋渼鍓婂摦鍤ｉ攢娑堝娣嗘檽锟�".split("");
	for(a = 0; a != t[207].length; ++a)
		if(t[207][a].charCodeAt(0) !== 65533) { r[t[207][a]] = 52992 + a;
			e[52992 + a] = t[207][a] }
	t[208] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉皬瀛濇牎鑲栧暩绗戞晥妤斾簺姝囪潕闉嬪崗鎸熸惡閭枩鑳佽皭鍐欐鍗歌煿鎳堟硠娉昏阿灞戣柂鑺攲娆ｈ緵鏂板炕蹇冧俊琛呮槦鑵ョ尒鎯哄叴鍒戝瀷褰㈤偄琛岄啋骞告潖鎬у鍏勫嚩鑳稿寛姹归泟鐔婁紤淇緸鏈藉梾閿堢琚栫唬澧熸垖闇€铏氬槝椤诲緪璁歌搫閰楀彊鏃簭鐣滄仱绲┛缁画杞╁枾瀹ｆ偓鏃嬬巹锟�".split("");
	for(a = 0; a != t[208].length; ++a)
		if(t[208][a].charCodeAt(0) !== 65533) { r[t[208][a]] = 53248 + a;
			e[53248 + a] = t[208][a] }
	t[209] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介€夌櫍鐪╃粴闈磋枦瀛︾┐闆鍕嬬啅寰棳璇㈠椹贰娈夋睕璁閫婅繀鍘嬫娂楦﹂腑鍛€涓娊鐗欒殰宕栬娑泤鍝戜簹璁剁剦鍜介槈鐑熸饭鐩愪弗鐮旇湌宀╁欢瑷€棰滈槑鐐庢部濂勬帺鐪艰婕旇壋鍫扮嚂鍘岀牃闆佸攣褰︾劙瀹磋皻楠屾畠澶腐绉ф潹鎵蒋鐤＄緤娲嬮槼姘т话鐥掑吇鏍锋季閭€鑵板鐟讹拷".split("");
	for(a = 0; a != t[209].length; ++a)
		if(t[209][a].charCodeAt(0) !== 65533) { r[t[209][a]] = 53504 + a;
			e[53504 + a] = t[209][a] }
	t[210] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥憞灏ч仴绐戣埃濮氬挰鑸€鑽鑰€妞板檸鑰剁埛閲庡喍涔熼〉鎺栦笟鍙舵洺鑵嬪娑蹭竴澹瑰尰鎻栭摫渚濅紛琛ｉ澶烽仐绉讳华鑳扮枒娌傚疁濮ㄥ綕妞呰殎鍊氬凡涔欑煟浠ヨ壓鎶戞槗閭戝惫浜垮焦鑷嗛€歌倓鐤害瑁旀剰姣呭繂涔夌泭婧㈣璁皧璇戝紓缈肩繉缁庤尩鑽洜娈烽煶闃村Щ鍚熼摱娣瘏楗肮寮曢殣锟�".split("");
	for(a = 0; a != t[210].length; ++a)
		if(t[210][a].charCodeAt(0) !== 65533) { r[t[210][a]] = 53760 + a;
			e[53760 + a] = t[210][a] }
	t[211] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉嵃鑻辨ū濠撮拱搴旂绩鑾硅悿钀ヨ崸铦囪繋璧㈢泩褰遍纭槧鍝熸嫢浣ｈ噧鐥堝焊闆嶈笂铔瑰拸娉虫秾姘告伩鍕囩敤骞戒紭鎮犲咖灏ょ敱閭搥鐘规补娓搁厜鏈夊弸鍙充綉閲夎鍙堝辜杩傛筏浜庣泜姒嗚櫈鎰氳垎浣欎繛閫鹃奔鎰夋笣娓旈殔浜堝ū闆ㄤ笌灞跨瀹囪缇界帀鍩熻妺閮佸悂閬囧柣宄尽鎰堟鐙辫偛瑾夛拷".split("");
	for(a = 0; a != t[211].length; ++a)
		if(t[211][a].charCodeAt(0) !== 65533) { r[t[211][a]] = 54016 + a;
			e[54016 + a] = t[211][a] }
	t[212] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥荡瀵撹棰勮鲍椹赋娓婂啢鍏冨灒琚佸師鎻磋緯鍥憳鍦嗙尶婧愮紭杩滆嫅鎰挎€ㄩ櫌鏇扮害瓒婅穬閽ュ渤绮ゆ湀鎮﹂槄鑰樹簯閮у寑闄ㄥ厑杩愯暣閰濇檿闊靛瓡鍖濈牳鏉傛牻鍝夌伨瀹拌浇鍐嶅湪鍜辨敀鏆傝禐璧冭剰钁伃绯熷嚳钘绘灒鏃╂尽铓よ簛鍣€犵殏鐏剁嚗璐ｆ嫨鍒欐辰璐兼€庡鎲庢浘璧犳墡鍠虫福鏈涧锟�".split("");
	for(a = 0; a != t[212].length; ++a)
		if(t[212][a].charCodeAt(0) !== 65533) { r[t[212][a]] = 54272 + a;
			e[54272 + a] = t[212][a] }
	t[213] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介摗闂哥湪鏍呮Θ鍜嬩箥鐐歌瘓鎽樻枊瀹呯獎鍊哄鐬绘瑭圭矘娌剧洀鏂╄緱宕睍铇告爤鍗犳垬绔欐箾缁芥绔犲桨婕冲紶鎺屾定鏉栦笀甯愯处浠楄儉鐦撮殰鎷涙槶鎵炬布璧电収缃╁厗鑲囧彫閬姌鍝茶洶杈欒€呴敆钄楄繖娴欑弽鏂熺湡鐢勭牕鑷昏礊閽堜睛鏋曠柟璇婇渿鎸晣闃佃捀鎸ｇ潄寰佺嫲浜夋€旀暣鎷鏀匡拷".split("");
	for(a = 0; a != t[213].length; ++a)
		if(t[213][a].charCodeAt(0) !== 65533) { r[t[213][a]] = 54528 + a;
			e[54528 + a] = t[213][a] }
	t[214] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉抚鐥囬儜璇佽姖鏋濇敮鍚辫湗鐭ヨ偄鑴傛眮涔嬬粐鑱岀洿妞嶆畺鎵у€间緞鍧€鎸囨瓒惧彧鏃ㄧ焊蹇楁寶鎺疯嚦鑷寸疆甯滃硻鍒舵櫤绉╃璐ㄧ倷鐥旀粸娌荤獟涓泤蹇犻挓琛风粓绉嶈偪閲嶄徊浼楄垷鍛ㄥ窞娲茶瘜绮ヨ酱鑲樺笟鍜掔毐瀹欐樇楠ょ彔鏍洓鏈辩尓璇歌瘺閫愮鐑涚叜鎷勭灘鍢变富钁楁煴鍔╄泙璐摳绛戯拷".split("");
	for(a = 0; a != t[214].length; ++a)
		if(t[214][a].charCodeAt(0) !== 65533) { r[t[214][a]] = 54784 + a;
			e[54784 + a] = t[214][a] }
	t[215] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟戒綇娉ㄧ椹绘姄鐖嫿涓撶爾杞挵璧氱瘑妗╁簞瑁呭鎾炲．鐘舵閿ヨ拷璧樺潬缂€璋嗗噯鎹夋嫏鍗撴鐞㈣寔閰屽晞鐫€鐏兼祳鍏瑰挩璧勫Э婊嬫穭瀛滅传浠旂苯婊撳瓙鑷笉瀛楅瑑妫曡釜瀹楃患鎬荤旱閭硅蛋濂忔弽绉熻冻鍗掓棌绁栬瘏闃荤粍閽荤簜鍢撮唹鏈€缃皧閬垫槰宸︿綈鏌炲仛浣滃潗搴э拷锟斤拷锟斤拷锟�".split("");
	for(a = 0; a != t[215].length; ++a)
		if(t[215][a].charCodeAt(0) !== 65533) {
			r[t[215][a]] = 55040 + a;
			e[55040 + a] = t[215][a]
		}
	t[216] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟戒簫涓屽厐涓愬豢鍗呬笗浜樹笧楝插鍣╀辅绂轰缚鍖曚箛澶埢鍗皭鍥熻儰棣楁瘬鐫鹃紬涓朵簾榧愪箿涔╀簱鑺堝瓫鍟槒浠勫帊鍘濆帲鍘ュ幃闈ヨ禎鍖氬彽鍖﹀尞鍖捐禍鍗﹀崳鍒傚垐鍒庡埈鍒冲埧鍓€鍓屽墳鍓″墱钂壗鍔傚妬鍔愬姄鍐傜綌浜讳粌浠変粋浠ㄤ弧浠粸浼涗怀浼饯浠典讥浼т級浼綖浣ф敻浣氫綕锟�".split("");
	for(a = 0; a != t[216].length; ++a)
		if(t[216][a].charCodeAt(0) !== 65533) { r[t[216][a]] = 55296 + a;
			e[55296 + a] = t[216][a] }
	t[217] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟戒綗浣椾疾浼戒蕉浣翠緫渚変緝渚忎骄浣讳惊浣间粳渚斾喀淇ㄤ开淇呬繗淇ｄ繙淇戜繜淇稿€╁亴淇冲€€忓€€烤鍊滃€屽€ュ€ㄥ伨鍋冨仌鍋堝亷鍋伝鍌ュ偋鍌╁偤鍍栧剢鍍儸鍍﹀儺鍎囧剫浠濇敖浣樹渐淇庨緺姹嗙贝鍏方榛夐鍐佸鍕瑰實瑷囧寪鍑鍏曚籂鍏栦撼琛ⅳ浜佃剶瑁掔瀣磋爟缇稿啱鍐卞喗鍐硷拷".split("");
	for(a = 0; a != t[217].length; ++a)
		if(t[217][a].charCodeAt(0) !== 65533) { r[t[217][a]] = 55552 + a;
			e[55552 + a] = t[217][a] }
	t[218] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉噰鍐栧啟鍐ヨ疇璁﹁璁璁佃璇傝瘍璇嬭瘡璇庤瘨璇撹瘮璇栬瘶璇欒瘻璇熻癄璇よ璇╄璇拌璇惰璇艰璋€璋傝皠璋囪皩璋忚皯璋掕皵璋曡皷璋欒皼璋樿皾璋熻盃璋¤哎璋ц蔼璋爱璋安璋宠暗璋跺崺鍗洪槤闃㈤槨闃遍槳闃介樇闄傞檳闄旈櫉闄ч櫖闄查櫞闅堥殟闅楅毎閭楅倹閭濋倷閭偂閭撮偝閭堕偤锟�".split("");
	for(a = 0; a != t[218].length; ++a)
		if(t[218][a].charCodeAt(0) !== 65533) { r[t[218][a]] = 55808 + a;
			e[55808 + a] = t[218][a] }
	t[219] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介偢閭伴儚閮呴偩閮愰儎閮囬儞閮﹂儮閮滈儣閮涢儷閮兙閯勯劉閯為劊閯遍劘閯归厓閰嗗垗濂傚姠鍔姯鍔惧摽鍕愬嫋鍕板彑鐕煃寤村嚨鍑奸鍘跺紒鐣氬矾鍧屽灘鍨″【澧煎澹戝湬鍦湭鍦冲湽鍦湳鍧滃溁鍧傚潻鍨呭潾鍨嗗澕鍧诲潹鍧澏鍧冲灜鍨ゅ瀸鍨插煆鍨у灤鍨撳灎鍩曞煒鍩氬煓鍩掑灨鍩村煰鍩稿煠鍩濓拷".split("");
	for(a = 0; a != t[219].length; ++a)
		if(t[219][a].charCodeAt(0) !== 65533) { r[t[219][a]] = 56064 + a;
			e[56064 + a] = t[219][a] }
	t[220] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉爧鍫嶅熃鍩爛鍫炲牂濉勫牋濉ュ‖澧佸澧氬棣ㄩ紮鎳胯壒鑹借壙鑺忚妸鑺ㄨ妱鑺庤姂鑺楄姍鑺姼鑺捐姲鑻堣媻鑻ｈ姌鑺疯姰鑻嬭媽鑻佽姪鑺磋姟鑺姛鑻勮嫀鑺よ嫛鑼夎嫹鑻よ審鑼囪嫓鑻磋嫆鑻樿寣鑻昏嫇鑼戣寶鑼嗚寯鑼曡嫚鑻曡寽鑽戣崨鑽滆寛鑾掕尲鑼磋尡鑾涜崬鑼崗鑽囪崈鑽熻崁鑼楄崰鑼尯鑼宠崷鑽ワ拷".split("");
	for(a = 0; a != t[220].length; ++a)
		if(t[220][a].charCodeAt(0) !== 65533) { r[t[220][a]] = 56320 + a;
			e[56320 + a] = t[220][a] }
	t[221] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借崹鑼涜崺鑽崻鑽嵁鑾拌嵏鑾宠幋鑾犺帾鑾撹帨鑾呰嵓鑾惰帺鑽借幐鑽昏帢鑾炶帹鑾鸿幖鑿佽悂鑿ヨ彉鍫囪悩钀嬭彎鑿借彇钀滆惛钀戣悊鑿旇彑钀忚悆鑿歌徆鑿弲鑿€钀﹁彴鑿¤憸钁戣憵钁欒懗钂囪拡钁鸿拤钁歌惣钁嗚懇钁惰拰钂庤惐钁搧钃嶈搻钃﹁捊钃撹搳钂胯捄钃犺挕钂硅挻钂楄摜钃ｈ攲鐢嶈敻钃拌敼钄熻敽锟�".split("");
	for(a = 0; a != t[221].length; ++a)
		if(t[221][a].charCodeAt(0) !== 65533) { r[t[221][a]] = 56576 + a;
			e[56576 + a] = t[221][a] }
	t[222] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借晼钄昏摽钃艰暀钑堣暔钑よ暈钑虹灑钑冭暡钑昏枻钖ㄨ枃钖忚暪钖枩钖呰柟钖疯柊钘撹梺钘滆椏铇ц槄铇╄槚铇煎痪寮堝ぜ濂佽€峰濂氬鍖忓阿灏ュ艾灏存墝鎵姛鎶绘媻鎷氭嫍鎷將鎷舵尮鎹嬫崈鎺彾鎹辨嵑鎺庢幋鎹幀鎺婃崺鎺幖鎻叉徃鎻犳徔鎻勬彏鎻庢憭鎻嗘幘鎽呮憗鎼嬫悰鎼犳悓鎼︽悺鎽炴拕鎽挅锟�".split("");
	for(a = 0; a != t[222].length; ++a)
		if(t[222][a].charCodeAt(0) !== 65533) { r[t[222][a]] = 56832 + a;
			e[56832 + a] = t[222][a] }
	t[223] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥懞鎾锋捀鎾欐捄鎿€鎿愭摋鎿ゆ摙鏀夋敟鏀紜蹇掔敊寮戝崯鍙卞徑鍙╁彣鍙诲悞鍚栧悊鍛嬪憭鍛撳憯鍛栧憙鍚″憲鍛欏悾鍚插拏鍜斿懛鍛卞懁鍜氬挍鍜勫懚鍛﹀挐鍝愬挱鍝傚挻鍝掑挧鍜﹀摀鍝斿懖鍜ｅ摃鍜诲捒鍝屽摍鍝氬摐鍜╁挭鍜ゅ摑鍝忓摓鍞涘摟鍞犲摻鍞斿摮鍞㈠敚鍞忓攽鍞у敧鍟у枏鍠靛晧鍟晛鍟曞斂鍟愬敿锟�".split("");
	for(a = 0; a != t[223].length; ++a)
		if(t[223][a].charCodeAt(0) !== 65533) { r[t[223][a]] = 57088 + a;
			e[57088 + a] = t[223][a] }
	t[224] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉敺鍟栧暤鍟跺暦鍞冲敯鍟滃枊鍡掑杻鍠卞柟鍠堝杹鍠熷暰鍡栧枒鍟诲棢鍠藉柧鍠斿枡鍡椃鍡夊槦鍡戝棲鍡棓鍡﹀棟鍡勫棷鍡ュ棽鍡冲棇鍡嶅棬鍡靛棨杈斿槥鍢堝槍鍢佸槫鍢ｅ椌鍢€鍢у槶鍣樺樄鍣楀槵鍣嶅櫌鍣欏櫆鍣屽檾鍤嗗櫎鍣卞櫕鍣诲櫦鍤呭殦鍤洈鍥楀洕鍥″浀鍥浌鍥垮渼鍦婂湁鍦滃笍甯欏笖甯戝副甯诲讣锟�".split("");
	for(a = 0; a != t[224].length; ++a)
		if(t[224][a].charCodeAt(0) !== 65533) { r[t[224][a]] = 57344 + a;
			e[57344 + a] = t[224][a] }
	t[225] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉阜骞勫箶骞涘篂骞″矊灞哄矋宀愬矕宀堝矘宀欏矐宀氬矞宀靛并宀藉铂宀脖宀ｅ硜宀峰硠宄掑长宄嬪偿宕傚磧宕у处宕搐宕炲磫宕涘禈宕惧创宕藉惮宓涘弹宓濆但宓嬪祳宓╁荡宥傚稒宥濊背宥峰穮褰冲椒寰傚緡寰夊緦寰曞緳寰滃鲸寰镜寰艰、褰＄姯鐘扮姶鐘风姼鐙冪媮鐙庣媿鐙掔嫧鐙嫨鐙茬嫶鐙风寔鐙崇寖鐙猴拷".split("");
	for(a = 0; a != t[225].length; ++a)
		if(t[225][a].charCodeAt(0) !== 65533) { r[t[225][a]] = 57600 + a;
			e[57600 + a] = t[225][a] }
	t[226] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界嫽鐚楃寭鐚＄寠鐚炵対鐚曠將鐚圭尌鐚尭鐚辩崘鐛嶇崡鐛犵崿鐛嵕鑸涘ぅ椋уい澶傞ィ楗чエ楗╅オ楗ガ楗撮シ楗介棣勯棣婇棣愰棣撻棣曞簚搴戝簨搴栧亥搴犲汗搴靛壕搴宠祿寤掑粦寤涘花寤喓蹇勫繅蹇栧繌鎬冨慨鎬勫俊蹇ゅ烤鎬呮€嗗开蹇扛鎬欐€垫€︽€涙€忔€嶆€╂€€婃€挎€℃伕鎭规伝鎭烘亗锟�".split("");
	for(a = 0; a != t[226].length; ++a)
		if(t[226][a].charCodeAt(0) !== 65533) { r[t[226][a]] = 57856 + a;
			e[57856 + a] = t[226][a] }
	t[227] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥仾鎭芥倴鎮氭偔鎮濇們鎮掓倢鎮涙儸鎮绘偙鎯濇儤鎯嗘儦鎮存劆鎰︽剷鎰ｆ兇鎰€鎰庢劔鎱婃叺鎲啍鎲ф喎鎳旀嚨蹇濋毘闂╅棲闂遍棾闂甸椂闂奸椌闃冮槃闃嗛槇闃婇構闃岄槏闃忛槖闃曢槚闃楅槞闃氫脯鐖挎垥姘垫睌姹滄眾娌ｆ矃娌愭矓娌屾报姹╂贝姹舵矄娌╂硱娉旀箔娉锋掣娉辨硹娌叉碃娉栨澈娉钞娌辨硴娉尘锟�".split("");
	for(a = 0; a != t[227].length; ++a)
		if(t[227][a].charCodeAt(0) !== 65533) { r[t[227][a]] = 58112 + a;
			e[58112 + a] = t[227][a] }
	t[228] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥垂娲ф磳娴冩祱娲囨磩娲欐磶娲祶娲吹娲氭祻娴掓禂娲虫稇娴稙娑犳禐娑撴稊娴滄禒娴兼担娓氭穱娣呮窞娓庢犊娣犳笐娣︽窛娣欐笘娑笇娑斧婀箮婀翰婀熸簡婀撴箶娓叉弗婀勬粺婧辨簶婊犳辑婊㈡亥婧ф航婧绘悍婊楁捍婊忔簭婊傛簾娼㈡絾娼囨激婕曟还婕级娼嬫酱婕級婕╂緣婢嶆緦娼告讲娼兼胶婵戯拷".split("");
	for(a = 0; a != t[228].length; ++a)
		if(t[228][a].charCodeAt(0) !== 65533) { r[t[228][a]] = 58368 + a;
			e[58368 + a] = t[228][a] }
	t[229] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥繅婢ф竟婢舵總婵℃慨婵炴繝婵€氱€ｇ€涚€圭€电亸鐏炲畝瀹勫畷瀹撳瀹哥敮楠炴惔瀵ゅ瑜板韫囪瑖杈惰繐杩曡骏杩郡杩╄喀杩宠卡閫呴€勯€嬮€﹂€戦€嶉€栭€￠€甸€堕€€亜閬戦亽閬愰仺閬橀仮閬涙毠閬撮伣閭傞倛閭冮倠褰愬綏褰栧綐灏诲挮灞愬睓瀛卞保灞︾炯寮缉寮壌寮奸灞濡冨濡╁Κ濡ｏ拷".split("");
	for(a = 0; a != t[229].length; ++a)
		if(t[229][a].charCodeAt(0) !== 65533) { r[t[229][a]] = 58624 + a;
			e[58624 + a] = t[229][a] }
	t[230] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟藉濮婂Λ濡炲Δ濮掑Σ濡濡惧▍濞嗗濞堝В濮樺Ч濞屽▔濞插ù濞戝ǎ濞撳﹢濠у濠曞濠㈠┑鑳濯涘┓濠哄瀚瀚掑珨濯稿珷瀚ｅ瀚栧瀚樺珳瀣夊瑮瀣栧瀣峰瓈灏曞皽瀛氬瀛冲瓚瀛撳椹甸┓椹搁┖椹块┙楠€楠侀獏楠堥獖楠愰獟楠撻獤楠橀獩楠滈獫楠熼獱楠㈤楠ラ绾熺骸绾ｇ亥绾ㄧ憨锟�".split("");
	for(a = 0; a != t[230].length; ++a)
		if(t[230][a].charCodeAt(0) !== 65533) { r[t[230][a]] = 58880 + a;
			e[58880 + a] = t[230][a] }
	t[231] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界涵绾扮壕缁€缁佺粋缁夌粙缁岀粣缁旂粭缁涚粻缁＄花缁划缁槐缁茬紞缁剁缓缁荤痪缂佺紓缂冪紘缂堢紜缂岀紡缂戠紥缂楃紮缂滅紱缂熺肌缂㈢迹缂ょ讥缂︾姬缂极缂辑缂及缂辩疾缂崇嫉骞虹暱宸涚斁閭曠帋鐜戠幃鐜㈢師鐝忕弬鐝戠幏鐜崇弨鐝夌張鐝ョ彊椤肩悐鐝╃彠鐝炵幒鐝茬悘鐞憶鐞︾惀鐞ㄧ惏鐞惉锟�".split("");
	for(a = 0; a != t[231].length; ++a)
		if(t[231][a].charCodeAt(0) !== 65533) { r[t[231][a]] = 59136 + a;
			e[59136 + a] = t[231][a] }
	t[232] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界悰鐞氱憗鐟滅憲鐟曠憴鐟风懎鐟剧挏鐠庣拃鐠佺拠鐠嬬挒鐠ㄧ挬鐠愮挧鐡掔捄闊煫闊潓鏉撴潪鏉堟潻鏋ユ瀲鏉澇鏋樻灖鏉垫灗鏋炴灜鏋嬫澐鏉兼煱鏍夋煒鏍婃煩鏋版爩鏌欐灥鏌氭灣鏌濇爛鏌冩灨鏌㈡爭鏌佹熃鏍叉牫妗犳　妗庢、妗勬·姊冩牆妗曟ˇ妗佹¨妗€鏍炬妗夋牘姊垫妗存》姊撴～妫傛ギ妫兼妞犳９锟�".split("");
	for(a = 0; a != t[232].length; ++a)
		if(t[232][a].charCodeAt(0) !== 65533) { r[t[232][a]] = 59392 + a;
			e[59392 + a] = t[232][a] }
	t[233] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥い妫版妞佹妫ｆ妤辨す妤犳妤濇妤姒樻ジ妞存姒囨妲庢妤︽ィ妤规姒ф姒Ν妲旀Ρ妲佹妲熸妲犳妲挎ǒ妲妯樻━妲叉﹦妯炬獱姗愭妯垫獛姗规ń妯ㄦ姗兼獞妾愭妾楁鐚风崚娈佹畟娈囨畡娈掓畵娈嶆畾娈涙娈将杞奖杞茶匠杞佃蕉杞歌椒杞硅胶杞艰骄杈佽緜杈勮緡杈嬶拷".split("");
	for(a = 0; a != t[233].length; ++a)
		if(t[233][a].charCodeAt(0) !== 65533) { r[t[233][a]] = 59648 + a;
			e[59648 + a] = t[233][a] }
	t[234] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借緧杈庤緩杈樿練杌庢垕鎴楁垱鎴熸垻鎴℃垾鎴ゆ埇鑷х摨鐡寸摽鐢忕攽鐢撴敶鏃棷鏃版槉鏄欐澆鏄冩槙鏄€鐐呮浄鏄濇槾鏄辨樁鏄佃€嗘櫉鏅旀檨鏅忔櫀鏅℃櫁鏅锋殑鏆屾毀鏆濇毦鏇涙洔鏇︽洨璐茶闯璐惰椿璐借祤璧呰祮璧堣祲璧囪祶璧曡禉瑙囪瑙嬭瑙庤瑙愯鐗姛鐗濈墻鐗壘鐗跨妱鐘嬬妽鐘忕姃鎸堟尣鎺帮拷".split("");
	for(a = 0; a != t[234].length; ++a)
		if(t[234][a].charCodeAt(0) !== 65533) { r[t[234][a]] = 59904 + a;
			e[59904 + a] = t[234][a] }
	t[235] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥惪鎿樿€勬姣虫姣垫姘呮皣姘嗘皪姘曟皹姘欐皻姘℃癌姘ゆ蔼姘叉數鏁曟暙鐗嶇墥鐗栫埌铏㈠垨鑲熻倻鑲撹偧鏈婅偨鑲辫偒鑲偞鑲疯儳鑳ㄨ儵鑳儧鑳傝儎鑳欒儘鑳楁湊鑳濊儷鑳辫兇鑳剭鑴庤儾鑳兼湑鑴掕睔鑴惰劄鑴剺鑴茶厛鑵岃厯鑵磋厵鑵氳叡鑵犺叐鑵艰吔鑵収濉嶅鑶堣唫鑶戞粫鑶ｈ啰鑷屾湨鑷婅喕锟�".split("");
	for(a = 0; a != t[235].length; ++a)
		if(t[235][a].charCodeAt(0) !== 65533) { r[t[235][a]] = 60160 + a;
			e[60160 + a] = t[235][a] }
	t[236] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借噥鑶︽娆锋姝冩瓎姝欓椋掗椋曢椋氭褰€姣傝С鏂愰綉鏂撴柤鏃嗘梽鏃冩棇鏃庢棐鏃栫個鐐滅倴鐐濈偦鐑€鐐风偒鐐辩儴鐑婄剱鐒撶剸鐒劚鐓崇厹鐓ㄧ厖鐓茬厞鐓哥吅鐔樼喅鐔电啫鐔犵嚑鐕旂嚙鐕圭垵鐖ㄧ伂鐒樼叇鐔规埦鎴芥墐鎵堟墘绀荤绁嗙绁涚绁撶绁㈢绁犵ク绁хズ绂呯绂氱Η绂冲繎蹇愶拷".split("");
	for(a = 0; a != t[236].length; ++a)
		if(t[236][a].charCodeAt(0) !== 65533) { r[t[236][a]] = 60416 + a;
			e[60416 + a] = t[236][a] }
	t[237] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟芥€兼仢鎭氭仹鎭佹仚鎭ｆ偒鎰嗘剭鎱濇啯鎲濇噵鎳戞垎鑲€鑱挎矒娉舵芳鐭剁煾鐮€鐮夌牀鐮樼爲鏂牠鐮滅牆鐮圭牶鐮荤牊鐮肩牓鐮牐鐮╃纭纭楃牔纭愮纭岀—纰涚纰氱纰滅ⅰ纰ｇ⒉纰圭ⅴ纾旂纾夌，纾茬纾寸绀ょ绀撮緵榛归换榛肩洷鐪勭湇鐩圭渿鐪堢湚鐪㈢湙鐪湨鐪电湼鐫愮潙鐫囩潈鐫氱潹锟�".split("");
	for(a = 0; a != t[237].length; ++a)
		if(t[237][a].charCodeAt(0) !== 65533) { r[t[237][a]] = 60672 + a;
			e[60672 + a] = t[237][a] }
	t[238] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界潰鐫ョ澘鐬嶇澖鐬€鐬岀瀾鐬熺灎鐬扮灥鐬界敽鐣€鐣庣晪鐣堢暃鐣茬暪鐤冪綐缃＄綗瑭堢建缃寸奖缃圭緛缃剧泹鐩ヨ牪閽呴拞閽囬拫閽婇拰閽嶉拸閽愰挃閽楅挄閽氶挍閽滈挘閽ら挮閽挱閽挴閽伴挷閽撮挾閽烽捀閽归捄閽奸捊閽块搫閾堥搲閾婇搵閾岄搷閾庨搻閾戦搾閾曢摉閾楅摍閾橀摏閾為摕閾犻摙閾ら摜閾ч摠閾拷".split("");
	for(a = 0; a != t[238].length; ++a)
		if(t[238][a].charCodeAt(0) !== 65533) { r[t[238][a]] = 60928 + a;
			e[60928 + a] = t[238][a] }
	t[239] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介摡閾摦閾摮閾撮摰閾烽摴閾奸摻閾块攦閿傞攩閿囬攭閿婇攳閿庨攺閿掗敁閿旈敃閿栭敇閿涢敐閿為敓閿㈤敧閿敥閿敱閿查敶閿堕敺閿搁敿閿鹃斂闀傞數闀勯晠闀嗛晧闀岄晭闀忛晵闀撻晹闀栭晽闀橀暀闀涢暈闀熼暆闀￠暍闀ら暐闀﹂暓闀ㄩ暕闀暙闀暞闀遍暡闀抽敽鐭х煬闆夌绉В绉▎宓囩▋绋傜绋旓拷".split("");
	for(a = 0; a != t[239].length; ++a)
		if(t[239][a].charCodeAt(0) !== 65533) { r[t[239][a]] = 61184 + a;
			e[61184 + a] = t[239][a] }
	t[240] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界ü绋风榛忛Ε绌扮殘鐨庣殦鐨欑殼鐡炵摖鐢笭楦㈤辅楦╅釜楦脯楦查副楦堕父楦烽腹楦洪妇楣侀箓楣勯箚楣囬箞楣夐箣楣岄箮楣戦箷楣楅箽楣涢箿楣為梗楣﹂恭楣ㄩ供楣公楣贡楣钩鐤掔枖鐤栫枲鐤濈柆鐤ｇ柍鐤寸柛鐥勭柋鐤扮梼鐥傜棖鐥嶇棧鐥ㄧ棪鐥ょ棲鐥х槂鐥辩椉鐥跨槓鐦€鐦呯槍鐦楃槉鐦ョ槝鐦曠槞锟�".split("");
	for(a = 0; a != t[240].length; ++a)
		if(t[240][a].charCodeAt(0) !== 65533) { r[t[240][a]] = 61440 + a;
			e[61440 + a] = t[240][a] }
	t[241] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界槢鐦肩槩鐦犵檧鐦槹鐦跨樀鐧冪樉鐦崇檷鐧炵檾鐧滅櫀鐧櫙缈婄绌哥┕绐€绐嗙獔绐曠绐犵绐ㄧ绐宠·琛╄〔琛借】琚傝ⅱ瑁嗚⒎琚艰瑁㈣瑁ｈ％瑁辫瑁艰（瑁捐０瑜¤瑜撹瑜婅ご瑜ざ瑗佽ウ瑗荤枊鑳ョ毑鐨寸煖鑰掕€旇€栬€滆€犺€㈣€ヨ€﹁€ц€╄€ㄨ€辫€嬭€佃亙鑱嗚亶鑱掕仼鑱辫椤搁棰冿拷".split("");
	for(a = 0; a != t[241].length; ++a)
		if(t[241][a].charCodeAt(0) !== 65533) { r[t[241][a]] = 61696 + a;
			e[61696 + a] = t[241][a] }
	t[242] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介棰岄棰忛棰氶棰為棰￠ⅱ棰ラⅵ铏嶈檾铏櫘铏胯櫤铏艰櫥铓ㄨ殟铓嬭毈铓濊毀铓ｈ毆铓撹毄铓惰泟铓佃泿铓拌毢铓辫毌铔夎洀铓磋洨铔辫洸铔洺铔愯湏铔炶洿铔熻洏铔戣渻铚囪浉铚堣湂铚嶈湁铚ｈ溁铚炶湧铚湚铚捐潏铚磋湵铚╄湻铚胯瀭铚㈣澖铦捐澔铦犺澃铦岃澁铻嬭潛铦ｈ澕铦よ潤铦ヨ灀铻灗锜掞拷".split("");
	for(a = 0; a != t[242].length; ++a)
		if(t[242][a].charCodeAt(0) !== 65533) { r[t[242][a]] = 61952 + a;
			e[61952 + a] = t[242][a] }
	t[243] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借焼铻堣瀰铻灄铻冭灚锜ヨ灛铻佃灣锜嬭煋铻借煈锜€锜婅煕锜煚锜爾锠撹熅锠婅牄锠¤牴锠肩级缃傜絼缃呰垚绔虹绗堢瑑绗勭瑫绗婄绗忕瓏绗哥绗欑绗辩瑺绗ョ绗崇绗炵瓨绛氱瓍绛电瓕绛濈瓲绛绛㈢绛辩異绠︾绠哥绠濈绠呯绠滅绠绡戠瘉绡岀瘽绡氱绡︾绨岀绡肩皬绨栫皨锟�".split("");
	for(a = 0; a != t[243].length; ++a)
		if(t[243][a].charCodeAt(0) !== 65533) { r[t[243][a]] = 62208 + a;
			e[62208 + a] = t[243][a] }
	t[244] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界盁绨唉绨哥眮绫€鑷捐垇鑸傝垊鑷鑸¤垻鑸ｈ埈鑸埁鑸埜鑸昏埑鑸磋埦鑹勮墘鑹嬭墢鑹氳墴鑹ㄨ【琚呰瑁樿瑗炵緷缇熺晶缇景缇茬奔鏁夌矐绮濈矞绮炵并绮茬布绮界硜绯囩硨绯嶇硤绯呯硹绯ㄨ壆鏆ㄧ究缈庣繒缈ョ俊缈︾咯缈砍绯哥捣缍︾懂绻囩簺楹搁捍璧宠秳瓒旇稇瓒辫掸璧眹璞夐厞閰愰厧閰忛叅锟�".split("");
	for(a = 0; a != t[244].length; ++a)
		if(t[244][a].charCodeAt(0) !== 65533) { r[t[244][a]] = 62464 + a;
			e[62464 + a] = t[244][a] }
	t[245] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介參閰￠叞閰╅叝閰介吘閰查叴閰归唽閱呴啇閱嶉啈閱㈤啠閱啳閱啹閱甸喆閱鸿睍楣捐陡璺竻韫欒供瓒佃犊瓒艰逗璺勮窎璺楄窔璺炶穾璺忚窙璺嗚番璺疯犯璺ｈ饭璺昏筏韪夎方韪旇笣韪熻脯韪福韪负韫€韪硅傅韪借副韫夎箒韫傝箲韫掕箠韫拌苟韫艰汞韫磋簠韬忚簲韬愯簻韬炶备璨傝矈璨呰矘璨旀枦瑙栬瑙氳锟�".split("");
	for(a = 0; a != t[245].length; ++a)
		if(t[245][a].charCodeAt(0) !== 65533) { r[t[245][a]] = 62720 + a;
			e[62720 + a] = t[245][a] }
	t[246] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟借Д瑙Н瑷捐闈撻洨闆抽洴闇嗛渷闇堥湉闇庨湭闇湴闇鹃線榫冮緟榫嗛緡榫堥緣榫婇緦榛鹃紜榧嶉毠闅奸毥闆庨洅鐬块洜閵庨姰閶堥尵閸強閹忛惥閼笨椴傞矃椴嗛矅椴堢ǎ椴嬮矌椴愰矐椴掗矓椴曢矚椴涢矠椴熼矤椴￠并椴ｉ播椴﹂钵椴ㄩ博椴箔椴舶椴遍膊椴抽泊椴甸捕椴烽埠椴婚布椴介硠槌呴硢槌囬硦槌嬶拷".split("");
	for(a = 0; a != t[246].length; ++a)
		if(t[246][a].charCodeAt(0) !== 65533) { r[t[246][a]] = 62976 + a;
			e[62976 + a] = t[246][a] }
	t[247] = "锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟介硨槌嶉硯槌忛硱槌撻硵槌曢硹槌橀硻槌滈碀槌熼尝闈奸瀰闉戦瀿闉旈灟闉灒闉查灤楠遍楠烽箻楠堕楠奸珌楂€楂呴珎楂嬮珜楂戦瓍榄冮瓏榄夐瓐榄嶉瓚椋ㄩ椁楗旈珶楂￠楂楂婚楂归瑘楝忛瑩楝熼楹介壕绺婚簜楹囬簣楹嬮簰閺栭簼楹熼粵榛滈粷榛犻粺榛㈤哗榛ч互榛化榧㈤棘榧脊榧烽冀榧鹃絼锟�".split("");
	for(a = 0; a != t[247].length; ++a)
		if(t[247][a].charCodeAt(0) !== 65533) { r[t[247][a]] = 63232 + a;
			e[63232 + a] = t[247][a] }
	return { enc: r, dec: e }
}();
cptable[10029] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~脛膧膩脡膭脰脺谩膮膶盲膷膯膰茅殴藕膸铆膹膾膿臇贸臈么枚玫煤臍臎眉鈥犅澳樎Ｂр€⒙睹熉┾劉臋篓鈮犇Ｄ墹鈮ツ垛垈鈭懪偰荒寄侥灸鼓号吪喤兟垰艅艊鈭喡烩€β犈埮惷暸懪屸€撯€斺€溾€濃€樷€櫭封棅艒艛艜艠鈥光€号櫯柵椗犫€氣€炁∨毰浢伵づッ嵟脚九撁斉毰芭迸才趁澝侥放慌伵寄⑺�",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[10079] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~脛脜脟脡脩脰脺谩脿芒盲茫氓莽茅猫锚毛铆矛卯茂帽贸貌么枚玫煤霉没眉脻掳垄拢搂鈥⒙睹熉┾劉麓篓鈮犆喢樷垶卤鈮も墺楼碌鈭傗垜鈭徬€鈭衡劍忙酶驴隆卢鈭毱掆増鈭喡烩€β犆€脙脮艗艙鈥撯€斺€溾€濃€樷€櫭封棅每鸥鈦劼っ惷懊灻久铰封€氣€炩€懊偯娒伱嬅埫嵜幟徝屆撁旓拷脪脷脹脵谋藛藴炉藰藱藲赂藵藳藝",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
cptable[10081] = function() { var e = "\0　�\b\t\n\x0B\f\r !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~脛脜脟脡脩脰脺谩脿芒盲茫氓莽茅猫锚毛铆矛卯茂帽贸貌么枚玫煤霉没眉鈥犅奥⒙Ｂр€⒙睹熉┾劉麓篓鈮犆喢樷垶卤鈮も墺楼碌鈭傗垜鈭徬€鈭衡劍忙酶驴隆卢鈭毱掆増鈭喡烩€β犆€脙脮艗艙鈥撯€斺€溾€濃€樷€櫭封棅每鸥臑臒陌谋艦艧鈥÷封€氣€炩€懊偯娒伱嬅埫嵜幟徝屆撁旓拷脪脷脹脵锟剿喫溌標櫵毬杆澦浰�",
		r = [],
		t = {}; for(var a = 0; a != e.length; ++a) { if(e.charCodeAt(a) !== 65533) t[e.charAt(a)] = a;
		r[a] = e.charAt(a) } return { enc: t, dec: r } }();
if(typeof module !== "undefined" && module.exports && typeof DO_NOT_EXPORT_CODEPAGE === "undefined") module.exports = cptable;
(function(e, r) { "use strict"; if(typeof cptable === "undefined") { if(typeof require !== "undefined") { var t = cptable; if(typeof module !== "undefined" && module.exports && typeof DO_NOT_EXPORT_CODEPAGE === "undefined") module.exports = r(t);
			else e.cptable = r(t) } else throw new Error("cptable not found") } else cptable = r(cptable) })(this, function(e) { "use strict"; var r = { 1200: "utf16le", 1201: "utf16be", 12000: "utf32le", 12001: "utf32be", 16969: "utf64le", 20127: "ascii", 65000: "utf7", 65001: "utf8" }; var t = [874, 1250, 1251, 1252, 1253, 1254, 1255, 1256, 1e4]; var a = [932, 936, 949, 950]; var n = [65001]; var i = {}; var s = {}; var f = {}; var o = {}; var l = function R(e) { return String.fromCharCode(e) }; var c = function D(e) { return e.charCodeAt(0) }; var h = typeof Buffer !== "undefined"; if(h) { if(!Buffer.from) Buffer.from = function(e, r) { return r ? new Buffer(e, r) : new Buffer(e) }; if(!Buffer.allocUnsafe) Buffer.allocUnsafe = function(e) { return new Buffer(e) }; var u = 1024,
			d = Buffer.allocUnsafe(u); var p = function O(e) { var r = Buffer.allocUnsafe(65536); for(var t = 0; t < 65536; ++t) r[t] = 0; var a = Object.keys(e),
				n = a.length; for(var i = 0, s = a[i]; i < n; ++i) { if(!(s = a[i])) continue;
				r[s.charCodeAt(0)] = e[s] } return r }; var v = function F(r) { var t = p(e[r].enc); return function a(e, r) { var a = e.length; var n, i = 0,
					s = 0,
					f = 0,
					o = 0; if(typeof e === "string") { n = Buffer.allocUnsafe(a); for(i = 0; i < a; ++i) n[i] = t[e.charCodeAt(i)] } else if(Buffer.isBuffer(e)) { n = Buffer.allocUnsafe(2 * a);
					s = 0; for(i = 0; i < a; ++i) { f = e[i]; if(f < 128) n[s++] = t[f];
						else if(f < 224) { n[s++] = t[((f & 31) << 6) + (e[i + 1] & 63)];++i } else if(f < 240) { n[s++] = t[((f & 15) << 12) + ((e[i + 1] & 63) << 6) + (e[i + 2] & 63)];
							i += 2 } else { o = ((f & 7) << 18) + ((e[i + 1] & 63) << 12) + ((e[i + 2] & 63) << 6) + (e[i + 3] & 63);
							i += 3; if(o < 65536) n[s++] = t[o];
							else { o -= 65536;
								n[s++] = t[55296 + (o >> 10 & 1023)];
								n[s++] = t[56320 + (o & 1023)] } } } n = n.slice(0, s) } else { n = Buffer.allocUnsafe(a); for(i = 0; i < a; ++i) n[i] = t[e[i].charCodeAt(0)] } if(!r || r === "buf") return n; if(r !== "arr") return n.toString("binary"); return [].slice.call(n) } }; var g = function P(r) { var t = e[r].dec; var a = Buffer.allocUnsafe(131072),
				n = 0,
				i = ""; for(n = 0; n < t.length; ++n) { if(!(i = t[n])) continue; var s = i.charCodeAt(0);
				a[2 * n] = s & 255;
				a[2 * n + 1] = s >> 8 } return function f(e) { var r = e.length,
					t = 0,
					n = 0; if(2 * r > u) { u = 2 * r;
					d = Buffer.allocUnsafe(u) } if(Buffer.isBuffer(e)) { for(t = 0; t < r; t++) { n = 2 * e[t];
						d[2 * t] = a[n];
						d[2 * t + 1] = a[n + 1] } } else if(typeof e === "string") { for(t = 0; t < r; t++) { n = 2 * e.charCodeAt(t);
						d[2 * t] = a[n];
						d[2 * t + 1] = a[n + 1] } } else { for(t = 0; t < r; t++) { n = 2 * e[t];
						d[2 * t] = a[n];
						d[2 * t + 1] = a[n + 1] } } return d.slice(0, 2 * r).toString("ucs2") } }; var m = function N(r) { var t = e[r].enc; var a = Buffer.allocUnsafe(131072); for(var n = 0; n < 131072; ++n) a[n] = 0; var i = Object.keys(t); for(var s = 0, f = i[s]; s < i.length; ++s) { if(!(f = i[s])) continue; var o = f.charCodeAt(0);
				a[2 * o] = t[f] & 255;
				a[2 * o + 1] = t[f] >> 8 } return function l(e, r) { var t = e.length,
					n = Buffer.allocUnsafe(2 * t),
					i = 0,
					s = 0,
					f = 0,
					o = 0,
					l = 0; if(typeof e === "string") { for(i = o = 0; i < t; ++i) { s = e.charCodeAt(i) * 2;
						n[o++] = a[s + 1] || a[s]; if(a[s + 1] > 0) n[o++] = a[s] } n = n.slice(0, o) } else if(Buffer.isBuffer(e)) { for(i = o = 0; i < t; ++i) { l = e[i]; if(l < 128) s = l;
						else if(l < 224) { s = ((l & 31) << 6) + (e[i + 1] & 63);++i } else if(l < 240) { s = ((l & 15) << 12) + ((e[i + 1] & 63) << 6) + (e[i + 2] & 63);
							i += 2 } else { s = ((l & 7) << 18) + ((e[i + 1] & 63) << 12) + ((e[i + 2] & 63) << 6) + (e[i + 3] & 63);
							i += 3 } if(s < 65536) { s *= 2;
							n[o++] = a[s + 1] || a[s]; if(a[s + 1] > 0) n[o++] = a[s] } else { f = s - 65536;
							s = 2 * (55296 + (f >> 10 & 1023));
							n[o++] = a[s + 1] || a[s]; if(a[s + 1] > 0) n[o++] = a[s];
							s = 2 * (56320 + (f & 1023));
							n[o++] = a[s + 1] || a[s]; if(a[s + 1] > 0) n[o++] = a[s] } } n = n.slice(0, o) } else { for(i = o = 0; i < t; i++) { s = e[i].charCodeAt(0) * 2;
						n[o++] = a[s + 1] || a[s]; if(a[s + 1] > 0) n[o++] = a[s] } } if(!r || r === "buf") return n; if(r !== "arr") return n.toString("binary"); return [].slice.call(n) } }; var b = function L(r) { var t = e[r].dec; var a = Buffer.allocUnsafe(131072),
				n = 0,
				i, s = 0,
				f = 0,
				o = 0; for(o = 0; o < 65536; ++o) { a[2 * o] = 255;
				a[2 * o + 1] = 253 } for(n = 0; n < t.length; ++n) { if(!(i = t[n])) continue;
				s = i.charCodeAt(0);
				f = 2 * n;
				a[f] = s & 255;
				a[f + 1] = s >> 8 } return function l(e) { var r = e.length,
					t = Buffer.allocUnsafe(2 * r),
					n = 0,
					i = 0,
					s = 0; if(Buffer.isBuffer(e)) { for(n = 0; n < r; n++) { i = 2 * e[n]; if(a[i] === 255 && a[i + 1] === 253) { i = 2 * ((e[n] << 8) + e[n + 1]);++n } t[s++] = a[i];
						t[s++] = a[i + 1] } } else if(typeof e === "string") { for(n = 0; n < r; n++) { i = 2 * e.charCodeAt(n); if(a[i] === 255 && a[i + 1] === 253) { i = 2 * ((e.charCodeAt(n) << 8) + e.charCodeAt(n + 1));++n } t[s++] = a[i];
						t[s++] = a[i + 1] } } else { for(n = 0; n < r; n++) { i = 2 * e[n]; if(a[i] === 255 && a[i + 1] === 253) { i = 2 * ((e[n] << 8) + e[n + 1]);++n } t[s++] = a[i];
						t[s++] = a[i + 1] } } return t.slice(0, s).toString("ucs2") } };
		i[65001] = function M(e) { if(typeof e === "string") return M(e.split("").map(c)); var r = e.length,
				t = 0,
				a = 0; if(4 * r > u) { u = 4 * r;
				d = Buffer.allocUnsafe(u) } var n = 0; if(r >= 3 && e[0] == 239)
				if(e[1] == 187 && e[2] == 191) n = 3; for(var i = 1, s = 0, f = 0; n < r; n += i) { i = 1;
				f = e[n]; if(f < 128) t = f;
				else if(f < 224) { t = (f & 31) * 64 + (e[n + 1] & 63);
					i = 2 } else if(f < 240) { t = ((f & 15) << 12) + (e[n + 1] & 63) * 64 + (e[n + 2] & 63);
					i = 3 } else { t = (f & 7) * 262144 + ((e[n + 1] & 63) << 12) + (e[n + 2] & 63) * 64 + (e[n + 3] & 63);
					i = 4 } if(t < 65536) { d[s++] = t & 255;
					d[s++] = t >> 8 } else { t -= 65536;
					a = 55296 + (t >> 10 & 1023);
					t = 56320 + (t & 1023);
					d[s++] = a & 255;
					d[s++] = a >>> 8;
					d[s++] = t & 255;
					d[s++] = t >>> 8 & 255 } } return d.slice(0, s).toString("ucs2") };
		s[65001] = function U(e, r) { if(h && Buffer.isBuffer(e)) { if(!r || r === "buf") return e; if(r !== "arr") return e.toString("binary"); return [].slice.call(e) } var t = e.length,
				a = 0,
				n = 0,
				i = 0; var s = typeof e === "string"; if(4 * t > u) { u = 4 * t;
				d = Buffer.allocUnsafe(u) } for(var f = 0; f < t; ++f) { a = s ? e.charCodeAt(f) : e[f].charCodeAt(0); if(a <= 127) d[i++] = a;
				else if(a <= 2047) { d[i++] = 192 + (a >> 6);
					d[i++] = 128 + (a & 63) } else if(a >= 55296 && a <= 57343) { a -= 55296;++f;
					n = (s ? e.charCodeAt(f) : e[f].charCodeAt(0)) - 56320 + (a << 10);
					d[i++] = 240 + (n >>> 18 & 7);
					d[i++] = 144 + (n >>> 12 & 63);
					d[i++] = 128 + (n >>> 6 & 63);
					d[i++] = 128 + (n & 63) } else { d[i++] = 224 + (a >> 12);
					d[i++] = 128 + (a >> 6 & 63);
					d[i++] = 128 + (a & 63) } } if(!r || r === "buf") return d.slice(0, i); if(r !== "arr") return d.slice(0, i).toString("binary"); return [].slice.call(d, 0, i) } } var C = function H() { if(h) { if(f[t[0]]) return; var r = 0,
				l = 0; for(r = 0; r < t.length; ++r) { l = t[r]; if(e[l]) { f[l] = g(l);
					o[l] = v(l) } } for(r = 0; r < a.length; ++r) { l = a[r]; if(e[l]) { f[l] = b(l);
					o[l] = m(l) } } for(r = 0; r < n.length; ++r) { l = n[r]; if(i[l]) f[l] = i[l]; if(s[l]) o[l] = s[l] } } }; var w = function(e, r) { void r; return "" }; var E = function W(e) { delete f[e];
		delete o[e] }; var k = function V() { if(h) { if(!f[t[0]]) return;
			t.forEach(E);
			a.forEach(E);
			n.forEach(E) } B = w;
		T = 0 }; var S = { encache: C, decache: k, sbcs: t, dbcs: a };
	C(); var A = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"; var _ = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'(),-./:?"; var B = w,
		T = 0; var x = function z(t, a, n) { if(t === T && B) { return B(a, n) } if(o[t]) { B = o[T = t]; return B(a, n) } if(h && Buffer.isBuffer(a)) a = a.toString("utf8"); var i = a.length; var s = h ? Buffer.allocUnsafe(4 * i) : [],
			f = 0,
			c = 0,
			u = 0,
			d = 0; var p = e[t],
			v, g = ""; var m = typeof a === "string"; if(p && (v = p.enc))
			for(c = 0; c < i; ++c, ++u) { f = v[m ? a.charAt(c) : a[c]]; if(f > 255) { s[u] = f >> 8;
					s[++u] = f & 255 } else s[u] = f & 255 } else if(g = r[t]) switch(g) {
				case "utf8":
					if(h && m) { s = Buffer.from(a, g);
						u = s.length; break } for(c = 0; c < i; ++c, ++u) { f = m ? a.charCodeAt(c) : a[c].charCodeAt(0); if(f <= 127) s[u] = f;
						else if(f <= 2047) { s[u] = 192 + (f >> 6);
							s[++u] = 128 + (f & 63) } else if(f >= 55296 && f <= 57343) { f -= 55296;
							d = (m ? a.charCodeAt(++c) : a[++c].charCodeAt(0)) - 56320 + (f << 10);
							s[u] = 240 + (d >>> 18 & 7);
							s[++u] = 144 + (d >>> 12 & 63);
							s[++u] = 128 + (d >>> 6 & 63);
							s[++u] = 128 + (d & 63) } else { s[u] = 224 + (f >> 12);
							s[++u] = 128 + (f >> 6 & 63);
							s[++u] = 128 + (f & 63) } } break;
				case "ascii":
					if(h && typeof a === "string") { s = Buffer.from(a, g);
						u = s.length; break } for(c = 0; c < i; ++c, ++u) { f = m ? a.charCodeAt(c) : a[c].charCodeAt(0); if(f <= 127) s[u] = f;
						else throw new Error("bad ascii " + f) } break;
				case "utf16le":
					if(h && typeof a === "string") { s = Buffer.from(a, g);
						u = s.length; break } for(c = 0; c < i; ++c) { f = m ? a.charCodeAt(c) : a[c].charCodeAt(0);
						s[u++] = f & 255;
						s[u++] = f >> 8 } break;
				case "utf16be":
					for(c = 0; c < i; ++c) { f = m ? a.charCodeAt(c) : a[c].charCodeAt(0);
						s[u++] = f >> 8;
						s[u++] = f & 255 } break;
				case "utf32le":
					for(c = 0; c < i; ++c) { f = m ? a.charCodeAt(c) : a[c].charCodeAt(0); if(f >= 55296 && f <= 57343) f = 65536 + (f - 55296 << 10) + (a[++c].charCodeAt(0) - 56320);
						s[u++] = f & 255;
						f >>= 8;
						s[u++] = f & 255;
						f >>= 8;
						s[u++] = f & 255;
						f >>= 8;
						s[u++] = f & 255 } break;
				case "utf32be":
					for(c = 0; c < i; ++c) { f = m ? a.charCodeAt(c) : a[c].charCodeAt(0); if(f >= 55296 && f <= 57343) f = 65536 + (f - 55296 << 10) + (a[++c].charCodeAt(0) - 56320);
						s[u + 3] = f & 255;
						f >>= 8;
						s[u + 2] = f & 255;
						f >>= 8;
						s[u + 1] = f & 255;
						f >>= 8;
						s[u] = f & 255;
						u += 4 } break;
				case "utf7":
					for(c = 0; c < i; c++) { var b = m ? a.charAt(c) : a[c].charAt(0); if(b === "+") { s[u++] = 43;
							s[u++] = 45; continue } if(_.indexOf(b) > -1) { s[u++] = b.charCodeAt(0); continue } var C = z(1201, b);
						s[u++] = 43;
						s[u++] = A.charCodeAt(C[0] >> 2);
						s[u++] = A.charCodeAt(((C[0] & 3) << 4) + ((C[1] || 0) >> 4));
						s[u++] = A.charCodeAt(((C[1] & 15) << 2) + ((C[2] || 0) >> 6));
						s[u++] = 45 } break;
				default:
					throw new Error("Unsupported magic: " + t + " " + r[t]); } else throw new Error("Unrecognized CP: " + t);
		s = s.slice(0, u); if(!h) return n == "str" ? s.map(l).join("") : s; if(!n || n === "buf") return s; if(n !== "arr") return s.toString("binary"); return [].slice.call(s) }; var y = function X(t, a) { var n; if(n = f[t]) return n(a); if(typeof a === "string") return X(t, a.split("").map(c)); var i = a.length,
			s = new Array(i),
			o = "",
			l = 0,
			u = 0,
			d = 1,
			p = 0,
			v = 0; var g = e[t],
			m, b = ""; if(g && (m = g.dec)) { for(u = 0; u < i; u += d) { d = 2;
				o = m[(a[u] << 8) + a[u + 1]]; if(!o) { d = 1;
					o = m[a[u]] } if(!o) throw new Error("Unrecognized code: " + a[u] + " " + a[u + d - 1] + " " + u + " " + d + " " + m[a[u]]);
				s[p++] = o } } else if(b = r[t]) switch(b) {
			case "utf8":
				if(i >= 3 && a[0] == 239)
					if(a[1] == 187 && a[2] == 191) u = 3; for(; u < i; u += d) { d = 1; if(a[u] < 128) l = a[u];
					else if(a[u] < 224) { l = (a[u] & 31) * 64 + (a[u + 1] & 63);
						d = 2 } else if(a[u] < 240) { l = ((a[u] & 15) << 12) + (a[u + 1] & 63) * 64 + (a[u + 2] & 63);
						d = 3 } else { l = (a[u] & 7) * 262144 + ((a[u + 1] & 63) << 12) + (a[u + 2] & 63) * 64 + (a[u + 3] & 63);
						d = 4 } if(l < 65536) { s[p++] = String.fromCharCode(l) } else { l -= 65536;
						v = 55296 + (l >> 10 & 1023);
						l = 56320 + (l & 1023);
						s[p++] = String.fromCharCode(v);
						s[p++] = String.fromCharCode(l) } } break;
			case "ascii":
				if(h && Buffer.isBuffer(a)) return a.toString(b); for(u = 0; u < i; u++) s[u] = String.fromCharCode(a[u]);
				p = i; break;
			case "utf16le":
				if(i >= 2 && a[0] == 255)
					if(a[1] == 254) u = 2; if(h && Buffer.isBuffer(a)) return a.toString(b);
				d = 2; for(; u + 1 < i; u += d) { s[p++] = String.fromCharCode((a[u + 1] << 8) + a[u]) } break;
			case "utf16be":
				if(i >= 2 && a[0] == 254)
					if(a[1] == 255) u = 2;
				d = 2; for(; u + 1 < i; u += d) { s[p++] = String.fromCharCode((a[u] << 8) + a[u + 1]) } break;
			case "utf32le":
				if(i >= 4 && a[0] == 255)
					if(a[1] == 254 && a[2] === 0 && a[3] === 0) u = 4;
				d = 4; for(; u < i; u += d) { l = (a[u + 3] << 24) + (a[u + 2] << 16) + (a[u + 1] << 8) + a[u]; if(l > 65535) { l -= 65536;
						s[p++] = String.fromCharCode(55296 + (l >> 10 & 1023));
						s[p++] = String.fromCharCode(56320 + (l & 1023)) } else s[p++] = String.fromCharCode(l) } break;
			case "utf32be":
				if(i >= 4 && a[3] == 255)
					if(a[2] == 254 && a[1] === 0 && a[0] === 0) u = 4;
				d = 4; for(; u < i; u += d) { l = (a[u] << 24) + (a[u + 1] << 16) + (a[u + 2] << 8) + a[u + 3]; if(l > 65535) { l -= 65536;
						s[p++] = String.fromCharCode(55296 + (l >> 10 & 1023));
						s[p++] = String.fromCharCode(56320 + (l & 1023)) } else s[p++] = String.fromCharCode(l) } break;
			case "utf7":
				if(i >= 4 && a[0] == 43 && a[1] == 47 && a[2] == 118) { if(i >= 5 && a[3] == 56 && a[4] == 45) u = 5;
					else if(a[3] == 56 || a[3] == 57 || a[3] == 43 || a[3] == 47) u = 4 } for(; u < i; u += d) { if(a[u] !== 43) { d = 1;
						s[p++] = String.fromCharCode(a[u]); continue } d = 1; if(a[u + 1] === 45) { d = 2;
						s[p++] = "+"; continue } while(String.fromCharCode(a[u + d]).match(/[A-Za-z0-9+\/]/)) d++; var C = 0; if(a[u + d] === 45) {++d;
						C = 1 } var w = []; var E = ""; var k = 0,
						S = 0,
						_ = 0; var B = 0,
						T = 0,
						x = 0,
						y = 0; for(var I = 1; I < d - C;) { B = A.indexOf(String.fromCharCode(a[u + I++]));
						T = A.indexOf(String.fromCharCode(a[u + I++]));
						k = B << 2 | T >> 4;
						w.push(k);
						x = A.indexOf(String.fromCharCode(a[u + I++])); if(x === -1) break;
						S = (T & 15) << 4 | x >> 2;
						w.push(S);
						y = A.indexOf(String.fromCharCode(a[u + I++])); if(y === -1) break;
						_ = (x & 3) << 6 | y; if(y < 64) w.push(_) } E = X(1201, w); for(I = 0; I < E.length; ++I) s[p++] = E.charAt(I) } break;
			default:
				throw new Error("Unsupported magic: " + t + " " + r[t]); } else throw new Error("Unrecognized CP: " + t); return s.slice(0, p).join("") }; var I = function G(t) { return !!(e[t] || r[t]) };
	e.utils = { decode: y, encode: x, hascp: I, magic: r, cache: S }; return e });
var XLSX = {};
(function e(r) {
	r.version = "0.12.12";
	var t = 1200,
		a = 1252;
	if(typeof module !== "undefined" && typeof require !== "undefined") { if(typeof cptable === "undefined") { if(typeof global !== "undefined") global.cptable = undefined;
			else if(typeof window !== "undefined") window.cptable = undefined } }
	var n = [874, 932, 936, 949, 950];
	for(var i = 0; i <= 8; ++i) n.push(1250 + i);
	var s = { 0: 1252, 1: 65001, 2: 65001, 77: 1e4, 128: 932, 129: 949, 130: 1361, 134: 936, 136: 950, 161: 1253, 162: 1254, 163: 1258, 177: 1255, 178: 1256, 186: 1257, 204: 1251, 222: 874, 238: 1250, 255: 1252, 69: 6969 };
	var f = function(e) { if(n.indexOf(e) == -1) return;
		a = s[0] = e };

	function o() { f(1252) }
	var l = function(e) { t = e;
		f(e) };

	function c() { l(1200);
		o() }

	function h(e) { var r = []; for(var t = 0, a = e.length; t < a; ++t) r[t] = e.charCodeAt(t); return r }

	function u(e) { var r = []; for(var t = 0; t < e.length >> 1; ++t) r[t] = String.fromCharCode(e.charCodeAt(2 * t) + (e.charCodeAt(2 * t + 1) << 8)); return r.join("") }

	function d(e) { var r = []; for(var t = 0; t < e.length >> 1; ++t) r[t] = String.fromCharCode(e.charCodeAt(2 * t + 1) + (e.charCodeAt(2 * t) << 8)); return r.join("") }
	var p = function(e) { var r = e.charCodeAt(0),
			t = e.charCodeAt(1); if(r == 255 && t == 254) return u(e.slice(2)); if(r == 254 && t == 255) return d(e.slice(2)); if(r == 65279) return e.slice(1); return e };
	var v = function Dg(e) { return String.fromCharCode(e) };
	if(typeof cptable !== "undefined") { l = function(e) { t = e };
		p = function(e) { if(e.charCodeAt(0) === 255 && e.charCodeAt(1) === 254) { return cptable.utils.decode(1200, h(e.slice(2))) } return e };
		v = function Og(e) { if(t === 1200) return String.fromCharCode(e); return cptable.utils.decode(t, [e & 255, e >> 8])[0] } }
	var g = null;
	var m = true;
	var b = function Fg() { var e = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="; return { encode: function(r) { var t = ""; var a = 0,
					n = 0,
					i = 0,
					s = 0,
					f = 0,
					o = 0,
					l = 0; for(var c = 0; c < r.length;) { a = r.charCodeAt(c++);
					s = a >> 2;
					n = r.charCodeAt(c++);
					f = (a & 3) << 4 | n >> 4;
					i = r.charCodeAt(c++);
					o = (n & 15) << 2 | i >> 6;
					l = i & 63; if(isNaN(n)) { o = l = 64 } else if(isNaN(i)) { l = 64 } t += e.charAt(s) + e.charAt(f) + e.charAt(o) + e.charAt(l) } return t }, decode: function r(t) { var a = ""; var n = 0,
					i = 0,
					s = 0,
					f = 0,
					o = 0,
					l = 0,
					c = 0;
				t = t.replace(/[^\w\+\/\=]/g, ""); for(var h = 0; h < t.length;) { f = e.indexOf(t.charAt(h++));
					o = e.indexOf(t.charAt(h++));
					n = f << 2 | o >> 4;
					a += String.fromCharCode(n);
					l = e.indexOf(t.charAt(h++));
					i = (o & 15) << 4 | l >> 2; if(l !== 64) { a += String.fromCharCode(i) } c = e.indexOf(t.charAt(h++));
					s = (l & 3) << 6 | c; if(c !== 64) { a += String.fromCharCode(s) } } return a } } }();
	var C = typeof Buffer !== "undefined" && typeof process !== "undefined" && typeof process.versions !== "undefined" && process.versions.node;
	if(typeof Buffer !== "undefined") { if(!Buffer.from) Buffer.from = function(e, r) { return r ? new Buffer(e, r) : new Buffer(e) }; if(!Buffer.alloc) Buffer.alloc = function(e) { return new Buffer(e) } }

	function w(e) { return C ? Buffer.alloc(e) : new Array(e) }
	var E = function Pg(e) { if(C) return Buffer.from(e, "binary"); return e.split("").map(function(e) { return e.charCodeAt(0) & 255 }) };

	function k(e) { if(typeof ArrayBuffer === "undefined") return E(e); var r = new ArrayBuffer(e.length),
			t = new Uint8Array(r); for(var a = 0; a != e.length; ++a) t[a] = e.charCodeAt(a) & 255; return r }

	function S(e) { if(Array.isArray(e)) return e.map(_p).join(""); var r = []; for(var t = 0; t < e.length; ++t) r[t] = _p(e[t]); return r.join("") }

	function A(e) { if(typeof Uint8Array === "undefined") throw new Error("Unsupported"); return new Uint8Array(e) }

	function _(e) { if(typeof ArrayBuffer == "undefined") throw new Error("Unsupported"); if(e instanceof ArrayBuffer) return _(new Uint8Array(e)); var r = new Array(e.length); for(var t = 0; t < e.length; ++t) r[t] = e[t]; return r }
	var B = function(e) { return [].concat.apply([], e) };
	var T = /\u0000/g,
		x = /[\u0001-\u0006]/g;
	var y = {};
	var I = function Ng(e) {
		e.version = "0.10.2";

		function r(e) { var r = "",
				t = e.length - 1; while(t >= 0) r += e.charAt(t--); return r }

		function t(e, r) { var t = ""; while(t.length < r) t += e; return t }

		function a(e, r) { var a = "" + e; return a.length >= r ? a : t("0", r - a.length) + a }

		function n(e, r) { var a = "" + e; return a.length >= r ? a : t(" ", r - a.length) + a }

		function i(e, r) { var a = "" + e; return a.length >= r ? a : a + t(" ", r - a.length) }

		function s(e, r) { var a = "" + Math.round(e); return a.length >= r ? a : t("0", r - a.length) + a }

		function f(e, r) { var a = "" + e; return a.length >= r ? a : t("0", r - a.length) + a }
		var o = Math.pow(2, 32);

		function l(e, r) { if(e > o || e < -o) return s(e, r); var t = Math.round(e); return f(t, r) }

		function c(e, r) { r = r || 0; return e.length >= 7 + r && (e.charCodeAt(r) | 32) === 103 && (e.charCodeAt(r + 1) | 32) === 101 && (e.charCodeAt(r + 2) | 32) === 110 && (e.charCodeAt(r + 3) | 32) === 101 && (e.charCodeAt(r + 4) | 32) === 114 && (e.charCodeAt(r + 5) | 32) === 97 && (e.charCodeAt(r + 6) | 32) === 108 }
		var h = [
			["Sun", "Sunday"],
			["Mon", "Monday"],
			["Tue", "Tuesday"],
			["Wed", "Wednesday"],
			["Thu", "Thursday"],
			["Fri", "Friday"],
			["Sat", "Saturday"]
		];
		var u = [
			["J", "Jan", "January"],
			["F", "Feb", "February"],
			["M", "Mar", "March"],
			["A", "Apr", "April"],
			["M", "May", "May"],
			["J", "Jun", "June"],
			["J", "Jul", "July"],
			["A", "Aug", "August"],
			["S", "Sep", "September"],
			["O", "Oct", "October"],
			["N", "Nov", "November"],
			["D", "Dec", "December"]
		];

		function d(e) { e[0] = "General";
			e[1] = "0";
			e[2] = "0.00";
			e[3] = "#,##0";
			e[4] = "#,##0.00";
			e[9] = "0%";
			e[10] = "0.00%";
			e[11] = "0.00E+00";
			e[12] = "# ?/?";
			e[13] = "# ??/??";
			e[14] = "m/d/yy";
			e[15] = "d-mmm-yy";
			e[16] = "d-mmm";
			e[17] = "mmm-yy";
			e[18] = "h:mm AM/PM";
			e[19] = "h:mm:ss AM/PM";
			e[20] = "h:mm";
			e[21] = "h:mm:ss";
			e[22] = "m/d/yy h:mm";
			e[37] = "#,##0 ;(#,##0)";
			e[38] = "#,##0 ;[Red](#,##0)";
			e[39] = "#,##0.00;(#,##0.00)";
			e[40] = "#,##0.00;[Red](#,##0.00)";
			e[45] = "mm:ss";
			e[46] = "[h]:mm:ss";
			e[47] = "mmss.0";
			e[48] = "##0.0E+0";
			e[49] = "@";
			e[56] = '"涓婂崍/涓嬪崍 "hh"鏅�"mm"鍒�"ss"绉� "';
			e[65535] = "General" }
		var p = {};
		d(p);

		function v(e, r, t) { var a = e < 0 ? -1 : 1; var n = e * a; var i = 0,
				s = 1,
				f = 0; var o = 1,
				l = 0,
				c = 0; var h = Math.floor(n); while(l < r) { h = Math.floor(n);
				f = h * s + i;
				c = h * l + o; if(n - h < 5e-8) break;
				n = 1 / (n - h);
				i = s;
				s = f;
				o = l;
				l = c } if(c > r) { if(l > r) { c = o;
					f = i } else { c = l;
					f = s } } if(!t) return [0, a * f, c]; var u = Math.floor(a * f / c); return [u, a * f - u * c, c] }

		function g(e, r, t) { if(e > 2958465 || e < 0) return null; var a = e | 0,
				n = Math.floor(86400 * (e - a)),
				i = 0; var s = []; var f = { D: a, T: n, u: 86400 * (e - a) - n, y: 0, m: 0, d: 0, H: 0, M: 0, S: 0, q: 0 }; if(Math.abs(f.u) < 1e-6) f.u = 0; if(r && r.date1904) a += 1462; if(f.u > .9999) { f.u = 0; if(++n == 86400) { f.T = n = 0;++a;++f.D } } if(a === 60) { s = t ? [1317, 10, 29] : [1900, 2, 29];
				i = 3 } else if(a === 0) { s = t ? [1317, 8, 29] : [1900, 1, 0];
				i = 6 } else { if(a > 60) --a; var o = new Date(1900, 0, 1);
				o.setDate(o.getDate() + a - 1);
				s = [o.getFullYear(), o.getMonth() + 1, o.getDate()];
				i = o.getDay(); if(a < 60) i = (i + 6) % 7; if(t) i = A(o, s) } f.y = s[0];
			f.m = s[1];
			f.d = s[2];
			f.S = n % 60;
			n = Math.floor(n / 60);
			f.M = n % 60;
			n = Math.floor(n / 60);
			f.H = n;
			f.q = i; return f } e.parse_date_code = g;
		var m = new Date(1899, 11, 31, 0, 0, 0);
		var b = m.getTime();
		var C = new Date(1900, 2, 1, 0, 0, 0);

		function w(e, r) { var t = e.getTime(); if(r) t -= 1461 * 24 * 60 * 60 * 1e3;
			else if(e >= C) t += 24 * 60 * 60 * 1e3; return(t - (b + (e.getTimezoneOffset() - m.getTimezoneOffset()) * 6e4)) / (24 * 60 * 60 * 1e3) }

		function E(e) { return e.toString(10) } e._general_int = E;
		var k = function M() {
			var e = /\.(\d*[1-9])0+$/,
				r = /\.0*$/,
				t = /\.(\d*[1-9])0+/,
				a = /\.0*[Ee]/,
				n = /(E[+-])(\d)$/;

			function i(e) { var r = e < 0 ? 12 : 11; var t = o(e.toFixed(12)); if(t.length <= r) return t;
				t = e.toPrecision(10); if(t.length <= r) return t; return e.toExponential(5) }

			function s(r) { var t = r.toFixed(11).replace(e, ".$1"); if(t.length > (r < 0 ? 12 : 11)) t = r.toPrecision(6); return t }

			function f(e) {
				for(var r = 0; r != e.length; ++r)
					if((e.charCodeAt(r) | 32) === 101) return e.replace(t, ".$1").replace(a, "E").replace("e", "E").replace(n, "$10$2");
				return e
			}

			function o(t) { return t.indexOf(".") > -1 ? t.replace(r, "").replace(e, ".$1") : t }
			return function l(e) { var r = Math.floor(Math.log(Math.abs(e)) * Math.LOG10E),
					t; if(r >= -4 && r <= -1) t = e.toPrecision(10 + r);
				else if(Math.abs(r) <= 9) t = i(e);
				else if(r === 10) t = e.toFixed(10).substr(0, 12);
				else t = s(e); return o(f(t)) }
		}();
		e._general_num = k;

		function S(e, r) { switch(typeof e) {
				case "string":
					return e;
				case "boolean":
					return e ? "TRUE" : "FALSE";
				case "number":
					return(e | 0) === e ? E(e) : k(e);
				case "undefined":
					return "";
				case "object":
					if(e == null) return ""; if(e instanceof Date) return N(14, w(e, r && r.date1904), r); } throw new Error("unsupported value in General format: " + e) } e._general = S;

		function A() { return 0 }

		function _(e, r, t, n) { var i = "",
				s = 0,
				f = 0,
				o = t.y,
				l, c = 0; switch(e) {
				case 98:
					o = t.y + 543;
				case 121:
					switch(r.length) {
						case 1:
							;
						case 2:
							l = o % 100;
							c = 2; break;
						default:
							l = o % 1e4;
							c = 4; break; } break;
				case 109:
					switch(r.length) {
						case 1:
							;
						case 2:
							l = t.m;
							c = r.length; break;
						case 3:
							return u[t.m - 1][1];
						case 5:
							return u[t.m - 1][0];
						default:
							return u[t.m - 1][2]; } break;
				case 100:
					switch(r.length) {
						case 1:
							;
						case 2:
							l = t.d;
							c = r.length; break;
						case 3:
							return h[t.q][0];
						default:
							return h[t.q][1]; } break;
				case 104:
					switch(r.length) {
						case 1:
							;
						case 2:
							l = 1 + (t.H + 11) % 12;
							c = r.length; break;
						default:
							throw "bad hour format: " + r; } break;
				case 72:
					switch(r.length) {
						case 1:
							;
						case 2:
							l = t.H;
							c = r.length; break;
						default:
							throw "bad hour format: " + r; } break;
				case 77:
					switch(r.length) {
						case 1:
							;
						case 2:
							l = t.M;
							c = r.length; break;
						default:
							throw "bad minute format: " + r; } break;
				case 115:
					if(r != "s" && r != "ss" && r != ".0" && r != ".00" && r != ".000") throw "bad second format: " + r; if(t.u === 0 && (r == "s" || r == "ss")) return a(t.S, r.length); if(n >= 2) f = n === 3 ? 1e3 : 100;
					else f = n === 1 ? 10 : 1;
					s = Math.round(f * (t.S + t.u)); if(s >= 60 * f) s = 0; if(r === "s") return s === 0 ? "0" : "" + s / f;
					i = a(s, 2 + n); if(r === "ss") return i.substr(0, 2); return "." + i.substr(2, r.length - 1);
				case 90:
					switch(r) {
						case "[h]":
							;
						case "[hh]":
							l = t.D * 24 + t.H; break;
						case "[m]":
							;
						case "[mm]":
							l = (t.D * 24 + t.H) * 60 + t.M; break;
						case "[s]":
							;
						case "[ss]":
							l = ((t.D * 24 + t.H) * 60 + t.M) * 60 + Math.round(t.S + t.u); break;
						default:
							throw "bad abstime format: " + r; } c = r.length === 3 ? 1 : 2; break;
				case 101:
					l = o;
					c = 1; } if(c > 0) return a(l, c);
			else return "" }

		function B(e) { var r = 3; if(e.length <= r) return e; var t = e.length % r,
				a = e.substr(0, t); for(; t != e.length; t += r) a += (a.length > 0 ? "," : "") + e.substr(t, r); return a }
		var T = function U() { var e = /%/g;

			function s(r, a, n) { var i = a.replace(e, ""),
					s = a.length - i.length; return T(r, i, n * Math.pow(10, 2 * s)) + t("%", s) }

			function f(e, r, t) { var a = r.length - 1; while(r.charCodeAt(a - 1) === 44) --a; return T(e, r.substr(0, a), t / Math.pow(10, 3 * (r.length - a))) }

			function o(e, r) { var t; var a = e.indexOf("E") - e.indexOf(".") - 1; if(e.match(/^#+0.0E\+0$/)) { if(r == 0) return "0.0E+0";
					else if(r < 0) return "-" + o(e, -r); var n = e.indexOf("."); if(n === -1) n = e.indexOf("E"); var i = Math.floor(Math.log(r) * Math.LOG10E) % n; if(i < 0) i += n;
					t = (r / Math.pow(10, i)).toPrecision(a + 1 + (n + i) % n); if(t.indexOf("e") === -1) { var s = Math.floor(Math.log(r) * Math.LOG10E); if(t.indexOf(".") === -1) t = t.charAt(0) + "." + t.substr(1) + "E+" + (s - t.length + i);
						else t += "E+" + (s - i); while(t.substr(0, 2) === "0.") { t = t.charAt(0) + t.substr(2, n) + "." + t.substr(2 + n);
							t = t.replace(/^0+([1-9])/, "$1").replace(/^0+\./, "0.") } t = t.replace(/\+-/, "-") } t = t.replace(/^([+-]?)(\d*)\.(\d*)[Ee]/, function(e, r, t, a) { return r + t + a.substr(0, (n + i) % n) + "." + a.substr(i) + "E" }) } else t = r.toExponential(a); if(e.match(/E\+00$/) && t.match(/e[+-]\d$/)) t = t.substr(0, t.length - 1) + "0" + t.charAt(t.length - 1); if(e.match(/E\-/) && t.match(/e\+/)) t = t.replace(/e\+/, "e"); return t.replace("e", "E") } var c = /# (\?+)( ?)\/( ?)(\d+)/;

			function h(e, r, i) { var s = parseInt(e[4], 10),
					f = Math.round(r * s),
					o = Math.floor(f / s); var l = f - o * s,
					c = s; return i + (o === 0 ? "" : "" + o) + " " + (l === 0 ? t(" ", e[1].length + 1 + e[4].length) : n(l, e[1].length) + e[2] + "/" + e[3] + a(c, e[4].length)) }

			function u(e, r, a) { return a + (r === 0 ? "" : "" + r) + t(" ", e[1].length + 2 + e[4].length) } var d = /^#*0*\.([0#]+)/; var p = /\).*[0#]/; var g = /\(###\) ###\\?-####/;

			function m(e) { var r = "",
					t; for(var a = 0; a != e.length; ++a) switch(t = e.charCodeAt(a)) {
					case 35:
						break;
					case 63:
						r += " "; break;
					case 48:
						r += "0"; break;
					default:
						r += String.fromCharCode(t); }
				return r }

			function b(e, r) { var t = Math.pow(10, r); return "" + Math.round(e * t) / t }

			function C(e, r) { if(r < ("" + Math.round((e - Math.floor(e)) * Math.pow(10, r))).length) { return 0 } return Math.round((e - Math.floor(e)) * Math.pow(10, r)) }

			function w(e, r) { if(r < ("" + Math.round((e - Math.floor(e)) * Math.pow(10, r))).length) { return 1 } return 0 }

			function E(e) { if(e < 2147483647 && e > -2147483648) return "" + (e >= 0 ? e | 0 : e - 1 | 0); return "" + Math.floor(e) }

			function k(e, u, S) { if(e.charCodeAt(0) === 40 && !u.match(p)) { var A = u.replace(/\( */, "").replace(/ \)/, "").replace(/\)/, ""); if(S >= 0) return k("n", A, S); return "(" + k("n", A, -S) + ")" } if(u.charCodeAt(u.length - 1) === 44) return f(e, u, S); if(u.indexOf("%") !== -1) return s(e, u, S); if(u.indexOf("E") !== -1) return o(u, S); if(u.charCodeAt(0) === 36) return "$" + k(e, u.substr(u.charAt(1) == " " ? 2 : 1), S); var _; var x, y, I, R = Math.abs(S),
					D = S < 0 ? "-" : ""; if(u.match(/^00+$/)) return D + l(R, u.length); if(u.match(/^[#?]+$/)) { _ = l(S, 0); if(_ === "0") _ = ""; return _.length > u.length ? _ : m(u.substr(0, u.length - _.length)) + _ } if(x = u.match(c)) return h(x, R, D); if(u.match(/^#+0+$/)) return D + l(R, u.length - u.indexOf("0")); if(x = u.match(d)) { _ = b(S, x[1].length).replace(/^([^\.]+)$/, "$1." + m(x[1])).replace(/\.$/, "." + m(x[1])).replace(/\.(\d*)$/, function(e, r) { return "." + r + t("0", m(x[1]).length - r.length) }); return u.indexOf("0.") !== -1 ? _ : _.replace(/^0\./, ".") } u = u.replace(/^#+([0.])/, "$1"); if(x = u.match(/^(0*)\.(#*)$/)) { return D + b(R, x[2].length).replace(/\.(\d*[1-9])0*$/, ".$1").replace(/^(-?\d*)$/, "$1.").replace(/^0\./, x[1].length ? "0." : ".") } if(x = u.match(/^#{1,3},##0(\.?)$/)) return D + B(l(R, 0)); if(x = u.match(/^#,##0\.([#0]*0)$/)) { return S < 0 ? "-" + k(e, u, -S) : B("" + (Math.floor(S) + w(S, x[1].length))) + "." + a(C(S, x[1].length), x[1].length) } if(x = u.match(/^#,#*,#0/)) return k(e, u.replace(/^#,#*,/, ""), S); if(x = u.match(/^([0#]+)(\\?-([0#]+))+$/)) { _ = r(k(e, u.replace(/[\\-]/g, ""), S));
					y = 0; return r(r(u.replace(/\\/g, "")).replace(/[0#]/g, function(e) { return y < _.length ? _.charAt(y++) : e === "0" ? "0" : "" })) } if(u.match(g)) { _ = k(e, "##########", S); return "(" + _.substr(0, 3) + ") " + _.substr(3, 3) + "-" + _.substr(6) } var O = ""; if(x = u.match(/^([#0?]+)( ?)\/( ?)([#0?]+)/)) { y = Math.min(x[4].length, 7);
					I = v(R, Math.pow(10, y) - 1, false);
					_ = "" + D;
					O = T("n", x[1], I[1]); if(O.charAt(O.length - 1) == " ") O = O.substr(0, O.length - 1) + "0";
					_ += O + x[2] + "/" + x[3];
					O = i(I[2], y); if(O.length < x[4].length) O = m(x[4].substr(x[4].length - O.length)) + O;
					_ += O; return _ } if(x = u.match(/^# ([#0?]+)( ?)\/( ?)([#0?]+)/)) { y = Math.min(Math.max(x[1].length, x[4].length), 7);
					I = v(R, Math.pow(10, y) - 1, true); return D + (I[0] || (I[1] ? "" : "0")) + " " + (I[1] ? n(I[1], y) + x[2] + "/" + x[3] + i(I[2], y) : t(" ", 2 * y + 1 + x[2].length + x[3].length)) } if(x = u.match(/^[#0?]+$/)) { _ = l(S, 0); if(u.length <= _.length) return _; return m(u.substr(0, u.length - _.length)) + _ } if(x = u.match(/^([#0?]+)\.([#0]+)$/)) { _ = "" + S.toFixed(Math.min(x[2].length, 10)).replace(/([^0])0+$/, "$1");
					y = _.indexOf("."); var F = u.indexOf(".") - y,
						P = u.length - _.length - F; return m(u.substr(0, F) + _ + u.substr(u.length - P)) } if(x = u.match(/^00,000\.([#0]*0)$/)) { y = C(S, x[1].length); return S < 0 ? "-" + k(e, u, -S) : B(E(S)).replace(/^\d,\d{3}$/, "0$&").replace(/^\d*$/, function(e) { return "00," + (e.length < 3 ? a(0, 3 - e.length) : "") + e }) + "." + a(y, x[1].length) } switch(u) {
					case "###,##0.00":
						return k(e, "#,##0.00", S);
					case "###,###":
						;
					case "##,###":
						;
					case "#,###":
						var N = B(l(R, 0)); return N !== "0" ? D + N : "";
					case "###,###.00":
						return k(e, "###,##0.00", S).replace(/^0\./, ".");
					case "#,###.00":
						return k(e, "#,##0.00", S).replace(/^0\./, ".");
					default:
						; } throw new Error("unsupported format |" + u + "|") }

			function S(e, r, t) { var a = r.length - 1; while(r.charCodeAt(a - 1) === 44) --a; return T(e, r.substr(0, a), t / Math.pow(10, 3 * (r.length - a))) }

			function A(r, a, n) { var i = a.replace(e, ""),
					s = a.length - i.length; return T(r, i, n * Math.pow(10, 2 * s)) + t("%", s) }

			function _(e, r) { var t; var a = e.indexOf("E") - e.indexOf(".") - 1; if(e.match(/^#+0.0E\+0$/)) { if(r == 0) return "0.0E+0";
					else if(r < 0) return "-" + _(e, -r); var n = e.indexOf("."); if(n === -1) n = e.indexOf("E"); var i = Math.floor(Math.log(r) * Math.LOG10E) % n; if(i < 0) i += n;
					t = (r / Math.pow(10, i)).toPrecision(a + 1 + (n + i) % n); if(!t.match(/[Ee]/)) { var s = Math.floor(Math.log(r) * Math.LOG10E); if(t.indexOf(".") === -1) t = t.charAt(0) + "." + t.substr(1) + "E+" + (s - t.length + i);
						else t += "E+" + (s - i);
						t = t.replace(/\+-/, "-") } t = t.replace(/^([+-]?)(\d*)\.(\d*)[Ee]/, function(e, r, t, a) { return r + t + a.substr(0, (n + i) % n) + "." + a.substr(i) + "E" }) } else t = r.toExponential(a); if(e.match(/E\+00$/) && t.match(/e[+-]\d$/)) t = t.substr(0, t.length - 1) + "0" + t.charAt(t.length - 1); if(e.match(/E\-/) && t.match(/e\+/)) t = t.replace(/e\+/, "e"); return t.replace("e", "E") }

			function x(e, s, f) { if(e.charCodeAt(0) === 40 && !s.match(p)) { var o = s.replace(/\( */, "").replace(/ \)/, "").replace(/\)/, ""); if(f >= 0) return x("n", o, f); return "(" + x("n", o, -f) + ")" } if(s.charCodeAt(s.length - 1) === 44) return S(e, s, f); if(s.indexOf("%") !== -1) return A(e, s, f); if(s.indexOf("E") !== -1) return _(s, f); if(s.charCodeAt(0) === 36) return "$" + x(e, s.substr(s.charAt(1) == " " ? 2 : 1), f); var l; var h, b, C, w = Math.abs(f),
					E = f < 0 ? "-" : ""; if(s.match(/^00+$/)) return E + a(w, s.length); if(s.match(/^[#?]+$/)) { l = "" + f; if(f === 0) l = ""; return l.length > s.length ? l : m(s.substr(0, s.length - l.length)) + l } if(h = s.match(c)) return u(h, w, E); if(s.match(/^#+0+$/)) return E + a(w, s.length - s.indexOf("0")); if(h = s.match(d)) { l = ("" + f).replace(/^([^\.]+)$/, "$1." + m(h[1])).replace(/\.$/, "." + m(h[1]));
					l = l.replace(/\.(\d*)$/, function(e, r) { return "." + r + t("0", m(h[1]).length - r.length) }); return s.indexOf("0.") !== -1 ? l : l.replace(/^0\./, ".") } s = s.replace(/^#+([0.])/, "$1"); if(h = s.match(/^(0*)\.(#*)$/)) { return E + ("" + w).replace(/\.(\d*[1-9])0*$/, ".$1").replace(/^(-?\d*)$/, "$1.").replace(/^0\./, h[1].length ? "0." : ".") } if(h = s.match(/^#{1,3},##0(\.?)$/)) return E + B("" + w); if(h = s.match(/^#,##0\.([#0]*0)$/)) { return f < 0 ? "-" + x(e, s, -f) : B("" + f) + "." + t("0", h[1].length) } if(h = s.match(/^#,#*,#0/)) return x(e, s.replace(/^#,#*,/, ""), f); if(h = s.match(/^([0#]+)(\\?-([0#]+))+$/)) { l = r(x(e, s.replace(/[\\-]/g, ""), f));
					b = 0; return r(r(s.replace(/\\/g, "")).replace(/[0#]/g, function(e) { return b < l.length ? l.charAt(b++) : e === "0" ? "0" : "" })) } if(s.match(g)) { l = x(e, "##########", f); return "(" + l.substr(0, 3) + ") " + l.substr(3, 3) + "-" + l.substr(6) } var k = ""; if(h = s.match(/^([#0?]+)( ?)\/( ?)([#0?]+)/)) { b = Math.min(h[4].length, 7);
					C = v(w, Math.pow(10, b) - 1, false);
					l = "" + E;
					k = T("n", h[1], C[1]); if(k.charAt(k.length - 1) == " ") k = k.substr(0, k.length - 1) + "0";
					l += k + h[2] + "/" + h[3];
					k = i(C[2], b); if(k.length < h[4].length) k = m(h[4].substr(h[4].length - k.length)) + k;
					l += k; return l } if(h = s.match(/^# ([#0?]+)( ?)\/( ?)([#0?]+)/)) { b = Math.min(Math.max(h[1].length, h[4].length), 7);
					C = v(w, Math.pow(10, b) - 1, true); return E + (C[0] || (C[1] ? "" : "0")) + " " + (C[1] ? n(C[1], b) + h[2] + "/" + h[3] + i(C[2], b) : t(" ", 2 * b + 1 + h[2].length + h[3].length)) } if(h = s.match(/^[#0?]+$/)) { l = "" + f; if(s.length <= l.length) return l; return m(s.substr(0, s.length - l.length)) + l } if(h = s.match(/^([#0]+)\.([#0]+)$/)) { l = "" + f.toFixed(Math.min(h[2].length, 10)).replace(/([^0])0+$/, "$1");
					b = l.indexOf("."); var y = s.indexOf(".") - b,
						I = s.length - l.length - y; return m(s.substr(0, y) + l + s.substr(s.length - I)) } if(h = s.match(/^00,000\.([#0]*0)$/)) { return f < 0 ? "-" + x(e, s, -f) : B("" + f).replace(/^\d,\d{3}$/, "0$&").replace(/^\d*$/, function(e) { return "00," + (e.length < 3 ? a(0, 3 - e.length) : "") + e }) + "." + a(0, h[1].length) } switch(s) {
					case "###,###":
						;
					case "##,###":
						;
					case "#,###":
						var R = B("" + w); return R !== "0" ? E + R : "";
					default:
						if(s.match(/\.[0#?]*$/)) return x(e, s.slice(0, s.lastIndexOf(".")), f) + m(s.slice(s.lastIndexOf("."))); } throw new Error("unsupported format |" + s + "|") } return function y(e, r, t) { return(t | 0) === t ? x(e, r, t) : k(e, r, t) } }();

		function x(e) { var r = []; var t = false; for(var a = 0, n = 0; a < e.length; ++a) switch(e.charCodeAt(a)) {
				case 34:
					t = !t; break;
				case 95:
					;
				case 42:
					;
				case 92:
					++a; break;
				case 59:
					r[r.length] = e.substr(n, a - n);
					n = a + 1; } r[r.length] = e.substr(n); if(t === true) throw new Error("Format |" + e + "| unterminated string "); return r } e._split = x;
		var y = /\[[HhMmSs]*\]/;

		function I(e) { var r = 0,
				t = "",
				a = ""; while(r < e.length) { switch(t = e.charAt(r)) {
					case "G":
						if(c(e, r)) r += 6;
						r++; break;
					case '"':
						for(; e.charCodeAt(++r) !== 34 && r < e.length;) ++r;++r; break;
					case "\\":
						r += 2; break;
					case "_":
						r += 2; break;
					case "@":
						++r; break;
					case "B":
						;
					case "b":
						if(e.charAt(r + 1) === "1" || e.charAt(r + 1) === "2") return true;
					case "M":
						;
					case "D":
						;
					case "Y":
						;
					case "H":
						;
					case "S":
						;
					case "E":
						;
					case "m":
						;
					case "d":
						;
					case "y":
						;
					case "h":
						;
					case "s":
						;
					case "e":
						;
					case "g":
						return true;
					case "A":
						;
					case "a":
						if(e.substr(r, 3).toUpperCase() === "A/P") return true; if(e.substr(r, 5).toUpperCase() === "AM/PM") return true;++r; break;
					case "[":
						a = t; while(e.charAt(r++) !== "]" && r < e.length) a += e.charAt(r); if(a.match(y)) return true; break;
					case ".":
						;
					case "0":
						;
					case "#":
						while(r < e.length && ("0#?.,E+-%".indexOf(t = e.charAt(++r)) > -1 || t == "\\" && e.charAt(r + 1) == "-" && "0#".indexOf(e.charAt(r + 2)) > -1)) {} break;
					case "?":
						while(e.charAt(++r) === t) {} break;
					case "*":
						++r; if(e.charAt(r) == " " || e.charAt(r) == "*") ++r; break;
					case "(":
						;
					case ")":
						++r; break;
					case "1":
						;
					case "2":
						;
					case "3":
						;
					case "4":
						;
					case "5":
						;
					case "6":
						;
					case "7":
						;
					case "8":
						;
					case "9":
						while(r < e.length && "0123456789".indexOf(e.charAt(++r)) > -1) {} break;
					case " ":
						++r; break;
					default:
						++r; break; } } return false } e.is_date = I;

		function R(e, r, t, a) { var n = [],
				i = "",
				s = 0,
				f = "",
				o = "t",
				l, h, u; var d = "H"; while(s < e.length) { switch(f = e.charAt(s)) {
					case "G":
						if(!c(e, s)) throw new Error("unrecognized character " + f + " in " + e);
						n[n.length] = { t: "G", v: "General" };
						s += 7; break;
					case '"':
						for(i = "";
							(u = e.charCodeAt(++s)) !== 34 && s < e.length;) i += String.fromCharCode(u);
						n[n.length] = { t: "t", v: i };++s; break;
					case "\\":
						var p = e.charAt(++s),
							v = p === "(" || p === ")" ? p : "t";
						n[n.length] = { t: v, v: p };++s; break;
					case "_":
						n[n.length] = { t: "t", v: " " };
						s += 2; break;
					case "@":
						n[n.length] = { t: "T", v: r };++s; break;
					case "B":
						;
					case "b":
						if(e.charAt(s + 1) === "1" || e.charAt(s + 1) === "2") { if(l == null) { l = g(r, t, e.charAt(s + 1) === "2"); if(l == null) return "" } n[n.length] = { t: "X", v: e.substr(s, 2) };
							o = f;
							s += 2; break };
					case "M":
						;
					case "D":
						;
					case "Y":
						;
					case "H":
						;
					case "S":
						;
					case "E":
						f = f.toLowerCase();
					case "m":
						;
					case "d":
						;
					case "y":
						;
					case "h":
						;
					case "s":
						;
					case "e":
						;
					case "g":
						if(r < 0) return ""; if(l == null) { l = g(r, t); if(l == null) return "" } i = f; while(++s < e.length && e.charAt(s).toLowerCase() === f) i += f; if(f === "m" && o.toLowerCase() === "h") f = "M"; if(f === "h") f = d;
						n[n.length] = { t: f, v: i };
						o = f; break;
					case "A":
						;
					case "a":
						var m = { t: f, v: f }; if(l == null) l = g(r, t); if(e.substr(s, 3).toUpperCase() === "A/P") { if(l != null) m.v = l.H >= 12 ? "P" : "A";
							m.t = "T";
							d = "h";
							s += 3 } else if(e.substr(s, 5).toUpperCase() === "AM/PM") { if(l != null) m.v = l.H >= 12 ? "PM" : "AM";
							m.t = "T";
							s += 5;
							d = "h" } else { m.t = "t";++s } if(l == null && m.t === "T") return "";
						n[n.length] = m;
						o = f; break;
					case "[":
						i = f; while(e.charAt(s++) !== "]" && s < e.length) i += e.charAt(s); if(i.slice(-1) !== "]") throw 'unterminated "[" block: |' + i + "|"; if(i.match(y)) { if(l == null) { l = g(r, t); if(l == null) return "" } n[n.length] = { t: "Z", v: i.toLowerCase() };
							o = i.charAt(1) } else if(i.indexOf("$") > -1) { i = (i.match(/\$([^-\[\]]*)/) || [])[1] || "$"; if(!I(e)) n[n.length] = { t: "t", v: i } } break;
					case ".":
						if(l != null) { i = f; while(++s < e.length && (f = e.charAt(s)) === "0") i += f;
							n[n.length] = { t: "s", v: i }; break };
					case "0":
						;
					case "#":
						i = f; while(++s < e.length && "0#?.,E+-%".indexOf(f = e.charAt(s)) > -1 || f == "\\" && e.charAt(s + 1) == "-" && s < e.length - 2 && "0#".indexOf(e.charAt(s + 2)) > -1) i += f;
						n[n.length] = { t: "n", v: i }; break;
					case "?":
						i = f; while(e.charAt(++s) === f) i += f;
						n[n.length] = { t: f, v: i };
						o = f; break;
					case "*":
						++s; if(e.charAt(s) == " " || e.charAt(s) == "*") ++s; break;
					case "(":
						;
					case ")":
						n[n.length] = { t: a === 1 ? "t" : f, v: f };++s; break;
					case "1":
						;
					case "2":
						;
					case "3":
						;
					case "4":
						;
					case "5":
						;
					case "6":
						;
					case "7":
						;
					case "8":
						;
					case "9":
						i = f; while(s < e.length && "0123456789".indexOf(e.charAt(++s)) > -1) i += e.charAt(s);
						n[n.length] = { t: "D", v: i }; break;
					case " ":
						n[n.length] = { t: f, v: f };++s; break;
					default:
						if(",$-+/():!^&'~{}<>=鈧琣cfijklopqrtuvwxzP".indexOf(f) === -1) throw new Error("unrecognized character " + f + " in " + e);
						n[n.length] = { t: "t", v: f };++s; break; } } var b = 0,
				C = 0,
				w; for(s = n.length - 1, o = "t"; s >= 0; --s) { switch(n[s].t) {
					case "h":
						;
					case "H":
						n[s].t = d;
						o = "h"; if(b < 1) b = 1; break;
					case "s":
						if(w = n[s].v.match(/\.0+$/)) C = Math.max(C, w[0].length - 1); if(b < 3) b = 3;
					case "d":
						;
					case "y":
						;
					case "M":
						;
					case "e":
						o = n[s].t; break;
					case "m":
						if(o === "s") { n[s].t = "M"; if(b < 2) b = 2 } break;
					case "X":
						break;
					case "Z":
						if(b < 1 && n[s].v.match(/[Hh]/)) b = 1; if(b < 2 && n[s].v.match(/[Mm]/)) b = 2; if(b < 3 && n[s].v.match(/[Ss]/)) b = 3; } } switch(b) {
				case 0:
					break;
				case 1:
					if(l.u >= .5) { l.u = 0;++l.S } if(l.S >= 60) { l.S = 0;++l.M } if(l.M >= 60) { l.M = 0;++l.H } break;
				case 2:
					if(l.u >= .5) { l.u = 0;++l.S } if(l.S >= 60) { l.S = 0;++l.M } break; } var E = "",
				k; for(s = 0; s < n.length; ++s) { switch(n[s].t) {
					case "t":
						;
					case "T":
						;
					case " ":
						;
					case "D":
						break;
					case "X":
						n[s].v = "";
						n[s].t = ";"; break;
					case "d":
						;
					case "m":
						;
					case "y":
						;
					case "h":
						;
					case "H":
						;
					case "M":
						;
					case "s":
						;
					case "e":
						;
					case "b":
						;
					case "Z":
						n[s].v = _(n[s].t.charCodeAt(0), n[s].v, l, C);
						n[s].t = "t"; break;
					case "n":
						;
					case "(":
						;
					case "?":
						k = s + 1; while(n[k] != null && ((f = n[k].t) === "?" || f === "D" || (f === " " || f === "t") && n[k + 1] != null && (n[k + 1].t === "?" || n[k + 1].t === "t" && n[k + 1].v === "/") || n[s].t === "(" && (f === " " || f === "n" || f === ")") || f === "t" && (n[k].v === "/" || n[k].v === " " && n[k + 1] != null && n[k + 1].t == "?"))) { n[s].v += n[k].v;
							n[k] = { v: "", t: ";" };++k } E += n[s].v;
						s = k - 1; break;
					case "G":
						n[s].t = "t";
						n[s].v = S(r, t); break; } } var A = "",
				B, x; if(E.length > 0) { if(E.charCodeAt(0) == 40) { B = r < 0 && E.charCodeAt(0) === 45 ? -r : r;
					x = T("(", E, B) } else { B = r < 0 && a > 1 ? -r : r;
					x = T("n", E, B); if(B < 0 && n[0] && n[0].t == "t") { x = x.substr(1);
						n[0].v = "-" + n[0].v } } k = x.length - 1; var R = n.length; for(s = 0; s < n.length; ++s)
					if(n[s] != null && n[s].t != "t" && n[s].v.indexOf(".") > -1) { R = s; break }
				var D = n.length; if(R === n.length && x.indexOf("E") === -1) { for(s = n.length - 1; s >= 0; --s) { if(n[s] == null || "n?(".indexOf(n[s].t) === -1) continue; if(k >= n[s].v.length - 1) { k -= n[s].v.length;
							n[s].v = x.substr(k + 1, n[s].v.length) } else if(k < 0) n[s].v = "";
						else { n[s].v = x.substr(0, k + 1);
							k = -1 } n[s].t = "t";
						D = s } if(k >= 0 && D < n.length) n[D].v = x.substr(0, k + 1) + n[D].v } else if(R !== n.length && x.indexOf("E") === -1) { k = x.indexOf(".") - 1; for(s = R; s >= 0; --s) { if(n[s] == null || "n?(".indexOf(n[s].t) === -1) continue;
						h = n[s].v.indexOf(".") > -1 && s === R ? n[s].v.indexOf(".") - 1 : n[s].v.length - 1;
						A = n[s].v.substr(h + 1); for(; h >= 0; --h) { if(k >= 0 && (n[s].v.charAt(h) === "0" || n[s].v.charAt(h) === "#")) A = x.charAt(k--) + A } n[s].v = A;
						n[s].t = "t";
						D = s } if(k >= 0 && D < n.length) n[D].v = x.substr(0, k + 1) + n[D].v;
					k = x.indexOf(".") + 1; for(s = R; s < n.length; ++s) { if(n[s] == null || "n?(".indexOf(n[s].t) === -1 && s !== R) continue;
						h = n[s].v.indexOf(".") > -1 && s === R ? n[s].v.indexOf(".") + 1 : 0;
						A = n[s].v.substr(0, h); for(; h < n[s].v.length; ++h) { if(k < x.length) A += x.charAt(k++) } n[s].v = A;
						n[s].t = "t";
						D = s } } } for(s = 0; s < n.length; ++s)
				if(n[s] != null && "n(?".indexOf(n[s].t) > -1) { B = a > 1 && r < 0 && s > 0 && n[s - 1].v === "-" ? -r : r;
					n[s].v = T(n[s].t, n[s].v, B);
					n[s].t = "t" }
			var O = ""; for(s = 0; s !== n.length; ++s)
				if(n[s] != null) O += n[s].v; return O } e._eval = R;
		var D = /\[[=<>]/;
		var O = /\[(=|>[=]?|<[>=]?)(-?\d+(?:\.\d*)?)\]/;

		function F(e, r) { if(r == null) return false; var t = parseFloat(r[2]); switch(r[1]) {
				case "=":
					if(e == t) return true; break;
				case ">":
					if(e > t) return true; break;
				case "<":
					if(e < t) return true; break;
				case "<>":
					if(e != t) return true; break;
				case ">=":
					if(e >= t) return true; break;
				case "<=":
					if(e <= t) return true; break; } return false }

		function P(e, r) { var t = x(e); var a = t.length,
				n = t[a - 1].indexOf("@"); if(a < 4 && n > -1) --a; if(t.length > 4) throw new Error("cannot find right format for |" + t.join("|") + "|"); if(typeof r !== "number") return [4, t.length === 4 || n > -1 ? t[t.length - 1] : "@"]; switch(t.length) {
				case 1:
					t = n > -1 ? ["General", "General", "General", t[0]] : [t[0], t[0], t[0], "@"]; break;
				case 2:
					t = n > -1 ? [t[0], t[0], t[0], t[1]] : [t[0], t[1], t[0], "@"]; break;
				case 3:
					t = n > -1 ? [t[0], t[1], t[0], t[2]] : [t[0], t[1], t[2], "@"]; break;
				case 4:
					break; } var i = r > 0 ? t[0] : r < 0 ? t[1] : t[2]; if(t[0].indexOf("[") === -1 && t[1].indexOf("[") === -1) return [a, i]; if(t[0].match(D) != null || t[1].match(D) != null) { var s = t[0].match(O); var f = t[1].match(O); return F(r, s) ? [a, t[0]] : F(r, f) ? [a, t[1]] : [a, t[s != null && f != null ? 2 : 1]] } return [a, i] }

		function N(e, r, t) { if(t == null) t = {}; var a = ""; switch(typeof e) {
				case "string":
					if(e == "m/d/yy" && t.dateNF) a = t.dateNF;
					else a = e; break;
				case "number":
					if(e == 14 && t.dateNF) a = t.dateNF;
					else a = (t.table != null ? t.table : p)[e]; break; } if(c(a, 0)) return S(r, t); if(r instanceof Date) r = w(r, t.date1904); var n = P(a, r); if(c(n[1])) return S(r, t); if(r === true) r = "TRUE";
			else if(r === false) r = "FALSE";
			else if(r === "" || r == null) return ""; return R(n[1], r, t, n[0]) }

		function L(e, r) { if(typeof r != "number") { r = +r || -1; for(var t = 0; t < 392; ++t) { if(p[t] == undefined) { if(r < 0) r = t; continue } if(p[t] == e) { r = t; break } } if(r < 0) r = 391 } p[r] = e; return r } e.load = L;
		e._table = p;
		e.get_table = function H() { return p };
		e.load_table = function W(e) { for(var r = 0; r != 392; ++r)
				if(e[r] !== undefined) L(e[r], r) };
		e.init_table = d;
		e.format = N
	};
	I(y);
	var R = { "General Number": "General", "General Date": y._table[22], "Long Date": "dddd, mmmm dd, yyyy", "Medium Date": y._table[15], "Short Date": y._table[14], "Long Time": y._table[19], "Medium Time": y._table[18], "Short Time": y._table[20], Currency: '"$"#,##0.00_);[Red]\\("$"#,##0.00\\)', Fixed: y._table[2], Standard: y._table[4], Percent: y._table[10], Scientific: y._table[11], "Yes/No": '"Yes";"Yes";"No";@', "True/False": '"True";"True";"False";@', "On/Off": '"Yes";"Yes";"No";@' };
	var D = { 5: '"$"#,##0_);\\("$"#,##0\\)', 6: '"$"#,##0_);[Red]\\("$"#,##0\\)', 7: '"$"#,##0.00_);\\("$"#,##0.00\\)', 8: '"$"#,##0.00_);[Red]\\("$"#,##0.00\\)', 23: "General", 24: "General", 25: "General", 26: "General", 27: "m/d/yy", 28: "m/d/yy", 29: "m/d/yy", 30: "m/d/yy", 31: "m/d/yy", 32: "h:mm:ss", 33: "h:mm:ss", 34: "h:mm:ss", 35: "h:mm:ss", 36: "m/d/yy", 41: '_(* #,##0_);_(* (#,##0);_(* "-"_);_(@_)', 42: '_("$"* #,##0_);_("$"* (#,##0);_("$"* "-"_);_(@_)', 43: '_(* #,##0.00_);_(* (#,##0.00);_(* "-"??_);_(@_)', 44: '_("$"* #,##0.00_);_("$"* (#,##0.00);_("$"* "-"??_);_(@_)', 50: "m/d/yy", 51: "m/d/yy", 52: "m/d/yy", 53: "m/d/yy", 54: "m/d/yy", 55: "m/d/yy", 56: "m/d/yy", 57: "m/d/yy", 58: "m/d/yy", 59: "0", 60: "0.00", 61: "#,##0", 62: "#,##0.00", 63: '"$"#,##0_);\\("$"#,##0\\)', 64: '"$"#,##0_);[Red]\\("$"#,##0\\)', 65: '"$"#,##0.00_);\\("$"#,##0.00\\)', 66: '"$"#,##0.00_);[Red]\\("$"#,##0.00\\)', 67: "0%", 68: "0.00%", 69: "# ?/?", 70: "# ??/??", 71: "m/d/yy", 72: "m/d/yy", 73: "d-mmm-yy", 74: "d-mmm", 75: "mmm-yy", 76: "h:mm", 77: "h:mm:ss", 78: "m/d/yy h:mm", 79: "mm:ss", 80: "[h]:mm:ss", 81: "mmss.0" };
	var O = /[dD]+|[mM]+|[yYeE]+|[Hh]+|[Ss]+/g;

	function F(e) { var r = typeof e == "number" ? y._table[e] : e;
		r = r.replace(O, "(\\d+)"); return new RegExp("^" + r + "$") }

	function P(e, r, t) { var a = -1,
			n = -1,
			i = -1,
			s = -1,
			f = -1,
			o = -1;
		(r.match(O) || []).forEach(function(e, r) { var l = parseInt(t[r + 1], 10); switch(e.toLowerCase().charAt(0)) {
				case "y":
					a = l; break;
				case "d":
					i = l; break;
				case "h":
					s = l; break;
				case "s":
					o = l; break;
				case "m":
					if(s >= 0) f = l;
					else n = l; break; } }); if(o >= 0 && f == -1 && n >= 0) { f = n;
			n = -1 } var l = ("" + (a >= 0 ? a : (new Date).getFullYear())).slice(-4) + "-" + ("00" + (n >= 1 ? n : 1)).slice(-2) + "-" + ("00" + (i >= 1 ? i : 1)).slice(-2); if(l.length == 7) l = "0" + l; if(l.length == 8) l = "20" + l; var c = ("00" + (s >= 0 ? s : 0)).slice(-2) + ":" + ("00" + (f >= 0 ? f : 0)).slice(-2) + ":" + ("00" + (o >= 0 ? o : 0)).slice(-2); if(s == -1 && f == -1 && o == -1) return l; if(a == -1 && n == -1 && i == -1) return c; return l + "T" + c }
	var N = true;
	var L = function Lg() {
		var e = {};
		e.version = "1.0.7";

		function r(e, r) { var t = e.split("/"),
				a = r.split("/"); for(var n = 0, i = 0, s = Math.min(t.length, a.length); n < s; ++n) { if(i = t[n].length - a[n].length) return i; if(t[n] != a[n]) return t[n] < a[n] ? -1 : 1 } return t.length - a.length }

		function t(e) { if(e.charAt(e.length - 1) == "/") return e.slice(0, -1).indexOf("/") === -1 ? e : t(e.slice(0, -1)); var r = e.lastIndexOf("/"); return r === -1 ? e : e.slice(0, r + 1) }

		function a(e) { if(e.charAt(e.length - 1) == "/") return a(e.slice(0, -1)); var r = e.lastIndexOf("/"); return r === -1 ? e : e.slice(r + 1) }
		var n;

		function i() { return n || (n = require("fs")) }

		function s(e, r) { if(e.length < 512) throw new Error("CFB file size " + e.length + " < 512"); var t = 3; var a = 512; var n = 0; var i = 0; var s = 0; var h = 0; var d = 0; var g = []; var m = e.slice(0, 512);
			Hr(m, 0); var b = f(m);
			t = b[0]; switch(t) {
				case 3:
					a = 512; break;
				case 4:
					a = 4096; break;
				default:
					throw new Error("Major Version: Expected 3 or 4 saw " + t); } if(a !== 512) { m = e.slice(0, a);
				Hr(m, 28) } var C = e.slice(0, a);
			o(m, t); var w = m._R(4, "i"); if(t === 3 && w !== 0) throw new Error("# Directory Sectors: Expected 0 saw " + w);
			m.l += 4;
			s = m._R(4, "i");
			m.l += 4;
			m.chk("00100000", "Mini Stream Cutoff Size: ");
			h = m._R(4, "i");
			n = m._R(4, "i");
			d = m._R(4, "i");
			i = m._R(4, "i"); for(var E = -1, k = 0; k < 109; ++k) { E = m._R(4, "i"); if(E < 0) break;
				g[k] = E } var S = l(e, a);
			u(d, i, S, a, g); var A = p(S, s, g, a);
			A[s].name = "!Directory"; if(n > 0 && h !== I) A[h].name = "!MiniFAT";
			A[g[0]].name = "!FAT";
			A.fat_addrs = g;
			A.ssz = a; var _ = {},
				B = [],
				T = [],
				x = [];
			v(s, A, S, B, n, _, T, h);
			c(T, x, B);
			B.shift(); var y = { FileIndex: T, FullPaths: x }; if(r && r.raw) y.raw = { header: C, sectors: S }; return y }

		function f(e) { e.chk(R, "Header Signature: ");
			e.chk(O, "CLSID: "); var r = e._R(2, "u"); return [e._R(2, "u"), r] }

		function o(e, r) { var t = 9;
			e.l += 2; switch(t = e._R(2)) {
				case 9:
					if(r != 3) throw new Error("Sector Shift: Expected 9 saw " + t); break;
				case 12:
					if(r != 4) throw new Error("Sector Shift: Expected 12 saw " + t); break;
				default:
					throw new Error("Sector Shift: Expected 9 or 12 saw " + t); } e.chk("0600", "Mini Sector Shift: ");
			e.chk("000000000000", "Reserved: ") }

		function l(e, r) { var t = Math.ceil(e.length / r) - 1; var a = []; for(var n = 1; n < t; ++n) a[n - 1] = e.slice(n * r, (n + 1) * r);
			a[t - 1] = e.slice(t * r); return a }

		function c(e, r, t) { var a = 0,
				n = 0,
				i = 0,
				s = 0,
				f = 0,
				o = t.length; var l = [],
				c = []; for(; a < o; ++a) { l[a] = c[a] = a;
				r[a] = t[a] } for(; f < c.length; ++f) { a = c[f];
				n = e[a].L;
				i = e[a].R;
				s = e[a].C; if(l[a] === a) { if(n !== -1 && l[n] !== n) l[a] = l[n]; if(i !== -1 && l[i] !== i) l[a] = l[i] } if(s !== -1) l[s] = a; if(n !== -1) { l[n] = l[a]; if(c.lastIndexOf(n) < f) c.push(n) } if(i !== -1) { l[i] = l[a]; if(c.lastIndexOf(i) < f) c.push(i) } } for(a = 1; a < o; ++a)
				if(l[a] === a) { if(i !== -1 && l[i] !== i) l[a] = l[i];
					else if(n !== -1 && l[n] !== n) l[a] = l[n] }
			for(a = 1; a < o; ++a) { if(e[a].type === 0) continue;
				f = l[a]; if(f === 0) r[a] = r[0] + "/" + r[a];
				else
					while(f !== 0 && f !== l[f]) { r[a] = r[f] + "/" + r[a];
						f = l[f] } l[a] = 0 } r[0] += "/"; for(a = 1; a < o; ++a) { if(e[a].type !== 2) r[a] += "/" } }

		function h(e, r, t) { var a = e.start,
				n = e.size; var i = []; var s = a; while(t && n > 0 && s >= 0) { i.push(r.slice(s * y, s * y + y));
				n -= y;
				s = Dr(t, s * 4) } if(i.length === 0) return Vr(0); return B(i).slice(0, e.size) }

		function u(e, r, t, a, n) { var i = I; if(e === I) { if(r !== 0) throw new Error("DIFAT chain shorter than expected") } else if(e !== -1) { var s = t[e],
					f = (a >>> 2) - 1; if(!s) return; for(var o = 0; o < f; ++o) { if((i = Dr(s, o * 4)) === I) break;
					n.push(i) } u(Dr(s, a - 4), r - 1, t, a, n) } }

		function d(e, r, t, a, n) { var i = [],
				s = []; if(!n) n = []; var f = a - 1,
				o = 0,
				l = 0; for(o = r; o >= 0;) { n[o] = true;
				i[i.length] = o;
				s.push(e[o]); var c = t[Math.floor(o * 4 / a)];
				l = o * 4 & f; if(a < 4 + l) throw new Error("FAT boundary crossed: " + o + " 4 " + a); if(!e[c]) break;
				o = Dr(e[c], l) } return { nodes: i, data: fr([s]) } }

		function p(e, r, t, a) { var n = e.length,
				i = []; var s = [],
				f = [],
				o = []; var l = a - 1,
				c = 0,
				h = 0,
				u = 0,
				d = 0; for(c = 0; c < n; ++c) { f = [];
				u = c + r; if(u >= n) u -= n; if(s[u]) continue;
				o = []; for(h = u; h >= 0;) { s[h] = true;
					f[f.length] = h;
					o.push(e[h]); var p = t[Math.floor(h * 4 / a)];
					d = h * 4 & l; if(a < 4 + d) throw new Error("FAT boundary crossed: " + h + " 4 " + a); if(!e[p]) break;
					h = Dr(e[p], d) } i[u] = { nodes: f, data: fr([o]) } } return i }

		function v(e, r, t, a, n, i, s, f) { var o = 0,
				l = a.length ? 2 : 0; var c = r[e].data; var u = 0,
				p = 0,
				v; for(; u < c.length; u += 128) { var m = c.slice(u, u + 128);
				Hr(m, 64);
				p = m._R(2);
				v = lr(m, 0, p - l);
				a.push(v); var b = { name: v, type: m._R(1), color: m._R(1), L: m._R(4, "i"), R: m._R(4, "i"), C: m._R(4, "i"), clsid: m._R(16), state: m._R(4, "i"), start: 0, size: 0 }; var C = m._R(2) + m._R(2) + m._R(2) + m._R(2); if(C !== 0) b.ct = g(m, m.l - 8); var w = m._R(2) + m._R(2) + m._R(2) + m._R(2); if(w !== 0) b.mt = g(m, m.l - 8);
				b.start = m._R(4, "i");
				b.size = m._R(4, "i"); if(b.size < 0 && b.start < 0) { b.size = b.type = 0;
					b.start = I;
					b.name = "" } if(b.type === 5) { o = b.start; if(n > 0 && o !== I) r[o].name = "!StreamData" } else if(b.size >= 4096) { b.storage = "fat"; if(r[b.start] === undefined) r[b.start] = d(t, b.start, r.fat_addrs, r.ssz);
					r[b.start].name = b.name;
					b.content = r[b.start].data.slice(0, b.size) } else { b.storage = "minifat"; if(b.size < 0) b.size = 0;
					else if(o !== I && b.start !== I && r[o]) { b.content = h(b, r[o].data, (r[f] || {}).data) } } if(b.content) Hr(b.content, 0);
				i[v] = b;
				s.push(b) } }

		function g(e, r) { return new Date((Rr(e, r + 4) / 1e7 * Math.pow(2, 32) + Rr(e, r) / 1e7 - 11644473600) * 1e3) }

		function m(e, r) { i(); return s(n.readFileSync(e), r) }

		function C(e, r) { switch(r && r.type || "base64") {
				case "file":
					return m(e, r);
				case "base64":
					return s(E(b.decode(e)), r);
				case "binary":
					return s(E(e), r); } return s(e, r) }

		function w(e, r) { var t = r || {},
				a = t.root || "Root Entry"; if(!e.FullPaths) e.FullPaths = []; if(!e.FileIndex) e.FileIndex = []; if(e.FullPaths.length !== e.FileIndex.length) throw new Error("inconsistent CFB structure"); if(e.FullPaths.length === 0) { e.FullPaths[0] = a + "/";
				e.FileIndex[0] = { name: a, type: 5 } } if(t.CLSID) e.FileIndex[0].clsid = t.CLSID;
			k(e) }

		function k(e) { var r = "Sh33tJ5"; if(L.find(e, "/" + r)) return; var t = Vr(4);
			t[0] = 55;
			t[1] = t[3] = 50;
			t[2] = 54;
			e.FileIndex.push({ name: r, type: 2, content: t, size: 4, L: 69, R: 69, C: 69 });
			e.FullPaths.push(e.FullPaths[0] + r);
			S(e) }

		function S(e, n) { w(e); var i = false,
				s = false; for(var f = e.FullPaths.length - 1; f >= 0; --f) { var o = e.FileIndex[f]; switch(o.type) {
					case 0:
						if(s) i = true;
						else { e.FileIndex.pop();
							e.FullPaths.pop() } break;
					case 1:
						;
					case 2:
						;
					case 5:
						s = true; if(isNaN(o.R * o.L * o.C)) i = true; if(o.R > -1 && o.L > -1 && o.R == o.L) i = true; break;
					default:
						i = true; break; } } if(!i && !n) return; var l = new Date(1987, 1, 19),
				c = 0; var h = []; for(f = 0; f < e.FullPaths.length; ++f) { if(e.FileIndex[f].type === 0) continue;
				h.push([e.FullPaths[f], e.FileIndex[f]]) } for(f = 0; f < h.length; ++f) { var u = t(h[f][0]);
				s = false; for(c = 0; c < h.length; ++c)
					if(h[c][0] === u) s = true; if(!s) h.push([u, { name: a(u).replace("/", ""), type: 1, clsid: O, ct: l, mt: l, content: null }]) } h.sort(function(e, t) { return r(e[0], t[0]) });
			e.FullPaths = [];
			e.FileIndex = []; for(f = 0; f < h.length; ++f) { e.FullPaths[f] = h[f][0];
				e.FileIndex[f] = h[f][1] } for(f = 0; f < h.length; ++f) { var d = e.FileIndex[f]; var p = e.FullPaths[f];
				d.name = a(p).replace("/", "");
				d.L = d.R = d.C = -(d.color = 1);
				d.size = d.content ? d.content.length : 0;
				d.start = 0;
				d.clsid = d.clsid || O; if(f === 0) { d.C = h.length > 1 ? 1 : -1;
					d.size = 0;
					d.type = 5 } else if(p.slice(-1) == "/") { for(c = f + 1; c < h.length; ++c)
						if(t(e.FullPaths[c]) == p) break;
					d.C = c >= h.length ? -1 : c; for(c = f + 1; c < h.length; ++c)
						if(t(e.FullPaths[c]) == t(p)) break;
					d.R = c >= h.length ? -1 : c;
					d.type = 1 } else { if(t(e.FullPaths[f + 1] || "") == t(p)) d.R = f + 1;
					d.type = 2 } } }

		function A(e, r) { var t = r || {};
			S(e); var a = function(e) { var r = 0,
					t = 0; for(var a = 0; a < e.FileIndex.length; ++a) { var n = e.FileIndex[a]; if(!n.content) continue; var i = n.content.length; if(i > 0) { if(i < 4096) r += i + 63 >> 6;
						else t += i + 511 >> 9 } } var s = e.FullPaths.length + 3 >> 2; var f = r + 7 >> 3; var o = r + 127 >> 7; var l = f + t + s + o; var c = l + 127 >> 7; var h = c <= 109 ? 0 : Math.ceil((c - 109) / 127); while(l + c + h + 127 >> 7 > c) h = ++c <= 109 ? 0 : Math.ceil((c - 109) / 127); var u = [1, h, c, o, s, t, r, 0];
				e.FileIndex[0].size = r << 6;
				u[7] = (e.FileIndex[0].start = u[0] + u[1] + u[2] + u[3] + u[4] + u[5]) + (u[6] + 7 >> 3); return u }(e); var n = Vr(a[7] << 9); var i = 0,
				s = 0; { for(i = 0; i < 8; ++i) n._W(1, D[i]); for(i = 0; i < 8; ++i) n._W(2, 0);
				n._W(2, 62);
				n._W(2, 3);
				n._W(2, 65534);
				n._W(2, 9);
				n._W(2, 6); for(i = 0; i < 3; ++i) n._W(2, 0);
				n._W(4, 0);
				n._W(4, a[2]);
				n._W(4, a[0] + a[1] + a[2] + a[3] - 1);
				n._W(4, 0);
				n._W(4, 1 << 12);
				n._W(4, a[3] ? a[0] + a[1] + a[2] - 1 : I);
				n._W(4, a[3]);
				n._W(-4, a[1] ? a[0] - 1 : I);
				n._W(4, a[1]); for(i = 0; i < 109; ++i) n._W(-4, i < a[2] ? a[1] + i : -1) } if(a[1]) { for(s = 0; s < a[1]; ++s) { for(; i < 236 + s * 127; ++i) n._W(-4, i < a[2] ? a[1] + i : -1);
					n._W(-4, s === a[1] - 1 ? I : s + 1) } } var f = function(e) { for(s += e; i < s - 1; ++i) n._W(-4, i + 1); if(e) {++i;
					n._W(-4, I) } };
			s = i = 0; for(s += a[1]; i < s; ++i) n._W(-4, F.DIFSECT); for(s += a[2]; i < s; ++i) n._W(-4, F.FATSECT);
			f(a[3]);
			f(a[4]); var o = 0,
				l = 0; var c = e.FileIndex[0]; for(; o < e.FileIndex.length; ++o) { c = e.FileIndex[o]; if(!c.content) continue;
				l = c.content.length; if(l < 4096) continue;
				c.start = s;
				f(l + 511 >> 9) } f(a[6] + 7 >> 3); while(n.l & 511) n._W(-4, F.ENDOFCHAIN);
			s = i = 0; for(o = 0; o < e.FileIndex.length; ++o) { c = e.FileIndex[o]; if(!c.content) continue;
				l = c.content.length; if(!l || l >= 4096) continue;
				c.start = s;
				f(l + 63 >> 6) } while(n.l & 511) n._W(-4, F.ENDOFCHAIN); for(i = 0; i < a[4] << 2; ++i) { var h = e.FullPaths[i]; if(!h || h.length === 0) { for(o = 0; o < 17; ++o) n._W(4, 0); for(o = 0; o < 3; ++o) n._W(4, -1); for(o = 0; o < 12; ++o) n._W(4, 0); continue } c = e.FileIndex[i]; if(i === 0) c.start = c.size ? c.start - 1 : I; var u = i === 0 && t.root || c.name;
				l = 2 * (u.length + 1);
				n._W(64, u, "utf16le");
				n._W(2, l);
				n._W(1, c.type);
				n._W(1, c.color);
				n._W(-4, c.L);
				n._W(-4, c.R);
				n._W(-4, c.C); if(!c.clsid)
					for(o = 0; o < 4; ++o) n._W(4, 0);
				else n._W(16, c.clsid, "hex");
				n._W(4, c.state || 0);
				n._W(4, 0);
				n._W(4, 0);
				n._W(4, 0);
				n._W(4, 0);
				n._W(4, c.start);
				n._W(4, c.size);
				n._W(4, 0) } for(i = 1; i < e.FileIndex.length; ++i) { c = e.FileIndex[i]; if(c.size >= 4096) { n.l = c.start + 1 << 9; for(o = 0; o < c.size; ++o) n._W(1, c.content[o]); for(; o & 511; ++o) n._W(1, 0) } } for(i = 1; i < e.FileIndex.length; ++i) { c = e.FileIndex[i]; if(c.size > 0 && c.size < 4096) { for(o = 0; o < c.size; ++o) n._W(1, c.content[o]); for(; o & 63; ++o) n._W(1, 0) } } while(n.l < n.length) n._W(1, 0); return n }

		function _(e, r) {
			var t = e.FullPaths.map(function(e) {
				return e.toUpperCase()
			});
			var a = t.map(function(e) { var r = e.split("/"); return r[r.length - (e.slice(-1) == "/" ? 2 : 1)] });
			var n = false;
			if(r.charCodeAt(0) === 47) { n = true;
				r = t[0].slice(0, -1) + r } else n = r.indexOf("/") !== -1;
			var i = r.toUpperCase();
			var s = n === true ? t.indexOf(i) : a.indexOf(i);
			if(s !== -1) return e.FileIndex[s];
			var f = !i.match(x);
			i = i.replace(T, "");
			if(f) i = i.replace(x, "!");
			for(s = 0; s < t.length; ++s) { if((f ? t[s].replace(x, "!") : t[s]).replace(T, "") == i) return e.FileIndex[s]; if((f ? a[s].replace(x, "!") : a[s]).replace(T, "") == i) return e.FileIndex[s] }
			return null
		}
		var y = 64;
		var I = -2;
		var R = "d0cf11e0a1b11ae1";
		var D = [208, 207, 17, 224, 161, 177, 26, 225];
		var O = "00000000000000000000000000000000";
		var F = { MAXREGSECT: -6, DIFSECT: -4, FATSECT: -3, ENDOFCHAIN: I, FREESECT: -1, HEADER_SIGNATURE: R, HEADER_MINOR_VERSION: "3e00", MAXREGSID: -6, NOSTREAM: -1, HEADER_CLSID: O, EntryTypes: ["unknown", "storage", "stream", "lockbytes", "property", "root"] };

		function P(e, r, t) { i(); var a = A(e, t);
			n.writeFileSync(r, a) }

		function N(e) { var r = new Array(e.length); for(var t = 0; t < e.length; ++t) r[t] = String.fromCharCode(e[t]); return r.join("") }

		function M(e, r) { var t = A(e, r); switch(r && r.type) {
				case "file":
					i();
					n.writeFileSync(r.filename, t); return t;
				case "binary":
					return N(t);
				case "base64":
					return b.encode(N(t)); } return t }

		function U(e) { var r = {};
			w(r, e); return r }

		function H(e, r, t, n) { var i = n && n.unsafe; if(!i) w(e); var s = !i && L.find(e, r); if(!s) { var f = e.FullPaths[0]; if(r.slice(0, f.length) == f) f = r;
				else { if(f.slice(-1) != "/") f += "/";
					f = (f + r).replace("//", "/") } s = { name: a(r), type: 2 };
				e.FileIndex.push(s);
				e.FullPaths.push(f); if(!i) L.utils.cfb_gc(e) } s.content = t;
			s.size = t ? t.length : 0; if(n) { if(n.CLSID) s.clsid = n.CLSID } return s }

		function W(e, r) { w(e); var t = L.find(e, r); if(t)
				for(var a = 0; a < e.FileIndex.length; ++a)
					if(e.FileIndex[a] == t) { e.FileIndex.splice(a, 1);
						e.FullPaths.splice(a, 1); return true }
			return false }

		function V(e, r, t) { w(e); var n = L.find(e, r); if(n)
				for(var i = 0; i < e.FileIndex.length; ++i)
					if(e.FileIndex[i] == n) { e.FileIndex[i].name = a(t);
						e.FullPaths[i] = t; return true }
			return false }

		function z(e) { S(e, true) } e.find = _;
		e.read = C;
		e.parse = s;
		e.write = M;
		e.writeFile = P;
		e.utils = { cfb_new: U, cfb_add: H, cfb_del: W, cfb_mov: V, cfb_gc: z, ReadShift: Fr, CheckField: Ur, prep_blob: Hr, bconcat: B, consts: F };
		return e
	}();
	if(typeof require !== "undefined" && typeof module !== "undefined" && typeof N === "undefined") { module.exports = L }
	var M;
	if(typeof require !== "undefined") try { M = require("fs") } catch(U) {}

	function H(e) { if(typeof e === "string") return k(e); if(Array.isArray(e)) return A(e); return e }

	function W(e, r, t) { if(typeof M !== "undefined" && M.writeFileSync) return t ? M.writeFileSync(e, r, t) : M.writeFileSync(e, r); var a = t == "utf8" ? We(r) : r; if(typeof IE_SaveFile !== "undefined") return IE_SaveFile(a, e); if(typeof Blob !== "undefined") { var n = new Blob([H(a)], { type: "application/octet-stream" }); if(typeof navigator !== "undefined" && navigator.msSaveBlob) return navigator.msSaveBlob(n, e); if(typeof saveAs !== "undefined") return saveAs(n, e); if(typeof URL !== "undefined" && typeof document !== "undefined" && document.createElement && URL.createObjectURL) { var i = URL.createObjectURL(n); if(typeof chrome === "object" && typeof(chrome.downloads || {}).download == "function") { if(URL.revokeObjectURL && typeof setTimeout !== "undefined") setTimeout(function() { URL.revokeObjectURL(i) }, 6e4); return chrome.downloads.download({ url: i, filename: e, saveAs: true }) } var s = document.createElement("a"); if(s.download != null) { s.download = e;
					s.href = i;
					document.body.appendChild(s);
					s.click();
					document.body.removeChild(s); if(URL.revokeObjectURL && typeof setTimeout !== "undefined") setTimeout(function() { URL.revokeObjectURL(i) }, 6e4); return i } } } if(typeof $ !== "undefined" && typeof File !== "undefined" && typeof Folder !== "undefined") try { var f = File(e);
			f.open("w");
			f.encoding = "binary"; if(Array.isArray(r)) r = S(r);
			f.write(r);
			f.close(); return r } catch(o) { if(!o.message || !o.message.match(/onstruct/)) throw o }
		throw new Error("cannot save file " + e) }

	function V(e) { if(typeof M !== "undefined") return M.readFileSync(e); if(typeof $ !== "undefined" && typeof File !== "undefined" && typeof Folder !== "undefined") try { var r = File(e);
			r.open("r");
			r.encoding = "binary"; var t = r.read();
			r.close(); return t } catch(a) { if(!a.message || !a.message.match(/onstruct/)) throw a }
		throw new Error("Cannot access file " + e) }

	function z(e) { var r = Object.keys(e),
			t = []; for(var a = 0; a < r.length; ++a)
			if(e.hasOwnProperty(r[a])) t.push(r[a]); return t }

	function X(e, r) { var t = [],
			a = z(e); for(var n = 0; n !== a.length; ++n)
			if(t[e[a[n]][r]] == null) t[e[a[n]][r]] = a[n]; return t }

	function G(e) { var r = [],
			t = z(e); for(var a = 0; a !== t.length; ++a) r[e[t[a]]] = t[a]; return r }

	function j(e) { var r = [],
			t = z(e); for(var a = 0; a !== t.length; ++a) r[e[t[a]]] = parseInt(t[a], 10); return r }

	function K(e) { var r = [],
			t = z(e); for(var a = 0; a !== t.length; ++a) { if(r[e[t[a]]] == null) r[e[t[a]]] = [];
			r[e[t[a]]].push(t[a]) } return r }
	var Y = new Date(1899, 11, 30, 0, 0, 0);
	var Z = Y.getTime() + ((new Date).getTimezoneOffset() - Y.getTimezoneOffset()) * 6e4;

	function Q(e, r) { var t = e.getTime(); if(r) t -= 1462 * 24 * 60 * 60 * 1e3; return(t - Z) / (24 * 60 * 60 * 1e3) }

	function J(e) { var r = new Date;
		r.setTime(e * 24 * 60 * 60 * 1e3 + Z); return r }

	function q(e) { var r = 0,
			t = 0,
			a = false; var n = e.match(/P([0-9\.]+Y)?([0-9\.]+M)?([0-9\.]+D)?T([0-9\.]+H)?([0-9\.]+M)?([0-9\.]+S)?/); if(!n) throw new Error("|" + e + "| is not an ISO8601 Duration"); for(var i = 1; i != n.length; ++i) { if(!n[i]) continue;
			t = 1; if(i > 3) a = true; switch(n[i].slice(n[i].length - 1)) {
				case "Y":
					throw new Error("Unsupported ISO Duration Field: " + n[i].slice(n[i].length - 1));
				case "D":
					t *= 24;
				case "H":
					t *= 60;
				case "M":
					if(!a) throw new Error("Unsupported ISO Duration Field: M");
					else t *= 60;
				case "S":
					break; } r += t * parseInt(n[i], 10) } return r }
	var ee = new Date("2017-02-19T19:06:09.000Z");
	if(isNaN(ee.getFullYear())) ee = new Date("2/19/17");
	var re = ee.getFullYear() == 2017;

	function te(e, r) { var t = new Date(e); if(re) { if(r > 0) t.setTime(t.getTime() + t.getTimezoneOffset() * 60 * 1e3);
			else if(r < 0) t.setTime(t.getTime() - t.getTimezoneOffset() * 60 * 1e3); return t } if(e instanceof Date) return e; if(ee.getFullYear() == 1917 && !isNaN(t.getFullYear())) { var a = t.getFullYear(); if(e.indexOf("" + a) > -1) return t;
			t.setFullYear(t.getFullYear() + 100); return t } var n = e.match(/\d+/g) || ["2017", "2", "19", "0", "0", "0"]; var i = new Date(+n[0], +n[1] - 1, +n[2], +n[3] || 0, +n[4] || 0, +n[5] || 0); if(e.indexOf("Z") > -1) i = new Date(i.getTime() - i.getTimezoneOffset() * 60 * 1e3); return i }

	function ae(e) { var r = ""; for(var t = 0; t != e.length; ++t) r += String.fromCharCode(e[t]); return r }

	function ne(e) { if(typeof JSON != "undefined" && !Array.isArray(e)) return JSON.parse(JSON.stringify(e)); if(typeof e != "object" || e == null) return e; if(e instanceof Date) return new Date(e.getTime()); var r = {}; for(var t in e)
			if(e.hasOwnProperty(t)) r[t] = ne(e[t]); return r }

	function ie(e, r) { var t = ""; while(t.length < r) t += e; return t }

	function se(e) { var r = Number(e); if(!isNaN(r)) return r; var t = 1; var a = e.replace(/([\d]),([\d])/g, "$1$2").replace(/[$]/g, "").replace(/[%]/g, function() { t *= 100; return "" }); if(!isNaN(r = Number(a))) return r / t;
		a = a.replace(/[(](.*)[)]/, function(e, r) { t = -t; return r }); if(!isNaN(r = Number(a))) return r / t; return r }

	function fe(e) { var r = new Date(e),
			t = new Date(NaN); var a = r.getYear(),
			n = r.getMonth(),
			i = r.getDate(); if(isNaN(i)) return t; if(a < 0 || a > 8099) return t; if((n > 0 || i > 1) && a != 101) return r; if(e.toLowerCase().match(/jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec/)) return r; if(e.match(/[^-0-9:,\/\\]/)) return t; return r }
	var oe = "abacaba".split(/(:?b)/i).length == 5;

	function le(e, r, t) { if(oe || typeof r == "string") return e.split(r); var a = e.split(r),
			n = [a[0]]; for(var i = 1; i < a.length; ++i) { n.push(t);
			n.push(a[i]) } return n }

	function ce(e) { if(!e) return null; if(e.data) return p(e.data); if(e.asNodeBuffer && C) return p(e.asNodeBuffer().toString("binary")); if(e.asBinary) return p(e.asBinary()); if(e._data && e._data.getContent) return p(ae(Array.prototype.slice.call(e._data.getContent(), 0))); return null }

	function he(e) { if(!e) return null; if(e.data) return h(e.data); if(e.asNodeBuffer && C) return e.asNodeBuffer(); if(e._data && e._data.getContent) { var r = e._data.getContent(); if(typeof r == "string") return h(r); return Array.prototype.slice.call(r) } return null }

	function ue(e) { return e && e.name.slice(-4) === ".bin" ? he(e) : ce(e) }

	function de(e, r) { var t = z(e.files); var a = r.toLowerCase(),
			n = a.replace(/\//g, "\\"); for(var i = 0; i < t.length; ++i) { var s = t[i].toLowerCase(); if(a == s || n == s) return e.files[t[i]] } return null }

	function pe(e, r) { var t = de(e, r); if(t == null) throw new Error("Cannot find file " + r + " in zip"); return t }

	function ve(e, r, t) { if(!t) return ue(pe(e, r)); if(!r) return null; try { return ve(e, r) } catch(a) { return null } }

	function ge(e, r, t) { if(!t) return ce(pe(e, r)); if(!r) return null; try { return ge(e, r) } catch(a) { return null } }

	function me(e) { var r = z(e.files),
			t = []; for(var a = 0; a < r.length; ++a)
			if(r[a].slice(-1) != "/") t.push(r[a]); return t.sort() }
	var be;
	if(typeof JSZipSync !== "undefined") be = JSZipSync;
	if(typeof exports !== "undefined") { if(typeof module !== "undefined" && module.exports) { if(typeof be === "undefined") be = undefined } }

	function Ce(e, r) { var t = r.split("/"); if(r.slice(-1) != "/") t.pop(); var a = e.split("/"); while(a.length !== 0) { var n = a.shift(); if(n === "..") t.pop();
			else if(n !== ".") t.push(n) } return t.join("/") }
	var we = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>\r\n';
	var Ee = /([^"\s?>\/]+)\s*=\s*((?:")([^"]*)(?:")|(?:')([^']*)(?:')|([^'">\s]+))/g;
	var ke = /<[\/\?]?[a-zA-Z0-9:]+(?:\s+[^"\s?>\/]+\s*=\s*(?:"[^"]*"|'[^']*'|[^'">\s=]+))*\s?[\/\?]?>/g;
	if(!we.match(ke)) ke = /<[^>]*>/g;
	var Se = /<\w*:/,
		Ae = /<(\/?)\w+:/;

	function _e(e, r) { var t = {}; var a = 0,
			n = 0; for(; a !== e.length; ++a)
			if((n = e.charCodeAt(a)) === 32 || n === 10 || n === 13) break; if(!r) t[0] = e.slice(0, a); if(a === e.length) return t; var i = e.match(Ee),
			s = 0,
			f = "",
			o = 0,
			l = "",
			c = "",
			h = 1; if(i)
			for(o = 0; o != i.length; ++o) { c = i[o]; for(n = 0; n != c.length; ++n)
					if(c.charCodeAt(n) === 61) break;
				l = c.slice(0, n).trim(); while(c.charCodeAt(n + 1) == 32) ++n;
				h = (a = c.charCodeAt(n + 1)) == 34 || a == 39 ? 1 : 0;
				f = c.slice(n + 1 + h, c.length - h); for(s = 0; s != l.length; ++s)
					if(l.charCodeAt(s) === 58) break; if(s === l.length) { if(l.indexOf("_") > 0) l = l.slice(0, l.indexOf("_"));
					t[l] = f } else { var u = (s === 5 && l.slice(0, 5) === "xmlns" ? "xmlns" : "") + l.slice(s + 1); if(t[u] && l.slice(s - 3, s) == "ext") continue;
					t[u] = f } }
		return t }

	function Be(e) { return e.replace(Ae, "<$1") }
	var Te = { "&quot;": '"', "&apos;": "'", "&gt;": ">", "&lt;": "<", "&amp;": "&" };
	var xe = G(Te);
	var ye = function() { var e = /&(?:quot|apos|gt|lt|amp|#x?([\da-fA-F]+));/g,
			r = /_x([\da-fA-F]{4})_/g; return function t(a) { var n = a + "",
				i = n.indexOf("<![CDATA["); if(i == -1) return n.replace(e, function(e, r) { return Te[e] || String.fromCharCode(parseInt(r, e.indexOf("x") > -1 ? 16 : 10)) || e }).replace(r, function(e, r) { return String.fromCharCode(parseInt(r, 16)) }); var s = n.indexOf("]]>"); return t(n.slice(0, i)) + n.slice(i + 9, s) + t(n.slice(s + 3)) } }();
	var Ie = /[&<>'"]/g,
		Re = /[\u0000-\u0008\u000b-\u001f]/g;

	function De(e) { var r = e + ""; return r.replace(Ie, function(e) { return xe[e] }).replace(Re, function(e) { return "_x" + ("000" + e.charCodeAt(0).toString(16)).slice(-4) + "_" }) }

	function Oe(e) { return De(e).replace(/ /g, "_x0020_") }
	var Fe = /[\u0000-\u001f]/g;

	function Pe(e) { var r = e + ""; return r.replace(Ie, function(e) { return xe[e] }).replace(Fe, function(e) { return "&#x" + ("000" + e.charCodeAt(0).toString(16)).slice(-4) + ";" }) }

	function Ne(e) { var r = e + ""; return r.replace(Ie, function(e) { return xe[e] }).replace(Fe, function(e) { return "&#x" + e.charCodeAt(0).toString(16).toUpperCase() + ";" }) }
	var Le = function() { var e = /&#(\d+);/g;

		function r(e, r) { return String.fromCharCode(parseInt(r, 10)) } return function t(a) { return a.replace(e, r) } }();
	var Me = function() { return function e(r) { return r.replace(/(\r\n|[\r\n])/g, "&#10;") } }();

	function Ue(e) { switch(e) {
			case 1:
				;
			case true:
				;
			case "1":
				;
			case "true":
				;
			case "TRUE":
				return true;
			default:
				return false; } }
	var He = function Mg(e) { var r = "",
			t = 0,
			a = 0,
			n = 0,
			i = 0,
			s = 0,
			f = 0; while(t < e.length) { a = e.charCodeAt(t++); if(a < 128) { r += String.fromCharCode(a); continue } n = e.charCodeAt(t++); if(a > 191 && a < 224) { s = (a & 31) << 6;
				s |= n & 63;
				r += String.fromCharCode(s); continue } i = e.charCodeAt(t++); if(a < 240) { r += String.fromCharCode((a & 15) << 12 | (n & 63) << 6 | i & 63); continue } s = e.charCodeAt(t++);
			f = ((a & 7) << 18 | (n & 63) << 12 | (i & 63) << 6 | s & 63) - 65536;
			r += String.fromCharCode(55296 + (f >>> 10 & 1023));
			r += String.fromCharCode(56320 + (f & 1023)) } return r };
	var We = function(e) { var r = [],
			t = 0,
			a = 0,
			n = 0; while(t < e.length) { a = e.charCodeAt(t++); switch(true) {
				case a < 128:
					r.push(String.fromCharCode(a)); break;
				case a < 2048:
					r.push(String.fromCharCode(192 + (a >> 6)));
					r.push(String.fromCharCode(128 + (a & 63))); break;
				case a >= 55296 && a < 57344:
					a -= 55296;
					n = e.charCodeAt(t++) - 56320 + (a << 10);
					r.push(String.fromCharCode(240 + (n >> 18 & 7)));
					r.push(String.fromCharCode(144 + (n >> 12 & 63)));
					r.push(String.fromCharCode(128 + (n >> 6 & 63)));
					r.push(String.fromCharCode(128 + (n & 63))); break;
				default:
					r.push(String.fromCharCode(224 + (a >> 12)));
					r.push(String.fromCharCode(128 + (a >> 6 & 63)));
					r.push(String.fromCharCode(128 + (a & 63))); } } return r.join("") };
	if(C) { var Ve = function Ug(e) { var r = Buffer.alloc(2 * e.length),
				t, a, n = 1,
				i = 0,
				s = 0,
				f; for(a = 0; a < e.length; a += n) { n = 1; if((f = e.charCodeAt(a)) < 128) t = f;
				else if(f < 224) { t = (f & 31) * 64 + (e.charCodeAt(a + 1) & 63);
					n = 2 } else if(f < 240) { t = (f & 15) * 4096 + (e.charCodeAt(a + 1) & 63) * 64 + (e.charCodeAt(a + 2) & 63);
					n = 3 } else { n = 4;
					t = (f & 7) * 262144 + (e.charCodeAt(a + 1) & 63) * 4096 + (e.charCodeAt(a + 2) & 63) * 64 + (e.charCodeAt(a + 3) & 63);
					t -= 65536;
					s = 55296 + (t >>> 10 & 1023);
					t = 56320 + (t & 1023) } if(s !== 0) { r[i++] = s & 255;
					r[i++] = s >>> 8;
					s = 0 } r[i++] = t % 256;
				r[i++] = t >>> 8 } return r.slice(0, i).toString("ucs2") }; var ze = "foo bar baz芒聵聝冒聼聧拢"; if(He(ze) == Ve(ze)) He = Ve; var Xe = function Hg(e) { return Buffer.from(e, "binary").toString("utf8") }; if(He(ze) == Xe(ze)) He = Xe;
		We = function(e) { return Buffer.from(e, "utf8").toString("binary") } }
	var Ge = function() { var e = {}; return function r(t, a) { var n = t + "|" + (a || ""); if(e[n]) return e[n]; return e[n] = new RegExp("<(?:\\w+:)?" + t + '(?: xml:space="preserve")?(?:[^>]*)>([\\s\\S]*?)</(?:\\w+:)?' + t + ">", a || "") } }();
	var je = function() { var e = [
			["nbsp", " "],
			["middot", "路"],
			["quot", '"'],
			["apos", "'"],
			["gt", ">"],
			["lt", "<"],
			["amp", "&"]
		].map(function(e) { return [new RegExp("&" + e[0] + ";", "g"), e[1]] }); return function r(t) { var a = t.trim().replace(/\s+/g, " ").replace(/<\s*[bB][rR]\s*\/?>/g, "\n").replace(/<[^>]*>/g, ""); for(var n = 0; n < e.length; ++n) a = a.replace(e[n][0], e[n][1]); return a } }();
	var Ke = function() { var e = {}; return function r(t) { if(e[t] !== undefined) return e[t]; return e[t] = new RegExp("<(?:vt:)?" + t + ">([\\s\\S]*?)</(?:vt:)?" + t + ">", "g") } }();
	var Ye = /<\/?(?:vt:)?variant>/g,
		$e = /<(?:vt:)([^>]*)>([\s\S]*)</;

	function Ze(e, r) { var t = _e(e); var a = e.match(Ke(t.baseType)) || []; var n = []; if(a.length != t.size) { if(r.WTF) throw new Error("unexpected vector length " + a.length + " != " + t.size); return n } a.forEach(function(e) { var r = e.replace(Ye, "").match($e); if(r) n.push({ v: He(r[2]), t: r[1] }) }); return n }
	var Qe = /(^\s|\s$|\n)/;

	function Je(e, r) { return "<" + e + (r.match(Qe) ? ' xml:space="preserve"' : "") + ">" + r + "</" + e + ">" }

	function qe(e) { return z(e).map(function(r) { return " " + r + '="' + e[r] + '"' }).join("") }

	function er(e, r, t) { return "<" + e + (t != null ? qe(t) : "") + (r != null ? (r.match(Qe) ? ' xml:space="preserve"' : "") + ">" + r + "</" + e : "/") + ">" }

	function rr(e, r) { try { return e.toISOString().replace(/\.\d*/, "") } catch(t) { if(r) throw t } return "" }

	function tr(e) { switch(typeof e) {
			case "string":
				return er("vt:lpwstr", e);
			case "number":
				return er((e | 0) == e ? "vt:i4" : "vt:r8", String(e));
			case "boolean":
				return er("vt:bool", e ? "true" : "false"); } if(e instanceof Date) return er("vt:filetime", rr(e)); throw new Error("Unable to serialize " + e) }
	var ar = { dc: "http://purl.org/dc/elements/1.1/", dcterms: "http://purl.org/dc/terms/", dcmitype: "http://purl.org/dc/dcmitype/", mx: "http://schemas.microsoft.com/office/mac/excel/2008/main", r: "http://schemas.openxmlformats.org/officeDocument/2006/relationships", sjs: "http://schemas.openxmlformats.org/package/2006/sheetjs/core-properties", vt: "http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", xsi: "http://www.w3.org/2001/XMLSchema-instance", xsd: "http://www.w3.org/2001/XMLSchema" };
	ar.main = ["http://schemas.openxmlformats.org/spreadsheetml/2006/main", "http://purl.oclc.org/ooxml/spreadsheetml/main", "http://schemas.microsoft.com/office/excel/2006/main", "http://schemas.microsoft.com/office/excel/2006/2"];
	var nr = { o: "urn:schemas-microsoft-com:office:office", x: "urn:schemas-microsoft-com:office:excel", ss: "urn:schemas-microsoft-com:office:spreadsheet", dt: "uuid:C2F41010-65B3-11d1-A29F-00AA00C14882", mv: "http://macVmlSchemaUri", v: "urn:schemas-microsoft-com:vml", html: "http://www.w3.org/TR/REC-html40" };

	function ir(e, r) { var t = 1 - 2 * (e[r + 7] >>> 7); var a = ((e[r + 7] & 127) << 4) + (e[r + 6] >>> 4 & 15); var n = e[r + 6] & 15; for(var i = 5; i >= 0; --i) n = n * 256 + e[r + i]; if(a == 2047) return n == 0 ? t * Infinity : NaN; if(a == 0) a = -1022;
		else { a -= 1023;
			n += Math.pow(2, 52) } return t * Math.pow(2, a - 52) * n }

	function sr(e, r, t) { var a = (r < 0 || 1 / r == -Infinity ? 1 : 0) << 7,
			n = 0,
			i = 0; var s = a ? -r : r; if(!isFinite(s)) { n = 2047;
			i = isNaN(r) ? 26985 : 0 } else if(s == 0) n = i = 0;
		else { n = Math.floor(Math.log(s) / Math.LN2);
			i = s * Math.pow(2, 52 - n); if(n <= -1023 && (!isFinite(i) || i < Math.pow(2, 52))) { n = -1022 } else { i -= Math.pow(2, 52);
				n += 1023 } } for(var f = 0; f <= 5; ++f, i /= 256) e[t + f] = i & 255;
		e[t + 6] = (n & 15) << 4 | i & 15;
		e[t + 7] = n >> 4 | a }
	var fr = function(e) { var r = [],
			t = 10240; for(var a = 0; a < e[0].length; ++a)
			if(e[0][a])
				for(var n = 0, i = e[0][a].length; n < i; n += t) r.push.apply(r, e[0][a].slice(n, n + t)); return r };
	var or = fr;
	var lr = function(e, r, t) { var a = []; for(var n = r; n < t; n += 2) a.push(String.fromCharCode(yr(e, n))); return a.join("").replace(T, "") };
	var cr = lr;
	var hr = function(e, r, t) { var a = []; for(var n = r; n < r + t; ++n) a.push(("0" + e[n].toString(16)).slice(-2)); return a.join("") };
	var ur = hr;
	var dr = function(e, r, t) { var a = []; for(var n = r; n < t; n++) a.push(String.fromCharCode(xr(e, n))); return a.join("") };
	var pr = dr;
	var vr = function(e, r) { var t = Rr(e, r); return t > 0 ? dr(e, r + 4, r + 4 + t - 1) : "" };
	var gr = vr;
	var mr = function(e, r) { var t = Rr(e, r); return t > 0 ? dr(e, r + 4, r + 4 + t - 1) : "" };
	var br = mr;
	var Cr = function(e, r) { var t = 2 * Rr(e, r); return t > 0 ? dr(e, r + 4, r + 4 + t - 1) : "" };
	var wr = Cr;
	var Er, kr;
	Er = kr = function Wg(e, r) { var t = Rr(e, r); return t > 0 ? lr(e, r + 4, r + 4 + t) : "" };
	var Sr = function(e, r) { var t = Rr(e, r); return t > 0 ? dr(e, r + 4, r + 4 + t) : "" };
	var Ar = Sr;
	var _r, Br;
	_r = Br = function(e, r) { return ir(e, r) };
	var Tr = function Vg(e) { return Array.isArray(e) };
	if(C) { lr = function(e, r, t) { if(!Buffer.isBuffer(e)) return cr(e, r, t); return e.toString("utf16le", r, t).replace(T, "") };
		hr = function(e, r, t) { return Buffer.isBuffer(e) ? e.toString("hex", r, r + t) : ur(e, r, t) };
		vr = function zg(e, r) { if(!Buffer.isBuffer(e)) return gr(e, r); var t = e.readUInt32LE(r); return t > 0 ? e.toString("utf8", r + 4, r + 4 + t - 1) : "" };
		mr = function Xg(e, r) { if(!Buffer.isBuffer(e)) return br(e, r); var t = e.readUInt32LE(r); return t > 0 ? e.toString("utf8", r + 4, r + 4 + t - 1) : "" };
		Cr = function Gg(e, r) { if(!Buffer.isBuffer(e)) return wr(e, r); var t = 2 * e.readUInt32LE(r); return e.toString("utf16le", r + 4, r + 4 + t - 1) };
		Er = function jg(e, r) { if(!Buffer.isBuffer(e)) return kr(e, r); var t = e.readUInt32LE(r); return e.toString("utf16le", r + 4, r + 4 + t) };
		Sr = function Kg(e, r) { if(!Buffer.isBuffer(e)) return Ar(e, r); var t = e.readUInt32LE(r); return e.toString("utf8", r + 4, r + 4 + t) };
		dr = function Yg(e, r, t) { return Buffer.isBuffer(e) ? e.toString("utf8", r, t) : pr(e, r, t) };
		fr = function(e) { return e[0].length > 0 && Buffer.isBuffer(e[0][0]) ? Buffer.concat(e[0]) : or(e) };
		B = function(e) { return Buffer.isBuffer(e[0]) ? Buffer.concat(e) : [].concat.apply([], e) };
		_r = function $g(e, r) { if(Buffer.isBuffer(e)) return e.readDoubleLE(r); return Br(e, r) };
		Tr = function Zg(e) { return Buffer.isBuffer(e) || Array.isArray(e) } }
	if(typeof cptable !== "undefined") { lr = function(e, r, t) { return cptable.utils.decode(1200, e.slice(r, t)).replace(T, "") };
		dr = function(e, r, t) { return cptable.utils.decode(65001, e.slice(r, t)) };
		vr = function(e, r) { var t = Rr(e, r); return t > 0 ? cptable.utils.decode(a, e.slice(r + 4, r + 4 + t - 1)) : "" };
		mr = function(e, r) { var a = Rr(e, r); return a > 0 ? cptable.utils.decode(t, e.slice(r + 4, r + 4 + a - 1)) : "" };
		Cr = function(e, r) { var t = 2 * Rr(e, r); return t > 0 ? cptable.utils.decode(1200, e.slice(r + 4, r + 4 + t - 1)) : "" };
		Er = function(e, r) { var t = Rr(e, r); return t > 0 ? cptable.utils.decode(1200, e.slice(r + 4, r + 4 + t)) : "" };
		Sr = function(e, r) { var t = Rr(e, r); return t > 0 ? cptable.utils.decode(65001, e.slice(r + 4, r + 4 + t)) : "" } }
	var xr = function(e, r) { return e[r] };
	var yr = function(e, r) { return e[r + 1] * (1 << 8) + e[r] };
	var Ir = function(e, r) { var t = e[r + 1] * (1 << 8) + e[r]; return t < 32768 ? t : (65535 - t + 1) * -1 };
	var Rr = function(e, r) { return e[r + 3] * (1 << 24) + (e[r + 2] << 16) + (e[r + 1] << 8) + e[r] };
	var Dr = function(e, r) { return e[r + 3] << 24 | e[r + 2] << 16 | e[r + 1] << 8 | e[r] };
	var Or = function(e, r) { return e[r] << 24 | e[r + 1] << 16 | e[r + 2] << 8 | e[r + 3] };

	function Fr(e, r) { var a = "",
			n, i, s = [],
			f, o, l, c; switch(r) {
			case "dbcs":
				c = this.l; if(C && Buffer.isBuffer(this)) a = this.slice(this.l, this.l + 2 * e).toString("utf16le");
				else
					for(l = 0; l < e; ++l) { a += String.fromCharCode(yr(this, c));
						c += 2 } e *= 2; break;
			case "utf8":
				a = dr(this, this.l, this.l + e); break;
			case "utf16le":
				e *= 2;
				a = lr(this, this.l, this.l + e); break;
			case "wstr":
				if(typeof cptable !== "undefined") a = cptable.utils.decode(t, this.slice(this.l, this.l + 2 * e));
				else return Fr.call(this, e, "dbcs");
				e = 2 * e; break;
			case "lpstr-ansi":
				a = vr(this, this.l);
				e = 4 + Rr(this, this.l); break;
			case "lpstr-cp":
				a = mr(this, this.l);
				e = 4 + Rr(this, this.l); break;
			case "lpwstr":
				a = Cr(this, this.l);
				e = 4 + 2 * Rr(this, this.l); break;
			case "lpp4":
				e = 4 + Rr(this, this.l);
				a = Er(this, this.l); if(e & 2) e += 2; break;
			case "8lpp4":
				e = 4 + Rr(this, this.l);
				a = Sr(this, this.l); if(e & 3) e += 4 - (e & 3); break;
			case "cstr":
				e = 0;
				a = ""; while((f = xr(this, this.l + e++)) !== 0) s.push(v(f));
				a = s.join(""); break;
			case "_wstr":
				e = 0;
				a = ""; while((f = yr(this, this.l + e)) !== 0) { s.push(v(f));
					e += 2 } e += 2;
				a = s.join(""); break;
			case "dbcs-cont":
				a = "";
				c = this.l; for(l = 0; l < e; ++l) { if(this.lens && this.lens.indexOf(c) !== -1) { f = xr(this, c);
						this.l = c + 1;
						o = Fr.call(this, e - l, f ? "dbcs-cont" : "sbcs-cont"); return s.join("") + o } s.push(v(yr(this, c)));
					c += 2 } a = s.join("");
				e *= 2; break;
			case "cpstr":
				if(typeof cptable !== "undefined") { a = cptable.utils.decode(t, this.slice(this.l, this.l + e)); break };
			case "sbcs-cont":
				a = "";
				c = this.l; for(l = 0; l != e; ++l) { if(this.lens && this.lens.indexOf(c) !== -1) { f = xr(this, c);
						this.l = c + 1;
						o = Fr.call(this, e - l, f ? "dbcs-cont" : "sbcs-cont"); return s.join("") + o } s.push(v(xr(this, c)));
					c += 1 } a = s.join(""); break;
			default:
				switch(e) {
					case 1:
						n = xr(this, this.l);
						this.l++; return n;
					case 2:
						n = (r === "i" ? Ir : yr)(this, this.l);
						this.l += 2; return n;
					case 4:
						;
					case -4:
						if(r === "i" || (this[this.l + 3] & 128) === 0) { n = (e > 0 ? Dr : Or)(this, this.l);
							this.l += 4; return n } else { i = Rr(this, this.l);
							this.l += 4 } return i;
					case 8:
						;
					case -8:
						if(r === "f") { if(e == 8) i = _r(this, this.l);
							else i = _r([this[this.l + 7], this[this.l + 6], this[this.l + 5], this[this.l + 4], this[this.l + 3], this[this.l + 2], this[this.l + 1], this[this.l + 0]], 0);
							this.l += 8; return i } else e = 8;
					case 16:
						a = hr(this, this.l, e); break; }; } this.l += e; return a }
	var Pr = function(e, r, t) { e[t] = r & 255;
		e[t + 1] = r >>> 8 & 255;
		e[t + 2] = r >>> 16 & 255;
		e[t + 3] = r >>> 24 & 255 };
	var Nr = function(e, r, t) { e[t] = r & 255;
		e[t + 1] = r >> 8 & 255;
		e[t + 2] = r >> 16 & 255;
		e[t + 3] = r >> 24 & 255 };
	var Lr = function(e, r, t) { e[t] = r & 255;
		e[t + 1] = r >>> 8 & 255 };

	function Mr(e, r, t) { var a = 0,
			n = 0; if(t === "dbcs") { for(n = 0; n != r.length; ++n) Lr(this, r.charCodeAt(n), this.l + 2 * n);
			a = 2 * r.length } else if(t === "sbcs") { r = r.replace(/[^\x00-\x7F]/g, "_"); for(n = 0; n != r.length; ++n) this[this.l + n] = r.charCodeAt(n) & 255;
			a = r.length } else if(t === "hex") { for(; n < e; ++n) { this[this.l++] = parseInt(r.slice(2 * n, 2 * n + 2), 16) || 0 } return this } else if(t === "utf16le") { var i = Math.min(this.l + e, this.length); for(n = 0; n < Math.min(r.length, e); ++n) { var s = r.charCodeAt(n);
				this[this.l++] = s & 255;
				this[this.l++] = s >> 8 } while(this.l < i) this[this.l++] = 0; return this } else switch(e) {
			case 1:
				a = 1;
				this[this.l] = r & 255; break;
			case 2:
				a = 2;
				this[this.l] = r & 255;
				r >>>= 8;
				this[this.l + 1] = r & 255; break;
			case 3:
				a = 3;
				this[this.l] = r & 255;
				r >>>= 8;
				this[this.l + 1] = r & 255;
				r >>>= 8;
				this[this.l + 2] = r & 255; break;
			case 4:
				a = 4;
				Pr(this, r, this.l); break;
			case 8:
				a = 8; if(t === "f") { sr(this, r, this.l); break };
			case 16:
				break;
			case -4:
				a = 4;
				Nr(this, r, this.l); break; } this.l += a; return this }

	function Ur(e, r) { var t = hr(this, this.l, e.length >> 1); if(t !== e) throw new Error(r + "Expected " + e + " saw " + t);
		this.l += e.length >> 1 }

	function Hr(e, r) { e.l = r;
		e._R = Fr;
		e.chk = Ur;
		e._W = Mr }

	function Wr(e, r) { e.l += r }

	function Vr(e) { var r = w(e);
		Hr(r, 0); return r }

	function zr(e, r, t) { if(!e) return; var a, n, i;
		Hr(e, e.l || 0); var s = e.length,
			f = 0,
			o = 0; while(e.l < s) { f = e._R(1); if(f & 128) f = (f & 127) + ((e._R(1) & 127) << 7); var l = fv[f] || fv[65535];
			a = e._R(1);
			i = a & 127; for(n = 1; n < 4 && a & 128; ++n) i += ((a = e._R(1)) & 127) << 7 * n;
			o = e.l + i; var c = (l.f || Wr)(e, i, t);
			e.l = o; if(r(c, l.n, f)) return } }

	function Xr() { var e = [],
			r = C ? 256 : 2048; var t = function o(e) { var r = Vr(e);
			Hr(r, 0); return r }; var a = t(r); var n = function l() { if(!a) return; if(a.length > a.l) { a = a.slice(0, a.l);
				a.l = a.length } if(a.length > 0) e.push(a);
			a = null }; var i = function c(e) { if(a && e < a.length - a.l) return a;
			n(); return a = t(Math.max(e + 1, r)) }; var s = function h() { n(); return fr([e]) }; var f = function u(e) { n();
			a = e; if(a.l == null) a.l = a.length;
			i(r) }; return { next: i, push: f, end: s, _bufs: e } }

	function Gr(e, r, t, a) { var n = +ov[r],
			i; if(isNaN(n)) return; if(!a) a = fv[n].p || (t || []).length || 0;
		i = 1 + (n >= 128 ? 1 : 0) + 1; if(a >= 128) ++i; if(a >= 16384) ++i; if(a >= 2097152) ++i; var s = e.next(i); if(n <= 127) s._W(1, n);
		else { s._W(1, (n & 127) + 128);
			s._W(1, n >> 7) } for(var f = 0; f != 4; ++f) { if(a >= 128) { s._W(1, (a & 127) + 128);
				a >>= 7 } else { s._W(1, a); break } } if(a > 0 && Tr(t)) e.push(t) }

	function jr(e, r, t) { var a = ne(e); if(r.s) { if(a.cRel) a.c += r.s.c; if(a.rRel) a.r += r.s.r } else { if(a.cRel) a.c += r.c; if(a.rRel) a.r += r.r } if(!t || t.biff < 12) { while(a.c >= 256) a.c -= 256; while(a.r >= 65536) a.r -= 65536 } return a }

	function Kr(e, r, t) { var a = ne(e);
		a.s = jr(a.s, r.s, t);
		a.e = jr(a.e, r.s, t); return a }

	function Yr(e, r) { if(e.cRel && e.c < 0) { e = ne(e);
			e.c += r > 8 ? 16384 : 256 } if(e.rRel && e.r < 0) { e = ne(e);
			e.r += r > 8 ? 1048576 : r > 5 ? 65536 : 16384 } var t = ot(e); if(e.cRel === 0) t = nt(t); if(e.rRel === 0) t = et(t); return t }

	function $r(e, r) { if(e.s.r == 0 && !e.s.rRel) { if(e.e.r == (r.biff >= 12 ? 1048575 : r.biff >= 8 ? 65536 : 16384) && !e.e.rRel) { return(e.s.cRel ? "" : "$") + at(e.s.c) + ":" + (e.e.cRel ? "" : "$") + at(e.e.c) } } if(e.s.c == 0 && !e.s.cRel) { if(e.e.c == (r.biff >= 12 ? 65535 : 255) && !e.e.cRel) { return(e.s.rRel ? "" : "$") + qr(e.s.r) + ":" + (e.e.rRel ? "" : "$") + qr(e.e.r) } } return Yr(e.s, r.biff) + ":" + Yr(e.e, r.biff) }
	var Zr = {};
	var Qr = function(e, r) { var t; if(typeof r !== "undefined") t = r;
		else if(typeof require !== "undefined") { try { t = undefined } catch(a) { t = null } } e.rc4 = function(e, r) { var t = new Array(256); var a = 0,
				n = 0,
				i = 0,
				s = 0; for(n = 0; n != 256; ++n) t[n] = n; for(n = 0; n != 256; ++n) { i = i + t[n] + e[n % e.length].charCodeAt(0) & 255;
				s = t[n];
				t[n] = t[i];
				t[i] = s } n = i = 0; var f = Buffer(r.length); for(a = 0; a != r.length; ++a) { n = n + 1 & 255;
				i = (i + t[n]) % 256;
				s = t[n];
				t[n] = t[i];
				t[i] = s;
				f[a] = r[a] ^ t[t[n] + t[i] & 255] } return f };
		e.md5 = function(e) { if(!t) throw new Error("Unsupported crypto"); return t.createHash("md5").update(e).digest("hex") } };
	Qr(Zr, typeof crypto !== "undefined" ? crypto : undefined);

	function Jr(e) { return parseInt(rt(e), 10) - 1 }

	function qr(e) { return "" + (e + 1) }

	function et(e) { return e.replace(/([A-Z]|^)(\d+)$/, "$1$$$2") }

	function rt(e) { return e.replace(/\$(\d+)$/, "$1") }

	function tt(e) { var r = it(e),
			t = 0,
			a = 0; for(; a !== r.length; ++a) t = 26 * t + r.charCodeAt(a) - 64; return t - 1 }

	function at(e) { var r = ""; for(++e; e; e = Math.floor((e - 1) / 26)) r = String.fromCharCode((e - 1) % 26 + 65) + r; return r }

	function nt(e) { return e.replace(/^([A-Z])/, "$$$1") }

	function it(e) { return e.replace(/^\$([A-Z])/, "$1") }

	function st(e) { return e.replace(/(\$?[A-Z]*)(\$?\d*)/, "$1,$2").split(",") }

	function ft(e) { var r = st(e); return { c: tt(r[0]), r: Jr(r[1]) } }

	function ot(e) { return at(e.c) + qr(e.r) }

	function lt(e) { var r = e.split(":").map(ft); return { s: r[0], e: r[r.length - 1] } }

	function ct(e, r) { if(typeof r === "undefined" || typeof r === "number") { return ct(e.s, e.e) } if(typeof e !== "string") e = ot(e); if(typeof r !== "string") r = ot(r); return e == r ? e : e + ":" + r }

	function ht(e) { var r = { s: { c: 0, r: 0 }, e: { c: 0, r: 0 } }; var t = 0,
			a = 0,
			n = 0; var i = e.length; for(t = 0; a < i; ++a) { if((n = e.charCodeAt(a) - 64) < 1 || n > 26) break;
			t = 26 * t + n } r.s.c = --t; for(t = 0; a < i; ++a) { if((n = e.charCodeAt(a) - 48) < 0 || n > 9) break;
			t = 10 * t + n } r.s.r = --t; if(a === i || e.charCodeAt(++a) === 58) { r.e.c = r.s.c;
			r.e.r = r.s.r; return r } for(t = 0; a != i; ++a) { if((n = e.charCodeAt(a) - 64) < 1 || n > 26) break;
			t = 26 * t + n } r.e.c = --t; for(t = 0; a != i; ++a) { if((n = e.charCodeAt(a) - 48) < 0 || n > 9) break;
			t = 10 * t + n } r.e.r = --t; return r }

	function ut(e, r) { var t = e.t == "d" && r instanceof Date; if(e.z != null) try { return e.w = y.format(e.z, t ? Q(r) : r) } catch(a) {}
		try { return e.w = y.format((e.XF || {}).numFmtId || (t ? 14 : 0), t ? Q(r) : r) } catch(a) { return "" + r } }

	function dt(e, r, t) { if(e == null || e.t == null || e.t == "z") return ""; if(e.w !== undefined) return e.w; if(e.t == "d" && !e.z && t && t.dateNF) e.z = t.dateNF; if(r == undefined) return ut(e, e.v); return ut(e, r) }

	function pt(e, r) { var t = r && r.sheet ? r.sheet : "Sheet1"; var a = {};
		a[t] = e; return { SheetNames: [t], Sheets: a } }

	function vt(e, r, t) { var a = t || {}; var n = e ? Array.isArray(e) : a.dense; if(g != null && n == null) n = g; var i = e || (n ? [] : {}); var s = 0,
			f = 0; if(i && a.origin != null) { if(typeof a.origin == "number") s = a.origin;
			else { var o = typeof a.origin == "string" ? ft(a.origin) : a.origin;
				s = o.r;
				f = o.c } } var l = { s: { c: 1e7, r: 1e7 }, e: { c: 0, r: 0 } }; if(i["!ref"]) { var c = ht(i["!ref"]);
			l.s.c = c.s.c;
			l.s.r = c.s.r;
			l.e.c = Math.max(l.e.c, c.e.c);
			l.e.r = Math.max(l.e.r, c.e.r); if(s == -1) l.e.r = s = c.e.r + 1 } for(var h = 0; h != r.length; ++h) { for(var u = 0; u != r[h].length; ++u) { if(typeof r[h][u] === "undefined") continue; var d = { v: r[h][u] }; if(Array.isArray(d.v)) { d.f = r[h][u][1];
					d.v = d.v[0] } var p = s + h,
					v = f + u; if(l.s.r > p) l.s.r = p; if(l.s.c > v) l.s.c = v; if(l.e.r < p) l.e.r = p; if(l.e.c < v) l.e.c = v; if(d.v === null) { if(d.f) d.t = "n";
					else if(!a.cellStubs) continue;
					else d.t = "z" } else if(typeof d.v === "number") d.t = "n";
				else if(typeof d.v === "boolean") d.t = "b";
				else if(d.v instanceof Date) { d.z = a.dateNF || y._table[14]; if(a.cellDates) { d.t = "d";
						d.w = y.format(d.z, Q(d.v)) } else { d.t = "n";
						d.v = Q(d.v);
						d.w = y.format(d.z, d.v) } } else d.t = "s"; if(n) { if(!i[p]) i[p] = [];
					i[p][v] = d } else { var m = ot({ c: v, r: p });
					i[m] = d } } } if(l.s.c < 1e7) i["!ref"] = ct(l); return i }

	function gt(e, r) { return vt(null, e, r) }

	function mt(e, r) { if(!r) r = Vr(4);
		r._W(4, e); return r }

	function bt(e) { var r = e._R(4); return r === 0 ? "" : e._R(r, "dbcs") }

	function Ct(e, r) { var t = false; if(r == null) { t = true;
			r = Vr(4 + 2 * e.length) } r._W(4, e.length); if(e.length > 0) r._W(0, e, "dbcs"); return t ? r.slice(0, r.l) : r }

	function wt(e) { return { ich: e._R(2), ifnt: e._R(2) } }

	function Et(e, r) { if(!r) r = Vr(4);
		r._W(2, e.ich || 0);
		r._W(2, e.ifnt || 0); return r }

	function kt(e, r) { var t = e.l; var a = e._R(1); var n = bt(e); var i = []; var s = { t: n, h: n }; if((a & 1) !== 0) { var f = e._R(4); for(var o = 0; o != f; ++o) i.push(wt(e));
			s.r = i } else s.r = [{ ich: 0, ifnt: 0 }];
		e.l = t + r; return s }

	function St(e, r) { var t = false; if(r == null) { t = true;
			r = Vr(15 + 4 * e.t.length) } r._W(1, 0);
		Ct(e.t, r); return t ? r.slice(0, r.l) : r }
	var At = kt;

	function _t(e, r) { var t = false; if(r == null) { t = true;
			r = Vr(23 + 4 * e.t.length) } r._W(1, 1);
		Ct(e.t, r);
		r._W(4, 1);
		Et({ ich: 0, ifnt: 0 }, r); return t ? r.slice(0, r.l) : r }

	function Bt(e) { var r = e._R(4); var t = e._R(2);
		t += e._R(1) << 16;
		e.l++; return { c: r, iStyleRef: t } }

	function Tt(e, r) { if(r == null) r = Vr(8);
		r._W(-4, e.c);
		r._W(3, e.iStyleRef || e.s);
		r._W(1, 0); return r }
	var xt = bt;
	var yt = Ct;

	function It(e) { var r = e._R(4); return r === 0 || r === 4294967295 ? "" : e._R(r, "dbcs") }

	function Rt(e, r) { var t = false; if(r == null) { t = true;
			r = Vr(127) } r._W(4, e.length > 0 ? e.length : 4294967295); if(e.length > 0) r._W(0, e, "dbcs"); return t ? r.slice(0, r.l) : r }
	var Dt = bt;
	var Ot = It;
	var Ft = Rt;

	function Pt(e) { var r = e.slice(e.l, e.l + 4); var t = r[0] & 1,
			a = r[0] & 2;
		e.l += 4;
		r[0] &= 252; var n = a === 0 ? _r([0, 0, 0, 0, r[0], r[1], r[2], r[3]], 0) : Dr(r, 0) >> 2; return t ? n / 100 : n }

	function Nt(e, r) { if(r == null) r = Vr(4); var t = 0,
			a = 0,
			n = e * 100; if(e == (e | 0) && e >= -(1 << 29) && e < 1 << 29) { a = 1 } else if(n == (n | 0) && n >= -(1 << 29) && n < 1 << 29) { a = 1;
			t = 1 } if(a) r._W(-4, ((t ? n : e) << 2) + (t + 2));
		else throw new Error("unsupported RkNumber " + e) }

	function Lt(e) { var r = { s: {}, e: {} };
		r.s.r = e._R(4);
		r.e.r = e._R(4);
		r.s.c = e._R(4);
		r.e.c = e._R(4); return r }

	function Mt(e, r) { if(!r) r = Vr(16);
		r._W(4, e.s.r);
		r._W(4, e.e.r);
		r._W(4, e.s.c);
		r._W(4, e.e.c); return r }
	var Ut = Lt;
	var Ht = Mt;

	function Wt(e) { return e._R(8, "f") }

	function Vt(e, r) { return(r || Vr(8))._W(8, e, "f") }
	var zt = { 0: "#NULL!", 7: "#DIV/0!", 15: "#VALUE!", 23: "#REF!", 29: "#NAME?", 36: "#NUM!", 42: "#N/A", 43: "#GETTING_DATA", 255: "#WTF?" };
	var Xt = j(zt);

	function Gt(e) { var r = {}; var t = e._R(1); var a = t >>> 1; var n = e._R(1); var i = e._R(2, "i"); var s = e._R(1); var f = e._R(1); var o = e._R(1);
		e.l++; switch(a) {
			case 0:
				r.auto = 1; break;
			case 1:
				r.index = n; var l = Ca[n]; if(l) r.rgb = Gf(l); break;
			case 2:
				r.rgb = Gf([s, f, o]); break;
			case 3:
				r.theme = n; break; } if(i != 0) r.tint = i > 0 ? i / 32767 : i / 32768; return r }

	function jt(e, r) {
		if(!r) r = Vr(8);
		if(!e || e.auto) { r._W(4, 0);
			r._W(4, 0); return r }
		if(e.index) { r._W(1, 2);
			r._W(1, e.index) } else if(e.theme) { r._W(1, 6);
			r._W(1, e.theme) } else { r._W(1, 5);
			r._W(1, 0) }
		var t = e.tint || 0;
		if(t > 0) t *= 32767;
		else if(t < 0) t *= 32768;
		r._W(2, t);
		if(!e.rgb) { r._W(2, 0);
			r._W(1, 0);
			r._W(1, 0) } else {
			var a = e.rgb || "FFFFFF";
			r._W(1, parseInt(a.slice(0, 2), 16));
			r._W(1, parseInt(a.slice(2, 4), 16));
			r._W(1, parseInt(a.slice(4, 6), 16));
			r._W(1, 255)
		}
		return r
	}

	function Kt(e) { var r = e._R(1);
		e.l++; var t = { fItalic: r & 2, fStrikeout: r & 8, fOutline: r & 16, fShadow: r & 32, fCondense: r & 64, fExtend: r & 128 }; return t }

	function Yt(e, r) { if(!r) r = Vr(2); var t = (e.italic ? 2 : 0) | (e.strike ? 8 : 0) | (e.outline ? 16 : 0) | (e.shadow ? 32 : 0) | (e.condense ? 64 : 0) | (e.extend ? 128 : 0);
		r._W(1, t);
		r._W(1, 0); return r }

	function $t(e, r) { var t = { 2: "BITMAP", 3: "METAFILEPICT", 8: "DIB", 14: "ENHMETAFILE" }; var a = e._R(4); switch(a) {
			case 0:
				return "";
			case 4294967295:
				;
			case 4294967294:
				return t[e._R(4)] || ""; } if(a > 400) throw new Error("Unsupported Clipboard: " + a.toString(16));
		e.l -= 4; return e._R(0, r == 1 ? "lpstr" : "lpwstr") }

	function Zt(e) { return $t(e, 1) }

	function Qt(e) { return $t(e, 2) }
	var Jt = 2;
	var qt = 3;
	var ea = 11;
	var ra = 12;
	var ta = 19;
	var aa = 30;
	var na = 64;
	var ia = 65;
	var sa = 71;
	var fa = 4096;
	var oa = 80;
	var la = 81;
	var ca = [oa, la];
	var ha = { 1: { n: "CodePage", t: Jt }, 2: { n: "Category", t: oa }, 3: { n: "PresentationFormat", t: oa }, 4: { n: "ByteCount", t: qt }, 5: { n: "LineCount", t: qt }, 6: { n: "ParagraphCount", t: qt }, 7: { n: "SlideCount", t: qt }, 8: { n: "NoteCount", t: qt }, 9: { n: "HiddenCount", t: qt }, 10: { n: "MultimediaClipCount", t: qt }, 11: { n: "ScaleCrop", t: ea }, 12: { n: "HeadingPairs", t: fa | ra }, 13: { n: "TitlesOfParts", t: fa | aa }, 14: { n: "Manager", t: oa }, 15: { n: "Company", t: oa }, 16: { n: "LinksUpToDate", t: ea }, 17: { n: "CharacterCount", t: qt }, 19: { n: "SharedDoc", t: ea }, 22: { n: "HyperlinksChanged", t: ea }, 23: { n: "AppVersion", t: qt, p: "version" }, 24: { n: "DigSig", t: ia }, 26: { n: "ContentType", t: oa }, 27: { n: "ContentStatus", t: oa }, 28: { n: "Language", t: oa }, 29: { n: "Version", t: oa }, 255: {} };
	var ua = { 1: { n: "CodePage", t: Jt }, 2: { n: "Title", t: oa }, 3: { n: "Subject", t: oa }, 4: { n: "Author", t: oa }, 5: { n: "Keywords", t: oa }, 6: { n: "Comments", t: oa }, 7: { n: "Template", t: oa }, 8: { n: "LastAuthor", t: oa }, 9: { n: "RevNumber", t: oa }, 10: { n: "EditTime", t: na }, 11: { n: "LastPrinted", t: na }, 12: { n: "CreatedDate", t: na }, 13: { n: "ModifiedDate", t: na }, 14: { n: "PageCount", t: qt }, 15: { n: "WordCount", t: qt }, 16: { n: "CharCount", t: qt }, 17: { n: "Thumbnail", t: sa }, 18: { n: "Application", t: oa }, 19: { n: "DocSecurity", t: qt }, 255: {} };
	var da = { 2147483648: { n: "Locale", t: ta }, 2147483651: { n: "Behavior", t: ta }, 1919054434: {} };
	(function() { for(var e in da)
			if(da.hasOwnProperty(e)) ha[e] = ua[e] = da[e] })();
	var pa = X(ha, "n");
	var va = X(ua, "n");
	var ga = { 1: "US", 2: "CA", 3: "", 7: "RU", 20: "EG", 30: "GR", 31: "NL", 32: "BE", 33: "FR", 34: "ES", 36: "HU", 39: "IT", 41: "CH", 43: "AT", 44: "GB", 45: "DK", 46: "SE", 47: "NO", 48: "PL", 49: "DE", 52: "MX", 55: "BR", 61: "AU", 64: "NZ", 66: "TH", 81: "JP", 82: "KR", 84: "VN", 86: "CN", 90: "TR", 105: "JS", 213: "DZ", 216: "MA", 218: "LY", 351: "PT", 354: "IS", 358: "FI", 420: "CZ", 886: "TW", 961: "LB", 962: "JO", 963: "SY", 964: "IQ", 965: "KW", 966: "SA", 971: "AE", 972: "IL", 974: "QA", 981: "IR", 65535: "US" };
	var ma = [null, "solid", "mediumGray", "darkGray", "lightGray", "darkHorizontal", "darkVertical", "darkDown", "darkUp", "darkGrid", "darkTrellis", "lightHorizontal", "lightVertical", "lightDown", "lightUp", "lightGrid", "lightTrellis", "gray125", "gray0625"];

	function ba(e) { return e.map(function(e) { return [e >> 16 & 255, e >> 8 & 255, e & 255] }) }
	var Ca = ba([0, 16777215, 16711680, 65280, 255, 16776960, 16711935, 65535, 0, 16777215, 16711680, 65280, 255, 16776960, 16711935, 65535, 8388608, 32768, 128, 8421376, 8388736, 32896, 12632256, 8421504, 10066431, 10040166, 16777164, 13434879, 6684774, 16744576, 26316, 13421823, 128, 16711935, 16776960, 65535, 8388736, 8388608, 32896, 255, 52479, 13434879, 13434828, 16777113, 10079487, 16751052, 13408767, 16764057, 3368703, 3394764, 10079232, 16763904, 16750848, 16737792, 6710937, 9868950, 13158, 3381606, 13056, 3355392, 10040064, 10040166, 3355545, 3355443, 16777215, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
	var wa = { "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml": "workbooks", "application/vnd.ms-excel.binIndexWs": "TODO", "application/vnd.ms-excel.intlmacrosheet": "TODO", "application/vnd.ms-excel.binIndexMs": "TODO", "application/vnd.openxmlformats-package.core-properties+xml": "coreprops", "application/vnd.openxmlformats-officedocument.custom-properties+xml": "custprops", "application/vnd.openxmlformats-officedocument.extended-properties+xml": "extprops", "application/vnd.openxmlformats-officedocument.customXmlProperties+xml": "TODO", "application/vnd.openxmlformats-officedocument.spreadsheetml.customProperty": "TODO", "application/vnd.ms-excel.pivotTable": "TODO", "application/vnd.openxmlformats-officedocument.spreadsheetml.pivotTable+xml": "TODO", "application/vnd.ms-office.chartcolorstyle+xml": "TODO", "application/vnd.ms-office.chartstyle+xml": "TODO", "application/vnd.ms-excel.calcChain": "calcchains", "application/vnd.openxmlformats-officedocument.spreadsheetml.calcChain+xml": "calcchains", "application/vnd.openxmlformats-officedocument.spreadsheetml.printerSettings": "TODO", "application/vnd.ms-office.activeX": "TODO", "application/vnd.ms-office.activeX+xml": "TODO", "application/vnd.ms-excel.attachedToolbars": "TODO", "application/vnd.ms-excel.connections": "TODO", "application/vnd.openxmlformats-officedocument.spreadsheetml.connections+xml": "TODO", "application/vnd.ms-excel.externalLink": "links", "application/vnd.openxmlformats-officedocument.spreadsheetml.externalLink+xml": "links", "application/vnd.ms-excel.sheetMetadata": "TODO", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheetMetadata+xml": "TODO", "application/vnd.ms-excel.pivotCacheDefinition": "TODO", "application/vnd.ms-excel.pivotCacheRecords": "TODO", "application/vnd.openxmlformats-officedocument.spreadsheetml.pivotCacheDefinition+xml": "TODO", "application/vnd.openxmlformats-officedocument.spreadsheetml.pivotCacheRecords+xml": "TODO", "application/vnd.ms-excel.queryTable": "TODO", "application/vnd.openxmlformats-officedocument.spreadsheetml.queryTable+xml": "TODO", "application/vnd.ms-excel.userNames": "TODO", "application/vnd.ms-excel.revisionHeaders": "TODO", "application/vnd.ms-excel.revisionLog": "TODO", "application/vnd.openxmlformats-officedocument.spreadsheetml.revisionHeaders+xml": "TODO", "application/vnd.openxmlformats-officedocument.spreadsheetml.revisionLog+xml": "TODO", "application/vnd.openxmlformats-officedocument.spreadsheetml.userNames+xml": "TODO", "application/vnd.ms-excel.tableSingleCells": "TODO", "application/vnd.openxmlformats-officedocument.spreadsheetml.tableSingleCells+xml": "TODO", "application/vnd.ms-excel.slicer": "TODO", "application/vnd.ms-excel.slicerCache": "TODO", "application/vnd.ms-excel.slicer+xml": "TODO", "application/vnd.ms-excel.slicerCache+xml": "TODO", "application/vnd.ms-excel.wsSortMap": "TODO", "application/vnd.ms-excel.table": "TODO", "application/vnd.openxmlformats-officedocument.spreadsheetml.table+xml": "TODO", "application/vnd.openxmlformats-officedocument.theme+xml": "themes", "application/vnd.openxmlformats-officedocument.themeOverride+xml": "TODO", "application/vnd.ms-excel.Timeline+xml": "TODO", "application/vnd.ms-excel.TimelineCache+xml": "TODO", "application/vnd.ms-office.vbaProject": "vba", "application/vnd.ms-office.vbaProjectSignature": "vba", "application/vnd.ms-office.volatileDependencies": "TODO", "application/vnd.openxmlformats-officedocument.spreadsheetml.volatileDependencies+xml": "TODO", "application/vnd.ms-excel.controlproperties+xml": "TODO", "application/vnd.openxmlformats-officedocument.model+data": "TODO", "application/vnd.ms-excel.Survey+xml": "TODO", "application/vnd.openxmlformats-officedocument.drawing+xml": "drawings", "application/vnd.openxmlformats-officedocument.drawingml.chart+xml": "TODO", "application/vnd.openxmlformats-officedocument.drawingml.chartshapes+xml": "TODO", "application/vnd.openxmlformats-officedocument.drawingml.diagramColors+xml": "TODO", "application/vnd.openxmlformats-officedocument.drawingml.diagramData+xml": "TODO", "application/vnd.openxmlformats-officedocument.drawingml.diagramLayout+xml": "TODO", "application/vnd.openxmlformats-officedocument.drawingml.diagramStyle+xml": "TODO", "application/vnd.openxmlformats-officedocument.vmlDrawing": "TODO", "application/vnd.openxmlformats-package.relationships+xml": "rels", "application/vnd.openxmlformats-officedocument.oleObject": "TODO", "image/png": "TODO", sheet: "js" };
	var Ea = function() { var e = { workbooks: { xlsx: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml", xlsm: "application/vnd.ms-excel.sheet.macroEnabled.main+xml", xlsb: "application/vnd.ms-excel.sheet.binary.macroEnabled.main", xlam: "application/vnd.ms-excel.addin.macroEnabled.main+xml", xltx: "application/vnd.openxmlformats-officedocument.spreadsheetml.template.main+xml" }, strs: { xlsx: "application/vnd.openxmlformats-officedocument.spreadsheetml.sharedStrings+xml", xlsb: "application/vnd.ms-excel.sharedStrings" }, comments: { xlsx: "application/vnd.openxmlformats-officedocument.spreadsheetml.comments+xml", xlsb: "application/vnd.ms-excel.comments" }, sheets: { xlsx: "application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml", xlsb: "application/vnd.ms-excel.worksheet" }, charts: { xlsx: "application/vnd.openxmlformats-officedocument.spreadsheetml.chartsheet+xml", xlsb: "application/vnd.ms-excel.chartsheet" }, dialogs: { xlsx: "application/vnd.openxmlformats-officedocument.spreadsheetml.dialogsheet+xml", xlsb: "application/vnd.ms-excel.dialogsheet" }, macros: { xlsx: "application/vnd.ms-excel.macrosheet+xml", xlsb: "application/vnd.ms-excel.macrosheet" }, styles: { xlsx: "application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml", xlsb: "application/vnd.ms-excel.styles" } };
		z(e).forEach(function(r) {
			["xlsm", "xlam"].forEach(function(t) { if(!e[r][t]) e[r][t] = e[r].xlsx }) });
		z(e).forEach(function(r) { z(e[r]).forEach(function(t) { wa[e[r][t]] = r }) }); return e }();
	var ka = K(wa);
	ar.CT = "http://schemas.openxmlformats.org/package/2006/content-types";

	function Sa() { return { workbooks: [], sheets: [], charts: [], dialogs: [], macros: [], rels: [], strs: [], comments: [], links: [], coreprops: [], extprops: [], custprops: [], themes: [], styles: [], calcchains: [], vba: [], drawings: [], TODO: [], xmlns: "" } }

	function Aa(e) { var r = Sa(); if(!e || !e.match) return r; var t = {};
		(e.match(ke) || []).forEach(function(e) { var a = _e(e); switch(a[0].replace(Se, "<")) {
				case "<?xml":
					break;
				case "<Types":
					r.xmlns = a["xmlns" + (a[0].match(/<(\w+):/) || ["", ""])[1]]; break;
				case "<Default":
					t[a.Extension] = a.ContentType; break;
				case "<Override":
					if(r[wa[a.ContentType]] !== undefined) r[wa[a.ContentType]].push(a.PartName); break; } }); if(r.xmlns !== ar.CT) throw new Error("Unknown Namespace: " + r.xmlns);
		r.calcchain = r.calcchains.length > 0 ? r.calcchains[0] : "";
		r.sst = r.strs.length > 0 ? r.strs[0] : "";
		r.style = r.styles.length > 0 ? r.styles[0] : "";
		r.defaults = t;
		delete r.calcchains; return r }
	var _a = er("Types", null, { xmlns: ar.CT, "xmlns:xsd": ar.xsd, "xmlns:xsi": ar.xsi });
	var Ba = [
		["xml", "application/xml"],
		["bin", "application/vnd.ms-excel.sheet.binary.macroEnabled.main"],
		["vml", "application/vnd.openxmlformats-officedocument.vmlDrawing"],
		["bmp", "image/bmp"],
		["png", "image/png"],
		["gif", "image/gif"],
		["emf", "image/x-emf"],
		["wmf", "image/x-wmf"],
		["jpg", "image/jpeg"],
		["jpeg", "image/jpeg"],
		["tif", "image/tiff"],
		["tiff", "image/tiff"],
		["pdf", "application/pdf"],
		["rels", ka.rels[0]]
	].map(function(e) { return er("Default", null, { Extension: e[0], ContentType: e[1] }) });

	function Ta(e, r) { var t = [],
			a;
		t[t.length] = we;
		t[t.length] = _a;
		t = t.concat(Ba); var n = function(n) { if(e[n] && e[n].length > 0) { a = e[n][0];
				t[t.length] = er("Override", null, { PartName: (a[0] == "/" ? "" : "/") + a, ContentType: Ea[n][r.bookType || "xlsx"] }) } }; var i = function(a) {
			(e[a] || []).forEach(function(e) { t[t.length] = er("Override", null, { PartName: (e[0] == "/" ? "" : "/") + e, ContentType: Ea[a][r.bookType || "xlsx"] }) }) }; var s = function(r) {
			(e[r] || []).forEach(function(e) { t[t.length] = er("Override", null, { PartName: (e[0] == "/" ? "" : "/") + e, ContentType: ka[r][0] }) }) };
		n("workbooks");
		i("sheets");
		i("charts");
		s("themes");
		["strs", "styles"].forEach(n);
		["coreprops", "extprops", "custprops"].forEach(s);
		s("vba");
		s("comments");
		s("drawings"); if(t.length > 2) { t[t.length] = "</Types>";
			t[1] = t[1].replace("/>", ">") } return t.join("") }
	var xa = { WB: "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument", SHEET: "http://sheetjs.openxmlformats.org/officeDocument/2006/relationships/officeDocument", HLINK: "http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink", VML: "http://schemas.openxmlformats.org/officeDocument/2006/relationships/vmlDrawing", VBA: "http://schemas.microsoft.com/office/2006/relationships/vbaProject" };

	function ya(e) { var r = e.lastIndexOf("/"); return e.slice(0, r + 1) + "_rels/" + e.slice(r + 1) + ".rels" }

	function Ia(e, r) { if(!e) return e; if(r.charAt(0) !== "/") { r = "/" + r } var t = {}; var a = {};
		(e.match(ke) || []).forEach(function(e) { var n = _e(e); if(n[0] === "<Relationship") { var i = {};
				i.Type = n.Type;
				i.Target = n.Target;
				i.Id = n.Id;
				i.TargetMode = n.TargetMode; var s = n.TargetMode === "External" ? n.Target : Ce(n.Target, r);
				t[s] = i;
				a[n.Id] = i } });
		t["!id"] = a; return t } ar.RELS = "http://schemas.openxmlformats.org/package/2006/relationships";
	var Ra = er("Relationships", null, { xmlns: ar.RELS });

	function Da(e) { var r = [we, Ra];
		z(e["!id"]).forEach(function(t) { r[r.length] = er("Relationship", null, e["!id"][t]) }); if(r.length > 2) { r[r.length] = "</Relationships>";
			r[1] = r[1].replace("/>", ">") } return r.join("") }

	function Oa(e, r, t, a, n) { if(!n) n = {}; if(!e["!id"]) e["!id"] = {}; if(r < 0)
			for(r = 1; e["!id"]["rId" + r]; ++r) {} n.Id = "rId" + r;
		n.Type = a;
		n.Target = t; if(n.Type == xa.HLINK) n.TargetMode = "External"; if(e["!id"][n.Id]) throw new Error("Cannot rewrite rId " + r);
		e["!id"][n.Id] = n;
		e[("/" + n.Target).replace("//", "/")] = n; return r }
	var Fa = "application/vnd.oasis.opendocument.spreadsheet";

	function Pa(e, r) { var t = Fp(e); var a; var n; while(a = Pp.exec(t)) switch(a[3]) {
			case "manifest":
				break;
			case "file-entry":
				n = _e(a[0], false); if(n.path == "/" && n.type !== Fa) throw new Error("This OpenDocument is not a spreadsheet"); break;
			case "encryption-data":
				;
			case "algorithm":
				;
			case "start-key-generation":
				;
			case "key-derivation":
				throw new Error("Unsupported ODS Encryption");
			default:
				if(r && r.WTF) throw a; } }

	function Na(e) { var r = [we];
		r.push('<manifest:manifest xmlns:manifest="urn:oasis:names:tc:opendocument:xmlns:manifest:1.0" manifest:version="1.2">\n');
		r.push('  <manifest:file-entry manifest:full-path="/" manifest:version="1.2" manifest:media-type="application/vnd.oasis.opendocument.spreadsheet"/>\n'); for(var t = 0; t < e.length; ++t) r.push('  <manifest:file-entry manifest:full-path="' + e[t][0] + '" manifest:media-type="' + e[t][1] + '"/>\n');
		r.push("</manifest:manifest>"); return r.join("") }

	function La(e, r, t) { return ['  <rdf:Description rdf:about="' + e + '">\n', '    <rdf:type rdf:resource="http://docs.oasis-open.org/ns/office/1.2/meta/' + (t || "odf") + "#" + r + '"/>\n', "  </rdf:Description>\n"].join("") }

	function Ma(e, r) { return ['  <rdf:Description rdf:about="' + e + '">\n', '    <ns0:hasPart xmlns:ns0="http://docs.oasis-open.org/ns/office/1.2/meta/pkg#" rdf:resource="' + r + '"/>\n', "  </rdf:Description>\n"].join("") }

	function Ua(e) { var r = [we];
		r.push('<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">\n'); for(var t = 0; t != e.length; ++t) { r.push(La(e[t][0], e[t][1]));
			r.push(Ma("", e[t][0])) } r.push(La("", "Document", "pkg"));
		r.push("</rdf:RDF>"); return r.join("") }
	var Ha = function() { var e = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><office:document-meta xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0" xmlns:meta="urn:oasis:names:tc:opendocument:xmlns:meta:1.0" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:xlink="http://www.w3.org/1999/xlink" office:version="1.2"><office:meta><meta:generator>Sheet' + "JS " + r.version + "</meta:generator></office:meta></office:document-meta>"; return function t() { return e } }();
	var Wa = [
		["cp:category", "Category"],
		["cp:contentStatus", "ContentStatus"],
		["cp:keywords", "Keywords"],
		["cp:lastModifiedBy", "LastAuthor"],
		["cp:lastPrinted", "LastPrinted"],
		["cp:revision", "RevNumber"],
		["cp:version", "Version"],
		["dc:creator", "Author"],
		["dc:description", "Comments"],
		["dc:identifier", "Identifier"],
		["dc:language", "Language"],
		["dc:subject", "Subject"],
		["dc:title", "Title"],
		["dcterms:created", "CreatedDate", "date"],
		["dcterms:modified", "ModifiedDate", "date"]
	];
	ar.CORE_PROPS = "http://schemas.openxmlformats.org/package/2006/metadata/core-properties";
	xa.CORE_PROPS = "http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties";
	var Va = function() { var e = new Array(Wa.length); for(var r = 0; r < Wa.length; ++r) { var t = Wa[r]; var a = "(?:" + t[0].slice(0, t[0].indexOf(":")) + ":)" + t[0].slice(t[0].indexOf(":") + 1);
			e[r] = new RegExp("<" + a + "[^>]*>([\\s\\S]*?)</" + a + ">") } return e }();

	function za(e) { var r = {};
		e = He(e); for(var t = 0; t < Wa.length; ++t) { var a = Wa[t],
				n = e.match(Va[t]); if(n != null && n.length > 0) r[a[1]] = n[1]; if(a[2] === "date" && r[a[1]]) r[a[1]] = te(r[a[1]]) } return r }
	var Xa = er("cp:coreProperties", null, { "xmlns:cp": ar.CORE_PROPS, "xmlns:dc": ar.dc, "xmlns:dcterms": ar.dcterms, "xmlns:dcmitype": ar.dcmitype, "xmlns:xsi": ar.xsi });

	function Ga(e, r, t, a, n) { if(n[e] != null || r == null || r === "") return;
		n[e] = r;
		a[a.length] = t ? er(e, r, t) : Je(e, r) }

	function ja(e, r) { var t = r || {}; var a = [we, Xa],
			n = {}; if(!e && !t.Props) return a.join(""); if(e) { if(e.CreatedDate != null) Ga("dcterms:created", typeof e.CreatedDate === "string" ? e.CreatedDate : rr(e.CreatedDate, t.WTF), { "xsi:type": "dcterms:W3CDTF" }, a, n); if(e.ModifiedDate != null) Ga("dcterms:modified", typeof e.ModifiedDate === "string" ? e.ModifiedDate : rr(e.ModifiedDate, t.WTF), { "xsi:type": "dcterms:W3CDTF" }, a, n) } for(var i = 0; i != Wa.length; ++i) { var s = Wa[i]; var f = t.Props && t.Props[s[1]] != null ? t.Props[s[1]] : e ? e[s[1]] : null; if(f === true) f = "1";
			else if(f === false) f = "0";
			else if(typeof f == "number") f = String(f); if(f != null) Ga(s[0], f, null, a, n) } if(a.length > 2) { a[a.length] = "</cp:coreProperties>";
			a[1] = a[1].replace("/>", ">") } return a.join("") }
	var Ka = [
		["Application", "Application", "string"],
		["AppVersion", "AppVersion", "string"],
		["Company", "Company", "string"],
		["DocSecurity", "DocSecurity", "string"],
		["Manager", "Manager", "string"],
		["HyperlinksChanged", "HyperlinksChanged", "bool"],
		["SharedDoc", "SharedDoc", "bool"],
		["LinksUpToDate", "LinksUpToDate", "bool"],
		["ScaleCrop", "ScaleCrop", "bool"],
		["HeadingPairs", "HeadingPairs", "raw"],
		["TitlesOfParts", "TitlesOfParts", "raw"]
	];
	ar.EXT_PROPS = "http://schemas.openxmlformats.org/officeDocument/2006/extended-properties";
	xa.EXT_PROPS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties";
	var Ya = ["Worksheets", "SheetNames", "NamedRanges", "DefinedNames", "Chartsheets", "ChartNames"];

	function $a(e, r, t, a) { var n = []; if(typeof e == "string") n = Ze(e, a);
		else
			for(var i = 0; i < e.length; ++i) n = n.concat(e[i].map(function(e) { return { v: e } })); var s = typeof r == "string" ? Ze(r, a).map(function(e) { return e.v }) : r; var f = 0,
			o = 0; if(s.length > 0)
			for(var l = 0; l !== n.length; l += 2) { o = +n[l + 1].v; switch(n[l].v) {
					case "Worksheets":
						;
					case "宸ヤ綔琛�":
						;
					case "袥懈褋褌褘":
						;
					case "兀賵乇丕賯 丕賱毓賲賱":
						;
					case "銉兗銈偡銉笺儓":
						;
					case "讙诇讬讜谞讜转 注讘讜讚讛":
						;
					case "Arbeitsbl盲tter":
						;
					case "脟al谋艧ma Sayfalar谋":
						;
					case "Feuilles de calcul":
						;
					case "Fogli di lavoro":
						;
					case "Folhas de c谩lculo":
						;
					case "Planilhas":
						;
					case "Regneark":
						;
					case "Werkbladen":
						t.Worksheets = o;
						t.SheetNames = s.slice(f, f + o); break;
					case "Named Ranges":
						;
					case "鍚嶅墠浠樸亶涓€瑕�":
						;
					case "Benannte Bereiche":
						;
					case "Navngivne omr氓der":
						t.NamedRanges = o;
						t.DefinedNames = s.slice(f, f + o); break;
					case "Charts":
						;
					case "Diagramme":
						t.Chartsheets = o;
						t.ChartNames = s.slice(f, f + o); break; } f += o } }

	function Za(e, r, t) { var a = {}; if(!r) r = {};
		e = He(e);
		Ka.forEach(function(t) { switch(t[2]) {
				case "string":
					r[t[1]] = (e.match(Ge(t[0])) || [])[1]; break;
				case "bool":
					r[t[1]] = (e.match(Ge(t[0])) || [])[1] === "true"; break;
				case "raw":
					var n = e.match(new RegExp("<" + t[0] + "[^>]*>([\\s\\S]*?)</" + t[0] + ">")); if(n && n.length > 0) a[t[1]] = n[1]; break; } }); if(a.HeadingPairs && a.TitlesOfParts) $a(a.HeadingPairs, a.TitlesOfParts, r, t); return r }
	var Qa = er("Properties", null, { xmlns: ar.EXT_PROPS, "xmlns:vt": ar.vt });

	function Ja(e) { var r = [],
			t = er; if(!e) e = {};
		e.Application = "SheetJS";
		r[r.length] = we;
		r[r.length] = Qa;
		Ka.forEach(function(a) { if(e[a[1]] === undefined) return; var n; switch(a[2]) {
				case "string":
					n = String(e[a[1]]); break;
				case "bool":
					n = e[a[1]] ? "true" : "false"; break; } if(n !== undefined) r[r.length] = t(a[0], n) });
		r[r.length] = t("HeadingPairs", t("vt:vector", t("vt:variant", "<vt:lpstr>Worksheets</vt:lpstr>") + t("vt:variant", t("vt:i4", String(e.Worksheets))), { size: 2, baseType: "variant" }));
		r[r.length] = t("TitlesOfParts", t("vt:vector", e.SheetNames.map(function(e) { return "<vt:lpstr>" + De(e) + "</vt:lpstr>" }).join(""), { size: e.Worksheets, baseType: "lpstr" })); if(r.length > 2) { r[r.length] = "</Properties>";
			r[1] = r[1].replace("/>", ">") } return r.join("") } ar.CUST_PROPS = "http://schemas.openxmlformats.org/officeDocument/2006/custom-properties";
	xa.CUST_PROPS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/custom-properties";
	var qa = /<[^>]+>[^<]*/g;

	function en(e, r) { var t = {},
			a = ""; var n = e.match(qa); if(n)
			for(var i = 0; i != n.length; ++i) { var s = n[i],
					f = _e(s); switch(f[0]) {
					case "<?xml":
						break;
					case "<Properties":
						break;
					case "<property":
						a = f.name; break;
					case "</property>":
						a = null; break;
					default:
						if(s.indexOf("<vt:") === 0) { var o = s.split(">"); var l = o[0].slice(4),
								c = o[1]; switch(l) {
								case "lpstr":
									;
								case "bstr":
									;
								case "lpwstr":
									t[a] = ye(c); break;
								case "bool":
									t[a] = Ue(c); break;
								case "i1":
									;
								case "i2":
									;
								case "i4":
									;
								case "i8":
									;
								case "int":
									;
								case "uint":
									t[a] = parseInt(c, 10); break;
								case "r4":
									;
								case "r8":
									;
								case "decimal":
									t[a] = parseFloat(c); break;
								case "filetime":
									;
								case "date":
									t[a] = te(c); break;
								case "cy":
									;
								case "error":
									t[a] = ye(c); break;
								default:
									if(l.slice(-1) == "/") break; if(r.WTF && typeof console !== "undefined") console.warn("Unexpected", s, l, o); } } else if(s.slice(0, 2) === "</") {} else if(r.WTF) throw new Error(s); } }
		return t }
	var rn = er("Properties", null, { xmlns: ar.CUST_PROPS, "xmlns:vt": ar.vt });

	function tn(e) { var r = [we, rn]; if(!e) return r.join(""); var t = 1;
		z(e).forEach(function a(n) {++t;
			r[r.length] = er("property", tr(e[n]), { fmtid: "{D5CDD505-2E9C-101B-9397-08002B2CF9AE}", pid: t, name: n }) }); if(r.length > 2) { r[r.length] = "</Properties>";
			r[1] = r[1].replace("/>", ">") } return r.join("") }
	var an = { Title: "Title", Subject: "Subject", Author: "Author", Keywords: "Keywords", Comments: "Description", LastAuthor: "LastAuthor", RevNumber: "Revision", Application: "AppName", LastPrinted: "LastPrinted", CreatedDate: "Created", ModifiedDate: "LastSaved", Category: "Category", Manager: "Manager", Company: "Company", AppVersion: "Version", ContentStatus: "ContentStatus", Identifier: "Identifier", Language: "Language" };
	var nn = G(an);

	function sn(e, r, t) { r = nn[r] || r;
		e[r] = t }

	function fn(e, r) { var t = [];
		z(an).map(function(e) { for(var r = 0; r < Wa.length; ++r)
				if(Wa[r][1] == e) return Wa[r]; for(r = 0; r < Ka.length; ++r)
				if(Ka[r][1] == e) return Ka[r]; throw e }).forEach(function(a) { if(e[a[1]] == null) return; var n = r && r.Props && r.Props[a[1]] != null ? r.Props[a[1]] : e[a[1]]; switch(a[2]) {
				case "date":
					n = new Date(n).toISOString().replace(/\.\d*Z/, "Z"); break; } if(typeof n == "number") n = String(n);
			else if(n === true || n === false) { n = n ? "1" : "0" } else if(n instanceof Date) n = new Date(n).toISOString().replace(/\.\d*Z/, "");
			t.push(Je(an[a[1]] || a[1], n)) }); return er("DocumentProperties", t.join(""), { xmlns: nr.o }) }

	function on(e, r) { var t = ["Worksheets", "SheetNames"]; var a = "CustomDocumentProperties"; var n = []; if(e) z(e).forEach(function(r) { if(!e.hasOwnProperty(r)) return; for(var a = 0; a < Wa.length; ++a)
				if(r == Wa[a][1]) return; for(a = 0; a < Ka.length; ++a)
				if(r == Ka[a][1]) return; for(a = 0; a < t.length; ++a)
				if(r == t[a]) return; var i = e[r]; var s = "string"; if(typeof i == "number") { s = "float";
				i = String(i) } else if(i === true || i === false) { s = "boolean";
				i = i ? "1" : "0" } else i = String(i);
			n.push(er(Oe(r), i, { "dt:dt": s })) }); if(r) z(r).forEach(function(t) { if(!r.hasOwnProperty(t)) return; if(e && e.hasOwnProperty(t)) return; var a = r[t]; var i = "string"; if(typeof a == "number") { i = "float";
				a = String(a) } else if(a === true || a === false) { i = "boolean";
				a = a ? "1" : "0" } else if(a instanceof Date) { i = "dateTime.tz";
				a = a.toISOString() } else a = String(a);
			n.push(er(Oe(t), a, { "dt:dt": i })) }); return "<" + a + ' xmlns="' + nr.o + '">' + n.join("") + "</" + a + ">" }

	function ln(e) { var r = e._R(4),
			t = e._R(4); return new Date((t / 1e7 * Math.pow(2, 32) + r / 1e7 - 11644473600) * 1e3).toISOString().replace(/\.000/, "") }

	function cn(e) { var r = typeof e == "string" ? new Date(Date.parse(e)) : e; var t = r.getTime() / 1e3 + 11644473600; var a = t % Math.pow(2, 32),
			n = (t - a) / Math.pow(2, 32);
		a *= 1e7;
		n *= 1e7; var i = a / Math.pow(2, 32) | 0; if(i > 0) { a = a % Math.pow(2, 32);
			n += i } var s = Vr(8);
		s._W(4, a);
		s._W(4, n); return s }

	function hn(e, r, t) { var a = e.l; var n = e._R(0, "lpstr-cp"); if(t)
			while(e.l - a & 3) ++e.l; return n }

	function un(e, r, t) { var a = e._R(0, "lpwstr"); if(t) e.l += 4 - (a.length + 1 & 3) & 3; return a }

	function dn(e, r, t) { if(r === 31) return un(e); return hn(e, r, t) }

	function pn(e, r, t) { return dn(e, r, t === false ? 0 : 4) }

	function vn(e, r) { if(!r) throw new Error("VtUnalignedString must have positive length"); return dn(e, r, 0) }

	function gn(e) { var r = e._R(4); var t = []; for(var a = 0; a != r; ++a) t[a] = e._R(0, "lpstr-cp").replace(T, ""); return t }

	function mn(e) { return gn(e) }

	function bn(e) { var r = An(e, la); var t = An(e, qt); return [r, t] }

	function Cn(e) { var r = e._R(4); var t = []; for(var a = 0; a != r / 2; ++a) t.push(bn(e)); return t }

	function wn(e) { return Cn(e) }

	function En(e, r) { var t = e._R(4); var a = {}; for(var n = 0; n != t; ++n) { var i = e._R(4); var s = e._R(4);
			a[i] = e._R(s, r === 1200 ? "utf16le" : "utf8").replace(T, "").replace(x, "!"); if(r === 1200 && s % 2) e.l += 2 } if(e.l & 3) e.l = e.l >> 2 + 1 << 2; return a }

	function kn(e) { var r = e._R(4); var t = e.slice(e.l, e.l + r);
		e.l += r; if((r & 3) > 0) e.l += 4 - (r & 3) & 3; return t }

	function Sn(e) { var r = {};
		r.Size = e._R(4);
		e.l += r.Size + 3 - (r.Size - 1) % 4; return r }

	function An(e, r, t) { var a = e._R(2),
			n, i = t || {};
		e.l += 2; if(r !== ra)
			if(a !== r && ca.indexOf(r) === -1) throw new Error("Expected type " + r + " saw " + a); switch(r === ra ? a : r) {
			case 2:
				n = e._R(2, "i"); if(!i.raw) e.l += 2; return n;
			case 3:
				n = e._R(4, "i"); return n;
			case 11:
				return e._R(4) !== 0;
			case 19:
				n = e._R(4); return n;
			case 30:
				return hn(e, a, 4).replace(T, "");
			case 31:
				return un(e);
			case 64:
				return ln(e);
			case 65:
				return kn(e);
			case 71:
				return Sn(e);
			case 80:
				return pn(e, a, !i.raw).replace(T, "");
			case 81:
				return vn(e, a).replace(T, "");
			case 4108:
				return wn(e);
			case 4126:
				return mn(e);
			default:
				throw new Error("TypedPropertyValue unrecognized type " + r + " " + a); } }

	function _n(e, r) { var t = Vr(4),
			a = Vr(4);
		t._W(4, e == 80 ? 31 : e); switch(e) {
			case 3:
				a._W(-4, r); break;
			case 5:
				a = Vr(8);
				a._W(8, r, "f"); break;
			case 11:
				a._W(4, r ? 1 : 0); break;
			case 64:
				a = cn(r); break;
			case 31:
				;
			case 80:
				a = Vr(4 + 2 * (r.length + 1) + (r.length % 2 ? 0 : 2));
				a._W(4, r.length + 1);
				a._W(0, r, "dbcs"); while(a.l != a.length) a._W(1, 0); break;
			default:
				throw new Error("TypedPropertyValue unrecognized type " + e + " " + r); } return B([t, a]) }

	function Bn(e, r) { var t = e.l; var a = e._R(4); var n = e._R(4); var i = [],
			s = 0; var f = 0; var o = -1,
			c = {}; for(s = 0; s != n; ++s) { var h = e._R(4); var u = e._R(4);
			i[s] = [h, u + t] } i.sort(function(e, r) { return e[1] - r[1] }); var d = {}; for(s = 0; s != n; ++s) { if(e.l !== i[s][1]) { var p = true; if(s > 0 && r) switch(r[i[s - 1][0]].t) {
					case 2:
						if(e.l + 2 === i[s][1]) { e.l += 2;
							p = false } break;
					case 80:
						if(e.l <= i[s][1]) { e.l = i[s][1];
							p = false } break;
					case 4108:
						if(e.l <= i[s][1]) { e.l = i[s][1];
							p = false } break; }
				if((!r || s == 0) && e.l <= i[s][1]) { p = false;
					e.l = i[s][1] } if(p) throw new Error("Read Error: Expected address " + i[s][1] + " at " + e.l + " :" + s) } if(r) { var v = r[i[s][0]];
				d[v.n] = An(e, v.t, { raw: true }); if(v.p === "version") d[v.n] = String(d[v.n] >> 16) + "." + ("0000" + String(d[v.n] & 65535)).slice(-4); if(v.n == "CodePage") switch(d[v.n]) {
					case 0:
						d[v.n] = 1252;
					case 874:
						;
					case 932:
						;
					case 936:
						;
					case 949:
						;
					case 950:
						;
					case 1250:
						;
					case 1251:
						;
					case 1253:
						;
					case 1254:
						;
					case 1255:
						;
					case 1256:
						;
					case 1257:
						;
					case 1258:
						;
					case 1e4:
						;
					case 1200:
						;
					case 1201:
						;
					case 1252:
						;
					case 65e3:
						;
					case -536:
						;
					case 65001:
						;
					case -535:
						l(f = d[v.n] >>> 0 & 65535); break;
					default:
						throw new Error("Unsupported CodePage: " + d[v.n]); } } else { if(i[s][0] === 1) { f = d.CodePage = An(e, Jt);
					l(f); if(o !== -1) { var g = e.l;
						e.l = i[o][1];
						c = En(e, f);
						e.l = g } } else if(i[s][0] === 0) { if(f === 0) { o = s;
						e.l = i[s + 1][1]; continue } c = En(e, f) } else { var m = c[i[s][0]]; var b; switch(e[e.l]) {
						case 65:
							e.l += 4;
							b = kn(e); break;
						case 30:
							e.l += 4;
							b = pn(e, e[e.l - 4]).replace(/\u0000+$/, ""); break;
						case 31:
							e.l += 4;
							b = pn(e, e[e.l - 4]).replace(/\u0000+$/, ""); break;
						case 3:
							e.l += 4;
							b = e._R(4, "i"); break;
						case 19:
							e.l += 4;
							b = e._R(4); break;
						case 5:
							e.l += 4;
							b = e._R(8, "f"); break;
						case 11:
							e.l += 4;
							b = Pn(e, 4); break;
						case 64:
							e.l += 4;
							b = te(ln(e)); break;
						default:
							throw new Error("unparsed value: " + e[e.l]); } d[m] = b } } } e.l = t + a; return d }
	var Tn = ["CodePage", "Thumbnail", "_PID_LINKBASE", "_PID_HLINKS", "SystemIdentifier", "FMTID"].concat(Ya);

	function xn(e) { switch(typeof e) {
			case "boolean":
				return 11;
			case "number":
				return(e | 0) == e ? 3 : 5;
			case "string":
				return 31;
			case "object":
				if(e instanceof Date) return 64; break; } return -1 }

	function yn(e, r, t) { var a = Vr(8),
			n = [],
			i = []; var s = 8,
			f = 0; var o = Vr(8),
			l = Vr(8);
		o._W(4, 2);
		o._W(4, 1200);
		l._W(4, 1);
		i.push(o);
		n.push(l);
		s += 8 + o.length; if(!r) { l = Vr(8);
			l._W(4, 0);
			n.unshift(l); var c = [Vr(4)];
			c[0]._W(4, e.length); for(f = 0; f < e.length; ++f) { var h = e[f][0];
				o = Vr(4 + 4 + 2 * (h.length + 1) + (h.length % 2 ? 0 : 2));
				o._W(4, f + 2);
				o._W(4, h.length + 1);
				o._W(0, h, "dbcs"); while(o.l != o.length) o._W(1, 0);
				c.push(o) } o = B(c);
			i.unshift(o);
			s += 8 + o.length } for(f = 0; f < e.length; ++f) { if(r && !r[e[f][0]]) continue; if(Tn.indexOf(e[f][0]) > -1) continue; if(e[f][1] == null) continue; var u = e[f][1],
				d = 0; if(r) { d = +r[e[f][0]]; var p = t[d]; if(p.p == "version" && typeof u == "string") u = (+(u = u.split("."))[0] << 16) + (+u[1] || 0);
				o = _n(p.t, u) } else { var v = xn(u); if(v == -1) { v = 31;
					u = String(u) } o = _n(v, u) } i.push(o);
			l = Vr(8);
			l._W(4, !r ? 2 + f : d);
			n.push(l);
			s += 8 + o.length } var g = 8 * (i.length + 1); for(f = 0; f < i.length; ++f) { n[f]._W(4, g);
			g += i[f].length } a._W(4, s);
		a._W(4, i.length); return B([a].concat(n).concat(i)) }

	function In(e, r, t) { var a = e.content; if(!a) return {};
		Hr(a, 0); var n, i, s, f, o = 0;
		a.chk("feff", "Byte Order: ");
		a._R(2); var l = a._R(4); var c = a._R(16); if(c !== L.utils.consts.HEADER_CLSID && c !== t) throw new Error("Bad PropertySet CLSID " + c);
		n = a._R(4); if(n !== 1 && n !== 2) throw new Error("Unrecognized #Sets: " + n);
		i = a._R(16);
		f = a._R(4); if(n === 1 && f !== a.l) throw new Error("Length mismatch: " + f + " !== " + a.l);
		else if(n === 2) { s = a._R(16);
			o = a._R(4) } var h = Bn(a, r); var u = { SystemIdentifier: l }; for(var d in h) u[d] = h[d];
		u.FMTID = i; if(n === 1) return u; if(o - a.l == 2) a.l += 2; if(a.l !== o) throw new Error("Length mismatch 2: " + a.l + " !== " + o); var p; try { p = Bn(a, null) } catch(v) {} for(d in p) u[d] = p[d];
		u.FMTID = [i, s]; return u }

	function Rn(e, r, t, a, n, i) { var s = Vr(n ? 68 : 48); var f = [s];
		s._W(2, 65534);
		s._W(2, 0);
		s._W(4, 842412599);
		s._W(16, L.utils.consts.HEADER_CLSID, "hex");
		s._W(4, n ? 2 : 1);
		s._W(16, r, "hex");
		s._W(4, n ? 68 : 48); var o = yn(e, t, a);
		f.push(o); if(n) { var l = yn(n, null, null);
			s._W(16, i, "hex");
			s._W(4, 68 + o.length);
			f.push(l) } return B(f) }

	function Dn(e, r) { e._R(r); return null }

	function On(e, r) { if(!r) r = Vr(e); for(var t = 0; t < e; ++t) r._W(1, 0); return r }

	function Fn(e, r, t) { var a = [],
			n = e.l + r; while(e.l < n) a.push(t(e, n - e.l)); if(n !== e.l) throw new Error("Slurp error"); return a }

	function Pn(e, r) { return e._R(r) === 1 }

	function Nn(e, r) { if(!r) r = Vr(2);
		r._W(2, +!!e); return r }

	function Ln(e) { return e._R(2, "u") }

	function Mn(e, r) { if(!r) r = Vr(2);
		r._W(2, e); return r }

	function Un(e, r) { return Fn(e, r, Ln) }

	function Hn(e) { var r = e._R(1),
			t = e._R(1); return t === 1 ? r : r === 1 }

	function Wn(e, r, t) { if(!t) t = Vr(2);
		t._W(1, +e);
		t._W(1, r == "e" ? 1 : 0); return t }

	function Vn(e, r, a) { var n = e._R(a && a.biff >= 12 ? 2 : 1); var i = "sbcs-cont"; var s = t; if(a && a.biff >= 8) t = 1200; if(!a || a.biff == 8) { var f = e._R(1); if(f) { i = "dbcs-cont" } } else if(a.biff == 12) { i = "wstr" } if(a.biff >= 2 && a.biff <= 5) i = "cpstr"; var o = n ? e._R(n, i) : "";
		t = s; return o }

	function zn(e) { var r = t;
		t = 1200; var a = e._R(2),
			n = e._R(1); var i = n & 4,
			s = n & 8; var f = 1 + (n & 1); var o = 0,
			l; var c = {}; if(s) o = e._R(2); if(i) l = e._R(4); var h = f == 2 ? "dbcs-cont" : "sbcs-cont"; var u = a === 0 ? "" : e._R(a, h); if(s) e.l += 4 * o; if(i) e.l += l;
		c.t = u; if(!s) { c.raw = "<t>" + c.t + "</t>";
			c.r = c.t } t = r; return c }

	function Xn(e, r, t) { var a; if(t) { if(t.biff >= 2 && t.biff <= 5) return e._R(r, "cpstr"); if(t.biff >= 12) return e._R(r, "dbcs-cont") } var n = e._R(1); if(n === 0) { a = e._R(r, "sbcs-cont") } else { a = e._R(r, "dbcs-cont") } return a }

	function Gn(e, r, t) { var a = e._R(t && t.biff == 2 ? 1 : 2); if(a === 0) { e.l++; return "" } return Xn(e, a, t) }

	function jn(e, r, t) { if(t.biff > 5) return Gn(e, r, t); var a = e._R(1); if(a === 0) { e.l++; return "" } return e._R(a, t.biff <= 4 || !e.lens ? "cpstr" : "sbcs-cont") }

	function Kn(e, r, t) { if(!t) t = Vr(3 + 2 * e.length);
		t._W(2, e.length);
		t._W(1, 1);
		t._W(31, e, "utf16le"); return t }

	function Yn(e) { var r = e._R(1);
		e.l++; var t = e._R(2);
		e.l += 2; return [r, t] }

	function $n(e) {
		var r = e._R(4),
			t = e.l;
		var a = false;
		if(r > 24) {
			e.l += r - 24;
			if(e._R(16) === "795881f43b1d7f48af2c825dc4852763") a = true;
			e.l = t
		}
		var n = e._R((a ? r - 24 : r) >> 1, "utf16le").replace(T, "");
		if(a) e.l += 24;
		return n
	}

	function Zn(e) { e.l += 2; var r = e._R(0, "lpstr-ansi");
		e.l += 2; if(e._R(2) != 57005) throw new Error("Bad FileMoniker"); var t = e._R(4); if(t === 0) return r.replace(/\\/g, "/"); var a = e._R(4); if(e._R(2) != 3) throw new Error("Bad FileMoniker"); var n = e._R(a >> 1, "utf16le").replace(T, ""); return n }

	function Qn(e, r) { var t = e._R(16);
		r -= 16; switch(t) {
			case "e0c9ea79f9bace118c8200aa004ba90b":
				return $n(e, r);
			case "0303000000000000c000000000000046":
				return Zn(e, r);
			default:
				throw new Error("Unsupported Moniker " + t); } }

	function Jn(e) { var r = e._R(4); var t = r > 0 ? e._R(r, "utf16le").replace(T, "") : ""; return t }

	function qn(e, r) { var t = e.l + r; var a = e._R(4); if(a !== 2) throw new Error("Unrecognized streamVersion: " + a); var n = e._R(2);
		e.l += 2; var i, s, f, o, l = "",
			c, h; if(n & 16) i = Jn(e, t - e.l); if(n & 128) s = Jn(e, t - e.l); if((n & 257) === 257) f = Jn(e, t - e.l); if((n & 257) === 1) o = Qn(e, t - e.l); if(n & 8) l = Jn(e, t - e.l); if(n & 32) c = e._R(16); if(n & 64) h = ln(e);
		e.l = t; var u = s || f || o || ""; if(u && l) u += "#" + l; if(!u) u = "#" + l; var d = { Target: u }; if(c) d.guid = c; if(h) d.time = h; if(i) d.Tooltip = i; return d }

	function ei(e) { var r = Vr(512),
			t = 0; var a = e.Target; var n = a.indexOf("#") > -1 ? 31 : 23; switch(a.charAt(0)) {
			case "#":
				n = 28; break;
			case ".":
				n &= ~2; break; } r._W(4, 2);
		r._W(4, n); var i = [8, 6815827, 6619237, 4849780, 83]; for(t = 0; t < i.length; ++t) r._W(4, i[t]); if(n == 28) { a = a.slice(1);
			r._W(4, a.length + 1); for(t = 0; t < a.length; ++t) r._W(2, a.charCodeAt(t));
			r._W(2, 0) } else if(n & 2) { i = "e0 c9 ea 79 f9 ba ce 11 8c 82 00 aa 00 4b a9 0b".split(" "); for(t = 0; t < i.length; ++t) r._W(1, parseInt(i[t], 16));
			r._W(4, 2 * (a.length + 1)); for(t = 0; t < a.length; ++t) r._W(2, a.charCodeAt(t));
			r._W(2, 0) } else { i = "03 03 00 00 00 00 00 00 c0 00 00 00 00 00 00 46".split(" "); for(t = 0; t < i.length; ++t) r._W(1, parseInt(i[t], 16)); var s = 0; while(a.slice(s * 3, s * 3 + 3) == "../" || a.slice(s * 3, s * 3 + 3) == "..\\") ++s;
			r._W(2, s);
			r._W(4, a.length + 1); for(t = 0; t < a.length; ++t) r._W(1, a.charCodeAt(t) & 255);
			r._W(1, 0);
			r._W(2, 65535);
			r._W(2, 57005); for(t = 0; t < 6; ++t) r._W(4, 0) } return r.slice(0, r.l) }

	function ri(e) { var r = e._R(1),
			t = e._R(1),
			a = e._R(1),
			n = e._R(1); return [r, t, a, n] }

	function ti(e, r) { var t = ri(e, r);
		t[3] = 0; return t }

	function ai(e) { var r = e._R(2); var t = e._R(2); var a = e._R(2); return { r: r, c: t, ixfe: a } }

	function ni(e, r, t, a) { if(!a) a = Vr(6);
		a._W(2, e);
		a._W(2, r);
		a._W(2, t || 0); return a }

	function ii(e) { var r = e._R(2); var t = e._R(2);
		e.l += 8; return { type: r, flags: t } }

	function si(e, r, t) { return r === 0 ? "" : jn(e, r, t) }

	function fi(e, r, t) { var a = t.biff > 8 ? 4 : 2; var n = e._R(a),
			i = e._R(a, "i"),
			s = e._R(a, "i"); return [n, i, s] }

	function oi(e) { var r = e._R(2); var t = Pt(e); return [r, t] }

	function li(e, r, t) { e.l += 4;
		r -= 4; var a = e.l + r; var n = Vn(e, r, t); var i = e._R(2);
		a -= e.l; if(i !== a) throw new Error("Malformed AddinUdf: padding = " + a + " != " + i);
		e.l += i; return n }

	function ci(e) { var r = e._R(2); var t = e._R(2); var a = e._R(2); var n = e._R(2); return { s: { c: a, r: r }, e: { c: n, r: t } } }

	function hi(e, r) { if(!r) r = Vr(8);
		r._W(2, e.s.r);
		r._W(2, e.e.r);
		r._W(2, e.s.c);
		r._W(2, e.e.c); return r }

	function ui(e) { var r = e._R(2); var t = e._R(2); var a = e._R(1); var n = e._R(1); return { s: { c: a, r: r }, e: { c: n, r: t } } }
	var di = ui;

	function pi(e) { e.l += 4; var r = e._R(2); var t = e._R(2); var a = e._R(2);
		e.l += 12; return [t, r, a] }

	function vi(e) { var r = {};
		e.l += 4;
		e.l += 16;
		r.fSharedNote = e._R(2);
		e.l += 4; return r }

	function gi(e) { var r = {};
		e.l += 4;
		e.cf = e._R(2); return r }

	function mi(e) { e.l += 2;
		e.l += e._R(2) }
	var bi = { 0: mi, 4: mi, 5: mi, 6: mi, 7: gi, 8: mi, 9: mi, 10: mi, 11: mi, 12: mi, 13: vi, 14: mi, 15: mi, 16: mi, 17: mi, 18: mi, 19: mi, 20: mi, 21: pi };

	function Ci(e, r) { var t = e.l + r; var a = []; while(e.l < t) { var n = e._R(2);
			e.l -= 2; try { a.push(bi[n](e, t - e.l)) } catch(i) { e.l = t; return a } } if(e.l != t) e.l = t; return a }

	function wi(e, r) { var t = { BIFFVer: 0, dt: 0 };
		t.BIFFVer = e._R(2);
		r -= 2; if(r >= 2) { t.dt = e._R(2);
			e.l -= 2 } switch(t.BIFFVer) {
			case 1536:
				;
			case 1280:
				;
			case 1024:
				;
			case 768:
				;
			case 512:
				;
			case 2:
				;
			case 7:
				break;
			default:
				if(r > 6) throw new Error("Unexpected BIFF Ver " + t.BIFFVer); } e._R(r); return t }

	function Ei(e, r, t) { var a = 1536,
			n = 16; switch(t.bookType) {
			case "biff8":
				break;
			case "biff5":
				a = 1280;
				n = 8; break;
			case "biff4":
				a = 4;
				n = 6; break;
			case "biff3":
				a = 3;
				n = 6; break;
			case "biff2":
				a = 2;
				n = 4; break;
			case "xla":
				break;
			default:
				throw new Error("unsupported BIFF version"); } var i = Vr(n);
		i._W(2, a);
		i._W(2, r); if(n > 4) i._W(2, 29282); if(n > 6) i._W(2, 1997); if(n > 8) { i._W(2, 49161);
			i._W(2, 1);
			i._W(2, 1798);
			i._W(2, 0) } return i }

	function ki(e, r) { if(r === 0) return 1200; if(e._R(2) !== 1200) {} return 1200 }

	function Si(e, r, t) { if(t.enc) { e.l += r; return "" } var a = e.l; var n = jn(e, 0, t);
		e._R(r + a - e.l); return n }

	function Ai(e, r) { var t = !r || r.biff == 8; var a = Vr(t ? 112 : 54);
		a._W(r.biff == 8 ? 2 : 1, 7); if(t) a._W(1, 0);
		a._W(4, 859007059);
		a._W(4, 5458548 | (t ? 0 : 536870912)); while(a.l < a.length) a._W(1, t ? 0 : 32); return a }

	function _i(e, r, t) { var a = t && t.biff == 8 || r == 2 ? e._R(2) : (e.l += r, 0); return { fDialog: a & 16 } }

	function Bi(e, r, t) { var a = e._R(4); var n = e._R(1) & 3; var i = e._R(1); switch(i) {
			case 0:
				i = "Worksheet"; break;
			case 1:
				i = "Macrosheet"; break;
			case 2:
				i = "Chartsheet"; break;
			case 6:
				i = "VBAModule"; break; } var s = Vn(e, 0, t); if(s.length === 0) s = "Sheet1"; return { pos: a, hs: n, dt: i, name: s } }

	function Ti(e, r) { var t = !r || r.biff >= 8 ? 2 : 1; var a = Vr(8 + t * e.name.length);
		a._W(4, e.pos);
		a._W(1, e.hs || 0);
		a._W(1, e.dt);
		a._W(1, e.name.length); if(r.biff >= 8) a._W(1, 1);
		a._W(t * e.name.length, e.name, r.biff < 8 ? "sbcs" : "utf16le"); var n = a.slice(0, a.l);
		n.l = a.l; return n }

	function xi(e, r) { var t = e.l + r; var a = e._R(4); var n = e._R(4); var i = []; for(var s = 0; s != n && e.l < t; ++s) { i.push(zn(e)) } i.Count = a;
		i.Unique = n; return i }

	function yi(e, r) { var t = {};
		t.dsst = e._R(2);
		e.l += r - 2; return t }

	function Ii(e) { var r = {};
		r.r = e._R(2);
		r.c = e._R(2);
		r.cnt = e._R(2) - r.c; var t = e._R(2);
		e.l += 4; var a = e._R(1);
		e.l += 3; if(a & 7) r.level = a & 7; if(a & 32) r.hidden = true; if(a & 64) r.hpt = t / 20; return r }

	function Ri(e) { var r = ii(e); if(r.type != 2211) throw new Error("Invalid Future Record " + r.type); var t = e._R(4); return t !== 0 }

	function Di(e) { e._R(2); return e._R(4) }

	function Oi(e, r, t) { var a = 0; if(!(t && t.biff == 2)) { a = e._R(2) } var n = e._R(2); if(t && t.biff == 2) { a = 1 - (n >> 15);
			n &= 32767 } var i = { Unsynced: a & 1, DyZero: (a & 2) >> 1, ExAsc: (a & 4) >> 2, ExDsc: (a & 8) >> 3 }; return [i, n] }

	function Fi(e) { var r = e._R(2),
			t = e._R(2),
			a = e._R(2),
			n = e._R(2); var i = e._R(2),
			s = e._R(2),
			f = e._R(2); var o = e._R(2),
			l = e._R(2); return { Pos: [r, t], Dim: [a, n], Flags: i, CurTab: s, FirstTab: f, Selected: o, TabRatio: l } }

	function Pi() { var e = Vr(18);
		e._W(2, 0);
		e._W(2, 0);
		e._W(2, 29280);
		e._W(2, 17600);
		e._W(2, 56);
		e._W(2, 0);
		e._W(2, 0);
		e._W(2, 1);
		e._W(2, 500); return e }

	function Ni(e, r, t) { if(t && t.biff >= 2 && t.biff < 8) return {}; var a = e._R(2); return { RTL: a & 64 } }

	function Li(e) { var r = Vr(18),
			t = 1718; if(e && e.RTL) t |= 64;
		r._W(2, t);
		r._W(4, 0);
		r._W(4, 64);
		r._W(4, 0);
		r._W(4, 0); return r }

	function Mi(e, r, t) { var a = { dyHeight: e._R(2), fl: e._R(2) }; switch(t && t.biff || 8) {
			case 2:
				break;
			case 3:
				;
			case 4:
				e.l += 2; break;
			default:
				e.l += 10; break; } a.name = Vn(e, 0, t); return a }

	function Ui(e, r) { var t = e.name || "Arial"; var a = r && r.biff == 5,
			n = a ? 15 + t.length : 16 + 2 * t.length; var i = Vr(n);
		i._W(2, (e.sz || 12) * 20);
		i._W(4, 0);
		i._W(2, 400);
		i._W(4, 0);
		i._W(2, 0);
		i._W(1, t.length); if(!a) i._W(1, 1);
		i._W((a ? 1 : 2) * t.length, t, a ? "sbcs" : "utf16le"); return i }

	function Hi(e) { var r = ai(e);
		r.isst = e._R(4); return r }

	function Wi(e, r, t) { var a = e.l + r; var n = ai(e, 6); if(t.biff == 2) e.l++; var i = Gn(e, a - e.l, t);
		n.val = i; return n }

	function Vi(e, r, t, a, n) { var i = !n || n.biff == 8; var s = Vr(6 + 2 + +i + (1 + i) * t.length);
		ni(e, r, a, s);
		s._W(2, t.length); if(i) s._W(1, 1);
		s._W((1 + i) * t.length, t, i ? "utf16le" : "sbcs"); return s }

	function zi(e, r, t) { var a = e._R(2); var n = jn(e, 0, t); return [a, n] }

	function Xi(e, r, t, a) { var n = t && t.biff == 5; if(!a) a = Vr(n ? 3 + r.length : 5 + 2 * r.length);
		a._W(2, e);
		a._W(n ? 1 : 2, r.length); if(!n) a._W(1, 1);
		a._W((n ? 1 : 2) * r.length, r, n ? "sbcs" : "utf16le"); var i = a.length > a.l ? a.slice(0, a.l) : a; if(i.l == null) i.l = i.length; return i }
	var Gi = jn;

	function ji(e, r, t) { var a = e.l + r; var n = t.biff == 8 || !t.biff ? 4 : 2; var i = e._R(n),
			s = e._R(n); var f = e._R(2),
			o = e._R(2);
		e.l = a; return { s: { r: i, c: f }, e: { r: s, c: o } } }

	function Ki(e, r) { var t = r.biff == 8 || !r.biff ? 4 : 2; var a = Vr(2 * t + 6);
		a._W(t, e.s.r);
		a._W(t, e.e.r + 1);
		a._W(2, e.s.c);
		a._W(2, e.e.c + 1);
		a._W(2, 0); return a }

	function Yi(e) { var r = e._R(2),
			t = e._R(2); var a = oi(e); return { r: r, c: t, ixfe: a[0], rknum: a[1] } }

	function $i(e, r) { var t = e.l + r - 2; var a = e._R(2),
			n = e._R(2); var i = []; while(e.l < t) i.push(oi(e)); if(e.l !== t) throw new Error("MulRK read error"); var s = e._R(2); if(i.length != s - n + 1) throw new Error("MulRK length mismatch"); return { r: a, c: n, C: s, rkrec: i } }

	function Zi(e, r) { var t = e.l + r - 2; var a = e._R(2),
			n = e._R(2); var i = []; while(e.l < t) i.push(e._R(2)); if(e.l !== t) throw new Error("MulBlank read error"); var s = e._R(2); if(i.length != s - n + 1) throw new Error("MulBlank length mismatch"); return { r: a, c: n, C: s, ixfe: i } }

	function Qi(e, r, t, a) { var n = {}; var i = e._R(4),
			s = e._R(4); var f = e._R(4),
			o = e._R(2);
		n.patternType = ma[f >> 26]; if(!a.cellStyles) return n;
		n.alc = i & 7;
		n.fWrap = i >> 3 & 1;
		n.alcV = i >> 4 & 7;
		n.fJustLast = i >> 7 & 1;
		n.trot = i >> 8 & 255;
		n.cIndent = i >> 16 & 15;
		n.fShrinkToFit = i >> 20 & 1;
		n.iReadOrder = i >> 22 & 2;
		n.fAtrNum = i >> 26 & 1;
		n.fAtrFnt = i >> 27 & 1;
		n.fAtrAlc = i >> 28 & 1;
		n.fAtrBdr = i >> 29 & 1;
		n.fAtrPat = i >> 30 & 1;
		n.fAtrProt = i >> 31 & 1;
		n.dgLeft = s & 15;
		n.dgRight = s >> 4 & 15;
		n.dgTop = s >> 8 & 15;
		n.dgBottom = s >> 12 & 15;
		n.icvLeft = s >> 16 & 127;
		n.icvRight = s >> 23 & 127;
		n.grbitDiag = s >> 30 & 3;
		n.icvTop = f & 127;
		n.icvBottom = f >> 7 & 127;
		n.icvDiag = f >> 14 & 127;
		n.dgDiag = f >> 21 & 15;
		n.icvFore = o & 127;
		n.icvBack = o >> 7 & 127;
		n.fsxButton = o >> 14 & 1; return n }

	function Ji(e, r, t) { var a = {};
		a.ifnt = e._R(2);
		a.numFmtId = e._R(2);
		a.flags = e._R(2);
		a.fStyle = a.flags >> 2 & 1;
		r -= 6;
		a.data = Qi(e, r, a.fStyle, t); return a }

	function qi(e, r, t, a) { var n = t && t.biff == 5; if(!a) a = Vr(n ? 16 : 20);
		a._W(2, 0); if(e.style) { a._W(2, e.numFmtId || 0);
			a._W(2, 65524) } else { a._W(2, e.numFmtId || 0);
			a._W(2, r << 4) } a._W(4, 0);
		a._W(4, 0); if(!n) a._W(4, 0);
		a._W(2, 0); return a }

	function es(e) { e.l += 4; var r = [e._R(2), e._R(2)]; if(r[0] !== 0) r[0]--; if(r[1] !== 0) r[1]--; if(r[0] > 7 || r[1] > 7) throw new Error("Bad Gutters: " + r.join("|")); return r }

	function rs(e) { var r = Vr(8);
		r._W(4, 0);
		r._W(2, e[0] ? e[0] + 1 : 0);
		r._W(2, e[1] ? e[1] + 1 : 0); return r }

	function ts(e, r, t) { var a = ai(e, 6); if(t.biff == 2) ++e.l; var n = Hn(e, 2);
		a.val = n;
		a.t = n === true || n === false ? "b" : "e"; return a }

	function as(e, r, t, a, n, i) { var s = Vr(8);
		ni(e, r, a, s);
		Wn(t, i, s); return s }

	function ns(e) { var r = ai(e, 6); var t = Wt(e, 8);
		r.val = t; return r }

	function is(e, r, t, a) { var n = Vr(14);
		ni(e, r, a, n);
		Vt(t, n); return n }
	var ss = si;

	function fs(e, r, t) { var a = e.l + r; var n = e._R(2); var i = e._R(2);
		t.sbcch = i; if(i == 1025 || i == 14849) return [i, n]; if(i < 1 || i > 255) throw new Error("Unexpected SupBook type: " + i); var s = Xn(e, i); var f = []; while(a > e.l) f.push(Gn(e)); return [i, n, s, f] }

	function os(e, r, t) { var a = e._R(2); var n; var i = { fBuiltIn: a & 1, fWantAdvise: a >>> 1 & 1, fWantPict: a >>> 2 & 1, fOle: a >>> 3 & 1, fOleLink: a >>> 4 & 1, cf: a >>> 5 & 1023, fIcon: a >>> 15 & 1 }; if(t.sbcch === 14849) n = li(e, r - 2, t);
		i.body = n || e._R(r - 2); if(typeof n === "string") i.Name = n; return i }
	var ls = ["_xlnm.Consolidate_Area", "_xlnm.Auto_Open", "_xlnm.Auto_Close", "_xlnm.Extract", "_xlnm.Database", "_xlnm.Criteria", "_xlnm.Print_Area", "_xlnm.Print_Titles", "_xlnm.Recorder", "_xlnm.Data_Form", "_xlnm.Auto_Activate", "_xlnm.Auto_Deactivate", "_xlnm.Sheet_Title", "_xlnm._FilterDatabase"];

	function cs(e, r, t) { var a = e.l + r; var n = e._R(2); var i = e._R(1); var s = e._R(1); var f = e._R(t && t.biff == 2 ? 1 : 2); var o = 0; if(!t || t.biff >= 5) { if(t.biff != 5) e.l += 2;
			o = e._R(2); if(t.biff == 5) e.l += 2;
			e.l += 4 } var l = Xn(e, s, t); if(n & 32) l = ls[l.charCodeAt(0)]; var c = a - e.l; if(t && t.biff == 2) --c; var h = a == e.l || f === 0 ? [] : _h(e, c, t, f); return { chKey: i, Name: l, itab: o, rgce: h } }

	function hs(e, r, t) { if(t.biff < 8) return us(e, r, t); var a = [],
			n = e.l + r,
			i = e._R(t.biff > 8 ? 4 : 2); while(i-- !== 0) a.push(fi(e, t.biff > 8 ? 12 : 6, t)); if(e.l != n) throw new Error("Bad ExternSheet: " + e.l + " != " + n); return a }

	function us(e, r, t) { if(e[e.l + 1] == 3) e[e.l]++; var a = Vn(e, r, t); return a.charCodeAt(0) == 3 ? a.slice(1) : a }

	function ds(e, r, t) { if(t.biff < 8) { e.l += r; return } var a = e._R(2); var n = e._R(2); var i = Xn(e, a, t); var s = Xn(e, n, t); return [i, s] }

	function ps(e, r, t) { var a = ui(e, 6);
		e.l++; var n = e._R(1);
		r -= 8; return [Bh(e, r, t), n, a] }

	function vs(e, r, t) { var a = di(e, 6); switch(t.biff) {
			case 2:
				e.l++;
				r -= 7; break;
			case 3:
				;
			case 4:
				e.l += 2;
				r -= 8; break;
			default:
				e.l += 6;
				r -= 12; } return [a, Sh(e, r, t, a)] }

	function gs(e) { var r = e._R(4) !== 0; var t = e._R(4) !== 0; var a = e._R(4); return [r, t, a] }

	function ms(e, r, t) { if(t.biff < 8) return; var a = e._R(2),
			n = e._R(2); var i = e._R(2),
			s = e._R(2); var f = jn(e, 0, t); if(t.biff < 8) e._R(1); return [{ r: a, c: n }, f, s, i] }

	function bs(e, r, t) { return ms(e, r, t) }

	function Cs(e, r) { var t = []; var a = e._R(2); while(a--) t.push(ci(e, r)); return t }

	function ws(e) { var r = Vr(2 + e.length * 8);
		r._W(2, e.length); for(var t = 0; t < e.length; ++t) hi(e[t], r); return r }

	function Es(e, r, t) { if(t && t.biff < 8) return Ss(e, r, t); var a = pi(e, 22); var n = Ci(e, r - 22, a[1]); return { cmo: a, ft: n } }
	var ks = [];
	ks[8] = function(e, r) { var t = e.l + r;
		e.l += 10; var a = e._R(2);
		e.l += 4;
		e.l += 2;
		e.l += 2;
		e.l += 2;
		e.l += 4; var n = e._R(1);
		e.l += n;
		e.l = t; return { fmt: a } };

	function Ss(e, r, t) { e.l += 4; var a = e._R(2); var n = e._R(2); var i = e._R(2);
		e.l += 2;
		e.l += 2;
		e.l += 2;
		e.l += 2;
		e.l += 2;
		e.l += 2;
		e.l += 2;
		e.l += 2;
		e.l += 2;
		e.l += 6;
		r -= 36; var s = [];
		s.push((ks[a] || Wr)(e, r, t)); return { cmo: [n, a, i], ft: s } }

	function As(e, r, t) { var a = e.l; var n = ""; try { e.l += 4; var i = (t.lastobj || { cmo: [0, 0] }).cmo[1]; var s; if([0, 5, 7, 11, 12, 14].indexOf(i) == -1) e.l += 6;
			else s = Yn(e, 6, t); var f = e._R(2);
			e._R(2);
			Ln(e, 2); var o = e._R(2);
			e.l += o; for(var l = 1; l < e.lens.length - 1; ++l) { if(e.l - a != e.lens[l]) throw new Error("TxO: bad continue record"); var c = e[e.l]; var h = Xn(e, e.lens[l + 1] - e.lens[l] - 1);
				n += h; if(n.length >= (c ? f : 2 * f)) break } if(n.length !== f && n.length !== f * 2) { throw new Error("cchText: " + f + " != " + n.length) } e.l = a + r; return { t: n } } catch(u) { e.l = a + r; return { t: n } } }

	function _s(e, r) { var t = ci(e, 8);
		e.l += 16; var a = qn(e, r - 24); return [t, a] }

	function Bs(e) { var r = Vr(24); var t = ft(e[0]);
		r._W(2, t.r);
		r._W(2, t.r);
		r._W(2, t.c);
		r._W(2, t.c); var a = "d0 c9 ea 79 f9 ba ce 11 8c 82 00 aa 00 4b a9 0b".split(" "); for(var n = 0; n < 16; ++n) r._W(1, parseInt(a[n], 16)); return B([r, ei(e[1])]) }

	function Ts(e, r) { e._R(2); var t = ci(e, 8); var a = e._R((r - 10) / 2, "dbcs-cont");
		a = a.replace(T, ""); return [t, a] }

	function xs(e) { var r = e[1].Tooltip; var t = Vr(10 + 2 * (r.length + 1));
		t._W(2, 2048); var a = ft(e[0]);
		t._W(2, a.r);
		t._W(2, a.r);
		t._W(2, a.c);
		t._W(2, a.c); for(var n = 0; n < r.length; ++n) t._W(2, r.charCodeAt(n));
		t._W(2, 0); return t }

	function ys(e) { var r = [0, 0],
			t;
		t = e._R(2);
		r[0] = ga[t] || t;
		t = e._R(2);
		r[1] = ga[t] || t; return r }

	function Is(e) { if(!e) e = Vr(4);
		e._W(2, 1);
		e._W(2, 1); return e }

	function Rs(e) { var r = e._R(2); var t = []; while(r-- > 0) t.push(ti(e, 8)); return t }

	function Ds(e) { var r = e._R(2); var t = []; while(r-- > 0) t.push(ti(e, 8)); return t }

	function Os(e) { e.l += 2; var r = { cxfs: 0, crc: 0 };
		r.cxfs = e._R(2);
		r.crc = e._R(4); return r }

	function Fs(e, r, t) { if(!t.cellStyles) return Wr(e, r); var a = t && t.biff >= 12 ? 4 : 2; var n = e._R(a); var i = e._R(a); var s = e._R(a); var f = e._R(a); var o = e._R(2); if(a == 2) e.l += 2; return { s: n, e: i, w: s, ixfe: f, flags: o } }

	function Ps(e, r) { var t = {}; if(r < 32) return t;
		e.l += 16;
		t.header = Wt(e, 8);
		t.footer = Wt(e, 8);
		e.l += 2; return t }

	function Ns(e, r, t) { var a = { area: false }; if(t.biff != 5) { e.l += r; return a } var n = e._R(1);
		e.l += 3; if(n & 16) a.area = true; return a }

	function Ls(e) { var r = Vr(2 * e); for(var t = 0; t < e; ++t) r._W(2, t + 1); return r }
	var Ms = ai;
	var Us = Un;
	var Hs = Gn;

	function Ws(e) { var r = e._R(2); var t = e._R(2); var a = e._R(4); var n = { fmt: r, env: t, len: a, data: e.slice(e.l, e.l + a) };
		e.l += a; return n }

	function Vs(e, r, t) { var a = ai(e, 6);++e.l; var n = jn(e, r - 7, t);
		a.t = "str";
		a.val = n; return a }

	function zs(e) { var r = ai(e, 6);++e.l; var t = Wt(e, 8);
		r.t = "n";
		r.val = t; return r }

	function Xs(e, r, t) { var a = Vr(15);
		uv(a, e, r);
		a._W(8, t, "f"); return a }

	function Gs(e) { var r = ai(e, 6);++e.l; var t = e._R(2);
		r.t = "n";
		r.val = t; return r }

	function js(e, r, t) { var a = Vr(9);
		uv(a, e, r);
		a._W(2, t); return a }

	function Ks(e) { var r = e._R(1); if(r === 0) { e.l++; return "" } return e._R(r, "sbcs-cont") }

	function Ys(e, r) { e.l += 6;
		e.l += 2;
		e.l += 1;
		e.l += 3;
		e.l += 1;
		e.l += r - 13 }

	function $s(e, r, t) { var a = e.l + r; var n = ai(e, 6); var i = e._R(2); var s = Xn(e, i, t);
		e.l = a;
		n.t = "str";
		n.val = s; return n }
	var Zs = function() { var e = { 1: 437, 2: 850, 3: 1252, 4: 1e4, 100: 852, 101: 866, 102: 865, 103: 861, 104: 895, 105: 620, 106: 737, 107: 857, 120: 950, 121: 949, 122: 936, 123: 932, 124: 874, 125: 1255, 126: 1256, 150: 10007, 151: 10029, 152: 10006, 200: 1250, 201: 1251, 202: 1254, 203: 1253, 0: 20127, 8: 865, 9: 437, 10: 850, 11: 437, 13: 437, 14: 850, 15: 437, 16: 850, 17: 437, 18: 850, 19: 932, 20: 850, 21: 437, 22: 850, 23: 865, 24: 437, 25: 437, 26: 850, 27: 437, 28: 863, 29: 850, 31: 852, 34: 852, 35: 852, 36: 860, 37: 850, 38: 866, 55: 850, 64: 852, 77: 936, 78: 949, 79: 950, 80: 874, 87: 1252, 88: 1252, 89: 1252, 255: 16969 };

		function r(r, t) { var a = []; var n = w(1); switch(t.type) {
				case "base64":
					n = E(b.decode(r)); break;
				case "binary":
					n = E(r); break;
				case "buffer":
					;
				case "array":
					n = r; break; } Hr(n, 0); var i = n._R(1); var s = false; var f = false,
				o = false; switch(i) {
				case 2:
					;
				case 3:
					break;
				case 48:
					f = true;
					s = true; break;
				case 49:
					f = true; break;
				case 131:
					s = true; break;
				case 139:
					s = true; break;
				case 140:
					s = true;
					o = true; break;
				case 245:
					s = true; break;
				default:
					throw new Error("DBF Unsupported Version: " + i.toString(16)); } var l = 0,
				c = 0; if(i == 2) l = n._R(2);
			n.l += 3; if(i != 2) l = n._R(4); if(i != 2) c = n._R(2); var h = n._R(2); var u = 1252; if(i != 2) { n.l += 16;
				n._R(1); if(n[n.l] !== 0) u = e[n[n.l]];
				n.l += 1;
				n.l += 2 } if(o) n.l += 36; var d = [],
				p = {}; var v = c - 10 - (f ? 264 : 0),
				g = o ? 32 : 11; while(i == 2 ? n.l < n.length && n[n.l] != 13 : n.l < v) { p = {};
				p.name = cptable.utils.decode(u, n.slice(n.l, n.l + g)).replace(/[\u0000\r\n].*$/g, "");
				n.l += g;
				p.type = String.fromCharCode(n._R(1)); if(i != 2 && !o) p.offset = n._R(4);
				p.len = n._R(1); if(i == 2) p.offset = n._R(2);
				p.dec = n._R(1); if(p.name.length) d.push(p); if(i != 2) n.l += o ? 13 : 14; switch(p.type) {
					case "B":
						if((!f || p.len != 8) && t.WTF) console.log("Skipping " + p.name + ":" + p.type); break;
					case "G":
						;
					case "P":
						if(t.WTF) console.log("Skipping " + p.name + ":" + p.type); break;
					case "C":
						;
					case "D":
						;
					case "F":
						;
					case "I":
						;
					case "L":
						;
					case "M":
						;
					case "N":
						;
					case "O":
						;
					case "T":
						;
					case "Y":
						;
					case "0":
						;
					case "@":
						;
					case "+":
						break;
					default:
						throw new Error("Unknown Field Type: " + p.type); } } if(n[n.l] !== 13) n.l = c - 1;
			else if(i == 2) n.l = 521; if(i != 2) { if(n._R(1) !== 13) throw new Error("DBF Terminator not found " + n.l + " " + n[n.l]);
				n.l = c } var m = 0,
				C = 0;
			a[0] = []; for(C = 0; C != d.length; ++C) a[0][C] = d[C].name; while(l-- > 0) { if(n[n.l] === 42) { n.l += h; continue }++n.l;
				a[++m] = [];
				C = 0; for(C = 0; C != d.length; ++C) { var k = n.slice(n.l, n.l + d[C].len);
					n.l += d[C].len;
					Hr(k, 0); var S = cptable.utils.decode(u, k); switch(d[C].type) {
						case "C":
							a[m][C] = cptable.utils.decode(u, k);
							a[m][C] = a[m][C].trim(); break;
						case "D":
							if(S.length === 8) a[m][C] = new Date(+S.slice(0, 4), +S.slice(4, 6) - 1, +S.slice(6, 8));
							else a[m][C] = S; break;
						case "F":
							a[m][C] = parseFloat(S.trim()); break;
						case "+":
							;
						case "I":
							a[m][C] = o ? k._R(-4, "i") ^ 2147483648 : k._R(4, "i"); break;
						case "L":
							switch(S.toUpperCase()) {
								case "Y":
									;
								case "T":
									a[m][C] = true; break;
								case "N":
									;
								case "F":
									a[m][C] = false; break;
								case " ":
									;
								case "?":
									a[m][C] = false; break;
								default:
									throw new Error("DBF Unrecognized L:|" + S + "|"); } break;
						case "M":
							if(!s) throw new Error("DBF Unexpected MEMO for type " + i.toString(16));
							a[m][C] = "##MEMO##" + (o ? parseInt(S.trim(), 10) : k._R(4)); break;
						case "N":
							a[m][C] = +S.replace(/\u0000/g, "").trim(); break;
						case "@":
							a[m][C] = new Date(k._R(-8, "f") - 621356832e5); break;
						case "T":
							a[m][C] = new Date((k._R(4) - 2440588) * 864e5 + k._R(4)); break;
						case "Y":
							a[m][C] = k._R(4, "i") / 1e4; break;
						case "O":
							a[m][C] = -k._R(-8, "f"); break;
						case "B":
							if(f && d[C].len == 8) { a[m][C] = k._R(8, "f"); break };
						case "G":
							;
						case "P":
							k.l += d[C].len; break;
						case "0":
							if(d[C].name === "_NullFlags") break;
						default:
							throw new Error("DBF Unsupported data type " + d[C].type); } } } if(i != 2)
				if(n.l < n.length && n[n.l++] != 26) throw new Error("DBF EOF Marker missing " + (n.l - 1) + " of " + n.length + " " + n[n.l - 1].toString(16)); if(t && t.sheetRows) a = a.slice(0, t.sheetRows); return a }

		function t(e, t) { var a = t || {}; if(!a.dateNF) a.dateNF = "yyyymmdd"; return gt(r(e, a), a) }

		function a(e, r) { try { return pt(t(e, r), r) } catch(a) { if(r && r.WTF) throw a } return { SheetNames: [], Sheets: {} } } var n = { B: 8, C: 250, L: 1, D: 8, "?": 0, "": 0 };

		function i(e, r) { var t = r || {}; if(t.type == "string") throw new Error("Cannot write DBF to JS string"); var a = Xr(); var i = Sg(e, { header: 1, raw: true, cellDates: true }); var s = i[0],
				f = i.slice(1); var o = 0,
				l = 0,
				c = 0,
				h = 1; for(o = 0; o < s.length; ++o) { if(o == null) continue;++c; if(typeof s[o] === "number") s[o] = s[o].toString(10); if(typeof s[o] !== "string") throw new Error("DBF Invalid column name " + s[o] + " |" + typeof s[o] + "|"); if(s.indexOf(s[o]) !== o)
					for(l = 0; l < 1024; ++l)
						if(s.indexOf(s[o] + "_" + l) == -1) { s[o] += "_" + l; break } } var u = ht(e["!ref"]); var d = []; for(o = 0; o <= u.e.c - u.s.c; ++o) { var p = []; for(l = 0; l < f.length; ++l) { if(f[l][o] != null) p.push(f[l][o]) } if(p.length == 0 || s[o] == null) { d[o] = "?"; continue } var v = "",
					g = ""; for(l = 0; l < p.length; ++l) { switch(typeof p[l]) {
						case "number":
							g = "B"; break;
						case "string":
							g = "C"; break;
						case "boolean":
							g = "L"; break;
						case "object":
							g = p[l] instanceof Date ? "D" : "C"; break;
						default:
							g = "C"; } v = v && v != g ? "C" : g; if(v == "C") break } h += n[v] || 0;
				d[o] = v } var m = a.next(32);
			m._W(4, 318902576);
			m._W(4, f.length);
			m._W(2, 296 + 32 * c);
			m._W(2, h); for(o = 0; o < 4; ++o) m._W(4, 0);
			m._W(4, 768); for(o = 0, l = 0; o < s.length; ++o) { if(s[o] == null) continue; var b = a.next(32); var C = (s[o].slice(-10) + "\0\0\0\0\0\0\0\0\0\0\0").slice(0, 11);
				b._W(1, C, "sbcs");
				b._W(1, d[o] == "?" ? "C" : d[o], "sbcs");
				b._W(4, l);
				b._W(1, n[d[o]] || 0);
				b._W(1, 0);
				b._W(1, 2);
				b._W(4, 0);
				b._W(1, 0);
				b._W(4, 0);
				b._W(4, 0);
				l += n[d[o]] || 0 } var w = a.next(264);
			w._W(4, 13); for(o = 0; o < 65; ++o) w._W(4, 0); for(o = 0; o < f.length; ++o) { var E = a.next(h);
				E._W(1, 0); for(l = 0; l < s.length; ++l) { if(s[l] == null) continue; switch(d[l]) {
						case "L":
							E._W(1, f[o][l] == null ? 63 : f[o][l] ? 84 : 70); break;
						case "B":
							E._W(8, f[o][l] || 0, "f"); break;
						case "D":
							if(!f[o][l]) E._W(8, "00000000", "sbcs");
							else { E._W(4, ("0000" + f[o][l].getFullYear()).slice(-4), "sbcs");
								E._W(2, ("00" + (f[o][l].getMonth() + 1)).slice(-2), "sbcs");
								E._W(2, ("00" + f[o][l].getDate()).slice(-2), "sbcs") } break;
						case "C":
							var k = String(f[o][l] || "");
							E._W(1, k, "sbcs"); for(c = 0; c < 250 - k.length; ++c) E._W(1, 32); break; } } } a.next(1)._W(1, 26); return a.end() } return { to_workbook: a, to_sheet: t, from_sheet: i } }();
	var Qs = function() {
		function e(e, t) { switch(t.type) {
				case "base64":
					return r(b.decode(e), t);
				case "binary":
					return r(e, t);
				case "buffer":
					return r(e.toString("binary"), t);
				case "array":
					return r(ae(e), t); } throw new Error("Unrecognized type " + t.type) }

		function r(e, r) { var t = e.split(/[\n\r]+/),
				a = -1,
				n = -1,
				i = 0,
				s = 0,
				f = []; var o = []; var l = null; var c = {},
				h = [],
				u = [],
				d = []; var p = 0,
				v; for(; i !== t.length; ++i) { p = 0; var g = t[i].trim(); var m = g.replace(/;;/g, "").split(";").map(function(e) { return e.replace(/\u0001/g, ";") }); var b = m[0],
					C; if(g.length > 0) switch(b) {
					case "ID":
						break;
					case "E":
						break;
					case "B":
						break;
					case "O":
						break;
					case "P":
						if(m[1].charAt(0) == "P") o.push(g.slice(3).replace(/;;/g, ";")); break;
					case "C":
						var w = false; for(s = 1; s < m.length; ++s) switch(m[s].charAt(0)) {
							case "X":
								n = parseInt(m[s].slice(1)) - 1; break;
							case "Y":
								a = parseInt(m[s].slice(1)) - 1;
								n = 0; for(v = f.length; v <= a; ++v) f[v] = []; break;
							case "K":
								C = m[s].slice(1); if(C.charAt(0) === '"') C = C.slice(1, C.length - 1);
								else if(C === "TRUE") C = true;
								else if(C === "FALSE") C = false;
								else if(!isNaN(se(C))) { C = se(C); if(l !== null && y.is_date(l)) C = J(C) } else if(!isNaN(fe(C).getDate())) { C = te(C) } w = true; break;
							case "E":
								var E = Wl(m[s].slice(1), { r: a, c: n });
								f[a][n] = [f[a][n], E]; break;
							default:
								if(r && r.WTF) throw new Error("SYLK bad record " + g); }
						if(w) { f[a][n] = C;
							l = null } break;
					case "F":
						var k = 0; for(s = 1; s < m.length; ++s) switch(m[s].charAt(0)) {
							case "X":
								n = parseInt(m[s].slice(1)) - 1;++k; break;
							case "Y":
								a = parseInt(m[s].slice(1)) - 1; for(v = f.length; v <= a; ++v) f[v] = []; break;
							case "M":
								p = parseInt(m[s].slice(1)) / 20; break;
							case "F":
								break;
							case "G":
								break;
							case "P":
								l = o[parseInt(m[s].slice(1))]; break;
							case "S":
								break;
							case "D":
								break;
							case "N":
								break;
							case "W":
								d = m[s].slice(1).split(" "); for(v = parseInt(d[0], 10); v <= parseInt(d[1], 10); ++v) { p = parseInt(d[2], 10);
									u[v - 1] = p === 0 ? { hidden: true } : { wch: p };
									no(u[v - 1]) } break;
							case "C":
								n = parseInt(m[s].slice(1)) - 1; if(!u[n]) u[n] = {}; break;
							case "R":
								a = parseInt(m[s].slice(1)) - 1; if(!h[a]) h[a] = {}; if(p > 0) { h[a].hpt = p;
									h[a].hpx = oo(p) } else if(p === 0) h[a].hidden = true; break;
							default:
								if(r && r.WTF) throw new Error("SYLK bad record " + g); }
						if(k < 1) l = null; break;
					default:
						if(r && r.WTF) throw new Error("SYLK bad record " + g); } } if(h.length > 0) c["!rows"] = h; if(u.length > 0) c["!cols"] = u; if(r && r.sheetRows) f = f.slice(0, r.sheetRows); return [f, c] }

		function t(r, t) { var a = e(r, t); var n = a[0],
				i = a[1]; var s = gt(n, t);
			z(i).forEach(function(e) { s[e] = i[e] }); return s }

		function a(e, r) { return pt(t(e, r), r) }

		function n(e, r, t, a) { var n = "C;Y" + (t + 1) + ";X" + (a + 1) + ";K"; switch(e.t) {
				case "n":
					n += e.v || 0; if(e.f && !e.F) n += ";E" + zl(e.f, { r: t, c: a }); break;
				case "b":
					n += e.v ? "TRUE" : "FALSE"; break;
				case "e":
					n += e.w || e.v; break;
				case "d":
					n += '"' + (e.w || e.v) + '"'; break;
				case "s":
					n += '"' + e.v.replace(/"/g, "") + '"'; break; } return n }

		function i(e, r) { r.forEach(function(r, t) { var a = "F;W" + (t + 1) + " " + (t + 1) + " "; if(r.hidden) a += "0";
				else { if(typeof r.width == "number") r.wpx = qf(r.width); if(typeof r.wpx == "number") r.wch = eo(r.wpx); if(typeof r.wch == "number") a += Math.round(r.wch) } if(a.charAt(a.length - 1) != " ") e.push(a) }) }

		function s(e, r) { r.forEach(function(r, t) { var a = "F;"; if(r.hidden) a += "M0;";
				else if(r.hpt) a += "M" + 20 * r.hpt + ";";
				else if(r.hpx) a += "M" + 20 * fo(r.hpx) + ";"; if(a.length > 2) e.push(a + "R" + (t + 1)) }) }

		function f(e, r) { var t = ["ID;PWXL;N;E"],
				a = []; var f = ht(e["!ref"]),
				o; var l = Array.isArray(e); var c = "\r\n";
			t.push("P;PGeneral");
			t.push("F;P0;DG0G8;M255"); if(e["!cols"]) i(t, e["!cols"]); if(e["!rows"]) s(t, e["!rows"]);
			t.push("B;Y" + (f.e.r - f.s.r + 1) + ";X" + (f.e.c - f.s.c + 1) + ";D" + [f.s.c, f.s.r, f.e.c, f.e.r].join(" ")); for(var h = f.s.r; h <= f.e.r; ++h) { for(var u = f.s.c; u <= f.e.c; ++u) { var d = ot({ r: h, c: u });
					o = l ? (e[h] || [])[u] : e[d]; if(!o || o.v == null && (!o.f || o.F)) continue;
					a.push(n(o, e, h, u, r)) } } return t.join(c) + c + a.join(c) + c + "E" + c } return { to_workbook: a, to_sheet: t, from_sheet: f } }();
	var Js = function() {
		function e(e, t) { switch(t.type) {
				case "base64":
					return r(b.decode(e), t);
				case "binary":
					return r(e, t);
				case "buffer":
					return r(e.toString("binary"), t);
				case "array":
					return r(ae(e), t); } throw new Error("Unrecognized type " + t.type) }

		function r(e, r) { var t = e.split("\n"),
				a = -1,
				n = -1,
				i = 0,
				s = []; for(; i !== t.length; ++i) { if(t[i].trim() === "BOT") { s[++a] = [];
					n = 0; continue } if(a < 0) continue; var f = t[i].trim().split(","); var o = f[0],
					l = f[1];++i; var c = t[i].trim(); switch(+o) {
					case -1:
						if(c === "BOT") { s[++a] = [];
							n = 0; continue } else if(c !== "EOD") throw new Error("Unrecognized DIF special command " + c); break;
					case 0:
						if(c === "TRUE") s[a][n] = true;
						else if(c === "FALSE") s[a][n] = false;
						else if(!isNaN(se(l))) s[a][n] = se(l);
						else if(!isNaN(fe(l).getDate())) s[a][n] = te(l);
						else s[a][n] = l;++n; break;
					case 1:
						c = c.slice(1, c.length - 1);
						s[a][n++] = c !== "" ? c : null; break; } if(c === "EOD") break } if(r && r.sheetRows) s = s.slice(0, r.sheetRows); return s }

		function t(r, t) { return gt(e(r, t), t) }

		function a(e, r) { return pt(t(e, r), r) } var n = function() { var e = function t(e, r, a, n, i) { e.push(r);
				e.push(a + "," + n);
				e.push('"' + i.replace(/"/g, '""') + '"') }; var r = function a(e, r, t, n) { e.push(r + "," + t);
				e.push(r == 1 ? '"' + n.replace(/"/g, '""') + '"' : n) }; return function n(t) { var a = []; var n = ht(t["!ref"]),
					i; var s = Array.isArray(t);
				e(a, "TABLE", 0, 1, "sheetjs");
				e(a, "VECTORS", 0, n.e.r - n.s.r + 1, "");
				e(a, "TUPLES", 0, n.e.c - n.s.c + 1, "");
				e(a, "DATA", 0, 0, ""); for(var f = n.s.r; f <= n.e.r; ++f) { r(a, -1, 0, "BOT"); for(var o = n.s.c; o <= n.e.c; ++o) { var l = ot({ r: f, c: o });
						i = s ? (t[f] || [])[o] : t[l]; if(!i) { r(a, 1, 0, ""); continue } switch(i.t) {
							case "n":
								var c = m ? i.w : i.v; if(!c && i.v != null) c = i.v; if(c == null) { if(m && i.f && !i.F) r(a, 1, 0, "=" + i.f);
									else r(a, 1, 0, "") } else r(a, 0, c, "V"); break;
							case "b":
								r(a, 0, i.v ? 1 : 0, i.v ? "TRUE" : "FALSE"); break;
							case "s":
								r(a, 1, 0, !m || isNaN(i.v) ? i.v : '="' + i.v + '"'); break;
							case "d":
								if(!i.w) i.w = y.format(i.z || y._table[14], Q(te(i.v))); if(m) r(a, 0, i.w, "V");
								else r(a, 1, 0, i.w); break;
							default:
								r(a, 1, 0, ""); } } } r(a, -1, 0, "EOD"); var h = "\r\n"; var u = a.join(h); return u } }(); return { to_workbook: a, to_sheet: t, from_sheet: n } }();
	var qs = function() {
		function e(e) { return e.replace(/\\b/g, "\\").replace(/\\c/g, ":").replace(/\\n/g, "\n") }

		function r(e) { return e.replace(/\\/g, "\\b").replace(/:/g, "\\c").replace(/\n/g, "\\n") }

		function t(r, t) { var a = r.split("\n"),
				n = -1,
				i = -1,
				s = 0,
				f = []; for(; s !== a.length; ++s) { var o = a[s].trim().split(":"); if(o[0] !== "cell") continue; var l = ft(o[1]); if(f.length <= l.r)
					for(n = f.length; n <= l.r; ++n)
						if(!f[n]) f[n] = [];
				n = l.r;
				i = l.c; switch(o[2]) {
					case "t":
						f[n][i] = e(o[3]); break;
					case "v":
						f[n][i] = +o[3]; break;
					case "vtf":
						var c = o[o.length - 1];
					case "vtc":
						switch(o[3]) {
							case "nl":
								f[n][i] = +o[4] ? true : false; break;
							default:
								f[n][i] = +o[4]; break; } if(o[2] == "vtf") f[n][i] = [f[n][i], c]; } } if(t && t.sheetRows) f = f.slice(0, t.sheetRows); return f }

		function a(e, r) { return gt(t(e, r), r) }

		function n(e, r) { return pt(a(e, r), r) } var i = ["socialcalc:version:1.5", "MIME-Version: 1.0", "Content-Type: multipart/mixed; boundary=SocialCalcSpreadsheetControlSave"].join("\n"); var s = ["--SocialCalcSpreadsheetControlSave", "Content-type: text/plain; charset=UTF-8"].join("\n") + "\n"; var f = ["# SocialCalc Spreadsheet Control Save", "part:sheet"].join("\n"); var o = "--SocialCalcSpreadsheetControlSave--";

		function l(e) { if(!e || !e["!ref"]) return ""; var t = [],
				a = [],
				n, i = ""; var s = lt(e["!ref"]); var f = Array.isArray(e); for(var o = s.s.r; o <= s.e.r; ++o) { for(var l = s.s.c; l <= s.e.c; ++l) { i = ot({ r: o, c: l });
					n = f ? (e[o] || [])[l] : e[i]; if(!n || n.v == null || n.t === "z") continue;
					a = ["cell", i, "t"]; switch(n.t) {
						case "s":
							;
						case "str":
							a.push(r(n.v)); break;
						case "n":
							if(!n.f) { a[2] = "v";
								a[3] = n.v } else { a[2] = "vtf";
								a[3] = "n";
								a[4] = n.v;
								a[5] = r(n.f) } break;
						case "b":
							a[2] = "vt" + (n.f ? "f" : "c");
							a[3] = "nl";
							a[4] = n.v ? "1" : "0";
							a[5] = r(n.f || (n.v ? "TRUE" : "FALSE")); break;
						case "d":
							var c = Q(te(n.v));
							a[2] = "vtc";
							a[3] = "nd";
							a[4] = "" + c;
							a[5] = n.w || y.format(n.z || y._table[14], c); break;
						case "e":
							continue; } t.push(a.join(":")) } } t.push("sheet:c:" + (s.e.c - s.s.c + 1) + ":r:" + (s.e.r - s.s.r + 1) + ":tvf:1");
			t.push("valueformat:1:text-wiki"); return t.join("\n") }

		function c(e) { return [i, s, f, s, l(e), o].join("\n") } return { to_workbook: n, to_sheet: a, from_sheet: c } }();
	var ef = function() {
		function e(e, r, t, a, n) { if(n.raw) r[t][a] = e;
			else if(e === "TRUE") r[t][a] = true;
			else if(e === "FALSE") r[t][a] = false;
			else if(e === "") {} else if(!isNaN(se(e))) r[t][a] = se(e);
			else if(!isNaN(fe(e).getDate())) r[t][a] = te(e);
			else r[t][a] = e }

		function r(r, t) { var a = t || {}; var n = []; if(!r || r.length === 0) return n; var i = r.split(/[\r\n]/); var s = i.length - 1; while(s >= 0 && i[s].length === 0) --s; var f = 10,
				o = 0; var l = 0; for(; l <= s; ++l) { o = i[l].indexOf(" "); if(o == -1) o = i[l].length;
				else o++;
				f = Math.max(f, o) } for(l = 0; l <= s; ++l) { n[l] = []; var c = 0;
				e(i[l].slice(0, f).trim(), n, l, c, a); for(c = 1; c <= (i[l].length - f) / 10 + 1; ++c) e(i[l].slice(f + (c - 1) * 10, f + c * 10).trim(), n, l, c, a) } if(a.sheetRows) n = n.slice(0, a.sheetRows); return n }
		var t = { 44: ",", 9: "\t", 59: ";" };
		var a = { 44: 3, 9: 2, 59: 1 };

		function n(e) {
			var r = {},
				n = false,
				i = 0,
				s = 0;
			for(; i < e.length; ++i) { if((s = e.charCodeAt(i)) == 34) n = !n;
				else if(!n && s in t) r[s] = (r[s] || 0) + 1 } s = [];
			for(i in r)
				if(r.hasOwnProperty(i)) { s.push([r[i], i]) }
			if(!s.length) {
				r = a;
				for(i in r)
					if(r.hasOwnProperty(i)) {
						s.push([r[i], i])
					}
			}
			s.sort(function(e, r) { return e[0] - r[0] || a[e[1]] - a[r[1]] });
			return t[s.pop()[1]]
		}

		function i(e, r) { var t = r || {}; var a = ""; if(g != null && t.dense == null) t.dense = g; var i = t.dense ? [] : {}; var s = { s: { c: 0, r: 0 }, e: { c: 0, r: 0 } }; if(e.slice(0, 4) == "sep=" && e.charCodeAt(5) == 10) { a = e.charAt(4);
				e = e.slice(6) } else a = n(e.slice(0, 1024)); var f = 0,
				o = 0,
				l = 0; var c = 0,
				h = 0,
				u = a.charCodeAt(0),
				d = false,
				p = 0;
			e = e.replace(/\r\n/gm, "\n"); var v = t.dateNF != null ? F(t.dateNF) : null;

			function m() { var r = e.slice(c, h); var a = {}; if(r.charAt(0) == '"' && r.charAt(r.length - 1) == '"') r = r.slice(1, -1).replace(/""/g, '"'); if(r.length === 0) a.t = "z";
				else if(t.raw) { a.t = "s";
					a.v = r } else if(r.trim().length === 0) { a.t = "s";
					a.v = r } else if(r.charCodeAt(0) == 61) { if(r.charCodeAt(1) == 34 && r.charCodeAt(r.length - 1) == 34) { a.t = "s";
						a.v = r.slice(2, -1).replace(/""/g, '"') } else if(jl(r)) { a.t = "n";
						a.f = r.slice(1) } else { a.t = "s";
						a.v = r } } else if(r == "TRUE") { a.t = "b";
					a.v = true } else if(r == "FALSE") { a.t = "b";
					a.v = false } else if(!isNaN(l = se(r))) { a.t = "n"; if(t.cellText !== false) a.w = r;
					a.v = l } else if(!isNaN(fe(r).getDate()) || v && r.match(v)) { a.z = t.dateNF || y._table[14]; var n = 0; if(v && r.match(v)) { r = P(r, t.dateNF, r.match(v) || []);
						n = 1 } if(t.cellDates) { a.t = "d";
						a.v = te(r, n) } else { a.t = "n";
						a.v = Q(te(r, n)) } if(t.cellText !== false) a.w = y.format(a.z, a.v instanceof Date ? Q(a.v) : a.v); if(!t.cellNF) delete a.z } else { a.t = "s";
					a.v = r } if(a.t == "z") {} else if(t.dense) { if(!i[f]) i[f] = [];
					i[f][o] = a } else i[ot({ c: o, r: f })] = a;
				c = h + 1; if(s.e.c < o) s.e.c = o; if(s.e.r < f) s.e.r = f; if(p == u) ++o;
				else { o = 0;++f; if(t.sheetRows && t.sheetRows <= f) return true } } e: for(; h < e.length; ++h) switch(p = e.charCodeAt(h)) {
				case 34:
					d = !d; break;
				case u:
					;
				case 10:
					;
				case 13:
					if(!d && m()) break e; break;
				default:
					break; }
			if(h - c > 0) m();
			i["!ref"] = ct(s); return i }

		function s(e, t) { if(e.slice(0, 4) == "sep=") return i(e, t); if(e.indexOf("\t") >= 0 || e.indexOf(",") >= 0 || e.indexOf(";") >= 0) return i(e, t); return gt(r(e, t), t) }

		function f(e, r) { var t = "",
				a = r.type == "string" ? [0, 0, 0, 0] : ng(e, r); switch(r.type) {
				case "base64":
					t = b.decode(e); break;
				case "binary":
					t = e; break;
				case "buffer":
					t = e.toString("binary"); break;
				case "array":
					t = ae(e); break;
				case "string":
					t = e; break;
				default:
					throw new Error("Unrecognized type " + r.type); } if(a[0] == 239 && a[1] == 187 && a[2] == 191) t = He(t.slice(3));
			else if((r.type == "binary" || r.type == "buffer") && typeof cptable !== "undefined" && r.codepage) t = cptable.utils.decode(r.codepage, cptable.utils.encode(1252, t)); if(t.slice(0, 19) == "socialcalc:version:") return qs.to_sheet(r.type == "string" ? t : He(t), r); return s(t, r) }

		function o(e, r) { return pt(f(e, r), r) }

		function l(e) { var r = []; var t = ht(e["!ref"]),
				a; var n = Array.isArray(e); for(var i = t.s.r; i <= t.e.r; ++i) { var s = []; for(var f = t.s.c; f <= t.e.c; ++f) { var o = ot({ r: i, c: f });
					a = n ? (e[i] || [])[f] : e[o]; if(!a || a.v == null) { s.push("          "); continue } var l = (a.w || (dt(a), a.w) || "").slice(0, 10); while(l.length < 10) l += " ";
					s.push(l + (f === 0 ? " " : "")) } r.push(s.join("")) } return r.join("\n") }
		return { to_workbook: o, to_sheet: f, from_sheet: l }
	}();

	function rf(e, r) { var t = r || {},
			a = !!t.WTF;
		t.WTF = true; try { var n = Qs.to_workbook(e, t);
			t.WTF = a; return n } catch(i) { t.WTF = a; if(!i.message.match(/SYLK bad record ID/) && a) throw i; return ef.to_workbook(e, r) } }
	var tf = function() {
		function e(e, r, t) { if(!e) return;
			Hr(e, e.l || 0); var a = t.Enum || C; while(e.l < e.length) { var n = e._R(2); var i = a[n] || a[255]; var s = e._R(2); var f = e.l + s; var o = (i.f || Wr)(e, s, t);
				e.l = f; if(r(o, i.n, n)) return } }

		function r(e, r) { switch(r.type) {
				case "base64":
					return t(E(b.decode(e)), r);
				case "binary":
					return t(E(e), r);
				case "buffer":
					;
				case "array":
					return t(e, r); } throw "Unsupported type " + r.type }

		function t(r, t) { if(!r) return r; var a = t || {}; if(g != null && a.dense == null) a.dense = g; var n = a.dense ? [] : {},
				i = "Sheet1",
				s = 0; var f = {},
				o = [i]; var l = { s: { r: 0, c: 0 }, e: { r: 0, c: 0 } }; var c = a.sheetRows || 0; if(r[2] == 2) a.Enum = C;
			else if(r[2] == 26) a.Enum = w;
			else if(r[2] == 14) { a.Enum = w;
				a.qpro = true;
				r.l = 0 } else throw new Error("Unrecognized LOTUS BOF " + r[2]);
			e(r, function(e, t, h) { if(r[2] == 2) switch(h) {
					case 0:
						a.vers = e; if(e >= 4096) a.qpro = true; break;
					case 6:
						l = e; break;
					case 15:
						if(!a.qpro) e[1].v = e[1].v.slice(1);
					case 13:
						;
					case 14:
						;
					case 16:
						;
					case 51:
						if(h == 14 && (e[2] & 112) == 112 && (e[2] & 15) > 1 && (e[2] & 15) < 15) { e[1].z = a.dateNF || y._table[14]; if(a.cellDates) { e[1].t = "d";
								e[1].v = J(e[1].v) } } if(a.dense) { if(!n[e[0].r]) n[e[0].r] = [];
							n[e[0].r][e[0].c] = e[1] } else n[ot(e[0])] = e[1]; break; } else switch(h) {
					case 22:
						e[1].v = e[1].v.slice(1);
					case 23:
						;
					case 24:
						;
					case 25:
						;
					case 37:
						;
					case 39:
						;
					case 40:
						if(e[3] > s) { n["!ref"] = ct(l);
							f[i] = n;
							n = a.dense ? [] : {};
							l = { s: { r: 0, c: 0 }, e: { r: 0, c: 0 } };
							s = e[3];
							i = "Sheet" + (s + 1);
							o.push(i) } if(c > 0 && e[0].r >= c) break; if(a.dense) { if(!n[e[0].r]) n[e[0].r] = [];
							n[e[0].r][e[0].c] = e[1] } else n[ot(e[0])] = e[1]; if(l.e.c < e[0].c) l.e.c = e[0].c; if(l.e.r < e[0].r) l.e.r = e[0].r; break;
					default:
						break; } }, a);
			n["!ref"] = ct(l);
			f[i] = n; return { SheetNames: o, Sheets: f } }

		function a(e) { var r = { s: { c: 0, r: 0 }, e: { c: 0, r: 0 } };
			r.s.c = e._R(2);
			r.s.r = e._R(2);
			r.e.c = e._R(2);
			r.e.r = e._R(2); if(r.s.c == 65535) r.s.c = r.e.c = r.s.r = r.e.r = 0; return r }

		function n(e, r, t) { var a = [{ c: 0, r: 0 }, { t: "n", v: 0 }, 0]; if(t.qpro && t.vers != 20768) { a[0].c = e._R(1);
				e.l++;
				a[0].r = e._R(2);
				e.l += 2 } else { a[2] = e._R(1);
				a[0].c = e._R(2);
				a[0].r = e._R(2) } return a }

		function i(e, r, t) { var a = e.l + r; var i = n(e, r, t);
			i[1].t = "s"; if(t.vers == 20768) { e.l++; var s = e._R(1);
				i[1].v = e._R(s, "utf8"); return i } if(t.qpro) e.l++;
			i[1].v = e._R(a - e.l, "cstr"); return i }

		function s(e, r, t) { var a = n(e, r, t);
			a[1].v = e._R(2, "i"); return a }

		function f(e, r, t) { var a = n(e, r, t);
			a[1].v = e._R(8, "f"); return a }

		function o(e, r, t) { var a = e.l + r; var i = n(e, r, t);
			i[1].v = e._R(8, "f"); if(t.qpro) e.l = a;
			else { var s = e._R(2);
				e.l += s } return i }

		function l(e) { var r = [{ c: 0, r: 0 }, { t: "n", v: 0 }, 0];
			r[0].r = e._R(2);
			r[3] = e[e.l++];
			r[0].c = e[e.l++]; return r }

		function c(e, r) { var t = l(e, r);
			t[1].t = "s";
			t[1].v = e._R(r - 4, "cstr"); return t }

		function h(e, r) { var t = l(e, r);
			t[1].v = e._R(2); var a = t[1].v >> 1; if(t[1].v & 1) { switch(a & 7) {
					case 1:
						a = (a >> 3) * 500; break;
					case 2:
						a = (a >> 3) / 20; break;
					case 4:
						a = (a >> 3) / 2e3; break;
					case 6:
						a = (a >> 3) / 16; break;
					case 7:
						a = (a >> 3) / 64; break;
					default:
						throw "unknown NUMBER_18 encoding " + (a & 7); } } t[1].v = a; return t }

		function u(e, r) { var t = l(e, r); var a = e._R(4); var n = e._R(4); var i = e._R(2); if(i == 65535) { t[1].v = 0; return t } var s = i & 32768;
			i = (i & 32767) - 16446;
			t[1].v = (s * 2 - 1) * ((i > 0 ? n << i : n >>> -i) + (i > -32 ? a << i + 32 : a >>> -(i + 32))); return t }

		function d(e, r) { var t = u(e, 14);
			e.l += r - 14; return t }

		function p(e, r) { var t = l(e, r); var a = e._R(4);
			t[1].v = a >> 6; return t }

		function v(e, r) { var t = l(e, r); var a = e._R(8, "f");
			t[1].v = a; return t }

		function m(e, r) { var t = v(e, 14);
			e.l += r - 10; return t } var C = { 0: { n: "BOF", f: Ln }, 1: { n: "EOF" }, 2: { n: "CALCMODE" }, 3: { n: "CALCORDER" }, 4: { n: "SPLIT" }, 5: { n: "SYNC" }, 6: { n: "RANGE", f: a }, 7: { n: "WINDOW1" }, 8: { n: "COLW1" }, 9: { n: "WINTWO" }, 10: { n: "COLW2" }, 11: { n: "NAME" }, 12: { n: "BLANK" }, 13: { n: "INTEGER", f: s }, 14: { n: "NUMBER", f: f }, 15: { n: "LABEL", f: i }, 16: { n: "FORMULA", f: o }, 24: { n: "TABLE" }, 25: { n: "ORANGE" }, 26: { n: "PRANGE" }, 27: { n: "SRANGE" }, 28: { n: "FRANGE" }, 29: { n: "KRANGE1" }, 32: { n: "HRANGE" }, 35: { n: "KRANGE2" }, 36: { n: "PROTEC" }, 37: { n: "FOOTER" }, 38: { n: "HEADER" }, 39: { n: "SETUP" }, 40: { n: "MARGINS" }, 41: { n: "LABELFMT" }, 42: { n: "TITLES" }, 43: { n: "SHEETJS" }, 45: { n: "GRAPH" }, 46: { n: "NGRAPH" }, 47: { n: "CALCCOUNT" }, 48: { n: "UNFORMATTED" }, 49: { n: "CURSORW12" }, 50: { n: "WINDOW" }, 51: { n: "STRING", f: i }, 55: { n: "PASSWORD" }, 56: { n: "LOCKED" }, 60: { n: "QUERY" }, 61: { n: "QUERYNAME" }, 62: { n: "PRINT" }, 63: { n: "PRINTNAME" }, 64: { n: "GRAPH2" }, 65: { n: "GRAPHNAME" }, 66: { n: "ZOOM" }, 67: { n: "SYMSPLIT" }, 68: { n: "NSROWS" }, 69: { n: "NSCOLS" }, 70: { n: "RULER" }, 71: { n: "NNAME" }, 72: { n: "ACOMM" }, 73: { n: "AMACRO" }, 74: { n: "PARSE" }, 255: { n: "", f: Wr } }; var w = { 0: { n: "BOF" }, 1: { n: "EOF" }, 3: { n: "??" }, 4: { n: "??" }, 5: { n: "??" }, 6: { n: "??" }, 7: { n: "??" }, 9: { n: "??" }, 10: { n: "??" }, 11: { n: "??" }, 12: { n: "??" }, 14: { n: "??" }, 15: { n: "??" }, 16: { n: "??" }, 17: { n: "??" }, 18: { n: "??" }, 19: { n: "??" }, 21: { n: "??" }, 22: { n: "LABEL16", f: c }, 23: { n: "NUMBER17", f: u }, 24: { n: "NUMBER18", f: h }, 25: { n: "FORMULA19", f: d }, 26: { n: "??" }, 27: { n: "??" }, 28: { n: "??" }, 29: { n: "??" }, 30: { n: "??" }, 31: { n: "??" }, 33: { n: "??" }, 37: { n: "NUMBER25", f: p }, 39: { n: "NUMBER27", f: v }, 40: { n: "FORMULA28", f: m }, 255: { n: "", f: Wr } }; return { to_workbook: r } }();
	var af = function Qg() { var e = Ge("t"),
			r = Ge("rPr"),
			t = /<(?:\w+:)?r>/g,
			a = /<\/(?:\w+:)?r>/,
			n = /\r\n/g; var i = function o(e, r, t) { var a = {},
				n = 65001,
				i = ""; var f = e.match(ke),
				o = 0; if(f)
				for(; o != f.length; ++o) { var l = _e(f[o]); switch(l[0].replace(/\w*:/g, "")) {
						case "<condense":
							break;
						case "<extend":
							break;
						case "<shadow":
							if(!l.val) break;
						case "<shadow>":
							;
						case "<shadow/>":
							a.shadow = 1; break;
						case "</shadow>":
							break;
						case "<charset":
							if(l.val == "1") break;
							n = s[parseInt(l.val, 10)]; break;
						case "<outline":
							if(!l.val) break;
						case "<outline>":
							;
						case "<outline/>":
							a.outline = 1; break;
						case "</outline>":
							break;
						case "<rFont":
							a.name = l.val; break;
						case "<sz":
							a.sz = l.val; break;
						case "<strike":
							if(!l.val) break;
						case "<strike>":
							;
						case "<strike/>":
							a.strike = 1; break;
						case "</strike>":
							break;
						case "<u":
							if(!l.val) break; switch(l.val) {
								case "double":
									a.uval = "double"; break;
								case "singleAccounting":
									a.uval = "single-accounting"; break;
								case "doubleAccounting":
									a.uval = "double-accounting"; break; };
						case "<u>":
							;
						case "<u/>":
							a.u = 1; break;
						case "</u>":
							break;
						case "<b":
							if(l.val == "0") break;
						case "<b>":
							;
						case "<b/>":
							a.b = 1; break;
						case "</b>":
							break;
						case "<i":
							if(l.val == "0") break;
						case "<i>":
							;
						case "<i/>":
							a.i = 1; break;
						case "</i>":
							break;
						case "<color":
							if(l.rgb) a.color = l.rgb.slice(2, 8); break;
						case "<family":
							a.family = l.val; break;
						case "<vertAlign":
							i = l.val; break;
						case "<scheme":
							break;
						default:
							if(l[0].charCodeAt(1) !== 47) throw "Unrecognized rich format " + l[0]; } }
			var c = []; if(a.u) c.push("text-decoration: underline;"); if(a.uval) c.push("text-underline-style:" + a.uval + ";"); if(a.sz) c.push("font-size:" + a.sz + "pt;"); if(a.outline) c.push("text-effect: outline;"); if(a.shadow) c.push("text-shadow: auto;");
			r.push('<span style="' + c.join("") + '">'); if(a.b) { r.push("<b>");
				t.push("</b>") } if(a.i) { r.push("<i>");
				t.push("</i>") } if(a.strike) { r.push("<s>");
				t.push("</s>") } if(i == "superscript") i = "sup";
			else if(i == "subscript") i = "sub"; if(i != "") { r.push("<" + i + ">");
				t.push("</" + i + ">") } t.push("</span>"); return n };

		function f(t) { var a = [
				[], "", []
			]; var s = t.match(e); if(!s) return "";
			a[1] = s[1]; var f = t.match(r); if(f) i(f[1], a[0], a[2]); return a[0].join("") + a[1].replace(n, "<br/>") + a[2].join("") } return function l(e) { return e.replace(t, "").split(a).map(f).join("") } }();
	var nf = /<(?:\w+:)?t[^>]*>([^<]*)<\/(?:\w+:)?t>/g,
		sf = /<(?:\w+:)?r>/;
	var ff = /<(?:\w+:)?rPh.*?>([\s\S]*?)<\/(?:\w+:)?rPh>/g;

	function of (e, r) { var t = r ? r.cellHTML : true; var a = {}; if(!e) return null; if(e.match(/^\s*<(?:\w+:)?t[^>]*>/)) { a.t = ye(He(e.slice(e.indexOf(">") + 1).split(/<\/(?:\w+:)?t>/)[0] || ""));
			a.r = He(e); if(t) a.h = Pe(a.t) } else if(e.match(sf)) { a.r = He(e);
			a.t = ye(He((e.replace(ff, "").match(nf) || []).join("").replace(ke, ""))); if(t) a.h = af(a.r) } return a }
	var lf = /<(?:\w+:)?sst([^>]*)>([\s\S]*)<\/(?:\w+:)?sst>/;
	var cf = /<(?:\w+:)?(?:si|sstItem)>/g;
	var hf = /<\/(?:\w+:)?(?:si|sstItem)>/;

	function uf(e, r) { var t = [],
			a = ""; if(!e) return t; var n = e.match(lf); if(n) { a = n[2].replace(cf, "").split(hf); for(var i = 0; i != a.length; ++i) { var s = of (a[i].trim(), r); if(s != null) t[t.length] = s } n = _e(n[1]);
			t.Count = n.count;
			t.Unique = n.uniqueCount } return t } xa.SST = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/sharedStrings";
	var df = /^\s|\s$|[\t\n\r]/;

	function pf(e, r) { if(!r.bookSST) return ""; var t = [we];
		t[t.length] = er("sst", null, { xmlns: ar.main[0], count: e.Count, uniqueCount: e.Unique }); for(var a = 0; a != e.length; ++a) { if(e[a] == null) continue; var n = e[a]; var i = "<si>"; if(n.r) i += n.r;
			else { i += "<t"; if(!n.t) n.t = ""; if(n.t.match(df)) i += ' xml:space="preserve"';
				i += ">" + De(n.t) + "</t>" } i += "</si>";
			t[t.length] = i } if(t.length > 2) { t[t.length] = "</sst>";
			t[1] = t[1].replace("/>", ">") } return t.join("") }

	function vf(e) { return [e._R(4), e._R(4)] }

	function gf(e, r) { var t = []; var a = false;
		zr(e, function n(e, i, s) { switch(s) {
				case 159:
					t.Count = e[0];
					t.Unique = e[1]; break;
				case 19:
					t.push(e); break;
				case 160:
					return true;
				case 35:
					a = true; break;
				case 36:
					a = false; break;
				default:
					if(i.indexOf("Begin") > 0) {} else if(i.indexOf("End") > 0) {} if(!a || r.WTF) throw new Error("Unexpected record " + s + " " + i); } }); return t }

	function mf(e, r) { if(!r) r = Vr(8);
		r._W(4, e.Count);
		r._W(4, e.Unique); return r }
	var bf = St;

	function Cf(e) { var r = Xr();
		Gr(r, "BrtBeginSst", mf(e)); for(var t = 0; t < e.length; ++t) Gr(r, "BrtSSTItem", bf(e[t]));
		Gr(r, "BrtEndSst"); return r.end() }

	function wf(e) { if(typeof cptable !== "undefined") return cptable.utils.encode(a, e); var r = [],
			t = e.split(""); for(var n = 0; n < t.length; ++n) r[n] = t[n].charCodeAt(0); return r }

	function Ef(e, r) { var t = {};
		t.Major = e._R(2);
		t.Minor = e._R(2); if(r >= 4) e.l += r - 4; return t }

	function kf(e) { var r = {};
		r.id = e._R(0, "lpp4");
		r.R = Ef(e, 4);
		r.U = Ef(e, 4);
		r.W = Ef(e, 4); return r }

	function Sf(e) { var r = e._R(4); var t = e.l + r - 4; var a = {}; var n = e._R(4); var i = []; while(n-- > 0) i.push({ t: e._R(4), v: e._R(0, "lpp4") });
		a.name = e._R(0, "lpp4");
		a.comps = i; if(e.l != t) throw new Error("Bad DataSpaceMapEntry: " + e.l + " != " + t); return a }

	function Af(e) { var r = [];
		e.l += 4; var t = e._R(4); while(t-- > 0) r.push(Sf(e)); return r }

	function _f(e) { var r = [];
		e.l += 4; var t = e._R(4); while(t-- > 0) r.push(e._R(0, "lpp4")); return r }

	function Bf(e) { var r = {};
		e._R(4);
		e.l += 4;
		r.id = e._R(0, "lpp4");
		r.name = e._R(0, "lpp4");
		r.R = Ef(e, 4);
		r.U = Ef(e, 4);
		r.W = Ef(e, 4); return r }

	function Tf(e) { var r = Bf(e);
		r.ename = e._R(0, "8lpp4");
		r.blksz = e._R(4);
		r.cmode = e._R(4); if(e._R(4) != 4) throw new Error("Bad !Primary record"); return r }

	function xf(e, r) { var t = e.l + r; var a = {};
		a.Flags = e._R(4) & 63;
		e.l += 4;
		a.AlgID = e._R(4); var n = false; switch(a.AlgID) {
			case 26126:
				;
			case 26127:
				;
			case 26128:
				n = a.Flags == 36; break;
			case 26625:
				n = a.Flags == 4; break;
			case 0:
				n = a.Flags == 16 || a.Flags == 4 || a.Flags == 36; break;
			default:
				throw "Unrecognized encryption algorithm: " + a.AlgID; } if(!n) throw new Error("Encryption Flags/AlgID mismatch");
		a.AlgIDHash = e._R(4);
		a.KeySize = e._R(4);
		a.ProviderType = e._R(4);
		e.l += 8;
		a.CSPName = e._R(t - e.l >> 1, "utf16le");
		e.l = t; return a }

	function yf(e, r) { var t = {},
			a = e.l + r;
		e.l += 4;
		t.Salt = e.slice(e.l, e.l + 16);
		e.l += 16;
		t.Verifier = e.slice(e.l, e.l + 16);
		e.l += 16;
		e._R(4);
		t.VerifierHash = e.slice(e.l, a);
		e.l = a; return t }

	function If(e) { var r = Ef(e); switch(r.Minor) {
			case 2:
				return [r.Minor, Rf(e, r)];
			case 3:
				return [r.Minor, Df(e, r)];
			case 4:
				return [r.Minor, Of(e, r)]; } throw new Error("ECMA-376 Encrypted file unrecognized Version: " + r.Minor) }

	function Rf(e) { var r = e._R(4); if((r & 63) != 36) throw new Error("EncryptionInfo mismatch"); var t = e._R(4); var a = xf(e, t); var n = yf(e, e.length - e.l); return { t: "Std", h: a, v: n } }

	function Df() { throw new Error("File is password-protected: ECMA-376 Extensible") }

	function Of(e) { var r = ["saltSize", "blockSize", "keyBits", "hashSize", "cipherAlgorithm", "cipherChaining", "hashAlgorithm", "saltValue"];
		e.l += 4; var t = e._R(e.length - e.l, "utf8"); var a = {};
		t.replace(ke, function n(e) { var t = _e(e); switch(Be(t[0])) {
				case "<?xml":
					break;
				case "<encryption":
					;
				case "</encryption>":
					break;
				case "<keyData":
					r.forEach(function(e) { a[e] = t[e] }); break;
				case "<dataIntegrity":
					a.encryptedHmacKey = t.encryptedHmacKey;
					a.encryptedHmacValue = t.encryptedHmacValue; break;
				case "<keyEncryptors>":
					;
				case "<keyEncryptors":
					a.encs = []; break;
				case "</keyEncryptors>":
					break;
				case "<keyEncryptor":
					a.uri = t.uri; break;
				case "</keyEncryptor>":
					break;
				case "<encryptedKey":
					a.encs.push(t); break;
				default:
					throw t[0]; } }); return a }

	function Ff(e, r) { var t = {}; var a = t.EncryptionVersionInfo = Ef(e, 4);
		r -= 4; if(a.Minor != 2) throw new Error("unrecognized minor version code: " + a.Minor); if(a.Major > 4 || a.Major < 2) throw new Error("unrecognized major version code: " + a.Major);
		t.Flags = e._R(4);
		r -= 4; var n = e._R(4);
		r -= 4;
		t.EncryptionHeader = xf(e, n);
		r -= n;
		t.EncryptionVerifier = yf(e, r); return t }

	function Pf(e) { var r = {}; var t = r.EncryptionVersionInfo = Ef(e, 4); if(t.Major != 1 || t.Minor != 1) throw "unrecognized version code " + t.Major + " : " + t.Minor;
		r.Salt = e._R(16);
		r.EncryptedVerifier = e._R(16);
		r.EncryptedVerifierHash = e._R(16); return r }

	function Nf(e) { var r = 0,
			t; var a = wf(e); var n = a.length + 1,
			i, s; var f, o, l;
		t = w(n);
		t[0] = a.length; for(i = 1; i != n; ++i) t[i] = a[i - 1]; for(i = n - 1; i >= 0; --i) { s = t[i];
			f = (r & 16384) === 0 ? 0 : 1;
			o = r << 1 & 32767;
			l = f | o;
			r = l ^ s } return r ^ 52811 }
	var Lf = function() { var e = [187, 255, 255, 186, 255, 255, 185, 128, 0, 190, 15, 0, 191, 15, 0]; var r = [57840, 7439, 52380, 33984, 4364, 3600, 61902, 12606, 6258, 57657, 54287, 34041, 10252, 43370, 20163]; var t = [44796, 19929, 39858, 10053, 20106, 40212, 10761, 31585, 63170, 64933, 60267, 50935, 40399, 11199, 17763, 35526, 1453, 2906, 5812, 11624, 23248, 885, 1770, 3540, 7080, 14160, 28320, 56640, 55369, 41139, 20807, 41614, 21821, 43642, 17621, 28485, 56970, 44341, 19019, 38038, 14605, 29210, 60195, 50791, 40175, 10751, 21502, 43004, 24537, 18387, 36774, 3949, 7898, 15796, 31592, 63184, 47201, 24803, 49606, 37805, 14203, 28406, 56812, 17824, 35648, 1697, 3394, 6788, 13576, 27152, 43601, 17539, 35078, 557, 1114, 2228, 4456, 30388, 60776, 51953, 34243, 7079, 14158, 28316, 14128, 28256, 56512, 43425, 17251, 34502, 7597, 13105, 26210, 52420, 35241, 883, 1766, 3532, 4129, 8258, 16516, 33032, 4657, 9314, 18628]; var a = function(e) { return(e / 2 | e * 128) & 255 }; var n = function(e, r) { return a(e ^ r) }; var i = function(e) { var a = r[e.length - 1]; var n = 104; for(var i = e.length - 1; i >= 0; --i) { var s = e[i]; for(var f = 0; f != 7; ++f) { if(s & 64) a ^= t[n];
					s *= 2;--n } } return a }; return function(r) { var t = wf(r); var a = i(t); var s = t.length; var f = w(16); for(var o = 0; o != 16; ++o) f[o] = 0; var l, c, h; if((s & 1) === 1) { l = a >> 8;
				f[s] = n(e[0], l);--s;
				l = a & 255;
				c = t[t.length - 1];
				f[s] = n(c, l) } while(s > 0) {--s;
				l = a >> 8;
				f[s] = n(t[s], l);--s;
				l = a & 255;
				f[s] = n(t[s], l) } s = 15;
			h = 15 - t.length; while(h > 0) { l = a >> 8;
				f[s] = n(e[h], l);--s;--h;
				l = a & 255;
				f[s] = n(t[s], l);--s;--h } return f } }();
	var Mf = function(e, r, t, a, n) { if(!n) n = r; if(!a) a = Lf(e); var i, s; for(i = 0; i != r.length; ++i) { s = r[i];
			s ^= a[t];
			s = (s >> 5 | s << 3) & 255;
			n[i] = s;++t } return [n, t, a] };
	var Uf = function(e) { var r = 0,
			t = Lf(e); return function(e) { var a = Mf("", e, r, t);
			r = a[1]; return a[0] } };

	function Hf(e, r, t, a) { var n = { key: Ln(e), verificationBytes: Ln(e) }; if(t.password) n.verifier = Nf(t.password);
		a.valid = n.verificationBytes === n.verifier; if(a.valid) a.insitu = Uf(t.password); return n }

	function Wf(e, r, t) { var a = t || {};
		a.Info = e._R(2);
		e.l -= 2; if(a.Info === 1) a.Data = Pf(e, r);
		else a.Data = Ff(e, r); return a }

	function Vf(e, r, t) { var a = { Type: t.biff >= 8 ? e._R(2) : 0 }; if(a.Type) Wf(e, r - 2, a);
		else Hf(e, t.biff >= 8 ? r : r - 2, t, a); return a }
	var zf = function() {
		function e(e, t) { switch(t.type) {
				case "base64":
					return r(b.decode(e), t);
				case "binary":
					return r(e, t);
				case "buffer":
					return r(e.toString("binary"), t);
				case "array":
					return r(ae(e), t); } throw new Error("Unrecognized type " + t.type) }

		function r(e, r) { var t = r || {}; var a = t.dense ? [] : {}; var n = { s: { c: 0, r: 0 }, e: { c: 0, r: 0 } }; if(!e.match(/\\trowd/)) throw new Error("RTF missing table");
			a["!ref"] = ct(n); return a }

		function t(r, t) { return pt(e(r, t), t) }

		function a(e) { var r = ["{\\rtf1\\ansi"]; var t = ht(e["!ref"]),
				a; var n = Array.isArray(e); for(var i = t.s.r; i <= t.e.r; ++i) { r.push("\\trowd\\trautofit1"); for(var s = t.s.c; s <= t.e.c; ++s) r.push("\\cellx" + (s + 1));
				r.push("\\pard\\intbl"); for(s = t.s.c; s <= t.e.c; ++s) { var f = ot({ r: i, c: s });
					a = n ? (e[i] || [])[s] : e[f]; if(!a || a.v == null && (!a.f || a.F)) continue;
					r.push(" " + (a.w || (dt(a), a.w)));
					r.push("\\cell") } r.push("\\pard\\intbl\\row") } return r.join("") + "}" } return { to_workbook: t, to_sheet: e, from_sheet: a } }();

	function Xf(e) { var r = e.slice(e[0] === "#" ? 1 : 0).slice(0, 6); return [parseInt(r.slice(0, 2), 16), parseInt(r.slice(2, 4), 16), parseInt(r.slice(4, 6), 16)] }

	function Gf(e) { for(var r = 0, t = 1; r != 3; ++r) t = t * 256 + (e[r] > 255 ? 255 : e[r] < 0 ? 0 : e[r]); return t.toString(16).toUpperCase().slice(1) }

	function jf(e) { var r = e[0] / 255,
			t = e[1] / 255,
			a = e[2] / 255; var n = Math.max(r, t, a),
			i = Math.min(r, t, a),
			s = n - i; if(s === 0) return [0, 0, r]; var f = 0,
			o = 0,
			l = n + i;
		o = s / (l > 1 ? 2 - l : l); switch(n) {
			case r:
				f = ((t - a) / s + 6) % 6; break;
			case t:
				f = (a - r) / s + 2; break;
			case a:
				f = (r - t) / s + 4; break; } return [f / 6, o, l / 2] }

	function Kf(e) { var r = e[0],
			t = e[1],
			a = e[2]; var n = t * 2 * (a < .5 ? a : 1 - a),
			i = a - n / 2; var s = [i, i, i],
			f = 6 * r; var o; if(t !== 0) switch(f | 0) {
			case 0:
				;
			case 6:
				o = n * f;
				s[0] += n;
				s[1] += o; break;
			case 1:
				o = n * (2 - f);
				s[0] += o;
				s[1] += n; break;
			case 2:
				o = n * (f - 2);
				s[1] += n;
				s[2] += o; break;
			case 3:
				o = n * (4 - f);
				s[1] += o;
				s[2] += n; break;
			case 4:
				o = n * (f - 4);
				s[2] += n;
				s[0] += o; break;
			case 5:
				o = n * (6 - f);
				s[2] += o;
				s[0] += n; break; }
		for(var l = 0; l != 3; ++l) s[l] = Math.round(s[l] * 255); return s }

	function Yf(e, r) { if(r === 0) return e; var t = jf(Xf(e)); if(r < 0) t[2] = t[2] * (1 + r);
		else t[2] = 1 - (1 - t[2]) * (1 - r); return Gf(Kf(t)) }
	var $f = 6,
		Zf = 15,
		Qf = 1,
		Jf = $f;

	function qf(e) { return Math.floor((e + Math.round(128 / Jf) / 256) * Jf) }

	function eo(e) { return Math.floor((e - 5) / Jf * 100 + .5) / 100 }

	function ro(e) { return Math.round((e * Jf + 5) / Jf * 256) / 256 }

	function to(e) { return ro(eo(qf(e))) }

	function ao(e) { var r = Math.abs(e - to(e)),
			t = Jf; if(r > .005)
			for(Jf = Qf; Jf < Zf; ++Jf)
				if(Math.abs(e - to(e)) <= r) { r = Math.abs(e - to(e));
					t = Jf }
		Jf = t }

	function no(e) { if(e.width) { e.wpx = qf(e.width);
			e.wch = eo(e.wpx);
			e.MDW = Jf } else if(e.wpx) { e.wch = eo(e.wpx);
			e.width = ro(e.wch);
			e.MDW = Jf } else if(typeof e.wch == "number") { e.width = ro(e.wch);
			e.wpx = qf(e.width);
			e.MDW = Jf } if(e.customWidth) delete e.customWidth }
	var io = 96,
		so = io;

	function fo(e) { return e * 96 / so }

	function oo(e) { return e * so / 96 }
	var lo = { None: "none", Solid: "solid", Gray50: "mediumGray", Gray75: "darkGray", Gray25: "lightGray", HorzStripe: "darkHorizontal", VertStripe: "darkVertical", ReverseDiagStripe: "darkDown", DiagStripe: "darkUp", DiagCross: "darkGrid", ThickDiagCross: "darkTrellis", ThinHorzStripe: "lightHorizontal", ThinVertStripe: "lightVertical", ThinReverseDiagStripe: "lightDown", ThinHorzCross: "lightGrid" };

	function co(e, r, t, a) { r.Borders = []; var n = {}; var i = false;
		e[0].match(ke).forEach(function(e) { var t = _e(e); switch(t[0]) {
				case "<borders":
					;
				case "<borders>":
					;
				case "</borders>":
					break;
				case "<border":
					;
				case "<border>":
					;
				case "<border/>":
					n = {}; if(t.diagonalUp) { n.diagonalUp = t.diagonalUp } if(t.diagonalDown) { n.diagonalDown = t.diagonalDown } r.Borders.push(n); break;
				case "</border>":
					break;
				case "<left/>":
					break;
				case "<left":
					;
				case "<left>":
					break;
				case "</left>":
					break;
				case "<right/>":
					break;
				case "<right":
					;
				case "<right>":
					break;
				case "</right>":
					break;
				case "<top/>":
					break;
				case "<top":
					;
				case "<top>":
					break;
				case "</top>":
					break;
				case "<bottom/>":
					break;
				case "<bottom":
					;
				case "<bottom>":
					break;
				case "</bottom>":
					break;
				case "<diagonal":
					;
				case "<diagonal>":
					;
				case "<diagonal/>":
					break;
				case "</diagonal>":
					break;
				case "<horizontal":
					;
				case "<horizontal>":
					;
				case "<horizontal/>":
					break;
				case "</horizontal>":
					break;
				case "<vertical":
					;
				case "<vertical>":
					;
				case "<vertical/>":
					break;
				case "</vertical>":
					break;
				case "<start":
					;
				case "<start>":
					;
				case "<start/>":
					break;
				case "</start>":
					break;
				case "<end":
					;
				case "<end>":
					;
				case "<end/>":
					break;
				case "</end>":
					break;
				case "<color":
					;
				case "<color>":
					break;
				case "<color/>":
					;
				case "</color>":
					break;
				case "<extLst":
					;
				case "<extLst>":
					;
				case "</extLst>":
					break;
				case "<ext":
					i = true; break;
				case "</ext>":
					i = false; break;
				default:
					if(a && a.WTF) { if(!i) throw new Error("unrecognized " + t[0] + " in borders") }; } }) }

	function ho(e, r, t, a) { r.Fills = []; var n = {}; var i = false;
		e[0].match(ke).forEach(function(e) { var t = _e(e); switch(t[0]) {
				case "<fills":
					;
				case "<fills>":
					;
				case "</fills>":
					break;
				case "<fill>":
					;
				case "<fill":
					;
				case "<fill/>":
					n = {};
					r.Fills.push(n); break;
				case "</fill>":
					break;
				case "<gradientFill>":
					break;
				case "<gradientFill":
					;
				case "</gradientFill>":
					r.Fills.push(n);
					n = {}; break;
				case "<patternFill":
					;
				case "<patternFill>":
					if(t.patternType) n.patternType = t.patternType; break;
				case "<patternFill/>":
					;
				case "</patternFill>":
					break;
				case "<bgColor":
					if(!n.bgColor) n.bgColor = {}; if(t.indexed) n.bgColor.indexed = parseInt(t.indexed, 10); if(t.theme) n.bgColor.theme = parseInt(t.theme, 10); if(t.tint) n.bgColor.tint = parseFloat(t.tint); if(t.rgb) n.bgColor.rgb = t.rgb.slice(-6); break;
				case "<bgColor/>":
					;
				case "</bgColor>":
					break;
				case "<fgColor":
					if(!n.fgColor) n.fgColor = {}; if(t.theme) n.fgColor.theme = parseInt(t.theme, 10); if(t.tint) n.fgColor.tint = parseFloat(t.tint); if(t.rgb) n.fgColor.rgb = t.rgb.slice(-6); break;
				case "<fgColor/>":
					;
				case "</fgColor>":
					break;
				case "<stop":
					;
				case "<stop/>":
					break;
				case "</stop>":
					break;
				case "<color":
					;
				case "<color/>":
					break;
				case "</color>":
					break;
				case "<extLst":
					;
				case "<extLst>":
					;
				case "</extLst>":
					break;
				case "<ext":
					i = true; break;
				case "</ext>":
					i = false; break;
				default:
					if(a && a.WTF) { if(!i) throw new Error("unrecognized " + t[0] + " in fills") }; } }) }

	function uo(e, r, t, a) { r.Fonts = []; var n = {}; var i = false;
		e[0].match(ke).forEach(function(e) { var f = _e(e); switch(f[0]) {
				case "<fonts":
					;
				case "<fonts>":
					;
				case "</fonts>":
					break;
				case "<font":
					;
				case "<font>":
					break;
				case "</font>":
					;
				case "<font/>":
					r.Fonts.push(n);
					n = {}; break;
				case "<name":
					if(f.val) n.name = f.val; break;
				case "<name/>":
					;
				case "</name>":
					break;
				case "<b":
					n.bold = f.val ? Ue(f.val) : 1; break;
				case "<b/>":
					n.bold = 1; break;
				case "<i":
					n.italic = f.val ? Ue(f.val) : 1; break;
				case "<i/>":
					n.italic = 1; break;
				case "<u":
					switch(f.val) {
						case "none":
							n.underline = 0; break;
						case "single":
							n.underline = 1; break;
						case "double":
							n.underline = 2; break;
						case "singleAccounting":
							n.underline = 33; break;
						case "doubleAccounting":
							n.underline = 34; break; } break;
				case "<u/>":
					n.underline = 1; break;
				case "<strike":
					n.strike = f.val ? Ue(f.val) : 1; break;
				case "<strike/>":
					n.strike = 1; break;
				case "<outline":
					n.outline = f.val ? Ue(f.val) : 1; break;
				case "<outline/>":
					n.outline = 1; break;
				case "<shadow":
					n.shadow = f.val ? Ue(f.val) : 1; break;
				case "<shadow/>":
					n.shadow = 1; break;
				case "<condense":
					n.condense = f.val ? Ue(f.val) : 1; break;
				case "<condense/>":
					n.condense = 1; break;
				case "<extend":
					n.extend = f.val ? Ue(f.val) : 1; break;
				case "<extend/>":
					n.extend = 1; break;
				case "<sz":
					if(f.val) n.sz = +f.val; break;
				case "<sz/>":
					;
				case "</sz>":
					break;
				case "<vertAlign":
					if(f.val) n.vertAlign = f.val; break;
				case "<vertAlign/>":
					;
				case "</vertAlign>":
					break;
				case "<family":
					if(f.val) n.family = parseInt(f.val, 10); break;
				case "<family/>":
					;
				case "</family>":
					break;
				case "<scheme":
					if(f.val) n.scheme = f.val; break;
				case "<scheme/>":
					;
				case "</scheme>":
					break;
				case "<charset":
					if(f.val == "1") break;
					f.codepage = s[parseInt(f.val, 10)]; break;
				case "<color":
					if(!n.color) n.color = {}; if(f.auto) n.color.auto = Ue(f.auto); if(f.rgb) n.color.rgb = f.rgb.slice(-6);
					else if(f.indexed) { n.color.index = parseInt(f.indexed, 10); var o = Ca[n.color.index]; if(n.color.index == 81) o = Ca[1]; if(!o) throw new Error(e);
						n.color.rgb = o[0].toString(16) + o[1].toString(16) + o[2].toString(16) } else if(f.theme) { n.color.theme = parseInt(f.theme, 10); if(f.tint) n.color.tint = parseFloat(f.tint); if(f.theme && t.themeElements && t.themeElements.clrScheme) { n.color.rgb = Yf(t.themeElements.clrScheme[n.color.theme].rgb, n.color.tint || 0) } } break;
				case "<color/>":
					;
				case "</color>":
					break;
				case "<extLst":
					;
				case "<extLst>":
					;
				case "</extLst>":
					break;
				case "<ext":
					i = true; break;
				case "</ext>":
					i = false; break;
				default:
					if(a && a.WTF) { if(!i) throw new Error("unrecognized " + f[0] + " in fonts") }; } }) }

	function po(e, r, t) { r.NumberFmt = []; var a = z(y._table); for(var n = 0; n < a.length; ++n) r.NumberFmt[a[n]] = y._table[a[n]]; var i = e[0].match(ke); if(!i) return; for(n = 0; n < i.length; ++n) { var s = _e(i[n]); switch(s[0]) {
				case "<numFmts":
					;
				case "</numFmts>":
					;
				case "<numFmts/>":
					;
				case "<numFmts>":
					break;
				case "<numFmt":
					{ var f = ye(He(s.formatCode)),
							o = parseInt(s.numFmtId, 10);r.NumberFmt[o] = f; if(o > 0) { if(o > 392) { for(o = 392; o > 60; --o)
									if(r.NumberFmt[o] == null) break;
								r.NumberFmt[o] = f } y.load(f, o) } } break;
				case "</numFmt>":
					break;
				default:
					if(t.WTF) throw new Error("unrecognized " + s[0] + " in numFmts"); } } }

	function vo(e) { var r = ["<numFmts>"];
		[
			[5, 8],
			[23, 26],
			[41, 44],
			[50, 392]
		].forEach(function(t) { for(var a = t[0]; a <= t[1]; ++a)
				if(e[a] != null) r[r.length] = er("numFmt", null, { numFmtId: a, formatCode: De(e[a]) }) }); if(r.length === 1) return "";
		r[r.length] = "</numFmts>";
		r[0] = er("numFmts", null, { count: r.length - 2 }).replace("/>", ">"); return r.join("") }
	var go = ["numFmtId", "fillId", "fontId", "borderId", "xfId"];
	var mo = ["applyAlignment", "applyBorder", "applyFill", "applyFont", "applyNumberFormat", "applyProtection", "pivotButton", "quotePrefix"];

	function bo(e, r, t) { r.CellXf = []; var a; var n = false;
		e[0].match(ke).forEach(function(e) { var i = _e(e),
				s = 0; switch(i[0]) {
				case "<cellXfs":
					;
				case "<cellXfs>":
					;
				case "<cellXfs/>":
					;
				case "</cellXfs>":
					break;
				case "<xf":
					;
				case "<xf/>":
					a = i;
					delete a[0]; for(s = 0; s < go.length; ++s)
						if(a[go[s]]) a[go[s]] = parseInt(a[go[s]], 10); for(s = 0; s < mo.length; ++s)
						if(a[mo[s]]) a[mo[s]] = Ue(a[mo[s]]); if(a.numFmtId > 392) { for(s = 392; s > 60; --s)
							if(r.NumberFmt[a.numFmtId] == r.NumberFmt[s]) { a.numFmtId = s; break } } r.CellXf.push(a); break;
				case "</xf>":
					break;
				case "<alignment":
					;
				case "<alignment/>":
					var f = {}; if(i.vertical) f.vertical = i.vertical; if(i.horizontal) f.horizontal = i.horizontal; if(i.textRotation != null) f.textRotation = i.textRotation; if(i.indent) f.indent = i.indent; if(i.wrapText) f.wrapText = i.wrapText;
					a.alignment = f; break;
				case "</alignment>":
					break;
				case "<protection":
					;
				case "</protection>":
					;
				case "<protection/>":
					break;
				case "<extLst":
					;
				case "<extLst>":
					;
				case "</extLst>":
					break;
				case "<ext":
					n = true; break;
				case "</ext>":
					n = false; break;
				default:
					if(t && t.WTF) { if(!n) throw new Error("unrecognized " + i[0] + " in cellXfs") }; } }) }

	function Co(e) { var r = [];
		r[r.length] = er("cellXfs", null);
		e.forEach(function(e) { r[r.length] = er("xf", null, e) });
		r[r.length] = "</cellXfs>"; if(r.length === 2) return "";
		r[0] = er("cellXfs", null, { count: r.length - 2 }).replace("/>", ">"); return r.join("") }
	var wo = function Jg() { var e = /<numFmts([^>]*)>[\S\s]*?<\/numFmts>/; var r = /<cellXfs([^>]*)>[\S\s]*?<\/cellXfs>/; var t = /<fills([^>]*)>[\S\s]*?<\/fills>/; var a = /<fonts([^>]*)>[\S\s]*?<\/fonts>/; var n = /<borders([^>]*)>[\S\s]*?<\/borders>/; return function i(s, f, o) { var l = {}; if(!s) return l;
			s = s.replace(/<!--([\s\S]*?)-->/gm, "").replace(/<!DOCTYPE[^\[]*\[[^\]]*\]>/gm, ""); var c; if(c = s.match(e)) po(c, l, o); if(c = s.match(a)) uo(c, l, f, o); if(c = s.match(t)) ho(c, l, f, o); if(c = s.match(n)) co(c, l, f, o); if(c = s.match(r)) bo(c, l, o); return l } }();
	var Eo = er("styleSheet", null, { xmlns: ar.main[0], "xmlns:vt": ar.vt });
	xa.STY = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles";

	function ko(e, r) { var t = [we, Eo],
			a; if(e.SSF && (a = vo(e.SSF)) != null) t[t.length] = a;
		t[t.length] = '<fonts count="1"><font><sz val="12"/><color theme="1"/><name val="Calibri"/><family val="2"/><scheme val="minor"/></font></fonts>';
		t[t.length] = '<fills count="2"><fill><patternFill patternType="none"/></fill><fill><patternFill patternType="gray125"/></fill></fills>';
		t[t.length] = '<borders count="1"><border><left/><right/><top/><bottom/><diagonal/></border></borders>';
		t[t.length] = '<cellStyleXfs count="1"><xf numFmtId="0" fontId="0" fillId="0" borderId="0"/></cellStyleXfs>'; if(a = Co(r.cellXfs)) t[t.length] = a;
		t[t.length] = '<cellStyles count="1"><cellStyle name="Normal" xfId="0" builtinId="0"/></cellStyles>';
		t[t.length] = '<dxfs count="0"/>';
		t[t.length] = '<tableStyles count="0" defaultTableStyle="TableStyleMedium9" defaultPivotStyle="PivotStyleMedium4"/>'; if(t.length > 2) { t[t.length] = "</styleSheet>";
			t[1] = t[1].replace("/>", ">") } return t.join("") }

	function So(e, r) { var t = e._R(2); var a = bt(e, r - 2); return [t, a] }

	function Ao(e, r, t) { if(!t) t = Vr(6 + 4 * r.length);
		t._W(2, e);
		Ct(r, t); var a = t.length > t.l ? t.slice(0, t.l) : t; if(t.l == null) t.l = t.length; return a }

	function _o(e, r, t) { var a = {};
		a.sz = e._R(2) / 20; var n = Kt(e, 2, t); if(n.fCondense) a.condense = 1; if(n.fExtend) a.extend = 1; if(n.fShadow) a.shadow = 1; if(n.fOutline) a.outline = 1; if(n.fStrikeout) a.strike = 1; if(n.fItalic) a.italic = 1; var i = e._R(2); if(i === 700) a.bold = 1; switch(e._R(2)) {
			case 1:
				a.vertAlign = "superscript"; break;
			case 2:
				a.vertAlign = "subscript"; break; } var s = e._R(1); if(s != 0) a.underline = s; var f = e._R(1); if(f > 0) a.family = f; var o = e._R(1); if(o > 0) a.charset = o;
		e.l++;
		a.color = Gt(e, 8); switch(e._R(1)) {
			case 1:
				a.scheme = "major"; break;
			case 2:
				a.scheme = "minor"; break; } a.name = bt(e, r - 21); return a }

	function Bo(e, r) { if(!r) r = Vr(25 + 4 * 32);
		r._W(2, e.sz * 20);
		Yt(e, r);
		r._W(2, e.bold ? 700 : 400); var t = 0; if(e.vertAlign == "superscript") t = 1;
		else if(e.vertAlign == "subscript") t = 2;
		r._W(2, t);
		r._W(1, e.underline || 0);
		r._W(1, e.family || 0);
		r._W(1, e.charset || 0);
		r._W(1, 0);
		jt(e.color, r); var a = 0; if(e.scheme == "major") a = 1; if(e.scheme == "minor") a = 2;
		r._W(1, a);
		Ct(e.name, r); return r.length > r.l ? r.slice(0, r.l) : r }
	var To = ["none", "solid", "mediumGray", "darkGray", "lightGray", "darkHorizontal", "darkVertical", "darkDown", "darkUp", "darkGrid", "darkTrellis", "lightHorizontal", "lightVertical", "lightDown", "lightUp", "lightGrid", "lightTrellis", "gray125", "gray0625"];
	var xo = G(To);
	var yo = Wr;

	function Io(e, r) { if(!r) r = Vr(4 * 3 + 8 * 7 + 16 * 1); var t = xo[e.patternType]; if(t == null) t = 40;
		r._W(4, t); var a = 0; if(t != 40) { jt({ auto: 1 }, r);
			jt({ auto: 1 }, r); for(; a < 12; ++a) r._W(4, 0) } else { for(; a < 4; ++a) r._W(4, 0); for(; a < 12; ++a) r._W(4, 0) } return r.length > r.l ? r.slice(0, r.l) : r }

	function Ro(e, r) { var t = e.l + r; var a = e._R(2); var n = e._R(2);
		e.l = t; return { ixfe: a, numFmtId: n } }

	function Do(e, r, t) { if(!t) t = Vr(16);
		t._W(2, r || 0);
		t._W(2, e.numFmtId || 0);
		t._W(2, 0);
		t._W(2, 0);
		t._W(2, 0);
		t._W(1, 0);
		t._W(1, 0);
		t._W(1, 0);
		t._W(1, 0);
		t._W(1, 0);
		t._W(1, 0); return t }

	function Oo(e, r) {
		if(!r) r = Vr(10);
		r._W(1, 0);
		r._W(1, 0);
		r._W(4, 0);
		r._W(4, 0);
		return r;
	}
	var Fo = Wr;

	function Po(e, r) { if(!r) r = Vr(51);
		r._W(1, 0);
		Oo(null, r);
		Oo(null, r);
		Oo(null, r);
		Oo(null, r);
		Oo(null, r); return r.length > r.l ? r.slice(0, r.l) : r }

	function No(e, r) { if(!r) r = Vr(12 + 4 * 10);
		r._W(4, e.xfId);
		r._W(2, 1);
		r._W(1, +e.builtinId);
		r._W(1, 0);
		Rt(e.name || "", r); return r.length > r.l ? r.slice(0, r.l) : r }

	function Lo(e, r, t) { var a = Vr(4 + 256 * 2 * 4);
		a._W(4, e);
		Rt(r, a);
		Rt(t, a); return a.length > a.l ? a.slice(0, a.l) : a }

	function Mo(e, r, t) { var a = {};
		a.NumberFmt = []; for(var n in y._table) a.NumberFmt[n] = y._table[n];
		a.CellXf = [];
		a.Fonts = []; var i = []; var s = false;
		zr(e, function f(e, n, o) { switch(o) {
				case 44:
					a.NumberFmt[e[0]] = e[1];
					y.load(e[1], e[0]); break;
				case 43:
					a.Fonts.push(e); if(e.color.theme != null && r && r.themeElements && r.themeElements.clrScheme) { e.color.rgb = Yf(r.themeElements.clrScheme[e.color.theme].rgb, e.color.tint || 0) } break;
				case 1025:
					break;
				case 45:
					break;
				case 46:
					break;
				case 47:
					if(i[i.length - 1] == "BrtBeginCellXFs") { a.CellXf.push(e) } break;
				case 48:
					;
				case 507:
					;
				case 572:
					;
				case 475:
					break;
				case 1171:
					;
				case 2102:
					;
				case 1130:
					;
				case 512:
					;
				case 2095:
					;
				case 3072:
					break;
				case 35:
					s = true; break;
				case 36:
					s = false; break;
				case 37:
					i.push(n); break;
				case 38:
					i.pop(); break;
				default:
					if((n || "").indexOf("Begin") > 0) i.push(n);
					else if((n || "").indexOf("End") > 0) i.pop();
					else if(!s || t.WTF) throw new Error("Unexpected record " + o + " " + n); } }); return a }

	function Uo(e, r) { if(!r) return; var t = 0;
		[
			[5, 8],
			[23, 26],
			[41, 44],
			[50, 392]
		].forEach(function(e) { for(var a = e[0]; a <= e[1]; ++a)
				if(r[a] != null) ++t }); if(t == 0) return;
		Gr(e, "BrtBeginFmts", mt(t));
		[
			[5, 8],
			[23, 26],
			[41, 44],
			[50, 392]
		].forEach(function(t) { for(var a = t[0]; a <= t[1]; ++a)
				if(r[a] != null) Gr(e, "BrtFmt", Ao(a, r[a])) });
		Gr(e, "BrtEndFmts") }

	function Ho(e) { var r = 1; if(r == 0) return;
		Gr(e, "BrtBeginFonts", mt(r));
		Gr(e, "BrtFont", Bo({ sz: 12, color: { theme: 1 }, name: "Calibri", family: 2, scheme: "minor" }));
		Gr(e, "BrtEndFonts") }

	function Wo(e) { var r = 2; if(r == 0) return;
		Gr(e, "BrtBeginFills", mt(r));
		Gr(e, "BrtFill", Io({ patternType: "none" }));
		Gr(e, "BrtFill", Io({ patternType: "gray125" }));
		Gr(e, "BrtEndFills") }

	function Vo(e) { var r = 1; if(r == 0) return;
		Gr(e, "BrtBeginBorders", mt(r));
		Gr(e, "BrtBorder", Po({}));
		Gr(e, "BrtEndBorders") }

	function zo(e) { var r = 1;
		Gr(e, "BrtBeginCellStyleXFs", mt(r));
		Gr(e, "BrtXF", Do({ numFmtId: 0, fontId: 0, fillId: 0, borderId: 0 }, 65535));
		Gr(e, "BrtEndCellStyleXFs") }

	function Xo(e, r) { Gr(e, "BrtBeginCellXFs", mt(r.length));
		r.forEach(function(r) { Gr(e, "BrtXF", Do(r, 0)) });
		Gr(e, "BrtEndCellXFs") }

	function Go(e) { var r = 1;
		Gr(e, "BrtBeginStyles", mt(r));
		Gr(e, "BrtStyle", No({ xfId: 0, builtinId: 0, name: "Normal" }));
		Gr(e, "BrtEndStyles") }

	function jo(e) { var r = 0;
		Gr(e, "BrtBeginDXFs", mt(r));
		Gr(e, "BrtEndDXFs") }

	function Ko(e) { var r = 0;
		Gr(e, "BrtBeginTableStyles", Lo(r, "TableStyleMedium9", "PivotStyleMedium4"));
		Gr(e, "BrtEndTableStyles") }

	function Yo() { return }

	function $o(e, r) { var t = Xr();
		Gr(t, "BrtBeginStyleSheet");
		Uo(t, e.SSF);
		Ho(t, e);
		Wo(t, e);
		Vo(t, e);
		zo(t, e);
		Xo(t, r.cellXfs);
		Go(t, e);
		jo(t, e);
		Ko(t, e);
		Yo(t, e);
		Gr(t, "BrtEndStyleSheet"); return t.end() } xa.THEME = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme";

	function Zo(e, r, t) { r.themeElements.clrScheme = []; var a = {};
		(e[0].match(ke) || []).forEach(function(e) { var n = _e(e); switch(n[0]) {
				case "<a:clrScheme":
					;
				case "</a:clrScheme>":
					break;
				case "<a:srgbClr":
					a.rgb = n.val; break;
				case "<a:sysClr":
					a.rgb = n.lastClr; break;
				case "<a:dk1>":
					;
				case "</a:dk1>":
					;
				case "<a:lt1>":
					;
				case "</a:lt1>":
					;
				case "<a:dk2>":
					;
				case "</a:dk2>":
					;
				case "<a:lt2>":
					;
				case "</a:lt2>":
					;
				case "<a:accent1>":
					;
				case "</a:accent1>":
					;
				case "<a:accent2>":
					;
				case "</a:accent2>":
					;
				case "<a:accent3>":
					;
				case "</a:accent3>":
					;
				case "<a:accent4>":
					;
				case "</a:accent4>":
					;
				case "<a:accent5>":
					;
				case "</a:accent5>":
					;
				case "<a:accent6>":
					;
				case "</a:accent6>":
					;
				case "<a:hlink>":
					;
				case "</a:hlink>":
					;
				case "<a:folHlink>":
					;
				case "</a:folHlink>":
					if(n[0].charAt(1) === "/") { r.themeElements.clrScheme.push(a);
						a = {} } else { a.name = n[0].slice(3, n[0].length - 1) } break;
				default:
					if(t && t.WTF) throw new Error("Unrecognized " + n[0] + " in clrScheme"); } }) }

	function Qo() {}

	function Jo() {}
	var qo = /<a:clrScheme([^>]*)>[\s\S]*<\/a:clrScheme>/;
	var el = /<a:fontScheme([^>]*)>[\s\S]*<\/a:fontScheme>/;
	var rl = /<a:fmtScheme([^>]*)>[\s\S]*<\/a:fmtScheme>/;

	function tl(e, r, t) { r.themeElements = {}; var a;
		[
			["clrScheme", qo, Zo],
			["fontScheme", el, Qo],
			["fmtScheme", rl, Jo]
		].forEach(function(n) { if(!(a = e.match(n[1]))) throw new Error(n[0] + " not found in themeElements");
			n[2](a, r, t) }) }
	var al = /<a:themeElements([^>]*)>[\s\S]*<\/a:themeElements>/;

	function nl(e, r) { if(!e || e.length === 0) return nl(il()); var t; var a = {}; if(!(t = e.match(al))) throw new Error("themeElements not found in theme");
		tl(t[0], a, r); return a }

	function il(e, r) { if(r && r.themeXLSX) return r.themeXLSX; var t = [we];
		t[t.length] = '<a:theme xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" name="Office Theme">';
		t[t.length] = "<a:themeElements>";
		t[t.length] = '<a:clrScheme name="Office">';
		t[t.length] = '<a:dk1><a:sysClr val="windowText" lastClr="000000"/></a:dk1>';
		t[t.length] = '<a:lt1><a:sysClr val="window" lastClr="FFFFFF"/></a:lt1>';
		t[t.length] = '<a:dk2><a:srgbClr val="1F497D"/></a:dk2>';
		t[t.length] = '<a:lt2><a:srgbClr val="EEECE1"/></a:lt2>';
		t[t.length] = '<a:accent1><a:srgbClr val="4F81BD"/></a:accent1>';
		t[t.length] = '<a:accent2><a:srgbClr val="C0504D"/></a:accent2>';
		t[t.length] = '<a:accent3><a:srgbClr val="9BBB59"/></a:accent3>';
		t[t.length] = '<a:accent4><a:srgbClr val="8064A2"/></a:accent4>';
		t[t.length] = '<a:accent5><a:srgbClr val="4BACC6"/></a:accent5>';
		t[t.length] = '<a:accent6><a:srgbClr val="F79646"/></a:accent6>';
		t[t.length] = '<a:hlink><a:srgbClr val="0000FF"/></a:hlink>';
		t[t.length] = '<a:folHlink><a:srgbClr val="800080"/></a:folHlink>';
		t[t.length] = "</a:clrScheme>";
		t[t.length] = '<a:fontScheme name="Office">';
		t[t.length] = "<a:majorFont>";
		t[t.length] = '<a:latin typeface="Cambria"/>';
		t[t.length] = '<a:ea typeface=""/>';
		t[t.length] = '<a:cs typeface=""/>';
		t[t.length] = '<a:font script="Jpan" typeface="锛汲 锛般偞銈枫儍銈�"/>';
		t[t.length] = '<a:font script="Hang" typeface="毵戩潃 瓿犽敃"/>';
		t[t.length] = '<a:font script="Hans" typeface="瀹嬩綋"/>';
		t[t.length] = '<a:font script="Hant" typeface="鏂扮窗鏄庨珨"/>';
		t[t.length] = '<a:font script="Arab" typeface="Times New Roman"/>';
		t[t.length] = '<a:font script="Hebr" typeface="Times New Roman"/>';
		t[t.length] = '<a:font script="Thai" typeface="Tahoma"/>';
		t[t.length] = '<a:font script="Ethi" typeface="Nyala"/>';
		t[t.length] = '<a:font script="Beng" typeface="Vrinda"/>';
		t[t.length] = '<a:font script="Gujr" typeface="Shruti"/>';
		t[t.length] = '<a:font script="Khmr" typeface="MoolBoran"/>';
		t[t.length] = '<a:font script="Knda" typeface="Tunga"/>';
		t[t.length] = '<a:font script="Guru" typeface="Raavi"/>';
		t[t.length] = '<a:font script="Cans" typeface="Euphemia"/>';
		t[t.length] = '<a:font script="Cher" typeface="Plantagenet Cherokee"/>';
		t[t.length] = '<a:font script="Yiii" typeface="Microsoft Yi Baiti"/>';
		t[t.length] = '<a:font script="Tibt" typeface="Microsoft Himalaya"/>';
		t[t.length] = '<a:font script="Thaa" typeface="MV Boli"/>';
		t[t.length] = '<a:font script="Deva" typeface="Mangal"/>';
		t[t.length] = '<a:font script="Telu" typeface="Gautami"/>';
		t[t.length] = '<a:font script="Taml" typeface="Latha"/>';
		t[t.length] = '<a:font script="Syrc" typeface="Estrangelo Edessa"/>';
		t[t.length] = '<a:font script="Orya" typeface="Kalinga"/>';
		t[t.length] = '<a:font script="Mlym" typeface="Kartika"/>';
		t[t.length] = '<a:font script="Laoo" typeface="DokChampa"/>';
		t[t.length] = '<a:font script="Sinh" typeface="Iskoola Pota"/>';
		t[t.length] = '<a:font script="Mong" typeface="Mongolian Baiti"/>';
		t[t.length] = '<a:font script="Viet" typeface="Times New Roman"/>';
		t[t.length] = '<a:font script="Uigh" typeface="Microsoft Uighur"/>';
		t[t.length] = '<a:font script="Geor" typeface="Sylfaen"/>';
		t[t.length] = "</a:majorFont>";
		t[t.length] = "<a:minorFont>";
		t[t.length] = '<a:latin typeface="Calibri"/>';
		t[t.length] = '<a:ea typeface=""/>';
		t[t.length] = '<a:cs typeface=""/>';
		t[t.length] = '<a:font script="Jpan" typeface="锛汲 锛般偞銈枫儍銈�"/>';
		t[t.length] = '<a:font script="Hang" typeface="毵戩潃 瓿犽敃"/>';
		t[t.length] = '<a:font script="Hans" typeface="瀹嬩綋"/>';
		t[t.length] = '<a:font script="Hant" typeface="鏂扮窗鏄庨珨"/>';
		t[t.length] = '<a:font script="Arab" typeface="Arial"/>';
		t[t.length] = '<a:font script="Hebr" typeface="Arial"/>';
		t[t.length] = '<a:font script="Thai" typeface="Tahoma"/>';
		t[t.length] = '<a:font script="Ethi" typeface="Nyala"/>';
		t[t.length] = '<a:font script="Beng" typeface="Vrinda"/>';
		t[t.length] = '<a:font script="Gujr" typeface="Shruti"/>';
		t[t.length] = '<a:font script="Khmr" typeface="DaunPenh"/>';
		t[t.length] = '<a:font script="Knda" typeface="Tunga"/>';
		t[t.length] = '<a:font script="Guru" typeface="Raavi"/>';
		t[t.length] = '<a:font script="Cans" typeface="Euphemia"/>';
		t[t.length] = '<a:font script="Cher" typeface="Plantagenet Cherokee"/>';
		t[t.length] = '<a:font script="Yiii" typeface="Microsoft Yi Baiti"/>';
		t[t.length] = '<a:font script="Tibt" typeface="Microsoft Himalaya"/>';
		t[t.length] = '<a:font script="Thaa" typeface="MV Boli"/>';
		t[t.length] = '<a:font script="Deva" typeface="Mangal"/>';
		t[t.length] = '<a:font script="Telu" typeface="Gautami"/>';
		t[t.length] = '<a:font script="Taml" typeface="Latha"/>';
		t[t.length] = '<a:font script="Syrc" typeface="Estrangelo Edessa"/>';
		t[t.length] = '<a:font script="Orya" typeface="Kalinga"/>';
		t[t.length] = '<a:font script="Mlym" typeface="Kartika"/>';
		t[t.length] = '<a:font script="Laoo" typeface="DokChampa"/>';
		t[t.length] = '<a:font script="Sinh" typeface="Iskoola Pota"/>';
		t[t.length] = '<a:font script="Mong" typeface="Mongolian Baiti"/>';
		t[t.length] = '<a:font script="Viet" typeface="Arial"/>';
		t[t.length] = '<a:font script="Uigh" typeface="Microsoft Uighur"/>';
		t[t.length] = '<a:font script="Geor" typeface="Sylfaen"/>';
		t[t.length] = "</a:minorFont>";
		t[t.length] = "</a:fontScheme>";
		t[t.length] = '<a:fmtScheme name="Office">';
		t[t.length] = "<a:fillStyleLst>";
		t[t.length] = '<a:solidFill><a:schemeClr val="phClr"/></a:solidFill>';
		t[t.length] = '<a:gradFill rotWithShape="1">';
		t[t.length] = "<a:gsLst>";
		t[t.length] = '<a:gs pos="0"><a:schemeClr val="phClr"><a:tint val="50000"/><a:satMod val="300000"/></a:schemeClr></a:gs>';
		t[t.length] = '<a:gs pos="35000"><a:schemeClr val="phClr"><a:tint val="37000"/><a:satMod val="300000"/></a:schemeClr></a:gs>';
		t[t.length] = '<a:gs pos="100000"><a:schemeClr val="phClr"><a:tint val="15000"/><a:satMod val="350000"/></a:schemeClr></a:gs>';
		t[t.length] = "</a:gsLst>";
		t[t.length] = '<a:lin ang="16200000" scaled="1"/>';
		t[t.length] = "</a:gradFill>";
		t[t.length] = '<a:gradFill rotWithShape="1">';
		t[t.length] = "<a:gsLst>";
		t[t.length] = '<a:gs pos="0"><a:schemeClr val="phClr"><a:tint val="100000"/><a:shade val="100000"/><a:satMod val="130000"/></a:schemeClr></a:gs>';
		t[t.length] = '<a:gs pos="100000"><a:schemeClr val="phClr"><a:tint val="50000"/><a:shade val="100000"/><a:satMod val="350000"/></a:schemeClr></a:gs>';
		t[t.length] = "</a:gsLst>";
		t[t.length] = '<a:lin ang="16200000" scaled="0"/>';
		t[t.length] = "</a:gradFill>";
		t[t.length] = "</a:fillStyleLst>";
		t[t.length] = "<a:lnStyleLst>";
		t[t.length] = '<a:ln w="9525" cap="flat" cmpd="sng" algn="ctr"><a:solidFill><a:schemeClr val="phClr"><a:shade val="95000"/><a:satMod val="105000"/></a:schemeClr></a:solidFill><a:prstDash val="solid"/></a:ln>';
		t[t.length] = '<a:ln w="25400" cap="flat" cmpd="sng" algn="ctr"><a:solidFill><a:schemeClr val="phClr"/></a:solidFill><a:prstDash val="solid"/></a:ln>';
		t[t.length] = '<a:ln w="38100" cap="flat" cmpd="sng" algn="ctr"><a:solidFill><a:schemeClr val="phClr"/></a:solidFill><a:prstDash val="solid"/></a:ln>';
		t[t.length] = "</a:lnStyleLst>";
		t[t.length] = "<a:effectStyleLst>";
		t[t.length] = "<a:effectStyle>";
		t[t.length] = "<a:effectLst>";
		t[t.length] = '<a:outerShdw blurRad="40000" dist="20000" dir="5400000" rotWithShape="0"><a:srgbClr val="000000"><a:alpha val="38000"/></a:srgbClr></a:outerShdw>';
		t[t.length] = "</a:effectLst>";
		t[t.length] = "</a:effectStyle>";
		t[t.length] = "<a:effectStyle>";
		t[t.length] = "<a:effectLst>";
		t[t.length] = '<a:outerShdw blurRad="40000" dist="23000" dir="5400000" rotWithShape="0"><a:srgbClr val="000000"><a:alpha val="35000"/></a:srgbClr></a:outerShdw>';
		t[t.length] = "</a:effectLst>";
		t[t.length] = "</a:effectStyle>";
		t[t.length] = "<a:effectStyle>";
		t[t.length] = "<a:effectLst>";
		t[t.length] = '<a:outerShdw blurRad="40000" dist="23000" dir="5400000" rotWithShape="0"><a:srgbClr val="000000"><a:alpha val="35000"/></a:srgbClr></a:outerShdw>';
		t[t.length] = "</a:effectLst>";
		t[t.length] = '<a:scene3d><a:camera prst="orthographicFront"><a:rot lat="0" lon="0" rev="0"/></a:camera><a:lightRig rig="threePt" dir="t"><a:rot lat="0" lon="0" rev="1200000"/></a:lightRig></a:scene3d>';
		t[t.length] = '<a:sp3d><a:bevelT w="63500" h="25400"/></a:sp3d>';
		t[t.length] = "</a:effectStyle>";
		t[t.length] = "</a:effectStyleLst>";
		t[t.length] = "<a:bgFillStyleLst>";
		t[t.length] = '<a:solidFill><a:schemeClr val="phClr"/></a:solidFill>';
		t[t.length] = '<a:gradFill rotWithShape="1">';
		t[t.length] = "<a:gsLst>";
		t[t.length] = '<a:gs pos="0"><a:schemeClr val="phClr"><a:tint val="40000"/><a:satMod val="350000"/></a:schemeClr></a:gs>';
		t[t.length] = '<a:gs pos="40000"><a:schemeClr val="phClr"><a:tint val="45000"/><a:shade val="99000"/><a:satMod val="350000"/></a:schemeClr></a:gs>';
		t[t.length] = '<a:gs pos="100000"><a:schemeClr val="phClr"><a:shade val="20000"/><a:satMod val="255000"/></a:schemeClr></a:gs>';
		t[t.length] = "</a:gsLst>";
		t[t.length] = '<a:path path="circle"><a:fillToRect l="50000" t="-80000" r="50000" b="180000"/></a:path>';
		t[t.length] = "</a:gradFill>";
		t[t.length] = '<a:gradFill rotWithShape="1">';
		t[t.length] = "<a:gsLst>";
		t[t.length] = '<a:gs pos="0"><a:schemeClr val="phClr"><a:tint val="80000"/><a:satMod val="300000"/></a:schemeClr></a:gs>';
		t[t.length] = '<a:gs pos="100000"><a:schemeClr val="phClr"><a:shade val="30000"/><a:satMod val="200000"/></a:schemeClr></a:gs>';
		t[t.length] = "</a:gsLst>";
		t[t.length] = '<a:path path="circle"><a:fillToRect l="50000" t="50000" r="50000" b="50000"/></a:path>';
		t[t.length] = "</a:gradFill>";
		t[t.length] = "</a:bgFillStyleLst>";
		t[t.length] = "</a:fmtScheme>";
		t[t.length] = "</a:themeElements>";
		t[t.length] = "<a:objectDefaults>";
		t[t.length] = "<a:spDef>";
		t[t.length] = '<a:spPr/><a:bodyPr/><a:lstStyle/><a:style><a:lnRef idx="1"><a:schemeClr val="accent1"/></a:lnRef><a:fillRef idx="3"><a:schemeClr val="accent1"/></a:fillRef><a:effectRef idx="2"><a:schemeClr val="accent1"/></a:effectRef><a:fontRef idx="minor"><a:schemeClr val="lt1"/></a:fontRef></a:style>';
		t[t.length] = "</a:spDef>";
		t[t.length] = "<a:lnDef>";
		t[t.length] = '<a:spPr/><a:bodyPr/><a:lstStyle/><a:style><a:lnRef idx="2"><a:schemeClr val="accent1"/></a:lnRef><a:fillRef idx="0"><a:schemeClr val="accent1"/></a:fillRef><a:effectRef idx="1"><a:schemeClr val="accent1"/></a:effectRef><a:fontRef idx="minor"><a:schemeClr val="tx1"/></a:fontRef></a:style>';
		t[t.length] = "</a:lnDef>";
		t[t.length] = "</a:objectDefaults>";
		t[t.length] = "<a:extraClrSchemeLst/>";
		t[t.length] = "</a:theme>"; return t.join("") }

	function sl(e, r, t) { var a = e.l + r; var n = e._R(4); if(n === 124226) return; if(!t.cellStyles || !be) { e.l = a; return } var i = e.slice(e.l);
		e.l = a; var s; try { s = new be(i) } catch(f) { return } var o = ge(s, "theme/theme/theme1.xml", true); if(!o) return; return nl(o, t) }

	function fl(e) { return e._R(4) }

	function ol(e) { var r = {};
		r.xclrType = e._R(2);
		r.nTintShade = e._R(2); switch(r.xclrType) {
			case 0:
				e.l += 4; break;
			case 1:
				r.xclrValue = ll(e, 4); break;
			case 2:
				r.xclrValue = ri(e, 4); break;
			case 3:
				r.xclrValue = fl(e, 4); break;
			case 4:
				e.l += 4; break; } e.l += 8; return r }

	function ll(e, r) { return Wr(e, r) }

	function cl(e, r) { return Wr(e, r) }

	function hl(e) { var r = e._R(2); var t = e._R(2) - 4; var a = [r]; switch(r) {
			case 4:
				;
			case 5:
				;
			case 7:
				;
			case 8:
				;
			case 9:
				;
			case 10:
				;
			case 11:
				;
			case 13:
				a[1] = ol(e, t); break;
			case 6:
				a[1] = cl(e, t); break;
			case 14:
				;
			case 15:
				a[1] = e._R(t === 1 ? 1 : 2); break;
			default:
				throw new Error("Unrecognized ExtProp type: " + r + " " + t); } return a }

	function ul(e, r) { var t = e.l + r;
		e.l += 2; var a = e._R(2);
		e.l += 2; var n = e._R(2); var i = []; while(n-- > 0) i.push(hl(e, t - e.l)); return { ixfe: a, ext: i } }

	function dl(e, r) { r.forEach(function(e) { switch(e[0]) {
				case 4:
					break;
				case 5:
					break;
				case 6:
					break;
				case 7:
					break;
				case 8:
					break;
				case 9:
					break;
				case 10:
					break;
				case 11:
					break;
				case 13:
					break;
				case 14:
					break;
				case 15:
					break; } }) }

	function pl(e) { var r = []; if(!e) return r; var t = 1;
		(e.match(ke) || []).forEach(function(e) { var a = _e(e); switch(a[0]) {
				case "<?xml":
					break;
				case "<calcChain":
					;
				case "<calcChain>":
					;
				case "</calcChain>":
					break;
				case "<c":
					delete a[0]; if(a.i) t = a.i;
					else a.i = t;
					r.push(a); break; } }); return r }

	function vl(e) { var r = {};
		r.i = e._R(4); var t = {};
		t.r = e._R(4);
		t.c = e._R(4);
		r.r = ot(t); var a = e._R(1); if(a & 2) r.l = "1"; if(a & 8) r.a = "1"; return r }

	function gl(e, r, t) { var a = []; var n = false;
		zr(e, function i(e, r, s) { switch(s) {
				case 63:
					a.push(e); break;
				default:
					if((r || "").indexOf("Begin") > 0) {} else if((r || "").indexOf("End") > 0) {} else if(!n || t.WTF) throw new Error("Unexpected record " + s + " " + r); } }); return a }

	function ml() {}

	function bl(e, r, t) { if(!e) return e; var a = t || {}; var n = false,
			i = false;
		zr(e, function s(e, r, t) { if(i) return; switch(t) {
				case 359:
					;
				case 363:
					;
				case 364:
					;
				case 366:
					;
				case 367:
					;
				case 368:
					;
				case 369:
					;
				case 370:
					;
				case 371:
					;
				case 472:
					;
				case 577:
					;
				case 578:
					;
				case 579:
					;
				case 580:
					;
				case 581:
					;
				case 582:
					;
				case 583:
					;
				case 584:
					;
				case 585:
					;
				case 586:
					;
				case 587:
					break;
				case 35:
					n = true; break;
				case 36:
					n = false; break;
				default:
					if((r || "").indexOf("Begin") > 0) {} else if((r || "").indexOf("End") > 0) {} else if(!n || a.WTF) throw new Error("Unexpected record " + t.toString(16) + " " + r); } }, a) } xa.IMG = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image";
	xa.DRAW = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/drawing";

	function Cl(e, r) { if(!e) return "??"; var t = (e.match(/<c:chart [^>]*r:id="([^"]*)"/) || ["", ""])[1]; return r["!id"][t].Target }
	var wl = 1024;

	function El(e, r) { var t = [21600, 21600]; var a = ["m0,0l0", t[1], t[0], t[1], t[0], "0xe"].join(","); var n = [er("xml", null, { "xmlns:v": nr.v, "xmlns:o": nr.o, "xmlns:x": nr.x, "xmlns:mv": nr.mv }).replace(/\/>/, ">"), er("o:shapelayout", er("o:idmap", null, { "v:ext": "edit", data: e }), { "v:ext": "edit" }), er("v:shapetype", [er("v:stroke", null, { joinstyle: "miter" }), er("v:path", null, { gradientshapeok: "t", "o:connecttype": "rect" })].join(""), { id: "_x0000_t202", "o:spt": 202, coordsize: t.join(","), path: a })]; while(wl < e * 1e3) wl += 1e3;
		r.forEach(function(e) { var r = ft(e[0]);
			n = n.concat(["<v:shape" + qe({ id: "_x0000_s" + ++wl, type: "#_x0000_t202", style: "position:absolute; margin-left:80pt;margin-top:5pt;width:104pt;height:64pt;z-index:10;visibility:hidden", fillcolor: "#ECFAD4", strokecolor: "#edeaa1" }) + ">", er("v:fill", er("o:fill", null, { type: "gradientUnscaled", "v:ext": "view" }), { color2: "#BEFF82", angle: "-180", type: "gradient" }), er("v:shadow", null, { on: "t", obscured: "t" }), er("v:path", null, { "o:connecttype": "none" }), '<v:textbox><div style="text-align:left"></div></v:textbox>', '<x:ClientData ObjectType="Note">', "<x:MoveWithCells/>", "<x:SizeWithCells/>", Je("x:Anchor", [r.c, 0, r.r, 0, r.c + 3, 100, r.r + 5, 100].join(",")), Je("x:AutoFill", "False"), Je("x:Row", String(r.r)), Je("x:Column", String(r.c)), "<x:Visible/>", "</x:ClientData>", "</v:shape>"]) });
		n.push("</xml>"); return n.join("") } xa.CMNT = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/comments";

	function kl(e, r, t, a, n) { for(var i = 0; i != r.length; ++i) { var s = r[i]; var f = pp(ve(e, s.replace(/^\//, ""), true), s, n); if(!f || !f.length) continue; var o = z(t); for(var l = 0; l != o.length; ++l) { var c = o[l]; var h = a[c]; if(h) { var u = h[s]; if(u) Sl(c, t[c], f) } } } }

	function Sl(e, r, t) { var a = Array.isArray(r); var n, i;
		t.forEach(function(e) { if(a) { i = ft(e.ref); if(!r[i.r]) r[i.r] = [];
				n = r[i.r][i.c] } else n = r[e.ref]; if(!n) { n = {}; if(a) r[i.r][i.c] = n;
				else r[e.ref] = n; var t = ht(r["!ref"] || "BDWGO1000001:A1"); var s = ft(e.ref); if(t.s.r > s.r) t.s.r = s.r; if(t.e.r < s.r) t.e.r = s.r; if(t.s.c > s.c) t.s.c = s.c; if(t.e.c < s.c) t.e.c = s.c; var f = ct(t); if(f !== r["!ref"]) r["!ref"] = f } if(!n.c) n.c = []; var o = { a: e.author, t: e.t, r: e.r }; if(e.h) o.h = e.h;
			n.c.push(o) }) }

	function Al(e, r) { if(e.match(/<(?:\w+:)?comments *\/>/)) return []; var t = []; var a = []; var n = e.match(/<(?:\w+:)?authors>([\s\S]*)<\/(?:\w+:)?authors>/); if(n && n[1]) n[1].split(/<\/\w*:?author>/).forEach(function(e) { if(e === "" || e.trim() === "") return; var r = e.match(/<(?:\w+:)?author[^>]*>(.*)/); if(r) t.push(r[1]) }); var i = e.match(/<(?:\w+:)?commentList>([\s\S]*)<\/(?:\w+:)?commentList>/); if(i && i[1]) i[1].split(/<\/\w*:?comment>/).forEach(function(e) { if(e === "" || e.trim() === "") return; var n = e.match(/<(?:\w+:)?comment[^>]*>/); if(!n) return; var i = _e(n[0]); var s = { author: i.authorId && t[i.authorId] || "sheetjsghost", ref: i.ref, guid: i.guid }; var f = ft(i.ref); if(r.sheetRows && r.sheetRows <= f.r) return; var o = e.match(/<(?:\w+:)?text>([\s\S]*)<\/(?:\w+:)?text>/); var l = !!o && !!o[1] && of (o[1]) || { r: "", t: "", h: "" };
			s.r = l.r; if(l.r == "<t></t>") l.t = l.h = "";
			s.t = l.t.replace(/\r\n/g, "\n").replace(/\r/g, "\n"); if(r.cellHTML) s.h = l.h;
			a.push(s) }); return a }
	var _l = er("comments", null, { xmlns: ar.main[0] });

	function Bl(e) { var r = [we, _l]; var t = [];
		r.push("<authors>");
		e.forEach(function(e) { e[1].forEach(function(e) { var a = De(e.a); if(t.indexOf(a) > -1) return;
				t.push(a);
				r.push("<author>" + a + "</author>") }) });
		r.push("</authors>");
		r.push("<commentList>");
		e.forEach(function(e) { e[1].forEach(function(a) { r.push('<comment ref="' + e[0] + '" authorId="' + t.indexOf(De(a.a)) + '"><text>');
				r.push(Je("t", a.t == null ? "" : a.t));
				r.push("</text></comment>") }) });
		r.push("</commentList>"); if(r.length > 2) { r[r.length] = "</comments>";
			r[1] = r[1].replace("/>", ">") } return r.join("") }

	function Tl(e) { var r = {};
		r.iauthor = e._R(4); var t = Ut(e, 16);
		r.rfx = t.s;
		r.ref = ot(t.s);
		e.l += 16; return r }

	function xl(e, r) { if(r == null) r = Vr(36);
		r._W(4, e[1].iauthor);
		Ht(e[0], r);
		r._W(4, 0);
		r._W(4, 0);
		r._W(4, 0);
		r._W(4, 0); return r }
	var yl = bt;

	function Il(e) { return Ct(e.slice(0, 54)) }

	function Rl(e, r) { var t = []; var a = []; var n = {}; var i = false;
		zr(e, function s(e, f, o) { switch(o) {
				case 632:
					a.push(e); break;
				case 635:
					n = e; break;
				case 637:
					n.t = e.t;
					n.h = e.h;
					n.r = e.r; break;
				case 636:
					n.author = a[n.iauthor];
					delete n.iauthor; if(r.sheetRows && r.sheetRows <= n.rfx.r) break; if(!n.t) n.t = "";
					delete n.rfx;
					t.push(n); break;
				case 3072:
					break;
				case 35:
					i = true; break;
				case 36:
					i = false; break;
				case 37:
					break;
				case 38:
					break;
				default:
					if((f || "").indexOf("Begin") > 0) {} else if((f || "").indexOf("End") > 0) {} else if(!i || r.WTF) throw new Error("Unexpected record " + o + " " + f); } }); return t }

	function Dl(e) { var r = Xr(); var t = [];
		Gr(r, "BrtBeginComments");
		Gr(r, "BrtBeginCommentAuthors");
		e.forEach(function(e) { e[1].forEach(function(e) { if(t.indexOf(e.a) > -1) return;
				t.push(e.a.slice(0, 54));
				Gr(r, "BrtCommentAuthor", Il(e.a)) }) });
		Gr(r, "BrtEndCommentAuthors");
		Gr(r, "BrtBeginCommentList");
		e.forEach(function(e) { e[1].forEach(function(a) { a.iauthor = t.indexOf(a.a); var n = { s: ft(e[0]), e: ft(e[0]) };
				Gr(r, "BrtBeginComment", xl([n, a])); if(a.t && a.t.length > 0) Gr(r, "BrtCommentText", _t(a));
				Gr(r, "BrtEndComment");
				delete a.iauthor }) });
		Gr(r, "BrtEndCommentList");
		Gr(r, "BrtEndComments"); return r.end() }
	var Ol = "application/vnd.ms-office.vbaProject";

	function Fl(e) { var r = L.utils.cfb_new({ root: "R" });
		e.FullPaths.forEach(function(t, a) { if(t.slice(-1) === "/" || !t.match(/_VBA_PROJECT_CUR/)) return; var n = t.replace(/^[^\/]*/, "R").replace(/\/_VBA_PROJECT_CUR\u0000*/, "");
			L.utils.cfb_add(r, n, e.FileIndex[a].content) }); return L.write(r) }

	function Pl(e, r) { r.FullPaths.forEach(function(t, a) { if(a == 0) return; var n = t.replace(/[^\/]*[\/]/, "/_VBA_PROJECT_CUR/"); if(n.slice(-1) !== "/") L.utils.cfb_add(e, n, r.FileIndex[a].content) }) }
	var Nl = ["xlsb", "xlsm", "xlam", "biff8", "xla"];
	xa.DS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/dialogsheet";
	xa.MS = "http://schemas.microsoft.com/office/2006/relationships/xlMacrosheet";

	function Ll() { return { "!type": "dialog" } }

	function Ml() { return { "!type": "dialog" } }

	function Ul() { return { "!type": "macro" } }

	function Hl() { return { "!type": "macro" } }
	var Wl = function() { var e = /(^|[^A-Za-z])R(\[?)(-?\d+|)\]?C(\[?)(-?\d+|)\]?/g; var r = { r: 0, c: 0 };

		function t(e, t, a, n, i, s) { var f = n.length > 0 ? parseInt(n, 10) | 0 : 0,
				o = s.length > 0 ? parseInt(s, 10) | 0 : 0; if(o < 0 && i.length === 0) o = 0; var l = false,
				c = false; if(i.length > 0 || s.length == 0) l = true; if(l) o += r.c;
			else --o; if(a.length > 0 || n.length == 0) c = true; if(c) f += r.r;
			else --f; return t + (l ? "" : "$") + at(o) + (c ? "" : "$") + qr(f) } return function a(n, i) { r = i; return n.replace(e, t) } }();
	var Vl = /(^|[^._A-Z0-9])([$]?)([A-Z]{1,2}|[A-W][A-Z]{2}|X[A-E][A-Z]|XF[A-D])([$]?)([1-9]\d{0,5}|10[0-3]\d{4}|104[0-7]\d{3}|1048[0-4]\d{2}|10485[0-6]\d|104857[0-6])(?![_.\(A-Za-z0-9])/g;
	var zl = function() { return function e(r, t) { return r.replace(Vl, function(e, r, a, n, i, s) { var f = tt(n) - (a ? 0 : t.c); var o = Jr(s) - (i ? 0 : t.r); var l = o == 0 ? "" : !i ? "[" + o + "]" : o + 1; var c = f == 0 ? "" : !a ? "[" + f + "]" : f + 1; return r + "R" + l + "C" + c }) } }();

	function Xl(e, r) { return e.replace(Vl, function(e, t, a, n, i, s) { return t + (a == "$" ? a + n : at(tt(n) + r.c)) + (i == "$" ? i + s : qr(Jr(s) + r.r)) }) }

	function Gl(e, r, t) { var a = lt(r),
			n = a.s,
			i = ft(t); var s = { r: i.r - n.r, c: i.c - n.c }; return Xl(e, s) }

	function jl(e) { if(e.length == 1) return false; return true }

	function Kl(e) { return e.replace(/_xlfn\./g, "") }

	function Yl(e) { e.l += 1; return }

	function $l(e, r) { var t = e._R(r == 1 ? 1 : 2); return [t & 16383, t >> 14 & 1, t >> 15 & 1] }

	function Zl(e, r, t) { var a = 2; if(t) { if(t.biff >= 2 && t.biff <= 5) return Ql(e, r, t);
			else if(t.biff == 12) a = 4 } var n = e._R(a),
			i = e._R(a); var s = $l(e, 2); var f = $l(e, 2); return { s: { r: n, c: s[0], cRel: s[1], rRel: s[2] }, e: { r: i, c: f[0], cRel: f[1], rRel: f[2] } } }

	function Ql(e) { var r = $l(e, 2),
			t = $l(e, 2); var a = e._R(1); var n = e._R(1); return { s: { r: r[0], c: a, cRel: r[1], rRel: r[2] }, e: { r: t[0], c: n, cRel: t[1], rRel: t[2] } } }

	function Jl(e, r, t) { if(t.biff < 8) return Ql(e, r, t); var a = e._R(t.biff == 12 ? 4 : 2),
			n = e._R(t.biff == 12 ? 4 : 2); var i = $l(e, 2); var s = $l(e, 2); return { s: { r: a, c: i[0], cRel: i[1], rRel: i[2] }, e: { r: n, c: s[0], cRel: s[1], rRel: s[2] } } }

	function ql(e, r, t) { if(t && t.biff >= 2 && t.biff <= 5) return ec(e, r, t); var a = e._R(t && t.biff == 12 ? 4 : 2); var n = $l(e, 2); return { r: a, c: n[0], cRel: n[1], rRel: n[2] } }

	function ec(e) { var r = $l(e, 2); var t = e._R(1); return { r: r[0], c: t, cRel: r[1], rRel: r[2] } }

	function rc(e) { var r = e._R(2); var t = e._R(2); return { r: r, c: t & 255, fQuoted: !!(t & 16384), cRel: t >> 15, rRel: t >> 15 } }

	function tc(e, r, t) { var a = t && t.biff ? t.biff : 8; if(a >= 2 && a <= 5) return ac(e, r, t); var n = e._R(a >= 12 ? 4 : 2); var i = e._R(2); var s = (i & 16384) >> 14,
			f = (i & 32768) >> 15;
		i &= 16383; if(f == 1)
			while(n > 524287) n -= 1048576; if(s == 1)
			while(i > 8191) i = i - 16384; return { r: n, c: i, cRel: s, rRel: f } }

	function ac(e) { var r = e._R(2); var t = e._R(1); var a = (r & 32768) >> 15,
			n = (r & 16384) >> 14;
		r &= 16383; if(a == 1 && r >= 8192) r = r - 16384; if(n == 1 && t >= 128) t = t - 256; return { r: r, c: t, cRel: n, rRel: a } }

	function nc(e, r, t) { var a = (e[e.l++] & 96) >> 5; var n = Zl(e, t.biff >= 2 && t.biff <= 5 ? 6 : 8, t); return [a, n] }

	function ic(e, r, t) { var a = (e[e.l++] & 96) >> 5; var n = e._R(2, "i"); var i = 8; if(t) switch(t.biff) {
			case 5:
				e.l += 12;
				i = 6; break;
			case 12:
				i = 12; break; }
		var s = Zl(e, i, t); return [a, n, s] }

	function sc(e, r, t) { var a = (e[e.l++] & 96) >> 5;
		e.l += t && t.biff > 8 ? 12 : t.biff < 8 ? 6 : 8; return [a] }

	function fc(e, r, t) { var a = (e[e.l++] & 96) >> 5; var n = e._R(2); var i = 8; if(t) switch(t.biff) {
			case 5:
				e.l += 12;
				i = 6; break;
			case 12:
				i = 12; break; } e.l += i; return [a, n] }

	function oc(e, r, t) { var a = (e[e.l++] & 96) >> 5; var n = Jl(e, r - 1, t); return [a, n] }

	function lc(e, r, t) { var a = (e[e.l++] & 96) >> 5;
		e.l += t.biff == 2 ? 6 : t.biff == 12 ? 14 : 7; return [a] }

	function cc(e) { var r = e[e.l + 1] & 1; var t = 1;
		e.l += 4; return [r, t] }

	function hc(e, r, t) { e.l += 2; var a = e._R(t && t.biff == 2 ? 1 : 2); var n = []; for(var i = 0; i <= a; ++i) n.push(e._R(t && t.biff == 2 ? 1 : 2)); return n }

	function uc(e, r, t) { var a = e[e.l + 1] & 255 ? 1 : 0;
		e.l += 2; return [a, e._R(t && t.biff == 2 ? 1 : 2)] }

	function dc(e, r, t) { var a = e[e.l + 1] & 255 ? 1 : 0;
		e.l += 2; return [a, e._R(t && t.biff == 2 ? 1 : 2)] }

	function pc(e) { var r = e[e.l + 1] & 255 ? 1 : 0;
		e.l += 2; return [r, e._R(2)] }

	function vc(e, r, t) { var a = e[e.l + 1] & 255 ? 1 : 0;
		e.l += t && t.biff == 2 ? 3 : 4; return [a] }

	function gc(e) { var r = e._R(1),
			t = e._R(1); return [r, t] }

	function mc(e) { e._R(2); return gc(e, 2) }

	function bc(e) { e._R(2); return gc(e, 2) }

	function Cc(e, r, t) { var a = (e[e.l] & 96) >> 5;
		e.l += 1; var n = ql(e, 0, t); return [a, n] }

	function wc(e, r, t) { var a = (e[e.l] & 96) >> 5;
		e.l += 1; var n = tc(e, 0, t); return [a, n] }

	function Ec(e, r, t) { var a = (e[e.l] & 96) >> 5;
		e.l += 1; var n = e._R(2); if(t && t.biff == 5) e.l += 12; var i = ql(e, 0, t); return [a, n, i] }

	function kc(e, r, t) { var a = (e[e.l] & 96) >> 5;
		e.l += 1; var n = e._R(t && t.biff <= 3 ? 1 : 2); return [Nh[n], Ph[n], a] }

	function Sc(e, r, t) { var a = e[e.l++]; var n = e._R(1),
			i = t && t.biff <= 3 ? [a == 88 ? -1 : 0, e._R(1)] : Ac(e); return [n, (i[0] === 0 ? Ph : Fh)[i[1]]] }

	function Ac(e) { return [e[e.l + 1] >> 7, e._R(2) & 32767] }

	function _c(e, r, t) { e.l += t && t.biff == 2 ? 3 : 4; return }

	function Bc(e, r, t) { e.l++; if(t && t.biff == 12) return [e._R(4, "i"), 0]; var a = e._R(2); var n = e._R(t && t.biff == 2 ? 1 : 2); return [a, n] }

	function Tc(e) { e.l++; return zt[e._R(1)] }

	function xc(e) { e.l++; return e._R(2) }

	function yc(e) { e.l++; return e._R(1) !== 0 }

	function Ic(e) { e.l++; return Wt(e, 8) }

	function Rc(e, r, t) { e.l++; return Vn(e, r - 1, t) }

	function Dc(e, r) { var t = [e._R(1)]; if(r == 12) switch(t[0]) {
			case 2:
				t[0] = 4; break;
			case 4:
				t[0] = 16; break;
			case 0:
				t[0] = 1; break;
			case 1:
				t[0] = 2; break; }
		switch(t[0]) {
			case 4:
				t[1] = Pn(e, 1) ? "TRUE" : "FALSE"; if(r != 12) e.l += 7; break;
			case 37:
				;
			case 16:
				t[1] = zt[e[e.l]];
				e.l += r == 12 ? 4 : 8; break;
			case 0:
				e.l += 8; break;
			case 1:
				t[1] = Wt(e, 8); break;
			case 2:
				t[1] = jn(e, 0, { biff: r > 0 && r < 8 ? 2 : r }); break;
			default:
				throw new Error("Bad SerAr: " + t[0]); } return t }

	function Oc(e, r, t) { var a = e._R(t.biff == 12 ? 4 : 2); var n = []; for(var i = 0; i != a; ++i) n.push((t.biff == 12 ? Ut : ci)(e, 8)); return n }

	function Fc(e, r, t) { var a = 0,
			n = 0; if(t.biff == 12) { a = e._R(4);
			n = e._R(4) } else { n = 1 + e._R(1);
			a = 1 + e._R(2) } if(t.biff >= 2 && t.biff < 8) {--a; if(--n == 0) n = 256 } for(var i = 0, s = []; i != a && (s[i] = []); ++i)
			for(var f = 0; f != n; ++f) s[i][f] = Dc(e, t.biff); return s }

	function Pc(e, r, t) { var a = e._R(1) >>> 5 & 3; var n = !t || t.biff >= 8 ? 4 : 2; var i = e._R(n); switch(t.biff) {
			case 2:
				e.l += 5; break;
			case 3:
				;
			case 4:
				e.l += 8; break;
			case 5:
				e.l += 12; break; } return [a, 0, i] }

	function Nc(e, r, t) { if(t.biff == 5) return Lc(e, r, t); var a = e._R(1) >>> 5 & 3; var n = e._R(2); var i = e._R(4); return [a, n, i] }

	function Lc(e) { var r = e._R(1) >>> 5 & 3; var t = e._R(2, "i");
		e.l += 8; var a = e._R(2);
		e.l += 12; return [r, t, a] }

	function Mc(e, r, t) { var a = e._R(1) >>> 5 & 3;
		e.l += t && t.biff == 2 ? 3 : 4; var n = e._R(t && t.biff == 2 ? 1 : 2); return [a, n] }

	function Uc(e, r, t) { var a = e._R(1) >>> 5 & 3; var n = e._R(t && t.biff == 2 ? 1 : 2); return [a, n] }

	function Hc(e, r, t) { var a = e._R(1) >>> 5 & 3;
		e.l += 4; if(t.biff < 8) e.l--; if(t.biff == 12) e.l += 2; return [a] }

	function Wc(e, r, t) { var a = (e[e.l++] & 96) >> 5; var n = e._R(2); var i = 4; if(t) switch(t.biff) {
			case 5:
				i = 15; break;
			case 12:
				i = 6; break; } e.l += i; return [a, n] }
	var Vc = Wr;
	var zc = Wr;
	var Xc = Wr;

	function Gc(e, r, t) { e.l += 2; return [rc(e, 4, t)] }

	function jc(e) { e.l += 6; return [] }
	var Kc = Gc;
	var Yc = jc;
	var $c = jc;
	var Zc = Gc;

	function Qc(e) { e.l += 2; return [Ln(e), e._R(2) & 1] }
	var Jc = Gc;
	var qc = Qc;
	var eh = jc;
	var rh = Gc;
	var th = Gc;
	var ah = ["Data", "All", "Headers", "??", "?Data2", "??", "?DataHeaders", "??", "Totals", "??", "??", "??", "?DataTotals", "??", "??", "??", "?Current"];

	function nh(e) { e.l += 2; var r = e._R(2); var t = e._R(2); var a = e._R(4); var n = e._R(2); var i = e._R(2); var s = ah[t >> 2 & 31]; return { ixti: r, coltype: t & 3, rt: s, idx: a, c: n, C: i } }

	function ih(e) { e.l += 2; return [e._R(4)] }

	function sh(e, r, t) { e.l += 5;
		e.l += 2;
		e.l += t.biff == 2 ? 1 : 4; return ["PTGSHEET"] }

	function fh(e, r, t) { e.l += t.biff == 2 ? 4 : 5; return ["PTGENDSHEET"] }

	function oh(e) { var r = e._R(1) >>> 5 & 3; var t = e._R(2); return [r, t] }

	function lh(e) { var r = e._R(1) >>> 5 & 3; var t = e._R(2); return [r, t] }

	function ch(e) { e.l += 4; return [0, 0] }
	var hh = {
		1: { n: "PtgExp", f: Bc },
		2: { n: "PtgTbl", f: Xc },
		3: { n: "PtgAdd", f: Yl },
		4: { n: "PtgSub", f: Yl },
		5: { n: "PtgMul", f: Yl },
		6: { n: "PtgDiv", f: Yl },
		7: { n: "PtgPower", f: Yl },
		8: { n: "PtgConcat", f: Yl },
		9: { n: "PtgLt", f: Yl },
		10: { n: "PtgLe", f: Yl },
		11: { n: "PtgEq", f: Yl },
		12: { n: "PtgGe", f: Yl },
		13: { n: "PtgGt", f: Yl },
		14: { n: "PtgNe", f: Yl },
		15: { n: "PtgIsect", f: Yl },
		16: { n: "PtgUnion", f: Yl },
		17: { n: "PtgRange", f: Yl },
		18: { n: "PtgUplus", f: Yl },
		19: { n: "PtgUminus", f: Yl },
		20: { n: "PtgPercent", f: Yl },
		21: { n: "PtgParen", f: Yl },
		22: { n: "PtgMissArg", f: Yl },
		23: { n: "PtgStr", f: Rc },
		26: { n: "PtgSheet", f: sh },
		27: { n: "PtgEndSheet", f: fh },
		28: { n: "PtgErr", f: Tc },
		29: { n: "PtgBool", f: yc },
		30: { n: "PtgInt", f: xc },
		31: { n: "PtgNum", f: Ic },
		32: { n: "PtgArray", f: lc },
		33: { n: "PtgFunc", f: kc },
		34: { n: "PtgFuncVar", f: Sc },
		35: { n: "PtgName", f: Pc },
		36: { n: "PtgRef", f: Cc },
		37: { n: "PtgArea", f: nc },
		38: { n: "PtgMemArea", f: Mc },
		39: { n: "PtgMemErr", f: Vc },
		40: { n: "PtgMemNoMem", f: zc },
		41: { n: "PtgMemFunc", f: Uc },
		42: { n: "PtgRefErr", f: Hc },
		43: { n: "PtgAreaErr", f: sc },
		44: { n: "PtgRefN", f: wc },
		45: { n: "PtgAreaN", f: oc },
		46: { n: "PtgMemAreaN", f: oh },
		47: { n: "PtgMemNoMemN", f: lh },
		57: { n: "PtgNameX", f: Nc },
		58: { n: "PtgRef3d", f: Ec },
		59: { n: "PtgArea3d", f: ic },
		60: { n: "PtgRefErr3d", f: Wc },
		61: {
			n: "PtgAreaErr3d",
			f: fc
		},
		255: {}
	};
	var uh = { 64: 32, 96: 32, 65: 33, 97: 33, 66: 34, 98: 34, 67: 35, 99: 35, 68: 36, 100: 36, 69: 37, 101: 37, 70: 38, 102: 38, 71: 39, 103: 39, 72: 40, 104: 40, 73: 41, 105: 41, 74: 42, 106: 42, 75: 43, 107: 43, 76: 44, 108: 44, 77: 45, 109: 45, 78: 46, 110: 46, 79: 47, 111: 47, 88: 34, 120: 34, 89: 57, 121: 57, 90: 58, 122: 58, 91: 59, 123: 59, 92: 60, 124: 60, 93: 61, 125: 61 };
	(function() { for(var e in uh) hh[e] = hh[uh[e]] })();
	var dh = { 1: { n: "PtgElfLel", f: Qc }, 2: { n: "PtgElfRw", f: rh }, 3: { n: "PtgElfCol", f: Kc }, 6: { n: "PtgElfRwV", f: th }, 7: { n: "PtgElfColV", f: Zc }, 10: { n: "PtgElfRadical", f: Jc }, 11: { n: "PtgElfRadicalS", f: eh }, 13: { n: "PtgElfColS", f: Yc }, 15: { n: "PtgElfColSV", f: $c }, 16: { n: "PtgElfRadicalLel", f: qc }, 25: { n: "PtgList", f: nh }, 29: { n: "PtgSxName", f: ih }, 255: {} };
	var ph = { 0: { n: "PtgAttrNoop", f: ch }, 1: { n: "PtgAttrSemi", f: vc }, 2: { n: "PtgAttrIf", f: dc }, 4: { n: "PtgAttrChoose", f: hc }, 8: { n: "PtgAttrGoto", f: uc }, 16: { n: "PtgAttrSum", f: _c }, 32: { n: "PtgAttrBaxcel", f: cc }, 64: { n: "PtgAttrSpace", f: mc }, 65: { n: "PtgAttrSpaceSemi", f: bc }, 128: { n: "PtgAttrIfError", f: pc }, 255: {} };
	ph[33] = ph[32];

	function vh(e, r, t, a) { if(a.biff < 8) return Wr(e, r); var n = e.l + r; var i = []; for(var s = 0; s !== t.length; ++s) { switch(t[s][0]) {
				case "PtgArray":
					t[s][1] = Fc(e, 0, a);
					i.push(t[s][1]); break;
				case "PtgMemArea":
					t[s][2] = Oc(e, t[s][1], a);
					i.push(t[s][2]); break;
				case "PtgExp":
					if(a && a.biff == 12) { t[s][1][1] = e._R(4);
						i.push(t[s][1]) } break;
				case "PtgList":
					;
				case "PtgElfRadicalS":
					;
				case "PtgElfColS":
					;
				case "PtgElfColSV":
					throw "Unsupported " + t[s][0];
				default:
					break; } } r = n - e.l; if(r !== 0) i.push(Wr(e, r)); return i }

	function gh(e, r, t) { var a = e.l + r; var n, i, s = []; while(a != e.l) { r = a - e.l;
			i = e[e.l];
			n = hh[i]; if(i === 24 || i === 25) n = (i === 24 ? dh : ph)[e[e.l + 1]]; if(!n || !n.f) { Wr(e, r) } else { s.push([n.n, n.f(e, r, t)]) } } return s }

	function mh(e) { var r = []; for(var t = 0; t < e.length; ++t) { var a = e[t],
				n = []; for(var i = 0; i < a.length; ++i) { var s = a[i]; if(s) switch(s[0]) {
					case 2:
						n.push('"' + s[1].replace(/"/g, '""') + '"'); break;
					default:
						n.push(s[1]); } else n.push("") } r.push(n.join(",")) } return r.join(";") }
	var bh = { PtgAdd: "+", PtgConcat: "&", PtgDiv: "/", PtgEq: "=", PtgGe: ">=", PtgGt: ">", PtgLe: "<=", PtgLt: "<", PtgMul: "*", PtgNe: "<>", PtgPower: "^", PtgSub: "-" };

	function Ch(e, r) { if(!e && !(r && r.biff <= 5 && r.biff >= 2)) throw new Error("empty sheet name"); if(e.indexOf(" ") > -1) return "'" + e + "'"; return e }

	function wh(e, r, t) { if(!e) return "SH33TJSERR0"; if(!e.XTI) return "SH33TJSERR6"; var a = e.XTI[r]; if(t.biff > 8 && !e.XTI[r]) return e.SheetNames[r]; if(t.biff < 8) { if(r > 1e4) r -= 65536; if(r < 0) r = -r; return r == 0 ? "" : e.XTI[r - 1] } if(!a) return "SH33TJSERR1"; var n = ""; if(t.biff > 8) switch(e[a[0]][0]) {
			case 357:
				n = a[1] == -1 ? "#REF" : e.SheetNames[a[1]]; return a[1] == a[2] ? n : n + ":" + e.SheetNames[a[2]];
			case 358:
				if(t.SID != null) return e.SheetNames[t.SID]; return "SH33TJSSAME" + e[a[0]][0];
			case 355:
				;
			default:
				return "SH33TJSSRC" + e[a[0]][0]; }
		switch(e[a[0]][0][0]) {
			case 1025:
				n = a[1] == -1 ? "#REF" : e.SheetNames[a[1]] || "SH33TJSERR3"; return a[1] == a[2] ? n : n + ":" + e.SheetNames[a[2]];
			case 14849:
				return "SH33TJSERR8";
			default:
				if(!e[a[0]][0][3]) return "SH33TJSERR2";
				n = a[1] == -1 ? "#REF" : e[a[0]][0][3][a[1]] || "SH33TJSERR4"; return a[1] == a[2] ? n : n + ":" + e[a[0]][0][3][a[2]]; } }

	function Eh(e, r, t) { return Ch(wh(e, r, t), t) }

	function kh(e, r, t, a, n) { var i = n && n.biff || 8; var s = { s: { c: 0, r: 0 }, e: { c: 0, r: 0 } }; var f = [],
			o, l, c, h = 0,
			u = 0,
			d, p = ""; if(!e[0] || !e[0][0]) return ""; var v = -1,
			g = ""; for(var m = 0, b = e[0].length; m < b; ++m) { var C = e[0][m]; switch(C[0]) {
				case "PtgUminus":
					f.push("-" + f.pop()); break;
				case "PtgUplus":
					f.push("+" + f.pop()); break;
				case "PtgPercent":
					f.push(f.pop() + "%"); break;
				case "PtgAdd":
					;
				case "PtgConcat":
					;
				case "PtgDiv":
					;
				case "PtgEq":
					;
				case "PtgGe":
					;
				case "PtgGt":
					;
				case "PtgLe":
					;
				case "PtgLt":
					;
				case "PtgMul":
					;
				case "PtgNe":
					;
				case "PtgPower":
					;
				case "PtgSub":
					o = f.pop();
					l = f.pop(); if(v >= 0) { switch(e[0][v][1][0]) {
							case 0:
								g = ie(" ", e[0][v][1][1]); break;
							case 1:
								g = ie("\r", e[0][v][1][1]); break;
							default:
								g = ""; if(n.WTF) throw new Error("Unexpected PtgAttrSpaceType " + e[0][v][1][0]); } l = l + g;
						v = -1 } f.push(l + bh[C[0]] + o); break;
				case "PtgIsect":
					o = f.pop();
					l = f.pop();
					f.push(l + " " + o); break;
				case "PtgUnion":
					o = f.pop();
					l = f.pop();
					f.push(l + "," + o); break;
				case "PtgRange":
					o = f.pop();
					l = f.pop();
					f.push(l + ":" + o); break;
				case "PtgAttrChoose":
					break;
				case "PtgAttrGoto":
					break;
				case "PtgAttrIf":
					break;
				case "PtgAttrIfError":
					break;
				case "PtgRef":
					c = jr(C[1][1], s, n);
					f.push(Yr(c, i)); break;
				case "PtgRefN":
					c = t ? jr(C[1][1], t, n) : C[1][1];
					f.push(Yr(c, i)); break;
				case "PtgRef3d":
					h = C[1][1];
					c = jr(C[1][2], s, n);
					p = Eh(a, h, n); var w = p;
					f.push(p + "!" + Yr(c, i)); break;
				case "PtgFunc":
					;
				case "PtgFuncVar":
					var E = C[1][0],
						k = C[1][1]; if(!E) E = 0;
					E &= 127; var S = E == 0 ? [] : f.slice(-E);
					f.length -= E; if(k === "User") k = S.shift();
					f.push(k + "(" + S.join(",") + ")"); break;
				case "PtgBool":
					f.push(C[1] ? "TRUE" : "FALSE"); break;
				case "PtgInt":
					f.push(C[1]); break;
				case "PtgNum":
					f.push(String(C[1])); break;
				case "PtgStr":
					f.push('"' + C[1] + '"'); break;
				case "PtgErr":
					f.push(C[1]); break;
				case "PtgAreaN":
					d = Kr(C[1][1], t ? { s: t } : s, n);
					f.push($r(d, n)); break;
				case "PtgArea":
					d = Kr(C[1][1], s, n);
					f.push($r(d, n)); break;
				case "PtgArea3d":
					h = C[1][1];
					d = C[1][2];
					p = Eh(a, h, n);
					f.push(p + "!" + $r(d, n)); break;
				case "PtgAttrSum":
					f.push("SUM(" + f.pop() + ")"); break;
				case "PtgAttrBaxcel":
					;
				case "PtgAttrSemi":
					break;
				case "PtgName":
					u = C[1][2]; var A = (a.names || [])[u - 1] || (a[0] || [])[u]; var _ = A ? A.Name : "SH33TJSNAME" + String(u); if(_ in Lh) _ = Lh[_];
					f.push(_); break;
				case "PtgNameX":
					var B = C[1][1];
					u = C[1][2]; var T; if(n.biff <= 5) { if(B < 0) B = -B; if(a[B]) T = a[B][u] } else { var x = ""; if(((a[B] || [])[0] || [])[0] == 14849) {} else if(((a[B] || [])[0] || [])[0] == 1025) { if(a[B][u] && a[B][u].itab > 0) { x = a.SheetNames[a[B][u].itab - 1] + "!" } } else x = a.SheetNames[u - 1] + "!"; if(a[B] && a[B][u]) x += a[B][u].Name;
						else if(a[0] && a[0][u]) x += a[0][u].Name;
						else x += "SH33TJSERRX";
						f.push(x); break } if(!T) T = { Name: "SH33TJSERRY" };
					f.push(T.Name); break;
				case "PtgParen":
					var y = "(",
						I = ")"; if(v >= 0) { g = ""; switch(e[0][v][1][0]) {
							case 2:
								y = ie(" ", e[0][v][1][1]) + y; break;
							case 3:
								y = ie("\r", e[0][v][1][1]) + y; break;
							case 4:
								I = ie(" ", e[0][v][1][1]) + I; break;
							case 5:
								I = ie("\r", e[0][v][1][1]) + I; break;
							default:
								if(n.WTF) throw new Error("Unexpected PtgAttrSpaceType " + e[0][v][1][0]); } v = -1 } f.push(y + f.pop() + I); break;
				case "PtgRefErr":
					f.push("#REF!"); break;
				case "PtgRefErr3d":
					f.push("#REF!"); break;
				case "PtgExp":
					c = { c: C[1][1], r: C[1][0] }; var R = { c: t.c, r: t.r }; if(a.sharedf[ot(c)]) { var D = a.sharedf[ot(c)];
						f.push(kh(D, s, R, a, n)) } else { var O = false; for(o = 0; o != a.arrayf.length; ++o) { l = a.arrayf[o]; if(c.c < l[0].s.c || c.c > l[0].e.c) continue; if(c.r < l[0].s.r || c.r > l[0].e.r) continue;
							f.push(kh(l[1], s, R, a, n));
							O = true; break } if(!O) f.push(C[1]) } break;
				case "PtgArray":
					f.push("{" + mh(C[1]) + "}"); break;
				case "PtgMemArea":
					break;
				case "PtgAttrSpace":
					;
				case "PtgAttrSpaceSemi":
					v = m; break;
				case "PtgTbl":
					break;
				case "PtgMemErr":
					break;
				case "PtgMissArg":
					f.push(""); break;
				case "PtgAreaErr":
					f.push("#REF!"); break;
				case "PtgAreaErr3d":
					f.push("#REF!"); break;
				case "PtgList":
					f.push("Table" + C[1].idx + "[#" + C[1].rt + "]"); break;
				case "PtgMemAreaN":
					;
				case "PtgMemNoMemN":
					;
				case "PtgAttrNoop":
					;
				case "PtgSheet":
					;
				case "PtgEndSheet":
					break;
				case "PtgMemFunc":
					break;
				case "PtgMemNoMem":
					break;
				case "PtgElfCol":
					;
				case "PtgElfColS":
					;
				case "PtgElfColSV":
					;
				case "PtgElfColV":
					;
				case "PtgElfLel":
					;
				case "PtgElfRadical":
					;
				case "PtgElfRadicalLel":
					;
				case "PtgElfRadicalS":
					;
				case "PtgElfRw":
					;
				case "PtgElfRwV":
					throw new Error("Unsupported ELFs");
				case "PtgSxName":
					throw new Error("Unrecognized Formula Token: " + String(C));
				default:
					throw new Error("Unrecognized Formula Token: " + String(C)); } var F = ["PtgAttrSpace", "PtgAttrSpaceSemi", "PtgAttrGoto"]; if(n.biff != 3)
				if(v >= 0 && F.indexOf(e[0][m][0]) == -1) { C = e[0][v]; var P = true; switch(C[1][0]) {
						case 4:
							P = false;
						case 0:
							g = ie(" ", C[1][1]); break;
						case 5:
							P = false;
						case 1:
							g = ie("\r", C[1][1]); break;
						default:
							g = ""; if(n.WTF) throw new Error("Unexpected PtgAttrSpaceType " + C[1][0]); } f.push((P ? g : "") + f.pop() + (P ? "" : g));
					v = -1 } } if(f.length > 1 && n.WTF) throw new Error("bad formula stack"); return f[0] }

	function Sh(e, r, t) { var a = e.l + r,
			n = t.biff == 2 ? 1 : 2; var i, s = e._R(n); if(s == 65535) return [
			[], Wr(e, r - 2)
		]; var f = gh(e, s, t); if(r !== s + n) i = vh(e, r - s - n, f, t);
		e.l = a; return [f, i] }

	function Ah(e, r, t) { var a = e.l + r,
			n = t.biff == 2 ? 1 : 2; var i, s = e._R(n); if(s == 65535) return [
			[], Wr(e, r - 2)
		]; var f = gh(e, s, t); if(r !== s + n) i = vh(e, r - s - n, f, t);
		e.l = a; return [f, i] }

	function _h(e, r, t, a) { var n = e.l + r; var i = gh(e, a, t); var s; if(n !== e.l) s = vh(e, n - e.l, i, t); return [i, s] }

	function Bh(e, r, t) { var a = e.l + r; var n, i = e._R(2); var s = gh(e, i, t); if(i == 65535) return [
			[], Wr(e, r - 2)
		]; if(r !== i + 2) n = vh(e, a - i - 2, s, t); return [s, n] }

	function Th(e) { var r; if(yr(e, e.l + 6) !== 65535) return [Wt(e), "n"]; switch(e[e.l]) {
			case 0:
				e.l += 8; return ["String", "s"];
			case 1:
				r = e[e.l + 2] === 1;
				e.l += 8; return [r, "b"];
			case 2:
				r = e[e.l + 2];
				e.l += 8; return [r, "e"];
			case 3:
				e.l += 8; return ["", "s"]; } return [] }

	function xh(e, r, t) { var a = e.l + r; var n = ai(e, 6); if(t.biff == 2) ++e.l; var i = Th(e, 8); var s = e._R(1); if(t.biff != 2) { e._R(1); if(t.biff >= 5) { e._R(4) } } var f = Ah(e, a - e.l, t); return { cell: n, val: i[0], formula: f, shared: s >> 3 & 1, tt: i[1] } }

	function yh(e, r, t) { var a = e._R(4); var n = gh(e, a, t); var i = e._R(4); var s = i > 0 ? vh(e, i, n, t) : null; return [n, s] }
	var Ih = yh;
	var Rh = yh;
	var Dh = yh;
	var Oh = yh;
	var Fh = { 0: "BEEP", 1: "OPEN", 2: "OPEN.LINKS", 3: "CLOSE.ALL", 4: "SAVE", 5: "SAVE.AS", 6: "FILE.DELETE", 7: "PAGE.SETUP", 8: "PRINT", 9: "PRINTER.SETUP", 10: "QUIT", 11: "NEW.WINDOW", 12: "ARRANGE.ALL", 13: "WINDOW.SIZE", 14: "WINDOW.MOVE", 15: "FULL", 16: "CLOSE", 17: "RUN", 22: "SET.PRINT.AREA", 23: "SET.PRINT.TITLES", 24: "SET.PAGE.BREAK", 25: "REMOVE.PAGE.BREAK", 26: "FONT", 27: "DISPLAY", 28: "PROTECT.DOCUMENT", 29: "PRECISION", 30: "A1.R1C1", 31: "CALCULATE.NOW", 32: "CALCULATION", 34: "DATA.FIND", 35: "EXTRACT", 36: "DATA.DELETE", 37: "SET.DATABASE", 38: "SET.CRITERIA", 39: "SORT", 40: "DATA.SERIES", 41: "TABLE", 42: "FORMAT.NUMBER", 43: "ALIGNMENT", 44: "STYLE", 45: "BORDER", 46: "CELL.PROTECTION", 47: "COLUMN.WIDTH", 48: "UNDO", 49: "CUT", 50: "COPY", 51: "PASTE", 52: "CLEAR", 53: "PASTE.SPECIAL", 54: "EDIT.DELETE", 55: "INSERT", 56: "FILL.RIGHT", 57: "FILL.DOWN", 61: "DEFINE.NAME", 62: "CREATE.NAMES", 63: "FORMULA.GOTO", 64: "FORMULA.FIND", 65: "SELECT.LAST.CELL", 66: "SHOW.ACTIVE.CELL", 67: "GALLERY.AREA", 68: "GALLERY.BAR", 69: "GALLERY.COLUMN", 70: "GALLERY.LINE", 71: "GALLERY.PIE", 72: "GALLERY.SCATTER", 73: "COMBINATION", 74: "PREFERRED", 75: "ADD.OVERLAY", 76: "GRIDLINES", 77: "SET.PREFERRED", 78: "AXES", 79: "LEGEND", 80: "ATTACH.TEXT", 81: "ADD.ARROW", 82: "SELECT.CHART", 83: "SELECT.PLOT.AREA", 84: "PATTERNS", 85: "MAIN.CHART", 86: "OVERLAY", 87: "SCALE", 88: "FORMAT.LEGEND", 89: "FORMAT.TEXT", 90: "EDIT.REPEAT", 91: "PARSE", 92: "JUSTIFY", 93: "HIDE", 94: "UNHIDE", 95: "WORKSPACE", 96: "FORMULA", 97: "FORMULA.FILL", 98: "FORMULA.ARRAY", 99: "DATA.FIND.NEXT", 100: "DATA.FIND.PREV", 101: "FORMULA.FIND.NEXT", 102: "FORMULA.FIND.PREV", 103: "ACTIVATE", 104: "ACTIVATE.NEXT", 105: "ACTIVATE.PREV", 106: "UNLOCKED.NEXT", 107: "UNLOCKED.PREV", 108: "COPY.PICTURE", 109: "SELECT", 110: "DELETE.NAME", 111: "DELETE.FORMAT", 112: "VLINE", 113: "HLINE", 114: "VPAGE", 115: "HPAGE", 116: "VSCROLL", 117: "HSCROLL", 118: "ALERT", 119: "NEW", 120: "CANCEL.COPY", 121: "SHOW.CLIPBOARD", 122: "MESSAGE", 124: "PASTE.LINK", 125: "APP.ACTIVATE", 126: "DELETE.ARROW", 127: "ROW.HEIGHT", 128: "FORMAT.MOVE", 129: "FORMAT.SIZE", 130: "FORMULA.REPLACE", 131: "SEND.KEYS", 132: "SELECT.SPECIAL", 133: "APPLY.NAMES", 134: "REPLACE.FONT", 135: "FREEZE.PANES", 136: "SHOW.INFO", 137: "SPLIT", 138: "ON.WINDOW", 139: "ON.DATA", 140: "DISABLE.INPUT", 142: "OUTLINE", 143: "LIST.NAMES", 144: "FILE.CLOSE", 145: "SAVE.WORKBOOK", 146: "DATA.FORM", 147: "COPY.CHART", 148: "ON.TIME", 149: "WAIT", 150: "FORMAT.FONT", 151: "FILL.UP", 152: "FILL.LEFT", 153: "DELETE.OVERLAY", 155: "SHORT.MENUS", 159: "SET.UPDATE.STATUS", 161: "COLOR.PALETTE", 162: "DELETE.STYLE", 163: "WINDOW.RESTORE", 164: "WINDOW.MAXIMIZE", 166: "CHANGE.LINK", 167: "CALCULATE.DOCUMENT", 168: "ON.KEY", 169: "APP.RESTORE", 170: "APP.MOVE", 171: "APP.SIZE", 172: "APP.MINIMIZE", 173: "APP.MAXIMIZE", 174: "BRING.TO.FRONT", 175: "SEND.TO.BACK", 185: "MAIN.CHART.TYPE", 186: "OVERLAY.CHART.TYPE", 187: "SELECT.END", 188: "OPEN.MAIL", 189: "SEND.MAIL", 190: "STANDARD.FONT", 191: "CONSOLIDATE", 192: "SORT.SPECIAL", 193: "GALLERY.3D.AREA", 194: "GALLERY.3D.COLUMN", 195: "GALLERY.3D.LINE", 196: "GALLERY.3D.PIE", 197: "VIEW.3D", 198: "GOAL.SEEK", 199: "WORKGROUP", 200: "FILL.GROUP", 201: "UPDATE.LINK", 202: "PROMOTE", 203: "DEMOTE", 204: "SHOW.DETAIL", 206: "UNGROUP", 207: "OBJECT.PROPERTIES", 208: "SAVE.NEW.OBJECT", 209: "SHARE", 210: "SHARE.NAME", 211: "DUPLICATE", 212: "APPLY.STYLE", 213: "ASSIGN.TO.OBJECT", 214: "OBJECT.PROTECTION", 215: "HIDE.OBJECT", 216: "SET.EXTRACT", 217: "CREATE.PUBLISHER", 218: "SUBSCRIBE.TO", 219: "ATTRIBUTES", 220: "SHOW.TOOLBAR", 222: "PRINT.PREVIEW", 223: "EDIT.COLOR", 224: "SHOW.LEVELS", 225: "FORMAT.MAIN", 226: "FORMAT.OVERLAY", 227: "ON.RECALC", 228: "EDIT.SERIES", 229: "DEFINE.STYLE", 240: "LINE.PRINT", 243: "ENTER.DATA", 249: "GALLERY.RADAR", 250: "MERGE.STYLES", 251: "EDITION.OPTIONS", 252: "PASTE.PICTURE", 253: "PASTE.PICTURE.LINK", 254: "SPELLING", 256: "ZOOM", 259: "INSERT.OBJECT", 260: "WINDOW.MINIMIZE", 265: "SOUND.NOTE", 266: "SOUND.PLAY", 267: "FORMAT.SHAPE", 268: "EXTEND.POLYGON", 269: "FORMAT.AUTO", 272: "GALLERY.3D.BAR", 273: "GALLERY.3D.SURFACE", 274: "FILL.AUTO", 276: "CUSTOMIZE.TOOLBAR", 277: "ADD.TOOL", 278: "EDIT.OBJECT", 279: "ON.DOUBLECLICK", 280: "ON.ENTRY", 281: "WORKBOOK.ADD", 282: "WORKBOOK.MOVE", 283: "WORKBOOK.COPY", 284: "WORKBOOK.OPTIONS", 285: "SAVE.WORKSPACE", 288: "CHART.WIZARD", 289: "DELETE.TOOL", 290: "MOVE.TOOL", 291: "WORKBOOK.SELECT", 292: "WORKBOOK.ACTIVATE", 293: "ASSIGN.TO.TOOL", 295: "COPY.TOOL", 296: "RESET.TOOL", 297: "CONSTRAIN.NUMERIC", 298: "PASTE.TOOL", 302: "WORKBOOK.NEW", 305: "SCENARIO.CELLS", 306: "SCENARIO.DELETE", 307: "SCENARIO.ADD", 308: "SCENARIO.EDIT", 309: "SCENARIO.SHOW", 310: "SCENARIO.SHOW.NEXT", 311: "SCENARIO.SUMMARY", 312: "PIVOT.TABLE.WIZARD", 313: "PIVOT.FIELD.PROPERTIES", 314: "PIVOT.FIELD", 315: "PIVOT.ITEM", 316: "PIVOT.ADD.FIELDS", 318: "OPTIONS.CALCULATION", 319: "OPTIONS.EDIT", 320: "OPTIONS.VIEW", 321: "ADDIN.MANAGER", 322: "MENU.EDITOR", 323: "ATTACH.TOOLBARS", 324: "VBAActivate", 325: "OPTIONS.CHART", 328: "VBA.INSERT.FILE", 330: "VBA.PROCEDURE.DEFINITION", 336: "ROUTING.SLIP", 338: "ROUTE.DOCUMENT", 339: "MAIL.LOGON", 342: "INSERT.PICTURE", 343: "EDIT.TOOL", 344: "GALLERY.DOUGHNUT", 350: "CHART.TREND", 352: "PIVOT.ITEM.PROPERTIES", 354: "WORKBOOK.INSERT", 355: "OPTIONS.TRANSITION", 356: "OPTIONS.GENERAL", 370: "FILTER.ADVANCED", 373: "MAIL.ADD.MAILER", 374: "MAIL.DELETE.MAILER", 375: "MAIL.REPLY", 376: "MAIL.REPLY.ALL", 377: "MAIL.FORWARD", 378: "MAIL.NEXT.LETTER", 379: "DATA.LABEL", 380: "INSERT.TITLE", 381: "FONT.PROPERTIES", 382: "MACRO.OPTIONS", 383: "WORKBOOK.HIDE", 384: "WORKBOOK.UNHIDE", 385: "WORKBOOK.DELETE", 386: "WORKBOOK.NAME", 388: "GALLERY.CUSTOM", 390: "ADD.CHART.AUTOFORMAT", 391: "DELETE.CHART.AUTOFORMAT", 392: "CHART.ADD.DATA", 393: "AUTO.OUTLINE", 394: "TAB.ORDER", 395: "SHOW.DIALOG", 396: "SELECT.ALL", 397: "UNGROUP.SHEETS", 398: "SUBTOTAL.CREATE", 399: "SUBTOTAL.REMOVE", 400: "RENAME.OBJECT", 412: "WORKBOOK.SCROLL", 413: "WORKBOOK.NEXT", 414: "WORKBOOK.PREV", 415: "WORKBOOK.TAB.SPLIT", 416: "FULL.SCREEN", 417: "WORKBOOK.PROTECT", 420: "SCROLLBAR.PROPERTIES", 421: "PIVOT.SHOW.PAGES", 422: "TEXT.TO.COLUMNS", 423: "FORMAT.CHARTTYPE", 424: "LINK.FORMAT", 425: "TRACER.DISPLAY", 430: "TRACER.NAVIGATE", 431: "TRACER.CLEAR", 432: "TRACER.ERROR", 433: "PIVOT.FIELD.GROUP", 434: "PIVOT.FIELD.UNGROUP", 435: "CHECKBOX.PROPERTIES", 436: "LABEL.PROPERTIES", 437: "LISTBOX.PROPERTIES", 438: "EDITBOX.PROPERTIES", 439: "PIVOT.REFRESH", 440: "LINK.COMBO", 441: "OPEN.TEXT", 442: "HIDE.DIALOG", 443: "SET.DIALOG.FOCUS", 444: "ENABLE.OBJECT", 445: "PUSHBUTTON.PROPERTIES", 446: "SET.DIALOG.DEFAULT", 447: "FILTER", 448: "FILTER.SHOW.ALL", 449: "CLEAR.OUTLINE", 450: "FUNCTION.WIZARD", 451: "ADD.LIST.ITEM", 452: "SET.LIST.ITEM", 453: "REMOVE.LIST.ITEM", 454: "SELECT.LIST.ITEM", 455: "SET.CONTROL.VALUE", 456: "SAVE.COPY.AS", 458: "OPTIONS.LISTS.ADD", 459: "OPTIONS.LISTS.DELETE", 460: "SERIES.AXES", 461: "SERIES.X", 462: "SERIES.Y", 463: "ERRORBAR.X", 464: "ERRORBAR.Y", 465: "FORMAT.CHART", 466: "SERIES.ORDER", 467: "MAIL.LOGOFF", 468: "CLEAR.ROUTING.SLIP", 469: "APP.ACTIVATE.MICROSOFT", 470: "MAIL.EDIT.MAILER", 471: "ON.SHEET", 472: "STANDARD.WIDTH", 473: "SCENARIO.MERGE", 474: "SUMMARY.INFO", 475: "FIND.FILE", 476: "ACTIVE.CELL.FONT", 477: "ENABLE.TIPWIZARD", 478: "VBA.MAKE.ADDIN", 480: "INSERTDATATABLE", 481: "WORKGROUP.OPTIONS", 482: "MAIL.SEND.MAILER", 485: "AUTOCORRECT", 489: "POST.DOCUMENT", 491: "PICKLIST", 493: "VIEW.SHOW", 494: "VIEW.DEFINE", 495: "VIEW.DELETE", 509: "SHEET.BACKGROUND", 510: "INSERT.MAP.OBJECT", 511: "OPTIONS.MENONO", 517: "MSOCHECKS", 518: "NORMAL", 519: "LAYOUT", 520: "RM.PRINT.AREA", 521: "CLEAR.PRINT.AREA", 522: "ADD.PRINT.AREA", 523: "MOVE.BRK", 545: "HIDECURR.NOTE", 546: "HIDEALL.NOTES", 547: "DELETE.NOTE", 548: "TRAVERSE.NOTES", 549: "ACTIVATE.NOTES", 620: "PROTECT.REVISIONS", 621: "UNPROTECT.REVISIONS", 647: "OPTIONS.ME", 653: "WEB.PUBLISH", 667: "NEWWEBQUERY", 673: "PIVOT.TABLE.CHART", 753: "OPTIONS.SAVE", 755: "OPTIONS.SPELL", 808: "HIDEALL.INKANNOTS" };
	var Ph = { 0: "COUNT", 1: "IF", 2: "ISNA", 3: "ISERROR", 4: "SUM", 5: "AVERAGE", 6: "MIN", 7: "MAX", 8: "ROW", 9: "COLUMN", 10: "NA", 11: "NPV", 12: "STDEV", 13: "DOLLAR", 14: "FIXED", 15: "SIN", 16: "COS", 17: "TAN", 18: "ATAN", 19: "PI", 20: "SQRT", 21: "EXP", 22: "LN", 23: "LOG10", 24: "ABS", 25: "INT", 26: "SIGN", 27: "ROUND", 28: "LOOKUP", 29: "INDEX", 30: "REPT", 31: "MID", 32: "LEN", 33: "VALUE", 34: "TRUE", 35: "FALSE", 36: "AND", 37: "OR", 38: "NOT", 39: "MOD", 40: "DCOUNT", 41: "DSUM", 42: "DAVERAGE", 43: "DMIN", 44: "DMAX", 45: "DSTDEV", 46: "VAR", 47: "DVAR", 48: "TEXT", 49: "LINEST", 50: "TREND", 51: "LOGEST", 52: "GROWTH", 53: "GOTO", 54: "HALT", 55: "RETURN", 56: "PV", 57: "FV", 58: "NPER", 59: "PMT", 60: "RATE", 61: "MIRR", 62: "IRR", 63: "RAND", 64: "MATCH", 65: "DATE", 66: "TIME", 67: "DAY", 68: "MONTH", 69: "YEAR", 70: "WEEKDAY", 71: "HOUR", 72: "MINUTE", 73: "SECOND", 74: "NOW", 75: "AREAS", 76: "ROWS", 77: "COLUMNS", 78: "OFFSET", 79: "ABSREF", 80: "RELREF", 81: "ARGUMENT", 82: "SEARCH", 83: "TRANSPOSE", 84: "ERROR", 85: "STEP", 86: "TYPE", 87: "ECHO", 88: "SET.NAME", 89: "CALLER", 90: "DEREF", 91: "WINDOWS", 92: "SERIES", 93: "DOCUMENTS", 94: "ACTIVE.CELL", 95: "SELECTION", 96: "RESULT", 97: "ATAN2", 98: "ASIN", 99: "ACOS", 100: "CHOOSE", 101: "HLOOKUP", 102: "VLOOKUP", 103: "LINKS", 104: "INPUT", 105: "ISREF", 106: "GET.FORMULA", 107: "GET.NAME", 108: "SET.VALUE", 109: "LOG", 110: "EXEC", 111: "CHAR", 112: "LOWER", 113: "UPPER", 114: "PROPER", 115: "LEFT", 116: "RIGHT", 117: "EXACT", 118: "TRIM", 119: "REPLACE", 120: "SUBSTITUTE", 121: "CODE", 122: "NAMES", 123: "DIRECTORY", 124: "FIND", 125: "CELL", 126: "ISERR", 127: "ISTEXT", 128: "ISNUMBER", 129: "ISBLANK", 130: "T", 131: "N", 132: "FOPEN", 133: "FCLOSE", 134: "FSIZE", 135: "FREADLN", 136: "FREAD", 137: "FWRITELN", 138: "FWRITE", 139: "FPOS", 140: "DATEVALUE", 141: "TIMEVALUE", 142: "SLN", 143: "SYD", 144: "DDB", 145: "GET.DEF", 146: "REFTEXT", 147: "TEXTREF", 148: "INDIRECT", 149: "REGISTER", 150: "CALL", 151: "ADD.BAR", 152: "ADD.MENU", 153: "ADD.COMMAND", 154: "ENABLE.COMMAND", 155: "CHECK.COMMAND", 156: "RENAME.COMMAND", 157: "SHOW.BAR", 158: "DELETE.MENU", 159: "DELETE.COMMAND", 160: "GET.CHART.ITEM", 161: "DIALOG.BOX", 162: "CLEAN", 163: "MDETERM", 164: "MINVERSE", 165: "MMULT", 166: "FILES", 167: "IPMT", 168: "PPMT", 169: "COUNTA", 170: "CANCEL.KEY", 171: "FOR", 172: "WHILE", 173: "BREAK", 174: "NEXT", 175: "INITIATE", 176: "REQUEST", 177: "POKE", 178: "EXECUTE", 179: "TERMINATE", 180: "RESTART", 181: "HELP", 182: "GET.BAR", 183: "PRODUCT", 184: "FACT", 185: "GET.CELL", 186: "GET.WORKSPACE", 187: "GET.WINDOW", 188: "GET.DOCUMENT", 189: "DPRODUCT", 190: "ISNONTEXT", 191: "GET.NOTE", 192: "NOTE", 193: "STDEVP", 194: "VARP", 195: "DSTDEVP", 196: "DVARP", 197: "TRUNC", 198: "ISLOGICAL", 199: "DCOUNTA", 200: "DELETE.BAR", 201: "UNREGISTER", 204: "USDOLLAR", 205: "FINDB", 206: "SEARCHB", 207: "REPLACEB", 208: "LEFTB", 209: "RIGHTB", 210: "MIDB", 211: "LENB", 212: "ROUNDUP", 213: "ROUNDDOWN", 214: "ASC", 215: "DBCS", 216: "RANK", 219: "ADDRESS", 220: "DAYS360", 221: "TODAY", 222: "VDB", 223: "ELSE", 224: "ELSE.IF", 225: "END.IF", 226: "FOR.CELL", 227: "MEDIAN", 228: "SUMPRODUCT", 229: "SINH", 230: "COSH", 231: "TANH", 232: "ASINH", 233: "ACOSH", 234: "ATANH", 235: "DGET", 236: "CREATE.OBJECT", 237: "VOLATILE", 238: "LAST.ERROR", 239: "CUSTOM.UNDO", 240: "CUSTOM.REPEAT", 241: "FORMULA.CONVERT", 242: "GET.LINK.INFO", 243: "TEXT.BOX", 244: "INFO", 245: "GROUP", 246: "GET.OBJECT", 247: "DB", 248: "PAUSE", 251: "RESUME", 252: "FREQUENCY", 253: "ADD.TOOLBAR", 254: "DELETE.TOOLBAR", 255: "User", 256: "RESET.TOOLBAR", 257: "EVALUATE", 258: "GET.TOOLBAR", 259: "GET.TOOL", 260: "SPELLING.CHECK", 261: "ERROR.TYPE", 262: "APP.TITLE", 263: "WINDOW.TITLE", 264: "SAVE.TOOLBAR", 265: "ENABLE.TOOL", 266: "PRESS.TOOL", 267: "REGISTER.ID", 268: "GET.WORKBOOK", 269: "AVEDEV", 270: "BETADIST", 271: "GAMMALN", 272: "BETAINV", 273: "BINOMDIST", 274: "CHIDIST", 275: "CHIINV", 276: "COMBIN", 277: "CONFIDENCE", 278: "CRITBINOM", 279: "EVEN", 280: "EXPONDIST", 281: "FDIST", 282: "FINV", 283: "FISHER", 284: "FISHERINV", 285: "FLOOR", 286: "GAMMADIST", 287: "GAMMAINV", 288: "CEILING", 289: "HYPGEOMDIST", 290: "LOGNORMDIST", 291: "LOGINV", 292: "NEGBINOMDIST", 293: "NORMDIST", 294: "NORMSDIST", 295: "NORMINV", 296: "NORMSINV", 297: "STANDARDIZE", 298: "ODD", 299: "PERMUT", 300: "POISSON", 301: "TDIST", 302: "WEIBULL", 303: "SUMXMY2", 304: "SUMX2MY2", 305: "SUMX2PY2", 306: "CHITEST", 307: "CORREL", 308: "COVAR", 309: "FORECAST", 310: "FTEST", 311: "INTERCEPT", 312: "PEARSON", 313: "RSQ", 314: "STEYX", 315: "SLOPE", 316: "TTEST", 317: "PROB", 318: "DEVSQ", 319: "GEOMEAN", 320: "HARMEAN", 321: "SUMSQ", 322: "KURT", 323: "SKEW", 324: "ZTEST", 325: "LARGE", 326: "SMALL", 327: "QUARTILE", 328: "PERCENTILE", 329: "PERCENTRANK", 330: "MODE", 331: "TRIMMEAN", 332: "TINV", 334: "MOVIE.COMMAND", 335: "GET.MOVIE", 336: "CONCATENATE", 337: "POWER", 338: "PIVOT.ADD.DATA", 339: "GET.PIVOT.TABLE", 340: "GET.PIVOT.FIELD", 341: "GET.PIVOT.ITEM", 342: "RADIANS", 343: "DEGREES", 344: "SUBTOTAL", 345: "SUMIF", 346: "COUNTIF", 347: "COUNTBLANK", 348: "SCENARIO.GET", 349: "OPTIONS.LISTS.GET", 350: "ISPMT", 351: "DATEDIF", 352: "DATESTRING", 353: "NUMBERSTRING", 354: "ROMAN", 355: "OPEN.DIALOG", 356: "SAVE.DIALOG", 357: "VIEW.GET", 358: "GETPIVOTDATA", 359: "HYPERLINK", 360: "PHONETIC", 361: "AVERAGEA", 362: "MAXA", 363: "MINA", 364: "STDEVPA", 365: "VARPA", 366: "STDEVA", 367: "VARA", 368: "BAHTTEXT", 369: "THAIDAYOFWEEK", 370: "THAIDIGIT", 371: "THAIMONTHOFYEAR", 372: "THAINUMSOUND", 373: "THAINUMSTRING", 374: "THAISTRINGLENGTH", 375: "ISTHAIDIGIT", 376: "ROUNDBAHTDOWN", 377: "ROUNDBAHTUP", 378: "THAIYEAR", 379: "RTD", 380: "CUBEVALUE", 381: "CUBEMEMBER", 382: "CUBEMEMBERPROPERTY", 383: "CUBERANKEDMEMBER", 384: "HEX2BIN", 385: "HEX2DEC", 386: "HEX2OCT", 387: "DEC2BIN", 388: "DEC2HEX", 389: "DEC2OCT", 390: "OCT2BIN", 391: "OCT2HEX", 392: "OCT2DEC", 393: "BIN2DEC", 394: "BIN2OCT", 395: "BIN2HEX", 396: "IMSUB", 397: "IMDIV", 398: "IMPOWER", 399: "IMABS", 400: "IMSQRT", 401: "IMLN", 402: "IMLOG2", 403: "IMLOG10", 404: "IMSIN", 405: "IMCOS", 406: "IMEXP", 407: "IMARGUMENT", 408: "IMCONJUGATE", 409: "IMAGINARY", 410: "IMREAL", 411: "COMPLEX", 412: "IMSUM", 413: "IMPRODUCT", 414: "SERIESSUM", 415: "FACTDOUBLE", 416: "SQRTPI", 417: "QUOTIENT", 418: "DELTA", 419: "GESTEP", 420: "ISEVEN", 421: "ISODD", 422: "MROUND", 423: "ERF", 424: "ERFC", 425: "BESSELJ", 426: "BESSELK", 427: "BESSELY", 428: "BESSELI", 429: "XIRR", 430: "XNPV", 431: "PRICEMAT", 432: "YIELDMAT", 433: "INTRATE", 434: "RECEIVED", 435: "DISC", 436: "PRICEDISC", 437: "YIELDDISC", 438: "TBILLEQ", 439: "TBILLPRICE", 440: "TBILLYIELD", 441: "PRICE", 442: "YIELD", 443: "DOLLARDE", 444: "DOLLARFR", 445: "NOMINAL", 446: "EFFECT", 447: "CUMPRINC", 448: "CUMIPMT", 449: "EDATE", 450: "EOMONTH", 451: "YEARFRAC", 452: "COUPDAYBS", 453: "COUPDAYS", 454: "COUPDAYSNC", 455: "COUPNCD", 456: "COUPNUM", 457: "COUPPCD", 458: "DURATION", 459: "MDURATION", 460: "ODDLPRICE", 461: "ODDLYIELD", 462: "ODDFPRICE", 463: "ODDFYIELD", 464: "RANDBETWEEN", 465: "WEEKNUM", 466: "AMORDEGRC", 467: "AMORLINC", 468: "CONVERT", 724: "SHEETJS", 469: "ACCRINT", 470: "ACCRINTM", 471: "WORKDAY", 472: "NETWORKDAYS", 473: "GCD", 474: "MULTINOMIAL", 475: "LCM", 476: "FVSCHEDULE", 477: "CUBEKPIMEMBER", 478: "CUBESET", 479: "CUBESETCOUNT", 480: "IFERROR", 481: "COUNTIFS", 482: "SUMIFS", 483: "AVERAGEIF", 484: "AVERAGEIFS" };
	var Nh = { 2: 1, 3: 1, 10: 0, 15: 1, 16: 1, 17: 1, 18: 1, 19: 0, 20: 1, 21: 1, 22: 1, 23: 1, 24: 1, 25: 1, 26: 1, 27: 2, 30: 2, 31: 3, 32: 1, 33: 1, 34: 0, 35: 0, 38: 1, 39: 2, 40: 3, 41: 3, 42: 3, 43: 3, 44: 3, 45: 3, 47: 3, 48: 2, 53: 1, 61: 3, 63: 0, 65: 3, 66: 3, 67: 1, 68: 1, 69: 1, 70: 1, 71: 1, 72: 1, 73: 1, 74: 0, 75: 1, 76: 1, 77: 1, 79: 2, 80: 2, 83: 1, 85: 0, 86: 1, 89: 0, 90: 1, 94: 0, 95: 0, 97: 2, 98: 1, 99: 1, 101: 3, 102: 3, 105: 1, 106: 1, 108: 2, 111: 1, 112: 1, 113: 1, 114: 1, 117: 2, 118: 1, 119: 4, 121: 1, 126: 1, 127: 1, 128: 1, 129: 1, 130: 1, 131: 1, 133: 1, 134: 1, 135: 1, 136: 2, 137: 2, 138: 2, 140: 1, 141: 1, 142: 3, 143: 4, 144: 4, 161: 1, 162: 1, 163: 1, 164: 1, 165: 2, 172: 1, 175: 2, 176: 2, 177: 3, 178: 2, 179: 1, 184: 1, 186: 1, 189: 3, 190: 1, 195: 3, 196: 3, 197: 1, 198: 1, 199: 3, 201: 1, 207: 4, 210: 3, 211: 1, 212: 2, 213: 2, 214: 1, 215: 1, 225: 0, 229: 1, 230: 1, 231: 1, 232: 1, 233: 1, 234: 1, 235: 3, 244: 1, 247: 4, 252: 2, 257: 1, 261: 1, 271: 1, 273: 4, 274: 2, 275: 2, 276: 2, 277: 3, 278: 3, 279: 1, 280: 3, 281: 3, 282: 3, 283: 1, 284: 1, 285: 2, 286: 4, 287: 3, 288: 2, 289: 4, 290: 3, 291: 3, 292: 3, 293: 4, 294: 1, 295: 3, 296: 1, 297: 3, 298: 1, 299: 2, 300: 3, 301: 3, 302: 4, 303: 2, 304: 2, 305: 2, 306: 2, 307: 2, 308: 2, 309: 3, 310: 2, 311: 2, 312: 2, 313: 2, 314: 2, 315: 2, 316: 4, 325: 2, 326: 2, 327: 2, 328: 2, 331: 2, 332: 2, 337: 2, 342: 1, 343: 1, 346: 2, 347: 1, 350: 4, 351: 3, 352: 1, 353: 2, 360: 1, 368: 1, 369: 1, 370: 1, 371: 1, 372: 1, 373: 1, 374: 1, 375: 1, 376: 1, 377: 1, 378: 1, 382: 3, 385: 1, 392: 1, 393: 1, 396: 2, 397: 2, 398: 2, 399: 1, 400: 1, 401: 1, 402: 1, 403: 1, 404: 1, 405: 1, 406: 1, 407: 1, 408: 1, 409: 1, 410: 1, 414: 4, 415: 1, 416: 1, 417: 2, 420: 1, 421: 1, 422: 2, 424: 1, 425: 2, 426: 2, 427: 2, 428: 2, 430: 3, 438: 3, 439: 3, 440: 3, 443: 2, 444: 2, 445: 2, 446: 2, 447: 6, 448: 6, 449: 2, 450: 2, 464: 2, 468: 3, 476: 2, 479: 1, 480: 2, 65535: 0 };
	var Lh = { "_xlfn.ACOT": "ACOT", "_xlfn.ACOTH": "ACOTH", "_xlfn.AGGREGATE": "AGGREGATE", "_xlfn.ARABIC": "ARABIC", "_xlfn.AVERAGEIF": "AVERAGEIF", "_xlfn.AVERAGEIFS": "AVERAGEIFS", "_xlfn.BASE": "BASE", "_xlfn.BETA.DIST": "BETA.DIST", "_xlfn.BETA.INV": "BETA.INV", "_xlfn.BINOM.DIST": "BINOM.DIST", "_xlfn.BINOM.DIST.RANGE": "BINOM.DIST.RANGE", "_xlfn.BINOM.INV": "BINOM.INV", "_xlfn.BITAND": "BITAND", "_xlfn.BITLSHIFT": "BITLSHIFT", "_xlfn.BITOR": "BITOR", "_xlfn.BITRSHIFT": "BITRSHIFT", "_xlfn.BITXOR": "BITXOR", "_xlfn.CEILING.MATH": "CEILING.MATH", "_xlfn.CEILING.PRECISE": "CEILING.PRECISE", "_xlfn.CHISQ.DIST": "CHISQ.DIST", "_xlfn.CHISQ.DIST.RT": "CHISQ.DIST.RT", "_xlfn.CHISQ.INV": "CHISQ.INV", "_xlfn.CHISQ.INV.RT": "CHISQ.INV.RT", "_xlfn.CHISQ.TEST": "CHISQ.TEST", "_xlfn.COMBINA": "COMBINA", "_xlfn.CONCAT": "CONCAT", "_xlfn.CONFIDENCE.NORM": "CONFIDENCE.NORM", "_xlfn.CONFIDENCE.T": "CONFIDENCE.T", "_xlfn.COT": "COT", "_xlfn.COTH": "COTH", "_xlfn.COUNTIFS": "COUNTIFS", "_xlfn.COVARIANCE.P": "COVARIANCE.P", "_xlfn.COVARIANCE.S": "COVARIANCE.S", "_xlfn.CSC": "CSC", "_xlfn.CSCH": "CSCH", "_xlfn.DAYS": "DAYS", "_xlfn.DECIMAL": "DECIMAL", "_xlfn.ECMA.CEILING": "ECMA.CEILING", "_xlfn.ERF.PRECISE": "ERF.PRECISE", "_xlfn.ERFC.PRECISE": "ERFC.PRECISE", "_xlfn.EXPON.DIST": "EXPON.DIST", "_xlfn.F.DIST": "F.DIST", "_xlfn.F.DIST.RT": "F.DIST.RT", "_xlfn.F.INV": "F.INV", "_xlfn.F.INV.RT": "F.INV.RT", "_xlfn.F.TEST": "F.TEST", "_xlfn.FILTERXML": "FILTERXML", "_xlfn.FLOOR.MATH": "FLOOR.MATH", "_xlfn.FLOOR.PRECISE": "FLOOR.PRECISE", "_xlfn.FORECAST.ETS": "FORECAST.ETS", "_xlfn.FORECAST.ETS.CONFINT": "FORECAST.ETS.CONFINT", "_xlfn.FORECAST.ETS.SEASONALITY": "FORECAST.ETS.SEASONALITY", "_xlfn.FORECAST.ETS.STAT": "FORECAST.ETS.STAT", "_xlfn.FORECAST.LINEAR": "FORECAST.LINEAR", "_xlfn.FORMULATEXT": "FORMULATEXT", "_xlfn.GAMMA": "GAMMA", "_xlfn.GAMMA.DIST": "GAMMA.DIST", "_xlfn.GAMMA.INV": "GAMMA.INV", "_xlfn.GAMMALN.PRECISE": "GAMMALN.PRECISE", "_xlfn.GAUSS": "GAUSS", "_xlfn.HYPGEOM.DIST": "HYPGEOM.DIST", "_xlfn.IFERROR": "IFERROR", "_xlfn.IFNA": "IFNA", "_xlfn.IFS": "IFS", "_xlfn.IMCOSH": "IMCOSH", "_xlfn.IMCOT": "IMCOT", "_xlfn.IMCSC": "IMCSC", "_xlfn.IMCSCH": "IMCSCH", "_xlfn.IMSEC": "IMSEC", "_xlfn.IMSECH": "IMSECH", "_xlfn.IMSINH": "IMSINH", "_xlfn.IMTAN": "IMTAN", "_xlfn.ISFORMULA": "ISFORMULA", "_xlfn.ISO.CEILING": "ISO.CEILING", "_xlfn.ISOWEEKNUM": "ISOWEEKNUM", "_xlfn.LOGNORM.DIST": "LOGNORM.DIST", "_xlfn.LOGNORM.INV": "LOGNORM.INV", "_xlfn.MAXIFS": "MAXIFS", "_xlfn.MINIFS": "MINIFS", "_xlfn.MODE.MULT": "MODE.MULT", "_xlfn.MODE.SNGL": "MODE.SNGL", "_xlfn.MUNIT": "MUNIT", "_xlfn.NEGBINOM.DIST": "NEGBINOM.DIST", "_xlfn.NETWORKDAYS.INTL": "NETWORKDAYS.INTL", "_xlfn.NIGBINOM": "NIGBINOM", "_xlfn.NORM.DIST": "NORM.DIST", "_xlfn.NORM.INV": "NORM.INV", "_xlfn.NORM.S.DIST": "NORM.S.DIST", "_xlfn.NORM.S.INV": "NORM.S.INV", "_xlfn.NUMBERVALUE": "NUMBERVALUE", "_xlfn.PDURATION": "PDURATION", "_xlfn.PERCENTILE.EXC": "PERCENTILE.EXC", "_xlfn.PERCENTILE.INC": "PERCENTILE.INC", "_xlfn.PERCENTRANK.EXC": "PERCENTRANK.EXC", "_xlfn.PERCENTRANK.INC": "PERCENTRANK.INC", "_xlfn.PERMUTATIONA": "PERMUTATIONA", "_xlfn.PHI": "PHI", "_xlfn.POISSON.DIST": "POISSON.DIST", "_xlfn.QUARTILE.EXC": "QUARTILE.EXC", "_xlfn.QUARTILE.INC": "QUARTILE.INC", "_xlfn.QUERYSTRING": "QUERYSTRING", "_xlfn.RANK.AVG": "RANK.AVG", "_xlfn.RANK.EQ": "RANK.EQ", "_xlfn.RRI": "RRI", "_xlfn.SEC": "SEC", "_xlfn.SECH": "SECH", "_xlfn.SHEET": "SHEET", "_xlfn.SHEETS": "SHEETS", "_xlfn.SKEW.P": "SKEW.P", "_xlfn.STDEV.P": "STDEV.P", "_xlfn.STDEV.S": "STDEV.S", "_xlfn.SUMIFS": "SUMIFS", "_xlfn.SWITCH": "SWITCH", "_xlfn.T.DIST": "T.DIST", "_xlfn.T.DIST.2T": "T.DIST.2T", "_xlfn.T.DIST.RT": "T.DIST.RT", "_xlfn.T.INV": "T.INV", "_xlfn.T.INV.2T": "T.INV.2T", "_xlfn.T.TEST": "T.TEST", "_xlfn.TEXTJOIN": "TEXTJOIN", "_xlfn.UNICHAR": "UNICHAR", "_xlfn.UNICODE": "UNICODE", "_xlfn.VAR.P": "VAR.P", "_xlfn.VAR.S": "VAR.S", "_xlfn.WEBSERVICE": "WEBSERVICE", "_xlfn.WEIBULL.DIST": "WEIBULL.DIST", "_xlfn.WORKDAY.INTL": "WORKDAY.INTL", "_xlfn.XOR": "XOR", "_xlfn.Z.TEST": "Z.TEST" };

	function Mh(e) { if(e.slice(0, 3) == "of:") e = e.slice(3); if(e.charCodeAt(0) == 61) { e = e.slice(1); if(e.charCodeAt(0) == 61) e = e.slice(1) } e = e.replace(/COM\.MICROSOFT\./g, "");
		e = e.replace(/\[((?:\.[A-Z]+[0-9]+)(?::\.[A-Z]+[0-9]+)?)\]/g, function(e, r) { return r.replace(/\./g, "") });
		e = e.replace(/\[.(#[A-Z]*[?!])\]/g, "$1"); return e.replace(/[;~]/g, ",").replace(/\|/g, ";") }

	function Uh(e) { var r = "of:=" + e.replace(Vl, "$1[.$2$3$4$5]").replace(/\]:\[/g, ":"); return r.replace(/;/g, "|").replace(/,/g, ";") }

	function Hh(e) { var r = e.split(":"); var t = r[0].split(".")[0]; return [t, r[0].split(".")[1] + (r.length > 1 ? ":" + (r[1].split(".")[1] || r[1].split(".")[0]) : "")] }

	function Wh(e) { return e.replace(/\./, "!") }
	var Vh = {};
	var zh = {};
	xa.WS = ["http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet", "http://purl.oclc.org/ooxml/officeDocument/relationships/worksheet"];

	function Xh(e, r) { for(var t = 0, a = e.length; t < a; ++t)
			if(e[t].t === r) { e.Count++; return t }
		e[a] = { t: r };
		e.Count++;
		e.Unique++; return a }

	function Gh(e, r) { var t = { min: e + 1, max: e + 1 }; var a = -1; if(r.MDW) Jf = r.MDW; if(r.width != null) t.customWidth = 1;
		else if(r.wpx != null) a = eo(r.wpx);
		else if(r.wch != null) a = r.wch; if(a > -1) { t.width = ro(a);
			t.customWidth = 1 } else if(r.width != null) t.width = r.width; if(r.hidden) t.hidden = true; return t }

	function jh(e, r) { if(!e) return; var t = [.7, .7, .75, .75, .3, .3]; if(r == "xlml") t = [1, 1, 1, 1, .5, .5]; if(e.left == null) e.left = t[0]; if(e.right == null) e.right = t[1]; if(e.top == null) e.top = t[2]; if(e.bottom == null) e.bottom = t[3]; if(e.header == null) e.header = t[4]; if(e.footer == null) e.footer = t[5] }

	function Kh(e, r, t) { var a = t.revssf[r.z != null ? r.z : "General"]; var n = 60,
			i = e.length; if(a == null && t.ssf) { for(; n < 392; ++n)
				if(t.ssf[n] == null) { y.load(r.z, n);
					t.ssf[n] = r.z;
					t.revssf[r.z] = a = n; break } } for(n = 0; n != i; ++n)
			if(e[n].numFmtId === a) return n;
		e[i] = { numFmtId: a, fontId: 0, fillId: 0, borderId: 0, xfId: 0, applyNumberFormat: 1 }; return i }

	function Yh(e, r, t, a, n, i) { if(e.t === "z") return; if(e.t === "d" && typeof e.v === "string") e.v = te(e.v); try { if(a.cellNF) e.z = y._table[r] } catch(s) { if(a.WTF) throw s } if(!a || a.cellText !== false) try { if(y._table[r] == null) y.load(D[r] || "General", r); if(e.t === "e") e.w = e.w || zt[e.v];
			else if(r === 0) { if(e.t === "n") { if((e.v | 0) === e.v) e.w = y._general_int(e.v);
					else e.w = y._general_num(e.v) } else if(e.t === "d") { var f = Q(e.v); if((f | 0) === f) e.w = y._general_int(f);
					else e.w = y._general_num(f) } else if(e.v === undefined) return "";
				else e.w = y._general(e.v, zh) } else if(e.t === "d") e.w = y.format(r, Q(e.v), zh);
			else e.w = y.format(r, e.v, zh) } catch(s) { if(a.WTF) throw s }
		if(!a.cellStyles) return; if(t != null) try { e.s = i.Fills[t]; if(e.s.fgColor && e.s.fgColor.theme && !e.s.fgColor.rgb) { e.s.fgColor.rgb = Yf(n.themeElements.clrScheme[e.s.fgColor.theme].rgb, e.s.fgColor.tint || 0); if(a.WTF) e.s.fgColor.raw_rgb = n.themeElements.clrScheme[e.s.fgColor.theme].rgb } if(e.s.bgColor && e.s.bgColor.theme) { e.s.bgColor.rgb = Yf(n.themeElements.clrScheme[e.s.bgColor.theme].rgb, e.s.bgColor.tint || 0); if(a.WTF) e.s.bgColor.raw_rgb = n.themeElements.clrScheme[e.s.bgColor.theme].rgb } } catch(s) { if(a.WTF && i.Fills) throw s } }

	function $h(e, r, t) { if(e && e["!ref"]) { var a = ht(e["!ref"]); if(a.e.c < a.s.c || a.e.r < a.s.r) throw new Error("Bad range (" + t + "): " + e["!ref"]) } }

	function Zh(e, r) { var t = ht(r); if(t.s.r <= t.e.r && t.s.c <= t.e.c && t.s.r >= 0 && t.s.c >= 0) e["!ref"] = ct(t) }
	var Qh = /<(?:\w:)?mergeCell ref="[A-Z0-9:]+"\s*[\/]?>/g;
	var Jh = /<(?:\w+:)?sheetData>([\s\S]*)<\/(?:\w+:)?sheetData>/;
	var qh = /<(?:\w:)?hyperlink [^>]*>/gm;
	var eu = /"(\w*:\w*)"/;
	var ru = /<(?:\w:)?col[^>]*[\/]?>/g;
	var tu = /<(?:\w:)?autoFilter[^>]*([\/]|>([\s\S]*)<\/(?:\w:)?autoFilter)>/g;
	var au = /<(?:\w:)?pageMargins[^>]*\/>/g;
	var nu = /<(?:\w:)?sheetPr(?:[^>a-z][^>]*)?\/>/;
	var iu = /<(?:\w:)?sheetViews[^>]*(?:[\/]|>([\s\S]*)<\/(?:\w:)?sheetViews)>/;

	function su(e, r, t, a, n, i, s) {
		if(!e) return e;
		if(g != null && r.dense == null) r.dense = g;
		var f = r.dense ? [] : {};
		var o = { s: { r: 2e6, c: 2e6 }, e: { r: 0, c: 0 } };
		var l = "",
			c = "";
		var h = e.match(Jh);
		if(h) { l = e.slice(0, h.index);
			c = e.slice(h.index + h[0].length) } else l = c = e;
		var u = l.match(nu);
		if(u) ou(u[0], f, n, t);
		var d = (l.match(/<(?:\w*:)?dimension/) || { index: -1 }).index;
		if(d > 0) { var p = l.slice(d, d + 50).match(eu); if(p) Zh(f, p[1]) }
		var v = l.match(iu);
		if(v && v[1]) bu(v[1], n);
		var m = [];
		if(r.cellStyles) { var b = l.match(ru); if(b) du(m, b) }
		if(h) Eu(h[1], f, r, o, i, s);
		var C = c.match(tu);
		if(C) f["!autofilter"] = vu(C[0]);
		var w = [];
		var E = c.match(Qh);
		if(E)
			for(d = 0; d != E.length; ++d) w[d] = ht(E[d].slice(E[d].indexOf('"') + 1));
		var k = c.match(qh);
		if(k) cu(f, k, a);
		var S = c.match(au);
		if(S) f["!margins"] = hu(_e(S[0]));
		if(!f["!ref"] && o.e.c >= o.s.c && o.e.r >= o.s.r) f["!ref"] = ct(o);
		if(r.sheetRows > 0 && f["!ref"]) { var A = ht(f["!ref"]); if(r.sheetRows <= +A.e.r) { A.e.r = r.sheetRows - 1; if(A.e.r > o.e.r) A.e.r = o.e.r; if(A.e.r < A.s.r) A.s.r = A.e.r; if(A.e.c > o.e.c) A.e.c = o.e.c; if(A.e.c < A.s.c) A.s.c = A.e.c;
				f["!fullref"] = f["!ref"];
				f["!ref"] = ct(A) } }
		if(m.length > 0) f["!cols"] = m;
		if(w.length > 0) f["!merges"] = w;
		return f
	}

	function fu(e) { if(e.length === 0) return ""; var r = '<mergeCells count="' + e.length + '">'; for(var t = 0; t != e.length; ++t) r += '<mergeCell ref="' + ct(e[t]) + '"/>'; return r + "</mergeCells>" }

	function ou(e, r, t, a) { var n = _e(e); if(!t.Sheets[a]) t.Sheets[a] = {}; if(n.codeName) t.Sheets[a].CodeName = n.codeName }

	function lu(e) { var r = { sheet: 1 }; var t = ["objects", "scenarios", "selectLockedCells", "selectUnlockedCells"]; var a = ["formatColumns", "formatRows", "formatCells", "insertColumns", "insertRows", "insertHyperlinks", "deleteColumns", "deleteRows", "sort", "autoFilter", "pivotTables"];
		t.forEach(function(t) { if(e[t] != null && e[t]) r[t] = "1" });
		a.forEach(function(t) { if(e[t] != null && !e[t]) r[t] = "0" }); if(e.password) r.password = Nf(e.password).toString(16).toUpperCase(); return er("sheetProtection", null, r) }

	function cu(e, r, t) { var a = Array.isArray(e); for(var n = 0; n != r.length; ++n) { var i = _e(He(r[n]), true); if(!i.ref) return; var s = ((t || {})["!id"] || [])[i.id]; if(s) { i.Target = s.Target; if(i.location) i.Target += "#" + i.location } else { i.Target = "#" + i.location;
				s = { Target: i.Target, TargetMode: "Internal" } } i.Rel = s; if(i.tooltip) { i.Tooltip = i.tooltip;
				delete i.tooltip } var f = ht(i.ref); for(var o = f.s.r; o <= f.e.r; ++o)
				for(var l = f.s.c; l <= f.e.c; ++l) { var c = ot({ c: l, r: o }); if(a) { if(!e[o]) e[o] = []; if(!e[o][l]) e[o][l] = { t: "z", v: undefined };
						e[o][l].l = i } else { if(!e[c]) e[c] = { t: "z", v: undefined };
						e[c].l = i } } } }

	function hu(e) { var r = {};
		["left", "right", "top", "bottom", "header", "footer"].forEach(function(t) { if(e[t]) r[t] = parseFloat(e[t]) }); return r }

	function uu(e) { jh(e); return er("pageMargins", null, e) }

	function du(e, r) { var t = false; for(var a = 0; a != r.length; ++a) { var n = _e(r[a], true); if(n.hidden) n.hidden = Ue(n.hidden); var i = parseInt(n.min, 10) - 1,
				s = parseInt(n.max, 10) - 1;
			delete n.min;
			delete n.max;
			n.width = +n.width; if(!t && n.width) { t = true;
				ao(n.width) } no(n); while(i <= s) e[i++] = ne(n) } }

	function pu(e, r) { var t = ["<cols>"],
			a; for(var n = 0; n != r.length; ++n) { if(!(a = r[n])) continue;
			t[t.length] = er("col", null, Gh(n, a)) } t[t.length] = "</cols>"; return t.join("") }

	function vu(e) { var r = { ref: (e.match(/ref="([^"]*)"/) || [])[1] }; return r }

	function gu(e) { return er("autoFilter", null, { ref: e.ref }) }
	var mu = /<(?:\w:)?sheetView(?:[^>a-z][^>]*)?\/>/;

	function bu(e, r) {
		(e.match(mu) || []).forEach(function(e) { var t = _e(e); if(Ue(t.rightToLeft)) { if(!r.Views) r.Views = [{}]; if(!r.Views[0]) r.Views[0] = {};
				r.Views[0].RTL = true } }) }

	function Cu(e, r, t, a) { var n = { workbookViewId: "0" }; if((((a || {}).Workbook || {}).Views || [])[0]) n.rightToLeft = a.Workbook.Views[0].RTL ? "1" : "0"; return er("sheetViews", er("sheetView", null, n), {}) }

	function wu(e, r, t, a) { if(e.v === undefined && e.f === undefined || e.t === "z") return ""; var n = ""; var i = e.t,
			s = e.v; switch(e.t) {
			case "b":
				n = e.v ? "1" : "0"; break;
			case "n":
				n = "" + e.v; break;
			case "e":
				n = zt[e.v]; break;
			case "d":
				if(a.cellDates) n = te(e.v, -1).toISOString();
				else { e = ne(e);
					e.t = "n";
					n = "" + (e.v = Q(te(e.v))) } if(typeof e.z === "undefined") e.z = y._table[14]; break;
			default:
				n = e.v; break; } var f = Je("v", De(n)),
			o = { r: r }; var l = Kh(a.cellXfs, e, a); if(l !== 0) o.s = l; switch(e.t) {
			case "n":
				break;
			case "d":
				o.t = "d"; break;
			case "b":
				o.t = "b"; break;
			case "e":
				o.t = "e"; break;
			default:
				if(e.v == null) { delete e.t; break } if(a.bookSST) { f = Je("v", "" + Xh(a.Strings, e.v));
					o.t = "s"; break } o.t = "str"; break; } if(e.t != i) { e.t = i;
			e.v = s } if(e.f) { var c = e.F && e.F.slice(0, r.length) == r ? { t: "array", ref: e.F } : null;
			f = er("f", De(e.f), c) + (e.v != null ? f : "") } if(e.l) t["!links"].push([r, e.l]); if(e.c) t["!comments"].push([r, e.c]); return er("c", f, o) }
	var Eu = function() { var e = /<(?:\w+:)?c[ >]/,
			r = /<\/(?:\w+:)?row>/; var t = /r=["']([^"']*)["']/,
			a = /<(?:\w+:)?is>([\S\s]*?)<\/(?:\w+:)?is>/; var n = /ref=["']([^"']*)["']/; var i = Ge("v"),
			s = Ge("f"); return function f(o, l, c, h, u, d) { var p = 0,
				v = "",
				g = [],
				m = [],
				b = 0,
				C = 0,
				w = 0,
				E = "",
				k; var S, A = 0,
				_ = 0; var B, T; var x = 0,
				I = 0; var R = Array.isArray(d.CellXf),
				D; var O = []; var F = []; var P = Array.isArray(l); var N = [],
				L = {},
				M = false; for(var U = o.split(r), H = 0, W = U.length; H != W; ++H) { v = U[H].trim(); var V = v.length; if(V === 0) continue; for(p = 0; p < V; ++p)
					if(v.charCodeAt(p) === 62) break;++p;
				S = _e(v.slice(0, p), true);
				A = S.r != null ? parseInt(S.r, 10) : A + 1;
				_ = -1; if(c.sheetRows && c.sheetRows < A) continue; if(h.s.r > A - 1) h.s.r = A - 1; if(h.e.r < A - 1) h.e.r = A - 1; if(c && c.cellStyles) { L = {};
					M = false; if(S.ht) { M = true;
						L.hpt = parseFloat(S.ht);
						L.hpx = oo(L.hpt) } if(S.hidden == "1") { M = true;
						L.hidden = true } if(S.outlineLevel != null) { M = true;
						L.level = +S.outlineLevel } if(M) N[A - 1] = L } g = v.slice(p).split(e); for(p = 0; p != g.length; ++p) { v = g[p].trim(); if(v.length === 0) continue;
					m = v.match(t);
					b = p;
					C = 0;
					w = 0;
					v = "<c " + (v.slice(0, 1) == "<" ? ">" : "") + v; if(m != null && m.length === 2) { b = 0;
						E = m[1]; for(C = 0; C != E.length; ++C) { if((w = E.charCodeAt(C) - 64) < 1 || w > 26) break;
							b = 26 * b + w }--b;
						_ = b } else ++_; for(C = 0; C != v.length; ++C)
						if(v.charCodeAt(C) === 62) break;++C;
					S = _e(v.slice(0, C), true); if(!S.r) S.r = ot({ r: A - 1, c: _ });
					E = v.slice(C);
					k = { t: "" }; if((m = E.match(i)) != null && m[1] !== "") k.v = ye(m[1]); if(c.cellFormula) { if((m = E.match(s)) != null && m[1] !== "") { k.f = Kl(ye(He(m[1]))); if(m[0].indexOf('t="array"') > -1) { k.F = (E.match(n) || [])[1]; if(k.F.indexOf(":") > -1) O.push([ht(k.F), k.F]) } else if(m[0].indexOf('t="shared"') > -1) { T = _e(m[0]);
								F[parseInt(T.si, 10)] = [T, Kl(ye(He(m[1])))] } } else if(m = E.match(/<f[^>]*\/>/)) { T = _e(m[0]); if(F[T.si]) k.f = Gl(F[T.si][1], F[T.si][0].ref, S.r) } var z = ft(S.r); for(C = 0; C < O.length; ++C)
							if(z.r >= O[C][0].s.r && z.r <= O[C][0].e.r)
								if(z.c >= O[C][0].s.c && z.c <= O[C][0].e.c) k.F = O[C][1] } if(S.t == null && k.v === undefined) { if(k.f || k.F) { k.v = 0;
							k.t = "n" } else if(!c.sheetStubs) continue;
						else k.t = "z" } else k.t = S.t || "n"; if(h.s.c > b) h.s.c = b; if(h.e.c < b) h.e.c = b; switch(k.t) {
						case "n":
							if(k.v == "" || k.v == null) { if(!c.sheetStubs) continue;
								k.t = "z" } else k.v = parseFloat(k.v); break;
						case "s":
							if(typeof k.v == "undefined") { if(!c.sheetStubs) continue;
								k.t = "z" } else { B = Vh[parseInt(k.v, 10)];
								k.v = B.t;
								k.r = B.r; if(c.cellHTML) k.h = B.h } break;
						case "str":
							k.t = "s";
							k.v = k.v != null ? He(k.v) : ""; if(c.cellHTML) k.h = Pe(k.v); break;
						case "inlineStr":
							m = E.match(a);
							k.t = "s"; if(m != null && (B = of (m[1]))) k.v = B.t;
							else k.v = ""; break;
						case "b":
							k.v = Ue(k.v); break;
						case "d":
							if(c.cellDates) k.v = te(k.v, 1);
							else { k.v = Q(te(k.v, 1));
								k.t = "n" } break;
						case "e":
							if(!c || c.cellText !== false) k.w = k.v;
							k.v = Xt[k.v]; break; } x = I = 0; if(R && S.s !== undefined) { D = d.CellXf[S.s]; if(D != null) { if(D.numFmtId != null) x = D.numFmtId; if(c.cellStyles) { if(D.fillId != null) I = D.fillId } } } Yh(k, x, I, c, u, d); if(c.cellDates && R && k.t == "n" && y.is_date(y._table[x])) { k.t = "d";
						k.v = J(k.v) } if(P) { var X = ft(S.r); if(!l[X.r]) l[X.r] = [];
						l[X.r][X.c] = k } else l[S.r] = k } } if(N.length > 0) l["!rows"] = N } }();

	function ku(e, r, t, a) { var n = [],
			i = [],
			s = ht(e["!ref"]),
			f = "",
			o, l = "",
			c = [],
			h = 0,
			u = 0,
			d = e["!rows"]; var p = Array.isArray(e); var v = { r: l },
			g, m = -1; for(u = s.s.c; u <= s.e.c; ++u) c[u] = at(u); for(h = s.s.r; h <= s.e.r; ++h) { i = [];
			l = qr(h); for(u = s.s.c; u <= s.e.c; ++u) { o = c[u] + l; var b = p ? (e[h] || [])[u] : e[o]; if(b === undefined) continue; if((f = wu(b, o, e, r, t, a)) != null) i.push(f) } if(i.length > 0 || d && d[h]) { v = { r: l }; if(d && d[h]) { g = d[h]; if(g.hidden) v.hidden = 1;
					m = -1; if(g.hpx) m = fo(g.hpx);
					else if(g.hpt) m = g.hpt; if(m > -1) { v.ht = m;
						v.customHeight = 1 } if(g.level) { v.outlineLevel = g.level } } n[n.length] = er("row", i.join(""), v) } } if(d)
			for(; h < d.length; ++h) { if(d && d[h]) { v = { r: h + 1 };
					g = d[h]; if(g.hidden) v.hidden = 1;
					m = -1; if(g.hpx) m = fo(g.hpx);
					else if(g.hpt) m = g.hpt; if(m > -1) { v.ht = m;
						v.customHeight = 1 } if(g.level) { v.outlineLevel = g.level } n[n.length] = er("row", "", v) } }
		return n.join("") }
	var Su = er("worksheet", null, { xmlns: ar.main[0], "xmlns:r": ar.r });

	function Au(e, r, t, a) { var n = [we, Su]; var i = t.SheetNames[e],
			s = 0,
			f = ""; var o = t.Sheets[i]; if(o == null) o = {}; var l = o["!ref"] || "A1"; var c = ht(l); if(c.e.c > 16383 || c.e.r > 1048575) { if(r.WTF) throw new Error("Range " + l + " exceeds format limit A1:XFD1048576");
			c.e.c = Math.min(c.e.c, 16383);
			c.e.r = Math.min(c.e.c, 1048575);
			l = ct(c) } if(!a) a = {};
		o["!comments"] = [];
		o["!drawing"] = []; if(r.bookType !== "xlsx" && t.vbaraw) { var h = t.SheetNames[e]; try { if(t.Workbook) h = t.Workbook.Sheets[e].CodeName || h } catch(u) {} n[n.length] = er("sheetPr", null, { codeName: De(h) }) } n[n.length] = er("dimension", null, { ref: l });
		n[n.length] = Cu(o, r, e, t); if(r.sheetFormat) n[n.length] = er("sheetFormatPr", null, { defaultRowHeight: r.sheetFormat.defaultRowHeight || "16", baseColWidth: r.sheetFormat.baseColWidth || "10", outlineLevelRow: r.sheetFormat.outlineLevelRow || "7" }); if(o["!cols"] != null && o["!cols"].length > 0) n[n.length] = pu(o, o["!cols"]);
		n[s = n.length] = "<sheetData/>";
		o["!links"] = []; if(o["!ref"] != null) { f = ku(o, r, e, t, a); if(f.length > 0) n[n.length] = f } if(n.length > s + 1) { n[n.length] = "</sheetData>";
			n[s] = n[s].replace("/>", ">") } if(o["!protect"] != null) n[n.length] = lu(o["!protect"]); if(o["!autofilter"] != null) n[n.length] = gu(o["!autofilter"]); if(o["!merges"] != null && o["!merges"].length > 0) n[n.length] = fu(o["!merges"]); var d = -1,
			p, v = -1; if(o["!links"].length > 0) { n[n.length] = "<hyperlinks>";
			o["!links"].forEach(function(e) { if(!e[1].Target) return;
				p = { ref: e[0] }; if(e[1].Target.charAt(0) != "#") { v = Oa(a, -1, De(e[1].Target).replace(/#.*$/, ""), xa.HLINK);
					p["r:id"] = "rId" + v } if((d = e[1].Target.indexOf("#")) > -1) p.location = De(e[1].Target.slice(d + 1)); if(e[1].Tooltip) p.tooltip = De(e[1].Tooltip);
				n[n.length] = er("hyperlink", null, p) });
			n[n.length] = "</hyperlinks>" } delete o["!links"]; if(o["!margins"] != null) n[n.length] = uu(o["!margins"]);
		n[n.length] = "";
		n[n.length] = Je("ignoredErrors", er("ignoredError", null, { numberStoredAsText: 1, sqref: l })); if(o["!drawing"].length > 0) { v = Oa(a, -1, "../drawings/drawing" + (e + 1) + ".xml", xa.DRAW);
			n[n.length] = er("drawing", null, { "r:id": "rId" + v }) } else delete o["!drawing"]; if(o["!comments"].length > 0) { v = Oa(a, -1, "../drawings/vmlDrawing" + (e + 1) + ".vml", xa.VML);
			n[n.length] = er("legacyDrawing", null, { "r:id": "rId" + v });
			o["!legacy"] = v } if(n.length > 2) { n[n.length] = "</worksheet>";
			n[1] = n[1].replace("/>", ">") } return n.join("") }

	function _u(e, r) { var t = {}; var a = e.l + r;
		t.r = e._R(4);
		e.l += 4; var n = e._R(2);
		e.l += 1; var i = e._R(1);
		e.l = a; if(i & 7) t.level = i & 7; if(i & 16) t.hidden = true; if(i & 32) t.hpt = n / 20; return t }

	function Bu(e, r, t) { var a = Vr(17 + 8 * 16); var n = (t["!rows"] || [])[e] || {};
		a._W(4, e);
		a._W(4, 0); var i = 320; if(n.hpx) i = fo(n.hpx) * 20;
		else if(n.hpt) i = n.hpt * 20;
		a._W(2, i);
		a._W(1, 0); var s = 0; if(n.level) s |= n.level; if(n.hidden) s |= 16; if(n.hpx || n.hpt) s |= 32;
		a._W(1, s);
		a._W(1, 0); var f = 0,
			o = a.l;
		a.l += 4; var l = { r: e, c: 0 }; for(var c = 0; c < 16; ++c) { if(r.s.c > c + 1 << 10 || r.e.c < c << 10) continue; var h = -1,
				u = -1; for(var d = c << 10; d < c + 1 << 10; ++d) { l.c = d; var p = Array.isArray(t) ? (t[l.r] || [])[l.c] : t[ot(l)]; if(p) { if(h < 0) h = d;
					u = d } } if(h < 0) continue;++f;
			a._W(4, h);
			a._W(4, u) } var v = a.l;
		a.l = o;
		a._W(4, f);
		a.l = v; return a.length > a.l ? a.slice(0, a.l) : a }

	function Tu(e, r, t, a) { var n = Bu(a, t, r); if(n.length > 17 || (r["!rows"] || [])[a]) Gr(e, "BrtRowHdr", n) }
	var xu = Ut;
	var yu = Ht;

	function Iu() {}

	function Ru(e, r) { var t = {};
		e.l += 19;
		t.name = xt(e, r - 19); return t }

	function Du(e, r) { if(r == null) r = Vr(84 + 4 * e.length); for(var t = 0; t < 3; ++t) r._W(1, 0);
		jt({ auto: 1 }, r);
		r._W(-4, -1);
		r._W(-4, -1);
		yt(e, r); return r.slice(0, r.l) }

	function Ou(e) { var r = Bt(e); return [r] }

	function Fu(e, r, t) { if(t == null) t = Vr(8); return Tt(r, t) }

	function Pu(e) { var r = Bt(e); var t = e._R(1); return [r, t, "b"] }

	function Nu(e, r, t) { if(t == null) t = Vr(9);
		Tt(r, t);
		t._W(1, e.v ? 1 : 0); return t }

	function Lu(e) { var r = Bt(e); var t = e._R(1); return [r, t, "e"] }

	function Mu(e) { var r = Bt(e); var t = e._R(4); return [r, t, "s"] }

	function Uu(e, r, t) { if(t == null) t = Vr(12);
		Tt(r, t);
		t._W(4, r.v); return t }

	function Hu(e) { var r = Bt(e); var t = Wt(e); return [r, t, "n"] }

	function Wu(e, r, t) { if(t == null) t = Vr(16);
		Tt(r, t);
		Vt(e.v, t); return t }

	function Vu(e) { var r = Bt(e); var t = Pt(e); return [r, t, "n"] }

	function zu(e, r, t) { if(t == null) t = Vr(12);
		Tt(r, t);
		Nt(e.v, t); return t }

	function Xu(e) { var r = Bt(e); var t = bt(e); return [r, t, "str"] }

	function Gu(e, r, t) { if(t == null) t = Vr(12 + 4 * e.v.length);
		Tt(r, t);
		Ct(e.v, t); return t.length > t.l ? t.slice(0, t.l) : t }

	function ju(e, r, t) { var a = e.l + r; var n = Bt(e);
		n.r = t["!row"]; var i = e._R(1); var s = [n, i, "b"]; if(t.cellFormula) { e.l += 2; var f = Rh(e, a - e.l, t);
			s[3] = kh(f, null, n, t.supbooks, t) } else e.l = a; return s }

	function Ku(e, r, t) { var a = e.l + r; var n = Bt(e);
		n.r = t["!row"]; var i = e._R(1); var s = [n, i, "e"]; if(t.cellFormula) { e.l += 2; var f = Rh(e, a - e.l, t);
			s[3] = kh(f, null, n, t.supbooks, t) } else e.l = a; return s }

	function Yu(e, r, t) { var a = e.l + r; var n = Bt(e);
		n.r = t["!row"]; var i = Wt(e); var s = [n, i, "n"]; if(t.cellFormula) { e.l += 2; var f = Rh(e, a - e.l, t);
			s[3] = kh(f, null, n, t.supbooks, t) } else e.l = a; return s }

	function $u(e, r, t) { var a = e.l + r; var n = Bt(e);
		n.r = t["!row"]; var i = bt(e); var s = [n, i, "str"]; if(t.cellFormula) { e.l += 2; var f = Rh(e, a - e.l, t);
			s[3] = kh(f, null, n, t.supbooks, t) } else e.l = a; return s }
	var Zu = Ut;
	var Qu = Ht;

	function Ju(e, r) { if(r == null) r = Vr(4);
		r._W(4, e); return r }

	function qu(e, r) { var t = e.l + r; var a = Ut(e, 16); var n = It(e); var i = bt(e); var s = bt(e); var f = bt(e);
		e.l = t; var o = { rfx: a, relId: n, loc: i, display: f }; if(s) o.Tooltip = s; return o }

	function ed(e, r) { var t = Vr(50 + 4 * (e[1].Target.length + (e[1].Tooltip || "").length));
		Ht({ s: ft(e[0]), e: ft(e[0]) }, t);
		Ft("rId" + r, t); var a = e[1].Target.indexOf("#"); var n = a == -1 ? "" : e[1].Target.slice(a + 1);
		Ct(n || "", t);
		Ct(e[1].Tooltip || "", t);
		Ct("", t); return t.slice(0, t.l) }

	function rd(e, r, t) { var a = e.l + r; var n = Lt(e, 16); var i = e._R(1); var s = [n];
		s[2] = i; if(t.cellFormula) { var f = Ih(e, a - e.l, t);
			s[1] = f } else e.l = a; return s }

	function td(e, r, t) { var a = e.l + r; var n = Ut(e, 16); var i = [n]; if(t.cellFormula) { var s = Oh(e, a - e.l, t);
			i[1] = s;
			e.l = a } else e.l = a; return i }

	function ad(e, r, t) { if(t == null) t = Vr(18); var a = Gh(e, r);
		t._W(-4, e);
		t._W(-4, e);
		t._W(4, (a.width || 10) * 256);
		t._W(4, 0); var n = 0; if(r.hidden) n |= 1; if(typeof a.width == "number") n |= 2;
		t._W(1, n);
		t._W(1, 0); return t }
	var nd = ["left", "right", "top", "bottom", "header", "footer"];

	function id(e) { var r = {};
		nd.forEach(function(t) { r[t] = Wt(e, 8) }); return r }

	function sd(e, r) { if(r == null) r = Vr(6 * 8);
		jh(e);
		nd.forEach(function(t) { Vt(e[t], r) }); return r }

	function fd(e) { var r = e._R(2);
		e.l += 28; return { RTL: r & 32 } }

	function od(e, r, t) { if(t == null) t = Vr(30); var a = 924; if((((r || {}).Views || [])[0] || {}).RTL) a |= 32;
		t._W(2, a);
		t._W(4, 0);
		t._W(4, 0);
		t._W(4, 0);
		t._W(1, 0);
		t._W(1, 0);
		t._W(2, 0);
		t._W(2, 100);
		t._W(2, 0);
		t._W(2, 0);
		t._W(2, 0);
		t._W(4, 0); return t }

	function ld(e) { var r = Vr(24);
		r._W(4, 4);
		r._W(4, 1);
		Ht(e, r); return r }

	function cd(e, r) { if(r == null) r = Vr(16 * 4 + 2);
		r._W(2, e.password ? Nf(e.password) : 0);
		r._W(4, 1);
		[
			["objects", false],
			["scenarios", false],
			["formatCells", true],
			["formatColumns", true],
			["formatRows", true],
			["insertColumns", true],
			["insertRows", true],
			["insertHyperlinks", true],
			["deleteColumns", true],
			["deleteRows", true],
			["selectLockedCells", false],
			["sort", true],
			["autoFilter", true],
			["pivotTables", true],
			["selectUnlockedCells", false]
		].forEach(function(t) { if(t[1]) r._W(4, e[t[0]] != null && !e[t[0]] ? 1 : 0);
			else r._W(4, e[t[0]] != null && e[t[0]] ? 0 : 1) }); return r }

	function hd(e, r, t, a, n, i, s) { if(!e) return e; var f = r || {}; if(!a) a = { "!id": {} }; if(g != null && f.dense == null) f.dense = g; var o = f.dense ? [] : {}; var l; var c = { s: { r: 2e6, c: 2e6 }, e: { r: 0, c: 0 } }; var h = false,
			u = false; var d, p, v, m, b, C, w, E, k; var S = [];
		f.biff = 12;
		f["!row"] = 0; var A = 0,
			_ = false; var B = []; var T = {}; var x = f.supbooks || [
			[]
		];
		x.sharedf = T;
		x.arrayf = B;
		x.SheetNames = n.SheetNames || n.Sheets.map(function(e) { return e.name }); if(!f.supbooks) { f.supbooks = x; if(n.Names)
				for(var I = 0; I < n.Names.length; ++I) x[0][I + 1] = n.Names[I] } var R = [],
			D = []; var O = false;
		zr(e, function P(e, r, g) { if(u) return; switch(g) {
				case 148:
					l = e; break;
				case 0:
					d = e; if(f.sheetRows && f.sheetRows <= d.r) u = true;
					E = qr(m = d.r);
					f["!row"] = d.r; if(e.hidden || e.hpt || e.level != null) { if(e.hpt) e.hpx = oo(e.hpt);
						D[e.r] = e } break;
				case 2:
					;
				case 3:
					;
				case 4:
					;
				case 5:
					;
				case 6:
					;
				case 7:
					;
				case 8:
					;
				case 9:
					;
				case 10:
					;
				case 11:
					p = { t: e[2] }; switch(e[2]) {
						case "n":
							p.v = e[1]; break;
						case "s":
							w = Vh[e[1]];
							p.v = w.t;
							p.r = w.r; break;
						case "b":
							p.v = e[1] ? true : false; break;
						case "e":
							p.v = e[1]; if(f.cellText !== false) p.w = zt[p.v]; break;
						case "str":
							p.t = "s";
							p.v = e[1]; break; } if(v = s.CellXf[e[0].iStyleRef]) Yh(p, v.numFmtId, null, f, i, s);
					b = e[0].c; if(f.dense) { if(!o[m]) o[m] = [];
						o[m][b] = p } else o[at(b) + E] = p; if(f.cellFormula) { _ = false; for(A = 0; A < B.length; ++A) { var I = B[A]; if(d.r >= I[0].s.r && d.r <= I[0].e.r)
								if(b >= I[0].s.c && b <= I[0].e.c) { p.F = ct(I[0]);
									_ = true } } if(!_ && e.length > 3) p.f = e[3] } if(c.s.r > d.r) c.s.r = d.r; if(c.s.c > b) c.s.c = b; if(c.e.r < d.r) c.e.r = d.r; if(c.e.c < b) c.e.c = b; if(f.cellDates && v && p.t == "n" && y.is_date(y._table[v.numFmtId])) { var F = y.parse_date_code(p.v); if(F) { p.t = "d";
							p.v = new Date(F.y, F.m - 1, F.d, F.H, F.M, F.S, F.u) } } break;
				case 1:
					if(!f.sheetStubs || h) break;
					p = { t: "z", v: undefined };
					b = e[0].c; if(f.dense) { if(!o[m]) o[m] = [];
						o[m][b] = p } else o[at(b) + E] = p; if(c.s.r > d.r) c.s.r = d.r; if(c.s.c > b) c.s.c = b; if(c.e.r < d.r) c.e.r = d.r; if(c.e.c < b) c.e.c = b; break;
				case 176:
					S.push(e); break;
				case 494:
					var P = a["!id"][e.relId]; if(P) { e.Target = P.Target; if(e.loc) e.Target += "#" + e.loc;
						e.Rel = P } else if(e.relId == "") { e.Target = "#" + e.loc } for(m = e.rfx.s.r; m <= e.rfx.e.r; ++m)
						for(b = e.rfx.s.c; b <= e.rfx.e.c; ++b) { if(f.dense) { if(!o[m]) o[m] = []; if(!o[m][b]) o[m][b] = { t: "z", v: undefined };
								o[m][b].l = e } else { C = ot({ c: b, r: m }); if(!o[C]) o[C] = { t: "z", v: undefined };
								o[C].l = e } }
					break;
				case 426:
					if(!f.cellFormula) break;
					B.push(e);
					k = f.dense ? o[m][b] : o[at(b) + E];
					k.f = kh(e[1], c, { r: d.r, c: b }, x, f);
					k.F = ct(e[0]); break;
				case 427:
					if(!f.cellFormula) break;
					T[ot(e[0].s)] = e[1];
					k = f.dense ? o[m][b] : o[at(b) + E];
					k.f = kh(e[1], c, { r: d.r, c: b }, x, f); break;
				case 60:
					if(!f.cellStyles) break; while(e.e >= e.s) { R[e.e--] = { width: e.w / 256, hidden: !!(e.flags & 1) }; if(!O) { O = true;
							ao(e.w / 256) } no(R[e.e + 1]) } break;
				case 161:
					o["!autofilter"] = { ref: ct(e) }; break;
				case 476:
					o["!margins"] = e; break;
				case 147:
					if(!n.Sheets[t]) n.Sheets[t] = {}; if(e.name) n.Sheets[t].CodeName = e.name; break;
				case 137:
					if(!n.Views) n.Views = [{}]; if(!n.Views[0]) n.Views[0] = {}; if(e.RTL) n.Views[0].RTL = true; break;
				case 485:
					break;
				case 175:
					;
				case 644:
					;
				case 625:
					;
				case 562:
					;
				case 396:
					;
				case 1112:
					;
				case 1146:
					;
				case 471:
					;
				case 1050:
					;
				case 649:
					;
				case 1105:
					;
				case 49:
					;
				case 589:
					;
				case 607:
					;
				case 564:
					;
				case 1055:
					;
				case 168:
					;
				case 174:
					;
				case 1180:
					;
				case 499:
					;
				case 64:
					;
				case 1053:
					;
				case 550:
					;
				case 171:
					;
				case 167:
					;
				case 1177:
					;
				case 169:
					;
				case 1181:
					;
				case 551:
					;
				case 552:
					;
				case 661:
					;
				case 639:
					;
				case 478:
					;
				case 151:
					;
				case 537:
					;
				case 477:
					;
				case 536:
					;
				case 1103:
					;
				case 680:
					;
				case 1104:
					;
				case 1024:
					;
				case 152:
					;
				case 663:
					;
				case 535:
					;
				case 678:
					;
				case 504:
					;
				case 1043:
					;
				case 428:
					;
				case 170:
					;
				case 3072:
					;
				case 50:
					;
				case 2070:
					;
				case 1045:
					break;
				case 35:
					h = true; break;
				case 36:
					h = false; break;
				case 37:
					break;
				case 38:
					break;
				default:
					if((r || "").indexOf("Begin") > 0) {} else if((r || "").indexOf("End") > 0) {} else if(!h || f.WTF) throw new Error("Unexpected record " + g + " " + r); } }, f);
		delete f.supbooks;
		delete f["!row"]; if(!o["!ref"] && (c.s.r < 2e6 || l && (l.e.r > 0 || l.e.c > 0 || l.s.r > 0 || l.s.c > 0))) o["!ref"] = ct(l || c); if(f.sheetRows && o["!ref"]) { var F = ht(o["!ref"]); if(f.sheetRows <= +F.e.r) { F.e.r = f.sheetRows - 1; if(F.e.r > c.e.r) F.e.r = c.e.r; if(F.e.r < F.s.r) F.s.r = F.e.r; if(F.e.c > c.e.c) F.e.c = c.e.c; if(F.e.c < F.s.c) F.s.c = F.e.c;
				o["!fullref"] = o["!ref"];
				o["!ref"] = ct(F) } } if(S.length > 0) o["!merges"] = S; if(R.length > 0) o["!cols"] = R; if(D.length > 0) o["!rows"] = D; return o }

	function ud(e, r, t, a, n, i) { if(r.v === undefined) return ""; var s = ""; switch(r.t) {
			case "b":
				s = r.v ? "1" : "0"; break;
			case "d":
				r = ne(r);
				r.z = r.z || y._table[14];
				r.v = Q(te(r.v));
				r.t = "n"; break;
			case "n":
				;
			case "e":
				s = "" + r.v; break;
			default:
				s = r.v; break; } var f = { r: t, c: a };
		f.s = Kh(n.cellXfs, r, n); if(r.l) i["!links"].push([ot(f), r.l]); if(r.c) i["!comments"].push([ot(f), r.c]); switch(r.t) {
			case "s":
				;
			case "str":
				if(n.bookSST) { s = Xh(n.Strings, r.v);
					f.t = "s";
					f.v = s;
					Gr(e, "BrtCellIsst", Uu(r, f)) } else { f.t = "str";
					Gr(e, "BrtCellSt", Gu(r, f)) } return;
			case "n":
				if(r.v == (r.v | 0) && r.v > -1e3 && r.v < 1e3) Gr(e, "BrtCellRk", zu(r, f));
				else Gr(e, "BrtCellReal", Wu(r, f)); return;
			case "b":
				f.t = "b";
				Gr(e, "BrtCellBool", Nu(r, f)); return;
			case "e":
				f.t = "e"; break; } Gr(e, "BrtCellBlank", Fu(r, f)) }

	function dd(e, r, t, a) { var n = ht(r["!ref"] || "A1"),
			i, s = "",
			f = [];
		Gr(e, "BrtBeginSheetData"); var o = Array.isArray(r); var l = n.e.r; if(r["!rows"]) l = Math.max(n.e.r, r["!rows"].length - 1); for(var c = n.s.r; c <= l; ++c) { s = qr(c);
			Tu(e, r, n, c); if(c <= n.e.r)
				for(var h = n.s.c; h <= n.e.c; ++h) { if(c === n.s.r) f[h] = at(h);
					i = f[h] + s; var u = o ? (r[c] || [])[h] : r[i]; if(!u) continue;
					ud(e, u, c, h, a, r) } } Gr(e, "BrtEndSheetData") }

	function pd(e, r) { if(!r || !r["!merges"]) return;
		Gr(e, "BrtBeginMergeCells", Ju(r["!merges"].length));
		r["!merges"].forEach(function(r) { Gr(e, "BrtMergeCell", Qu(r)) });
		Gr(e, "BrtEndMergeCells") }

	function vd(e, r) { if(!r || !r["!cols"]) return;
		Gr(e, "BrtBeginColInfos");
		r["!cols"].forEach(function(r, t) { if(r) Gr(e, "BrtColInfo", ad(t, r)) });
		Gr(e, "BrtEndColInfos") }

	function gd(e, r) { if(!r || !r["!ref"]) return;
		Gr(e, "BrtBeginCellIgnoreECs");
		Gr(e, "BrtCellIgnoreEC", ld(ht(r["!ref"])));
		Gr(e, "BrtEndCellIgnoreECs") }

	function md(e, r, t) { r["!links"].forEach(function(r) { if(!r[1].Target) return; var a = Oa(t, -1, r[1].Target.replace(/#.*$/, ""), xa.HLINK);
			Gr(e, "BrtHLink", ed(r, a)) });
		delete r["!links"] }

	function bd(e, r, t, a) { if(r["!comments"].length > 0) { var n = Oa(a, -1, "../drawings/vmlDrawing" + (t + 1) + ".vml", xa.VML);
			Gr(e, "BrtLegacyDrawing", Ft("rId" + n));
			r["!legacy"] = n } }

	function Cd(e, r) { if(!r["!autofilter"]) return;
		Gr(e, "BrtBeginAFilter", Ht(ht(r["!autofilter"].ref)));
		Gr(e, "BrtEndAFilter") }

	function wd(e, r, t) { Gr(e, "BrtBeginWsViews"); { Gr(e, "BrtBeginWsView", od(r, t));
			Gr(e, "BrtEndWsView") } Gr(e, "BrtEndWsViews") }

	function Ed() {}

	function kd(e, r) { if(!r["!protect"]) return;
		Gr(e, "BrtSheetProtection", cd(r["!protect"])) }

	function Sd(e, r, t, a) { var n = Xr(); var i = t.SheetNames[e],
			s = t.Sheets[i] || {}; var f = i; try { if(t && t.Workbook) f = t.Workbook.Sheets[e].CodeName || f } catch(o) {} var l = ht(s["!ref"] || "A1"); if(l.e.c > 16383 || l.e.r > 1048575) { if(r.WTF) throw new Error("Range " + (s["!ref"] || "A1") + " exceeds format limit A1:XFD1048576");
			l.e.c = Math.min(l.e.c, 16383);
			l.e.r = Math.min(l.e.c, 1048575) } s["!links"] = [];
		s["!comments"] = [];
		Gr(n, "BrtBeginSheet"); if(t.vbaraw) Gr(n, "BrtWsProp", Du(f));
		Gr(n, "BrtWsDim", yu(l));
		wd(n, s, t.Workbook);
		Ed(n, s);
		vd(n, s, e, r, t);
		dd(n, s, e, r, t);
		kd(n, s);
		Cd(n, s);
		pd(n, s);
		md(n, s, a); if(s["!margins"]) Gr(n, "BrtMargins", sd(s["!margins"]));
		gd(n, s);
		bd(n, s, e, a);
		Gr(n, "BrtEndSheet"); return n.end() }

	function Ad(e) { var r = [];
		(e.match(/<c:pt idx="(\d*)">(.*?)<\/c:pt>/gm) || []).forEach(function(e) { var t = e.match(/<c:pt idx="(\d*?)"><c:v>(.*)<\/c:v><\/c:pt>/); if(!t) return;
			r[+t[1]] = +t[2] }); var t = ye((e.match(/<c:formatCode>([\s\S]*?)<\/c:formatCode>/) || ["", "General"])[1]); return [r, t] }

	function _d(e, r, t, a, n, i) { var s = i || { "!type": "chart" }; if(!e) return i; var f = 0,
			o = 0,
			l = "A"; var c = { s: { r: 2e6, c: 2e6 }, e: { r: 0, c: 0 } };
		(e.match(/<c:numCache>[\s\S]*?<\/c:numCache>/gm) || []).forEach(function(e) { var r = Ad(e);
			c.s.r = c.s.c = 0;
			c.e.c = f;
			l = at(f);
			r[0].forEach(function(e, t) { s[l + qr(t)] = { t: "n", v: e, z: r[1] };
				o = t }); if(c.e.r < o) c.e.r = o;++f }); if(f > 0) s["!ref"] = ct(c); return s } xa.CS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/chartsheet";
	var Bd = er("chartsheet", null, { xmlns: ar.main[0], "xmlns:r": ar.r });

	function Td(e, r, t, a, n) { if(!e) return e; if(!a) a = { "!id": {} }; var i = { "!type": "chart", "!chart": null, "!rel": "" }; var s; var f = e.match(nu); if(f) ou(f[0], i, n, t); if(s = e.match(/drawing r:id="(.*?)"/)) i["!rel"] = s[1]; if(a["!id"][i["!rel"]]) i["!chart"] = a["!id"][i["!rel"]]; return i }

	function xd(e, r, t, a) { var n = [we, Bd];
		n[n.length] = er("drawing", null, { "r:id": "rId1" });
		Oa(a, -1, "../drawings/drawing" + (e + 1) + ".xml", xa.DRAW); if(n.length > 2) { n[n.length] = "</chartsheet>";
			n[1] = n[1].replace("/>", ">") } return n.join("") }

	function yd(e, r) { e.l += 10; var t = bt(e, r - 10); return { name: t } }

	function Id(e, r, t, a, n) { if(!e) return e; if(!a) a = { "!id": {} }; var i = { "!type": "chart", "!chart": null, "!rel": "" }; var s = []; var f = false;
		zr(e, function o(e, a, l) { switch(l) {
				case 550:
					i["!rel"] = e; break;
				case 651:
					if(!n.Sheets[t]) n.Sheets[t] = {}; if(e.name) n.Sheets[t].CodeName = e.name; break;
				case 562:
					;
				case 652:
					;
				case 669:
					;
				case 679:
					;
				case 551:
					;
				case 552:
					;
				case 476:
					;
				case 3072:
					break;
				case 35:
					f = true; break;
				case 36:
					f = false; break;
				case 37:
					s.push(a); break;
				case 38:
					s.pop(); break;
				default:
					if((a || "").indexOf("Begin") > 0) s.push(a);
					else if((a || "").indexOf("End") > 0) s.pop();
					else if(!f || r.WTF) throw new Error("Unexpected record " + l + " " + a); } }, r); if(a["!id"][i["!rel"]]) i["!chart"] = a["!id"][i["!rel"]]; return i }

	function Rd() { var e = Xr();
		Gr(e, "BrtBeginSheet");
		Gr(e, "BrtEndSheet"); return e.end() }
	var Dd = [
		["allowRefreshQuery", false, "bool"],
		["autoCompressPictures", true, "bool"],
		["backupFile", false, "bool"],
		["checkCompatibility", false, "bool"],
		["CodeName", ""],
		["date1904", false, "bool"],
		["defaultThemeVersion", 0, "int"],
		["filterPrivacy", false, "bool"],
		["hidePivotFieldList", false, "bool"],
		["promptedSolutions", false, "bool"],
		["publishItems", false, "bool"],
		["refreshAllConnections", false, "bool"],
		["saveExternalLinkValues", true, "bool"],
		["showBorderUnselectedTables", true, "bool"],
		["showInkAnnotation", true, "bool"],
		["showObjects", "all"],
		["showPivotChartFilter", false, "bool"],
		["updateLinks", "userSet"]
	];
	var Od = [
		["activeTab", 0, "int"],
		["autoFilterDateGrouping", true, "bool"],
		["firstSheet", 0, "int"],
		["minimized", false, "bool"],
		["showHorizontalScroll", true, "bool"],
		["showSheetTabs", true, "bool"],
		["showVerticalScroll", true, "bool"],
		["tabRatio", 600, "int"],
		["visibility", "visible"]
	];
	var Fd = [];
	var Pd = [
		["calcCompleted", "true"],
		["calcMode", "auto"],
		["calcOnSave", "true"],
		["concurrentCalc", "true"],
		["fullCalcOnLoad", "false"],
		["fullPrecision", "true"],
		["iterate", "false"],
		["iterateCount", "100"],
		["iterateDelta", "0.001"],
		["refMode", "A1"]
	];

	function Nd(e, r) { for(var t = 0; t != e.length; ++t) { var a = e[t]; for(var n = 0; n != r.length; ++n) { var i = r[n]; if(a[i[0]] == null) a[i[0]] = i[1];
				else switch(i[2]) {
					case "bool":
						if(typeof a[i[0]] == "string") a[i[0]] = Ue(a[i[0]]); break;
					case "int":
						if(typeof a[i[0]] == "string") a[i[0]] = parseInt(a[i[0]], 10); break; } } } }

	function Ld(e, r) { for(var t = 0; t != r.length; ++t) { var a = r[t]; if(e[a[0]] == null) e[a[0]] = a[1];
			else switch(a[2]) {
				case "bool":
					if(typeof e[a[0]] == "string") e[a[0]] = Ue(e[a[0]]); break;
				case "int":
					if(typeof e[a[0]] == "string") e[a[0]] = parseInt(e[a[0]], 10); break; } } }

	function Md(e) { Ld(e.WBProps, Dd);
		Ld(e.CalcPr, Pd);
		Nd(e.WBView, Od);
		Nd(e.Sheets, Fd);
		zh.date1904 = Ue(e.WBProps.date1904) }

	function Ud(e) { if(!e.Workbook) return "false"; if(!e.Workbook.WBProps) return "false"; return Ue(e.Workbook.WBProps.date1904) ? "true" : "false" }
	var Hd = "][*?/\\".split("");

	function Wd(e, r) { if(e.length > 31) { if(r) return false; throw new Error("Sheet names cannot exceed 31 chars") } var t = true;
		Hd.forEach(function(a) { if(e.indexOf(a) == -1) return; if(!r) throw new Error("Sheet name cannot contain : \\ / ? * [ ]");
			t = false }); return t }

	function Vd(e, r, t) { e.forEach(function(a, n) { Wd(a); for(var i = 0; i < n; ++i)
				if(a == e[i]) throw new Error("Duplicate Sheet Name: " + a); if(t) { var s = r && r[n] && r[n].CodeName || a; if(s.charCodeAt(0) == 95 && s.length > 22) throw new Error("Bad Code Name: Worksheet" + s) } }) }

	function zd(e) { if(!e || !e.SheetNames || !e.Sheets) throw new Error("Invalid Workbook"); if(!e.SheetNames.length) throw new Error("Workbook is empty"); var r = e.Workbook && e.Workbook.Sheets || [];
		Vd(e.SheetNames, r, !!e.vbaraw); for(var t = 0; t < e.SheetNames.length; ++t) $h(e.Sheets[e.SheetNames[t]], e.SheetNames[t], t) }
	var Xd = /<\w+:workbook/;

	function Gd(e, r) { if(!e) throw new Error("Could not find file"); var t = { AppVersion: {}, WBProps: {}, WBView: [], Sheets: [], CalcPr: {}, Names: [], xmlns: "" }; var a = false,
			n = "xmlns"; var i = {},
			s = 0;
		e.replace(ke, function f(o, l) { var c = _e(o); switch(Be(c[0])) {
				case "<?xml":
					break;
				case "<workbook":
					if(o.match(Xd)) n = "xmlns" + o.match(/<(\w+):/)[1];
					t.xmlns = c[n]; break;
				case "</workbook>":
					break;
				case "<fileVersion":
					delete c[0];
					t.AppVersion = c; break;
				case "<fileVersion/>":
					;
				case "</fileVersion>":
					break;
				case "<fileSharing":
					;
				case "<fileSharing/>":
					break;
				case "<workbookPr":
					;
				case "<workbookPr/>":
					Dd.forEach(function(e) { if(c[e[0]] == null) return; switch(e[2]) {
							case "bool":
								t.WBProps[e[0]] = Ue(c[e[0]]); break;
							case "int":
								t.WBProps[e[0]] = parseInt(c[e[0]], 10); break;
							default:
								t.WBProps[e[0]] = c[e[0]]; } }); if(c.codeName) t.WBProps.CodeName = c.codeName; break;
				case "</workbookPr>":
					break;
				case "<workbookProtection":
					break;
				case "<workbookProtection/>":
					break;
				case "<bookViews":
					;
				case "<bookViews>":
					;
				case "</bookViews>":
					break;
				case "<workbookView":
					;
				case "<workbookView/>":
					delete c[0];
					t.WBView.push(c); break;
				case "</workbookView>":
					break;
				case "<sheets":
					;
				case "<sheets>":
					;
				case "</sheets>":
					break;
				case "<sheet":
					switch(c.state) {
						case "hidden":
							c.Hidden = 1; break;
						case "veryHidden":
							c.Hidden = 2; break;
						default:
							c.Hidden = 0; } delete c.state;
					c.name = ye(He(c.name));
					delete c[0];
					t.Sheets.push(c); break;
				case "</sheet>":
					break;
				case "<functionGroups":
					;
				case "<functionGroups/>":
					break;
				case "<functionGroup":
					break;
				case "<externalReferences":
					;
				case "</externalReferences>":
					;
				case "<externalReferences>":
					break;
				case "<externalReference":
					break;
				case "<definedNames/>":
					break;
				case "<definedNames>":
					;
				case "<definedNames":
					a = true; break;
				case "</definedNames>":
					a = false; break;
				case "<definedName":
					{ i = {};i.Name = c.name; if(c.comment) i.Comment = c.comment; if(c.localSheetId) i.Sheet = +c.localSheetId;s = l + o.length } break;
				case "</definedName>":
					{ i.Ref = e.slice(s, l);t.Names.push(i) } break;
				case "<definedName/>":
					break;
				case "<calcPr":
					delete c[0];
					t.CalcPr = c; break;
				case "<calcPr/>":
					delete c[0];
					t.CalcPr = c; break;
				case "</calcPr>":
					break;
				case "<oleSize":
					break;
				case "<customWorkbookViews>":
					;
				case "</customWorkbookViews>":
					;
				case "<customWorkbookViews":
					break;
				case "<customWorkbookView":
					;
				case "</customWorkbookView>":
					break;
				case "<pivotCaches>":
					;
				case "</pivotCaches>":
					;
				case "<pivotCaches":
					break;
				case "<pivotCache":
					break;
				case "<smartTagPr":
					;
				case "<smartTagPr/>":
					break;
				case "<smartTagTypes":
					;
				case "<smartTagTypes>":
					;
				case "</smartTagTypes>":
					break;
				case "<smartTagType":
					break;
				case "<webPublishing":
					;
				case "<webPublishing/>":
					break;
				case "<fileRecoveryPr":
					;
				case "<fileRecoveryPr/>":
					break;
				case "<webPublishObjects>":
					;
				case "<webPublishObjects":
					;
				case "</webPublishObjects>":
					break;
				case "<webPublishObject":
					break;
				case "<extLst":
					;
				case "<extLst>":
					;
				case "</extLst>":
					;
				case "<extLst/>":
					break;
				case "<ext":
					a = true; break;
				case "</ext>":
					a = false; break;
				case "<ArchID":
					break;
				case "<AlternateContent":
					;
				case "<AlternateContent>":
					a = true; break;
				case "</AlternateContent>":
					a = false; break;
				case "<revisionPtr":
					break;
				default:
					if(!a && r.WTF) throw new Error("unrecognized " + c[0] + " in workbook"); } return o }); if(ar.main.indexOf(t.xmlns) === -1) throw new Error("Unknown Namespace: " + t.xmlns);
		Md(t); return t }
	var jd = er("workbook", null, { xmlns: ar.main[0], "xmlns:r": ar.r });

	function Kd(e) { var r = [we];
		r[r.length] = jd; var t = e.Workbook && (e.Workbook.Names || []).length > 0; var a = { codeName: "ThisWorkbook" }; if(e.Workbook && e.Workbook.WBProps) { Dd.forEach(function(r) { if(e.Workbook.WBProps[r[0]] == null) return; if(e.Workbook.WBProps[r[0]] == r[1]) return;
				a[r[0]] = e.Workbook.WBProps[r[0]] }); if(e.Workbook.WBProps.CodeName) { a.codeName = e.Workbook.WBProps.CodeName;
				delete a.CodeName } } r[r.length] = er("workbookPr", null, a);
		r[r.length] = "<sheets>"; var n = e.Workbook && e.Workbook.Sheets || []; for(var i = 0; i != e.SheetNames.length; ++i) { var s = { name: De(e.SheetNames[i].slice(0, 31)) };
			s.sheetId = "" + (i + 1);
			s["r:id"] = "rId" + (i + 1); if(n[i]) switch(n[i].Hidden) {
				case 1:
					s.state = "hidden"; break;
				case 2:
					s.state = "veryHidden"; break; } r[r.length] = er("sheet", null, s) } r[r.length] = "</sheets>"; if(t) { r[r.length] = "<definedNames>"; if(e.Workbook && e.Workbook.Names) e.Workbook.Names.forEach(function(e) { var t = { name: e.Name }; if(e.Comment) t.comment = e.Comment; if(e.Sheet != null) t.localSheetId = "" + e.Sheet; if(!e.Ref) return;
				r[r.length] = er("definedName", String(e.Ref), t) });
			r[r.length] = "</definedNames>" } if(r.length > 2) { r[r.length] = "</workbook>";
			r[1] = r[1].replace("/>", ">") } return r.join("") }

	function Yd(e, r) { var t = {};
		t.Hidden = e._R(4);
		t.iTabID = e._R(4);
		t.strRelID = Ot(e, r - 8);
		t.name = bt(e); return t }

	function $d(e, r) { if(!r) r = Vr(127);
		r._W(4, e.Hidden);
		r._W(4, e.iTabID);
		Ft(e.strRelID, r);
		Ct(e.name.slice(0, 31), r); return r.length > r.l ? r.slice(0, r.l) : r }

	function Zd(e, r) { var t = {}; var a = e._R(4);
		t.defaultThemeVersion = e._R(4); var n = r > 8 ? bt(e) : ""; if(n.length > 0) t.CodeName = n;
		t.autoCompressPictures = !!(a & 65536);
		t.backupFile = !!(a & 64);
		t.checkCompatibility = !!(a & 4096);
		t.date1904 = !!(a & 1);
		t.filterPrivacy = !!(a & 8);
		t.hidePivotFieldList = !!(a & 1024);
		t.promptedSolutions = !!(a & 16);
		t.publishItems = !!(a & 2048);
		t.refreshAllConnections = !!(a & 262144);
		t.saveExternalLinkValues = !!(a & 128);
		t.showBorderUnselectedTables = !!(a & 4);
		t.showInkAnnotation = !!(a & 32);
		t.showObjects = ["all", "placeholders", "none"][a >> 13 & 3];
		t.showPivotChartFilter = !!(a & 32768);
		t.updateLinks = ["userSet", "never", "always"][a >> 8 & 3]; return t }

	function Qd(e, r) { if(!r) r = Vr(72); var t = 0; if(e) { if(e.filterPrivacy) t |= 8 } r._W(4, t);
		r._W(4, 0);
		yt(e && e.CodeName || "ThisWorkbook", r); return r.slice(0, r.l) }

	function Jd(e, r) {
		var t = {};
		e._R(4);
		t.ArchID = e._R(4);
		e.l += r - 8;
		return t
	}

	function qd(e, r, t) { var a = e.l + r;
		e.l += 4;
		e.l += 1; var n = e._R(4); var i = Dt(e); var s = Dh(e, 0, t); var f = It(e);
		e.l = a; var o = { Name: i, Ptg: s }; if(n < 268435455) o.Sheet = n; if(f) o.Comment = f; return o }

	function ep(e, r) { var t = { AppVersion: {}, WBProps: {}, WBView: [], Sheets: [], CalcPr: {}, xmlns: "" }; var a = false; if(!r) r = {};
		r.biff = 12; var n = []; var i = [
			[]
		];
		i.SheetNames = [];
		i.XTI = [];
		zr(e, function s(e, f, o) { switch(o) {
				case 156:
					i.SheetNames.push(e.name);
					t.Sheets.push(e); break;
				case 153:
					t.WBProps = e; break;
				case 39:
					if(e.Sheet != null) r.SID = e.Sheet;
					e.Ref = kh(e.Ptg, null, null, i, r);
					delete r.SID;
					delete e.Ptg;
					n.push(e); break;
				case 1036:
					break;
				case 357:
					;
				case 358:
					;
				case 355:
					;
				case 667:
					if(!i[0].length) i[0] = [o, e];
					else i.push([o, e]);
					i[i.length - 1].XTI = []; break;
				case 362:
					if(i.length === 0) { i[0] = [];
						i[0].XTI = [] } i[i.length - 1].XTI = i[i.length - 1].XTI.concat(e);
					i.XTI = i.XTI.concat(e); break;
				case 361:
					break;
				case 3072:
					;
				case 3073:
					;
				case 2071:
					;
				case 534:
					;
				case 677:
					;
				case 158:
					;
				case 157:
					;
				case 610:
					;
				case 2050:
					;
				case 155:
					;
				case 548:
					;
				case 676:
					;
				case 128:
					;
				case 665:
					;
				case 2128:
					;
				case 2125:
					;
				case 549:
					;
				case 2053:
					;
				case 596:
					;
				case 2076:
					;
				case 2075:
					;
				case 2082:
					;
				case 397:
					;
				case 154:
					;
				case 1117:
					;
				case 553:
					;
				case 2091:
					break;
				case 35:
					a = true; break;
				case 36:
					a = false; break;
				case 37:
					break;
				case 38:
					break;
				case 16:
					break;
				default:
					if((f || "").indexOf("Begin") > 0) {} else if((f || "").indexOf("End") > 0) {} else if(!a || r.WTF) throw new Error("Unexpected record " + o + " " + f); } }, r);
		Md(t);
		t.Names = n;
		t.supbooks = i; return t }

	function rp(e, r) { Gr(e, "BrtBeginBundleShs"); for(var t = 0; t != r.SheetNames.length; ++t) { var a = r.Workbook && r.Workbook.Sheets && r.Workbook.Sheets[t] && r.Workbook.Sheets[t].Hidden || 0; var n = { Hidden: a, iTabID: t + 1, strRelID: "rId" + (t + 1), name: r.SheetNames[t] };
			Gr(e, "BrtBundleSh", $d(n)) } Gr(e, "BrtEndBundleShs") }

	function tp(e, t) { if(!t) t = Vr(127); for(var a = 0; a != 4; ++a) t._W(4, 0);
		Ct("SheetJS", t);
		Ct(r.version, t);
		Ct(r.version, t);
		Ct("7262", t);
		t.length = t.l; return t.length > t.l ? t.slice(0, t.l) : t }

	function ap(e, r) { if(!r) r = Vr(29);
		r._W(-4, 0);
		r._W(-4, 460);
		r._W(4, 28800);
		r._W(4, 17600);
		r._W(4, 500);
		r._W(4, e);
		r._W(4, e); var t = 120;
		r._W(1, t); return r.length > r.l ? r.slice(0, r.l) : r }

	function np(e, r) { if(!r.Workbook || !r.Workbook.Sheets) return; var t = r.Workbook.Sheets; var a = 0,
			n = -1,
			i = -1; for(; a < t.length; ++a) { if(!t[a] || !t[a].Hidden && n == -1) n = a;
			else if(t[a].Hidden == 1 && i == -1) i = a } if(i > n) return;
		Gr(e, "BrtBeginBookViews");
		Gr(e, "BrtBookView", ap(n));
		Gr(e, "BrtEndBookViews") }

	function ip(e, r) { var t = Xr();
		Gr(t, "BrtBeginBook");
		Gr(t, "BrtFileVersion", tp());
		Gr(t, "BrtWbProp", Qd(e.Workbook && e.Workbook.WBProps || null));
		np(t, e, r);
		rp(t, e, r);
		Gr(t, "BrtEndBook"); return t.end() }

	function sp(e, r, t) { if(r.slice(-4) === ".bin") return ep(e, t); return Gd(e, t) }

	function fp(e, r, t, a, n, i, s, f) { if(r.slice(-4) === ".bin") return hd(e, a, t, n, i, s, f); return su(e, a, t, n, i, s, f) }

	function op(e, r, t, a, n, i, s, f) { if(r.slice(-4) === ".bin") return Id(e, a, t, n, i, s, f); return Td(e, a, t, n, i, s, f) }

	function lp(e, r, t, a, n, i, s, f) { if(r.slice(-4) === ".bin") return Ul(e, a, t, n, i, s, f); return Hl(e, a, t, n, i, s, f) }

	function cp(e, r, t, a, n, i, s, f) { if(r.slice(-4) === ".bin") return Ll(e, a, t, n, i, s, f); return Ml(e, a, t, n, i, s, f) }

	function hp(e, r, t, a) { if(r.slice(-4) === ".bin") return Mo(e, t, a); return wo(e, t, a) }

	function up(e, r, t) { return nl(e, t) }

	function dp(e, r, t) { if(r.slice(-4) === ".bin") return gf(e, t); return uf(e, t) }

	function pp(e, r, t) { if(r.slice(-4) === ".bin") return Rl(e, t); return Al(e, t) }

	function vp(e, r, t) { if(r.slice(-4) === ".bin") return gl(e, r, t); return pl(e, r, t) }

	function gp(e, r, t) { if(r.slice(-4) === ".bin") return bl(e, r, t); return ml(e, r, t) }

	function mp(e, r, t) { return(r.slice(-4) === ".bin" ? ip : Kd)(e, t) }

	function bp(e, r, t, a, n) { return(r.slice(-4) === ".bin" ? Sd : Au)(e, t, a, n) }

	function Cp(e, r, t, a, n) { return(r.slice(-4) === ".bin" ? Rd : xd)(e, t, a, n) }

	function wp(e, r, t) { return(r.slice(-4) === ".bin" ? $o : ko)(e, t) }

	function Ep(e, r, t) { return(r.slice(-4) === ".bin" ? Cf : pf)(e, t) }

	function kp(e, r, t) { return(r.slice(-4) === ".bin" ? Dl : Bl)(e, t) }
	var Sp = /([\w:]+)=((?:")([^"]*)(?:")|(?:')([^']*)(?:'))/g;
	var Ap = /([\w:]+)=((?:")(?:[^"]*)(?:")|(?:')(?:[^']*)(?:'))/;
	var _p = function(e) { return String.fromCharCode(e) };

	function Bp(e, r) { var t = e.split(/\s+/); var a = []; if(!r) a[0] = t[0]; if(t.length === 1) return a; var n = e.match(Sp),
			i, s, f, o; if(n)
			for(o = 0; o != n.length; ++o) { i = n[o].match(Ap); if((s = i[1].indexOf(":")) === -1) a[i[1]] = i[2].slice(1, i[2].length - 1);
				else { if(i[1].slice(0, 6) === "xmlns:") f = "xmlns" + i[1].slice(6);
					else f = i[1].slice(s + 1);
					a[f] = i[2].slice(1, i[2].length - 1) } }
		return a }

	function Tp(e) { var r = e.split(/\s+/); var t = {}; if(r.length === 1) return t; var a = e.match(Sp),
			n, i, s, f; if(a)
			for(f = 0; f != a.length; ++f) { n = a[f].match(Ap); if((i = n[1].indexOf(":")) === -1) t[n[1]] = n[2].slice(1, n[2].length - 1);
				else { if(n[1].slice(0, 6) === "xmlns:") s = "xmlns" + n[1].slice(6);
					else s = n[1].slice(i + 1);
					t[s] = n[2].slice(1, n[2].length - 1) } }
		return t }

	function xp(e, r) { var t = R[e] || ye(e); if(t === "General") return y._general(r); return y.format(t, r) }

	function yp(e, r, t, a) { var n = a; switch((t[0].match(/dt:dt="([\w.]+)"/) || ["", ""])[1]) {
			case "boolean":
				n = Ue(a); break;
			case "i2":
				;
			case "int":
				n = parseInt(a, 10); break;
			case "r4":
				;
			case "float":
				n = parseFloat(a); break;
			case "date":
				;
			case "dateTime.tz":
				n = te(a); break;
			case "i8":
				;
			case "string":
				;
			case "fixed":
				;
			case "uuid":
				;
			case "bin.base64":
				break;
			default:
				throw new Error("bad custprop:" + t[0]); } e[ye(r)] = n }

	function Ip(e, r, t) { if(e.t === "z") return; if(!t || t.cellText !== false) try { if(e.t === "e") { e.w = e.w || zt[e.v] } else if(r === "General") { if(e.t === "n") { if((e.v | 0) === e.v) e.w = y._general_int(e.v);
					else e.w = y._general_num(e.v) } else e.w = y._general(e.v) } else e.w = xp(r || "General", e.v) } catch(a) { if(t.WTF) throw a }
		try { var n = R[r] || r || "General"; if(t.cellNF) e.z = n; if(t.cellDates && e.t == "n" && y.is_date(n)) { var i = y.parse_date_code(e.v); if(i) { e.t = "d";
					e.v = new Date(i.y, i.m - 1, i.d, i.H, i.M, i.S, i.u) } } } catch(a) { if(t.WTF) throw a } }

	function Rp(e, r, t) { if(t.cellStyles) { if(r.Interior) { var a = r.Interior; if(a.Pattern) a.patternType = lo[a.Pattern] || a.Pattern } } e[r.ID] = r }

	function Dp(e, r, t, a, n, i, s, f, o, l) { var c = "General",
			h = a.StyleID,
			u = {};
		l = l || {}; var d = []; var p = 0; if(h === undefined && f) h = f.StyleID; if(h === undefined && s) h = s.StyleID; while(i[h] !== undefined) { if(i[h].nf) c = i[h].nf; if(i[h].Interior) d.push(i[h].Interior); if(!i[h].Parent) break;
			h = i[h].Parent } switch(t.Type) {
			case "Boolean":
				a.t = "b";
				a.v = Ue(e); break;
			case "String":
				a.t = "s";
				a.r = Le(ye(e));
				a.v = e.indexOf("<") > -1 ? ye(r) : a.r; break;
			case "DateTime":
				if(e.slice(-1) != "Z") e += "Z";
				a.v = (te(e) - new Date(Date.UTC(1899, 11, 30))) / (24 * 60 * 60 * 1e3); if(a.v !== a.v) a.v = ye(e);
				else if(a.v < 60) a.v = a.v - 1; if(!c || c == "General") c = "yyyy-mm-dd";
			case "Number":
				if(a.v === undefined) a.v = +e; if(!a.t) a.t = "n"; break;
			case "Error":
				a.t = "e";
				a.v = Xt[e]; if(l.cellText !== false) a.w = e; break;
			default:
				a.t = "s";
				a.v = Le(r || e); break; } Ip(a, c, l); if(l.cellFormula !== false) { if(a.Formula) { var v = ye(a.Formula); if(v.charCodeAt(0) == 61) v = v.slice(1);
				a.f = Wl(v, n);
				delete a.Formula; if(a.ArrayRange == "RC") a.F = Wl("RC:RC", n);
				else if(a.ArrayRange) { a.F = Wl(a.ArrayRange, n);
					o.push([ht(a.F), a.F]) } } else { for(p = 0; p < o.length; ++p)
					if(n.r >= o[p][0].s.r && n.r <= o[p][0].e.r)
						if(n.c >= o[p][0].s.c && n.c <= o[p][0].e.c) a.F = o[p][1] } } if(l.cellStyles) { d.forEach(function(e) { if(!u.patternType && e.patternType) u.patternType = e.patternType });
			a.s = u } if(a.StyleID !== undefined) a.ixfe = a.StyleID }

	function Op(e) { e.t = e.v || "";
		e.t = e.t.replace(/\r\n/g, "\n").replace(/\r/g, "\n");
		e.v = e.w = e.ixfe = undefined }

	function Fp(e) { if(C && Buffer.isBuffer(e)) return e.toString("utf8"); if(typeof e === "string") return e; if(typeof Uint8Array !== "undefined" && e instanceof Uint8Array) return He(S(_(e))); throw new Error("Bad input format: expected Buffer or string") }
	var Pp = /<(\/?)([^\s?>!\/:]*:|)([^\s?>:\/]+)[^>]*>/gm;

	function Np(e, r) { var t = r || {};
		I(y); var a = p(Fp(e)); if(t.type == "binary" || t.type == "array" || t.type == "base64") { if(typeof cptable !== "undefined") a = cptable.utils.decode(65001, h(a));
			else a = He(a) } var n = a.slice(0, 1024).toLowerCase(),
			i = false; if(n.indexOf("<?xml") == -1)["html", "table", "head", "meta", "script", "style", "div"].forEach(function(e) { if(n.indexOf("<" + e) >= 0) i = true }); if(i) return xv.to_workbook(a, t); var s; var f = [],
			o; if(g != null && t.dense == null) t.dense = g; var l = {},
			c = [],
			u = t.dense ? [] : {},
			d = ""; var v = {},
			m = {},
			b = {}; var C = Bp('<Data ss:Type="String">'),
			w = 0; var E = 0,
			k = 0; var S = { s: { r: 2e6, c: 2e6 }, e: { r: 0, c: 0 } }; var A = {},
			_ = {}; var B = "",
			T = 0; var x = []; var D = {},
			O = {},
			F = 0,
			P = []; var N = [],
			L = {}; var M = [],
			U, H = false; var W = []; var V = [],
			z = {},
			X = 0,
			G = 0; var j = { Sheets: [], WBProps: { date1904: false } },
			K = {};
		Pp.lastIndex = 0;
		a = a.replace(/<!--([\s\S]*?)-->/gm, ""); while(s = Pp.exec(a)) switch(s[3]) {
			case "Data":
				if(f[f.length - 1][1]) break; if(s[1] === "/") Dp(a.slice(w, s.index), B, C, f[f.length - 1][0] == "Comment" ? L : m, { c: E, r: k }, A, M[E], b, W, t);
				else { B = "";
					C = Bp(s[0]);
					w = s.index + s[0].length } break;
			case "Cell":
				if(s[1] === "/") { if(N.length > 0) m.c = N; if((!t.sheetRows || t.sheetRows > k) && m.v !== undefined) { if(t.dense) { if(!u[k]) u[k] = [];
							u[k][E] = m } else u[at(E) + qr(k)] = m } if(m.HRef) { m.l = { Target: m.HRef }; if(m.HRefScreenTip) m.l.Tooltip = m.HRefScreenTip;
						delete m.HRef;
						delete m.HRefScreenTip } if(m.MergeAcross || m.MergeDown) { X = E + (parseInt(m.MergeAcross, 10) | 0);
						G = k + (parseInt(m.MergeDown, 10) | 0);
						x.push({ s: { c: E, r: k }, e: { c: X, r: G } }) } if(!t.sheetStubs) { if(m.MergeAcross) E = X + 1;
						else ++E } else if(m.MergeAcross || m.MergeDown) { for(var Y = E; Y <= X; ++Y) { for(var $ = k; $ <= G; ++$) { if(Y > E || $ > k) { if(t.dense) { if(!u[$]) u[$] = [];
										u[$][Y] = { t: "z" } } else u[at(Y) + qr($)] = { t: "z" } } } } E = X + 1 } else ++E } else { m = Tp(s[0]); if(m.Index) E = +m.Index - 1; if(E < S.s.c) S.s.c = E; if(E > S.e.c) S.e.c = E; if(s[0].slice(-2) === "/>") ++E;
					N = [] } break;
			case "Row":
				if(s[1] === "/" || s[0].slice(-2) === "/>") { if(k < S.s.r) S.s.r = k; if(k > S.e.r) S.e.r = k; if(s[0].slice(-2) === "/>") { b = Bp(s[0]); if(b.Index) k = +b.Index - 1 } E = 0;++k } else { b = Bp(s[0]); if(b.Index) k = +b.Index - 1;
					z = {}; if(b.AutoFitHeight == "0" || b.Height) { z.hpx = parseInt(b.Height, 10);
						z.hpt = fo(z.hpx);
						V[k] = z } if(b.Hidden == "1") { z.hidden = true;
						V[k] = z } } break;
			case "Worksheet":
				if(s[1] === "/") { if((o = f.pop())[0] !== s[3]) throw new Error("Bad state: " + o.join("|"));
					c.push(d); if(S.s.r <= S.e.r && S.s.c <= S.e.c) { u["!ref"] = ct(S); if(t.sheetRows && t.sheetRows <= S.e.r) { u["!fullref"] = u["!ref"];
							S.e.r = t.sheetRows - 1;
							u["!ref"] = ct(S) } } if(x.length) u["!merges"] = x; if(M.length > 0) u["!cols"] = M; if(V.length > 0) u["!rows"] = V;
					l[d] = u } else { S = { s: { r: 2e6, c: 2e6 }, e: { r: 0, c: 0 } };
					k = E = 0;
					f.push([s[3], false]);
					o = Bp(s[0]);
					d = ye(o.Name);
					u = t.dense ? [] : {};
					x = [];
					W = [];
					V = [];
					K = { name: d, Hidden: 0 };
					j.Sheets.push(K) } break;
			case "Table":
				if(s[1] === "/") { if((o = f.pop())[0] !== s[3]) throw new Error("Bad state: " + o.join("|")) } else if(s[0].slice(-2) == "/>") break;
				else { v = Bp(s[0]);
					f.push([s[3], false]);
					M = [];
					H = false } break;
			case "Style":
				if(s[1] === "/") Rp(A, _, t);
				else _ = Bp(s[0]); break;
			case "NumberFormat":
				_.nf = ye(Bp(s[0]).Format || "General"); if(R[_.nf]) _.nf = R[_.nf]; for(var Z = 0; Z != 392; ++Z)
					if(y._table[Z] == _.nf) break; if(Z == 392)
					for(Z = 57; Z != 392; ++Z)
						if(y._table[Z] == null) { y.load(_.nf, Z); break }
				break;
			case "Column":
				if(f[f.length - 1][0] !== "Table") break;
				U = Bp(s[0]); if(U.Hidden) { U.hidden = true;
					delete U.Hidden } if(U.Width) U.wpx = parseInt(U.Width, 10); if(!H && U.wpx > 10) { H = true;
					Jf = $f; for(var Q = 0; Q < M.length; ++Q)
						if(M[Q]) no(M[Q]) } if(H) no(U);
				M[U.Index - 1 || M.length] = U; for(var J = 0; J < +U.Span; ++J) M[M.length] = ne(U); break;
			case "NamedRange":
				if(!j.Names) j.Names = []; var q = _e(s[0]); var ee = { Name: q.Name, Ref: Wl(q.RefersTo.slice(1), { r: 0, c: 0 }) }; if(j.Sheets.length > 0) ee.Sheet = j.Sheets.length - 1;
				j.Names.push(ee); break;
			case "NamedCell":
				break;
			case "B":
				break;
			case "I":
				break;
			case "U":
				break;
			case "S":
				break;
			case "Sub":
				break;
			case "Sup":
				break;
			case "Span":
				break;
			case "Border":
				break;
			case "Alignment":
				break;
			case "Borders":
				break;
			case "Font":
				if(s[0].slice(-2) === "/>") break;
				else if(s[1] === "/") B += a.slice(T, s.index);
				else T = s.index + s[0].length; break;
			case "Interior":
				if(!t.cellStyles) break;
				_.Interior = Bp(s[0]); break;
			case "Protection":
				break;
			case "Author":
				;
			case "Title":
				;
			case "Description":
				;
			case "Created":
				;
			case "Keywords":
				;
			case "Subject":
				;
			case "Category":
				;
			case "Company":
				;
			case "LastAuthor":
				;
			case "LastSaved":
				;
			case "LastPrinted":
				;
			case "Version":
				;
			case "Revision":
				;
			case "TotalTime":
				;
			case "HyperlinkBase":
				;
			case "Manager":
				;
			case "ContentStatus":
				;
			case "Identifier":
				;
			case "Language":
				;
			case "AppName":
				if(s[0].slice(-2) === "/>") break;
				else if(s[1] === "/") sn(D, s[3], a.slice(F, s.index));
				else F = s.index + s[0].length; break;
			case "Paragraphs":
				break;
			case "Styles":
				;
			case "Workbook":
				if(s[1] === "/") { if((o = f.pop())[0] !== s[3]) throw new Error("Bad state: " + o.join("|")) } else f.push([s[3], false]); break;
			case "Comment":
				if(s[1] === "/") { if((o = f.pop())[0] !== s[3]) throw new Error("Bad state: " + o.join("|"));
					Op(L);
					N.push(L) } else { f.push([s[3], false]);
					o = Bp(s[0]);
					L = { a: o.Author } } break;
			case "AutoFilter":
				if(s[1] === "/") { if((o = f.pop())[0] !== s[3]) throw new Error("Bad state: " + o.join("|")) } else if(s[0].charAt(s[0].length - 2) !== "/") { var re = Bp(s[0]);
					u["!autofilter"] = { ref: Wl(re.Range).replace(/\$/g, "") };
					f.push([s[3], true]) } break;
			case "Name":
				break;
			case "ComponentOptions":
				;
			case "DocumentProperties":
				;
			case "CustomDocumentProperties":
				;
			case "OfficeDocumentSettings":
				;
			case "PivotTable":
				;
			case "PivotCache":
				;
			case "Names":
				;
			case "MapInfo":
				;
			case "PageBreaks":
				;
			case "QueryTable":
				;
			case "DataValidation":
				;
			case "Sorting":
				;
			case "Schema":
				;
			case "data":
				;
			case "ConditionalFormatting":
				;
			case "SmartTagType":
				;
			case "SmartTags":
				;
			case "ExcelWorkbook":
				;
			case "WorkbookOptions":
				;
			case "WorksheetOptions":
				if(s[1] === "/") { if((o = f.pop())[0] !== s[3]) throw new Error("Bad state: " + o.join("|")) } else if(s[0].charAt(s[0].length - 2) !== "/") f.push([s[3], true]); break;
			default:
				if(f.length == 0 && s[3] == "document") return Ov(a, t); if(f.length == 0 && s[3] == "UOF") return Ov(a, t); var te = true; switch(f[f.length - 1][0]) {
					case "OfficeDocumentSettings":
						switch(s[3]) {
							case "AllowPNG":
								break;
							case "RemovePersonalInformation":
								break;
							case "DownloadComponents":
								break;
							case "LocationOfComponents":
								break;
							case "Colors":
								break;
							case "Color":
								break;
							case "Index":
								break;
							case "RGB":
								break;
							case "PixelsPerInch":
								break;
							case "TargetScreenSize":
								break;
							case "ReadOnlyRecommended":
								break;
							default:
								te = false; } break;
					case "ComponentOptions":
						switch(s[3]) {
							case "Toolbar":
								break;
							case "HideOfficeLogo":
								break;
							case "SpreadsheetAutoFit":
								break;
							case "Label":
								break;
							case "Caption":
								break;
							case "MaxHeight":
								break;
							case "MaxWidth":
								break;
							case "NextSheetNumber":
								break;
							default:
								te = false; } break;
					case "ExcelWorkbook":
						switch(s[3]) {
							case "Date1904":
								j.WBProps.date1904 = true; break;
							case "WindowHeight":
								break;
							case "WindowWidth":
								break;
							case "WindowTopX":
								break;
							case "WindowTopY":
								break;
							case "TabRatio":
								break;
							case "ProtectStructure":
								break;
							case "ProtectWindows":
								break;
							case "ActiveSheet":
								break;
							case "DisplayInkNotes":
								break;
							case "FirstVisibleSheet":
								break;
							case "SupBook":
								break;
							case "SheetName":
								break;
							case "SheetIndex":
								break;
							case "SheetIndexFirst":
								break;
							case "SheetIndexLast":
								break;
							case "Dll":
								break;
							case "AcceptLabelsInFormulas":
								break;
							case "DoNotSaveLinkValues":
								break;
							case "Iteration":
								break;
							case "MaxIterations":
								break;
							case "MaxChange":
								break;
							case "Path":
								break;
							case "Xct":
								break;
							case "Count":
								break;
							case "SelectedSheets":
								break;
							case "Calculation":
								break;
							case "Uncalced":
								break;
							case "StartupPrompt":
								break;
							case "Crn":
								break;
							case "ExternName":
								break;
							case "Formula":
								break;
							case "ColFirst":
								break;
							case "ColLast":
								break;
							case "WantAdvise":
								break;
							case "Boolean":
								break;
							case "Error":
								break;
							case "Text":
								break;
							case "OLE":
								break;
							case "NoAutoRecover":
								break;
							case "PublishObjects":
								break;
							case "DoNotCalculateBeforeSave":
								break;
							case "Number":
								break;
							case "RefModeR1C1":
								break;
							case "EmbedSaveSmartTags":
								break;
							default:
								te = false; } break;
					case "WorkbookOptions":
						switch(s[3]) {
							case "OWCVersion":
								break;
							case "Height":
								break;
							case "Width":
								break;
							default:
								te = false; } break;
					case "WorksheetOptions":
						switch(s[3]) {
							case "Visible":
								if(s[0].slice(-2) === "/>") {} else if(s[1] === "/") switch(a.slice(F, s.index)) {
									case "SheetHidden":
										K.Hidden = 1; break;
									case "SheetVeryHidden":
										K.Hidden = 2; break; } else F = s.index + s[0].length; break;
							case "Header":
								if(!u["!margins"]) jh(u["!margins"] = {}, "xlml");
								u["!margins"].header = _e(s[0]).Margin; break;
							case "Footer":
								if(!u["!margins"]) jh(u["!margins"] = {}, "xlml");
								u["!margins"].footer = _e(s[0]).Margin; break;
							case "PageMargins":
								var ae = _e(s[0]); if(!u["!margins"]) jh(u["!margins"] = {}, "xlml"); if(ae.Top) u["!margins"].top = ae.Top; if(ae.Left) u["!margins"].left = ae.Left; if(ae.Right) u["!margins"].right = ae.Right; if(ae.Bottom) u["!margins"].bottom = ae.Bottom; break;
							case "DisplayRightToLeft":
								if(!j.Views) j.Views = []; if(!j.Views[0]) j.Views[0] = {};
								j.Views[0].RTL = true; break;
							case "Unsynced":
								break;
							case "Print":
								break;
							case "Panes":
								break;
							case "Scale":
								break;
							case "Pane":
								break;
							case "Number":
								break;
							case "Layout":
								break;
							case "PageSetup":
								break;
							case "Selected":
								break;
							case "ProtectObjects":
								break;
							case "EnableSelection":
								break;
							case "ProtectScenarios":
								break;
							case "ValidPrinterInfo":
								break;
							case "HorizontalResolution":
								break;
							case "VerticalResolution":
								break;
							case "NumberofCopies":
								break;
							case "ActiveRow":
								break;
							case "ActiveCol":
								break;
							case "ActivePane":
								break;
							case "TopRowVisible":
								break;
							case "TopRowBottomPane":
								break;
							case "LeftColumnVisible":
								break;
							case "LeftColumnRightPane":
								break;
							case "FitToPage":
								break;
							case "RangeSelection":
								break;
							case "PaperSizeIndex":
								break;
							case "PageLayoutZoom":
								break;
							case "PageBreakZoom":
								break;
							case "FilterOn":
								break;
							case "DoNotDisplayGridlines":
								break;
							case "SplitHorizontal":
								break;
							case "SplitVertical":
								break;
							case "FreezePanes":
								break;
							case "FrozenNoSplit":
								break;
							case "FitWidth":
								break;
							case "FitHeight":
								break;
							case "CommentsLayout":
								break;
							case "Zoom":
								break;
							case "LeftToRight":
								break;
							case "Gridlines":
								break;
							case "AllowSort":
								break;
							case "AllowFilter":
								break;
							case "AllowInsertRows":
								break;
							case "AllowDeleteRows":
								break;
							case "AllowInsertCols":
								break;
							case "AllowDeleteCols":
								break;
							case "AllowInsertHyperlinks":
								break;
							case "AllowFormatCells":
								break;
							case "AllowSizeCols":
								break;
							case "AllowSizeRows":
								break;
							case "NoSummaryRowsBelowDetail":
								break;
							case "TabColorIndex":
								break;
							case "DoNotDisplayHeadings":
								break;
							case "ShowPageLayoutZoom":
								break;
							case "NoSummaryColumnsRightDetail":
								break;
							case "BlackAndWhite":
								break;
							case "DoNotDisplayZeros":
								break;
							case "DisplayPageBreak":
								break;
							case "RowColHeadings":
								break;
							case "DoNotDisplayOutline":
								break;
							case "NoOrientation":
								break;
							case "AllowUsePivotTables":
								break;
							case "ZeroHeight":
								break;
							case "ViewableRange":
								break;
							case "Selection":
								break;
							case "ProtectContents":
								break;
							default:
								te = false; } break;
					case "PivotTable":
						;
					case "PivotCache":
						switch(s[3]) {
							case "ImmediateItemsOnDrop":
								break;
							case "ShowPageMultipleItemLabel":
								break;
							case "CompactRowIndent":
								break;
							case "Location":
								break;
							case "PivotField":
								break;
							case "Orientation":
								break;
							case "LayoutForm":
								break;
							case "LayoutSubtotalLocation":
								break;
							case "LayoutCompactRow":
								break;
							case "Position":
								break;
							case "PivotItem":
								break;
							case "DataType":
								break;
							case "DataField":
								break;
							case "SourceName":
								break;
							case "ParentField":
								break;
							case "PTLineItems":
								break;
							case "PTLineItem":
								break;
							case "CountOfSameItems":
								break;
							case "Item":
								break;
							case "ItemType":
								break;
							case "PTSource":
								break;
							case "CacheIndex":
								break;
							case "ConsolidationReference":
								break;
							case "FileName":
								break;
							case "Reference":
								break;
							case "NoColumnGrand":
								break;
							case "NoRowGrand":
								break;
							case "BlankLineAfterItems":
								break;
							case "Hidden":
								break;
							case "Subtotal":
								break;
							case "BaseField":
								break;
							case "MapChildItems":
								break;
							case "Function":
								break;
							case "RefreshOnFileOpen":
								break;
							case "PrintSetTitles":
								break;
							case "MergeLabels":
								break;
							case "DefaultVersion":
								break;
							case "RefreshName":
								break;
							case "RefreshDate":
								break;
							case "RefreshDateCopy":
								break;
							case "VersionLastRefresh":
								break;
							case "VersionLastUpdate":
								break;
							case "VersionUpdateableMin":
								break;
							case "VersionRefreshableMin":
								break;
							case "Calculation":
								break;
							default:
								te = false; } break;
					case "PageBreaks":
						switch(s[3]) {
							case "ColBreaks":
								break;
							case "ColBreak":
								break;
							case "RowBreaks":
								break;
							case "RowBreak":
								break;
							case "ColStart":
								break;
							case "ColEnd":
								break;
							case "RowEnd":
								break;
							default:
								te = false; } break;
					case "AutoFilter":
						switch(s[3]) {
							case "AutoFilterColumn":
								break;
							case "AutoFilterCondition":
								break;
							case "AutoFilterAnd":
								break;
							case "AutoFilterOr":
								break;
							default:
								te = false; } break;
					case "QueryTable":
						switch(s[3]) {
							case "Id":
								break;
							case "AutoFormatFont":
								break;
							case "AutoFormatPattern":
								break;
							case "QuerySource":
								break;
							case "QueryType":
								break;
							case "EnableRedirections":
								break;
							case "RefreshedInXl9":
								break;
							case "URLString":
								break;
							case "HTMLTables":
								break;
							case "Connection":
								break;
							case "CommandText":
								break;
							case "RefreshInfo":
								break;
							case "NoTitles":
								break;
							case "NextId":
								break;
							case "ColumnInfo":
								break;
							case "OverwriteCells":
								break;
							case "DoNotPromptForFile":
								break;
							case "TextWizardSettings":
								break;
							case "Source":
								break;
							case "Number":
								break;
							case "Decimal":
								break;
							case "ThousandSeparator":
								break;
							case "TrailingMinusNumbers":
								break;
							case "FormatSettings":
								break;
							case "FieldType":
								break;
							case "Delimiters":
								break;
							case "Tab":
								break;
							case "Comma":
								break;
							case "AutoFormatName":
								break;
							case "VersionLastEdit":
								break;
							case "VersionLastRefresh":
								break;
							default:
								te = false; } break;
					case "Sorting":
						;
					case "ConditionalFormatting":
						;
					case "DataValidation":
						switch(s[3]) {
							case "Range":
								break;
							case "Type":
								break;
							case "Min":
								break;
							case "Max":
								break;
							case "Sort":
								break;
							case "Descending":
								break;
							case "Order":
								break;
							case "CaseSensitive":
								break;
							case "Value":
								break;
							case "ErrorStyle":
								break;
							case "ErrorMessage":
								break;
							case "ErrorTitle":
								break;
							case "CellRangeList":
								break;
							case "InputMessage":
								break;
							case "InputTitle":
								break;
							case "ComboHide":
								break;
							case "InputHide":
								break;
							case "Condition":
								break;
							case "Qualifier":
								break;
							case "UseBlank":
								break;
							case "Value1":
								break;
							case "Value2":
								break;
							case "Format":
								break;
							default:
								te = false; } break;
					case "MapInfo":
						;
					case "Schema":
						;
					case "data":
						switch(s[3]) {
							case "Map":
								break;
							case "Entry":
								break;
							case "Range":
								break;
							case "XPath":
								break;
							case "Field":
								break;
							case "XSDType":
								break;
							case "FilterOn":
								break;
							case "Aggregate":
								break;
							case "ElementType":
								break;
							case "AttributeType":
								break;
							case "schema":
								;
							case "element":
								;
							case "complexType":
								;
							case "datatype":
								;
							case "all":
								;
							case "attribute":
								;
							case "extends":
								break;
							case "row":
								break;
							default:
								te = false; } break;
					case "SmartTags":
						break;
					default:
						te = false; break; } if(te) break; if(!f[f.length - 1][1]) throw "Unrecognized tag: " + s[3] + "|" + f.join("|"); if(f[f.length - 1][0] === "CustomDocumentProperties") { if(s[0].slice(-2) === "/>") break;
					else if(s[1] === "/") yp(O, s[3], P, a.slice(F, s.index));
					else { P = s;
						F = s.index + s[0].length } break } if(t.WTF) throw "Unrecognized tag: " + s[3] + "|" + f.join("|"); }
		var ie = {}; if(!t.bookSheets && !t.bookProps) ie.Sheets = l;
		ie.SheetNames = c;
		ie.Workbook = j;
		ie.SSF = y.get_table();
		ie.Props = D;
		ie.Custprops = O; return ie }

	function Lp(e, r) { $v(r = r || {}); switch(r.type || "base64") {
			case "base64":
				return Np(b.decode(e), r);
			case "binary":
				;
			case "buffer":
				;
			case "file":
				return Np(e, r);
			case "array":
				return Np(S(e), r); } }

	function Mp(e, r) { var t = []; if(e.Props) t.push(fn(e.Props, r)); if(e.Custprops) t.push(on(e.Props, e.Custprops, r)); return t.join("") }

	function Up() { return "" }

	function Hp(e, r) { var t = ['<Style ss:ID="Default" ss:Name="Normal"><NumberFormat/></Style>'];
		r.cellXfs.forEach(function(e, r) { var a = [];
			a.push(er("NumberFormat", null, { "ss:Format": De(y._table[e.numFmtId]) }));
			t.push(er("Style", a.join(""), { "ss:ID": "s" + (21 + r) })) }); return er("Styles", t.join("")) }

	function Wp(e) { return er("NamedRange", null, { "ss:Name": e.Name, "ss:RefersTo": "=" + zl(e.Ref, { r: 0, c: 0 }) }) }

	function Vp(e) { if(!((e || {}).Workbook || {}).Names) return ""; var r = e.Workbook.Names; var t = []; for(var a = 0; a < r.length; ++a) { var n = r[a]; if(n.Sheet != null) continue; if(n.Name.match(/^_xlfn\./)) continue;
			t.push(Wp(n)) } return er("Names", t.join("")) }

	function zp(e, r, t, a) { if(!e) return ""; if(!((a || {}).Workbook || {}).Names) return ""; var n = a.Workbook.Names; var i = [];
		e: for(var s = 0; s < n.length; ++s) { var f = n[s]; if(f.Sheet != t) continue; if(f.Name.match(/^_xlfn\./)) continue;
			i.push(Wp(f)) }
		return i.join("") }

	function Xp(e, r, t, a) { if(!e) return ""; var n = []; if(e["!margins"]) { n.push("<PageSetup>"); if(e["!margins"].header) n.push(er("Header", null, { "x:Margin": e["!margins"].header })); if(e["!margins"].footer) n.push(er("Footer", null, { "x:Margin": e["!margins"].footer }));
			n.push(er("PageMargins", null, { "x:Bottom": e["!margins"].bottom || "0.75", "x:Left": e["!margins"].left || "0.7", "x:Right": e["!margins"].right || "0.7", "x:Top": e["!margins"].top || "0.75" }));
			n.push("</PageSetup>") } if(a && a.Workbook && a.Workbook.Sheets && a.Workbook.Sheets[t]) { if(a.Workbook.Sheets[t].Hidden) n.push(er("Visible", a.Workbook.Sheets[t].Hidden == 1 ? "SheetHidden" : "SheetVeryHidden", {}));
			else { for(var i = 0; i < t; ++i)
					if(a.Workbook.Sheets[i] && !a.Workbook.Sheets[i].Hidden) break; if(i == t) n.push("<Selected/>") } } if(((((a || {}).Workbook || {}).Views || [])[0] || {}).RTL) n.push("<DisplayRightToLeft/>"); if(e["!protect"]) { n.push(Je("ProtectContents", "True")); if(e["!protect"].objects) n.push(Je("ProtectObjects", "True")); if(e["!protect"].scenarios) n.push(Je("ProtectScenarios", "True")); if(e["!protect"].selectLockedCells != null && !e["!protect"].selectLockedCells) n.push(Je("EnableSelection", "NoSelection"));
			else if(e["!protect"].selectUnlockedCells != null && !e["!protect"].selectUnlockedCells) n.push(Je("EnableSelection", "UnlockedCells"));
			[
				["formatCells", "AllowFormatCells"],
				["formatColumns", "AllowSizeCols"],
				["formatRows", "AllowSizeRows"],
				["insertColumns", "AllowInsertCols"],
				["insertRows", "AllowInsertRows"],
				["insertHyperlinks", "AllowInsertHyperlinks"],
				["deleteColumns", "AllowDeleteCols"],
				["deleteRows", "AllowDeleteRows"],
				["sort", "AllowSort"],
				["autoFilter", "AllowFilter"],
				["pivotTables", "AllowUsePivotTables"]
			].forEach(function(r) { if(e["!protect"][r[0]]) n.push("<" + r[1] + "/>") }) } if(n.length == 0) return ""; return er("WorksheetOptions", n.join(""), { xmlns: nr.x }) }

	function Gp(e) { return e.map(function(e) { var r = Me(e.t || ""); var t = er("ss:Data", r, { xmlns: "http://www.w3.org/TR/REC-html40" }); return er("Comment", t, { "ss:Author": e.a }) }).join("") }

	function jp(e, r, t, a, n, i, s) { if(!e || e.v == undefined && e.f == undefined) return ""; var f = {}; if(e.f) f["ss:Formula"] = "=" + De(zl(e.f, s)); if(e.F && e.F.slice(0, r.length) == r) { var o = ft(e.F.slice(r.length + 1));
			f["ss:ArrayRange"] = "RC:R" + (o.r == s.r ? "" : "[" + (o.r - s.r) + "]") + "C" + (o.c == s.c ? "" : "[" + (o.c - s.c) + "]") } if(e.l && e.l.Target) { f["ss:HRef"] = De(e.l.Target); if(e.l.Tooltip) f["x:HRefScreenTip"] = De(e.l.Tooltip) } if(t["!merges"]) { var l = t["!merges"]; for(var c = 0; c != l.length; ++c) { if(l[c].s.c != s.c || l[c].s.r != s.r) continue; if(l[c].e.c > l[c].s.c) f["ss:MergeAcross"] = l[c].e.c - l[c].s.c; if(l[c].e.r > l[c].s.r) f["ss:MergeDown"] = l[c].e.r - l[c].s.r } } var h = "",
			u = ""; switch(e.t) {
			case "z":
				return "";
			case "n":
				h = "Number";
				u = String(e.v); break;
			case "b":
				h = "Boolean";
				u = e.v ? "1" : "0"; break;
			case "e":
				h = "Error";
				u = zt[e.v]; break;
			case "d":
				h = "DateTime";
				u = new Date(e.v).toISOString(); if(e.z == null) e.z = e.z || y._table[14]; break;
			case "s":
				h = "String";
				u = Ne(e.v || ""); break; } var d = Kh(a.cellXfs, e, a);
		f["ss:StyleID"] = "s" + (21 + d);
		f["ss:Index"] = s.c + 1; var p = e.v != null ? u : ""; var v = '<Data ss:Type="' + h + '">' + p + "</Data>"; if((e.c || []).length > 0) v += Gp(e.c); return er("Cell", v, f) }

	function Kp(e, r) { var t = '<Row ss:Index="' + (e + 1) + '"'; if(r) { if(r.hpt && !r.hpx) r.hpx = oo(r.hpt); if(r.hpx) t += ' ss:AutoFitHeight="0" ss:Height="' + r.hpx + '"'; if(r.hidden) t += ' ss:Hidden="1"' } return t + ">" }

	function Yp(e, r, t, a) { if(!e["!ref"]) return ""; var n = ht(e["!ref"]); var i = e["!merges"] || [],
			s = 0; var f = []; if(e["!cols"]) e["!cols"].forEach(function(e, r) { no(e); var t = !!e.width; var a = Gh(r, e); var n = { "ss:Index": r + 1 }; if(t) n["ss:Width"] = qf(a.width); if(e.hidden) n["ss:Hidden"] = "1";
			f.push(er("Column", null, n)) }); var o = Array.isArray(e); for(var l = n.s.r; l <= n.e.r; ++l) { var c = [Kp(l, (e["!rows"] || [])[l])]; for(var h = n.s.c; h <= n.e.c; ++h) { var u = false; for(s = 0; s != i.length; ++s) { if(i[s].s.c > h) continue; if(i[s].s.r > l) continue; if(i[s].e.c < h) continue; if(i[s].e.r < l) continue; if(i[s].s.c != h || i[s].s.r != l) u = true; break } if(u) continue; var d = { r: l, c: h }; var p = ot(d),
					v = o ? (e[l] || [])[h] : e[p];
				c.push(jp(v, p, e, r, t, a, d)) } c.push("</Row>"); if(c.length > 2) f.push(c.join("")) } return f.join("") }

	function $p(e, r, t) { var a = []; var n = t.SheetNames[e]; var i = t.Sheets[n]; var s = i ? zp(i, r, e, t) : ""; if(s.length > 0) a.push("<Names>" + s + "</Names>");
		s = i ? Yp(i, r, e, t) : ""; if(s.length > 0) a.push("<Table>" + s + "</Table>");
		a.push(Xp(i, r, e, t)); return a.join("") }

	function Zp(e, r) { if(!r) r = {}; if(!e.SSF) e.SSF = y.get_table(); if(e.SSF) { I(y);
			y.load_table(e.SSF);
			r.revssf = j(e.SSF);
			r.revssf[e.SSF[65535]] = 0;
			r.ssf = e.SSF;
			r.cellXfs = [];
			Kh(r.cellXfs, {}, { revssf: { General: 0 } }) } var t = [];
		t.push(Mp(e, r));
		t.push(Up(e, r));
		t.push("");
		t.push(""); for(var a = 0; a < e.SheetNames.length; ++a) t.push(er("Worksheet", $p(a, r, e), { "ss:Name": De(e.SheetNames[a]) }));
		t[2] = Hp(e, r);
		t[3] = Vp(e, r); return we + er("Workbook", t.join(""), { xmlns: nr.ss, "xmlns:o": nr.o, "xmlns:x": nr.x, "xmlns:ss": nr.ss, "xmlns:dt": nr.dt, "xmlns:html": nr.html }) }

	function Qp(e) { var r = {}; var t = e.content;
		t.l = 28;
		r.AnsiUserType = t._R(0, "lpstr-ansi");
		r.AnsiClipboardFormat = Zt(t); if(t.length - t.l <= 4) return r; var a = t._R(4); if(a == 0 || a > 40) return r;
		t.l -= 4;
		r.Reserved1 = t._R(0, "lpstr-ansi"); if(t.length - t.l <= 4) return r;
		a = t._R(4); if(a !== 1907505652) return r;
		r.UnicodeClipboardFormat = Qt(t);
		a = t._R(4); if(a == 0 || a > 40) return r;
		t.l -= 4;
		r.Reserved2 = t._R(0, "lpwstr") }

	function Jp(e, r, t, a) { var n = t; var i = []; var s = r.slice(r.l, r.l + n); if(a && a.enc && a.enc.insitu) switch(e.n) {
			case "BOF":
				;
			case "FilePass":
				;
			case "FileLock":
				;
			case "InterfaceHdr":
				;
			case "RRDInfo":
				;
			case "RRDHead":
				;
			case "UsrExcl":
				break;
			default:
				if(s.length === 0) break;
				a.enc.insitu(s); } i.push(s);
		r.l += n; var f = lv[yr(r, r.l)]; var o = 0; while(f != null && f.n.slice(0, 8) === "Continue") { n = yr(r, r.l + 2);
			o = r.l + 4; if(f.n == "ContinueFrt") o += 4;
			else if(f.n.slice(0, 11) == "ContinueFrt") o += 12;
			i.push(r.slice(o, r.l + 4 + n));
			r.l += 4 + n;
			f = lv[yr(r, r.l)] } var l = B(i);
		Hr(l, 0); var c = 0;
		l.lens = []; for(var h = 0; h < i.length; ++h) { l.lens.push(c);
			c += i[h].length } return e.f(l, l.length, a) }

	function qp(e, r, t) { if(e.t === "z") return; if(!e.XF) return; var a = 0; try { a = e.z || e.XF.numFmtId || 0; if(r.cellNF) e.z = y._table[a] } catch(n) { if(r.WTF) throw n } if(!r || r.cellText !== false) try { if(e.t === "e") { e.w = e.w || zt[e.v] } else if(a === 0 || a == "General") { if(e.t === "n") { if((e.v | 0) === e.v) e.w = y._general_int(e.v);
					else e.w = y._general_num(e.v) } else e.w = y._general(e.v) } else e.w = y.format(a, e.v, { date1904: !!t }) } catch(n) { if(r.WTF) throw n }
		if(r.cellDates && a && e.t == "n" && y.is_date(y._table[a] || String(a))) { var i = y.parse_date_code(e.v); if(i) { e.t = "d";
				e.v = new Date(i.y, i.m - 1, i.d, i.H, i.M, i.S, i.u) } } }

	function ev(e, r, t) { return { v: e, ixfe: r, t: t } }

	function rv(e, r) {
		var t = { opts: {} };
		var a = {};
		if(g != null && r.dense == null) r.dense = g;
		var n = r.dense ? [] : {};
		var i = {};
		var s = {};
		var f = null;
		var o = [];
		var c = "";
		var h = {};
		var u, d = "",
			p, v, m, b;
		var C = {};
		var w = [];
		var E;
		var k;
		var S = true;
		var A = [];
		var _ = [];
		var B = { Sheets: [], WBProps: { date1904: false }, Views: [{}] },
			T = {};
		var x = function be(e) { if(e < 8) return Ca[e]; if(e < 64) return _[e - 8] || Ca[e]; return Ca[e] };
		var I = function Ce(e, r, t) { var a = r.XF.data; if(!a || !a.patternType || !t || !t.cellStyles) return;
			r.s = {};
			r.s.patternType = a.patternType; var n; if(n = Gf(x(a.icvFore))) { r.s.fgColor = { rgb: n } } if(n = Gf(x(a.icvBack))) { r.s.bgColor = { rgb: n } } };
		var R = function we(e, r, t) { if(X > 1) return; if(t.sheetRows && e.r >= t.sheetRows) S = false; if(!S) return; if(t.cellStyles && r.XF && r.XF.data) I(e, r, t);
			delete r.ixfe;
			delete r.XF;
			u = e;
			d = ot(e); if(s.s) { if(e.r < s.s.r) s.s.r = e.r; if(e.c < s.s.c) s.s.c = e.c } if(s.e) { if(e.r + 1 > s.e.r) s.e.r = e.r + 1; if(e.c + 1 > s.e.c) s.e.c = e.c + 1 } if(t.cellFormula && r.f) { for(var a = 0; a < w.length; ++a) { if(w[a][0].s.c > e.c || w[a][0].s.r > e.r) continue; if(w[a][0].e.c < e.c || w[a][0].e.r < e.r) continue;
					r.F = ct(w[a][0]); if(w[a][0].s.c != e.c || w[a][0].s.r != e.r) delete r.f; if(r.f) r.f = "" + kh(w[a][1], s, e, W, D); break } } { if(t.dense) { if(!n[e.r]) n[e.r] = [];
					n[e.r][e.c] = r } else n[d] = r } };
		var D = { enc: false, sbcch: 0, snames: [], sharedf: C, arrayf: w, rrtabid: [], lastuser: "", biff: 8, codepage: 0, winlocked: 0, cellStyles: !!r && !!r.cellStyles, WTF: !!r && !!r.wtf };
		if(r.password) D.password = r.password;
		var O;
		var F = [];
		var P = [];
		var N = [],
			L = [];
		var M = 0,
			U = 0;
		var H = false;
		var W = [];
		W.SheetNames = D.snames;
		W.sharedf = D.sharedf;
		W.arrayf = D.arrayf;
		W.names = [];
		W.XTI = [];
		var V = "";
		var X = 0;
		var G = 0,
			j = [];
		var K = [];
		var Y;
		D.codepage = 1200;
		l(1200);
		var $ = false;
		while(e.l < e.length - 1) {
			var Z = e.l;
			var Q = e._R(2);
			if(Q === 0 && V === "EOF") break;
			var J = e.l === e.length ? 0 : e._R(2);
			var q = lv[Q];
			if(q && q.f) {
				if(r.bookSheets) { if(V === "BoundSheet8" && q.n !== "BoundSheet8") break } V = q.n;
				if(q.r === 2 || q.r == 12) { var ee = e._R(2);
					J -= 2; if(!D.enc && ee !== Q && ((ee & 255) << 8 | ee >> 8) !== Q) throw new Error("rt mismatch: " + ee + "!=" + Q); if(q.r == 12) { e.l += 10;
						J -= 10 } }
				var re;
				if(q.n === "EOF") re = q.f(e, J, D);
				else re = Jp(q, e, J, D);
				var te = q.n;
				if(X == 0 && te != "BOF") continue;
				switch(te) {
					case "Date1904":
						t.opts.Date1904 = B.WBProps.date1904 = re;
						break;
					case "WriteProtect":
						t.opts.WriteProtect = true;
						break;
					case "FilePass":
						if(!D.enc) e.l = 0;
						D.enc = re;
						if(!r.password) throw new Error("File is password-protected");
						if(re.valid == null) throw new Error("Encryption scheme unsupported");
						if(!re.valid) throw new Error("Password is incorrect");
						break;
					case "WriteAccess":
						D.lastuser = re;
						break;
					case "FileSharing":
						break;
					case "CodePage":
						switch(re) {
							case 21010:
								re = 1200; break;
							case 32768:
								re = 1e4; break;
							case 32769:
								re = 1252; break; } l(D.codepage = re);
						$ = true;
						break;
					case "RRTabId":
						D.rrtabid = re;
						break;
					case "WinProtect":
						D.winlocked = re;
						break;
					case "Template":
						break;
					case "BookBool":
						break;
					case "UsesELFs":
						break;
					case "MTRSettings":
						break;
					case "RefreshAll":
						;
					case "CalcCount":
						;
					case "CalcDelta":
						;
					case "CalcIter":
						;
					case "CalcMode":
						;
					case "CalcPrecision":
						;
					case "CalcSaveRecalc":
						t.opts[te] = re;
						break;
					case "CalcRefMode":
						D.CalcRefMode = re;
						break;;
					case "Uncalced":
						break;
					case "ForceFullCalculation":
						t.opts.FullCalc = re;
						break;
					case "WsBool":
						if(re.fDialog) n["!type"] = "dialog";
						break;
					case "XF":
						A.push(re);
						break;
					case "ExtSST":
						break;
					case "BookExt":
						break;
					case "RichTextStream":
						break;
					case "BkHim":
						break;
					case "SupBook":
						W.push([re]);
						W[W.length - 1].XTI = [];
						break;
					case "ExternName":
						W[W.length - 1].push(re);
						break;
					case "Index":
						break;
					case "Lbl":
						Y = { Name: re.Name, Ref: kh(re.rgce, s, null, W, D) };
						if(re.itab > 0) Y.Sheet = re.itab - 1;
						W.names.push(Y);
						if(!W[0]) { W[0] = [];
							W[0].XTI = [] } W[W.length - 1].push(re);
						if(re.Name == "_xlnm._FilterDatabase" && re.itab > 0)
							if(re.rgce && re.rgce[0] && re.rgce[0][0] && re.rgce[0][0][0] == "PtgArea3d") K[re.itab - 1] = { ref: ct(re.rgce[0][0][1][2]) };
						break;
					case "ExternCount":
						D.ExternCount = re;
						break;
					case "ExternSheet":
						if(W.length == 0) { W[0] = [];
							W[0].XTI = [] } W[W.length - 1].XTI = W[W.length - 1].XTI.concat(re);
						W.XTI = W.XTI.concat(re);
						break;
					case "NameCmt":
						if(D.biff < 8) break;
						if(Y != null) Y.Comment = re[1];
						break;
					case "Protect":
						n["!protect"] = re;
						break;
					case "Password":
						if(re !== 0 && D.WTF) console.error("Password verifier: " + re);
						break;
					case "Prot4Rev":
						;
					case "Prot4RevPass":
						break;
					case "BoundSheet8":
						{ i[re.pos] = re;D.snames.push(re.name) }
						break;
					case "EOF":
						{ if(--X) break; if(s.e) { if(s.e.r > 0 && s.e.c > 0) { s.e.r--;
									s.e.c--;
									n["!ref"] = ct(s); if(r.sheetRows && r.sheetRows <= s.e.r) { var ae = s.e.r;
										s.e.r = r.sheetRows - 1;
										n["!fullref"] = n["!ref"];
										n["!ref"] = ct(s);
										s.e.r = ae } s.e.r++;
									s.e.c++ } if(F.length > 0) n["!merges"] = F; if(P.length > 0) n["!objects"] = P; if(N.length > 0) n["!cols"] = N; if(L.length > 0) n["!rows"] = L;
								B.Sheets.push(T) } if(c === "") h = n;
							else a[c] = n;n = r.dense ? [] : {} }
						break;
					case "BOF":
						{ if(D.biff === 8) D.biff = { 9: 2, 521: 3, 1033: 4 }[Q] || { 512: 2, 768: 3, 1024: 4, 1280: 5, 1536: 8, 2: 2, 7: 2 }[re.BIFFVer] || 8; if(X++) break;S = true;n = r.dense ? [] : {}; if(D.biff < 8 && !$) { $ = true;
								l(D.codepage = r.codepage || 1252) } if(D.biff < 5) { if(c === "") c = "Sheet1";
								s = { s: { r: 0, c: 0 }, e: { r: 0, c: 0 } }; var ne = { pos: e.l - J, name: c };
								i[ne.pos] = ne;
								D.snames.push(c) } else c = (i[Z] || { name: "" }).name; if(re.dt == 32) n["!type"] = "chart"; if(re.dt == 64) n["!type"] = "macro";F = [];P = [];D.arrayf = w = [];N = [];L = [];M = U = 0;H = false;T = { Hidden: (i[Z] || { hs: 0 }).hs, name: c } }
						break;
					case "Number":
						;
					case "BIFF2NUM":
						;
					case "BIFF2INT":
						{ if(n["!type"] == "chart")
								if(r.dense ? (n[re.r] || [])[re.c] : n[ot({ c: re.c, r: re.r })]) ++re.c;E = { ixfe: re.ixfe, XF: A[re.ixfe] || {}, v: re.val, t: "n" }; if(G > 0) E.z = j[E.ixfe >> 8 & 31];qp(E, r, t.opts.Date1904);R({ c: re.c, r: re.r }, E, r) }
						break;
					case "BoolErr":
						{ E = { ixfe: re.ixfe, XF: A[re.ixfe], v: re.val, t: re.t }; if(G > 0) E.z = j[E.ixfe >> 8 & 31];qp(E, r, t.opts.Date1904);R({ c: re.c, r: re.r }, E, r) }
						break;
					case "RK":
						{ E = { ixfe: re.ixfe, XF: A[re.ixfe], v: re.rknum, t: "n" }; if(G > 0) E.z = j[E.ixfe >> 8 & 31];qp(E, r, t.opts.Date1904);R({ c: re.c, r: re.r }, E, r) }
						break;
					case "MulRk":
						{ for(var ie = re.c; ie <= re.C; ++ie) { var se = re.rkrec[ie - re.c][0];
								E = { ixfe: se, XF: A[se], v: re.rkrec[ie - re.c][1], t: "n" }; if(G > 0) E.z = j[E.ixfe >> 8 & 31];
								qp(E, r, t.opts.Date1904);
								R({ c: ie, r: re.r }, E, r) } }
						break;
					case "Formula":
						{ if(re.val == "String") { f = re; break } E = ev(re.val, re.cell.ixfe, re.tt);E.XF = A[E.ixfe]; if(r.cellFormula) { var fe = re.formula; if(fe && fe[0] && fe[0][0] && fe[0][0][0] == "PtgExp") { var oe = fe[0][0][1][0],
										le = fe[0][0][1][1]; var ce = ot({ r: oe, c: le }); if(C[ce]) E.f = "" + kh(re.formula, s, re.cell, W, D);
									else E.F = ((r.dense ? (n[oe] || [])[le] : n[ce]) || {}).F } else E.f = "" + kh(re.formula, s, re.cell, W, D) } if(G > 0) E.z = j[E.ixfe >> 8 & 31];qp(E, r, t.opts.Date1904);R(re.cell, E, r);f = re }
						break;
					case "String":
						{ if(f) { f.val = re;
								E = ev(re, f.cell.ixfe, "s");
								E.XF = A[E.ixfe]; if(r.cellFormula) { E.f = "" + kh(f.formula, s, f.cell, W, D) } if(G > 0) E.z = j[E.ixfe >> 8 & 31];
								qp(E, r, t.opts.Date1904);
								R(f.cell, E, r);
								f = null } else throw new Error("String record expects Formula") }
						break;
					case "Array":
						{ w.push(re); var he = ot(re[0].s);p = r.dense ? (n[re[0].s.r] || [])[re[0].s.c] : n[he]; if(r.cellFormula && p) { if(!f) break; if(!he || !p) break;
								p.f = "" + kh(re[1], s, re[0], W, D);
								p.F = ct(re[0]) } }
						break;
					case "ShrFmla":
						{ if(!S) break; if(!r.cellFormula) break; if(d) { if(!f) break;
								C[ot(f.cell)] = re[0];
								p = r.dense ? (n[f.cell.r] || [])[f.cell.c] : n[ot(f.cell)];
								(p || {}).f = "" + kh(re[0], s, u, W, D) } }
						break;
					case "LabelSst":
						E = ev(o[re.isst].t, re.ixfe, "s");
						E.XF = A[E.ixfe];
						if(G > 0) E.z = j[E.ixfe >> 8 & 31];
						qp(E, r, t.opts.Date1904);
						R({ c: re.c, r: re.r }, E, r);
						break;
					case "Blank":
						if(r.sheetStubs) { E = { ixfe: re.ixfe, XF: A[re.ixfe], t: "z" }; if(G > 0) E.z = j[E.ixfe >> 8 & 31];
							qp(E, r, t.opts.Date1904);
							R({ c: re.c, r: re.r }, E, r) }
						break;
					case "MulBlank":
						if(r.sheetStubs) { for(var ue = re.c; ue <= re.C; ++ue) { var de = re.ixfe[ue - re.c];
								E = { ixfe: de, XF: A[de], t: "z" }; if(G > 0) E.z = j[E.ixfe >> 8 & 31];
								qp(E, r, t.opts.Date1904);
								R({ c: ue, r: re.r }, E, r) } }
						break;
					case "RString":
						;
					case "Label":
						;
					case "BIFF2STR":
						E = ev(re.val, re.ixfe, "s");
						E.XF = A[E.ixfe];
						if(G > 0) E.z = j[E.ixfe >> 8 & 31];
						qp(E, r, t.opts.Date1904);
						R({ c: re.c, r: re.r }, E, r);
						break;
					case "Dimensions":
						{ if(X === 1) s = re }
						break;
					case "SST":
						{ o = re }
						break;
					case "Format":
						{ if(D.biff == 4) { j[G++] = re[1]; for(var pe = 0; pe < G + 163; ++pe)
									if(y._table[pe] == re[1]) break; if(pe >= 163) y.load(re[1], G + 163) } else y.load(re[1], re[0]) }
						break;
					case "BIFF2FORMAT":
						{ j[G++] = re; for(var ve = 0; ve < G + 163; ++ve)
								if(y._table[ve] == re) break; if(ve >= 163) y.load(re, G + 163) }
						break;
					case "MergeCells":
						F = F.concat(re);
						break;
					case "Obj":
						P[re.cmo[0]] = D.lastobj = re;
						break;
					case "TxO":
						D.lastobj.TxO = re;
						break;
					case "ImData":
						D.lastobj.ImData = re;
						break;
					case "HLink":
						{ for(b = re[0].s.r; b <= re[0].e.r; ++b)
								for(m = re[0].s.c; m <= re[0].e.c; ++m) { p = r.dense ? (n[b] || [])[m] : n[ot({ c: m, r: b })]; if(p) p.l = re[1] } }
						break;
					case "HLinkTooltip":
						{ for(b = re[0].s.r; b <= re[0].e.r; ++b)
								for(m = re[0].s.c; m <= re[0].e.c; ++m) { p = r.dense ? (n[b] || [])[m] : n[ot({ c: m, r: b })]; if(p && p.l) p.l.Tooltip = re[1] } }
						break;
					case "Note":
						{ if(D.biff <= 5 && D.biff >= 2) break;p = r.dense ? (n[re[0].r] || [])[re[0].c] : n[ot(re[0])]; var ge = P[re[2]]; if(!p) break; if(!p.c) p.c = [];v = { a: re[1], t: ge.TxO.t };p.c.push(v) }
						break;
					default:
						switch(q.n) {
							case "ClrtClient":
								break;
							case "XFExt":
								dl(A[re.ixfe], re.ext); break;
							case "DefColWidth":
								M = re; break;
							case "DefaultRowHeight":
								U = re[1]; break;
							case "ColInfo":
								{ if(!D.cellStyles) break; while(re.e >= re.s) { N[re.e--] = { width: re.w / 256 }; if(!H) { H = true;
											ao(re.w / 256) } no(N[re.e + 1]) } } break;
							case "Row":
								{ var me = {}; if(re.level != null) { L[re.r] = me;
										me.level = re.level } if(re.hidden) { L[re.r] = me;
										me.hidden = true } if(re.hpt) { L[re.r] = me;
										me.hpt = re.hpt;
										me.hpx = oo(re.hpt) } } break;
							case "LeftMargin":
								;
							case "RightMargin":
								;
							case "TopMargin":
								;
							case "BottomMargin":
								if(!n["!margins"]) jh(n["!margins"] = {});
								n["!margins"][te.slice(0, -6).toLowerCase()] = re; break;
							case "Setup":
								if(!n["!margins"]) jh(n["!margins"] = {});
								n["!margins"].header = re.header;
								n["!margins"].footer = re.footer; break;
							case "Window2":
								if(re.RTL) B.Views[0].RTL = true; break;
							case "Header":
								break;
							case "Footer":
								break;
							case "HCenter":
								break;
							case "VCenter":
								break;
							case "Pls":
								break;
							case "GCW":
								break;
							case "LHRecord":
								break;
							case "DBCell":
								break;
							case "EntExU2":
								break;
							case "SxView":
								break;
							case "Sxvd":
								break;
							case "SXVI":
								break;
							case "SXVDEx":
								break;
							case "SxIvd":
								break;
							case "SXString":
								break;
							case "Sync":
								break;
							case "Addin":
								break;
							case "SXDI":
								break;
							case "SXLI":
								break;
							case "SXEx":
								break;
							case "QsiSXTag":
								break;
							case "Selection":
								break;
							case "Feat":
								break;
							case "FeatHdr":
								;
							case "FeatHdr11":
								break;
							case "Feature11":
								;
							case "Feature12":
								;
							case "List12":
								break;
							case "Country":
								k = re; break;
							case "RecalcId":
								break;
							case "DxGCol":
								break;
							case "Fbi":
								;
							case "Fbi2":
								;
							case "GelFrame":
								break;
							case "Font":
								break;
							case "XFCRC":
								break;
							case "Style":
								break;
							case "StyleExt":
								break;
							case "Palette":
								_ = re; break;
							case "Theme":
								O = re; break;
							case "ScenarioProtect":
								break;
							case "ObjProtect":
								break;
							case "CondFmt12":
								break;
							case "Table":
								break;
							case "TableStyles":
								break;
							case "TableStyle":
								break;
							case "TableStyleElement":
								break;
							case "SXStreamID":
								break;
							case "SXVS":
								break;
							case "DConRef":
								break;
							case "SXAddl":
								break;
							case "DConBin":
								break;
							case "DConName":
								break;
							case "SXPI":
								break;
							case "SxFormat":
								break;
							case "SxSelect":
								break;
							case "SxRule":
								break;
							case "SxFilt":
								break;
							case "SxItm":
								break;
							case "SxDXF":
								break;
							case "ScenMan":
								break;
							case "DCon":
								break;
							case "CellWatch":
								break;
							case "PrintRowCol":
								break;
							case "PrintGrid":
								break;
							case "PrintSize":
								break;
							case "XCT":
								break;
							case "CRN":
								break;
							case "Scl":
								{} break;
							case "SheetExt":
								{} break;
							case "SheetExtOptional":
								{} break;
							case "ObNoMacros":
								{} break;
							case "ObProj":
								{} break;
							case "CodeName":
								{ if(!c) B.WBProps.CodeName = re || "ThisWorkbook";
									else T.CodeName = re || T.name } break;
							case "GUIDTypeLib":
								{} break;
							case "WOpt":
								break;
							case "PhoneticInfo":
								break;
							case "OleObjectSize":
								break;
							case "DXF":
								;
							case "DXFN":
								;
							case "DXFN12":
								;
							case "DXFN12List":
								;
							case "DXFN12NoCB":
								break;
							case "Dv":
								;
							case "DVal":
								break;
							case "BRAI":
								;
							case "Series":
								;
							case "SeriesText":
								break;
							case "DConn":
								break;
							case "DbOrParamQry":
								break;
							case "DBQueryExt":
								break;
							case "OleDbConn":
								break;
							case "ExtString":
								break;
							case "IFmtRecord":
								break;
							case "CondFmt":
								;
							case "CF":
								;
							case "CF12":
								;
							case "CFEx":
								break;
							case "Excel9File":
								break;
							case "Units":
								break;
							case "InterfaceHdr":
								;
							case "Mms":
								;
							case "InterfaceEnd":
								;
							case "DSF":
								break;
							case "BuiltInFnGroupCount":
								break;
							case "Window1":
								;
							case "HideObj":
								;
							case "GridSet":
								;
							case "Guts":
								;
							case "UserBView":
								;
							case "UserSViewBegin":
								;
							case "UserSViewEnd":
								;
							case "Pane":
								break;
							default:
								switch(q.n) {
									case "Dat":
										;
									case "Begin":
										;
									case "End":
										;
									case "StartBlock":
										;
									case "EndBlock":
										;
									case "Frame":
										;
									case "Area":
										;
									case "Axis":
										;
									case "AxisLine":
										;
									case "Tick":
										break;
									case "AxesUsed":
										;
									case "CrtLayout12":
										;
									case "CrtLayout12A":
										;
									case "CrtLink":
										;
									case "CrtLine":
										;
									case "CrtMlFrt":
										;
									case "CrtMlFrtContinue":
										break;
									case "LineFormat":
										;
									case "AreaFormat":
										;
									case "Chart":
										;
									case "Chart3d":
										;
									case "Chart3DBarShape":
										;
									case "ChartFormat":
										;
									case "ChartFrtInfo":
										break;
									case "PlotArea":
										;
									case "PlotGrowth":
										break;
									case "SeriesList":
										;
									case "SerParent":
										;
									case "SerAuxTrend":
										break;
									case "DataFormat":
										;
									case "SerToCrt":
										;
									case "FontX":
										break;
									case "CatSerRange":
										;
									case "AxcExt":
										;
									case "SerFmt":
										break;
									case "ShtProps":
										break;
									case "DefaultText":
										;
									case "Text":
										;
									case "CatLab":
										break;
									case "DataLabExtContents":
										break;
									case "Legend":
										;
									case "LegendException":
										break;
									case "Pie":
										;
									case "Scatter":
										break;
									case "PieFormat":
										;
									case "MarkerFormat":
										break;
									case "StartObject":
										;
									case "EndObject":
										break;
									case "AlRuns":
										;
									case "ObjectLink":
										break;
									case "SIIndex":
										break;
									case "AttachedLabel":
										;
									case "YMult":
										break;
									case "Line":
										;
									case "Bar":
										break;
									case "Surf":
										break;
									case "AxisParent":
										break;
									case "Pos":
										break;
									case "ValueRange":
										break;
									case "SXViewEx9":
										break;
									case "SXViewLink":
										break;
									case "PivotChartBits":
										break;
									case "SBaseRef":
										break;
									case "TextPropsStream":
										break;
									case "LnExt":
										break;
									case "MkrExt":
										break;
									case "CrtCoopt":
										break;
									case "Qsi":
										;
									case "Qsif":
										;
									case "Qsir":
										;
									case "QsiSXTag":
										break;
									case "TxtQry":
										break;
									case "FilterMode":
										break;
									case "AutoFilter":
										;
									case "AutoFilterInfo":
										break;
									case "AutoFilter12":
										break;
									case "DropDownObjIds":
										break;
									case "Sort":
										break;
									case "SortData":
										break;
									case "ShapePropsStream":
										break;
									case "MsoDrawing":
										;
									case "MsoDrawingGroup":
										;
									case "MsoDrawingSelection":
										break;
									case "WebPub":
										;
									case "AutoWebPub":
										break;
									case "HeaderFooter":
										;
									case "HFPicture":
										;
									case "PLV":
										;
									case "HorizontalPageBreaks":
										;
									case "VerticalPageBreaks":
										break;
									case "Backup":
										;
									case "CompressPictures":
										;
									case "Compat12":
										break;
									case "Continue":
										;
									case "ContinueFrt12":
										break;
									case "FrtFontList":
										;
									case "FrtWrapper":
										break;
									default:
										switch(q.n) {
											case "TabIdConf":
												;
											case "Radar":
												;
											case "RadarArea":
												;
											case "DropBar":
												;
											case "Intl":
												;
											case "CoordList":
												;
											case "SerAuxErrBar":
												break;
											case "BIFF2FONTCLR":
												;
											case "BIFF2FMTCNT":
												;
											case "BIFF2FONTXTRA":
												break;
											case "BIFF2XF":
												;
											case "BIFF3XF":
												;
											case "BIFF4XF":
												break;
											case "BIFF4FMTCNT":
												;
											case "BIFF2ROW":
												;
											case "BIFF2WINDOW2":
												break;
											case "SCENARIO":
												;
											case "DConBin":
												;
											case "PicF":
												;
											case "DataLabExt":
												;
											case "Lel":
												;
											case "BopPop":
												;
											case "BopPopCustom":
												;
											case "RealTimeData":
												;
											case "Name":
												break;
											case "LHNGraph":
												;
											case "FnGroupName":
												;
											case "AddMenu":
												;
											case "LPr":
												break;
											case "ListObj":
												;
											case "ListField":
												break;
											case "RRSort":
												break;
											case "BigName":
												break;
											case "ToolbarHdr":
												;
											case "ToolbarEnd":
												break;
											case "DDEObjName":
												break;
											case "FRTArchId$":
												break;
											default:
												if(r.WTF) throw "Unrecognized Record " + q.n; }; }; };
				}
			} else e.l += J
		}
		t.SheetNames = z(i).sort(function(e, r) { return Number(e) - Number(r) }).map(function(e) { return i[e].name });
		if(!r.bookSheets) t.Sheets = a;
		if(t.Sheets) K.forEach(function(e, r) { t.Sheets[t.SheetNames[r]]["!autofilter"] = e });
		t.Preamble = h;
		t.Strings = o;
		t.SSF = y.get_table();
		if(D.enc) t.Encryption = D.enc;
		if(O) t.Themes = O;
		t.Metadata = {};
		if(k !== undefined) t.Metadata.Country = k;
		if(W.names.length > 0) B.Names = W.names;
		t.Workbook = B;
		return t
	}
	var tv = { SI: "e0859ff2f94f6810ab9108002b27b3d9", DSI: "02d5cdd59c2e1b10939708002b2cf9ae", UDI: "05d5cdd59c2e1b10939708002b2cf9ae" };

	function av(e, r, t) { var a = L.find(e, "!DocumentSummaryInformation"); if(a && a.size > 0) try { var n = In(a, ha, tv.DSI); for(var i in n) r[i] = n[i] } catch(s) { if(t.WTF) throw s }
		var f = L.find(e, "!SummaryInformation"); if(f && f.size > 0) try { var o = In(f, ua, tv.SI); for(var l in o)
				if(r[l] == null) r[l] = o[l] } catch(s) { if(t.WTF) throw s }
		if(r.HeadingPairs && r.TitlesOfParts) { $a(r.HeadingPairs, r.TitlesOfParts, r, t);
			delete r.HeadingPairs;
			delete r.TitlesOfParts } }

	function nv(e, r) { var t = [],
			a = [],
			n = []; var i = 0,
			s; if(e.Props) { s = z(e.Props); for(i = 0; i < s.length; ++i)(pa.hasOwnProperty(s[i]) ? t : va.hasOwnProperty(s[i]) ? a : n).push([s[i], e.Props[s[i]]]) } if(e.Custprops) { s = z(e.Custprops); for(i = 0; i < s.length; ++i)
				if(!e.Props.hasOwnProperty(s[i]))(pa.hasOwnProperty(s[i]) ? t : va.hasOwnProperty(s[i]) ? a : n).push([s[i], e.Custprops[s[i]]]) } var f = []; for(i = 0; i < n.length; ++i) { if(Tn.indexOf(n[i][0]) > -1) continue; if(n[i][1] == null) continue;
			f.push(n[i]) } if(a.length) L.utils.cfb_add(r, "/SummaryInformation", Rn(a, tv.SI, va, ua)); if(t.length || f.length) L.utils.cfb_add(r, "/DocumentSummaryInformation", Rn(t, tv.DSI, pa, ha, f.length ? f : null, tv.UDI)) }

	function iv(e, r) { if(!r) r = {};
		$v(r);
		c(); if(r.codepage) f(r.codepage); var t, a; if(e.FullPaths) { if(L.find(e, "/encryption")) throw new Error("File is password-protected");
			t = L.find(e, "!CompObj");
			a = L.find(e, "/Workbook") || L.find(e, "/Book") } else { switch(r.type) {
				case "base64":
					e = E(b.decode(e)); break;
				case "binary":
					e = E(e); break;
				case "buffer":
					break;
				case "array":
					if(!Array.isArray(e)) e = Array.prototype.slice.call(e); break; } Hr(e, 0);
			a = { content: e } } var n; var i; if(t) Qp(t); if(r.bookProps && !r.bookSheets) n = {};
		else { var s = C ? "buffer" : "array"; if(a && a.content) n = rv(a.content, r);
			else if((i = L.find(e, "PerfectOffice_MAIN")) && i.content) n = tf.to_workbook(i.content, (r.type = s, r));
			else if((i = L.find(e, "NativeContent_MAIN")) && i.content) n = tf.to_workbook(i.content, (r.type = s, r));
			else throw new Error("Cannot find Workbook stream"); if(r.bookVBA && e.FullPaths && L.find(e, "/_VBA_PROJECT_CUR/VBA/dir")) n.vbaraw = Fl(e) } var o = {}; if(e.FullPaths) av(e, o, r);
		n.Props = n.Custprops = o; if(r.bookFiles) n.cfb = e; return n }

	function sv(e, r) { var t = r || {}; var a = L.utils.cfb_new({ root: "R" }); var n = "/Workbook"; switch(t.bookType || "xls") {
			case "xls":
				t.bookType = "biff8";
			case "xla":
				if(!t.bookType) t.bookType = "xla";
			case "biff8":
				n = "/Workbook";
				t.biff = 8; break;
			case "biff5":
				n = "/Book";
				t.biff = 5; break;
			default:
				throw new Error("invalid type " + t.bookType + " for XLS CFB"); } L.utils.cfb_add(a, n, Tv(e, t)); if(t.biff == 8 && (e.Props || e.Custprops)) nv(e, a); if(t.biff == 8 && e.vbaraw) Pl(a, L.read(e.vbaraw, { type: typeof e.vbaraw == "string" ? "binary" : "buffer" })); return a }
	var fv = {
		0: { n: "BrtRowHdr", f: _u },
		1: { n: "BrtCellBlank", f: Ou },
		2: { n: "BrtCellRk", f: Vu },
		3: { n: "BrtCellError", f: Lu },
		4: { n: "BrtCellBool", f: Pu },
		5: { n: "BrtCellReal", f: Hu },
		6: { n: "BrtCellSt", f: Xu },
		7: { n: "BrtCellIsst", f: Mu },
		8: { n: "BrtFmlaString", f: $u },
		9: { n: "BrtFmlaNum", f: Yu },
		10: { n: "BrtFmlaBool", f: ju },
		11: { n: "BrtFmlaError", f: Ku },
		16: { n: "BrtFRTArchID$", f: Jd },
		19: { n: "BrtSSTItem", f: kt },
		20: { n: "BrtPCDIMissing" },
		21: { n: "BrtPCDINumber" },
		22: { n: "BrtPCDIBoolean" },
		23: { n: "BrtPCDIError" },
		24: { n: "BrtPCDIString" },
		25: { n: "BrtPCDIDatetime" },
		26: { n: "BrtPCDIIndex" },
		27: { n: "BrtPCDIAMissing" },
		28: { n: "BrtPCDIANumber" },
		29: { n: "BrtPCDIABoolean" },
		30: { n: "BrtPCDIAError" },
		31: { n: "BrtPCDIAString" },
		32: { n: "BrtPCDIADatetime" },
		33: { n: "BrtPCRRecord" },
		34: { n: "BrtPCRRecordDt" },
		35: { n: "BrtFRTBegin" },
		36: { n: "BrtFRTEnd" },
		37: { n: "BrtACBegin" },
		38: { n: "BrtACEnd" },
		39: { n: "BrtName", f: qd },
		40: { n: "BrtIndexRowBlock" },
		42: { n: "BrtIndexBlock" },
		43: { n: "BrtFont", f: _o },
		44: { n: "BrtFmt", f: So },
		45: { n: "BrtFill", f: yo },
		46: { n: "BrtBorder", f: Fo },
		47: { n: "BrtXF", f: Ro },
		48: { n: "BrtStyle" },
		49: { n: "BrtCellMeta" },
		50: { n: "BrtValueMeta" },
		51: { n: "BrtMdb" },
		52: { n: "BrtBeginFmd" },
		53: { n: "BrtEndFmd" },
		54: { n: "BrtBeginMdx" },
		55: { n: "BrtEndMdx" },
		56: { n: "BrtBeginMdxTuple" },
		57: { n: "BrtEndMdxTuple" },
		58: { n: "BrtMdxMbrIstr" },
		59: { n: "BrtStr" },
		60: { n: "BrtColInfo", f: Fs },
		62: { n: "BrtCellRString" },
		63: { n: "BrtCalcChainItem$", f: vl },
		64: { n: "BrtDVal" },
		65: { n: "BrtSxvcellNum" },
		66: { n: "BrtSxvcellStr" },
		67: { n: "BrtSxvcellBool" },
		68: { n: "BrtSxvcellErr" },
		69: { n: "BrtSxvcellDate" },
		70: { n: "BrtSxvcellNil" },
		128: { n: "BrtFileVersion" },
		129: { n: "BrtBeginSheet" },
		130: { n: "BrtEndSheet" },
		131: { n: "BrtBeginBook", f: Wr, p: 0 },
		132: { n: "BrtEndBook" },
		133: { n: "BrtBeginWsViews" },
		134: { n: "BrtEndWsViews" },
		135: { n: "BrtBeginBookViews" },
		136: { n: "BrtEndBookViews" },
		137: { n: "BrtBeginWsView", f: fd },
		138: { n: "BrtEndWsView" },
		139: { n: "BrtBeginCsViews" },
		140: { n: "BrtEndCsViews" },
		141: { n: "BrtBeginCsView" },
		142: { n: "BrtEndCsView" },
		143: { n: "BrtBeginBundleShs" },
		144: { n: "BrtEndBundleShs" },
		145: { n: "BrtBeginSheetData" },
		146: { n: "BrtEndSheetData" },
		147: { n: "BrtWsProp", f: Ru },
		148: { n: "BrtWsDim", f: xu, p: 16 },
		151: { n: "BrtPane" },
		152: { n: "BrtSel" },
		153: { n: "BrtWbProp", f: Zd },
		154: { n: "BrtWbFactoid" },
		155: { n: "BrtFileRecover" },
		156: { n: "BrtBundleSh", f: Yd },
		157: { n: "BrtCalcProp" },
		158: { n: "BrtBookView" },
		159: { n: "BrtBeginSst", f: vf },
		160: { n: "BrtEndSst" },
		161: { n: "BrtBeginAFilter", f: Ut },
		162: { n: "BrtEndAFilter" },
		163: { n: "BrtBeginFilterColumn" },
		164: { n: "BrtEndFilterColumn" },
		165: { n: "BrtBeginFilters" },
		166: { n: "BrtEndFilters" },
		167: { n: "BrtFilter" },
		168: { n: "BrtColorFilter" },
		169: { n: "BrtIconFilter" },
		170: { n: "BrtTop10Filter" },
		171: { n: "BrtDynamicFilter" },
		172: { n: "BrtBeginCustomFilters" },
		173: { n: "BrtEndCustomFilters" },
		174: { n: "BrtCustomFilter" },
		175: { n: "BrtAFilterDateGroupItem" },
		176: { n: "BrtMergeCell", f: Zu },
		177: { n: "BrtBeginMergeCells" },
		178: { n: "BrtEndMergeCells" },
		179: { n: "BrtBeginPivotCacheDef" },
		180: { n: "BrtEndPivotCacheDef" },
		181: { n: "BrtBeginPCDFields" },
		182: { n: "BrtEndPCDFields" },
		183: { n: "BrtBeginPCDField" },
		184: { n: "BrtEndPCDField" },
		185: { n: "BrtBeginPCDSource" },
		186: { n: "BrtEndPCDSource" },
		187: { n: "BrtBeginPCDSRange" },
		188: { n: "BrtEndPCDSRange" },
		189: { n: "BrtBeginPCDFAtbl" },
		190: { n: "BrtEndPCDFAtbl" },
		191: { n: "BrtBeginPCDIRun" },
		192: { n: "BrtEndPCDIRun" },
		193: { n: "BrtBeginPivotCacheRecords" },
		194: { n: "BrtEndPivotCacheRecords" },
		195: { n: "BrtBeginPCDHierarchies" },
		196: { n: "BrtEndPCDHierarchies" },
		197: { n: "BrtBeginPCDHierarchy" },
		198: { n: "BrtEndPCDHierarchy" },
		199: { n: "BrtBeginPCDHFieldsUsage" },
		200: { n: "BrtEndPCDHFieldsUsage" },
		201: { n: "BrtBeginExtConnection" },
		202: { n: "BrtEndExtConnection" },
		203: { n: "BrtBeginECDbProps" },
		204: { n: "BrtEndECDbProps" },
		205: { n: "BrtBeginECOlapProps" },
		206: { n: "BrtEndECOlapProps" },
		207: { n: "BrtBeginPCDSConsol" },
		208: { n: "BrtEndPCDSConsol" },
		209: { n: "BrtBeginPCDSCPages" },
		210: { n: "BrtEndPCDSCPages" },
		211: { n: "BrtBeginPCDSCPage" },
		212: { n: "BrtEndPCDSCPage" },
		213: { n: "BrtBeginPCDSCPItem" },
		214: { n: "BrtEndPCDSCPItem" },
		215: { n: "BrtBeginPCDSCSets" },
		216: { n: "BrtEndPCDSCSets" },
		217: { n: "BrtBeginPCDSCSet" },
		218: { n: "BrtEndPCDSCSet" },
		219: { n: "BrtBeginPCDFGroup" },
		220: { n: "BrtEndPCDFGroup" },
		221: { n: "BrtBeginPCDFGItems" },
		222: { n: "BrtEndPCDFGItems" },
		223: { n: "BrtBeginPCDFGRange" },
		224: { n: "BrtEndPCDFGRange" },
		225: { n: "BrtBeginPCDFGDiscrete" },
		226: { n: "BrtEndPCDFGDiscrete" },
		227: { n: "BrtBeginPCDSDTupleCache" },
		228: { n: "BrtEndPCDSDTupleCache" },
		229: { n: "BrtBeginPCDSDTCEntries" },
		230: { n: "BrtEndPCDSDTCEntries" },
		231: { n: "BrtBeginPCDSDTCEMembers" },
		232: { n: "BrtEndPCDSDTCEMembers" },
		233: { n: "BrtBeginPCDSDTCEMember" },
		234: { n: "BrtEndPCDSDTCEMember" },
		235: { n: "BrtBeginPCDSDTCQueries" },
		236: { n: "BrtEndPCDSDTCQueries" },
		237: { n: "BrtBeginPCDSDTCQuery" },
		238: { n: "BrtEndPCDSDTCQuery" },
		239: { n: "BrtBeginPCDSDTCSets" },
		240: { n: "BrtEndPCDSDTCSets" },
		241: { n: "BrtBeginPCDSDTCSet" },
		242: { n: "BrtEndPCDSDTCSet" },
		243: { n: "BrtBeginPCDCalcItems" },
		244: { n: "BrtEndPCDCalcItems" },
		245: { n: "BrtBeginPCDCalcItem" },
		246: { n: "BrtEndPCDCalcItem" },
		247: { n: "BrtBeginPRule" },
		248: { n: "BrtEndPRule" },
		249: { n: "BrtBeginPRFilters" },
		250: { n: "BrtEndPRFilters" },
		251: { n: "BrtBeginPRFilter" },
		252: { n: "BrtEndPRFilter" },
		253: { n: "BrtBeginPNames" },
		254: { n: "BrtEndPNames" },
		255: { n: "BrtBeginPName" },
		256: { n: "BrtEndPName" },
		257: { n: "BrtBeginPNPairs" },
		258: { n: "BrtEndPNPairs" },
		259: { n: "BrtBeginPNPair" },
		260: { n: "BrtEndPNPair" },
		261: { n: "BrtBeginECWebProps" },
		262: { n: "BrtEndECWebProps" },
		263: { n: "BrtBeginEcWpTables" },
		264: { n: "BrtEndECWPTables" },
		265: { n: "BrtBeginECParams" },
		266: { n: "BrtEndECParams" },
		267: { n: "BrtBeginECParam" },
		268: { n: "BrtEndECParam" },
		269: { n: "BrtBeginPCDKPIs" },
		270: { n: "BrtEndPCDKPIs" },
		271: { n: "BrtBeginPCDKPI" },
		272: { n: "BrtEndPCDKPI" },
		273: { n: "BrtBeginDims" },
		274: { n: "BrtEndDims" },
		275: { n: "BrtBeginDim" },
		276: { n: "BrtEndDim" },
		277: { n: "BrtIndexPartEnd" },
		278: { n: "BrtBeginStyleSheet" },
		279: { n: "BrtEndStyleSheet" },
		280: { n: "BrtBeginSXView" },
		281: { n: "BrtEndSXVI" },
		282: { n: "BrtBeginSXVI" },
		283: { n: "BrtBeginSXVIs" },
		284: { n: "BrtEndSXVIs" },
		285: { n: "BrtBeginSXVD" },
		286: { n: "BrtEndSXVD" },
		287: { n: "BrtBeginSXVDs" },
		288: { n: "BrtEndSXVDs" },
		289: { n: "BrtBeginSXPI" },
		290: { n: "BrtEndSXPI" },
		291: { n: "BrtBeginSXPIs" },
		292: { n: "BrtEndSXPIs" },
		293: { n: "BrtBeginSXDI" },
		294: { n: "BrtEndSXDI" },
		295: { n: "BrtBeginSXDIs" },
		296: { n: "BrtEndSXDIs" },
		297: { n: "BrtBeginSXLI" },
		298: { n: "BrtEndSXLI" },
		299: { n: "BrtBeginSXLIRws" },
		300: { n: "BrtEndSXLIRws" },
		301: { n: "BrtBeginSXLICols" },
		302: { n: "BrtEndSXLICols" },
		303: { n: "BrtBeginSXFormat" },
		304: { n: "BrtEndSXFormat" },
		305: { n: "BrtBeginSXFormats" },
		306: { n: "BrtEndSxFormats" },
		307: { n: "BrtBeginSxSelect" },
		308: { n: "BrtEndSxSelect" },
		309: { n: "BrtBeginISXVDRws" },
		310: { n: "BrtEndISXVDRws" },
		311: { n: "BrtBeginISXVDCols" },
		312: { n: "BrtEndISXVDCols" },
		313: { n: "BrtEndSXLocation" },
		314: { n: "BrtBeginSXLocation" },
		315: { n: "BrtEndSXView" },
		316: { n: "BrtBeginSXTHs" },
		317: { n: "BrtEndSXTHs" },
		318: { n: "BrtBeginSXTH" },
		319: { n: "BrtEndSXTH" },
		320: { n: "BrtBeginISXTHRws" },
		321: { n: "BrtEndISXTHRws" },
		322: { n: "BrtBeginISXTHCols" },
		323: { n: "BrtEndISXTHCols" },
		324: { n: "BrtBeginSXTDMPS" },
		325: { n: "BrtEndSXTDMPs" },
		326: { n: "BrtBeginSXTDMP" },
		327: { n: "BrtEndSXTDMP" },
		328: { n: "BrtBeginSXTHItems" },
		329: { n: "BrtEndSXTHItems" },
		330: { n: "BrtBeginSXTHItem" },
		331: { n: "BrtEndSXTHItem" },
		332: { n: "BrtBeginMetadata" },
		333: { n: "BrtEndMetadata" },
		334: { n: "BrtBeginEsmdtinfo" },
		335: { n: "BrtMdtinfo" },
		336: { n: "BrtEndEsmdtinfo" },
		337: { n: "BrtBeginEsmdb" },
		338: { n: "BrtEndEsmdb" },
		339: { n: "BrtBeginEsfmd" },
		340: { n: "BrtEndEsfmd" },
		341: { n: "BrtBeginSingleCells" },
		342: { n: "BrtEndSingleCells" },
		343: { n: "BrtBeginList" },
		344: { n: "BrtEndList" },
		345: { n: "BrtBeginListCols" },
		346: { n: "BrtEndListCols" },
		347: { n: "BrtBeginListCol" },
		348: { n: "BrtEndListCol" },
		349: { n: "BrtBeginListXmlCPr" },
		350: { n: "BrtEndListXmlCPr" },
		351: { n: "BrtListCCFmla" },
		352: { n: "BrtListTrFmla" },
		353: { n: "BrtBeginExternals" },
		354: { n: "BrtEndExternals" },
		355: { n: "BrtSupBookSrc", f: Ot },
		357: { n: "BrtSupSelf" },
		358: { n: "BrtSupSame" },
		359: { n: "BrtSupTabs" },
		360: { n: "BrtBeginSupBook" },
		361: { n: "BrtPlaceholderName" },
		362: { n: "BrtExternSheet", f: hs },
		363: { n: "BrtExternTableStart" },
		364: { n: "BrtExternTableEnd" },
		366: { n: "BrtExternRowHdr" },
		367: { n: "BrtExternCellBlank" },
		368: { n: "BrtExternCellReal" },
		369: { n: "BrtExternCellBool" },
		370: { n: "BrtExternCellError" },
		371: { n: "BrtExternCellString" },
		372: { n: "BrtBeginEsmdx" },
		373: { n: "BrtEndEsmdx" },
		374: { n: "BrtBeginMdxSet" },
		375: { n: "BrtEndMdxSet" },
		376: { n: "BrtBeginMdxMbrProp" },
		377: { n: "BrtEndMdxMbrProp" },
		378: { n: "BrtBeginMdxKPI" },
		379: { n: "BrtEndMdxKPI" },
		380: { n: "BrtBeginEsstr" },
		381: { n: "BrtEndEsstr" },
		382: { n: "BrtBeginPRFItem" },
		383: { n: "BrtEndPRFItem" },
		384: { n: "BrtBeginPivotCacheIDs" },
		385: { n: "BrtEndPivotCacheIDs" },
		386: { n: "BrtBeginPivotCacheID" },
		387: { n: "BrtEndPivotCacheID" },
		388: { n: "BrtBeginISXVIs" },
		389: { n: "BrtEndISXVIs" },
		390: { n: "BrtBeginColInfos" },
		391: { n: "BrtEndColInfos" },
		392: { n: "BrtBeginRwBrk" },
		393: { n: "BrtEndRwBrk" },
		394: { n: "BrtBeginColBrk" },
		395: { n: "BrtEndColBrk" },
		396: { n: "BrtBrk" },
		397: { n: "BrtUserBookView" },
		398: { n: "BrtInfo" },
		399: { n: "BrtCUsr" },
		400: { n: "BrtUsr" },
		401: { n: "BrtBeginUsers" },
		403: { n: "BrtEOF" },
		404: { n: "BrtUCR" },
		405: { n: "BrtRRInsDel" },
		406: { n: "BrtRREndInsDel" },
		407: { n: "BrtRRMove" },
		408: { n: "BrtRREndMove" },
		409: { n: "BrtRRChgCell" },
		410: { n: "BrtRREndChgCell" },
		411: { n: "BrtRRHeader" },
		412: { n: "BrtRRUserView" },
		413: { n: "BrtRRRenSheet" },
		414: { n: "BrtRRInsertSh" },
		415: { n: "BrtRRDefName" },
		416: { n: "BrtRRNote" },
		417: { n: "BrtRRConflict" },
		418: { n: "BrtRRTQSIF" },
		419: { n: "BrtRRFormat" },
		420: { n: "BrtRREndFormat" },
		421: { n: "BrtRRAutoFmt" },
		422: { n: "BrtBeginUserShViews" },
		423: { n: "BrtBeginUserShView" },
		424: { n: "BrtEndUserShView" },
		425: { n: "BrtEndUserShViews" },
		426: { n: "BrtArrFmla", f: rd },
		427: { n: "BrtShrFmla", f: td },
		428: { n: "BrtTable" },
		429: { n: "BrtBeginExtConnections" },
		430: { n: "BrtEndExtConnections" },
		431: { n: "BrtBeginPCDCalcMems" },
		432: { n: "BrtEndPCDCalcMems" },
		433: { n: "BrtBeginPCDCalcMem" },
		434: { n: "BrtEndPCDCalcMem" },
		435: { n: "BrtBeginPCDHGLevels" },
		436: { n: "BrtEndPCDHGLevels" },
		437: { n: "BrtBeginPCDHGLevel" },
		438: { n: "BrtEndPCDHGLevel" },
		439: { n: "BrtBeginPCDHGLGroups" },
		440: { n: "BrtEndPCDHGLGroups" },
		441: { n: "BrtBeginPCDHGLGroup" },
		442: { n: "BrtEndPCDHGLGroup" },
		443: { n: "BrtBeginPCDHGLGMembers" },
		444: { n: "BrtEndPCDHGLGMembers" },
		445: { n: "BrtBeginPCDHGLGMember" },
		446: { n: "BrtEndPCDHGLGMember" },
		447: { n: "BrtBeginQSI" },
		448: { n: "BrtEndQSI" },
		449: { n: "BrtBeginQSIR" },
		450: { n: "BrtEndQSIR" },
		451: { n: "BrtBeginDeletedNames" },
		452: { n: "BrtEndDeletedNames" },
		453: { n: "BrtBeginDeletedName" },
		454: { n: "BrtEndDeletedName" },
		455: { n: "BrtBeginQSIFs" },
		456: { n: "BrtEndQSIFs" },
		457: { n: "BrtBeginQSIF" },
		458: { n: "BrtEndQSIF" },
		459: { n: "BrtBeginAutoSortScope" },
		460: { n: "BrtEndAutoSortScope" },
		461: { n: "BrtBeginConditionalFormatting" },
		462: { n: "BrtEndConditionalFormatting" },
		463: { n: "BrtBeginCFRule" },
		464: { n: "BrtEndCFRule" },
		465: { n: "BrtBeginIconSet" },
		466: { n: "BrtEndIconSet" },
		467: { n: "BrtBeginDatabar" },
		468: { n: "BrtEndDatabar" },
		469: { n: "BrtBeginColorScale" },
		470: { n: "BrtEndColorScale" },
		471: { n: "BrtCFVO" },
		472: { n: "BrtExternValueMeta" },
		473: { n: "BrtBeginColorPalette" },
		474: { n: "BrtEndColorPalette" },
		475: { n: "BrtIndexedColor" },
		476: { n: "BrtMargins", f: id },
		477: { n: "BrtPrintOptions" },
		478: { n: "BrtPageSetup" },
		479: { n: "BrtBeginHeaderFooter" },
		480: { n: "BrtEndHeaderFooter" },
		481: { n: "BrtBeginSXCrtFormat" },
		482: { n: "BrtEndSXCrtFormat" },
		483: { n: "BrtBeginSXCrtFormats" },
		484: { n: "BrtEndSXCrtFormats" },
		485: { n: "BrtWsFmtInfo", f: Iu },
		486: { n: "BrtBeginMgs" },
		487: { n: "BrtEndMGs" },
		488: { n: "BrtBeginMGMaps" },
		489: { n: "BrtEndMGMaps" },
		490: { n: "BrtBeginMG" },
		491: { n: "BrtEndMG" },
		492: { n: "BrtBeginMap" },
		493: { n: "BrtEndMap" },
		494: { n: "BrtHLink", f: qu },
		495: { n: "BrtBeginDCon" },
		496: { n: "BrtEndDCon" },
		497: { n: "BrtBeginDRefs" },
		498: { n: "BrtEndDRefs" },
		499: { n: "BrtDRef" },
		500: { n: "BrtBeginScenMan" },
		501: { n: "BrtEndScenMan" },
		502: { n: "BrtBeginSct" },
		503: { n: "BrtEndSct" },
		504: { n: "BrtSlc" },
		505: { n: "BrtBeginDXFs" },
		506: { n: "BrtEndDXFs" },
		507: { n: "BrtDXF" },
		508: { n: "BrtBeginTableStyles" },
		509: { n: "BrtEndTableStyles" },
		510: { n: "BrtBeginTableStyle" },
		511: { n: "BrtEndTableStyle" },
		512: { n: "BrtTableStyleElement" },
		513: { n: "BrtTableStyleClient" },
		514: { n: "BrtBeginVolDeps" },
		515: { n: "BrtEndVolDeps" },
		516: { n: "BrtBeginVolType" },
		517: { n: "BrtEndVolType" },
		518: { n: "BrtBeginVolMain" },
		519: { n: "BrtEndVolMain" },
		520: { n: "BrtBeginVolTopic" },
		521: { n: "BrtEndVolTopic" },
		522: { n: "BrtVolSubtopic" },
		523: { n: "BrtVolRef" },
		524: { n: "BrtVolNum" },
		525: { n: "BrtVolErr" },
		526: { n: "BrtVolStr" },
		527: { n: "BrtVolBool" },
		528: { n: "BrtBeginCalcChain$" },
		529: { n: "BrtEndCalcChain$" },
		530: { n: "BrtBeginSortState" },
		531: { n: "BrtEndSortState" },
		532: { n: "BrtBeginSortCond" },
		533: { n: "BrtEndSortCond" },
		534: { n: "BrtBookProtection" },
		535: { n: "BrtSheetProtection" },
		536: { n: "BrtRangeProtection" },
		537: { n: "BrtPhoneticInfo" },
		538: { n: "BrtBeginECTxtWiz" },
		539: { n: "BrtEndECTxtWiz" },
		540: { n: "BrtBeginECTWFldInfoLst" },
		541: { n: "BrtEndECTWFldInfoLst" },
		542: { n: "BrtBeginECTwFldInfo" },
		548: { n: "BrtFileSharing" },
		549: { n: "BrtOleSize" },
		550: { n: "BrtDrawing", f: Ot },
		551: { n: "BrtLegacyDrawing" },
		552: { n: "BrtLegacyDrawingHF" },
		553: { n: "BrtWebOpt" },
		554: { n: "BrtBeginWebPubItems" },
		555: { n: "BrtEndWebPubItems" },
		556: { n: "BrtBeginWebPubItem" },
		557: { n: "BrtEndWebPubItem" },
		558: { n: "BrtBeginSXCondFmt" },
		559: { n: "BrtEndSXCondFmt" },
		560: { n: "BrtBeginSXCondFmts" },
		561: { n: "BrtEndSXCondFmts" },
		562: { n: "BrtBkHim" },
		564: { n: "BrtColor" },
		565: { n: "BrtBeginIndexedColors" },
		566: { n: "BrtEndIndexedColors" },
		569: { n: "BrtBeginMRUColors" },
		570: { n: "BrtEndMRUColors" },
		572: { n: "BrtMRUColor" },
		573: { n: "BrtBeginDVals" },
		574: { n: "BrtEndDVals" },
		577: { n: "BrtSupNameStart" },
		578: { n: "BrtSupNameValueStart" },
		579: { n: "BrtSupNameValueEnd" },
		580: { n: "BrtSupNameNum" },
		581: { n: "BrtSupNameErr" },
		582: { n: "BrtSupNameSt" },
		583: { n: "BrtSupNameNil" },
		584: { n: "BrtSupNameBool" },
		585: { n: "BrtSupNameFmla" },
		586: { n: "BrtSupNameBits" },
		587: { n: "BrtSupNameEnd" },
		588: { n: "BrtEndSupBook" },
		589: { n: "BrtCellSmartTagProperty" },
		590: { n: "BrtBeginCellSmartTag" },
		591: { n: "BrtEndCellSmartTag" },
		592: { n: "BrtBeginCellSmartTags" },
		593: { n: "BrtEndCellSmartTags" },
		594: { n: "BrtBeginSmartTags" },
		595: { n: "BrtEndSmartTags" },
		596: { n: "BrtSmartTagType" },
		597: { n: "BrtBeginSmartTagTypes" },
		598: { n: "BrtEndSmartTagTypes" },
		599: { n: "BrtBeginSXFilters" },
		600: { n: "BrtEndSXFilters" },
		601: { n: "BrtBeginSXFILTER" },
		602: { n: "BrtEndSXFilter" },
		603: { n: "BrtBeginFills" },
		604: { n: "BrtEndFills" },
		605: { n: "BrtBeginCellWatches" },
		606: { n: "BrtEndCellWatches" },
		607: { n: "BrtCellWatch" },
		608: { n: "BrtBeginCRErrs" },
		609: { n: "BrtEndCRErrs" },
		610: { n: "BrtCrashRecErr" },
		611: { n: "BrtBeginFonts" },
		612: { n: "BrtEndFonts" },
		613: { n: "BrtBeginBorders" },
		614: { n: "BrtEndBorders" },
		615: { n: "BrtBeginFmts" },
		616: { n: "BrtEndFmts" },
		617: { n: "BrtBeginCellXFs" },
		618: { n: "BrtEndCellXFs" },
		619: { n: "BrtBeginStyles" },
		620: { n: "BrtEndStyles" },
		625: { n: "BrtBigName" },
		626: { n: "BrtBeginCellStyleXFs" },
		627: { n: "BrtEndCellStyleXFs" },
		628: { n: "BrtBeginComments" },
		629: { n: "BrtEndComments" },
		630: { n: "BrtBeginCommentAuthors" },
		631: { n: "BrtEndCommentAuthors" },
		632: { n: "BrtCommentAuthor", f: yl },
		633: { n: "BrtBeginCommentList" },
		634: { n: "BrtEndCommentList" },
		635: { n: "BrtBeginComment", f: Tl },
		636: { n: "BrtEndComment" },
		637: { n: "BrtCommentText", f: At },
		638: { n: "BrtBeginOleObjects" },
		639: { n: "BrtOleObject" },
		640: { n: "BrtEndOleObjects" },
		641: { n: "BrtBeginSxrules" },
		642: { n: "BrtEndSxRules" },
		643: { n: "BrtBeginActiveXControls" },
		644: { n: "BrtActiveX" },
		645: { n: "BrtEndActiveXControls" },
		646: { n: "BrtBeginPCDSDTCEMembersSortBy" },
		648: { n: "BrtBeginCellIgnoreECs" },
		649: { n: "BrtCellIgnoreEC" },
		650: { n: "BrtEndCellIgnoreECs" },
		651: { n: "BrtCsProp", f: yd },
		652: { n: "BrtCsPageSetup" },
		653: { n: "BrtBeginUserCsViews" },
		654: { n: "BrtEndUserCsViews" },
		655: { n: "BrtBeginUserCsView" },
		656: { n: "BrtEndUserCsView" },
		657: { n: "BrtBeginPcdSFCIEntries" },
		658: { n: "BrtEndPCDSFCIEntries" },
		659: { n: "BrtPCDSFCIEntry" },
		660: { n: "BrtBeginListParts" },
		661: { n: "BrtListPart" },
		662: { n: "BrtEndListParts" },
		663: { n: "BrtSheetCalcProp" },
		664: { n: "BrtBeginFnGroup" },
		665: { n: "BrtFnGroup" },
		666: { n: "BrtEndFnGroup" },
		667: { n: "BrtSupAddin" },
		668: { n: "BrtSXTDMPOrder" },
		669: { n: "BrtCsProtection" },
		671: { n: "BrtBeginWsSortMap" },
		672: { n: "BrtEndWsSortMap" },
		673: { n: "BrtBeginRRSort" },
		674: { n: "BrtEndRRSort" },
		675: { n: "BrtRRSortItem" },
		676: { n: "BrtFileSharingIso" },
		677: { n: "BrtBookProtectionIso" },
		678: { n: "BrtSheetProtectionIso" },
		679: { n: "BrtCsProtectionIso" },
		680: { n: "BrtRangeProtectionIso" },
		1024: { n: "BrtRwDescent" },
		1025: { n: "BrtKnownFonts" },
		1026: { n: "BrtBeginSXTupleSet" },
		1027: { n: "BrtEndSXTupleSet" },
		1028: { n: "BrtBeginSXTupleSetHeader" },
		1029: { n: "BrtEndSXTupleSetHeader" },
		1030: { n: "BrtSXTupleSetHeaderItem" },
		1031: { n: "BrtBeginSXTupleSetData" },
		1032: { n: "BrtEndSXTupleSetData" },
		1033: { n: "BrtBeginSXTupleSetRow" },
		1034: { n: "BrtEndSXTupleSetRow" },
		1035: { n: "BrtSXTupleSetRowItem" },
		1036: { n: "BrtNameExt" },
		1037: { n: "BrtPCDH14" },
		1038: { n: "BrtBeginPCDCalcMem14" },
		1039: { n: "BrtEndPCDCalcMem14" },
		1040: { n: "BrtSXTH14" },
		1041: { n: "BrtBeginSparklineGroup" },
		1042: { n: "BrtEndSparklineGroup" },
		1043: { n: "BrtSparkline" },
		1044: { n: "BrtSXDI14" },
		1045: { n: "BrtWsFmtInfoEx14" },
		1046: { n: "BrtBeginConditionalFormatting14" },
		1047: { n: "BrtEndConditionalFormatting14" },
		1048: { n: "BrtBeginCFRule14" },
		1049: { n: "BrtEndCFRule14" },
		1050: { n: "BrtCFVO14" },
		1051: { n: "BrtBeginDatabar14" },
		1052: { n: "BrtBeginIconSet14" },
		1053: { n: "BrtDVal14" },
		1054: { n: "BrtBeginDVals14" },
		1055: { n: "BrtColor14" },
		1056: { n: "BrtBeginSparklines" },
		1057: { n: "BrtEndSparklines" },
		1058: { n: "BrtBeginSparklineGroups" },
		1059: { n: "BrtEndSparklineGroups" },
		1061: { n: "BrtSXVD14" },
		1062: { n: "BrtBeginSXView14" },
		1063: { n: "BrtEndSXView14" },
		1064: { n: "BrtBeginSXView16" },
		1065: { n: "BrtEndSXView16" },
		1066: { n: "BrtBeginPCD14" },
		1067: { n: "BrtEndPCD14" },
		1068: { n: "BrtBeginExtConn14" },
		1069: { n: "BrtEndExtConn14" },
		1070: { n: "BrtBeginSlicerCacheIDs" },
		1071: { n: "BrtEndSlicerCacheIDs" },
		1072: { n: "BrtBeginSlicerCacheID" },
		1073: { n: "BrtEndSlicerCacheID" },
		1075: { n: "BrtBeginSlicerCache" },
		1076: { n: "BrtEndSlicerCache" },
		1077: { n: "BrtBeginSlicerCacheDef" },
		1078: { n: "BrtEndSlicerCacheDef" },
		1079: { n: "BrtBeginSlicersEx" },
		1080: { n: "BrtEndSlicersEx" },
		1081: { n: "BrtBeginSlicerEx" },
		1082: { n: "BrtEndSlicerEx" },
		1083: { n: "BrtBeginSlicer" },
		1084: { n: "BrtEndSlicer" },
		1085: { n: "BrtSlicerCachePivotTables" },
		1086: { n: "BrtBeginSlicerCacheOlapImpl" },
		1087: { n: "BrtEndSlicerCacheOlapImpl" },
		1088: { n: "BrtBeginSlicerCacheLevelsData" },
		1089: { n: "BrtEndSlicerCacheLevelsData" },
		1090: { n: "BrtBeginSlicerCacheLevelData" },
		1091: { n: "BrtEndSlicerCacheLevelData" },
		1092: { n: "BrtBeginSlicerCacheSiRanges" },
		1093: { n: "BrtEndSlicerCacheSiRanges" },
		1094: { n: "BrtBeginSlicerCacheSiRange" },
		1095: { n: "BrtEndSlicerCacheSiRange" },
		1096: { n: "BrtSlicerCacheOlapItem" },
		1097: { n: "BrtBeginSlicerCacheSelections" },
		1098: { n: "BrtSlicerCacheSelection" },
		1099: { n: "BrtEndSlicerCacheSelections" },
		1100: { n: "BrtBeginSlicerCacheNative" },
		1101: { n: "BrtEndSlicerCacheNative" },
		1102: { n: "BrtSlicerCacheNativeItem" },
		1103: { n: "BrtRangeProtection14" },
		1104: { n: "BrtRangeProtectionIso14" },
		1105: {
			n: "BrtCellIgnoreEC14"
		},
		1111: { n: "BrtList14" },
		1112: { n: "BrtCFIcon" },
		1113: { n: "BrtBeginSlicerCachesPivotCacheIDs" },
		1114: { n: "BrtEndSlicerCachesPivotCacheIDs" },
		1115: { n: "BrtBeginSlicers" },
		1116: { n: "BrtEndSlicers" },
		1117: { n: "BrtWbProp14" },
		1118: { n: "BrtBeginSXEdit" },
		1119: { n: "BrtEndSXEdit" },
		1120: { n: "BrtBeginSXEdits" },
		1121: { n: "BrtEndSXEdits" },
		1122: { n: "BrtBeginSXChange" },
		1123: { n: "BrtEndSXChange" },
		1124: { n: "BrtBeginSXChanges" },
		1125: { n: "BrtEndSXChanges" },
		1126: { n: "BrtSXTupleItems" },
		1128: { n: "BrtBeginSlicerStyle" },
		1129: { n: "BrtEndSlicerStyle" },
		1130: { n: "BrtSlicerStyleElement" },
		1131: { n: "BrtBeginStyleSheetExt14" },
		1132: { n: "BrtEndStyleSheetExt14" },
		1133: { n: "BrtBeginSlicerCachesPivotCacheID" },
		1134: { n: "BrtEndSlicerCachesPivotCacheID" },
		1135: { n: "BrtBeginConditionalFormattings" },
		1136: { n: "BrtEndConditionalFormattings" },
		1137: { n: "BrtBeginPCDCalcMemExt" },
		1138: { n: "BrtEndPCDCalcMemExt" },
		1139: { n: "BrtBeginPCDCalcMemsExt" },
		1140: { n: "BrtEndPCDCalcMemsExt" },
		1141: { n: "BrtPCDField14" },
		1142: { n: "BrtBeginSlicerStyles" },
		1143: { n: "BrtEndSlicerStyles" },
		1144: { n: "BrtBeginSlicerStyleElements" },
		1145: { n: "BrtEndSlicerStyleElements" },
		1146: { n: "BrtCFRuleExt" },
		1147: { n: "BrtBeginSXCondFmt14" },
		1148: { n: "BrtEndSXCondFmt14" },
		1149: { n: "BrtBeginSXCondFmts14" },
		1150: { n: "BrtEndSXCondFmts14" },
		1152: { n: "BrtBeginSortCond14" },
		1153: { n: "BrtEndSortCond14" },
		1154: { n: "BrtEndDVals14" },
		1155: { n: "BrtEndIconSet14" },
		1156: { n: "BrtEndDatabar14" },
		1157: { n: "BrtBeginColorScale14" },
		1158: { n: "BrtEndColorScale14" },
		1159: { n: "BrtBeginSxrules14" },
		1160: { n: "BrtEndSxrules14" },
		1161: { n: "BrtBeginPRule14" },
		1162: { n: "BrtEndPRule14" },
		1163: { n: "BrtBeginPRFilters14" },
		1164: { n: "BrtEndPRFilters14" },
		1165: { n: "BrtBeginPRFilter14" },
		1166: { n: "BrtEndPRFilter14" },
		1167: { n: "BrtBeginPRFItem14" },
		1168: { n: "BrtEndPRFItem14" },
		1169: { n: "BrtBeginCellIgnoreECs14" },
		1170: { n: "BrtEndCellIgnoreECs14" },
		1171: { n: "BrtDxf14" },
		1172: { n: "BrtBeginDxF14s" },
		1173: { n: "BrtEndDxf14s" },
		1177: { n: "BrtFilter14" },
		1178: { n: "BrtBeginCustomFilters14" },
		1180: { n: "BrtCustomFilter14" },
		1181: { n: "BrtIconFilter14" },
		1182: { n: "BrtPivotCacheConnectionName" },
		2048: { n: "BrtBeginDecoupledPivotCacheIDs" },
		2049: { n: "BrtEndDecoupledPivotCacheIDs" },
		2050: { n: "BrtDecoupledPivotCacheID" },
		2051: { n: "BrtBeginPivotTableRefs" },
		2052: { n: "BrtEndPivotTableRefs" },
		2053: { n: "BrtPivotTableRef" },
		2054: { n: "BrtSlicerCacheBookPivotTables" },
		2055: { n: "BrtBeginSxvcells" },
		2056: { n: "BrtEndSxvcells" },
		2057: { n: "BrtBeginSxRow" },
		2058: { n: "BrtEndSxRow" },
		2060: { n: "BrtPcdCalcMem15" },
		2067: { n: "BrtQsi15" },
		2068: { n: "BrtBeginWebExtensions" },
		2069: { n: "BrtEndWebExtensions" },
		2070: { n: "BrtWebExtension" },
		2071: { n: "BrtAbsPath15" },
		2072: { n: "BrtBeginPivotTableUISettings" },
		2073: { n: "BrtEndPivotTableUISettings" },
		2075: { n: "BrtTableSlicerCacheIDs" },
		2076: { n: "BrtTableSlicerCacheID" },
		2077: { n: "BrtBeginTableSlicerCache" },
		2078: { n: "BrtEndTableSlicerCache" },
		2079: { n: "BrtSxFilter15" },
		2080: { n: "BrtBeginTimelineCachePivotCacheIDs" },
		2081: { n: "BrtEndTimelineCachePivotCacheIDs" },
		2082: { n: "BrtTimelineCachePivotCacheID" },
		2083: { n: "BrtBeginTimelineCacheIDs" },
		2084: { n: "BrtEndTimelineCacheIDs" },
		2085: { n: "BrtBeginTimelineCacheID" },
		2086: { n: "BrtEndTimelineCacheID" },
		2087: { n: "BrtBeginTimelinesEx" },
		2088: { n: "BrtEndTimelinesEx" },
		2089: { n: "BrtBeginTimelineEx" },
		2090: { n: "BrtEndTimelineEx" },
		2091: { n: "BrtWorkBookPr15" },
		2092: { n: "BrtPCDH15" },
		2093: { n: "BrtBeginTimelineStyle" },
		2094: { n: "BrtEndTimelineStyle" },
		2095: { n: "BrtTimelineStyleElement" },
		2096: { n: "BrtBeginTimelineStylesheetExt15" },
		2097: { n: "BrtEndTimelineStylesheetExt15" },
		2098: { n: "BrtBeginTimelineStyles" },
		2099: { n: "BrtEndTimelineStyles" },
		2100: { n: "BrtBeginTimelineStyleElements" },
		2101: { n: "BrtEndTimelineStyleElements" },
		2102: { n: "BrtDxf15" },
		2103: { n: "BrtBeginDxfs15" },
		2104: { n: "brtEndDxfs15" },
		2105: { n: "BrtSlicerCacheHideItemsWithNoData" },
		2106: { n: "BrtBeginItemUniqueNames" },
		2107: { n: "BrtEndItemUniqueNames" },
		2108: { n: "BrtItemUniqueName" },
		2109: { n: "BrtBeginExtConn15" },
		2110: { n: "BrtEndExtConn15" },
		2111: { n: "BrtBeginOledbPr15" },
		2112: { n: "BrtEndOledbPr15" },
		2113: { n: "BrtBeginDataFeedPr15" },
		2114: { n: "BrtEndDataFeedPr15" },
		2115: { n: "BrtTextPr15" },
		2116: { n: "BrtRangePr15" },
		2117: { n: "BrtDbCommand15" },
		2118: { n: "BrtBeginDbTables15" },
		2119: { n: "BrtEndDbTables15" },
		2120: { n: "BrtDbTable15" },
		2121: { n: "BrtBeginDataModel" },
		2122: { n: "BrtEndDataModel" },
		2123: { n: "BrtBeginModelTables" },
		2124: { n: "BrtEndModelTables" },
		2125: { n: "BrtModelTable" },
		2126: { n: "BrtBeginModelRelationships" },
		2127: { n: "BrtEndModelRelationships" },
		2128: { n: "BrtModelRelationship" },
		2129: { n: "BrtBeginECTxtWiz15" },
		2130: { n: "BrtEndECTxtWiz15" },
		2131: { n: "BrtBeginECTWFldInfoLst15" },
		2132: { n: "BrtEndECTWFldInfoLst15" },
		2133: { n: "BrtBeginECTWFldInfo15" },
		2134: { n: "BrtFieldListActiveItem" },
		2135: { n: "BrtPivotCacheIdVersion" },
		2136: { n: "BrtSXDI15" },
		2137: { n: "BrtBeginModelTimeGroupings" },
		2138: { n: "BrtEndModelTimeGroupings" },
		2139: { n: "BrtBeginModelTimeGrouping" },
		2140: { n: "BrtEndModelTimeGrouping" },
		2141: { n: "BrtModelTimeGroupingCalcCol" },
		3072: { n: "BrtUid" },
		3073: { n: "BrtRevisionPtr" },
		65535: { n: "" }
	};
	var ov = X(fv, "n");
	var lv = { 3: { n: "BIFF2NUM", f: zs }, 4: { n: "BIFF2STR", f: Vs }, 6: { n: "Formula", f: xh }, 9: { n: "BOF", f: wi }, 10: { n: "EOF", f: Dn }, 12: { n: "CalcCount", f: Ln }, 13: { n: "CalcMode", f: Ln }, 14: { n: "CalcPrecision", f: Pn }, 15: { n: "CalcRefMode", f: Pn }, 16: { n: "CalcDelta", f: Wt }, 17: { n: "CalcIter", f: Pn }, 18: { n: "Protect", f: Pn }, 19: { n: "Password", f: Ln }, 20: { n: "Header", f: ss }, 21: { n: "Footer", f: ss }, 23: { n: "ExternSheet", f: hs }, 24: { n: "Lbl", f: cs }, 25: { n: "WinProtect", f: Pn }, 26: { n: "VerticalPageBreaks" }, 27: { n: "HorizontalPageBreaks" }, 28: { n: "Note", f: bs }, 29: { n: "Selection" }, 34: { n: "Date1904", f: Pn }, 35: { n: "ExternName", f: os }, 38: { n: "LeftMargin", f: Wt }, 39: { n: "RightMargin", f: Wt }, 40: { n: "TopMargin", f: Wt }, 41: { n: "BottomMargin", f: Wt }, 42: { n: "PrintRowCol", f: Pn }, 43: { n: "PrintGrid", f: Pn }, 47: { n: "FilePass", f: Vf }, 49: { n: "Font", f: Mi }, 51: { n: "PrintSize", f: Ln }, 60: { n: "Continue" }, 61: { n: "Window1", f: Fi }, 64: { n: "Backup", f: Pn }, 65: { n: "Pane" }, 66: { n: "CodePage", f: Ln }, 77: { n: "Pls" }, 80: { n: "DCon" }, 81: { n: "DConRef" }, 82: { n: "DConName" }, 85: { n: "DefColWidth", f: Ln }, 89: { n: "XCT" }, 90: { n: "CRN" }, 91: { n: "FileSharing" }, 92: { n: "WriteAccess", f: Si }, 93: { n: "Obj", f: Es }, 94: { n: "Uncalced" }, 95: { n: "CalcSaveRecalc", f: Pn }, 96: { n: "Template" }, 97: { n: "Intl" }, 99: { n: "ObjProtect", f: Pn }, 125: { n: "ColInfo", f: Fs }, 128: { n: "Guts", f: es }, 129: { n: "WsBool", f: _i }, 130: { n: "GridSet", f: Ln }, 131: { n: "HCenter", f: Pn }, 132: { n: "VCenter", f: Pn }, 133: { n: "BoundSheet8", f: Bi }, 134: { n: "WriteProtect" }, 140: { n: "Country", f: ys }, 141: { n: "HideObj", f: Ln }, 144: { n: "Sort" }, 146: { n: "Palette", f: Ds }, 151: { n: "Sync" }, 152: { n: "LPr" }, 153: { n: "DxGCol" }, 154: { n: "FnGroupName" }, 155: { n: "FilterMode" }, 156: { n: "BuiltInFnGroupCount", f: Ln }, 157: { n: "AutoFilterInfo" }, 158: { n: "AutoFilter" }, 160: { n: "Scl", f: Us }, 161: { n: "Setup", f: Ps }, 174: { n: "ScenMan" }, 175: { n: "SCENARIO" }, 176: { n: "SxView" }, 177: { n: "Sxvd" }, 178: { n: "SXVI" }, 180: { n: "SxIvd" }, 181: { n: "SXLI" }, 182: { n: "SXPI" }, 184: { n: "DocRoute" }, 185: { n: "RecipName" }, 189: { n: "MulRk", f: $i }, 190: { n: "MulBlank", f: Zi }, 193: { n: "Mms", f: Dn }, 197: { n: "SXDI" }, 198: { n: "SXDB" }, 199: { n: "SXFDB" }, 200: { n: "SXDBB" }, 201: { n: "SXNum" }, 202: { n: "SxBool", f: Pn }, 203: { n: "SxErr" }, 204: { n: "SXInt" }, 205: { n: "SXString" }, 206: { n: "SXDtr" }, 207: { n: "SxNil" }, 208: { n: "SXTbl" }, 209: { n: "SXTBRGIITM" }, 210: { n: "SxTbpg" }, 211: { n: "ObProj" }, 213: { n: "SXStreamID" }, 215: { n: "DBCell" }, 216: { n: "SXRng" }, 217: { n: "SxIsxoper" }, 218: { n: "BookBool", f: Ln }, 220: { n: "DbOrParamQry" }, 221: { n: "ScenarioProtect", f: Pn }, 222: { n: "OleObjectSize" }, 224: { n: "XF", f: Ji }, 225: { n: "InterfaceHdr", f: ki }, 226: { n: "InterfaceEnd", f: Dn }, 227: { n: "SXVS" }, 229: { n: "MergeCells", f: Cs }, 233: { n: "BkHim" }, 235: { n: "MsoDrawingGroup" }, 236: { n: "MsoDrawing" }, 237: { n: "MsoDrawingSelection" }, 239: { n: "PhoneticInfo" }, 240: { n: "SxRule" }, 241: { n: "SXEx" }, 242: { n: "SxFilt" }, 244: { n: "SxDXF" }, 245: { n: "SxItm" }, 246: { n: "SxName" }, 247: { n: "SxSelect" }, 248: { n: "SXPair" }, 249: { n: "SxFmla" }, 251: { n: "SxFormat" }, 252: { n: "SST", f: xi }, 253: { n: "LabelSst", f: Hi }, 255: { n: "ExtSST", f: yi }, 256: { n: "SXVDEx" }, 259: { n: "SXFormula" }, 290: { n: "SXDBEx" }, 311: { n: "RRDInsDel" }, 312: { n: "RRDHead" }, 315: { n: "RRDChgCell" }, 317: { n: "RRTabId", f: Un }, 318: { n: "RRDRenSheet" }, 319: { n: "RRSort" }, 320: { n: "RRDMove" }, 330: { n: "RRFormat" }, 331: { n: "RRAutoFmt" }, 333: { n: "RRInsertSh" }, 334: { n: "RRDMoveBegin" }, 335: { n: "RRDMoveEnd" }, 336: { n: "RRDInsDelBegin" }, 337: { n: "RRDInsDelEnd" }, 338: { n: "RRDConflict" }, 339: { n: "RRDDefName" }, 340: { n: "RRDRstEtxp" }, 351: { n: "LRng" }, 352: { n: "UsesELFs", f: Pn }, 353: { n: "DSF", f: Dn }, 401: { n: "CUsr" }, 402: { n: "CbUsr" }, 403: { n: "UsrInfo" }, 404: { n: "UsrExcl" }, 405: { n: "FileLock" }, 406: { n: "RRDInfo" }, 407: { n: "BCUsrs" }, 408: { n: "UsrChk" }, 425: { n: "UserBView" }, 426: { n: "UserSViewBegin" }, 427: { n: "UserSViewEnd" }, 428: { n: "RRDUserView" }, 429: { n: "Qsi" }, 430: { n: "SupBook", f: fs }, 431: { n: "Prot4Rev", f: Pn }, 432: { n: "CondFmt" }, 433: { n: "CF" }, 434: { n: "DVal" }, 437: { n: "DConBin" }, 438: { n: "TxO", f: As }, 439: { n: "RefreshAll", f: Pn }, 440: { n: "HLink", f: _s }, 441: { n: "Lel" }, 442: { n: "CodeName", f: Gn }, 443: { n: "SXFDBType" }, 444: { n: "Prot4RevPass", f: Ln }, 445: { n: "ObNoMacros" }, 446: { n: "Dv" }, 448: { n: "Excel9File", f: Dn }, 449: { n: "RecalcId", f: Di, r: 2 }, 450: { n: "EntExU2", f: Dn }, 512: { n: "Dimensions", f: ji }, 513: { n: "Blank", f: Ms }, 515: { n: "Number", f: ns }, 516: { n: "Label", f: Wi }, 517: { n: "BoolErr", f: ts }, 518: { n: "Formula", f: xh }, 519: { n: "String", f: Hs }, 520: { n: "Row", f: Ii }, 523: { n: "Index" }, 545: { n: "Array", f: vs }, 549: { n: "DefaultRowHeight", f: Oi }, 566: { n: "Table" }, 574: { n: "Window2", f: Ni }, 638: { n: "RK", f: Yi }, 659: { n: "Style" }, 1030: { n: "Formula", f: xh }, 1048: { n: "BigName" }, 1054: { n: "Format", f: zi }, 1084: { n: "ContinueBigName" }, 1212: { n: "ShrFmla", f: ps }, 2048: { n: "HLinkTooltip", f: Ts }, 2049: { n: "WebPub" }, 2050: { n: "QsiSXTag" }, 2051: { n: "DBQueryExt" }, 2052: { n: "ExtString" }, 2053: { n: "TxtQry" }, 2054: { n: "Qsir" }, 2055: { n: "Qsif" }, 2056: { n: "RRDTQSIF" }, 2057: { n: "BOF", f: wi }, 2058: { n: "OleDbConn" }, 2059: { n: "WOpt" }, 2060: { n: "SXViewEx" }, 2061: { n: "SXTH" }, 2062: { n: "SXPIEx" }, 2063: { n: "SXVDTEx" }, 2064: { n: "SXViewEx9" }, 2066: { n: "ContinueFrt" }, 2067: { n: "RealTimeData" }, 2128: { n: "ChartFrtInfo" }, 2129: { n: "FrtWrapper" }, 2130: { n: "StartBlock" }, 2131: { n: "EndBlock" }, 2132: { n: "StartObject" }, 2133: { n: "EndObject" }, 2134: { n: "CatLab" }, 2135: { n: "YMult" }, 2136: { n: "SXViewLink" }, 2137: { n: "PivotChartBits" }, 2138: { n: "FrtFontList" }, 2146: { n: "SheetExt" }, 2147: { n: "BookExt", r: 12 }, 2148: { n: "SXAddl" }, 2149: { n: "CrErr" }, 2150: { n: "HFPicture" }, 2151: { n: "FeatHdr", f: Dn }, 2152: { n: "Feat" }, 2154: { n: "DataLabExt" }, 2155: { n: "DataLabExtContents" }, 2156: { n: "CellWatch" }, 2161: { n: "FeatHdr11" }, 2162: { n: "Feature11" }, 2164: { n: "DropDownObjIds" }, 2165: { n: "ContinueFrt11" }, 2166: { n: "DConn" }, 2167: { n: "List12" }, 2168: { n: "Feature12" }, 2169: { n: "CondFmt12" }, 2170: { n: "CF12" }, 2171: { n: "CFEx" }, 2172: { n: "XFCRC", f: Os, r: 12 }, 2173: { n: "XFExt", f: ul, r: 12 }, 2174: { n: "AutoFilter12" }, 2175: { n: "ContinueFrt12" }, 2180: { n: "MDTInfo" }, 2181: { n: "MDXStr" }, 2182: { n: "MDXTuple" }, 2183: { n: "MDXSet" }, 2184: { n: "MDXProp" }, 2185: { n: "MDXKPI" }, 2186: { n: "MDB" }, 2187: { n: "PLV" }, 2188: { n: "Compat12", f: Pn, r: 12 }, 2189: { n: "DXF" }, 2190: { n: "TableStyles", r: 12 }, 2191: { n: "TableStyle" }, 2192: { n: "TableStyleElement" }, 2194: { n: "StyleExt" }, 2195: { n: "NamePublish" }, 2196: { n: "NameCmt", f: ds, r: 12 }, 2197: { n: "SortData" }, 2198: { n: "Theme", f: sl, r: 12 }, 2199: { n: "GUIDTypeLib" }, 2200: { n: "FnGrp12" }, 2201: { n: "NameFnGrp12" }, 2202: { n: "MTRSettings", f: gs, r: 12 }, 2203: { n: "CompressPictures", f: Dn }, 2204: { n: "HeaderFooter" }, 2205: { n: "CrtLayout12" }, 2206: { n: "CrtMlFrt" }, 2207: { n: "CrtMlFrtContinue" }, 2211: { n: "ForceFullCalculation", f: Ri }, 2212: { n: "ShapePropsStream" }, 2213: { n: "TextPropsStream" }, 2214: { n: "RichTextStream" }, 2215: { n: "CrtLayout12A" }, 4097: { n: "Units" }, 4098: { n: "Chart" }, 4099: { n: "Series" }, 4102: { n: "DataFormat" }, 4103: { n: "LineFormat" }, 4105: { n: "MarkerFormat" }, 4106: { n: "AreaFormat" }, 4107: { n: "PieFormat" }, 4108: { n: "AttachedLabel" }, 4109: { n: "SeriesText" }, 4116: { n: "ChartFormat" }, 4117: { n: "Legend" }, 4118: { n: "SeriesList" }, 4119: { n: "Bar" }, 4120: { n: "Line" }, 4121: { n: "Pie" }, 4122: { n: "Area" }, 4123: { n: "Scatter" }, 4124: { n: "CrtLine" }, 4125: { n: "Axis" }, 4126: { n: "Tick" }, 4127: { n: "ValueRange" }, 4128: { n: "CatSerRange" }, 4129: { n: "AxisLine" }, 4130: { n: "CrtLink" }, 4132: { n: "DefaultText" }, 4133: { n: "Text" }, 4134: { n: "FontX", f: Ln }, 4135: { n: "ObjectLink" }, 4146: { n: "Frame" }, 4147: { n: "Begin" }, 4148: { n: "End" }, 4149: { n: "PlotArea" }, 4154: { n: "Chart3d" }, 4156: { n: "PicF" }, 4157: { n: "DropBar" }, 4158: { n: "Radar" }, 4159: { n: "Surf" }, 4160: { n: "RadarArea" }, 4161: { n: "AxisParent" }, 4163: { n: "LegendException" }, 4164: { n: "ShtProps", f: Ns }, 4165: { n: "SerToCrt" }, 4166: { n: "AxesUsed" }, 4168: { n: "SBaseRef" }, 4170: { n: "SerParent" }, 4171: { n: "SerAuxTrend" }, 4174: { n: "IFmtRecord" }, 4175: { n: "Pos" }, 4176: { n: "AlRuns" }, 4177: { n: "BRAI" }, 4187: { n: "SerAuxErrBar" }, 4188: { n: "ClrtClient", f: Rs }, 4189: { n: "SerFmt" }, 4191: { n: "Chart3DBarShape" }, 4192: { n: "Fbi" }, 4193: { n: "BopPop" }, 4194: { n: "AxcExt" }, 4195: { n: "Dat" }, 4196: { n: "PlotGrowth" }, 4197: { n: "SIIndex" }, 4198: { n: "GelFrame" }, 4199: { n: "BopPopCustom" }, 4200: { n: "Fbi2" }, 0: { n: "Dimensions", f: ji }, 2: { n: "BIFF2INT", f: Gs }, 5: { n: "BoolErr", f: ts }, 7: { n: "String", f: Ks }, 8: { n: "BIFF2ROW" }, 11: { n: "Index" }, 22: { n: "ExternCount", f: Ln }, 30: { n: "BIFF2FORMAT", f: Gi }, 31: { n: "BIFF2FMTCNT" }, 32: { n: "BIFF2COLINFO" }, 33: { n: "Array", f: vs }, 37: { n: "DefaultRowHeight", f: Oi }, 50: { n: "BIFF2FONTXTRA", f: Ys }, 52: { n: "DDEObjName" }, 62: { n: "BIFF2WINDOW2" }, 67: { n: "BIFF2XF" }, 69: { n: "BIFF2FONTCLR" }, 86: { n: "BIFF4FMTCNT" }, 126: { n: "RK" }, 127: { n: "ImData", f: Ws }, 135: { n: "Addin" }, 136: { n: "Edg" }, 137: { n: "Pub" }, 145: { n: "Sub" }, 148: { n: "LHRecord" }, 149: { n: "LHNGraph" }, 150: { n: "Sound" }, 169: { n: "CoordList" }, 171: { n: "GCW" }, 188: { n: "ShrFmla" }, 191: { n: "ToolbarHdr" }, 192: { n: "ToolbarEnd" }, 194: { n: "AddMenu" }, 195: { n: "DelMenu" }, 214: { n: "RString", f: $s }, 223: { n: "UDDesc" }, 234: { n: "TabIdConf" }, 354: { n: "XL5Modify" }, 421: { n: "FileSharing2" }, 521: { n: "BOF", f: wi }, 536: { n: "Lbl", f: cs }, 547: { n: "ExternName", f: os }, 561: { n: "Font" }, 579: { n: "BIFF3XF" }, 1033: { n: "BOF", f: wi }, 1091: { n: "BIFF4XF" }, 2157: { n: "FeatInfo" }, 2163: { n: "FeatInfo11" }, 2177: { n: "SXAddl12" }, 2240: { n: "AutoWebPub" }, 2241: { n: "ListObj" }, 2242: { n: "ListField" }, 2243: { n: "ListDV" }, 2244: { n: "ListCondFmt" }, 2245: { n: "ListCF" }, 2246: { n: "FMQry" }, 2247: { n: "FMSQry" }, 2248: { n: "PLV" }, 2249: { n: "LnExt" }, 2250: { n: "MkrExt" }, 2251: { n: "CrtCoopt" }, 2262: { n: "FRTArchId$", r: 12 }, 29282: {} };
	var cv = X(lv, "n");

	function hv(e, r, t, a) { var n = +r || +cv[r]; if(isNaN(n)) return; var i = a || (t || []).length || 0; var s = e.next(4);
		s._W(2, n);
		s._W(2, i); if(i > 0 && Tr(t)) e.push(t) }

	function uv(e, r, t) { if(!e) e = Vr(7);
		e._W(2, r);
		e._W(2, t);
		e._W(2, 0);
		e._W(1, 0); return e }

	function dv(e, r, t, a) { var n = Vr(9);
		uv(n, e, r); if(a == "e") { n._W(1, t);
			n._W(1, 1) } else { n._W(1, t ? 1 : 0);
			n._W(1, 0) } return n }

	function pv(e, r, t) { var a = Vr(8 + 2 * t.length);
		uv(a, e, r);
		a._W(1, t.length);
		a._W(t.length, t, "sbcs"); return a.l < a.length ? a.slice(0, a.l) : a }

	function vv(e, r, t, a) { if(r.v != null) switch(r.t) {
			case "d":
				;
			case "n":
				var n = r.t == "d" ? Q(te(r.v)) : r.v; if(n == (n | 0) && n >= 0 && n < 65536) hv(e, 2, js(t, a, n));
				else hv(e, 3, Xs(t, a, n)); return;
			case "b":
				;
			case "e":
				hv(e, 5, dv(t, a, r.v, r.t)); return;
			case "s":
				;
			case "str":
				hv(e, 4, pv(t, a, r.v)); return; } hv(e, 1, uv(null, t, a)) }

	function gv(e, r, t, a) { var n = Array.isArray(r); var i = ht(r["!ref"] || "A1"),
			s, f = "",
			o = []; if(i.e.c > 255 || i.e.r > 16383) { if(a.WTF) throw new Error("Range " + (r["!ref"] || "A1") + " exceeds format limit A1:IV16384");
			i.e.c = Math.min(i.e.c, 255);
			i.e.r = Math.min(i.e.c, 16383);
			s = ct(i) } for(var l = i.s.r; l <= i.e.r; ++l) { f = qr(l); for(var c = i.s.c; c <= i.e.c; ++c) { if(l === i.s.r) o[c] = at(c);
				s = o[c] + f; var h = n ? (r[l] || [])[c] : r[s]; if(!h) continue;
				vv(e, h, l, c, a) } } }

	function mv(e, r) { var t = r || {}; if(g != null && t.dense == null) t.dense = g; var a = Xr(); var n = 0; for(var i = 0; i < e.SheetNames.length; ++i)
			if(e.SheetNames[i] == t.sheet) n = i; if(n == 0 && !!t.sheet && e.SheetNames[0] != t.sheet) throw new Error("Sheet not found: " + t.sheet);
		hv(a, 9, Ei(e, 16, t));
		gv(a, e.Sheets[e.SheetNames[n]], n, t, e);
		hv(a, 10); return a.end() }

	function bv(e, r, t) { hv(e, "Font", Ui({ sz: 12, color: { theme: 1 }, name: "Arial", family: 2, scheme: "minor" }, t)) }

	function Cv(e, r, t) { if(!r) return;
		[
			[5, 8],
			[23, 26],
			[41, 44],
			[50, 392]
		].forEach(function(a) { for(var n = a[0]; n <= a[1]; ++n)
				if(r[n] != null) hv(e, "Format", Xi(n, r[n], t)) }) }

	function wv(e, r) { var t = Vr(19);
		t._W(4, 2151);
		t._W(4, 0);
		t._W(4, 0);
		t._W(2, 3);
		t._W(1, 1);
		t._W(4, 0);
		hv(e, "FeatHdr", t);
		t = Vr(39);
		t._W(4, 2152);
		t._W(4, 0);
		t._W(4, 0);
		t._W(2, 3);
		t._W(1, 0);
		t._W(4, 0);
		t._W(2, 1);
		t._W(4, 4);
		t._W(2, 0);
		hi(ht(r["!ref"] || "A1"), t);
		t._W(4, 4);
		hv(e, "Feat", t) }

	function Ev(e, r) { for(var t = 0; t < 16; ++t) hv(e, "XF", qi({ numFmtId: 0, style: true }, 0, r));
		r.cellXfs.forEach(function(t) { hv(e, "XF", qi(t, 0, r)) }) }

	function kv(e, r) { for(var t = 0; t < r["!links"].length; ++t) { var a = r["!links"][t];
			hv(e, "HLink", Bs(a)); if(a[1].Tooltip) hv(e, "HLinkTooltip", xs(a)) } delete r["!links"] }

	function Sv(e, r, t, a, n) { var i = 16 + Kh(n.cellXfs, r, n); if(r.v != null) switch(r.t) {
			case "d":
				;
			case "n":
				var s = r.t == "d" ? Q(te(r.v)) : r.v;
				hv(e, "Number", is(t, a, s, i, n)); return;
			case "b":
				;
			case "e":
				hv(e, 517, as(t, a, r.v, i, n, r.t)); return;
			case "s":
				;
			case "str":
				hv(e, "Label", Vi(t, a, r.v, i, n)); return; } hv(e, "Blank", ni(t, a, i)) }

	function Av(e, r, t) { var a = Xr(); var n = t.SheetNames[e],
			i = t.Sheets[n] || {}; var s = (t || {}).Workbook || {}; var f = (s.Sheets || [])[e] || {}; var o = Array.isArray(i); var l = r.biff == 8; var c, h = "",
			u = []; var d = ht(i["!ref"] || "A1"); var p = l ? 65536 : 16384; if(d.e.c > 255 || d.e.r >= p) { if(r.WTF) throw new Error("Range " + (i["!ref"] || "A1") + " exceeds format limit A1:IV16384");
			d.e.c = Math.min(d.e.c, 255);
			d.e.r = Math.min(d.e.c, p - 1) } hv(a, 2057, Ei(t, 16, r));
		hv(a, "CalcMode", Mn(1));
		hv(a, "CalcCount", Mn(100));
		hv(a, "CalcRefMode", Nn(true));
		hv(a, "CalcIter", Nn(false));
		hv(a, "CalcDelta", Vt(.001));
		hv(a, "CalcSaveRecalc", Nn(true));
		hv(a, "PrintRowCol", Nn(false));
		hv(a, "PrintGrid", Nn(false));
		hv(a, "GridSet", Mn(1));
		hv(a, "Guts", rs([0, 0]));
		hv(a, "HCenter", Nn(false));
		hv(a, "VCenter", Nn(false));
		hv(a, "Dimensions", Ki(d, r)); if(l) i["!links"] = []; for(var v = d.s.r; v <= d.e.r; ++v) { h = qr(v); for(var g = d.s.c; g <= d.e.c; ++g) { if(v === d.s.r) u[g] = at(g);
				c = u[g] + h; var m = o ? (i[v] || [])[g] : i[c]; if(!m) continue;
				Sv(a, m, v, g, r); if(l && m.l) i["!links"].push([c, m.l]) } } var b = f.CodeName || f.name || n; if(l && s.Views) hv(a, "Window2", Li(s.Views[0])); if(l && (i["!merges"] || []).length) hv(a, "MergeCells", ws(i["!merges"])); if(l) kv(a, i);
		hv(a, "CodeName", Kn(b, r)); if(l) wv(a, i);
		hv(a, "EOF"); return a.end() }

	function _v(e, r, t) { var a = Xr(); var n = (e || {}).Workbook || {}; var i = n.Sheets || []; var s = n.WBProps || {}; var f = t.biff == 8,
			o = t.biff == 5;
		hv(a, 2057, Ei(e, 5, t)); if(t.bookType == "xla") hv(a, "Addin");
		hv(a, "InterfaceHdr", f ? Mn(1200) : null);
		hv(a, "Mms", On(2)); if(o) hv(a, "ToolbarHdr"); if(o) hv(a, "ToolbarEnd");
		hv(a, "InterfaceEnd");
		hv(a, "WriteAccess", Ai("SheetJS", t));
		hv(a, "CodePage", Mn(f ? 1200 : 1252)); if(f) hv(a, "DSF", Mn(0)); if(f) hv(a, "Excel9File");
		hv(a, "RRTabId", Ls(e.SheetNames.length)); if(f && e.vbaraw) { hv(a, "ObProj"); var l = s.CodeName || "ThisWorkbook";
			hv(a, "CodeName", Kn(l, t)) } hv(a, "BuiltInFnGroupCount", Mn(17));
		hv(a, "WinProtect", Nn(false));
		hv(a, "Protect", Nn(false));
		hv(a, "Password", Mn(0)); if(f) hv(a, "Prot4Rev", Nn(false)); if(f) hv(a, "Prot4RevPass", Mn(0));
		hv(a, "Window1", Pi(t));
		hv(a, "Backup", Nn(false));
		hv(a, "HideObj", Mn(0));
		hv(a, "Date1904", Nn(Ud(e) == "true"));
		hv(a, "CalcPrecision", Nn(true)); if(f) hv(a, "RefreshAll", Nn(false));
		hv(a, "BookBool", Mn(0));
		bv(a, e, t);
		Cv(a, e.SSF, t);
		Ev(a, t); if(f) hv(a, "UsesELFs", Nn(false)); var c = a.end(); var h = Xr(); if(f) hv(h, "Country", Is());
		hv(h, "EOF"); var u = h.end(); var d = Xr(); var p = 0,
			v = 0; for(v = 0; v < e.SheetNames.length; ++v) p += (f ? 12 : 11) + (f ? 2 : 1) * e.SheetNames[v].length; var g = c.length + p + u.length; for(v = 0; v < e.SheetNames.length; ++v) { var m = i[v] || {};
			hv(d, "BoundSheet8", Ti({ pos: g, hs: m.Hidden || 0, dt: 0, name: e.SheetNames[v] }, t));
			g += r[v].length } var b = d.end(); if(p != b.length) throw new Error("BS8 " + p + " != " + b.length); var C = []; if(c.length) C.push(c); if(b.length) C.push(b); if(u.length) C.push(u); return fr([C]) }

	function Bv(e, r) { var t = r || {}; var a = []; if(e && !e.SSF) { e.SSF = y.get_table() } if(e && e.SSF) { I(y);
			y.load_table(e.SSF);
			t.revssf = j(e.SSF);
			t.revssf[e.SSF[65535]] = 0;
			t.ssf = e.SSF } t.cellXfs = [];
		t.Strings = [];
		t.Strings.Count = 0;
		t.Strings.Unique = 0;
		Kh(t.cellXfs, {}, { revssf: { General: 0 } }); for(var n = 0; n < e.SheetNames.length; ++n) a[a.length] = Av(n, t, e);
		a.unshift(_v(e, a, t)); return fr([a]) }

	function Tv(e, r) { var t = r || {}; switch(t.biff || 2) {
			case 8:
				;
			case 5:
				return Bv(e, r);
			case 4:
				;
			case 3:
				;
			case 2:
				return mv(e, r); } throw new Error("invalid type " + t.bookType + " for BIFF") }
	var xv = function() {
		function e(e, r) { var t = r || {}; if(g != null && t.dense == null) t.dense = g; var a = t.dense ? [] : {}; var n = e.match(/<table/i); if(!n) throw new Error("Invalid HTML: could not find <table>"); var i = e.match(/<\/table/i); var s = n.index,
				f = i && i.index || e.length; var o = le(e.slice(s, f), /(:?<tr[^>]*>)/i, "<tr>"); var l = -1,
				c = 0,
				h = 0,
				u = 0; var d = { s: { r: 1e7, c: 1e7 }, e: { r: 0, c: 0 } }; var p = []; for(s = 0; s < o.length; ++s) { var v = o[s].trim(); var m = v.slice(0, 3).toLowerCase(); if(m == "<tr") {++l; if(t.sheetRows && t.sheetRows <= l) {--l; break } c = 0; continue } if(m != "<td" && m != "<th") continue; var b = v.split(/<\/t[dh]>/i); for(f = 0; f < b.length; ++f) { var C = b[f].trim(); if(!C.match(/<t[dh]/i)) continue; var w = C,
						E = 0; while(w.charAt(0) == "<" && (E = w.indexOf(">")) > -1) w = w.slice(E + 1); var k = _e(C.slice(0, C.indexOf(">")));
					u = k.colspan ? +k.colspan : 1; if((h = +k.rowspan) > 0 || u > 1) p.push({ s: { r: l, c: c }, e: { r: l + (h || 1) - 1, c: c + u - 1 } }); var S = k.t || ""; if(!w.length) { c += u; continue } w = je(ye(w)); if(d.s.r > l) d.s.r = l; if(d.e.r < l) d.e.r = l; if(d.s.c > c) d.s.c = c; if(d.e.c < c) d.e.c = c; if(!w.length) continue; var A = { t: "s", v: w }; if(t.raw || !w.trim().length || S == "s") {} else if(w === "TRUE") A = { t: "b", v: true };
					else if(w === "FALSE") A = { t: "b", v: false };
					else if(!isNaN(se(w))) A = { t: "n", v: se(w) };
					else if(!isNaN(fe(w).getDate())) { A = { t: "d", v: te(w) }; if(!t.cellDates) A = { t: "n", v: Q(A.v) };
						A.z = t.dateNF || y._table[14] } if(t.dense) { if(!a[l]) a[l] = [];
						a[l][c] = A } else a[ot({ r: l, c: c })] = A;
					c += u } } a["!ref"] = ct(d); return a }

		function r(r, t) { return pt(e(r, t), t) }

		function t(e, r, t, a) { var n = e["!merges"] || []; var i = []; var s = "<td>" + (a.editable ? '<span contenteditable="true"></span>' : "") + "</td>"; for(var f = r.s.c; f <= r.e.c; ++f) { var o = 0,
					l = 0; for(var c = 0; c < n.length; ++c) { if(n[c].s.r > t || n[c].s.c > f) continue; if(n[c].e.r < t || n[c].e.c < f) continue; if(n[c].s.r < t || n[c].s.c < f) { o = -1; break } o = n[c].e.r - n[c].s.r + 1;
					l = n[c].e.c - n[c].s.c + 1; break } if(o < 0) continue; var h = ot({ r: t, c: f }); var u = a.dense ? (e[t] || [])[f] : e[h]; if(!u || u.v == null) { i.push(s); continue } var d = u.h || De(u.w || (dt(u), u.w) || ""); var p = {}; if(o > 1) p.rowspan = o; if(l > 1) p.colspan = l;
				p.t = u.t; if(a.editable) d = '<span contenteditable="true">' + d + "</span>";
				p.id = "sjs-" + h;
				i.push(er("td", d, p)) } var v = "<tr>"; return v + i.join("") + "</tr>" }

		function a(e, r, t) { var a = []; return a.join("") + "<table" + (t && t.id ? ' id="' + t.id + '"' : "") + ">" } var n = '<html><head><meta charset="utf-8"/><title>SheetJS Table Export</title></head><body>'; var i = "</body></html>";

		function s(e, r) { var s = r || {}; var f = s.header != null ? s.header : n; var o = s.footer != null ? s.footer : i; var l = [f]; var c = lt(e["!ref"]);
			s.dense = Array.isArray(e);
			l.push(a(e, c, s)); for(var h = c.s.r; h <= c.e.r; ++h) l.push(t(e, c, h, s));
			l.push("</table>" + o); return l.join("") } return { to_workbook: r, to_sheet: e, _row: t, BEGIN: n, END: i, _preamble: a, from_sheet: s } }();

	function yv(e, r) { var t = r || {}; if(g != null) t.dense = g; var a = t.dense ? [] : {}; var n = e.getElementsByTagName("tr"); var i = Math.min(t.sheetRows || 1e7, n.length); var s = { s: { r: 0, c: 0 }, e: { r: i - 1, c: 0 } }; var f = [],
			o = 0; var l = 0,
			c = 0,
			h = 0,
			u = 0,
			d = 0; for(; l < i; ++l) { var p = n[l]; var v = p.children; for(c = h = 0; c < v.length; ++c) { var m = v[c],
					b = je(v[c].innerHTML); for(o = 0; o < f.length; ++o) { var C = f[o]; if(C.s.c == h && C.s.r <= l && l <= C.e.r) { h = C.e.c + 1;
						o = -1 } } d = +m.getAttribute("colspan") || 1; if((u = +m.getAttribute("rowspan")) > 0 || d > 1) f.push({ s: { r: l, c: h }, e: { r: l + (u || 1) - 1, c: h + d - 1 } }); var w = { t: "s", v: b }; var E = m.getAttribute("t") || ""; if(b != null) { if(b.length == 0) w.t = E || "z";
					else if(t.raw || b.trim().length == 0 || E == "s") {} else if(b === "TRUE") w = { t: "b", v: true };
					else if(b === "FALSE") w = { t: "b", v: false };
					else if(!isNaN(se(b))) w = { t: "n", v: se(b) };
					else if(!isNaN(fe(b).getDate())) { w = { t: "d", v: te(b) }; if(!t.cellDates) w = { t: "n", v: Q(w.v) };
						w.z = t.dateNF || y._table[14] } } if(t.dense) { if(!a[l]) a[l] = [];
					a[l][h] = w } else a[ot({ c: h, r: l })] = w; if(s.e.c < h) s.e.c = h;
				h += d } } if(f.length) a["!merges"] = f;
		a["!ref"] = ct(s); if(i < n.length) a["!fullref"] = ct((s.e.r = n.length - 1, s)); return a }

	function Iv(e, r) { return pt(yv(e, r), r) }
	var Rv = function() {
		var e = function(e) { return ye(e.replace(/[\t\r\n]/g, " ").trim().replace(/ +/g, " ").replace(/<text:s\/>/g, " ").replace(/<text:s text:c="(\d+)"\/>/g, function(e, r) { return Array(parseInt(r, 10) + 1).join(" ") }).replace(/<text:tab[^>]*\/>/g, "\t").replace(/<text:line-break\/>/g, "\n").replace(/<[^>]*>/g, "")) };
		var r = { day: ["d", "dd"], month: ["m", "mm"], year: ["y", "yy"], hours: ["h", "hh"], minutes: ["m", "mm"], seconds: ["s", "ss"], "am-pm": ["A/P", "AM/PM"], "day-of-week": ["ddd", "dddd"], era: ["e", "ee"], quarter: ["\\Qm", 'm\\"th quarter"'] };
		return function t(a, n) {
			var i = n || {};
			if(g != null && i.dense == null) i.dense = g;
			var s = Fp(a);
			var f = [],
				o;
			var l;
			var c = { name: "" },
				h = "",
				u = 0;
			var d;
			var p;
			var v = {},
				m = [];
			var b = i.dense ? [] : {};
			var C, w;
			var E = { value: "" };
			var k = "",
				S = 0,
				A;
			var _ = -1,
				B = -1,
				T = { s: { r: 1e6, c: 1e7 }, e: { r: 0, c: 0 } };
			var x = 0;
			var y = {};
			var I = [],
				R = {},
				D = 0,
				O = 0;
			var F = [],
				P = 1,
				N = 1;
			var L = [];
			var M = { Names: [] };
			var U = {};
			var H = ["", ""];
			var W = [],
				V = {};
			var z = "",
				X = 0;
			var G = false,
				j = false;
			var K = 0;
			Pp.lastIndex = 0;
			s = s.replace(/<!--([\s\S]*?)-->/gm, "").replace(/<!DOCTYPE[^\[]*\[[^\]]*\]>/gm, "");
			while(C = Pp.exec(s)) switch(C[3] = C[3].replace(/_.*$/, "")) {
				case "table":
					;
				case "宸ヤ綔琛�":
					if(C[1] === "/") { if(T.e.c >= T.s.c && T.e.r >= T.s.r) b["!ref"] = ct(T); if(i.sheetRows > 0 && i.sheetRows <= T.e.r) { b["!fullref"] = b["!ref"];
							T.e.r = i.sheetRows - 1;
							b["!ref"] = ct(T) } if(I.length) b["!merges"] = I; if(F.length) b["!rows"] = F;
						d.name = He(d["鍚嶇О"] || d.name); if(typeof JSON !== "undefined") JSON.stringify(d);
						m.push(d.name);
						v[d.name] = b;
						j = false } else if(C[0].charAt(C[0].length - 2) !== "/") { d = _e(C[0], false);
						_ = B = -1;
						T.s.r = T.s.c = 1e7;
						T.e.r = T.e.c = 0;
						b = i.dense ? [] : {};
						I = [];
						F = [];
						j = true }
					break;
				case "table-row-group":
					if(C[1] === "/") --x;
					else ++x;
					break;
				case "table-row":
					;
				case "琛�":
					if(C[1] === "/") { _ += P;
						P = 1; break } p = _e(C[0], false);
					if(p["琛屽彿"]) _ = p["琛屽彿"] - 1;
					else if(_ == -1) _ = 0;
					P = +p["number-rows-repeated"] || 1;
					if(P < 10)
						for(K = 0; K < P; ++K)
							if(x > 0) F[_ + K] = { level: x };
					B = -1;
					break;
				case "covered-table-cell":
					++B;
					if(i.sheetStubs) { if(i.dense) { if(!b[_]) b[_] = [];
							b[_][B] = { t: "z" } } else b[ot({ r: _, c: B })] = { t: "z" } }
					break;
				case "table-cell":
					;
				case "鏁版嵁":
					if(C[0].charAt(C[0].length - 2) === "/") {++B;
						E = _e(C[0], false);
						N = parseInt(E["number-columns-repeated"] || "1", 10);
						w = { t: "z", v: null }; if(E.formula && i.cellFormula != false) w.f = Mh(ye(E.formula)); if((E["鏁版嵁绫诲瀷"] || E["value-type"]) == "string") { w.t = "s";
							w.v = ye(E["string-value"] || ""); if(i.dense) { if(!b[_]) b[_] = [];
								b[_][B] = w } else { b[ot({ r: _, c: B })] = w } } B += N - 1 } else if(C[1] !== "/") {++B;
						N = 1; var Y = P ? _ + P - 1 : _; if(B > T.e.c) T.e.c = B; if(B < T.s.c) T.s.c = B; if(_ < T.s.r) T.s.r = _; if(Y > T.e.r) T.e.r = Y;
						E = _e(C[0], false);
						W = [];
						V = {};
						w = { t: E["鏁版嵁绫诲瀷"] || E["value-type"], v: null }; if(i.cellFormula) { if(E.formula) E.formula = ye(E.formula); if(E["number-matrix-columns-spanned"] && E["number-matrix-rows-spanned"]) { D = parseInt(E["number-matrix-rows-spanned"], 10) || 0;
								O = parseInt(E["number-matrix-columns-spanned"], 10) || 0;
								R = { s: { r: _, c: B }, e: { r: _ + D - 1, c: B + O - 1 } };
								w.F = ct(R);
								L.push([R, w.F]) } if(E.formula) w.f = Mh(E.formula);
							else
								for(K = 0; K < L.length; ++K)
									if(_ >= L[K][0].s.r && _ <= L[K][0].e.r)
										if(B >= L[K][0].s.c && B <= L[K][0].e.c) w.F = L[K][1] } if(E["number-columns-spanned"] || E["number-rows-spanned"]) { D = parseInt(E["number-rows-spanned"], 10) || 0;
							O = parseInt(E["number-columns-spanned"], 10) || 0;
							R = { s: { r: _, c: B }, e: { r: _ + D - 1, c: B + O - 1 } };
							I.push(R) } if(E["number-columns-repeated"]) N = parseInt(E["number-columns-repeated"], 10); switch(w.t) {
							case "boolean":
								w.t = "b";
								w.v = Ue(E["boolean-value"]); break;
							case "float":
								w.t = "n";
								w.v = parseFloat(E.value); break;
							case "percentage":
								w.t = "n";
								w.v = parseFloat(E.value); break;
							case "currency":
								w.t = "n";
								w.v = parseFloat(E.value); break;
							case "date":
								w.t = "d";
								w.v = te(E["date-value"]); if(!i.cellDates) { w.t = "n";
									w.v = Q(w.v) } w.z = "m/d/yy"; break;
							case "time":
								w.t = "n";
								w.v = q(E["time-value"]) / 86400; break;
							case "number":
								w.t = "n";
								w.v = parseFloat(E["鏁版嵁鏁板€�"]); break;
							default:
								if(w.t === "string" || w.t === "text" || !w.t) { w.t = "s"; if(E["string-value"] != null) k = ye(E["string-value"]) } else throw new Error("Unsupported value type " + w.t); } } else { G = false; if(w.t === "s") { w.v = k || "";
							G = S == 0 } if(U.Target) w.l = U; if(W.length > 0) { w.c = W;
							W = [] } if(k && i.cellText !== false) w.w = k; if(!G || i.sheetStubs) { if(!(i.sheetRows && i.sheetRows <= _)) { for(var $ = 0; $ < P; ++$) { N = parseInt(E["number-columns-repeated"] || "1", 10); if(i.dense) { if(!b[_ + $]) b[_ + $] = [];
										b[_ + $][B] = $ == 0 ? w : ne(w); while(--N > 0) b[_ + $][B + N] = ne(w) } else { b[ot({ r: _ + $, c: B })] = w; while(--N > 0) b[ot({ r: _ + $, c: B + N })] = ne(w) } if(T.e.c <= B) T.e.c = B } } } N = parseInt(E["number-columns-repeated"] || "1", 10);
						B += N - 1;
						N = 0;
						w = {};
						k = "" } U = {};
					break;
				case "document":
					;
				case "document-content":
					;
				case "鐢靛瓙琛ㄦ牸鏂囨。":
					;
				case "spreadsheet":
					;
				case "涓讳綋":
					;
				case "scripts":
					;
				case "styles":
					;
				case "font-face-decls":
					if(C[1] === "/") { if((o = f.pop())[0] !== C[3]) throw "Bad state: " + o } else if(C[0].charAt(C[0].length - 2) !== "/") f.push([C[3], true]);
					break;
				case "annotation":
					if(C[1] === "/") { if((o = f.pop())[0] !== C[3]) throw "Bad state: " + o;
						V.t = k;
						V.a = z;
						W.push(V) } else if(C[0].charAt(C[0].length - 2) !== "/") { f.push([C[3], false]) } z = "";
					X = 0;
					k = "";
					S = 0;
					break;
				case "creator":
					if(C[1] === "/") { z = s.slice(X, C.index) } else X = C.index + C[0].length;
					break;
				case "meta":
					;
				case "鍏冩暟鎹�":
					;
				case "settings":
					;
				case "config-item-set":
					;
				case "config-item-map-indexed":
					;
				case "config-item-map-entry":
					;
				case "config-item-map-named":
					;
				case "shapes":
					;
				case "frame":
					;
				case "text-box":
					;
				case "image":
					;
				case "data-pilot-tables":
					;
				case "list-style":
					;
				case "form":
					;
				case "dde-links":
					;
				case "event-listeners":
					;
				case "chart":
					if(C[1] === "/") { if((o = f.pop())[0] !== C[3]) throw "Bad state: " + o } else if(C[0].charAt(C[0].length - 2) !== "/") f.push([C[3], false]);
					k = "";
					S = 0;
					break;
				case "scientific-number":
					break;
				case "currency-symbol":
					break;
				case "currency-style":
					break;
				case "number-style":
					;
				case "percentage-style":
					;
				case "date-style":
					;
				case "time-style":
					if(C[1] === "/") { y[c.name] = h; if((o = f.pop())[0] !== C[3]) throw "Bad state: " + o } else if(C[0].charAt(C[0].length - 2) !== "/") { h = "";
						c = _e(C[0], false);
						f.push([C[3], true]) }
					break;
				case "script":
					break;
				case "libraries":
					break;
				case "automatic-styles":
					break;
				case "master-styles":
					break;
				case "default-style":
					;
				case "page-layout":
					break;
				case "style":
					break;
				case "map":
					break;
				case "font-face":
					break;
				case "paragraph-properties":
					break;
				case "table-properties":
					break;
				case "table-column-properties":
					break;
				case "table-row-properties":
					break;
				case "table-cell-properties":
					break;
				case "number":
					switch(f[f.length - 1][0]) {
						case "time-style":
							;
						case "date-style":
							l = _e(C[0], false);
							h += r[C[3]][l.style === "long" ? 1 : 0]; break; }
					break;
				case "fraction":
					break;
				case "day":
					;
				case "month":
					;
				case "year":
					;
				case "era":
					;
				case "day-of-week":
					;
				case "week-of-year":
					;
				case "quarter":
					;
				case "hours":
					;
				case "minutes":
					;
				case "seconds":
					;
				case "am-pm":
					switch(f[f.length - 1][0]) {
						case "time-style":
							;
						case "date-style":
							l = _e(C[0], false);
							h += r[C[3]][l.style === "long" ? 1 : 0]; break; }
					break;
				case "boolean-style":
					break;
				case "boolean":
					break;
				case "text-style":
					break;
				case "text":
					if(C[0].slice(-2) === "/>") break;
					else if(C[1] === "/") switch(f[f.length - 1][0]) {
						case "number-style":
							;
						case "date-style":
							;
						case "time-style":
							h += s.slice(u, C.index); break; } else u = C.index + C[0].length;
					break;
				case "named-range":
					l = _e(C[0], false);
					H = Hh(l["cell-range-address"]);
					var Z = { Name: l.name, Ref: H[0] + "!" + H[1] };
					if(j) Z.Sheet = m.length;
					M.Names.push(Z);
					break;
				case "text-content":
					break;
				case "text-properties":
					break;
				case "embedded-text":
					break;
				case "body":
					;
				case "鐢靛瓙琛ㄦ牸":
					break;
				case "forms":
					break;
				case "table-column":
					break;
				case "table-header-rows":
					break;
				case "table-rows":
					break;
				case "table-column-group":
					break;
				case "table-header-columns":
					break;
				case "table-columns":
					break;
				case "null-date":
					break;
				case "graphic-properties":
					break;
				case "calculation-settings":
					break;
				case "named-expressions":
					break;
				case "label-range":
					break;
				case "label-ranges":
					break;
				case "named-expression":
					break;
				case "sort":
					break;
				case "sort-by":
					break;
				case "sort-groups":
					break;
				case "tab":
					break;
				case "line-break":
					break;
				case "span":
					break;
				case "p":
					;
				case "鏂囨湰涓�":
					if(C[1] === "/" && (!E || !E["string-value"])) k = (k.length > 0 ? k + "\n" : "") + e(s.slice(S, C.index), A);
					else { A = _e(C[0], false);
						S = C.index + C[0].length }
					break;
				case "s":
					break;
				case "database-range":
					if(C[1] === "/") break;
					try { H = Hh(_e(C[0])["target-range-address"]);
						v[H[0]]["!autofilter"] = { ref: H[1] } } catch(J) {}
					break;
				case "date":
					break;
				case "object":
					break;
				case "title":
					;
				case "鏍囬":
					break;
				case "desc":
					break;
				case "binary-data":
					break;
				case "table-source":
					break;
				case "scenario":
					break;
				case "iteration":
					break;
				case "content-validations":
					break;
				case "content-validation":
					break;
				case "help-message":
					break;
				case "error-message":
					break;
				case "database-ranges":
					break;
				case "filter":
					break;
				case "filter-and":
					break;
				case "filter-or":
					break;
				case "filter-condition":
					break;
				case "list-level-style-bullet":
					break;
				case "list-level-style-number":
					break;
				case "list-level-properties":
					break;
				case "sender-firstname":
					;
				case "sender-lastname":
					;
				case "sender-initials":
					;
				case "sender-title":
					;
				case "sender-position":
					;
				case "sender-email":
					;
				case "sender-phone-private":
					;
				case "sender-fax":
					;
				case "sender-company":
					;
				case "sender-phone-work":
					;
				case "sender-street":
					;
				case "sender-city":
					;
				case "sender-postal-code":
					;
				case "sender-country":
					;
				case "sender-state-or-province":
					;
				case "author-name":
					;
				case "author-initials":
					;
				case "chapter":
					;
				case "file-name":
					;
				case "template-name":
					;
				case "sheet-name":
					break;
				case "event-listener":
					break;
				case "initial-creator":
					;
				case "creation-date":
					;
				case "print-date":
					;
				case "generator":
					;
				case "document-statistic":
					;
				case "user-defined":
					;
				case "editing-duration":
					;
				case "editing-cycles":
					break;
				case "config-item":
					break;
				case "page-number":
					break;
				case "page-count":
					break;
				case "time":
					break;
				case "cell-range-source":
					break;
				case "detective":
					break;
				case "operation":
					break;
				case "highlighted-range":
					break;
				case "data-pilot-table":
					;
				case "source-cell-range":
					;
				case "source-service":
					;
				case "data-pilot-field":
					;
				case "data-pilot-level":
					;
				case "data-pilot-subtotals":
					;
				case "data-pilot-subtotal":
					;
				case "data-pilot-members":
					;
				case "data-pilot-member":
					;
				case "data-pilot-display-info":
					;
				case "data-pilot-sort-info":
					;
				case "data-pilot-layout-info":
					;
				case "data-pilot-field-reference":
					;
				case "data-pilot-groups":
					;
				case "data-pilot-group":
					;
				case "data-pilot-group-member":
					break;
				case "rect":
					break;
				case "dde-connection-decls":
					;
				case "dde-connection-decl":
					;
				case "dde-link":
					;
				case "dde-source":
					break;
				case "properties":
					break;
				case "property":
					break;
				case "a":
					if(C[1] !== "/") { U = _e(C[0], false); if(!U.href) break;
						U.Target = U.href;
						delete U.href; if(U.Target.charAt(0) == "#" && U.Target.indexOf(".") > -1) { H = Hh(U.Target.slice(1));
							U.Target = "#" + H[0] + "!" + H[1] } }
					break;
				case "table-protection":
					break;
				case "data-pilot-grand-total":
					break;
				case "office-document-common-attrs":
					break;
				default:
					switch(C[2]) {
						case "dc:":
							;
						case "calcext:":
							;
						case "loext:":
							;
						case "ooo:":
							;
						case "chartooo:":
							;
						case "draw:":
							;
						case "style:":
							;
						case "chart:":
							;
						case "form:":
							;
						case "uof:":
							;
						case "琛�:":
							;
						case "瀛�:":
							break;
						default:
							if(i.WTF) throw new Error(C); };
			}
			var ee = { Sheets: v, SheetNames: m, Workbook: M };
			if(i.bookSheets) delete ee.Sheets;
			return ee
		}
	}();

	function Dv(e, r) { r = r || {}; var t = !!de(e, "objectdata"); if(t) Pa(ve(e, "META-INF/manifest.xml"), r); var a = ge(e, "content.xml"); if(!a) throw new Error("Missing content.xml in " + (t ? "ODS" : "UOF") + " file"); var n = Rv(t ? a : He(a), r); if(de(e, "meta.xml")) n.Props = za(ve(e, "meta.xml")); return n }

	function Ov(e, r) { return Rv(e, r) }
	var Fv = function() { var e = "<office:document-styles " + qe({ "xmlns:office": "urn:oasis:names:tc:opendocument:xmlns:office:1.0", "xmlns:table": "urn:oasis:names:tc:opendocument:xmlns:table:1.0", "xmlns:style": "urn:oasis:names:tc:opendocument:xmlns:style:1.0", "xmlns:text": "urn:oasis:names:tc:opendocument:xmlns:text:1.0", "xmlns:draw": "urn:oasis:names:tc:opendocument:xmlns:drawing:1.0", "xmlns:fo": "urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0", "xmlns:xlink": "http://www.w3.org/1999/xlink", "xmlns:dc": "http://purl.org/dc/elements/1.1/", "xmlns:number": "urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0", "xmlns:svg": "urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0", "xmlns:of": "urn:oasis:names:tc:opendocument:xmlns:of:1.2", "office:version": "1.2" }) + "></office:document-styles>"; return function r() { return we + e } }();
	var Pv = function() { var e = function(e) { return De(e).replace(/  +/g, function(e) { return '<text:s text:c="' + e.length + '"/>' }).replace(/\t/g, "<text:tab/>").replace(/\n/g, "<text:line-break/>").replace(/^ /, "<text:s/>").replace(/ $/, "<text:s/>") }; var r = "          <table:table-cell />\n"; var t = "          <table:covered-table-cell/>\n"; var a = function(a, n, i) { var s = [];
			s.push('      <table:table table:name="' + De(n.SheetNames[i]) + '">\n'); var f = 0,
				o = 0,
				l = lt(a["!ref"]); var c = a["!merges"] || [],
				h = 0; var u = Array.isArray(a); for(f = 0; f < l.s.r; ++f) s.push("        <table:table-row></table:table-row>\n"); for(; f <= l.e.r; ++f) { s.push("        <table:table-row>\n"); for(o = 0; o < l.s.c; ++o) s.push(r); for(; o <= l.e.c; ++o) { var d = false,
						p = {},
						v = ""; for(h = 0; h != c.length; ++h) { if(c[h].s.c > o) continue; if(c[h].s.r > f) continue; if(c[h].e.c < o) continue; if(c[h].e.r < f) continue; if(c[h].s.c != o || c[h].s.r != f) d = true;
						p["table:number-columns-spanned"] = c[h].e.c - c[h].s.c + 1;
						p["table:number-rows-spanned"] = c[h].e.r - c[h].s.r + 1; break } if(d) { s.push(t); continue } var g = ot({ r: f, c: o }),
						m = u ? (a[f] || [])[o] : a[g]; if(m && m.f) { p["table:formula"] = De(Uh(m.f)); if(m.F) { if(m.F.slice(0, g.length) == g) { var b = lt(m.F);
								p["table:number-matrix-columns-spanned"] = b.e.c - b.s.c + 1;
								p["table:number-matrix-rows-spanned"] = b.e.r - b.s.r + 1 } } } if(!m) { s.push(r); continue } switch(m.t) {
						case "b":
							v = m.v ? "TRUE" : "FALSE";
							p["office:value-type"] = "boolean";
							p["office:boolean-value"] = m.v ? "true" : "false"; break;
						case "n":
							v = m.w || String(m.v || 0);
							p["office:value-type"] = "float";
							p["office:value"] = m.v || 0; break;
						case "s":
							;
						case "str":
							v = m.v;
							p["office:value-type"] = "string"; break;
						case "d":
							v = m.w || te(m.v).toISOString();
							p["office:value-type"] = "date";
							p["office:date-value"] = te(m.v).toISOString();
							p["table:style-name"] = "ce1"; break;
						default:
							s.push(r); continue; } var C = e(v); if(m.l && m.l.Target) { var w = m.l.Target;
						w = w.charAt(0) == "#" ? "#" + Wh(w.slice(1)) : w;
						C = er("text:a", C, { "xlink:href": w }) } s.push("          " + er("table:table-cell", er("text:p", C, {}), p) + "\n") } s.push("        </table:table-row>\n") } s.push("      </table:table>\n"); return s.join("") }; var n = function(e) { e.push(" <office:automatic-styles>\n");
			e.push('  <number:date-style style:name="N37" number:automatic-order="true">\n');
			e.push('   <number:month number:style="long"/>\n');
			e.push("   <number:text>/</number:text>\n");
			e.push('   <number:day number:style="long"/>\n');
			e.push("   <number:text>/</number:text>\n");
			e.push("   <number:year/>\n");
			e.push("  </number:date-style>\n");
			e.push('  <style:style style:name="ce1" style:family="table-cell" style:parent-style-name="Default" style:data-style-name="N37"/>\n');
			e.push(" </office:automatic-styles>\n") }; return function i(e, r) { var t = [we]; var i = qe({ "xmlns:office": "urn:oasis:names:tc:opendocument:xmlns:office:1.0", "xmlns:table": "urn:oasis:names:tc:opendocument:xmlns:table:1.0", "xmlns:style": "urn:oasis:names:tc:opendocument:xmlns:style:1.0", "xmlns:text": "urn:oasis:names:tc:opendocument:xmlns:text:1.0", "xmlns:draw": "urn:oasis:names:tc:opendocument:xmlns:drawing:1.0", "xmlns:fo": "urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0", "xmlns:xlink": "http://www.w3.org/1999/xlink", "xmlns:dc": "http://purl.org/dc/elements/1.1/", "xmlns:meta": "urn:oasis:names:tc:opendocument:xmlns:meta:1.0", "xmlns:number": "urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0", "xmlns:presentation": "urn:oasis:names:tc:opendocument:xmlns:presentation:1.0", "xmlns:svg": "urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0", "xmlns:chart": "urn:oasis:names:tc:opendocument:xmlns:chart:1.0", "xmlns:dr3d": "urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0", "xmlns:math": "http://www.w3.org/1998/Math/MathML", "xmlns:form": "urn:oasis:names:tc:opendocument:xmlns:form:1.0", "xmlns:script": "urn:oasis:names:tc:opendocument:xmlns:script:1.0", "xmlns:ooo": "http://openoffice.org/2004/office", "xmlns:ooow": "http://openoffice.org/2004/writer", "xmlns:oooc": "http://openoffice.org/2004/calc", "xmlns:dom": "http://www.w3.org/2001/xml-events", "xmlns:xforms": "http://www.w3.org/2002/xforms", "xmlns:xsd": "http://www.w3.org/2001/XMLSchema", "xmlns:xsi": "http://www.w3.org/2001/XMLSchema-instance", "xmlns:sheet": "urn:oasis:names:tc:opendocument:sh33tjs:1.0", "xmlns:rpt": "http://openoffice.org/2005/report", "xmlns:of": "urn:oasis:names:tc:opendocument:xmlns:of:1.2", "xmlns:xhtml": "http://www.w3.org/1999/xhtml", "xmlns:grddl": "http://www.w3.org/2003/g/data-view#", "xmlns:tableooo": "http://openoffice.org/2009/table", "xmlns:drawooo": "http://openoffice.org/2010/draw", "xmlns:calcext": "urn:org:documentfoundation:names:experimental:calc:xmlns:calcext:1.0", "xmlns:loext": "urn:org:documentfoundation:names:experimental:office:xmlns:loext:1.0", "xmlns:field": "urn:openoffice:names:experimental:ooo-ms-interop:xmlns:field:1.0", "xmlns:formx": "urn:openoffice:names:experimental:ooxml-odf-interop:xmlns:form:1.0", "xmlns:css3t": "http://www.w3.org/TR/css3-text/", "office:version": "1.2" }); var s = qe({ "xmlns:config": "urn:oasis:names:tc:opendocument:xmlns:config:1.0", "office:mimetype": "application/vnd.oasis.opendocument.spreadsheet" }); if(r.bookType == "fods") t.push("<office:document" + i + s + ">\n");
			else t.push("<office:document-content" + i + ">\n");
			n(t);
			t.push("  <office:body>\n");
			t.push("    <office:spreadsheet>\n"); for(var f = 0; f != e.SheetNames.length; ++f) t.push(a(e.Sheets[e.SheetNames[f]], e, f, r));
			t.push("    </office:spreadsheet>\n");
			t.push("  </office:body>\n"); if(r.bookType == "fods") t.push("</office:document>");
			else t.push("</office:document-content>"); return t.join("") } }();

	function Nv(e, r) { if(r.bookType == "fods") return Pv(e, r); var t = new be; var a = ""; var n = []; var i = [];
		a = "mimetype";
		t.file(a, "application/vnd.oasis.opendocument.spreadsheet");
		a = "content.xml";
		t.file(a, Pv(e, r));
		n.push([a, "text/xml"]);
		i.push([a, "ContentFile"]);
		a = "styles.xml";
		t.file(a, Fv(e, r));
		n.push([a, "text/xml"]);
		i.push([a, "StylesFile"]);
		a = "meta.xml";
		t.file(a, Ha());
		n.push([a, "text/xml"]);
		i.push([a, "MetadataFile"]);
		a = "manifest.rdf";
		t.file(a, Ua(i));
		n.push([a, "application/rdf+xml"]);
		a = "META-INF/manifest.xml";
		t.file(a, Na(n)); return t }

	function Lv(e, r) { if(!r) return 0; var t = e.SheetNames.indexOf(r); if(t == -1) throw new Error("Sheet not found: " + r); return t }

	function Mv(e) { return function r(t, a) { var n = Lv(t, a.sheet); return e.from_sheet(t.Sheets[t.SheetNames[n]], a, t) } }
	var Uv = Mv(xv);
	var Hv = Mv({ from_sheet: Bg });
	var Wv = Mv(Qs);
	var Vv = Mv(Js);
	var zv = Mv(ef);
	var Xv = Mv(zf);
	var Gv = Mv({ from_sheet: Tg });
	var jv = Mv(Zs);
	var Kv = Mv(qs);

	function Yv(e) { return function r(t) { for(var a = 0; a != e.length; ++a) { var n = e[a]; if(t[n[0]] === undefined) t[n[0]] = n[1]; if(n[2] === "n") t[n[0]] = Number(t[n[0]]) } } }
	var $v = Yv([
		["cellNF", false],
		["cellHTML", true],
		["cellFormula", true],
		["cellStyles", false],
		["cellText", true],
		["cellDates", false],
		["sheetStubs", false],
		["sheetRows", 0, "n"],
		["bookDeps", false],
		["bookSheets", false],
		["bookProps", false],
		["bookFiles", false],
		["bookVBA", false],
		["password", ""],
		["WTF", false]
	]);
	var Zv = Yv([
		["cellDates", false],
		["bookSST", false],
		["bookType", "xlsx"],
		["compression", false],
		["WTF", false]
	]);

	function Qv(e) { if(xa.WS.indexOf(e) > -1) return "sheet"; if(xa.CS && e == xa.CS) return "chart"; if(xa.DS && e == xa.DS) return "dialog"; if(xa.MS && e == xa.MS) return "macro"; return e && e.length ? e : "sheet" }

	function Jv(e, r) { if(!e) return 0; try { e = r.map(function a(r) { if(!r.id) r.id = r.strRelID; return [r.name, e["!id"][r.id].Target, Qv(e["!id"][r.id].Type)] }) } catch(t) { return null } return !e || e.length === 0 ? null : e }

	function qv(e, r, t, a, n, i, s, f, o, l, c, h) { try { i[a] = Ia(ge(e, t, true), r); var u = ve(e, r); switch(f) {
				case "sheet":
					s[a] = fp(u, r, n, o, i[a], l, c, h); break;
				case "chart":
					var d = op(u, r, n, o, i[a], l, c, h);
					s[a] = d; if(!d || !d["!chart"]) break; var p = Ce(d["!chart"].Target, r); var v = ya(p); var g = Cl(ge(e, p, true), Ia(ge(e, v, true), p)); var m = Ce(g, p); var b = ya(m);
					d = _d(ge(e, m, true), m, o, Ia(ge(e, b, true), m), l, d); break;
				case "macro":
					s[a] = lp(u, r, n, o, i[a], l, c, h); break;
				case "dialog":
					s[a] = cp(u, r, n, o, i[a], l, c, h); break; } } catch(C) { if(o.WTF) throw C } }

	function eg(e) { return e.charAt(0) == "/" ? e.slice(1) : e }

	function rg(e, r) { I(y);
		r = r || {};
		$v(r); if(de(e, "META-INF/manifest.xml")) return Dv(e, r); if(de(e, "objectdata.xml")) return Dv(e, r); if(de(e, "Index/Document.iwa")) throw new Error("Unsupported NUMBERS file"); var t = me(e); var a = Aa(ge(e, "[Content_Types].xml")); var n = false; var i, s; if(a.workbooks.length === 0) { s = "xl/workbook.xml"; if(ve(e, s, true)) a.workbooks.push(s) } if(a.workbooks.length === 0) { s = "xl/workbook.bin"; if(!ve(e, s, true)) throw new Error("Could not find workbook");
			a.workbooks.push(s);
			n = true } if(a.workbooks[0].slice(-3) == "bin") n = true; var f = {}; var o = {}; if(!r.bookSheets && !r.bookProps) { Vh = []; if(a.sst) try { Vh = dp(ve(e, eg(a.sst)), a.sst, r) } catch(l) { if(r.WTF) throw l }
			if(r.cellStyles && a.themes.length) f = up(ge(e, a.themes[0].replace(/^\//, ""), true) || "", a.themes[0], r); if(a.style) o = hp(ve(e, eg(a.style)), a.style, f, r) } a.links.map(function(t) { return gp(ve(e, eg(t)), t, r) }); var c = sp(ve(e, eg(a.workbooks[0])), a.workbooks[0], r); var h = {},
			u = ""; if(a.coreprops.length) { u = ve(e, eg(a.coreprops[0]), true); if(u) h = za(u); if(a.extprops.length !== 0) { u = ve(e, eg(a.extprops[0]), true); if(u) Za(u, h, r) } } var d = {}; if(!r.bookSheets || r.bookProps) { if(a.custprops.length !== 0) { u = ge(e, eg(a.custprops[0]), true); if(u) d = en(u, r) } } var p = {}; if(r.bookSheets || r.bookProps) { if(c.Sheets) i = c.Sheets.map(function x(e) { return e.name });
			else if(h.Worksheets && h.SheetNames.length > 0) i = h.SheetNames; if(r.bookProps) { p.Props = h;
				p.Custprops = d } if(r.bookSheets && typeof i !== "undefined") p.SheetNames = i; if(r.bookSheets ? p.SheetNames : r.bookProps) return p } i = {}; var v = {}; if(r.bookDeps && a.calcchain) v = vp(ve(e, eg(a.calcchain)), a.calcchain, r); var g = 0; var m = {}; var b, C; { var w = c.Sheets;
			h.Worksheets = w.length;
			h.SheetNames = []; for(var E = 0; E != w.length; ++E) { h.SheetNames[E] = w[E].name } } var k = n ? "bin" : "xml"; var S = a.workbooks[0].lastIndexOf("/"); var A = (a.workbooks[0].slice(0, S + 1) + "_rels/" + a.workbooks[0].slice(S + 1) + ".rels").replace(/^\//, ""); if(!de(e, A)) A = "xl/_rels/workbook." + k + ".rels"; var _ = Ia(ge(e, A, true), A); if(_) _ = Jv(_, c.Sheets); var B = ve(e, "xl/worksheets/sheet.xml", true) ? 1 : 0; for(g = 0; g != h.Worksheets; ++g) { var T = "sheet"; if(_ && _[g]) { b = "xl/" + _[g][1].replace(/[\/]?xl\//, ""); if(!de(e, b)) b = _[g][1]; if(!de(e, b)) b = A.replace(/_rels\/.*$/, "") + _[g][1];
				T = _[g][2] } else { b = "xl/worksheets/sheet" + (g + 1 - B) + "." + k;
				b = b.replace(/sheet0\./, "sheet.") } C = b.replace(/^(.*)(\/)([^\/]*)$/, "$1/_rels/$3.rels");
			qv(e, b, C, h.SheetNames[g], g, m, i, T, r, c, f, o) } if(a.comments) kl(e, a.comments, i, m, r);
		p = { Directory: a, Workbook: c, Props: h, Custprops: d, Deps: v, Sheets: i, SheetNames: h.SheetNames, Strings: Vh, Styles: o, Themes: f, SSF: y.get_table() }; if(r.bookFiles) { p.keys = t;
			p.files = e.files } if(r.bookVBA) { if(a.vba.length > 0) p.vbaraw = ve(e, eg(a.vba[0]), true);
			else if(a.defaults && a.defaults.bin === Ol) p.vbaraw = ve(e, "xl/vbaProject.bin", true) } return p }

	function tg(e, r) { var t = r || {}; var a = "/!DataSpaces/Version"; var n = L.find(e, a); if(!n || !n.content) throw new Error("ECMA-376 Encrypted file missing " + a);
		kf(n.content);
		a = "/!DataSpaces/DataSpaceMap";
		n = L.find(e, a); if(!n || !n.content) throw new Error("ECMA-376 Encrypted file missing " + a); var i = Af(n.content); if(i.length !== 1 || i[0].comps.length !== 1 || i[0].comps[0].t !== 0 || i[0].name !== "StrongEncryptionDataSpace" || i[0].comps[0].v !== "EncryptedPackage") throw new Error("ECMA-376 Encrypted file bad " + a);
		a = "/!DataSpaces/DataSpaceInfo/StrongEncryptionDataSpace";
		n = L.find(e, a); if(!n || !n.content) throw new Error("ECMA-376 Encrypted file missing " + a); var s = _f(n.content); if(s.length != 1 || s[0] != "StrongEncryptionTransform") throw new Error("ECMA-376 Encrypted file bad " + a);
		a = "/!DataSpaces/TransformInfo/StrongEncryptionTransform/!Primary";
		n = L.find(e, a); if(!n || !n.content) throw new Error("ECMA-376 Encrypted file missing " + a);
		Tf(n.content);
		a = "/EncryptionInfo";
		n = L.find(e, a); if(!n || !n.content) throw new Error("ECMA-376 Encrypted file missing " + a); var f = If(n.content);
		a = "/EncryptedPackage";
		n = L.find(e, a); if(!n || !n.content) throw new Error("ECMA-376 Encrypted file missing " + a); if(f[0] == 4 && typeof decrypt_agile !== "undefined") return decrypt_agile(f[1], n.content, t.password || "", t); if(f[0] == 2 && typeof decrypt_std76 !== "undefined") return decrypt_std76(f[1], n.content, t.password || "", t); throw new Error("File is password-protected") }

	function ag(e, r) { wl = 1024; if(r.bookType == "ods") return Nv(e, r); if(e && !e.SSF) { e.SSF = y.get_table() } if(e && e.SSF) { I(y);
			y.load_table(e.SSF);
			r.revssf = j(e.SSF);
			r.revssf[e.SSF[65535]] = 0;
			r.ssf = e.SSF } r.rels = {};
		r.wbrels = {};
		r.Strings = [];
		r.Strings.Count = 0;
		r.Strings.Unique = 0; var t = r.bookType == "xlsb" ? "bin" : "xml"; var a = Nl.indexOf(r.bookType) > -1; var n = Sa();
		Zv(r = r || {}); var i = new be; var s = "",
			f = 0;
		r.cellXfs = [];
		Kh(r.cellXfs, {}, { revssf: { General: 0 } }); if(!e.Props) e.Props = {};
		s = "docProps/core.xml";
		i.file(s, ja(e.Props, r));
		n.coreprops.push(s);
		Oa(r.rels, 2, s, xa.CORE_PROPS);
		s = "docProps/app.xml"; if(e.Props && e.Props.SheetNames) {} else if(!e.Workbook || !e.Workbook.Sheets) e.Props.SheetNames = e.SheetNames;
		else { var o = []; for(var l = 0; l < e.SheetNames.length; ++l)
				if((e.Workbook.Sheets[l] || {}).Hidden != 2) o.push(e.SheetNames[l]);
			e.Props.SheetNames = o } e.Props.Worksheets = e.Props.SheetNames.length;
		i.file(s, Ja(e.Props, r));
		n.extprops.push(s);
		Oa(r.rels, 3, s, xa.EXT_PROPS); if(e.Custprops !== e.Props && z(e.Custprops || {}).length > 0) { s = "docProps/custom.xml";
			i.file(s, tn(e.Custprops, r));
			n.custprops.push(s);
			Oa(r.rels, 4, s, xa.CUST_PROPS) } s = "xl/workbook." + t;
		i.file(s, mp(e, s, r));
		n.workbooks.push(s);
		Oa(r.rels, 1, s, xa.WB); for(f = 1; f <= e.SheetNames.length; ++f) { var c = { "!id": {} }; var h = e.Sheets[e.SheetNames[f - 1]]; var u = (h || {})["!type"] || "sheet"; switch(u) {
				case "chart":
					;
				default:
					s = "xl/worksheets/sheet" + f + "." + t;
					i.file(s, bp(f - 1, s, r, e, c));
					n.sheets.push(s);
					Oa(r.wbrels, -1, "worksheets/sheet" + f + "." + t, xa.WS[0]); } if(h) { var d = h["!comments"]; if(d && d.length > 0) { var p = "xl/comments" + f + "." + t;
					i.file(p, kp(d, p, r));
					n.comments.push(p);
					Oa(c, -1, "../comments" + f + "." + t, xa.CMNT) } if(h["!legacy"]) { i.file("xl/drawings/vmlDrawing" + f + ".vml", El(f, h["!comments"])) } delete h["!comments"];
				delete h["!legacy"] } if(c["!id"].rId1) i.file(ya(s), Da(c)) } if(r.Strings != null && r.Strings.length > 0) { s = "xl/sharedStrings." + t;
			i.file(s, Ep(r.Strings, s, r));
			n.strs.push(s);
			Oa(r.wbrels, -1, "sharedStrings." + t, xa.SST) } s = "xl/theme/theme1.xml";
		i.file(s, il(e.Themes, r));
		n.themes.push(s);
		Oa(r.wbrels, -1, "theme/theme1.xml", xa.THEME);
		s = "xl/styles." + t;
		i.file(s, wp(e, s, r));
		n.styles.push(s);
		Oa(r.wbrels, -1, "styles." + t, xa.STY); if(e.vbaraw && a) { s = "xl/vbaProject.bin";
			i.file(s, e.vbaraw);
			n.vba.push(s);
			Oa(r.wbrels, -1, "vbaProject.bin", xa.VBA) } i.file("[Content_Types].xml", Ta(n, r));
		i.file("_rels/.rels", Da(r.rels));
		i.file("xl/_rels/workbook." + t + ".rels", Da(r.wbrels));
		delete r.revssf;
		delete r.ssf; return i }

	function ng(e, r) { var t = ""; switch((r || {}).type || "base64") {
			case "buffer":
				return [e[0], e[1], e[2], e[3]];
			case "base64":
				t = b.decode(e.slice(0, 24)); break;
			case "binary":
				t = e; break;
			case "array":
				return [e[0], e[1], e[2], e[3]];
			default:
				throw new Error("Unrecognized type " + (r && r.type || "undefined")); } return [t.charCodeAt(0), t.charCodeAt(1), t.charCodeAt(2), t.charCodeAt(3)] }

	function ig(e, r) { if(L.find(e, "EncryptedPackage")) return tg(e, r); return iv(e, r) }

	function sg(e, r) { var t, a = e; var n = r || {}; if(!n.type) n.type = C && Buffer.isBuffer(e) ? "buffer" : "base64"; switch(n.type) {
			case "base64":
				t = new be(a, { base64: true }); break;
			case "binary":
				;
			case "array":
				t = new be(a, { base64: false }); break;
			case "buffer":
				t = new be(a); break;
			default:
				throw new Error("Unrecognized type " + n.type); } return rg(t, n) }

	function fg(e, r) { var t = 0;
		e: while(t < e.length) switch(e.charCodeAt(t)) {
			case 10:
				;
			case 13:
				;
			case 32:
				++t; break;
			case 60:
				return Lp(e.slice(t), r);
			default:
				break e; }
		return ef.to_workbook(e, r) }

	function og(e, r) { var t = "",
			a = ng(e, r); switch(r.type) {
			case "base64":
				t = b.decode(e); break;
			case "binary":
				t = e; break;
			case "buffer":
				t = e.toString("binary"); break;
			case "array":
				t = ae(e); break;
			default:
				throw new Error("Unrecognized type " + r.type); } if(a[0] == 239 && a[1] == 187 && a[2] == 191) t = He(t); return fg(t, r) }

	function lg(e, r) { var t = e; if(r.type == "base64") t = b.decode(t);
		t = cptable.utils.decode(1200, t.slice(2), "str");
		r.type = "binary"; return fg(t, r) }

	function cg(e) { return !e.match(/[^\x00-\x7F]/) ? e : We(e) }

	function hg(e, r, t, a) { if(a) { t.type = "string"; return ef.to_workbook(e, t) } return ef.to_workbook(r, t) }

	function ug(e, r) { c(); if(typeof ArrayBuffer !== "undefined" && e instanceof ArrayBuffer) return ug(new Uint8Array(e), r); var t = e,
			a = [0, 0, 0, 0],
			n = false; var i = r || {};
		zh = {}; if(i.dateNF) zh.dateNF = i.dateNF; if(!i.type) i.type = C && Buffer.isBuffer(e) ? "buffer" : "base64"; if(i.type == "file") { i.type = C ? "buffer" : "binary";
			t = V(e) } if(i.type == "string") { n = true;
			i.type = "binary";
			t = cg(e) } if(i.type == "array" && typeof Uint8Array !== "undefined" && e instanceof Uint8Array && typeof ArrayBuffer !== "undefined") { var s = new ArrayBuffer(3),
				f = new Uint8Array(s);
			f.foo = "bar"; if(!f.foo) { i = ne(i);
				i.type = "array"; return ug(_(t), i) } } switch((a = ng(t, i))[0]) {
			case 208:
				return ig(L.read(t, i), i);
			case 9:
				return iv(t, i);
			case 60:
				return Lp(t, i);
			case 73:
				if(a[1] === 68) return rf(t, i); break;
			case 84:
				if(a[1] === 65 && a[2] === 66 && a[3] === 76) return Js.to_workbook(t, i); break;
			case 80:
				return a[1] === 75 && a[2] < 9 && a[3] < 9 ? sg(t, i) : hg(e, t, i, n);
			case 239:
				return a[3] === 60 ? Lp(t, i) : hg(e, t, i, n);
			case 255:
				if(a[1] === 254) { return lg(t, i) } break;
			case 0:
				if(a[1] === 0 && a[2] >= 2 && a[3] === 0) return tf.to_workbook(t, i); break;
			case 3:
				;
			case 131:
				;
			case 139:
				;
			case 140:
				return Zs.to_workbook(t, i);
			case 123:
				if(a[1] === 92 && a[2] === 114 && a[3] === 116) return zf.to_workbook(t, i); break;
			case 10:
				;
			case 13:
				;
			case 32:
				return og(t, i); } if(a[2] <= 12 && a[3] <= 31) return Zs.to_workbook(t, i); return hg(e, t, i, n) }

	function dg(e, r) { var t = r || {};
		t.type = "file"; return ug(e, t) }

	function pg(e, r) { var t = r || {}; var a = ag(e, t); var n = {}; if(t.compression) n.compression = "DEFLATE"; switch(t.type) {
			case "base64":
				n.type = "base64"; break;
			case "binary":
				n.type = "string"; break;
			case "string":
				throw new Error("'string' output type invalid for '" + t.bookType + "' files");
			case "buffer":
				;
			case "file":
				n.type = C ? "nodebuffer" : "string"; break;
			default:
				throw new Error("Unrecognized type " + t.type); } if(t.type === "file") return W(t.file, a.generate(n)); var i = a.generate(n); return t.type == "string" ? He(i) : i }

	function vg(e, r) { var t = r || {}; var a = sv(e, t); switch(t.type) {
			case "base64":
				;
			case "binary":
				break;
			case "buffer":
				;
			case "array":
				t.type = ""; break;
			case "file":
				return W(t.file, L.write(a, { type: C ? "buffer" : "" }));
			case "string":
				throw new Error("'string' output type invalid for '" + t.bookType + "' files");
			default:
				throw new Error("Unrecognized type " + t.type); } return L.write(a, t) }

	function gg(e, r, t) { if(!t) t = ""; var a = t + e; switch(r.type) {
			case "base64":
				return b.encode(We(a));
			case "binary":
				return We(a);
			case "string":
				return e;
			case "file":
				return W(r.file, a, "utf8");
			case "buffer":
				{ if(C) return Buffer.from(a, "utf8");
					else return gg(a, { type: "binary" }).split("").map(function(e) { return e.charCodeAt(0) }) }; } throw new Error("Unrecognized type " + r.type) }

	function mg(e, r) { switch(r.type) {
			case "base64":
				return b.encode(e);
			case "binary":
				return e;
			case "string":
				return e;
			case "file":
				return W(r.file, e, "binary");
			case "buffer":
				{ if(C) return Buffer.from(e, "binary");
					else return e.split("").map(function(e) { return e.charCodeAt(0) }) }; } throw new Error("Unrecognized type " + r.type) }

	function bg(e, r) { switch(r.type) {
			case "string":
				;
			case "base64":
				;
			case "binary":
				var t = ""; for(var a = 0; a < e.length; ++a) t += String.fromCharCode(e[a]); return r.type == "base64" ? b.encode(t) : r.type == "string" ? He(t) : t;
			case "file":
				return W(r.file, e);
			case "buffer":
				return e;
			default:
				throw new Error("Unrecognized type " + r.type); } }

	function Cg(e, r) { zd(e); var t = r || {}; if(t.type == "array") { t.type = "binary"; var a = Cg(e, t);
			t.type = "array"; return k(a) } switch(t.bookType || "xlsb") {
			case "xml":
				;
			case "xlml":
				return gg(Zp(e, t), t);
			case "slk":
				;
			case "sylk":
				return gg(Wv(e, t), t);
			case "htm":
				;
			case "html":
				return gg(Uv(e, t), t);
			case "txt":
				return mg(Gv(e, t), t);
			case "csv":
				return gg(Hv(e, t), t, "\ufeff");
			case "dif":
				return gg(Vv(e, t), t);
			case "dbf":
				return bg(jv(e, t), t);
			case "prn":
				return gg(zv(e, t), t);
			case "rtf":
				return gg(Xv(e, t), t);
			case "eth":
				return gg(Kv(e, t), t);
			case "fods":
				return gg(Nv(e, t), t);
			case "biff2":
				if(!t.biff) t.biff = 2;
			case "biff3":
				if(!t.biff) t.biff = 3;
			case "biff4":
				if(!t.biff) t.biff = 4; return bg(Tv(e, t), t);
			case "biff5":
				if(!t.biff) t.biff = 5;
			case "biff8":
				;
			case "xla":
				;
			case "xls":
				if(!t.biff) t.biff = 8; return vg(e, t);
			case "xlsx":
				;
			case "xlsm":
				;
			case "xlam":
				;
			case "xlsb":
				;
			case "ods":
				return pg(e, t);
			default:
				throw new Error("Unrecognized bookType |" + t.bookType + "|"); } }

	function wg(e) { if(e.bookType) return; var r = { xls: "biff8", htm: "html", slk: "sylk", socialcalc: "eth", Sh33tJS: "WTF" }; var t = e.file.slice(e.file.lastIndexOf(".")).toLowerCase(); if(t.match(/^\.[a-z]+$/)) e.bookType = t.slice(1);
		e.bookType = r[e.bookType] || e.bookType }

	function Eg(e, r, t) { var a = t || {};
		a.type = "file";
		a.file = r;
		wg(a); return Cg(e, a) }

	function kg(e, r, t, a) { var n = t || {};
		n.type = "file";
		n.file = e;
		wg(n);
		n.type = "buffer"; var i = a; if(!(i instanceof Function)) i = t; return M.writeFile(e, Cg(r, n), i) }

	function Sg(e, r) { if(e == null || e["!ref"] == null) return []; var t = { t: "n", v: 0 },
			a = 0,
			n = 1,
			i = [],
			s = true,
			f = 0,
			o = ""; var l = { s: { r: 0, c: 0 }, e: { r: 0, c: 0 } }; var c = r || {}; var h = c.raw; var u = c.defval; var d = c.range != null ? c.range : e["!ref"]; if(c.header === 1) a = 1;
		else if(c.header === "A") a = 2;
		else if(Array.isArray(c.header)) a = 3; switch(typeof d) {
			case "string":
				l = ht(d); break;
			case "number":
				l = ht(e["!ref"]);
				l.s.r = d; break;
			default:
				l = d; } if(a > 0) n = 0; var p = qr(l.s.r); var v = []; var g = []; var m = 0,
			b = 0; var C = Array.isArray(e); var w = l.s.r,
			E = 0,
			k = 0; if(C && !e[w]) e[w] = []; for(E = l.s.c; E <= l.e.c; ++E) { v[E] = at(E);
			t = C ? e[w][E] : e[v[E] + p]; switch(a) {
				case 1:
					i[E] = E - l.s.c; break;
				case 2:
					i[E] = v[E]; break;
				case 3:
					i[E] = c.header[E - l.s.c]; break;
				default:
					if(t == null) t = { w: "__EMPTY", t: "s" };
					o = f = dt(t, null, c);
					b = 0; for(k = 0; k < i.length; ++k)
						if(i[k] == o) o = f + "_" + ++b;
					i[E] = o; } } var S = a === 1 ? [] : {}; for(w = l.s.r + n; w <= l.e.r; ++w) { p = qr(w);
			s = true; if(a === 1) S = [];
			else { S = {}; if(Object.defineProperty) try { Object.defineProperty(S, "__rowNum__", { value: w, enumerable: false }) } catch(A) { S.__rowNum__ = w } else S.__rowNum__ = w } if(!C || e[w])
				for(E = l.s.c; E <= l.e.c; ++E) { t = C ? e[w][E] : e[v[E] + p]; if(t === undefined || t.t === undefined) { if(u === undefined) continue; if(i[E] != null) { S[i[E]] = u } continue } f = t.v; switch(t.t) {
						case "z":
							if(f == null) break; continue;
						case "e":
							f = void 0; break;
						case "s":
							;
						case "d":
							;
						case "b":
							;
						case "n":
							break;
						default:
							throw new Error("unrecognized type " + t.t); } if(i[E] != null) { if(f == null) { if(u !== undefined) S[i[E]] = u;
							else if(h && f === null) S[i[E]] = null;
							else continue } else { S[i[E]] = h ? f : dt(t, f, c) } if(f != null) s = false } }
			if(s === false || (a === 1 ? c.blankrows !== false : !!c.blankrows)) g[m++] = S } g.length = m; return g }
	var Ag = /"/g;

	function _g(e, r, t, a, n, i, s, f) { var o = true; var l = [],
			c = "",
			h = qr(t); for(var u = r.s.c; u <= r.e.c; ++u) { if(!a[u]) continue; var d = f.dense ? (e[t] || [])[u] : e[a[u] + h]; if(d == null) c = "";
			else if(d.v != null) { o = false;
				c = "" + dt(d, null, f); for(var p = 0, v = 0; p !== c.length; ++p)
					if((v = c.charCodeAt(p)) === n || v === i || v === 34) { c = '"' + c.replace(Ag, '""') + '"'; break }
				if(c == "ID") c = '"ID"' } else if(d.f != null && !d.F) { o = false;
				c = "=" + d.f; if(c.indexOf(",") >= 0) c = '"' + c.replace(Ag, '""') + '"' } else c = "";
			l.push(c) } if(f.blankrows === false && o) return null; return l.join(s) }

	function Bg(e, r) { var t = []; var a = r == null ? {} : r; if(e == null || e["!ref"] == null) return ""; var n = ht(e["!ref"]); var i = a.FS !== undefined ? a.FS : ",",
			s = i.charCodeAt(0); var f = a.RS !== undefined ? a.RS : "\n",
			o = f.charCodeAt(0); var l = new RegExp((i == "|" ? "\\|" : i) + "+$"); var c = "",
			h = [];
		a.dense = Array.isArray(e); var u = a.skipHidden && e["!cols"] || []; var d = a.skipHidden && e["!rows"] || []; for(var p = n.s.c; p <= n.e.c; ++p)
			if(!(u[p] || {}).hidden) h[p] = at(p); for(var v = n.s.r; v <= n.e.r; ++v) { if((d[v] || {}).hidden) continue;
			c = _g(e, n, v, h, s, o, i, a); if(c == null) { continue } if(a.strip) c = c.replace(l, "");
			t.push(c + f) } delete a.dense; return t.join("") }

	function Tg(e, r) { if(!r) r = {};
		r.FS = "\t";
		r.RS = "\n"; var t = Bg(e, r); if(typeof cptable == "undefined" || r.type == "string") return t; var a = cptable.utils.encode(1200, t, "str"); return String.fromCharCode(255) + String.fromCharCode(254) + a }

	function xg(e) { var r = "",
			t, a = ""; if(e == null || e["!ref"] == null) return []; var n = ht(e["!ref"]),
			i = "",
			s = [],
			f; var o = []; var l = Array.isArray(e); for(f = n.s.c; f <= n.e.c; ++f) s[f] = at(f); for(var c = n.s.r; c <= n.e.r; ++c) { i = qr(c); for(f = n.s.c; f <= n.e.c; ++f) { r = s[f] + i;
				t = l ? (e[c] || [])[f] : e[r];
				a = ""; if(t === undefined) continue;
				else if(t.F != null) { r = t.F; if(!t.f) continue;
					a = t.f; if(r.indexOf(":") == -1) r = r + ":" + r } if(t.f != null) a = t.f;
				else if(t.t == "z") continue;
				else if(t.t == "n" && t.v != null) a = "" + t.v;
				else if(t.t == "b") a = t.v ? "TRUE" : "FALSE";
				else if(t.w !== undefined) a = "'" + t.w;
				else if(t.v === undefined) continue;
				else if(t.t == "s") a = "'" + t.v;
				else a = "" + t.v;
				o[o.length] = r + "=" + a } } return o }

	function yg(e, r, t) { var a = t || {}; var n = +!a.skipHeader; var i = e || {}; var s = 0,
			f = 0; if(i && a.origin != null) { if(typeof a.origin == "number") s = a.origin;
			else { var o = typeof a.origin == "string" ? ft(a.origin) : a.origin;
				s = o.r;
				f = o.c } } var l; var c = { s: { c: 0, r: 0 }, e: { c: f, r: s + r.length - 1 + n } }; if(i["!ref"]) { var h = ht(i["!ref"]);
			c.e.c = Math.max(c.e.c, h.e.c);
			c.e.r = Math.max(c.e.r, h.e.r); if(s == -1) { s = c.e.r + 1;
				c.e.r = s + r.length - 1 + n } } var u = a.header || [],
			d = 0;
		r.forEach(function(e, r) { z(e).forEach(function(t) { if((d = u.indexOf(t)) == -1) u[d = u.length] = t; var o = e[t]; var c = "z"; var h = ""; if(typeof o == "number") c = "n";
				else if(typeof o == "boolean") c = "b";
				else if(typeof o == "string") c = "s";
				else if(o instanceof Date) { c = "d"; if(!a.cellDates) { c = "n";
						o = Q(o) } h = a.dateNF || y._table[14] } i[ot({ c: f + d, r: s + r + n })] = l = { t: c, v: o }; if(h) l.z = h }) });
		c.e.c = Math.max(c.e.c, f + u.length - 1); var p = qr(s); if(n)
			for(d = 0; d < u.length; ++d) i[at(d + f) + p] = { t: "s", v: u[d] };
		i["!ref"] = ct(c); return i }

	function Ig(e, r) { return yg(null, e, r) }
	var Rg = { encode_col: at, encode_row: qr, encode_cell: ot, encode_range: ct, decode_col: tt, decode_row: Jr, split_cell: st, decode_cell: ft, decode_range: lt, format_cell: dt, get_formulae: xg, make_csv: Bg, make_json: Sg, make_formulae: xg, sheet_add_aoa: vt, sheet_add_json: yg, aoa_to_sheet: gt, json_to_sheet: Ig, table_to_sheet: yv, table_to_book: Iv, sheet_to_csv: Bg, sheet_to_txt: Tg, sheet_to_json: Sg, sheet_to_html: xv.from_sheet, sheet_to_dif: Js.from_sheet, sheet_to_slk: Qs.from_sheet, sheet_to_eth: qs.from_sheet, sheet_to_formulae: xg, sheet_to_row_object_array: Sg };
	(function(e) { e.consts = e.consts || {};

		function r(r) { r.forEach(function(r) { e.consts[r[0]] = r[1] }) }

		function t(e, r, t) { return e[r] != null ? e[r] : e[r] = t }

		function a(e, r, t) { if(typeof r == "string") return e[r] || (e[r] = { t: "z" }); if(typeof r != "number") return a(e, ot(r)); return a(e, ot({ r: r, c: t || 0 })) }

		function n(e, r) { if(typeof r == "number") { if(r >= 0 && e.SheetNames.length > r) return r; throw new Error("Cannot find sheet # " + r) } else if(typeof r == "string") { var t = e.SheetNames.indexOf(r); if(t > -1) return t; throw new Error("Cannot find sheet name |" + r + "|") } else throw new Error("Cannot find sheet |" + r + "|") } e.book_new = function() { return { SheetNames: [], Sheets: {} } };
		e.book_append_sheet = function(e, r, t) { if(!t)
				for(var a = 1; a <= 65535; ++a)
					if(e.SheetNames.indexOf(t = "Sheet" + a) == -1) break; if(!t) throw new Error("Too many worksheets");
			Wd(t); if(e.SheetNames.indexOf(t) >= 0) throw new Error("Worksheet with name |" + t + "| already exists!");
			e.SheetNames.push(t);
			e.Sheets[t] = r };
		e.book_set_sheet_visibility = function(e, r, a) { t(e, "Workbook", {});
			t(e.Workbook, "Sheets", []); var i = n(e, r);
			t(e.Workbook.Sheets, i, {}); switch(a) {
				case 0:
					;
				case 1:
					;
				case 2:
					break;
				default:
					throw new Error("Bad sheet visibility setting " + a); } e.Workbook.Sheets[i].Hidden = a };
		r([
			["SHEET_VISIBLE", 0],
			["SHEET_HIDDEN", 1],
			["SHEET_VERY_HIDDEN", 2]
		]);
		e.cell_set_number_format = function(e, r) { e.z = r; return e };
		e.cell_set_hyperlink = function(e, r, t) { if(!r) { delete e.l } else { e.l = { Target: r }; if(t) e.l.Tooltip = t } return e };
		e.cell_set_internal_link = function(r, t, a) { return e.cell_set_hyperlink(r, "#" + t, a) };
		e.cell_add_comment = function(e, r, t) { if(!e.c) e.c = [];
			e.c.push({ t: r, a: t || "SheetJS" }) };
		e.sheet_set_array_formula = function(e, r, t) { var n = typeof r != "string" ? r : ht(r); var i = typeof r == "string" ? r : ct(r); for(var s = n.s.r; s <= n.e.r; ++s)
				for(var f = n.s.c; f <= n.e.c; ++f) { var o = a(e, s, f);
					o.t = "n";
					o.F = i;
					delete o.v; if(s == n.s.r && f == n.s.c) o.f = t }
			return e }; return e })(Rg);
	if(C && typeof require != "undefined")(function() { var e = {}.Readable; var t = function(r, t) { var a = e(); var n = t == null ? {} : t; if(r == null || r["!ref"] == null) { a.push(null); return a } var i = ht(r["!ref"]); var s = n.FS !== undefined ? n.FS : ",",
				f = s.charCodeAt(0); var o = n.RS !== undefined ? n.RS : "\n",
				l = o.charCodeAt(0); var c = new RegExp((s == "|" ? "\\|" : s) + "+$"); var h = "",
				u = [];
			n.dense = Array.isArray(r); var d = n.skipHidden && r["!cols"] || []; var p = n.skipHidden && r["!rows"] || []; for(var v = i.s.c; v <= i.e.c; ++v)
				if(!(d[v] || {}).hidden) u[v] = at(v); var g = i.s.r; var m = false;
			a._read = function() { if(!m) { m = true; return a.push("\ufeff") } if(g > i.e.r) return a.push(null); while(g <= i.e.r) {++g; if((p[g - 1] || {}).hidden) continue;
					h = _g(r, i, g - 1, u, f, l, s, n); if(h != null) { if(n.strip) h = h.replace(c, "");
						a.push(h + o); break } } }; return a }; var a = function(r, t) { var a = e(); var n = t || {}; var i = n.header != null ? n.header : xv.BEGIN; var s = n.footer != null ? n.footer : xv.END;
			a.push(i); var f = lt(r["!ref"]);
			n.dense = Array.isArray(r);
			a.push(xv._preamble(r, f, n)); var o = f.s.r; var l = false;
			a._read = function() { if(o > f.e.r) { if(!l) { l = true;
						a.push("</table>" + s) } return a.push(null) } while(o <= f.e.r) { a.push(xv._row(r, f, o, n));++o; break } }; return a };
		r.stream = { to_html: a, to_csv: t } })();
	r.parse_xlscfb = iv;
	r.parse_ods = Dv;
	r.parse_fods = Ov;
	r.write_ods = Nv;
	r.parse_zip = rg;
	r.read = ug;
	r.readFile = dg;
	r.readFileSync = dg;
	r.write = Cg;
	r.writeFile = Eg;
	r.writeFileSync = Eg;
	r.writeFileAsync = kg;
	r.utils = Rg;
	r.SSF = y;
	r.CFB = L
})(typeof exports !== "undefined" ? exports : XLSX);
var XLS = XLSX,
	ODS = XLSX;