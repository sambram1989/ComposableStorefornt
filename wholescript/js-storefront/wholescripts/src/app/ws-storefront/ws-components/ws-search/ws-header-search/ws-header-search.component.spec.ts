import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsHeaderSearchComponent } from './ws-header-search.component';

describe('WsHeaderSearchComponent', () => {
  let component: WsHeaderSearchComponent;
  let fixture: ComponentFixture<WsHeaderSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsHeaderSearchComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsHeaderSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
