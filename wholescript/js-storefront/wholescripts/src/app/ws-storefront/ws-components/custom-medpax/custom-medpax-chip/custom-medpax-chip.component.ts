import { Component, EventEmitter, Input, Output } from '@angular/core';
@Component({
  selector: 'ws-custom-medpax-chip',
  templateUrl: './custom-medpax-chip.component.html',
  styleUrl: './custom-medpax-chip.component.scss'
})
export class CustomMedpaxChipComponent {
@Input() data!: string; 
@Input() isSelected: boolean = false; 
@Output() toggleChip = new EventEmitter<string>(); 

toggle() {
  this.toggleChip.emit(this.data);
}
}
