import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProtocolDetailsPageComponent } from './protocol-details-page.component';

describe('ProtocolDetailsPageComponent', () => {
  let component: ProtocolDetailsPageComponent;
  let fixture: ComponentFixture<ProtocolDetailsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProtocolDetailsPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProtocolDetailsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
