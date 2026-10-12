import { Component, input, Input, OnInit, SimpleChanges } from '@angular/core';

@Component({
  selector: 'ws-webinar-carts',
  templateUrl: './webinar-carts.component.html',
  styleUrl: './webinar-carts.component.scss'
})
export class WebinarCartsComponent implements OnInit {
 product = input<any>();
 presenterText: string = '';

  ngOnInit(): void {
    if (Array.isArray(this.product().presenter) && this.product().presenter.length > 0) {
      if (typeof this.product().presenter[0] === 'string') {
        this.presenterText = this.product().presenter.join(', ');
      } else if (typeof this.product().presenter[0] === 'object') {
        this.presenterText = this.product().presenter
          .map((item:any) => {
            const name: string = item.name;
            const designation: string | undefined = Object.keys(item).find(
              (key: string) =>
                key !== 'name' &&
                key !== 'code' &&
                key !== 'image' &&
                key !== 'presentPageUrl' &&
                item[key] === null
            );
            return `${name}${designation ? `, ${designation}` : ''}`;
          })
          .join(', ');
      }
    }
  }

}
