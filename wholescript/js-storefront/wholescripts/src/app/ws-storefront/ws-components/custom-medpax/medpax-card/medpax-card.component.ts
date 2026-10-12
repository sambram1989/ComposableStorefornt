import { Component, inject, input, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { Router } from '@angular/router';
@Component({
  selector: 'ws-medpax-card',
  templateUrl: './medpax-card.component.html',
  styleUrl: './medpax-card.component.scss'
})
export class MedpaxCardComponent implements OnInit {
  public location = inject(Location);
  private protocolService  = inject(ProtocolService);
  private router = inject(Router);

  medPaxData = input<any>();
  removalCard = input<boolean>(false);
  isPlUser = input<boolean>(false);
  isPatientUser = input<boolean>(false);
  currentBaseSite = this.protocolService.getBaseUrl();
  isWsProtocol = false;

  ngOnInit(): void {
    this.isWsProtocol = this.router.url === '/wsprotocols/list';
  }
}
