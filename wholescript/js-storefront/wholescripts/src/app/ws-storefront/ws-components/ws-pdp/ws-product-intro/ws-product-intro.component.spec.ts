import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsProductIntroComponent } from './ws-product-intro.component';

describe('WsProductIntroComponent', () => {
  let component: WsProductIntroComponent;
  let fixture: ComponentFixture<WsProductIntroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsProductIntroComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsProductIntroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
