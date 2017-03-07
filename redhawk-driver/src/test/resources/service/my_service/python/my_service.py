#!/usr/bin/env python
#
# AUTO-GENERATED
#
# Source: my_service.spd.xml

import sys, signal, copy, os
import logging

from ossie.cf import CF, CF__POA #@UnusedImport
from ossie.service import start_service
from omniORB import CORBA, URI, PortableServer

from bulkio.bulkioInterfaces import BULKIO
from bulkio.bulkioInterfaces import BULKIO__POA

class my_service(BULKIO__POA.dataFloat):

    def __init__(self, name="my_service", execparams={}):
        self.name = name
        self._log = logging.getLogger(self.name)

    def terminateService(self):
        pass

    def pushSRI(self, H):
        # TODO
        pass

    def pushPacket(self, data, T, EOS, streamID):
        # TODO
        pass

    def _get_state(self):
        # TODO
        pass

    def _get_statistics(self):
        # TODO
        pass

    def _get_activeSRIs(self):
        # TODO
        pass


if __name__ == '__main__':
    start_service(my_service, thread_policy=PortableServer.SINGLE_THREAD_MODEL)
