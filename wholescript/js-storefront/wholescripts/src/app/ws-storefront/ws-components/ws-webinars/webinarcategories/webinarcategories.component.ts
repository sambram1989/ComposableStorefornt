import { Component, input, Signal } from '@angular/core';

@Component({
  selector: 'ws-webinarcategories',
  templateUrl: './webinarcategories.component.html',
  styleUrl: './webinarcategories.component.scss'
})
export class WebinarcategoriesComponent {
categoryName = input<string>('');
categoryUrl = input<string>('');
}
