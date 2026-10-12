import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProtocolShareSuccessComponent } from './protocol-share-success.component';

describe('ProtocolShareSuccessComponent', () => {
  let component: ProtocolShareSuccessComponent;
  let fixture: ComponentFixture<ProtocolShareSuccessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProtocolShareSuccessComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProtocolShareSuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
