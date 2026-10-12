import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProtocolShareListComponent } from './protocol-share-list.component';

describe('ProtocolShareListComponent', () => {
  let component: ProtocolShareListComponent;
  let fixture: ComponentFixture<ProtocolShareListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProtocolShareListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProtocolShareListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
