/*-
 * #%L
 * FOKProjects Common Internet
 * %%
 * Copyright (C) 2016 - 2021 Frederik Kammel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package com.github.vatbub.common.internet

enum class HttpStatusCode(val integerValue: Int, val stringRepresentation: String) {
    Continue(100, "Continue"),
    SwitchingProtocols(101, "Switching Protocols"),
    Processing(102, "Processing"),
    OK(200, "OK"),
    Created(201, "Created"),
    Accepted(202, "Accepted"),
    NonAuthoritativeInformation(203, "Non-Authoritative Information"),
    NoContent(204, "No Content"),
    ResetContent(205, "Reset Content"),
    PartialContent(206, "Partial Content"),
    MultiStatus(207, "Multi-Status"),
    AlreadyReported(208, "Already Reported"),
    IMUsed(226, "IM Used"),
    MultipleChoices(300, "Multiple Choices"),
    MovedPermanently(301, "Moved Permanently"),
    MovedTemporarily(302, "Moved Temporarily"),
    SeeOther(303, "See Other"),
    NotModified(304, "Not Modified"),
    UseProxy(305, "Use Proxy"),
    TemporaryRedirect(307, "Temporary Redirect"),
    PermanentRedirect(308, "Permanent Redirect"),
    BadRequest(400, "Bad Request"),
    Unauthorized(401, "Unauthorized"),
    PaymentRequired(402, "Payment Required"),
    Forbidden(403, "Forbidden"),
    NotFound(404, "Not Found"),
    MethodNotAllowed(405, "Method Not Allowed"),
    NotAcceptable(406, "Not Acceptable"),
    ProxyAuthenticationRequired(407, "Proxy Authentication Required"),
    RequestTimeout(408, "Request Time-out"),
    Conflict(409, "Conflict"),
    Gone(410, "Gone"),
    LengthRequired(411, "Length required"),
    PreconditionFailed(412, "Precondition failed"),
    RequestEntityTooLarge(413, "Request Entity Too Large"),
    RequestURLTooLong(414, "Request-URL Too Long"),
    UnsupportedMediaType(415, "UnsupportedMedia Type"),
    RequestedRangeNotSatisfiable(416, "Requested range not satisfiable"),
    ExpectationFailed(417, "Expectation failed"),
    ImATeapot(418, "I'm a teapot"),
    PolicyNotFulfilled(420, "Policy Not Fulfilled"),
    MisdirectedRequest(421, "Misdirected Request"),
    UnprocessableEntity(422, "Unprocessable Entity"),
    Locked(423, "Locked"),
    FailedDependency(424, "Failed Dependency"),
    UnorderedCollection(425, "Unordered Collection"),
    UpgradeRequired(426, "Upgrade Required"),
    PreconditionRequired(428, "Precondition Required"),
    TooManyRequests(429, "Too Many Requests"),
    RequestHeaderFieldsTooLarge(431, "Request Header Fields Too Large"),
    UnavailableForLegalReasons(451, "Unavailable For Legal Reasons"),
    InternalServerError(500, "Internal Server Error"),
    NotImplemented(501, "Not Implemented"),
    BadGateway(502, "Bad Gateway"),
    ServiceUnavailable(503, "Service Unavailable"),
    GatewayTimeout(504, "Gateway Time-out"),
    HTTPVersionNotSupported(505, "HTTP Version not supported"),
    VariantAlsoNegotiates(506, "Variant Also Negotiates"),
    InsufficientStorage(507, "Insufficient Storage"),
    LoopDetected(508, "Loop Detected"),
    BandwidthLimitExceeded(509, "Bandwidth Limit Exceeded"),
    NotExtended(510, "Not Extended"),
    NetworkAuthenticationRequired(511, "Network Authentication Required");

    fun findByIntegerValue(integerValue: Int) = values().first { it.integerValue == integerValue }
}
