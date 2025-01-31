import {
  timeUnits
} from "./chunk-LM6NAUI5.js";
import {
  padStart
} from "./chunk-5KGWHAJY.js";
import {
  NgModule,
  Pipe,
  setClassMetadata,
  ɵɵdefineInjector,
  ɵɵdefineNgModule,
  ɵɵdefinePipe
} from "./chunk-AJ7NFLUQ.js";

// node_modules/ng-zorro-antd/fesm2022/ng-zorro-antd-core-pipe.mjs
var NzTimeRangePipe = class _NzTimeRangePipe {
  transform(value, format = "HH:mm:ss") {
    let duration = Number(value || 0);
    return timeUnits.reduce((current, [name, unit]) => {
      if (current.indexOf(name) !== -1) {
        const v = Math.floor(duration / unit);
        duration -= v * unit;
        return current.replace(new RegExp(`${name}+`, "g"), (match) => padStart(v.toString(), match.length, "0"));
      }
      return current;
    }, format);
  }
  static {
    this.ɵfac = function NzTimeRangePipe_Factory(__ngFactoryType__) {
      return new (__ngFactoryType__ || _NzTimeRangePipe)();
    };
  }
  static {
    this.ɵpipe = ɵɵdefinePipe({
      name: "nzTimeRange",
      type: _NzTimeRangePipe,
      pure: true
    });
  }
};
(() => {
  (typeof ngDevMode === "undefined" || ngDevMode) && setClassMetadata(NzTimeRangePipe, [{
    type: Pipe,
    args: [{
      name: "nzTimeRange",
      pure: true
    }]
  }], null, null);
})();
var NzPipesModule = class _NzPipesModule {
  static {
    this.ɵfac = function NzPipesModule_Factory(__ngFactoryType__) {
      return new (__ngFactoryType__ || _NzPipesModule)();
    };
  }
  static {
    this.ɵmod = ɵɵdefineNgModule({
      type: _NzPipesModule,
      imports: [NzTimeRangePipe],
      exports: [NzTimeRangePipe]
    });
  }
  static {
    this.ɵinj = ɵɵdefineInjector({});
  }
};
(() => {
  (typeof ngDevMode === "undefined" || ngDevMode) && setClassMetadata(NzPipesModule, [{
    type: NgModule,
    args: [{
      imports: [NzTimeRangePipe],
      exports: [NzTimeRangePipe]
    }]
  }], null, null);
})();

export {
  NzTimeRangePipe,
  NzPipesModule
};
//# sourceMappingURL=chunk-GOYI5BTK.js.map
