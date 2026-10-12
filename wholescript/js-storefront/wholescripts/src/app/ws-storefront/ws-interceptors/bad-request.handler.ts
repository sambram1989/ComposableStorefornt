/*
 * SPDX-FileCopyrightText: 2024 SAP Spartacus team <spartacus-team@sap.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import { HttpErrorResponse, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpErrorHandler, HttpResponseStatus, GlobalMessageType, ErrorModel, Priority } from '@spartacus/core';

const OAUTH_ENDPOINT = '/authorizationserver/oauth/token';

@Injectable({
  providedIn: 'root',
})
export class CustomBadRequestHandler extends HttpErrorHandler {
  override responseStatus = HttpResponseStatus.BAD_REQUEST;

  handleError(request: HttpRequest<any>, response: HttpErrorResponse): void {
    this.handleBadPassword(request, response);
    this.handleBadLoginResponse(request, response);
    this.handleValidationError(request, response);
    this.handleGuestDuplicateEmail(request, response);
    this.handleUnknownIdentifierError(request, response);
  }

  protected handleBadPassword(
    request: HttpRequest<any>,
    response: HttpErrorResponse
  ): void {
    if (
      response.url?.includes(OAUTH_ENDPOINT) &&
      response.error?.error === 'invalid_grant' &&
      request.body?.get('grant_type') === 'password'
    ) {
      this.globalMessageService.add(
        {
          key: this.getErrorTranslationKey(response.error?.error_description),
          params: {
            errorMessage:
              response.error.error_description || response.message || '',
          },
        },
        GlobalMessageType.MSG_TYPE_ERROR
      );
      this.globalMessageService.remove(GlobalMessageType.MSG_TYPE_CONFIRMATION);
    }
  }

  protected handleBadLoginResponse(
    _request: HttpRequest<any>,
    response: HttpErrorResponse
  ) {
    this.getErrors(response)
      .filter((error) => error.type === 'PasswordMismatchError')
      .forEach(() => {
        // Updating email and changing password share same http error occurence.
        // Determine the context of global error message based on request url
        const url = new URL(_request.url);
        const key = url.pathname.endsWith('/password')
          ? 'httpHandlers.badRequestOldPasswordIncorrect'
          : 'httpHandlers.validationErrors.invalid.password';

        this.globalMessageService.add(
          { key },
          GlobalMessageType.MSG_TYPE_ERROR
        );
      });
  }

  protected handleValidationError(
    _request: HttpRequest<any>,
    response: HttpErrorResponse
  ): void {
    this.getErrors(response)
      .filter((e) => e.type === 'ValidationError')
      .forEach((error) => {
        console.error('error', error);
      });
  }

  protected handleGuestDuplicateEmail(
    _request: HttpRequest<any>,
    response: HttpErrorResponse
  ): void {
    this.getErrors(response)
      .filter((e) => e.type === 'DuplicateUidError')
      .forEach((error) => {
        this.globalMessageService.add(
          {
            key: 'httpHandlers.badRequestGuestDuplicateEmail',
            params: {
              errorMessage: error.message || '',
            },
          },
          GlobalMessageType.MSG_TYPE_ERROR
        );
      });
  }

  protected handleUnknownIdentifierError(
    _request: HttpRequest<any>,
    response: HttpErrorResponse
  ): void {
    this.getErrors(response)
      .filter((e) => e.type === 'UnknownIdentifierError')
      .forEach((error) => {
        console.error('error', error);
      });
  }

  protected getErrors(response: HttpErrorResponse): ErrorModel[] {
    return (response.error?.errors || []).filter(
      (error: ErrorModel) => error.type !== 'JaloObjectNoLongerValidError'
    );
  }

  getPriority(): Priority {
    return Priority.LOW;
  }
}
