import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'ws-recommendation-review-notes',
  templateUrl: './ws-recommendation-review-notes.component.html',
  styleUrl: './ws-recommendation-review-notes.component.scss'
})
export class WsRecommendationReviewNotesComponent {
  @Input() notes: string = '';
  @Output() notesChange = new EventEmitter<string>();

  updateNotes(): void {
    this.notesChange.emit(this.notes);
  }
}
