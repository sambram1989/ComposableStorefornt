import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopPractitionerProtocolComponent } from './shop-practitioner-protocol.component';

describe('ShopPractitionerProtocolComponent', () => {
  let component: ShopPractitionerProtocolComponent;
  let fixture: ComponentFixture<ShopPractitionerProtocolComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShopPractitionerProtocolComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ShopPractitionerProtocolComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
