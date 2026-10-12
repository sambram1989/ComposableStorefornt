import { Injectable } from "@angular/core";

@Injectable({
    providedIn:'root'
})
export class WsUtilService{
  parseKeyValueArray(inputArray: string[], separator: string = ', '): { [key: string]: string | null }[] {
    return inputArray.map(item => {
        // Split by comma only for the main parameters
        const keyValuePairs = item.split(separator).map(pair => {
            // Find the first equal sign
            const equalIndex = pair.indexOf('=');
            if (equalIndex === -1) {
                return { key: pair.trim(), value: null }; // No key-value found
            }
            const key = pair.slice(0, equalIndex).trim();
            const value = pair.slice(equalIndex + 1).trim();

            return {
                key: key,
                value: value === 'null' ? null : value
            };
        });

        // Convert array of key-value pairs to an object
        return Object.fromEntries(keyValuePairs.map(kvp => [kvp.key, kvp.value]));
    });
}

}