import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateNewProtocolComponent } from './create-new-protocol.component';

describe('CreateNewProtocolComponent', () => {
  let component: CreateNewProtocolComponent;
  let fixture: ComponentFixture<CreateNewProtocolComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreateNewProtocolComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateNewProtocolComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
