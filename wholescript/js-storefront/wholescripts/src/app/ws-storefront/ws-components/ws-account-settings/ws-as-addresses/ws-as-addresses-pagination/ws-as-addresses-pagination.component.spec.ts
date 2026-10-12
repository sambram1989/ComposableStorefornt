import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAsAddressesPaginationComponent } from './ws-as-addresses-pagination.component';

describe('WsAsAddressesPaginationComponent', () => {
  let component: WsAsAddressesPaginationComponent;
  let fixture: ComponentFixture<WsAsAddressesPaginationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAsAddressesPaginationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAsAddressesPaginationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
