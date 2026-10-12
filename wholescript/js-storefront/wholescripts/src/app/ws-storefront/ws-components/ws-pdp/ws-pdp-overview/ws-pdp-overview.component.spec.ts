import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsPdpOverviewComponent } from './ws-pdp-overview.component';

describe('WsPdpOverviewComponent', () => {
  let component: WsPdpOverviewComponent;
  let fixture: ComponentFixture<WsPdpOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsPdpOverviewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsPdpOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
