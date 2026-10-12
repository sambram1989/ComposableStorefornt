import { AfterViewInit, ChangeDetectorRef, Component, inject } from '@angular/core';
import { getUserData } from '../../../ws-services/state-services/currentuser.service';
import { WsPage } from '../../../ws-services/ws-page.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'ws-my-account-dashboard',
  templateUrl: './ws-my-account-dashboard.component.html',
  styleUrl: './ws-my-account-dashboard.component.scss',
  providers: [DatePipe]
})
export class WsMyAccountDashboardComponent implements AfterViewInit{ 
 currentUserData: any = inject(getUserData);
 myAccountService = inject(WsPage);
 sharedRewards: any;
 nextLevel: any;
 today = new Date();
 quarters = ['Jan - Mar', 'Apr - Jun', 'Jul - Sep', 'Oct - Dec'];
 currentQuarter: any;

 constructor(private cdRef: ChangeDetectorRef, private datePipe: DatePipe) {
  this.getUserSharedRewards()
 }

 ngAfterViewInit(): void {
  if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Share Rewards';
 }
  getUserSharedRewards() {
    const formattedDate = this.datePipe.transform(this.today, 'yyyy-MM-dd');
    this.myAccountService.getSharedRewards(formattedDate).subscribe((res) => {
      this.sharedRewards = res;
      let level = this.sharedRewards?.rewardsLevel?.item.findIndex((i: any) => i.konda === this.sharedRewards?.shareReward?.curSrewards);
      this.nextLevel = level === 3 ? this.sharedRewards?.rewardsLevel?.item[level] : this.sharedRewards?.rewardsLevel?.item[level + 1];
      this.getQuarter();
      this.cdRef.detectChanges();
    })
  }
  loadRewardLevel(level: string) : string {
    let reward = '';
    switch (level) {
      case '01':
      reward = 'Standard';
      break;
      case '02':
      reward = 'Silver';
      break;
      case '03':
      reward = 'Gold';
      break;
      case '04':
      reward = 'Platinum';
      break;
      default:
      reward = '';
      break;
    }
    return reward;
  }
  getPriceDifference(rewards:any, nextRewards: any ): number {
    return nextRewards - rewards >=0 ? nextRewards - rewards : 0;
  }
  getQuarter() {
    const month = this.today.getMonth();
    const index = Math.floor(month / 3);
    this.currentQuarter = { quarter: `Q${index + 1}`, band: this.quarters[index] };
  }
}
