import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsSpecialOffersComponent } from './ws-special-offers.component';

describe('WsSpecialOffersComponent', () => {
  let component: WsSpecialOffersComponent;
  let fixture: ComponentFixture<WsSpecialOffersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsSpecialOffersComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsSpecialOffersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
