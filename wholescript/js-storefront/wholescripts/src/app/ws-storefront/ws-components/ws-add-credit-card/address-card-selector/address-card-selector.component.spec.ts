import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddressCardSelectorComponent } from './address-card-selector.component';

describe('AddressCardSelectorComponent', () => {
  let component: AddressCardSelectorComponent;
  let fixture: ComponentFixture<AddressCardSelectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddressCardSelectorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddressCardSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
