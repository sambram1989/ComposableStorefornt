import { Injectable } from "@angular/core";
import { toggleConfig, valueConfig } from "./toggle-switch.component";

@Injectable()
export class WsToggleConfig {
  value?: any;
  name?: string;
  disabled?: boolean;
  height?: number;
  width?: number;
  margin?: number;
  fontSize?: number;
  speed?: number;
  color?: string | toggleConfig;
  switchColor?: string | toggleConfig;
  labels?: boolean | toggleConfig;
  values?: valueConfig;
  fontColor?: string | toggleConfig;
  textAlign?: string | toggleConfig;
}