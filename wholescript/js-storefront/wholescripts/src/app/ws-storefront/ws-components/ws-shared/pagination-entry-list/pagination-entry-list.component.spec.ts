import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginationEntryListComponent } from './pagination-entry-list.component';

describe('PaginationEntryListComponent', () => {
  let component: PaginationEntryListComponent;
  let fixture: ComponentFixture<PaginationEntryListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PaginationEntryListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PaginationEntryListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
