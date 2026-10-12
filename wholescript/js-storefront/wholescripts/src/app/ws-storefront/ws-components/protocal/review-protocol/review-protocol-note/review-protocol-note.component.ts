import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'ws-review-protocol-note',
  templateUrl: './review-protocol-note.component.html',
  styleUrl: './review-protocol-note.component.scss'
})
export class ReviewProtocolNoteComponent {
  @Input() notes: string = '';
  @Output() notesChange = new EventEmitter<string>();

  updateNotes(): void {
    this.notesChange.emit(this.notes);
  }
}
