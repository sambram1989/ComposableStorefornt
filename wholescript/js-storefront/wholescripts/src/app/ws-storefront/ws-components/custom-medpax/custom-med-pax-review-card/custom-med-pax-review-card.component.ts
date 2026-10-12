import { Component, DestroyRef, effect, inject, input, Input, Pipe, PipeTransform, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormControl } from '@angular/forms';
import { startWith } from 'rxjs';
import { WsCustomMedPaxEvents } from '../medpaxService/medpaxEventservice.service';
import { medPaxPacketEnum } from '../medpaxService/customMedpaxservice.service';
export type images ={
  data:string,
  dataSet:string,
  src:string
}
@Component({
  selector: 'ws-custom-med-pax-review-card',
  templateUrl: './custom-med-pax-review-card.component.html',
  styleUrl: './custom-med-pax-review-card.component.scss'
})
export class CustomMedPaxReviewCardComponent {
  amData = [
    {
      data :'initial',
      dataSet: medPaxPacketEnum[0].am,
      src: '/assets/qa/black-dosing-am.png'
    },
    {
      data:'AM With Food',
      dataSet:medPaxPacketEnum[4].amWithFood,
      src: '/assets/qa/black-amWithFood.png'
    },
    {
      data:'AM Without Food',
      dataSet: medPaxPacketEnum[8].amWithOutFood,
      src: '/assets/qa/black-amWithoutFood.png'
    }
  ];

  pmData = [
    {
      data :'initial',
      dataSet:  medPaxPacketEnum[1].pm,
      src: '/assets/qa/black-dosing-pm.png'
    },
    {
      data:'PM With Food',
      dataSet: medPaxPacketEnum[5].pmWithFood,
      src: '/assets/qa/black-pmWithFood.png',
    },
    {
      data:'PM Without Food',
      dataSet:  medPaxPacketEnum[9].pmWithOutFood,
      src: '/assets/qa/black-pmWithoutFood.png'
    }
  ];

  dailyData = [
    {data :'initial',
      dataSet: medPaxPacketEnum[2].daily,
      src: '/assets/qa/black-dosing-daily.png'
    },
    {data:'Daily With Food',
      dataSet: medPaxPacketEnum[6].dailyWithFood,
      src: '/assets/qa/black-dailyWithFood.png',
    },
    {data:'Daily Without Food',
      dataSet:  medPaxPacketEnum[10].dailyWithOutFood,
      src: '/assets/qa/black-dailyWithoutFood.png'
    }
  ];
  bedTime = [
    {data :'initial',
      dataSet: medPaxPacketEnum[3].bedtime,
      src: '/assets/qa/black-dosing-bedtime.png'
    },
    {data:'BedTime With Food',
      dataSet: medPaxPacketEnum[7].bedtimeWithFood,
      src: '/assets/qa/black-bedtimeWithFood.png',
    },
    {data:'BedTime Without Food',
      dataSet:  medPaxPacketEnum[11].bedtimeWithOutFood,
      src: '/assets/qa/black-bedtimeWithoutFood.png'
    }
  ];
  private destroyRef = inject(DestroyRef);
  private eventService = inject(WsCustomMedPaxEvents);
  medPaxData = input<any>();
  packet = input<any>();
  currentCardIndex = input<number>();
  private _cardName = 'AM';
  @Input()
  set cardName(value: string) {
    this._cardName = value;
    this.updateCurrentData();
  }

  get cardName(): string {
    return this._cardName;
  }
  @Input() controlName: FormControl;

  currentData:any;

  currentSelectedImg = signal<string>('');
  constructor() {
    this.updateCurrentData();
    effect(() => {
      if(this.packet()?.itemTime?.code) {
       this.controlName.setValue(this.packet().itemTime.code);
      }
    });
  }

  private updateCurrentData(): void {
    switch (this._cardName) {
      case 'AM':
        this.currentData = this.amData;
        break;
      case 'PM':
        this.currentData = this.pmData;
        break;
      case 'Daily':
        this.currentData = this.dailyData;
        break;
        case 'Bedtime':
          this.currentData = this.bedTime;
          break;
      default:
        this.currentData = this.amData;
    }
  }

  ngAfterViewInit() {
    this.controlName.valueChanges.pipe(takeUntilDestroyed(this.destroyRef), startWith('initial')).subscribe((value: string) => {
      let currentDataLocal = this.currentData.find((item: any) => {
        if ((value === 'initial' && value == item?.data) || (value !== 'initial' && value == item?.dataSet)) {
          return item?.src;
        }
      });
      this.currentSelectedImg.set(currentDataLocal?.src);
    });
  }

  onLabelClick(value: string): void {
    this.controlName.setValue(value);
  }

  openEditName() {
    this.eventService.emitEditNameClick(this.currentData);
  }
}

@Pipe({
  name: 'getData'
})
export class GetDataPipe implements PipeTransform {
  transform(packetCode: string, packet: any): any {
    let UiData;
    const match = packet.find((item: any) => item.dataSet === packetCode);
    if (match) {UiData= match.data};
    if(UiData == 'initial') {
      const lowerCode = packetCode.toLowerCase();
      UiData= lowerCode.includes('am') ? 'AM' :
             lowerCode.includes('pm') ? 'PM' :
             lowerCode.includes('daily') ? 'Daily' :
             lowerCode.includes('bedtime') ? 'BedTime' : null;
    }
    return UiData;
  }
  
}

@Pipe({
  name: 'packetTitle'
})
export class GetTitleofPacket implements PipeTransform {
  transform(packetCode: string): any {
    const lowerCode = packetCode.toLowerCase();
    return lowerCode.includes('am') ? 'AM' :
           lowerCode.includes('pm') ? 'PM' :
           lowerCode.includes('daily') ? 'Daily' :
           lowerCode.includes('bedtime') ? 'BedTime' : null;
  }
}

