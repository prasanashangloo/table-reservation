# Task 1 - Problem Definition and Scope

> Owner / Author: **Prasana Shangloo** (23102A0076) | Copyright (c) 2026

## Problem statement
Small and mid-sized restaurants still take most reservations by phone or walk-in. Staff are interrupted during service, bookings are written on paper, double-bookings happen, and no-shows or cancellations leave tables empty because nobody can quickly re-sell the slot. Diners cannot see availability and have to call during business hours.

**The Automated Restaurant Table Reservation Platform** lets a diner register, see which tables and time slots are free for a date and party size, request a booking, receive confirmation, cancel, and track the booking status - without a phone call.

## Target users
| User | Need |
|---|---|
| Diner | See availability 24x7, book and cancel quickly, know the status of the booking |
| Restaurant host/manager (MVP: any logged-in owner of the booking confirms it) | Avoid double-booking, see which bookings are pending/confirmed |

## Existing pain points
1. Phone-only booking limits reservations to opening hours.
2. Manual diaries cause double bookings and lost bookings.
3. No visible status - diners do not know whether the booking is confirmed.
4. Cancelled slots are not released quickly.

## Stakeholders
| Stakeholder | Interest |
|---|---|
| Diners | Convenience, certainty |
| Restaurant owner / manager | Fewer no-shows, higher table utilisation |
| Front-of-house staff | Fewer phone interruptions |
| Development team (students) | Deliver an MVP and a working DevOps pipeline |
| Course instructor / evaluator | Evidence of each deliverable, viva |
| Operations / DevOps role | Repeatable build, test, deploy and rollback |

## Objectives and measurable success criteria
| # | Objective | Success criterion |
|---|---|---|
| O1 | Eliminate double bookings | 0 successful bookings for the same table+date+slot (API test proves 409) |
| O2 | Self-service booking | A new user completes register -> book in under 2 minutes (Selenium journey J4) |
| O3 | Status transparency | Status shown as PENDING / CONFIRMED / CANCELLED on every state change |
| O4 | Automated quality gate | 100% of commits run unit + 5 Selenium journeys; a failed test blocks deployment |
| O5 | Repeatable deployment | Commit-to-running-container with no manual step; rerunning provisioning changes nothing (idempotent) |
| O6 | Recoverability | Failed health check rolls back to the previous release automatically |

## Constraints
- Academic project: 15 tasks, small team, limited time - scope frozen to the MVP below.
- Free/open tooling only: Git/GitHub, Jenkins, Maven, Selenium, Docker, Ansible.
- Single restaurant, single location, fixed 6 tables and 7 time slots.
- No payments, no SMS/e-mail, no admin UI in the MVP.
- Must run on a laptop/VM (Java 17, Docker).

## Approved MVP scope (frozen) - 15 tasks
**In scope:** user registration and login, availability/slot view, booking request, confirmation, cancellation, status tracking, health endpoint, CI/CD with Jenkins, Selenium gate, Docker deployment, Ansible provisioning with rollback.

**Out of scope (future):** payments, e-mail/SMS notifications, restaurant admin dashboard, multi-restaurant support, waiting list, OAuth login, persistent sessions.

The 15 tasks are the 15 course tasks listed in the README table; scope is considered frozen once Task 1 is approved by the instructor.
