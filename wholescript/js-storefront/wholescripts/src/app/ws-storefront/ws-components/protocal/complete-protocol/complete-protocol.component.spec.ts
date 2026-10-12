import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompleteProtocolComponent } from './complete-protocol.component';

describe('CompleteProtocolComponent', () => {
  let component: CompleteProtocolComponent;
  let fixture: ComponentFixture<CompleteProtocolComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CompleteProtocolComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CompleteProtocolComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
