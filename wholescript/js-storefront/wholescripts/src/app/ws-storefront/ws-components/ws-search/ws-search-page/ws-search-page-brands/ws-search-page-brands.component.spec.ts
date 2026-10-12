import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsSearchPageBrandsComponent } from './ws-search-page-brands.component';

describe('WsSearchPageBrandsComponent', () => {
  let component: WsSearchPageBrandsComponent;
  let fixture: ComponentFixture<WsSearchPageBrandsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsSearchPageBrandsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsSearchPageBrandsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
