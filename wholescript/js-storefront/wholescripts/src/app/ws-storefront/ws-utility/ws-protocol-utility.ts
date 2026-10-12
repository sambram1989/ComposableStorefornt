export class WsProtocolUtility {
    static getDoseItem(doseItem: any) {
        let index = 0
        for(let dose of doseItem.doseTimeItems) {
            const id = dose.id.split("-");
            doseItem.doseTimeItems[index].id = id[0]+'-'+(Number(id[1]) + 1 );
            index ++;
        }
        return doseItem;
    }
}