import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProtocolProductComponent } from './protocol-product.component';

describe('ProtocolProductComponent', () => {
  let component: ProtocolProductComponent;
  let fixture: ComponentFixture<ProtocolProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProtocolProductComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProtocolProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
